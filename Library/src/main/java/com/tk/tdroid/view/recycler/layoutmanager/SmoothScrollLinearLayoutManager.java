package com.tk.tdroid.view.recycler.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/1
 *      desc : 使RecyclerView支持平滑滚动的速率调整
 * </pre>
 */

public class SmoothScrollLinearLayoutManager extends LinearLayoutManager {
    /**
     * 1英寸100ms
     */
    public static float DEFAULT = 100F;
    private float milliInch;

    public static SmoothScrollLinearLayoutManager create(Context context) {
        return new SmoothScrollLinearLayoutManager(context, VERTICAL, false, DEFAULT);
    }

    public static SmoothScrollLinearLayoutManager create(Context context, float milliInch) {
        return new SmoothScrollLinearLayoutManager(context, VERTICAL, false, milliInch);
    }

    public static SmoothScrollLinearLayoutManager create(Context context, int orientation, boolean reverseLayout) {
        return new SmoothScrollLinearLayoutManager(context, orientation, reverseLayout, DEFAULT);
    }

    public static SmoothScrollLinearLayoutManager create(Context context, int orientation, boolean reverseLayout, float milliInch) {
        return new SmoothScrollLinearLayoutManager(context, orientation, reverseLayout, milliInch);
    }

    private SmoothScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout, float milliInch) {
        super(context, orientation, reverseLayout);
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
