package com.hua.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.mapper.ArticleMapper;
import com.hua.mapper.CommentMapper;
import com.hua.mapper.UserMapper;
import com.hua.pojo.dto.EmailDTO;
import com.hua.pojo.dto.ReplyCountDTO;
import com.hua.pojo.entity.Comment;
import com.hua.pojo.vo.*;
import com.hua.pojo.vo.param.AuditParam;
import com.hua.pojo.vo.param.CommentParam;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.BlogInfoService;
import com.hua.service.CommentService;
import com.hua.util.HtmlUtils;
import com.hua.util.PageUtils;
import com.hua.util.RedisUtils;
import com.hua.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hua.common.constant.CommonConst.*;
import static com.hua.common.constant.MqPrefixConst.EMAIL_EXCHANGE;
import static com.hua.common.constant.RedisPrefixConst.COMMENT_LIKE_COUNT;
import static com.hua.common.constant.RedisPrefixConst.COMMENT_USER_LIKE;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 20:58
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogInfoService blogInfoService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${website.url}")
    private String websiteUrl;

    @Override
    public PageResult<CommentVO> listComment(Integer articleId) {
        // ?????????????????????
        Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                        .eq(Objects.nonNull(articleId), Comment::getArticleId, articleId)
                        .isNull(Objects.isNull(articleId), Comment::getArticleId)
                        .isNull(Comment::getParentId)
                        .eq(Comment::getIsAudit, TRUE));
        if (commentCount == 0) {
            return new PageResult<>();
        }
        // ????????????????????????
        List<CommentVO> commentVOList = commentMapper.listComment(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), articleId);
        if (CollectionUtils.isEmpty(commentVOList)) {
            return new PageResult<>();
        }
        // ??????redis?????????????????????
        Map<Object, Object> likeCountMap = redisUtils.hmGet(COMMENT_LIKE_COUNT);
        // ????????????id??????
        List<Integer> commentIdList = commentVOList.stream()
                .map(CommentVO::getId)
                .collect(Collectors.toList());
        // ????????????id????????????????????????
        List<ReplyVO> replyVOList = commentMapper.listReply(commentIdList);
        // ?????????????????????
        replyVOList.forEach(replyVO ->
                replyVO.setLikeCount((Integer) likeCountMap.get(String.valueOf(replyVO.getId()))));
        // ????????????id??????????????????
        Map<Integer, List<ReplyVO>> replyMap =
                replyVOList.stream().collect(Collectors.groupingBy(ReplyVO::getParentId));
        // ????????????id???????????????
        Map<Integer, Integer> replyCountMap =
                commentMapper.listReplyCountByCommentId(commentIdList)
                        .stream().collect(Collectors
                        .toMap(ReplyCountDTO::getCommentId, ReplyCountDTO::getReplyCount));
        // ??????????????????
        commentVOList.forEach(commentVO -> {
            commentVO.setLikeCount((Integer) likeCountMap.get(String.valueOf(commentVO.getId())));
            commentVO.setReplyVOList(replyMap.get(commentVO.getId()));
            commentVO.setReplyCount(replyCountMap.get(commentVO.getId()));
        });
        return new PageResult<>(commentVOList, commentCount);
    }

    @Override
    public List<ReplyVO> listReplyByCommentId(Integer commentId) {
        // ????????????????????????
        List<ReplyVO> replyVOList = commentMapper.listReplyByCommentId(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), commentId);
        // ??????redis?????????????????????
        Map<Object, Object> likeCountMap = redisUtils.hmGet(COMMENT_LIKE_COUNT);
        // ??????????????????
        replyVOList.forEach(replyVO ->
                replyVO.setLikeCount((Integer) likeCountMap.get(String.valueOf(replyVO.getId()))));
        return replyVOList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertComment(CommentParam commentParam) {
        // ????????????????????????
        WebsiteConfigVO websiteConfig = blogInfoService.getWebsiteConfig();
        Integer isCommentAudit = websiteConfig.getIsCommentAudit();
        // ????????????
        commentParam.setCommentContent(HtmlUtils.deleteTag(commentParam.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(UserUtils.getLoginUser().getId())
                .replyId(commentParam.getReplyId())
                .articleId(commentParam.getArticleId())
                .commentContent(commentParam.getCommentContent())
                .parentId(commentParam.getParentId())
                .isAudit(isCommentAudit == TRUE ? FALSE : TRUE)
                .build();
        commentMapper.insert(comment);
        // ?????????????????????????????????????????????
        if (websiteConfig.getIsEmailNotice().equals(TRUE)) {
            notice(comment);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void likeComment(Integer commentId) {
        // ?????????????????????
        String commentLikeKey = COMMENT_USER_LIKE + UserUtils.getLoginUser().getId();
        if (redisUtils.sHasKey(commentLikeKey, commentId)) {
            // ????????????????????????id
            redisUtils.sRem(commentLikeKey, commentId);
            // ??????????????? - 1
            redisUtils.hDecr(COMMENT_LIKE_COUNT, String.valueOf(commentId), 1L);
        } else {
            // ????????????????????????id
            redisUtils.sSet(commentLikeKey, commentId);
            // ??????????????? + 1
            redisUtils.hIncr(COMMENT_LIKE_COUNT, String.valueOf(commentId), 1L);
        }
    }

    @Override
    public PageResult<CommentBackVO> listBackComment(QueryParam queryParam) {
        // ?????????????????????
        Integer count = commentMapper.countComment(queryParam);
        if (count == 0) {
            return new PageResult<>();
        }
        // ????????????????????????
        List<CommentBackVO> commentBackVOList =
                commentMapper.listBackComment(PageUtils.getLimitCurrent(), PageUtils.getSize(),
                        queryParam);
        return new PageResult<>(commentBackVOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCommentsAudit(AuditParam auditParam) {
        // ??????????????????
        List<Comment> commentList = auditParam.getIdList().stream().map(id -> Comment.builder()
                .id(id)
                .isAudit(auditParam.getIsAudit())
                .build())
                .collect(Collectors.toList());
        this.updateBatchById(commentList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteComment(List<Integer> commentIdList) {
        // ??????????????????
        commentMapper.deleteBatchIds(commentIdList);
    }

    /**
     * ??????????????????
     * @param comment ????????????
     */
    @Async
    public void notice(Comment comment) {
        // ????????????????????????
        Integer authorId = articleMapper.selectById(comment.getArticleId()).getAuthorId();
        Integer userId = Optional.ofNullable(comment.getReplyId()).orElse(authorId);
        String email = userMapper.selectById(userId).getEmail();
        if (StringUtils.isNotEmpty(email)) {
            // ????????????
            EmailDTO emailDTO = new EmailDTO();
            if (comment.getIsAudit().equals(TRUE)) {
                // ????????????
                emailDTO.setEmail(email);
                emailDTO.setSubject("????????????");
                // ??????????????????
                String url = Objects.nonNull(comment.getArticleId())
                        ? websiteUrl + ARTICLE_PATH + comment.getArticleId()
                        : websiteUrl;
                emailDTO.setContent("???????????????????????????????????????\n" + url + "\n????????????");
            } else {
                // ?????????????????????
                String adminEmail = userMapper.selectById(authorId).getEmail();
                emailDTO.setEmail(adminEmail);
                emailDTO.setSubject("????????????");
                emailDTO.setContent("??????????????????????????????????????????????????????????????????");
            }
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*",
                    new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        }
    }

}
