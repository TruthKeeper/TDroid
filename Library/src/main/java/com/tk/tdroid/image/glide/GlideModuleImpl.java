package com.tk.tdroid.image.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.tk.tdroid.http.interceptor.OfflineInterceptor;
import com.tk.tdroid.http.progress.ProgressManager;
import com.tk.tdroid.image.Config;
import com.tk.tdroid.utils.AppUtils;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/04
 *     desc   : xxxx描述
 * </pre>
 */
@GlideModule
public class GlideModuleImpl extends AppGlideModule {
    private static int connectTimeoutMilli;
    private static String innerCacheName;
    private static int innerCacheSize;
    private static boolean httpCache;

    public static void init(Config config) {
        connectTimeoutMilli = config.getConnectTimeoutMilli();
        innerCacheName = config.getInnerCacheName();
        innerCacheSize = config.getInnerCacheSize();
        httpCache = config.isHttpCache();
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //默认ARGB_8888 , 图片内存缓存占用八分之一
        builder.setMemoryCache(new LruResourceCache((int) AppUtils.getMaxMemory() / 8))
                .setDiskCache(new InternalCacheDiskCacheFactory(context, innerCacheName, innerCacheSize))
                .setDefaultRequestOptions(new RequestOptions()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .timeout(connectTimeoutMilli));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        OkHttpClient.Builder glideBuilder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeoutMilli, TimeUnit.MILLISECONDS)
                .addInterceptor(new OfflineInterceptor());
        //添加监听进度的支持
        glideBuilder = ProgressManager.getInstance().wrapper(glideBuilder);
        //Http缓存Cache-Control的支持
        if (httpCache) {
            glideBuilder.cache(new Cache(new File(context.getCacheDir(), innerCacheName + "/http"), innerCacheSize));
        }
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(glideBuilder.build()));
    }
}