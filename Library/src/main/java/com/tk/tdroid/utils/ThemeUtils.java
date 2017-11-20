package com.tk.tdroid.utils;

import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/8
 *      desc : 主题的配置工具类
 * </pre>
 */

public final class ThemeUtils {
    private ThemeUtils() {
        throw new IllegalStateException();
    }

    /**
     * 获取ActionBar高度
     *
     * @param activity
     * @return
     */
    public static int getActionBarSize(@NonNull AppCompatActivity activity) {
        TypedValue value = new TypedValue();
        activity.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, value, true);
        return TypedValue.complexToDimensionPixelOffset(value.data, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 获取默认沉浸式颜色
     *
     * @param activity
     * @return
     */
    @ColorInt
    public static int getColorPrimary(@NonNull AppCompatActivity activity) {
        TypedValue value = new TypedValue();
        activity.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.colorPrimary, value, true);
        return ContextCompat.getColor(activity, value.resourceId);
    }

    /**
     * 获取默认沉浸式颜色
     *
     * @param activity
     * @return
     */
    @ColorInt
    public static int getColorPrimaryDark(@NonNull AppCompatActivity activity) {
        TypedValue value = new TypedValue();
        activity.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.colorPrimaryDark, value, true);
        return ContextCompat.getColor(activity, value.resourceId);
    }
}
