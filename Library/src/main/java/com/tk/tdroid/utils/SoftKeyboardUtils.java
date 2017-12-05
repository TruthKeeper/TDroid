package com.tk.tdroid.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
     */
    public static void showSoftKeyboard(@NonNull View view) {
        InputMethodManager manager = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        manager.showSoftInput(view, 0);
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public static void hideSoftKeyboard(@NonNull View view) {
        InputMethodManager manager = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.clearFocus();
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 切换软键盘的显示
     */
    public static void toggleSoftInput() {
        InputMethodManager manager = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, 0);
    }
}
