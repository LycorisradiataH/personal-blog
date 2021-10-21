package com.hua.controller;

import com.hua.common.annotation.OptLog;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.pojo.vo.param.RoleParam;
import com.hua.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.hua.common.constant.OptTypeConst.REMOVE;
import static com.hua.common.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * 角色模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/15 20:04
 */
@Api(tags = "角色模块")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 查询用户角色选项
     * @return 用户角色选项
     */
    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/admin/user/role")
    public Result listUserRole() {
        return Result.success(roleService.listUserRoles());
    }

    /**
     * 查询角色列表
     * @param queryParam 条件
     * @return 角色列表
     */
    @ApiOperation(value = "查询角色列表")
    @GetMapping("/admin/role")
    public Result listRole(QueryParam queryParam) {
        return Result.success(roleService.listRole(queryParam));
    }

    /**
     * 保存或更新角色
     * @param roleParam 角色信息
     * @return {@link Result}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新角色")
    @PostMapping("/admin/role")
    public Result saveRole(@Valid @RequestBody RoleParam roleParam) {
        roleService.insertOrUpdateRole(roleParam);
        return Result.success();
    }

    /**
     * 删除角色
     * @param roleIdList 角色id列表
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/admin/role")
    public Result deleteRole(@RequestBody List<Integer> roleIdList) {
        roleService.deleteRole(roleIdList);
        return Result.success();
    }

}
