package com.tk.tdroid.utils;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/7
 *      desc : 常见意图工具类；
 *      注意App内部文件目录会导致无权限打开！
 * </pre>
 */

public final class IntentUtils {
    private IntentUtils() {
        throw new IllegalStateException();
    }

    /**
     * 安装apk
     *
     * @param file APK文件
     */
    public static void installApk(@NonNull File file) {
        if (!FileUtils.exist(file)) {
            return;
        }
        installApk(Uri.fromFile(file));
    }


    /**
     * 安装apk
     *
     * @param file APK文件uri
     */
    public static void installApk(@NonNull Uri file) {
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
     * 搜索
     *
     * @param url
     */
    public static void toBrowser(@NonNull String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getApp().startActivity(intent);
    }

    /**
     * 拨打电话
     *
     * @param phone       电话
     * @param immediately 立即拨出
     */
    public static void toCallPhone(@NonNull String phone, boolean immediately) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(immediately ? Intent.ACTION_CALL : Intent.ACTION_DIAL, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getApp().startActivity(intent);
    }

    /**
     * 打开文件
     *
     * @param file
     * @return
     */
    public static boolean openFile(@NonNull File file) {
        if (!FileUtils.exist(file) || file.isDirectory()) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = FileUtils.getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        if (isSafeActivity(intent)) {
            Utils.getApp().startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 打开文件
     *
     * @param uri
     * @return
     */
    public static boolean openFile(@NonNull Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = FileUtils.getMIMEType(uri.getLastPathSegment());
        intent.setDataAndType(uri, type);
        if (isSafeActivity(intent)) {
            Utils.getApp().startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 是否是一个可被开启的Activity
     *
     * @param intent
     * @return
     */
    public static boolean isSafeActivity(@NonNull Intent intent) {
        List<ResolveInfo> result = Utils.getApp().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !EmptyUtils.isEmpty(result);
    }

    /**
     * 是否是一个可被开启的Service
     *
     * @param intent
     * @return
     */
    public static boolean isSafeService(@NonNull Intent intent) {
        List<ResolveInfo> result = Utils.getApp().getPackageManager().queryIntentServices(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !EmptyUtils.isEmpty(result);
    }

    /**
     * 适配Android 7.0的文件访问权限
     *
     * @param intent
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static Intent wrapperIntent(@NonNull Intent intent) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    /**
     * 跳转本应用的设置中心
     *
     * @return
     */
    public static void toSetting() {
        toSetting(Utils.getApp().getPackageName());
    }

    /**
     * 跳转某个应用的设置中心
     *
     * @param packageName
     * @return
     */
    public static void toSetting(String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts("package", packageName, null));
        Utils.getApp().startActivity(intent);
    }

    /**
     * 跳转到通知栏设置权限
     */
    public static void toNotifySetting() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra("app_package", Utils.getApp().getPackageName());
            intent.putExtra("app_uid", Binder.getCallingUid());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Utils.getApp().startActivity(intent);
        } else {
            toSetting();
        }
    }
}
