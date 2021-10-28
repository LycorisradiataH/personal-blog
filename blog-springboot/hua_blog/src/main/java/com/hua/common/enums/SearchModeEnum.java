package com.hua.common.enums;

/**
 * 搜索模式枚举
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/21 17:22
 */
public enum SearchModeEnum {

    /**
     * mysql
     */
    MYSQL("mysql", "mySqlSearchStrategyImpl"),
    /**
     * elasticsearch
     */
    ELASTICSEARCH("elasticsearch", "esSearchStrategyImpl")
    ;

    /**
     * 模式
     */
    private final String mode;

    /**
     * 策略
     */
    private final String strategy;

    SearchModeEnum(String mode, String strategy) {
        this.mode = mode;
        this.strategy = strategy;
    }

    public String getMode() {
        return mode;
    }

    public String getStrategy() {
        return strategy;
    }

    public static String getStrategy(String mode) {
        for (SearchModeEnum value : SearchModeEnum.values()) {
            if (value.getMode().equals(mode)) {
                return value.getStrategy();
            }
        }
        return null;
    }

}
