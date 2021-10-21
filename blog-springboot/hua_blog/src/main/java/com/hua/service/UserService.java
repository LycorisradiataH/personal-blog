package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.entity.User;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.UserAreaVO;
import com.hua.pojo.vo.UserBackVO;
import com.hua.pojo.vo.UserOnlineVO;
import com.hua.pojo.vo.param.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 23:10
 */
public interface UserService extends IService<User> {

    /**
     * 发送邮箱验证码
     * @param username 邮箱号
     */
    void sendCode(String username);

    /**
     * 注册用户
     * @param registerParam 注册参数
     */
    void register(RegisterParam registerParam);

    /**
     * 忘记密码
     * @param registerParam 忘记密码参数
     */
    void forgetPassword(RegisterParam registerParam);

    /**
     * 修改邮箱
     * @param registerParam 修改邮箱参数
     */
    void updateEmail(RegisterParam registerParam);

    /**
     * 修改用户信息
     * @param userInfoParam 用户信息参数
     */
    void updateUserInfo(UserInfoParam userInfoParam);

    /**
     * 修改用户头像
     * @param file 头像文件
     * @return 头像的地址
     */
    String updateUserAvatar(MultipartFile file);

    /**
     * 获取用户区域分布
     *
     * @param queryParam 条件
     * @return 用户区域分布
     */
    List<UserAreaVO> listUserAreas(QueryParam queryParam);

    /**
     * 查询后台用户列表
     * @param queryParam 条件
     * @return 用户列表
     */
    PageResult<UserBackVO> listBackUser(QueryParam queryParam);

    /**
     * 修改用户禁用状态
     * @param userDisableParam 用户禁用参数
     */
    void updateUserDisableStatus(UserDisableParam userDisableParam);

    /**
     * 修改用户角色
     * @param userRoleParam 用户权限
     */
    void updateUserRole(UserRoleParam userRoleParam);

    /**
     * 获取在线用户列表
     * @param queryParam 条件
     * @return 在线用户列表
     */
    PageResult<UserOnlineVO> listOnlineUser(QueryParam queryParam);

    /**
     * 下线用户
     * @param userId 用户id
     */
    void removeOnlineUser(Integer userId);

    /**
     * 修改管理员密码
     * @param passwordParam 密码参数
     */
    void updateAdminPassword(PasswordParam passwordParam);

}
