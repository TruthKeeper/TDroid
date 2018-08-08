package com.tk.tdroid.view.viewpager;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/20
 *      desc : 随条目高度自适应
 * </pre>
 */

public class FitItemViewPager extends DisableViewPager {
    private final SparseArrayCompat<View> children = new SparseArrayCompat<>();

    public FitItemViewPager(Context context) {
        this(context, null);
    }

    public FitItemViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDisabled(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int currentItem = getCurrentItem();
        int height = 0;
        if (children.size() > currentItem) {
            View child = children.get(currentItem);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = child.getMeasuredHeight();
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    /**
     * 绑定View和ViewPager Position的映射关系
     *
     * @param position
     * @param view
     */
    public void putChildren(int position, View view) {
        children.put(position, view);
    }
}
