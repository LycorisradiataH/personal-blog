package com.hua.controller;

import com.hua.common.annotation.OptLog;
import com.hua.common.enums.FilePathEnum;
import com.hua.pojo.dto.ArticleBackDTO;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.ArticleTopParam;
import com.hua.pojo.vo.param.DeleteParam;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.ArticleService;
import com.hua.util.QiniuUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static com.hua.common.constant.OptTypeConst.*;

/**
 * 文章模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 16:10
 */
@Api(tags = "文章模块")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 查看首页文章
     * @return 首页文章列表
     */
    @ApiOperation(value = "查看首页文章")
    @GetMapping("/home")
    public Result listArticle() {
        return Result.success(articleService.listArticle());
    }

    /**
     * 查看文章归档
     * @return 文章归档列表
     */
    @ApiOperation(value = "查看文章归档")
    @GetMapping("/article/archive")
    public Result listArchive() {
        return Result.success(articleService.listArchive());
    }

    /**
     * 根据id查看文章
     * @param articleId 文章id
     * @return 文章信息
     */
    @ApiOperation(value = "根据id查看文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/article/{id}")
    public Result getArticleById(@PathVariable("id") Integer articleId) {
        return Result.success(articleService.getArticleById(articleId));
    }

    /**
     * 根据条件查询文章
     * @param queryParam 条件
     * @return  文章列表
     */
    @ApiOperation(value = "根据条件查询文章")
    @GetMapping("/article/queryParam")
    public Result listArticleByQueryParam(QueryParam queryParam) {
        return Result.success(articleService.listArticleByQueryParam(queryParam));
    }

    /**
     * 点赞文章
     * @param articleId 文章id
     * @return {@link Result}
     */
    @ApiOperation(value = "点赞文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @PostMapping("/article/like/{id}")
    public Result likeArticle(@PathVariable("id") Integer articleId) {
        articleService.likeArticle(articleId);
        return Result.success();
    }

    /**
     * 搜索文章
     * @param queryParam 条件
     * @return 文章列表
     */
    @ApiOperation(value = "搜索文章")
    @GetMapping("/article/search")
    public Result searchArticle(QueryParam queryParam) {
        return Result.success(articleService.searchArticle(queryParam));
    }

    /**
     * 查看后台文章
     * @param queryParam 条件
     * @return 后台文章列表
     */
    @ApiOperation(value = "查看后台文章")
    @GetMapping("/admin/article")
    public Result listBackArticle(QueryParam queryParam) {
        return Result.success(articleService.listBackArticle(queryParam));
    }

    /**
     * 添加或修改文章
     * @param articleBackDTO 文章信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改文章")
    @PostMapping("/admin/article")
    public Result saveArticle(@Valid @RequestBody ArticleBackDTO articleBackDTO) {
        articleService.insertOrUpdateArticle(articleBackDTO);
        return Result.success();
    }

    /**
     * 修改文章置顶状态
     * @param articleTopParam 文章置顶信息
     * @return {@link Result}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文章置顶")
    @PutMapping("/admin/article/top")
    public Result updateArticleTop(@Valid @RequestBody ArticleTopParam articleTopParam) {
        articleService.updateArticleTop(articleTopParam);
        return Result.success();
    }

    /**
     * 上传文章图片
     * @param file 文件
     * @return 文章图片地址
     */
    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/article/image")
    public Result uploadArticleImage(MultipartFile file) {
        return Result.success(QiniuUtils.upload(file, FilePathEnum.ARTICLE.getPath()));
    }

    /**
     * 根据id查看后台文章
     * @param articleId 文章id
     * @return 后台文章
     */
    @ApiOperation(value = "根据id查看后台文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/admin/article/{id}")
    public Result getBackArticleById(@PathVariable("id") Integer articleId) {
        return Result.success(articleService.getBackArticleById(articleId));
    }

    /**
     * 恢复或删除文章
     * @param deleteParam 逻辑删除信息
     * @return {@link Result}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "恢复或删除文章")
    @PutMapping("/admin/article")
    public Result updateArticleDelete(DeleteParam deleteParam) {
        articleService.updateArticleDelete(deleteParam);
        return Result.success();
    }

    /**
     * 删除文章
     * @param articleIdList 文章id列表
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理删除文章")
    @DeleteMapping("/admin/article")
    public Result deleteArticleByIds(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticle(articleIdList);
        return Result.success();
    }

}
