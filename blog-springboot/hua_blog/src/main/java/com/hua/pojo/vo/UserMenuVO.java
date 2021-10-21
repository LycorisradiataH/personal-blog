package com.hua.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户菜单 vo
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 15:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMenuVO {

    /**
     * 菜单名
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 组件
     */
    private String component;

    /**
     * icon
     */
    private String icon;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    /**
     * 子菜单列表
     */
    private List<UserMenuVO> children;


}
