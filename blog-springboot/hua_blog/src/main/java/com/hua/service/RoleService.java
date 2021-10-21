package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.entity.Role;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.RoleVO;
import com.hua.pojo.vo.UserRoleVO;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.pojo.vo.param.RoleParam;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/15 20:03
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取用户角色选项
     * @return 角色
     */
    List<UserRoleVO> listUserRoles();

    /**
     * 查询角色列表
     * @param queryParam 条件
     * @return 角色列表
     */
    PageResult<RoleVO> listRole(QueryParam queryParam);

    /**
     * 添加或更新角色
     * @param roleParam 角色
     */
    void insertOrUpdateRole(RoleParam roleParam);

    /**
     * 删除角色
     * @param roleIdList 角色id列表
     */
    void deleteRole(List<Integer> roleIdList);
}
