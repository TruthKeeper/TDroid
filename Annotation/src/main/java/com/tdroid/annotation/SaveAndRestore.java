package com.tdroid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *      author : TK
 *      time : 2018/1/16
 *      desc : 用于在Activity 和 Fragment onSaveInstanceState时自动保存数据，并在合适时机还原数据
 * </pre>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface SaveAndRestore {
    /**
     * 非基本类型时，通过Parcelable接口转换，优先级低于gson
     *
     * @return
     */
    boolean parcelable() default false;

    /**
     * 非基本类型时，通过Gson转换
     *
     * @return
     */
    boolean gson() default false;
}
