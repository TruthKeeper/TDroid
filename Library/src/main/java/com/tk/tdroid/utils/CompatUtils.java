package com.tk.tdroid.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <pre>
 *     author : Administrator
 *     time   : 2019/3/26 11:16
 *     desc   : 兼容工具类
 * </pre>
 */
public class CompatUtils {
     private static Field sField_TN;
    private static Field sField_TN_Handler;
    private static boolean sWatchDog = false;

    /**
     * hook{@link Toast}的实现，修复Toast在7.x版本的{@link android.view.WindowManager.BadTokenException}
     *
     * @param toast
     */
    @MainThread
    public static void hookToast(@NonNull Toast toast) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return;
        }
        if (sField_TN == null) {
            //初始化一次
            try {
                sField_TN = Toast.class.getDeclaredField("mTN");
                sField_TN.setAccessible(true);
                sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
                sField_TN_Handler.setAccessible(true);

            } catch (Exception e) {
            }
        }
        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            //修饰一个拦截异常的Handler
            sField_TN_Handler.set(tn, new SafelyHandlerWrapper(preHandler));
        } catch (Exception e) {
        }
    }

    /**
     * 停止抛出FinalizerWatchdogDaemon导致的{@link java.util.concurrent.TimeoutException}
     * 建议在debug包或者灰度包中不要stop，保留发现问题的能力
     */
    public static void stopWatchDog(boolean debug) {
        if (debug || sWatchDog) {
            return;
        }
        // Android P 以后不能反射FinalizerWatchdogDaemon
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return;
        }
        sWatchDog = true;

        try {
            final Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            final Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            final Object watchdog = field.get(null);
            try {
                final Field thread = clazz.getSuperclass().getDeclaredField("thread");
                thread.setAccessible(true);
                thread.set(watchdog, null);
            } catch (Exception e1) {
                e1.printStackTrace();
                try {
                    // 直接调用stop方法，在Android 6.0之前会有线程安全问题
                    final Method method = clazz.getSuperclass().getDeclaredMethod("stop");
                    method.setAccessible(true);
                    method.invoke(watchdog);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        //使之生效
        Runtime.getRuntime().gc();
        System.runFinalization();
    }

    private static class SafelyHandlerWrapper extends Handler {
        private Handler impl;

        SafelyHandlerWrapper(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
            }
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);
        }
    }
}
