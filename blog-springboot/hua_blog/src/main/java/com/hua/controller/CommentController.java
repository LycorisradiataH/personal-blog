package com.hua.controller;

import com.hua.common.annotation.OptLog;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.AuditParam;
import com.hua.pojo.vo.param.CommentParam;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.hua.common.constant.OptTypeConst.REMOVE;
import static com.hua.common.constant.OptTypeConst.UPDATE;

/**
 * 评论模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 20:58
 */
@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 查询评论
     *
     * @param articleId 文章id
     * @return 评论列表
     */
    @ApiOperation(value = "查询评论")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/comment/{id}")
    public Result listComments(@PathVariable("id") Integer articleId) {
        return Result.success(commentService.listComment(articleId));
    }

    /**
     * 查询评论下的回复
     * @param commentId 评论id
     * @return 回复列表
     */
    @ApiOperation(value = "查询评论下的回复")
    @ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "Integer")
    @GetMapping("/comment/listReply/{id}")
    public Result listReply(@PathVariable("id") Integer commentId) {
        return Result.success(commentService.listReplyByCommentId(commentId));
    }

    /**
     * 添加评论
     * @param commentParam 评论信息
     * @return {@link Result}
     */
    @ApiOperation(value = "添加评论")
    @PostMapping("/comment/saveComment")
    public Result insertComment(@Valid @RequestBody CommentParam commentParam) {
        commentService.insertComment(commentParam);
        return Result.success();
    }

    /**
     * 评论点赞
     * @param commentId 评论id
     * @return {@link Result}
     */
    @ApiOperation(value = "评论点赞")
    @PostMapping("/comment/like/{id}")
    public Result likeComment(@PathVariable("id") Integer commentId) {
        commentService.likeComment(commentId);
        return Result.success();
    }

    /**
     * 查询后台评论
     * @param queryParam 条件
     * @return 后台评论列表
     */
    @ApiOperation(value = "查询后台评论")
    @GetMapping("/admin/comment")
    public Result listBackComment(QueryParam queryParam) {
        return Result.success(commentService.listBackComment(queryParam));
    }

    /**
     * 审核评论
     * @param auditParam 审核信息
     * @return {@link Result}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核评论")
    @PutMapping("/admin/comment/audit")
    public Result updateCommentsReview(@Valid @RequestBody AuditParam auditParam) {
        commentService.updateCommentsAudit(auditParam);
        return Result.success();
    }

    /**
     * 删除评论
     * @param commentIdList 评论id列表
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除评论")
    @DeleteMapping("/admin/comment")
    public Result deleteCommentByIds(@RequestBody List<Integer> commentIdList) {
        commentService.deleteComment(commentIdList);
        return Result.success();
    }

}
