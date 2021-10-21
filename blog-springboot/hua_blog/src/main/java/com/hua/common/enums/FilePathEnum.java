package com.hua.common.enums;

/**
 * 文件上传路径枚举
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 15:41
 */
public enum FilePathEnum {
    /**
     * 头像路径
     */
    AVATAR("avatar/", "头像路径"),
    /**
     * 文章路径
     */
    ARTICLE("article/", "文章路径"),
    /**
     * 配置图片路径
     */
    CONFIG("config/","配置图片路径")
    ;

    private final String path;

    private final String desc;

    FilePathEnum(String path, String desc) {
        this.path = path;
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public String getDesc() {
        return desc;
    }

}
