package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 后台分类 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 12:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBackVO {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

}
