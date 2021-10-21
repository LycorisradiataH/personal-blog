package com.hua.common.enums;

/**
 * 时区枚举
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/17 14:54
 */
public enum ZoneEnum {

    /**
     * 上海
     */
    SHANGHAI("Asia/Shanghai", "中国上海");

    /**
     * 时区
     */
    private final String zone;

    /**
     * 描述
     */
    private final String desc;

    ZoneEnum(String zone, String desc) {
        this.zone = zone;
        this.desc = desc;
    }

    public String getZone() {
        return zone;
    }

    public String getDesc() {
        return desc;
    }

}
