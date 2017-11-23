package com.tk.tdroid.widget.image;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

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
import com.tk.tdroid.utils.AppUtils;
import com.tk.tdroid.widget.http.interceptor.OfflineInterceptor;
import com.tk.tdroid.widget.http.progress.ProgressManager;

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
    private static boolean httpCache = true;

    private static void install(Builder builder) {
        connectTimeoutMilli = builder.connectTimeoutMilli;
        innerCacheName = builder.innerCacheName;
        innerCacheSize = builder.innerCacheSize;
        httpCache = builder.httpCache;
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

    public static final class Builder {
        private int connectTimeoutMilli = 15_000;

        private String innerCacheName = "TDroid_Glide";
        // 默认256M 256 * 1024 * 1024 = 268435456
        private int innerCacheSize = 1 << 28;

        private boolean httpCache = true;

        public Builder() {
        }

        /**
         * 缓存超时
         *
         * @param connectTimeoutMilli
         * @return
         */
        public Builder connectTimeoutMilli(@IntRange(from = 0) int connectTimeoutMilli) {
            this.connectTimeoutMilli = connectTimeoutMilli;
            return this;
        }

        /**
         * 缓存路径名
         *
         * @param innerCacheName
         * @return
         */
        public Builder innerCacheDir(@NonNull String innerCacheName) {
            this.innerCacheName = innerCacheName;
            return this;
        }

        /**
         * 缓存文件大小
         *
         * @param innerCacheSize
         * @return
         */
        public Builder innerCacheSize(@IntRange(from = 0) int innerCacheSize) {
            this.innerCacheSize = innerCacheSize;
            return this;
        }

        /**
         * 是否开启Http缓存 , 即根据Cache-Control来缓存
         *
         * @param httpCache 默认开启
         * @return
         */
        public Builder httpCache(boolean httpCache) {
            this.httpCache = httpCache;
            return this;
        }

        /**
         * 载入配置
         */
        public void install() {
            GlideModuleImpl.install(this);
        }
    }
}
