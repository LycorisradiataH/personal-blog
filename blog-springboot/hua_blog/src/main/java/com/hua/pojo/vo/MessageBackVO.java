package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 后台留言 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 16:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageBackVO {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户ip
     */
    private String ipAddress;

    /**
     * 用户ip地址
     */
    private String ipSource;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 留言内容
     */
    private String messageContent;

    /**
     * 是否审核
     */
    private Integer isAudit;

    /**
     * 留言时间
     */
    private LocalDateTime gmtCreate;

}
