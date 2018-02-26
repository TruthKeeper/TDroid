package com.tdroid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *      author : TK
 *      time : 2018/1/26
 *      desc : 用于自动读取数据注入到Activity(By getIntent) , Fragment(By getArguments)中
 * </pre>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface AutoInject {
    /**
     * 传递的名称，默认就是变量名
     *
     * @return
     */
    String name() default "";

    /**
     * 非基本类型时，通过Parcelable接口转换
     *
     * @return
     */
    boolean parcelable() default false;

    /**
     * 是否严格模式，即读取为空直接抛出crash异常
     *
     * @return
     */
    boolean strict() default false;

    /**
     * 添加描述
     *
     * @return
     */
    String desc() default "";
}
