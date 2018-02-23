package com.tk.tdroid.utils;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.regex.Pattern;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/14
 *      desc : Url工具类
 * </pre>
 */

public final class UrlUtils {
    private UrlUtils() {
        throw new IllegalStateException();
    }

    /**
     * 是否是Url，例如{@code https://www.github.com/}
     *
     * @param url
     * @return
     */
    public static boolean isUrl(@Nullable CharSequence url) {
        return !EmptyUtils.isEmpty(url) && Pattern.matches("[a-zA-z]+://[^\\s]*", url);
    }

    /**
     * 排除查询参数
     *
     * @param url
     * @return
     */
    public static String excludeQuery(@NonNull String url) {
        final Uri uri = Uri.parse(url);
        final StringBuilder builder = new StringBuilder();
        if (!EmptyUtils.isEmpty(uri.getScheme())) {
            builder.append(uri.getScheme());
            builder.append("://");
        }
        if (!EmptyUtils.isEmpty(uri.getAuthority())) {
            builder.append(uri.getAuthority());
        }
        if (!EmptyUtils.isEmpty(uri.getPath())) {
            builder.append(uri.getPath());
        }
        return builder.toString();
    }
}
