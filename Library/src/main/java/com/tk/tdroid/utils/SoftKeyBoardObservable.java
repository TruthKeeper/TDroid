package com.tk.tdroid.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/9/6
 *     desc   : 软键盘观察者
 * </pre>
 */
public class SoftKeyBoardObservable implements View.OnLayoutChangeListener {
    private static final String TAG = "SoftKeyBoardObservable";
    private static final String HEIGHT = "height";
    private static final float DEFAULT_HEIGHT = 0.4f;

    private final View decorView;
    private boolean keyboardShow;
    private OnSoftKeyboardListener onSoftKeyboardListener;

    public SoftKeyBoardObservable(@NonNull View decorView) {
        this.decorView = decorView;
    }

    public boolean isKeyboardShow() {
        return keyboardShow;
    }

    public void setOnSoftKeyboardListener(OnSoftKeyboardListener onSoftKeyboardListener) {
        this.onSoftKeyboardListener = onSoftKeyboardListener;
    }

    public static void initPanelView(@NonNull View panelView) {
        ViewGroup.LayoutParams params = panelView.getLayoutParams();
        params.height = SoftKeyBoardObservable.getKeyboardHeight();
        panelView.setLayoutParams(params);
    }

    public void register() {
        if (decorView != null) {
            decorView.addOnLayoutChangeListener(this);
        }
    }

    public void unregister() {
        if (decorView != null) {
            decorView.removeOnLayoutChangeListener(this);
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Rect rect = new Rect();
        //获取main在窗体的可视区域
        decorView.getWindowVisibleDisplayFrame(rect);
        //获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
        int mainInvisibleHeight = decorView.getRootView().getHeight() - rect.bottom;
        int screenHeight = decorView.getRootView().getHeight();//屏幕高度
        //不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
        boolean show = mainInvisibleHeight > screenHeight / 4;
        if (show == keyboardShow) {
            return;
        }
        keyboardShow = show;
        if (keyboardShow) {
            //软键盘显示了
            Log.e("onLayoutChange", "show:" + mainInvisibleHeight);
            saveKeyboardHeight(mainInvisibleHeight);
            if (onSoftKeyboardListener != null) {
                onSoftKeyboardListener.onSoftKeyboardShow(mainInvisibleHeight);
            }
        } else {
            //软键盘隐藏了
            Log.e("onLayoutChange", "dismiss");
            if (onSoftKeyboardListener != null) {
                onSoftKeyboardListener.onSoftKeyboardDismiss();
            }
        }
    }

    /**
     * 记录软键盘高度
     *
     * @param height
     */
    public static void saveKeyboardHeight(int height) {
        Utils.getApp().getSharedPreferences(TAG, Context.MODE_PRIVATE)
                .edit()
                .putInt(HEIGHT, height)
                .commit();
    }

    /**
     * 获取键盘高度
     *
     * @return
     */
    public static int getKeyboardHeight() {
        int height = Utils.getApp().getSharedPreferences(TAG, Context.MODE_PRIVATE)
                .getInt(HEIGHT, 0);
        if (0 >= height) {
            //默认的软键盘高度
            return (int) (Utils.getApp().getResources().getDisplayMetrics().heightPixels * DEFAULT_HEIGHT);
        }
        return height;
    }

    public interface OnSoftKeyboardListener {
        /**
         * 软键盘显示了
         *
         * @param softKeyboardHeight
         */
        void onSoftKeyboardShow(int softKeyboardHeight);

        /**
         * 软键盘隐藏了
         */
        void onSoftKeyboardDismiss();
    }
}
