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
     * @param context
     * @return
     */
    public static int getVerCode(@NonNull Context context) {
        int verCode = -1;
        try {
            String packageName = context.getPackageName();
            verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 得到软件版本名称
     *
     * @param context
     * @return
     */
    public static String getVerName(@NonNull Context context) {
        String verName = "";
        try {
            String packageName = context.getPackageName();
            verName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


    /**
     * 安装apk
     *
     * @param context
     * @param file    APK文件
     */
    public static void installApk(@NonNull Context context, @NonNull File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    /**
     * 安装apk
     *
     * @param context
     * @param file    APK文件uri
     */
    public static void installApk(@NonNull Context context, Uri file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(file, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    /**
     * 卸载apk
     *
     * @param context
     * @param packageName 包名
     */
    public static void uninstallApk(@NonNull Context context, @NonNull String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }


    /**
     * 检测服务是否运行
     *
     * @param context   上下文
     * @param className 类名
     * @return 是否运行的状态
     */
    public static boolean isServiceRunning(@NonNull Context context, @NonNull String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isApplicationAvailable(@NonNull Context context, @NonNull String packageName) {
        final PackageManager packageManager = context.getPackageManager();
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
     * @param context
     * @return
     */
    public static boolean isApplicationDebug(@NonNull Context context) {
        if (context.getApplicationInfo() != null) {
            return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
        throw new RuntimeException("Application is null");
    }
}
