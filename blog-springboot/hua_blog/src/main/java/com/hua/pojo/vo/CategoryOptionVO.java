package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类选项
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 12:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryOptionVO {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;

}
