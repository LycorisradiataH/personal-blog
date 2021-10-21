package com.hua.util;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/12 21:41
 */
public class HtmlUtils {

    /**
     * 删除标签
     * @param source 需要进行剔除Html标签的文本
     * @return 过滤后的内容
     */
    public static String deleteTag(String source) {
        // 保留图片标签
        source = source.replaceAll("(?!<(img).*?>)<.*?>", "");
        return deleteHTMLTag(source);
    }

    /**
     * 删除标签
     * @param source 文本
     * @return 过滤后的文本
     */
    private static String deleteHTMLTag(String source) {
        // 删除转义字符
        source = source.replaceAll("&.{2,6}?;", "");
        // 删除script标签
        source = source.replaceAll("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", "");
        // 删除style标签
        source = source.replaceAll("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", "");
        return source;
    }

}
