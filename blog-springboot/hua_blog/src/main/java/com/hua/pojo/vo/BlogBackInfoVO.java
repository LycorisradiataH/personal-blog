package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 博客后台信息
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/18 20:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogBackInfoVO {

    /**
     * 访问量
     */
    private Integer viewCount;

    /**
     * 留言量
     */
    private Integer messageCount;

    /**
     * 用户量
     */
    private Integer userCount;

    /**
     * 文章量
     */
    private Integer articleCount;

    /**
     * 分类统计
     */
    private List<CategoryVO> categoryVOList;

    /**
     * 标签列表
     */
    private List<TagVO> tagVOList;

    /**
     * 文章统计列表
     */
    private List<ArticleStatisticsVO> articleStatisticsList;

    /**
     * 一周用户量集合
     */
    private List<UniqueViewVO> uniqueViewVOList;

    /**
     * 文章浏览量排行
     */
    private List<ArticleRankVO> articleRankVOList;

}
