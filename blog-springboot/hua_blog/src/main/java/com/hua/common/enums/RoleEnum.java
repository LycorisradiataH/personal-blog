package com.hua.common.enums;

/**
 * 角色枚举类
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 14:00
 */
public enum RoleEnum {
    /**
     * 管理员
     */
    ADMIN(1, "管理员", "admin"),
    /**
     * 普通用户
     */
    USER(2, "用户", "user"),
    /**
     * 测试账号
     */
    TEST(3, "测试", "test")
    ;

    /**
     * 角色id
     */
    private final Integer roleId;

    /**
     * 描述
     */
    private final String name;

    /**
     * 权限标签
     */
    private final String label;

    RoleEnum(Integer roleId, String name, String label) {
        this.roleId = roleId;
        this.name = name;
        this.label = label;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

}
