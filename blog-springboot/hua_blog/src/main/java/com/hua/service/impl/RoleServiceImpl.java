package com.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.common.exception.ServiceException;
import com.hua.common.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.hua.mapper.RoleMapper;
import com.hua.mapper.UserRoleMapper;
import com.hua.pojo.entity.Role;
import com.hua.pojo.entity.RoleMenu;
import com.hua.pojo.entity.RoleResource;
import com.hua.pojo.entity.UserRole;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.RoleVO;
import com.hua.pojo.vo.UserRoleVO;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.pojo.vo.param.RoleParam;
import com.hua.service.RoleMenuService;
import com.hua.service.RoleResourceService;
import com.hua.service.RoleService;
import com.hua.util.BeanCopyUtils;
import com.hua.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hua.common.constant.CommonConst.FALSE;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/15 20:03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Override
    public List<UserRoleVO> listUserRoles() {
        // 查询角色列表
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleName));
        return BeanCopyUtils.copyList(roleList, UserRoleVO.class);
    }

    @Override
    public PageResult<RoleVO> listRole(QueryParam queryParam) {
        // 查询角色列表
        List<RoleVO> roleVOList = roleMapper.listRole(PageUtils.getLimitCurrent(),
                PageUtils.getSize(), queryParam);
        // 查询角色总数
        Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .like(StringUtils.isNotEmpty(queryParam.getKeywords()), Role::getRoleName,
                        queryParam.getKeywords()));
        return new PageResult<>(roleVOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertOrUpdateRole(RoleParam roleParam) {
        // 判断角色名重复
        Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .eq(Role::getRoleName, roleParam.getRoleName()));
        if (count > 0 && Objects.isNull(roleParam.getId())) {
            throw new ServiceException("角色名已存在");
        }
        // 添加或更新角色信息
        Role role = Role.builder()
                .id(roleParam.getId())
                .roleName(roleParam.getRoleName())
                .roleLabel(roleParam.getRoleLabel())
                .isDisable(FALSE)
                .build();
        this.saveOrUpdate(role);
        // 更新角色资源关系
        if (CollectionUtils.isNotEmpty(roleParam.getResourceIdList())) {
            if (Objects.nonNull(roleParam.getId())) {
                roleResourceService.remove(new LambdaQueryWrapper<RoleResource>()
                        .eq(RoleResource::getRoleId, roleParam.getId()));
            }
            List<RoleResource> roleResourceList = roleParam.getResourceIdList().stream()
                    .map(resourceId -> RoleResource.builder()
                            .roleId(role.getId())
                            .resourceId(resourceId)
                            .build())
                    .collect(Collectors.toList());
            roleResourceService.saveBatch(roleResourceList);
            // 重新加载角色资源信息
            filterInvocationSecurityMetadataSource.clearDataSource();
        }
        // 更新角色菜单关系
        if (CollectionUtils.isNotEmpty(roleParam.getMenuIdList())) {
            if (Objects.nonNull(roleParam.getId())) {
                roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>()
                        .eq(RoleMenu::getRoleId, roleParam.getId()));
            }
            List<RoleMenu> roleMenuList = roleParam.getMenuIdList().stream()
                    .map(menuId -> RoleMenu.builder()
                            .roleId(role.getId())
                            .menuId(menuId)
                            .build())
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenuList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRole(List<Integer> roleIdList) {
        // 判断角色下是否有用户
        Integer count = userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getRoleId, roleIdList));
        if (count > 0) {
            throw new ServiceException("该角色下存在用户");
        }
        roleMapper.deleteBatchIds(roleIdList);
    }

}
