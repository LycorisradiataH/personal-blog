package com.hua.common.constant;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * 公共常量
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 23:01
 */
public class CommonConst {

    /**
     * 否
     */
    public static final int FALSE = 0;

    /**
     * 是
     */
    public static final int TRUE = 1;

    /**
     * 高亮标签
     */
    public static final String PRE_TAG = "<span style='color:#f47466'>";

    /**
     * 高亮标签
     */
    public static final String POST_TAG = "</span>";

    /**
     * 当前页码
     */
    public static final String CURRENT = "current";

    /**
     * 页码条数
     */
    public static final String SIZE = "size";

    /**
     * 默认条数
     */
    public static final String DEFAULT_SIZE = "10";

    /**
     * 默认用户昵称
     */
    public static final String DEFAULT_NICKNAME = "用户" + IdWorker.getId();

    /**
     * 默认用户头像
     */
    public static final String DEFAULT_AVATAR = "http://OSS地址/config/tourist.png";

    /**
     * 浏览文章集合
     */
    public static String ARTICLE_SET = "articleSet";

    /**
     * 前端组件名
     */
    public static final String COMPONENT = "layout";

    /**
     * 文章页面路径
     */
    public static final String ARTICLE_PATH = "/article/";

    /**
     * 省
     */
    public static final String PROVINCE = "省";

    /**
     * 市
     */
    public static final String CITY = "市";

    /**
     * 未知的
     */
    public static final String UNKNOWN = "未知";

}
