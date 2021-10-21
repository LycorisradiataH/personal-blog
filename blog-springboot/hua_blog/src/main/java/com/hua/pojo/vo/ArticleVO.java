package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 22:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVO {

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
     * 内容
     */
    private String articleContent;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 文章类型
     */
    private Integer type;

    /**
     * 原文链接
     */
    private String originalUrl;

    /**
     * 发表时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModified;

    /**
     * 文章分类id
     */
    private Integer categoryId;

    /**
     * 文章分类名
     */
    private String categoryName;

    /**
     * 文章标签
     */
    private List<TagVO> tagVOList;

    /**
     * 上一篇文章
     */
    private ArticlePaginationVO lastArticle;

    /**
     * 下一篇文章
     */
    private ArticlePaginationVO nextArticle;

    /**
     * 推荐文章列表
     */
    private List<ArticleRecommendVO> recommendArticleList;

    /**
     * 最新文章列表
     */
    private List<ArticleRecommendVO> newestArticleList;

}
