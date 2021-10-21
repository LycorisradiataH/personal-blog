package com.hua.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 留言
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/13 13:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("hua_message")
public class Message {

    /**
     * 留言id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ip
     */
    private String ipAddr;

    /**
     * 用户地址
     */
    private String ipSource;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 留言内容
     */
    private String messageContent;

    /**
     * 弹幕速度
     */
    private Integer time;

    /**
     * 是否审核
     */
    private Integer isAudit;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

}
