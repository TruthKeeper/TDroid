package com.tk.tdroid.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/18
 *      desc : 反射工具类
 * </pre>
 */

public final class ReflectUtils {
    private ReflectUtils() {
        throw new IllegalStateException();
    }

    /**
     * 获取泛型类
     *
     * @param cls   泛型类
     * @param index 获取通配符的索引
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> Class<T> getT(@NonNull final Class cls, final int index) {
        Class temp = cls;
        while (temp != null) {
            Type type = temp.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType pType = ((ParameterizedType) type);
                int length = pType.getActualTypeArguments().length;
                if (length > 0 && index < length) {
                    if (pType.getActualTypeArguments()[index] instanceof Class) {
                        return ((Class<T>) pType.getActualTypeArguments()[index]);
                    }
                }
            }
            temp = temp.getSuperclass();
        }
        return null;
    }
}
