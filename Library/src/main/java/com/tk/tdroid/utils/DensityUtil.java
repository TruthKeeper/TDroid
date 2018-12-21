package com.tk.tdroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/03/16
 *     desc   : 像素转换工具
 * </pre>
 */
public final class DensityUtil {

    private DensityUtil() {
        throw new IllegalStateException();
    }

    /**
     * dp > px
     *
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dpValue * density);
    }

    /**
     * dp > px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(@NonNull Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dpValue * density);
    }

    /**
     * px > dp
     *
     * @param pxValue
     * @return
     */
    public static int px2dp(float pxValue) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(pxValue / density);
    }

    /**
     * px > dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(@NonNull Context context, float pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(pxValue / density);
    }

    /**
     * px > sp
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return Math.round(pxValue / fontScale);
    }

    /**
     * px > sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(@NonNull Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return Math.round(pxValue / fontScale);
    }

    /**
     * sp > px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return Math.round(spValue * fontScale);
    }

    /**
     * sp > px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(@NonNull Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return Math.round(spValue * fontScale);
    }
}