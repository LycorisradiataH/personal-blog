package com.hua.controller;

import com.hua.common.annotation.OptLog;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.*;
import com.hua.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.hua.common.constant.OptTypeConst.UPDATE;

/**
 * 用户模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 23:09
 */
@Api(tags = "用户模块")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送邮箱验证码
     * @param username 用户名
     * @return {@link Result}
     */
    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("/user/code")
    public Result sendCode(@RequestParam("username") String username) {
        userService.sendCode(username);
        return Result.success();
    }

    /**
     * 用户注册
     * @param registerParam 用户信息
     * @return {@link Result}
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/user/register")
    public Result register(@RequestBody RegisterParam registerParam) {
        userService.register(registerParam);
        return Result.success();
    }

    /**
     * 修改密码
     * @param registerParam 用户信息
     * @return {@link Result}
     */
    @ApiOperation(value = "修改密码")
    @PutMapping("/user/forget")
    public Result forgetPassword(@RequestBody RegisterParam registerParam) {
        userService.forgetPassword(registerParam);
        return Result.success();
    }

    /**
     * 换绑用户邮箱
     * @param registerParam 邮箱信息
     * @return {@link Result}
     */
    @ApiOperation(value = "绑定用户邮箱")
    @PutMapping("/user/updateEmail")
    public Result updateEmail(@RequestBody RegisterParam registerParam) {
        userService.updateEmail(registerParam);
        return Result.success();
    }

    /**
     * 更新用户信息
     * @param userInfoParam 用户信息
     * @return {@link Result}
     */
    @ApiOperation(value = "更新用户信息")
    @PutMapping("/user/updateUserInfo")
    public Result updateUserInfo(@Valid @RequestBody UserInfoParam userInfoParam) {
        userService.updateUserInfo(userInfoParam);
        return Result.success();
    }

    /**
     * 更新用户头像
     * @param file 文件
     * @return {@link Result} 头像地址
     */
    @ApiOperation(value = "更新用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile")
    @PostMapping("/user/avatar")
    public Result updateUserAvatar(MultipartFile file) {
        return Result.success(userService.updateUserAvatar(file));
    }

    /**
     * 获取用户区域分布
     * @param queryParam 条件
     * @return {@link Result} 用户区域分布
     */
    @ApiOperation(value = "获取用户区域分布")
    @GetMapping("/admin/user/area")
    public Result listUserAreas(QueryParam queryParam) {
        return Result.success(userService.listUserAreas(queryParam));
    }

    /**
     * 查询后台用户列表
     * @param queryParam 条件
     * @return {@link Result>} 用户列表
     */
    @ApiOperation(value = "查询后台用户列表")
    @GetMapping("/admin/user")
    public Result listBackUser(QueryParam queryParam) {
        return Result.success(userService.listBackUser(queryParam));
    }

    /**
     * 修改用户禁用状态
     *
     * @param userDisableParam 用户禁用信息
     * @return {@link Result}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/admin/user/{id}")
    public Result updateUserDisableStatus(@Valid @RequestBody UserDisableParam userDisableParam) {
        userService.updateUserDisableStatus(userDisableParam);
        return Result.success();
    }

    /**
     * 修改用户角色
     * @param userRoleParam 用户角色信息
     * @return {@link Result}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户角色")
    @PutMapping("/admin/user/role")
    public Result updateUserRole(@Valid @RequestBody UserRoleParam userRoleParam) {
        userService.updateUserRole(userRoleParam);
        return Result.success();
    }

    /**
     * 查看在线用户
     * @param queryParam 条件
     * @return {@link Result} 在线用户列表
     */
    @ApiOperation(value = "查看在线用户")
    @GetMapping("/admin/user/online")
    public Result listOnlineUser(QueryParam queryParam) {
        return Result.success(userService.listOnlineUser(queryParam));
    }

    /**
     * 下线用户
     * @param userId 用户信息
     * @return {@link Result}
     */
    @ApiOperation(value = "下线用户")
    @DeleteMapping("/admin/user/online/{id}")
    public Result removeOnlineUser(@PathVariable("id") Integer userId) {
        userService.removeOnlineUser(userId);
        return Result.success();
    }

    /**
     * 修改管理员密码
     * @param passwordParam 密码信息
     * @return {@link Result}
     */
    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/user/password")
    public Result updateUserPassword(@Valid @RequestBody PasswordParam passwordParam) {
        userService.updateAdminPassword(passwordParam);
        return Result.success();
    }

}
