package com.tk.tdroid.utils;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.SparseArrayCompat;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.util.Collection;
import java.util.Map;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/6
 *      desc : 判空工具类
 * </pre>
 */

public final class EmptyUtils {
    private EmptyUtils() {
        throw new IllegalStateException();
    }

    /**
     * @param charSequence
     * @return
     */
    public static boolean isEmpty(@Nullable CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(@Nullable Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * @param collection
     * @return
     */
    public static boolean isEmpty(@Nullable Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * @param map
     * @return
     */
    public static boolean isEmpty(@Nullable Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * @param arrayMap
     * @return
     */
    public static boolean isEmpty(@Nullable SimpleArrayMap arrayMap) {
        return arrayMap == null || arrayMap.isEmpty();
    }

    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(@Nullable SparseArray array) {
        return array == null || array.size() == 0;
    }

    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(@Nullable SparseArrayCompat array) {
        return array == null || array.size() == 0;
    }

    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(@Nullable SparseBooleanArray array) {
        return array == null || array.size() == 0;
    }

    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(@Nullable SparseIntArray array) {
        return array == null || array.size() == 0;
    }

    /**
     * @param array
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static boolean isEmpty(@Nullable SparseLongArray array) {
        return array == null || array.size() == 0;
    }

    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(@Nullable android.support.v4.util.LongSparseArray array) {
        return array == null || array.size() == 0;
    }

    /**
     * @param object
     * @return
     */
    public static boolean isEmpty(@Nullable Object object) {
        return object == null;
    }
}
