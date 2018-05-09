package com.tk.tdroid.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/4
 *      desc : 软键盘工具类
 * </pre>
 */

public final class SoftKeyboardUtils {
    /**
     * 显示软键盘
     *
     * @param view
     * @param requestFocus
     */
    public static void showSoftKeyboard(@NonNull View view, boolean requestFocus) {
        InputMethodManager manager = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (requestFocus) {
            view.requestFocus();
        }
        manager.showSoftInput(view, 0);
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     * @param clearFocus
     */
    public static void hideSoftKeyboard(@NonNull View view, boolean clearFocus) {
        InputMethodManager manager = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (clearFocus) {
            view.clearFocus();
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 切换软键盘的显示
     */
    public static void toggleSoftInput() {
        InputMethodManager manager = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, 0);
    }

    /**
     * 代理
     *
     * @param activity
     * @param ev
     * @param filterViews
     * @return
     */
    public static void delegateDispatchTouchEvent(Activity activity, MotionEvent ev, View[] filterViews) {
        if (ev.getAction() != MotionEvent.ACTION_DOWN) {
            return;
        }
        final View currentFocusView = activity.getCurrentFocus();
        final View decorView = activity.getWindow().getDecorView();
        if (currentFocusView == null || !isInSpace(decorView, ev)) {
            return;
        }
        if (decorView instanceof ViewGroup) {
            final View view = getTouchChild((ViewGroup) decorView, ev);
            if (view != null && (view instanceof EditText || CollectionUtils.searchFirstIndex(filterViews, view) != -1)) {
                //点击的是EditText或者需要过滤
            } else {
                hideSoftKeyboard(currentFocusView, false);
            }
        }
    }

    /**
     * 获取接受到事件的View
     *
     * @param viewGroup
     * @param ev
     * @return
     */
    private static View getTouchChild(ViewGroup viewGroup, MotionEvent ev) {
        View child = null;
        for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
            child = viewGroup.getChildAt(i);
            if (isInSpace(child, ev)) {
                if (child instanceof ViewGroup) {
                    View v = getTouchChild((ViewGroup) child, ev);
                    return v == null ? child : v;
                } else {
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * 触摸位置是否处于控件区域中
     *
     * @param view
     * @param event
     * @return
     */
    private static boolean isInSpace(View view, MotionEvent event) {
        if (view.getVisibility() != View.VISIBLE) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int left = location[0];
        int top = location[1];
        int bottom = top + view.getHeight();
        int right = left + view.getWidth();
        return event.getX() > left
                && event.getX() < right
                && event.getY() > top
                && event.getY() < bottom;
    }

}
