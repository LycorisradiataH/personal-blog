package com.hua.controller;

import com.hua.common.annotation.OptLog;
import com.hua.common.enums.FilePathEnum;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.WebsiteConfigVO;
import com.hua.pojo.vo.param.BlogInfoParam;
import com.hua.service.BlogInfoService;
import com.hua.util.QiniuUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.hua.common.constant.OptTypeConst.UPDATE;

/**
 * 博客信息模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 17:28
 */
@Api(tags = "博客信息模块")
@RestController
public class BlogInfoController {

    @Autowired
    private BlogInfoService blogInfoService;

    /**
     * 查看博客信息
     * @return 博客信息
     */
    @ApiOperation(value = "查看博客信息")
    @GetMapping()
    public Result getBlogInfo() {
        return Result.success(blogInfoService.getBlogInfo());
    }

    /**
     * 查看关于我信息
     * @return 关于我信息
     */
    @ApiOperation(value = "查看关于我信息")
    @GetMapping("/about")
    public Result getAboutMe() {
        return Result.success(blogInfoService.getAboutMe());
    }

    /**
     * 查看后台信息
     * @return 后台信息
     */
    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin")
    public Result getBlogBackInfo() {
        return Result.success(blogInfoService.getBlogBackInfo());
    }

    /**
     * 修改关于我信息
     * @param blogInfoParam 博客信息
     * @return {@link Result}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改关于我信息")
    @PutMapping("/admin/about")
    public Result updateAboutMe(@Valid @RequestBody BlogInfoParam blogInfoParam) {
        blogInfoService.updateAboutMe(blogInfoParam);
        return Result.success();
    }
    /**
     * 获取网站配置
     * @return 网站配置
     */
    @ApiOperation(value = "获取网站配置")
    @GetMapping("/admin/website/config")
    public Result getWebsiteConfig() {
        return Result.success(blogInfoService.getWebsiteConfig());
    }

    /**
     * 更新网站配置
     * @param websiteConfigVO 网站配置信息
     * @return {@link Result}
     */
    @ApiOperation(value = "更新网站配置")
    @PutMapping("/admin/website/config")
    public Result updateWebsiteConfig(@Valid @RequestBody WebsiteConfigVO websiteConfigVO) {
        blogInfoService.updateWebsiteConfig(websiteConfigVO);
        return Result.success();
    }

    /**
     * 上传博客配置图片
     * @param file 文件
     * @return 博客配置图片路径
     */
    @ApiOperation(value = "上传博客配置图片")
    @ApiImplicitParam(name = "file", value = "图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/config/images")
    public Result savePhotoAlbumCover(MultipartFile file) {
        return Result.success(QiniuUtils.upload(file, FilePathEnum.CONFIG.getPath()));
    }

    /**
     * 上传访客信息
     * @return {@link Result}
     */
    @PostMapping("/report")
    public Result report() {
        blogInfoService.report();
        return Result.success();
    }

}
