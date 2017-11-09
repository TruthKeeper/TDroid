package com.tk.tdroid.utils;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/7
 *      desc : 注意App内部文件目录会导致无权限打开！
 * </pre>
 */

public final class IntentUtils {
    private IntentUtils() {
        throw new IllegalStateException();
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
     * @param phone
     * @param immediately
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
}
