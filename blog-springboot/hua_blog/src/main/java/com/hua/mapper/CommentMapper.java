package com.hua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hua.pojo.dto.ReplyCountDTO;
import com.hua.pojo.entity.Comment;
import com.hua.pojo.vo.CommentBackVO;
import com.hua.pojo.vo.CommentVO;
import com.hua.pojo.vo.ReplyVO;
import com.hua.pojo.vo.param.QueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 20:57
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询评论
     * @param current 页码
     * @param size 页大小
     * @param articleId 文章id
     * @return 评论列表
     */
    List<CommentVO> listComment(@Param("current") Long current, @Param("size") Long size, @Param("articleId") Integer articleId);

    /**
     * 查询评论id集合下的回复
     * @param commentIdList 评论id集合
     * @return 回复列表
     */
    List<ReplyVO> listReply(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 根据评论id查询回复量
     * @param commentIdList 评论id集合
     * @return 回复量
     */
    List<ReplyCountDTO> listReplyCountByCommentId(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 查询当条评论下的回复
     * @param current 页码
     * @param size 页大小
     * @param commentId 评论id
     * @return 回复列表
     */
    List<ReplyVO> listReplyByCommentId(@Param("current") Long current, @Param("size") Long size, @Param("commentId") Integer commentId);

    /**
     * 统计后台评论量
     * @param queryParam 条件
     * @return 评论量
     */
    Integer countComment(@Param("queryParam") QueryParam queryParam);

    /**
     * 查询后台评论
     * @param current 页码
     * @param size 页大小
     * @param queryParam 条件
     * @return 评论列表
     */
    List<CommentBackVO> listBackComment(@Param("current") Long current, @Param("size") Long size, @Param("queryParam") QueryParam queryParam);
}
