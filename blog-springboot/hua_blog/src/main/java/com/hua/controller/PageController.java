package com.hua.controller;

import com.hua.common.annotation.OptLog;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.PageParam;
import com.hua.service.PageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.hua.common.constant.OptTypeConst.REMOVE;
import static com.hua.common.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * 页面模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 17:17
 */
@Api(tags = "页面模块")
@RestController
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 获取页面列表
     * @return 页面列表
     */
    @ApiOperation(value = "获取页面列表")
    @GetMapping("/admin/page")
    public Result listPages() {
        return Result.success(pageService.listPage());
    }

    /**
     * 添加或更新页面
     * @param pageParam 页面信息
     * @return {@link Result}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新页面")
    @PostMapping("/admin/page")
    public Result savePage(@Valid @RequestBody PageParam pageParam) {
        pageService.insertOrUpdatePage(pageParam);
        return Result.success();
    }

    /**
     * 删除页面
     * @param pageId 页面id
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除页面")
    @ApiImplicitParam(name = "pageId", value = "页面id", required = true, dataType = "Integer")
    @DeleteMapping("/admin/page/{id}")
    public Result deletePage(@PathVariable("id") Integer pageId) {
        pageService.deletePage(pageId);
        return Result.success();
    }

}
