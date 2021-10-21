package com.hua.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 复制对象或集合属性
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 18:36
 */
public class BeanCopyUtils {

    /**
     * 根据现有对象的属性创建目标对象，并赋值
     * @param source 源对象
     * @param target 目标类型
     * @param <T> 未知类型
     * @return 已赋值的目标对象
     */
    public static <T> T copyObject(Object source, Class<T> target) {
        T temp = null;
        try {
            temp = target.getDeclaredConstructor().newInstance();
            if (null != source) {
                BeanUtils.copyProperties(source, temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 拷贝集合
     * @param source 源集合
     * @param target 目标类型
     * @param <T> 源类型
     * @param <S> 目标类型
     * @return 目标集合
     */
    public static <T, S> List<T> copyList(List<S> source, Class<T> target) {
        List<T> list = new ArrayList<>();
        if (null != source && source.size() > 0) {
            for (Object obj : source) {
                list.add(copyObject(obj, target));
            }
        }
        return list;
    }

}
