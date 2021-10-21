package com.hua.controller;

import com.hua.common.annotation.OptLog;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.pojo.vo.param.TagParam;
import com.hua.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.hua.common.constant.OptTypeConst.REMOVE;
import static com.hua.common.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * 标签模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 18:33
 */
@Api(tags = "标签模块")
@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 查询标签列表
     * @return 标签列表
     */
    @ApiOperation(value = "查询标签列表")
    @GetMapping("/tag")
    public Result listTag() {
        return Result.success(tagService.listTag());
    }

    /**
     * 查询后台标签列表
     * @param queryParam 条件
     * @return 标签列表
     */
    @ApiOperation(value = "查询后台标签列表")
    @GetMapping("/admin/tag")
    public Result listBackTag(QueryParam queryParam) {
        return Result.success(tagService.listBackTag(queryParam));
    }

    /**
     * 搜索文章标签
     * @param queryParam 条件
     * @return 标签列表
     */
    @ApiOperation(value = "搜索文章标签")
    @GetMapping("/admin/tag/search")
    public Result listTagBySearch(QueryParam queryParam) {
        return Result.success(tagService.listTagBySearch(queryParam));
    }

    /**
     * 添加或修改标签
     * @param tagParam 标签信息
     * @return {@link Result}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改标签")
    @PostMapping("/admin/tag")
    public Result saveTag(@Valid @RequestBody TagParam tagParam) {
        tagService.insertOrUpdateTag(tagParam);
        return Result.success();
    }

    /**
     * 删除标签
     * @param tagIdList 标签id列表
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除标签")
    @DeleteMapping("/admin/tag")
    public Result deleteTagByIds(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTag(tagIdList);
        return Result.success();
    }

}
