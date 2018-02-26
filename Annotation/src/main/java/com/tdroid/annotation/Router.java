package com.tdroid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/24
 *     desc   : 路由
 * </pre>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Router {
    /**
     * 路由的链接，唯一标识
     * @return
     */
    String[] path();
}
