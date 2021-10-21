package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 后台评论 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 14:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentBackVO {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 被回复用户昵称
     */
    private String replyNickname;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 是否审核
     */
    private Integer isAudit;

    /**
     * 发表时间
     */
    private LocalDateTime gmtCreate;

}
