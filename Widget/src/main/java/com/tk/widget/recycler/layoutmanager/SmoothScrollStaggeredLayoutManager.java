package com.tk.widget.recycler.layoutmanager;

import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/1
 *      desc : 使RecyclerView支持平滑滚动的速率调整
 * </pre>
 */

public class SmoothScrollStaggeredLayoutManager extends StaggeredGridLayoutManager {
    /**
     * 1英寸100ms
     */
    public static float DEFAULT = 100F;
    private float milliInch;

    public static SmoothScrollStaggeredLayoutManager create(int spanCount, int orientation) {
        return new SmoothScrollStaggeredLayoutManager(spanCount, orientation, DEFAULT);
    }

    public static SmoothScrollStaggeredLayoutManager create(int spanCount, int orientation, float milliInch) {
        return new SmoothScrollStaggeredLayoutManager(spanCount, orientation, milliInch);
    }

    private SmoothScrollStaggeredLayoutManager(int spanCount, int orientation, float milliInch) {
        super(spanCount, orientation);
        this.milliInch = milliInch;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller smoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return milliInch / displayMetrics.densityDpi;
                    }
                };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
