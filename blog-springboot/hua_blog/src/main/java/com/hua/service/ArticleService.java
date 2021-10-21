package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.dto.ArticleBackDTO;
import com.hua.pojo.entity.Article;
import com.hua.pojo.vo.*;
import com.hua.pojo.vo.param.ArticleTopParam;
import com.hua.pojo.vo.param.DeleteParam;
import com.hua.pojo.vo.param.QueryParam;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 16:09
 */
public interface ArticleService extends IService<Article> {

    /**
     * 查询首页文章
     * @return 文章列表
     */
    List<ArticleHomeVO> listArticle();

    /**
     * 查询文章归档
     * @return 文章归档
     */
    PageResult<ArchiveVO> listArchive();

    /**
     * 根据id查看文章
     * @param articleId 文章id
     * @return 文章信息
     */
    ArticleVO getArticleById(Integer articleId);

    /**
     * 根据条件查找文章
     * @param queryParam 条件
     * @return 文章
     */
    ArticlePreviewListVO listArticleByQueryParam(QueryParam queryParam);

    /**
     * 点赞文章
     * @param articleId 文章id
     */
    void likeArticle(Integer articleId);

    /**
     * 搜索文章
     * @param queryParam 条件
     * @return 文章列表
     */
    List<ArticleSearchVO> searchArticle(QueryParam queryParam);

    /**
     * 查询后台文章
     * @param queryParam 条件
     * @return 文章列表
     */
    PageResult<ArticleBackVO> listBackArticle(QueryParam queryParam);

    /**
     * 添加或修改文章
     * @param articleBackDTO 文章参数
     */
    void insertOrUpdateArticle(ArticleBackDTO articleBackDTO);

    /**
     * 修改文章置顶
     * @param articleTopParam 文章置顶参数
     */
    void updateArticleTop(ArticleTopParam articleTopParam);

    /**
     * 根据id查看后台文章
     * @param articleId 文章id
     * @return 文章列表
     */
    ArticleBackDTO getBackArticleById(Integer articleId);

    /**
     * 删除或恢复文章
     * @param deleteParam 逻辑删除对象
     */
    void updateArticleDelete(DeleteParam deleteParam);

    /**
     * 物理删除文章
     * @param articleIdList 文章id集合
     */
    void deleteArticle(List<Integer> articleIdList);

}
