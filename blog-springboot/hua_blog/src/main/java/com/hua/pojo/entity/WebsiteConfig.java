package com.hua.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 网站配置
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/17 18:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("hua_website_config")
public class WebsiteConfig {

    /**
     * 配置id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 配置
     */
    private String config;

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
