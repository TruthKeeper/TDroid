package com.tk.tdroid.utils;

import android.content.res.Resources;

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
     * dp -> px
     *
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue) {
        return Math.round(dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * px -> dp
     *
     * @param pxValue
     * @return
     */
    public static int px2dp(float pxValue) {
        return Math.round(pxValue / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * px -> sp
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        return Math.round(pxValue / Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    /**
     * sp -> px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        return Math.round(spValue * Resources.getSystem().getDisplayMetrics().scaledDensity);
    }
}