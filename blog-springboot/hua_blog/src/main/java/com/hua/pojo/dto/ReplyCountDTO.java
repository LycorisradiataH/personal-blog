package com.hua.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回复量 dto
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/12 19:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyCountDTO {

    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 回复量
     */
    private Integer replyCount;

}
