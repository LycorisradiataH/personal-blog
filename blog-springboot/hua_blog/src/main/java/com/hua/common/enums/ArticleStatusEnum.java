package com.hua.common.enums;

/**
 * 文章状态枚举
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/17 17:10
 */
public enum ArticleStatusEnum {
    /**
     * 公开
     */
    PUBLIC(1, "公开"),
    /**
     * 私密
     */
    PRIVATE(2, "私密"),
    /**
     * 草稿
     */
    DRAFT(3, "草稿");
    ;

    /**
     * 状态
     */
    private final Integer status;

    /**
     * 描述
     */
    private final String desc;

    ArticleStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
