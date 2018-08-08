package com.tk.tdroid.view.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <pre>
 *      author : TK
 *      time : 2017/7/5
 *      desc :
 * </pre>
 */

public class DisableViewPager extends ViewPager {
    private boolean disabled = true;

    public DisableViewPager(Context context) {
        this(context, null);
    }

    public DisableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !disabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !disabled && super.onTouchEvent(ev);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }
}
