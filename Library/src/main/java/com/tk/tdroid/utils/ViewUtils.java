package com.tk.tdroid.utils;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/1
 *      desc : View工具类
 * </pre>
 */

public final class ViewUtils {
    private ViewUtils() {
        throw new IllegalStateException();
    }

    /**
     * 父容器是否为空
     *
     * @param viewGroup
     * @return
     */
    public static boolean isEmpty(@Nullable ViewGroup viewGroup) {
        return viewGroup == null || viewGroup.getChildCount() == 0;
    }

}