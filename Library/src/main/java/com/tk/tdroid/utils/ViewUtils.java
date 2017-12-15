package com.tk.tdroid.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
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

    /**
     * view 打印成 bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap view2Bitmap(@NonNull View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

}