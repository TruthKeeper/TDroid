package com.tk.tdroid.utils;

import android.os.SystemClock;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/19
 *     desc   : View 代理工具类
 * </pre>
 */
public final class ViewProxyUtils {
    private ViewProxyUtils() {
        throw new IllegalStateException();
    }

    /**
     * 单击事件代理，默认两次点击
     * {@link ClickListenerProxy#INTERVAL 阈值时长(ms)}
     *
     * @param view
     */
    public static void clickProxy(@NonNull View view) {
        clickProxy(view, ClickListenerProxy.INTERVAL);
    }

    /**
     * 单击事件代理
     *
     * @param view
     * @param interval 两次点击{@link ClickListenerProxy#INTERVAL 阈值时长(ms)}
     */
    public static void clickProxy(@NonNull View view, @IntRange(from = 0) int interval) {
        try {
            Class viewClazz = Class.forName("android.view.View");
            Method method = viewClazz.getDeclaredMethod("getListenerInfo");
            method.setAccessible(true);

            Object listenerInfo = method.invoke(view);
            Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
            Field field = listenerInfoClazz.getDeclaredField("mOnClickListener");
            field.setAccessible(true);

            View.OnClickListener listener = (View.OnClickListener) field.get(listenerInfo);

            //偷梁换柱
            field.set(listenerInfo, new ClickListenerProxy(listener, interval));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单击事件的代理
     */
    public static class ClickListenerProxy implements View.OnClickListener {
        /**
         * ms
         */
        public static final int INTERVAL = 500;
        private View.OnClickListener onClickListener;
        private long lastTime;
        private final long interval;

        public ClickListenerProxy(@NonNull View.OnClickListener onClickListener) {
            this(onClickListener, INTERVAL);
        }

        public ClickListenerProxy(@NonNull View.OnClickListener onClickListener, long interval) {
            this.onClickListener = onClickListener;
            this.interval = interval;
        }

        @Override
        public void onClick(View v) {
            long currentTime = SystemClock.uptimeMillis();
            if (currentTime - lastTime > interval) {
                lastTime = currentTime;
                onClickListener.onClick(v);
            }
        }
    }
}
