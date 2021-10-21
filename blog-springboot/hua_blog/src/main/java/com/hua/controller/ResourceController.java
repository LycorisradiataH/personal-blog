package com.hua.controller;

import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.pojo.vo.param.ResourceParam;
import com.hua.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 资源模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 17:31
 */
@Api(tags = "资源模块")
@RestController
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    /**
     * 导入Swagger接口
     * @return {@link Result}
     */
    @ApiOperation(value = "导入swagger接口")
    @GetMapping("/admin/resource/import/swagger")
    public Result importSwagger() {
        resourceService.importSwagger();
        return Result.success();
    }

    /**
     * 查看资源列表
     * @param queryParam 条件
     * @return 资源列表
     */
    @ApiOperation(value = "查看资源列表")
    @GetMapping("/admin/resource")
    public Result listResources(QueryParam queryParam) {
        return Result.success(resourceService.listResource(queryParam));
    }

    /**
     * 新增或修改资源
     * @param resourceParam 资源信息
     * @return {@link Result}
     */
    @ApiOperation(value = "新增或修改资源")
    @PostMapping("/admin/resource")
    public Result saveResource(@Valid @RequestBody ResourceParam resourceParam) {
        resourceService.insertOrUpdateResource(resourceParam);
        return Result.success();
    }

    /**
     * 删除资源
     * @param resourceId 资源id
     * @return {@link Result}
     */
    @ApiOperation(value = "删除资源")
    @DeleteMapping("/admin/resources/{id}")
    public Result deleteResource(@PathVariable("id") Integer resourceId) {
        resourceService.deleteResource(resourceId);
        return Result.success();
    }

    /**
     * 查看角色资源选项
     * @return 角色资源选项
     */
    @ApiOperation(value = "查看角色资源选项")
    @GetMapping("/admin/role/resource")
    public Result listResourceOption() {
        return Result.success(resourceService.listResourceOption());
    }

}
