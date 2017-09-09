package com.tk.tdroid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/03/17
 *     desc   : 网络工具
 * </pre>
 */
public final class NetworkUtils {
    private NetworkUtils() {
        throw new IllegalStateException();
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     */
    public static boolean isNetAvailable(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        return current.isAvailable();
    }
}
