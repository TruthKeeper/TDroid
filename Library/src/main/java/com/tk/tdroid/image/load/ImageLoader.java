package com.tk.tdroid.image.load;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.tk.tdroid.image.glide.GlideImpl;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/12/22
 *     desc   : 封装的图像加载工具
 * </pre>
 */
public final class ImageLoader {
    private ImageLoader() {
        throw new IllegalStateException();
    }

    private static ILoader ILoader;

    /**
     * 初始化
     */
    public static void init() {
        init(new GlideImpl(), new Config.Builder().build());
    }

    /**
     * 初始化
     *
     * @param iLoader
     */
    public static void init(@NonNull ILoader iLoader) {
        init(iLoader, new Config.Builder().build());
    }

    /**
     * 初始化
     *
     * @param iLoader
     * @param config
     */
    public static void init(@NonNull ILoader iLoader, @NonNull Config config) {
        ImageLoader.ILoader = iLoader;
        ImageLoader.ILoader.init(config);
    }

    /**
     * 入口
     *
     * @param context
     * @return
     */
    public static ImageRequest.Builder with(@Nullable Context context) {
        return new ImageRequest.Builder(context);
    }

    /**
     * @param imageView
     * @param imageRequest
     */
    static void load(ImageView imageView, ImageRequest imageRequest) {
        if (ILoader != null) {
            ILoader.load(imageView, imageRequest);
        }

    }

    /**
     * @param callback
     * @param imageRequest
     */
    static void load(LoaderCallback callback, ImageRequest imageRequest) {
        if (ILoader != null) {
            ILoader.load(callback, imageRequest);
        }
    }
}
