package com.hua.controller;

import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.MenuParam;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 菜单模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/16 16:52
 */
@Api(tags = "菜单模块")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 查询菜单列表
     * @param queryParam 条件
     * @return 菜单列表
     */
    @ApiOperation(value = "查看菜单列表")
    @GetMapping("/admin/menu")
    public Result listMenu(QueryParam queryParam) {
        return Result.success(menuService.listMenu(queryParam));
    }

    /**
     * 新增或修改菜单
     * @param menuParam 菜单
     * @return {@link Result}
     */
    @ApiOperation(value = "新增或修改菜单")
    @PostMapping("/admin/menu")
    public Result saveMenu(@Valid @RequestBody MenuParam menuParam) {
        menuService.insertOrUpdateMenu(menuParam);
        return Result.success();
    }

    /**
     * 删除菜单
     * @param menuId 菜单id
     * @return {@link Result}
     */
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/admin/menu/{menuId}")
    public Result deleteMenu(@PathVariable("menuId") Integer menuId){
        menuService.deleteMenu(menuId);
        return Result.success();
    }

    /**
     * 查看角色菜单选项
     * @return 查看角色菜单选项
     */
    @ApiOperation(value = "查看角色菜单选项")
    @GetMapping("/admin/role/menu")
    public Result listMenuOption() {
        return Result.success(menuService.listMenuOption());
    }

    /**
     * 查看当前用户菜单
     * @return 菜单列表
     */
    @ApiOperation(value = "查看当前用户菜单")
    @GetMapping("/admin/user/menu")
    public Result listUserMenu() {
        return Result.success(menuService.listUserMenu());
    }

}
