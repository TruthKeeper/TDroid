package com.tk.tdroid.utils;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.PowerManager;
import android.support.annotation.RequiresPermission;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/03/17
 *     desc   : 屏幕工具类
 * </pre>
 */
public final class ScreenUtils {
    private ScreenUtils() {
        throw new IllegalStateException();
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * 唤醒屏幕
     *
     * @param unlockKeyguard
     */
    @SuppressWarnings("ConstantConditions")
    @RequiresPermission(allOf = {Manifest.permission.WAKE_LOCK,
            Manifest.permission.DISABLE_KEYGUARD})
    public static void wakeScreen(boolean unlockKeyguard) {
        // 获取电源管理器对象
        PowerManager powerManager = (PowerManager) Utils.getApp().getSystemService(Context.POWER_SERVICE);
        boolean screenOn = powerManager.isScreenOn();
        if (!screenOn) {
            PowerManager.WakeLock screenWakeLock = powerManager.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,
                    "bright");
            screenWakeLock.setReferenceCounted(false);
            screenWakeLock.acquire(1000);
            screenWakeLock.release();
        }
        if (unlockKeyguard) {
            KeyguardManager keyguardManager = (KeyguardManager) Utils.getApp().getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
            keyguardLock.reenableKeyguard();
            keyguardLock.disableKeyguard();
        }
    }
}
