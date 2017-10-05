package com.tk.tdroid.widget.image;

import android.net.Uri;
import android.widget.ImageView;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/04
 *     desc   : xxxx描述
 * </pre>
 */
public final class NetImageUtils {
    private NetImageUtils() {
        throw new IllegalStateException();
    }

    /**
     * 第三方API处理裁剪
     *
     * @param url
     * @param imageView
     * @return
     */
    public static String netCrop(String url, ImageView imageView) {
        if (imageView.getMeasuredWidth() == 0 || imageView.getMeasuredHeight() == 0) {
            return url;
        }
        return url + "";
    }

    /**
     * 第三方API处理裁剪
     *
     * @param url
     * @param imageView
     * @return
     */
    public static Uri netCrop(Uri url, ImageView imageView) {
        if (imageView.getMeasuredWidth() == 0 || imageView.getMeasuredHeight() == 0) {
            return url;

        }
        return Uri.parse(url.toString() + "");
    }
}
