package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索文章 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 23:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Document(indexName = "article")
public class ArticleSearchVO {

    /**
     * 文章id
     */
    //@Id
    private Integer id;

    /**
     * 文章标题
     */
    //@Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String articleTitle;

    /**
     * 文章内容
     */
    //@Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String articleContent;

    /**
     * 是否删除
     */
    //@Field(type = FieldType.Integer)
    private Integer isDelete;

    /**
     * 文章状态
     */
    //@Field(type = FieldType.Integer)
    private Integer status;

}
