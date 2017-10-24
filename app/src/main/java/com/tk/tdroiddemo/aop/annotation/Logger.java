package com.tk.tdroiddemo.aop.annotation;

import android.util.Log;

import com.tk.tdroiddemo.aop.aspect.LoggerAspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/23
 *      desc : 修饰类、构造方法、方法，打印参数、方法耗时
 * </pre>
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Logger {
    /**
     * Logger的类型
     *
     * @return Log类型
     * <ul>
     * <li>{@link Log#VERBOSE}</li>
     * <li>{@link Log#DEBUG}</li>
     * <li>{@link Log#INFO}</li>
     * <li>{@link Log#WARN}</li>
     * <li>{@link Log#ERROR}</li>
     * <li>{@link Log#ASSERT}</li>
     * </ul>
     */
    int type() default Log.DEBUG;

    /**
     * tag
     *
     * @return
     */
    String tag() default LoggerAspect.TAG;
}
