package com.tk.tdroid.widget.image;

import android.widget.ImageView;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/05
 *     desc   : xxxx描述
 * </pre>
 */
@GlideExtension
public class GlideAPI {
    private GlideAPI() {
    }

    /**
     * 节省内存
     *
     * @param options
     * @param imageView
     */
    @GlideOption
    public static void override(RequestOptions options, ImageView imageView) {
        if (imageView.getMeasuredWidth() != 0 && imageView.getMeasuredHeight() != 0) {
            options = options.override(imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        }
    }
}
