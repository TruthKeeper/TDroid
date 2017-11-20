package com.tk.tdroid.utils;

import android.content.res.Resources;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/03/17
 *     desc   : 屏幕工具类
 * </pre>
 */
public final class ScreenUtils {
    private ScreenUtils() {
        throw new IllegalStateException();
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
