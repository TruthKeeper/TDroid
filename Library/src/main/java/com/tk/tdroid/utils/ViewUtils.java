package com.tk.tdroid.utils;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/1
 *      desc :
 * </pre>
 */

public final class ViewUtils {
    private ViewUtils() {
        throw new IllegalStateException();
    }

    public static boolean isEmpty(@Nullable ViewGroup viewGroup) {
        return viewGroup == null || viewGroup.getChildCount() == 0;
    }
}