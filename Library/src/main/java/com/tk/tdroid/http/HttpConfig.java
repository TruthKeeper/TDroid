package com.tk.tdroid.http;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tk.tdroid.http.interceptor.CookieInterceptor;
import com.tk.tdroid.utils.EmptyUtils;
import com.tk.tdroid.utils.Utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/14
 *      desc : Http配置
 * </pre>
 */

public final class HttpConfig {
    private String baseUrl;
    private boolean log;
    private long connectTimeoutMilli;

    private int offlineCacheMaxStale;
    private File cacheDir;
    private long cacheSize;

    private boolean cookieEnabled;
    private CookieInterceptor.CookieSaveProvider cookieSaveProvider;
    private CookieInterceptor.CookieLoadProvider cookieLoadProvider;

    private List<Interceptor> interceptors;
    private List<Interceptor> networkInterceptors;

    private boolean httpsEnabled;
    private InputStream httpsCertificate;
    private String httpsPassword;

    private HttpConfig(Builder builder) {
        baseUrl = builder.baseUrl;
        log = builder.log;
        connectTimeoutMilli = builder.connectTimeoutMilli;
        offlineCacheMaxStale = builder.offlineCacheMaxStale;
        cacheDir = builder.cacheDir;
        cacheSize = builder.cacheSize;
        cookieEnabled = builder.cookieEnabled;
        cookieSaveProvider = builder.cookieSaveProvider;
        cookieLoadProvider = builder.cookieLoadProvider;
        interceptors = builder.interceptors;
        networkInterceptors = builder.networkInterceptors;
        httpsEnabled = builder.httpsEnabled;
        httpsCertificate = builder.httpsCertificate;
        httpsPassword = builder.httpsPassword;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isLog() {
        return log;
    }

    public long getConnectTimeoutMilli() {
        return connectTimeoutMilli;
    }

    public int getOfflineCacheMaxStale() {
        return offlineCacheMaxStale;
    }

    public File getCacheDir() {
        return cacheDir;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public boolean isCookieEnabled() {
        return cookieEnabled;
    }

    public CookieInterceptor.CookieSaveProvider getCookieSaveProvider() {
        return cookieSaveProvider;
    }

    public CookieInterceptor.CookieLoadProvider getCookieLoadProvider() {
        return cookieLoadProvider;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public List<Interceptor> getNetworkInterceptors() {
        return networkInterceptors;
    }

    public boolean isHttpsEnabled() {
        return httpsEnabled;
    }

    public InputStream getHttpsCertificate() {
        return httpsCertificate;
    }

    public String getHttpsPassword() {
        return httpsPassword;
    }

    public static final class Builder {
        private String baseUrl = null;
        private boolean log = true;
        private long connectTimeoutMilli = 10_000;
        private int offlineCacheMaxStale = Integer.MAX_VALUE;
        private File cacheDir = Utils.getApp().getCacheDir();
        private long cacheSize = 16 * 1024 * 1024;
        private boolean cookieEnabled = true;
        private CookieInterceptor.CookieSaveProvider cookieSaveProvider = null;
        private CookieInterceptor.CookieLoadProvider cookieLoadProvider = null;
        private List<Interceptor> interceptors = null;
        private List<Interceptor> networkInterceptors = null;
        private boolean httpsEnabled = false;
        private InputStream httpsCertificate = null;
        private String httpsPassword = null;

        public Builder() {
        }

        /**
         * 可通过{@link RuntimeUrlManager}动态修改
         *
         * @param baseUrl
         * @return
         */
        public Builder baseUrl(@NonNull String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * 打印日志
         *
         * @param log
         * @return
         */
        public Builder log(boolean log) {
            this.log = log;
            return this;
        }

        /**
         * 超时时长
         *
         * @param connectTimeoutMilli 毫秒
         * @return
         */
        public Builder connectTimeoutMilli(@IntRange(from = 0) long connectTimeoutMilli) {
            this.connectTimeoutMilli = connectTimeoutMilli;
            return this;
        }

        /**
         * 离线缓存最大时长
         *
         * @param offlineCacheMaxStale 单位: 秒 , 小于0则不启用离线缓存
         * @return
         */
        public Builder offlineCacheMaxStale(int offlineCacheMaxStale) {
            this.offlineCacheMaxStale = offlineCacheMaxStale;
            return this;
        }

        /**
         * 缓存目录
         *
         * @param cacheDir 缓存目录 , null则不启用缓存
         * @return
         */
        public Builder cacheDir(@Nullable File cacheDir) {
            this.cacheDir = cacheDir;
            return this;
        }

        /**
         * 缓存最大大小
         *
         * @param cacheSize 单位 b , 小于0则不启用缓存
         * @return
         */
        public Builder cacheSize(long cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }

        /**
         * 是否启用Cookie支持，并持久化，通过{@link CookieManager}操作
         *
         * @param cookieEnabled
         * @return
         */
        public Builder cookieEnabled(boolean cookieEnabled) {
            this.cookieEnabled = cookieEnabled;
            return this;
        }

        /**
         * Cookie 保存的提供者策略，默认记录
         *
         * @param cookieSaveProvider
         * @return
         */
        public Builder cookieSaveProvider(@Nullable CookieInterceptor.CookieSaveProvider cookieSaveProvider) {
            this.cookieSaveProvider = cookieSaveProvider;
            return this;
        }

        /**
         * Cookie 加载的提供者策略，默认返回
         *
         * @param cookieLoadProvider
         * @return
         */
        public Builder cookieLoadProvider(@Nullable CookieInterceptor.CookieLoadProvider cookieLoadProvider) {
            this.cookieLoadProvider = cookieLoadProvider;
            return this;
        }

        /**
         * 添加拦截器
         *
         * @param interceptor
         * @return
         */
        public Builder addInterceptor(@NonNull Interceptor interceptor) {
            if (EmptyUtils.isEmpty(interceptors)) {
                interceptors = new ArrayList<>();
            }
            interceptors.add(interceptor);
            return this;
        }

        /**
         * 添加能操作重定向的拦截器
         *
         * @param interceptor
         * @return
         */
        public Builder addNetworkInterceptor(@NonNull Interceptor interceptor) {
            if (EmptyUtils.isEmpty(networkInterceptors)) {
                networkInterceptors = new ArrayList<>();
            }
            networkInterceptors.add(interceptor);
            return this;
        }

        /**
         * 是否开启Https的支持
         *
         * @param httpsEnabled
         * @return
         */
        public Builder httpsEnabled(boolean httpsEnabled) {
            this.httpsEnabled = httpsEnabled;
            return this;
        }

        /**
         * Https的证书 .bks文件
         *
         * @param httpsCertificate
         * @return
         */
        public Builder httpsCertificate(@NonNull InputStream httpsCertificate) {
            this.httpsCertificate = httpsCertificate;
            return this;
        }

        /**
         * 证书密钥
         *
         * @param httpsPassword
         * @return
         */
        public Builder httpsPassword(@NonNull String httpsPassword) {
            this.httpsPassword = httpsPassword;
            return this;
        }

        public HttpConfig build() {
            return new HttpConfig(this);
        }
    }
}
