package com.hua.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 访问量
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/13 13:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("hua_unique_view")
public class UniqueView {

    /**
     * 访问量id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 访问量
     */
    private Integer viewCount;

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
