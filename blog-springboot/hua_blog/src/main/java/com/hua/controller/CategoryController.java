package com.hua.controller;

import com.hua.common.annotation.OptLog;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.CategoryParam;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.hua.common.constant.OptTypeConst.REMOVE;
import static com.hua.common.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * 分类模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 18:15
 */
@Api(tags = "分类模块")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查看分类列表
     * @return 分类列表
     */
    @ApiOperation(value = "查看分类列表")
    @GetMapping("/category")
    public Result listCategory() {
        return Result.success(categoryService.listCategory());
    }

    /**
     * 查看后台分类列表
     * @param queryParam 条件
     * @return 后台分类列表
     */
    @ApiOperation(value = "查看后台分类列表")
    @GetMapping("/admin/category")
    public Result listBackCategory(QueryParam queryParam) {
        return Result.success(categoryService.listBackCategory(queryParam));
    }

    /**
     * 搜索文章分类
     * @param queryParam 条件
     * @return 分类列表
     */
    @ApiOperation(value = "搜索文章分类")
    @GetMapping("/admin/category/search")
    public Result listCategoriesBySearch(QueryParam queryParam) {
        return Result.success(categoryService.listCategoryBySearch(queryParam));
    }

    /**
     * 添加或修改分类
     * @param categoryParam 分类信息
     * @return {@link Result}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改分类")
    @PostMapping("/admin/category")
    public Result saveCategory(@Valid @RequestBody CategoryParam categoryParam) {
        categoryService.insertOrUpdateCategory(categoryParam);
        return Result.success();
    }

    /**
     * 删除分类
     * @param categoryIdList 分类id列表
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除分类")
    @DeleteMapping("/admin/category")
    public Result deleteCategoryByIds(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategory(categoryIdList);
        return Result.success();
    }

}
