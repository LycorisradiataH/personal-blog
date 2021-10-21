package com.hua.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/15 22:49
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {

    /**
     * @return 操作类型
     */
    String optType() default "";

}
