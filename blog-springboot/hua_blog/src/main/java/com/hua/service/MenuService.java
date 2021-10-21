package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.entity.Menu;
import com.hua.pojo.vo.LabelOptionVO;
import com.hua.pojo.vo.MenuVO;
import com.hua.pojo.vo.UserMenuVO;
import com.hua.pojo.vo.param.MenuParam;
import com.hua.pojo.vo.param.QueryParam;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/16 16:33
 */
public interface MenuService extends IService<Menu> {

    /**
     * 查询菜单列表
     * @param queryParam 条件
     * @return 菜单列表
     */
    List<MenuVO> listMenu(QueryParam queryParam);

    /**
     * 新增或修改菜单
     * @param menuParam 菜单信息
     */
    void insertOrUpdateMenu(MenuParam menuParam);

    /**
     * 删除菜单
     * @param menuId 菜单id
     */
    void deleteMenu(Integer menuId);

    /**
     * 查询角色菜单选项
     * @return 角色菜单选项
     */
    List<LabelOptionVO> listMenuOption();

    /**
     * 查询用户菜单
     * @return 菜单列表
     */
    List<UserMenuVO> listUserMenu();
}
