package com.hua.common.enums;

/**
 * 用户地区类型枚举
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/17 12:48
 */
public enum UserAreaTypeEnum {

    /**
     * 用户
     */
    USER(1, "用户"),
    /**
     * 游客
     */
    VISITOR(2, "游客"),
    ;

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    UserAreaTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 获取用户区域类型
     *
     * @param type 类型
     * @return 用户区域类型枚举
     */
    public static UserAreaTypeEnum getUserAreaType(Integer type) {
        for (UserAreaTypeEnum value : UserAreaTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
