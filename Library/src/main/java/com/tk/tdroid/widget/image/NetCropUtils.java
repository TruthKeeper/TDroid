package com.tk.tdroid.widget.image;

import android.net.Uri;
import android.widget.ImageView;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/04
 *     desc   : 网络裁剪工具
 * </pre>
 * <ul>
 * <li><a href="https://cloud.tencent.com/document/product/460/6929">腾讯万象优图<a/></li>
 * <li><a href="https://developer.qiniu.com/dora/manual/1279/basic-processing-images-imageview2">七牛</a></li>
 * <ul/>
 */
public final class NetCropUtils {
    private NetCropUtils() {
        throw new IllegalStateException();
    }

    /**
     * 第三方API处理裁剪
     *
     * @param url
     * @param imageView
     * @return
     */
    public static String wrap(String url, ImageView imageView) {
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
    public static Uri wrap(Uri url, ImageView imageView) {
        if (imageView.getMeasuredWidth() == 0 || imageView.getMeasuredHeight() == 0) {
            return url;

        }
        return Uri.parse(url.toString() + "");
    }
}
