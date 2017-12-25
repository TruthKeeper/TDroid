package com.tk.tdroid.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/19
 *      desc : 截屏工具类
 * </pre>
 */

public final class ScreenShotUtils {
    private ScreenShotUtils() {
        throw new IllegalStateException();
    }

    /**
     * 通过Activity,不包括通知栏
     *
     * @param activity
     * @return
     */
    public static Bitmap byActivity(@NonNull Activity activity) {
        return byView(activity.getWindow().getDecorView());
    }

    /**
     * 通过View
     *
     * @param view
     * @return
     */
    public static Bitmap byView(@NonNull View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getWidth(), view.getHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 通过ScrollView
     *
     * @param scrollView
     * @param fullScrollView 是否完整绘制ScrollView，即内部View高度小于ScrollView高度时
     * @return
     */
    public static Bitmap byScrollView(@NonNull ScrollView scrollView, boolean fullScrollView) {
        int totalH = 0;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            totalH += scrollView.getChildAt(i).getHeight();
        }
        if (fullScrollView) {
            totalH = Math.max(totalH, scrollView.getHeight());
        }
        Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(), totalH, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 通过HorizontalScrollView
     *
     * @param scrollView
     * @param fullScrollView 是否完整绘制HorizontalScrollView，即内部View宽度小于HorizontalScrollView宽度时
     * @return
     */
    public static Bitmap byScrollView(@NonNull HorizontalScrollView scrollView, boolean fullScrollView) {
        int totalW = 0;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            totalW += scrollView.getChildAt(i).getWidth();
        }
        if (fullScrollView) {
            totalW = Math.max(totalW, scrollView.getWidth());
        }
        Bitmap bitmap = Bitmap.createBitmap(totalW, scrollView.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 通过NestedScrollView
     *
     * @param scrollView
     * @param fullScrollView 是否完整绘制NestedScrollView，即内部View高度小于NestedScrollView高度时
     * @return
     */
    public static Bitmap byScrollView(@NonNull NestedScrollView scrollView, boolean fullScrollView) {
        int totalH = 0;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            totalH += scrollView.getChildAt(i).getHeight();
        }
        if (fullScrollView) {
            totalH = Math.max(totalH, scrollView.getHeight());
        }
        Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(), totalH, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

}
