package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.entity.Comment;
import com.hua.pojo.vo.CommentBackVO;
import com.hua.pojo.vo.CommentVO;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.ReplyVO;
import com.hua.pojo.vo.param.AuditParam;
import com.hua.pojo.vo.param.CommentParam;
import com.hua.pojo.vo.param.QueryParam;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 20:57
 */
public interface CommentService extends IService<Comment> {

    /**
     * 查看评论
     * @param articleId 文章id
     * @return 评论列表
     */
    PageResult<CommentVO> listComment(Integer articleId);

    /**
     * 查看评论下的回复
     * @param commentId 评论id
     * @return 回复集合
     */
    List<ReplyVO> listReplyByCommentId(Integer commentId);

    /**
     * 添加评论或回复
     * @param commentParam 评论信息
     */
    void insertComment(CommentParam commentParam);

    /**
     * 点赞评论
     * @param commentId 评论id
     */
    void likeComment(Integer commentId);

    /**
     * 查询后台评论
     * @param queryParam 条件
     * @return 评论列表
     */
    PageResult<CommentBackVO> listBackComment(QueryParam queryParam);

    /**
     * 审核评论
     * @param auditParam 审核参数
     */
    void updateCommentsAudit(AuditParam auditParam);

    /**
     * 物理删除评论
     * @param commentIdList 评论id集合
     */
    void deleteComment(List<Integer> commentIdList);

}
