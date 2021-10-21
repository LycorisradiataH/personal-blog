package com.hua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hua.pojo.entity.Article;
import com.hua.pojo.vo.*;
import com.hua.pojo.vo.param.QueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 16:06
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页查询文章
     * @param current 页码
     * @param size 页大小
     * @return 文章列表
     */
    List<ArticleHomeVO> listArticle(@Param("current") Long current, @Param("size") Long size);

    /**
     * 根据文章id查询文章详情
     * @param articleId 文章id
     * @return 文章
     */
    ArticleVO getArticleById(@Param("articleId") Integer articleId);

    /**
     * 查看文章的推荐文章
     * @param articleId 文章id
     * @return 文章列表
     */
    List<ArticleRecommendVO> listRecommendArticle(Integer articleId);

    /**
     * 根据条件查询文章
     * @param current 页码
     * @param size 页大小
     * @param queryParam 查询条件
     * @return 文章列表
     */
    List<ArticlePreviewVO> listArticleByQueryParam(@Param("current") Long current, @Param("size") Long size, @Param("queryParam") QueryParam queryParam);

    /**
     * 查询后台文章
     * @param current 当前页
     * @param size 页大小
     * @param queryParam 条件
     * @return 文章列表
     */
    List<ArticleBackVO> listBackArticle(@Param("current") Long current, @Param("size") Long size,
                                    @Param("queryParam") QueryParam queryParam);

    /**
     * 文章统计
     * @return 文章统计结果
     */
    List<ArticleStatisticsVO> listArticleStatistics();

    /**
     * 查询后台文章总数
     * @param queryParam 条件
     * @return 文章总数
     */
    Integer queryCount(@Param("queryParam") QueryParam queryParam);
}
