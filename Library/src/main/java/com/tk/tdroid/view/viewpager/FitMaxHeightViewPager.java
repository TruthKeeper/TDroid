package com.tk.tdroid.view.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * <pre>
 *      author : TK
 *      time : 2018/7/26
 *      desc : 取最大高度的ViewPager
 * </pre>
 */

public class FitMaxHeightViewPager extends DisableViewPager {

    public FitMaxHeightViewPager(Context context) {
        this(context, null);
    }

    public FitMaxHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDisabled(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = Math.max(height, child.getMeasuredHeight());
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
}
