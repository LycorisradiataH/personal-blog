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
        // 查询文章评论量
        Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                        .eq(Objects.nonNull(articleId), Comment::getArticleId, articleId)
                        .isNull(Objects.isNull(articleId), Comment::getArticleId)
                        .isNull(Comment::getParentId)
                        .eq(Comment::getIsAudit, TRUE));
        if (commentCount == 0) {
            return new PageResult<>();
        }
        // 分页查询评论集合
        List<CommentVO> commentVOList = commentMapper.listComment(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), articleId);
        if (CollectionUtils.isEmpty(commentVOList)) {
            return new PageResult<>();
        }
        // 查询redis的评论点赞数据
        Map<Object, Object> likeCountMap = redisUtils.hmGet(COMMENT_LIKE_COUNT);
        // 提取评论id集合
        List<Integer> commentIdList = commentVOList.stream()
                .map(CommentVO::getId)
                .collect(Collectors.toList());
        // 根据评论id集合查询回复数据
        List<ReplyVO> replyVOList = commentMapper.listReply(commentIdList);
        // 封装回复点赞量
        replyVOList.forEach(replyVO ->
                replyVO.setLikeCount((Integer) likeCountMap.get(String.valueOf(replyVO.getId()))));
        // 根据评论id分组回复数据
        Map<Integer, List<ReplyVO>> replyMap =
                replyVOList.stream().collect(Collectors.groupingBy(ReplyVO::getParentId));
        // 根据评论id查询回复量
        Map<Integer, Integer> replyCountMap =
                commentMapper.listReplyCountByCommentId(commentIdList)
                        .stream().collect(Collectors
                        .toMap(ReplyCountDTO::getCommentId, ReplyCountDTO::getReplyCount));
        // 封装评论数据
        commentVOList.forEach(commentVO -> {
            commentVO.setLikeCount((Integer) likeCountMap.get(String.valueOf(commentVO.getId())));
            commentVO.setReplyVOList(replyMap.get(commentVO.getId()));
            commentVO.setReplyCount(replyCountMap.get(commentVO.getId()));
        });
        return new PageResult<>(commentVOList, commentCount);
    }

    @Override
    public List<ReplyVO> listReplyByCommentId(Integer commentId) {
        // 查询评论下的回复
        List<ReplyVO> replyVOList = commentMapper.listReplyByCommentId(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), commentId);
        // 查询redis的评论点赞集合
        Map<Object, Object> likeCountMap = redisUtils.hmGet(COMMENT_LIKE_COUNT);
        // 封装点赞数据
        replyVOList.forEach(replyVO ->
                replyVO.setLikeCount((Integer) likeCountMap.get(String.valueOf(replyVO.getId()))));
        return replyVOList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertComment(CommentParam commentParam) {
        // 判断是否需要审核
        WebsiteConfigVO websiteConfig = blogInfoService.getWebsiteConfig();
        Integer isCommentAudit = websiteConfig.getIsCommentAudit();
        // 过滤标签
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
        // 判断是否开启邮箱通知，通知用户
        if (websiteConfig.getIsEmailNotice().equals(TRUE)) {
            notice(comment);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void likeComment(Integer commentId) {
        // 判断是否点过赞
        String commentLikeKey = COMMENT_USER_LIKE + UserUtils.getLoginUser().getId();
        if (redisUtils.sHasKey(commentLikeKey, commentId)) {
            // 点过赞则删除评论id
            redisUtils.sRem(commentLikeKey, commentId);
            // 评论点赞量 - 1
            redisUtils.hDecr(COMMENT_LIKE_COUNT, String.valueOf(commentId), 1);
        } else {
            // 未点赞则增加评论id
            redisUtils.sSet(commentLikeKey, commentId);
            // 评论点赞量 + 1
            redisUtils.hIncr(COMMENT_LIKE_COUNT, String.valueOf(commentId), 1);
        }
    }

    @Override
    public PageResult<CommentBackVO> listBackComment(QueryParam queryParam) {
        // 统计后台评论量
        Integer count = commentMapper.countComment(queryParam);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台评论集合
        List<CommentBackVO> commentBackVOList =
                commentMapper.listBackComment(PageUtils.getLimitCurrent(), PageUtils.getSize(),
                        queryParam);
        return new PageResult<>(commentBackVOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCommentsAudit(AuditParam auditParam) {
        // 修改审核状态
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
        // 物理删除评论
        commentMapper.deleteBatchIds(commentIdList);
    }

    /**
     * 通知评论用户
     * @param comment 评论信息
     */
    @Async
    public void notice(Comment comment) {
        // 查询回复用户邮箱
        Integer authorId = articleMapper.selectById(comment.getArticleId()).getAuthorId();
        Integer userId = Optional.ofNullable(comment.getReplyId()).orElse(authorId);
        String email = userMapper.selectById(userId).getEmail();
        if (StringUtils.isNotEmpty(email)) {
            // 发送消息
            EmailDTO emailDTO = new EmailDTO();
            if (comment.getIsAudit().equals(TRUE)) {
                // 评论提醒
                emailDTO.setEmail(email);
                emailDTO.setSubject("评论提醒");
                // 判断页面路径
                String url = Objects.nonNull(comment.getArticleId())
                        ? websiteUrl + ARTICLE_PATH + comment.getArticleId()
                        : websiteUrl;
                emailDTO.setContent("您收到一条新的回复，请前往\n" + url + "\n页面查看");
            } else {
                // 管理员审核提醒
                String adminEmail = userMapper.selectById(authorId).getEmail();
                emailDTO.setEmail(adminEmail);
                emailDTO.setSubject("审核提醒");
                emailDTO.setContent("您收到了一条新的回复，请前往后台管理页面审核");
            }
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*",
                    new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        }
    }

}
