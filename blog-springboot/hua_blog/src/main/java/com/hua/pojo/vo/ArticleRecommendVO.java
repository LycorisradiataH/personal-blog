package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 推荐文章 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 22:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRecommendVO {

    /**
     * id
     */
    private Integer id;

    /**
     * 文章缩略图
     */
    private String articleCover;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

}
