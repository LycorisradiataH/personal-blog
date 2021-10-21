package com.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.common.exception.ServiceException;
import com.hua.mapper.MenuMapper;
import com.hua.mapper.RoleMenuMapper;
import com.hua.pojo.entity.Menu;
import com.hua.pojo.entity.RoleMenu;
import com.hua.pojo.vo.LabelOptionVO;
import com.hua.pojo.vo.MenuVO;
import com.hua.pojo.vo.UserMenuVO;
import com.hua.pojo.vo.param.MenuParam;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.MenuService;
import com.hua.util.BeanCopyUtils;
import com.hua.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.hua.common.constant.CommonConst.COMPONENT;
import static com.hua.common.constant.CommonConst.TRUE;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/16 16:34
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<MenuVO> listMenu(QueryParam queryParam) {
        // 查询菜单数据
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .like(StringUtils.isNotBlank(queryParam.getKeywords()), Menu::getName,
                        queryParam.getKeywords()));
        // 获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menuList);
        // 组装目录菜单数据
        List<MenuVO> menuVOList = catalogList.stream().map(item -> {
            MenuVO menuVO = BeanCopyUtils.copyObject(item, MenuVO.class);
            // 获取目录下的菜单排序
            List<MenuVO> list = BeanCopyUtils.copyList(childrenMap.get(item.getId()), MenuVO.class)
                    .stream()
                    .sorted(Comparator.comparing(MenuVO::getOrderNum))
                    .collect(Collectors.toList());
            menuVO.setChildren(list);
            childrenMap.remove(item.getId());
            return menuVO;
        }).sorted(Comparator.comparing(MenuVO::getOrderNum)).collect(Collectors.toList());
        // 若还有菜单未取出则拼接
        if (CollectionUtils.isNotEmpty(childrenMap)) {
            List<Menu> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<MenuVO> childrenVOList = childrenList.stream()
                    .map(item -> BeanCopyUtils.copyObject(item, MenuVO.class))
                    .sorted(Comparator.comparing(MenuVO::getOrderNum))
                    .collect(Collectors.toList());
            menuVOList.addAll(childrenVOList);
        }
        return menuVOList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertOrUpdateMenu(MenuParam menuParam) {
        Menu menu = BeanCopyUtils.copyObject(menuParam, Menu.class);
        this.saveOrUpdate(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMenu(Integer menuId) {
        // 查询是否有角色关联
        Integer count = roleMenuMapper.selectCount(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getMenuId, menuId));
        if (count > 0) {
            throw new ServiceException("菜单下有角色关联");
        }
        // 查询子菜单
        List<Integer> menuIdList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                    .select(Menu::getId)
                    .eq(Menu::getParentId, menuId))
                .stream()
                .map(Menu::getId)
                .collect(Collectors.toList());
        menuIdList.add(menuId);
        menuMapper.deleteBatchIds(menuIdList);
    }

    @Override
    public List<LabelOptionVO> listMenuOption() {
        // 查询菜单数据
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getName, Menu::getParentId, Menu::getOrderNum));
        // 获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menuList);
        // 组装目录菜单数据
        return catalogList.stream().map(item -> {
            // 获取目录下的菜单排序
            List<LabelOptionVO> list = new ArrayList<>();
            List<Menu> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> LabelOptionVO.builder()
                                .id(menu.getId())
                                .label(menu.getName())
                                .build())
                        .collect(Collectors.toList());
            }
            return LabelOptionVO.builder()
                    .id(item.getId())
                    .label(item.getName())
                    .children(list)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserMenuVO> listUserMenu() {
        // 查询用户菜单信息
        List<Menu> menuList =
                menuMapper.listMenusByUserId(UserUtils.getLoginUser().getId());
        // 获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menuList);
        // 转换前端菜单格式
        return convertUserMenuList(catalogList, childrenMap);
    }

    /**
     * 转换用户菜单格式
     * @param catalogList 目录
     * @param childrenMap 子菜单
     * @return 用户菜单列表
     */
    private List<UserMenuVO> convertUserMenuList(List<Menu> catalogList,
                                                 Map<Integer, List<Menu>> childrenMap) {
        return catalogList.stream().map(item -> {
            // 获取目录
            UserMenuVO userMenuVO = new UserMenuVO();
            List<UserMenuVO> list = new ArrayList<>();
            // 获取目录下的子菜单
            List<Menu> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                // 多级菜单处理
                userMenuVO = BeanCopyUtils.copyObject(item, UserMenuVO.class);
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> {
                            UserMenuVO vo = BeanCopyUtils.copyObject(menu, UserMenuVO.class);
                            vo.setHidden(menu.getIsHidden().equals(TRUE));
                            return vo;
                        })
                        .collect(Collectors.toList());
            } else {
                // 一级菜单处理
                userMenuVO.setPath(item.getPath());
                userMenuVO.setComponent(COMPONENT);
                list.add(UserMenuVO.builder()
                        .path("")
                        .name(item.getName())
                        .icon(item.getIcon())
                        .component(item.getComponent())
                        .build());
            }
            userMenuVO.setHidden(item.getIsHidden().equals(TRUE));
            userMenuVO.setChildren(list);
            return userMenuVO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取目录下菜单列表
     * @param menuList 菜单列表
     * @return 目录下的菜单列表
     */
    private Map<Integer, List<Menu>> getMenuMap(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(Menu::getParentId));
    }

    /**
     * 获取目录列表
     * @param menuList 菜单列表
     * @return 目录列表
     */
    private List<Menu> listCatalog(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());
    }
}
