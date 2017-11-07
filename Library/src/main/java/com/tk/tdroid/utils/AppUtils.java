package com.tk.tdroid.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.tk.tdroid.BuildConfig;

import java.io.File;
import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/09/09
 *     desc   : App工具类
 * </pre>
 */
public final class AppUtils {
    private static boolean isDebug = BuildConfig.DEBUG;

    private AppUtils() {
        throw new IllegalStateException();
    }

    /**
     * 得到软件版本号
     *
     * @return
     */
    public static int getVerCode() {
        int verCode = -1;
        try {
            String packageName = Utils.getApp().getPackageName();
            verCode = Utils.getApp().getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 得到软件版本名称
     *
     * @return
     */
    public static String getVerName() {
        String verName = "";
        try {
            String packageName = Utils.getApp().getPackageName();
            verName = Utils.getApp().getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


    /**
     * 安装apk
     *
     * @param file APK文件
     */
    public static void installApk(@NonNull File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        Utils.getApp().startActivity(intent);
    }


    /**
     * 安装apk
     *
     * @param file APK文件uri
     */
    public static void installApk(Uri file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(file, "application/vnd.android.package-archive");
        Utils.getApp().startActivity(intent);
    }


    /**
     * 卸载apk
     *
     * @param packageName 包名
     */
    public static void uninstallApk(@NonNull String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        Utils.getApp().startActivity(intent);
    }


    /**
     * 检测服务是否运行
     *
     * @param className 类名
     * @return 是否运行的状态
     */
    public static boolean isServiceRunning(@NonNull String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : servicesList) {
            if (className.equals(info.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 是否安装了某应用
     *
     * @param packageName
     * @return
     */
    public static boolean isApplicationAvailable(@NonNull String packageName) {
        final PackageManager packageManager = Utils.getApp().getPackageManager();
        List<PackageInfo> infoList = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : infoList) {
            if (packageInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取应用运行的最大内存
     *
     * @return 最大内存，单位 B
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * 获取手机系统SDK版本号
     *
     * @return 例如21
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 开发是否是debug模式
     *
     * @return
     */
    public static boolean isDebug() {
        return isDebug;
    }

    /**
     * 应用是否是debug版本
     *
     * @return
     */
    public static boolean isApplicationDebug() {
        if (Utils.getApp().getApplicationInfo() != null) {
            return (Utils.getApp().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
        throw new RuntimeException("Application is null");
    }
}
