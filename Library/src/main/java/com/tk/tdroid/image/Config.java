package com.tk.tdroid.image;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/22
 *      desc :
 * </pre>
 */

public class Config {
    private int connectTimeoutMilli;
    private String innerCacheName;
    private int innerCacheSize;
    private boolean httpCache;

    private Config(Builder builder) {
        connectTimeoutMilli = builder.connectTimeoutMilli;
        innerCacheName = builder.innerCacheName;
        innerCacheSize = builder.innerCacheSize;
        httpCache = builder.httpCache;
    }

    public int getConnectTimeoutMilli() {
        return connectTimeoutMilli;
    }

    public String getInnerCacheName() {
        return innerCacheName;
    }

    public int getInnerCacheSize() {
        return innerCacheSize;
    }

    public boolean isHttpCache() {
        return httpCache;
    }

    public static final class Builder {
        private int connectTimeoutMilli = 15_000;
        private String innerCacheName = "TDroid_Glide";
        private int innerCacheSize = 1 << 28;
        private boolean httpCache = true;

        public Builder() {
        }

        /**
         * 超时时间
         *
         * @param connectTimeoutMilli 默认 15_000
         * @return
         */
        public Builder connectTimeoutMilli(int connectTimeoutMilli) {
            this.connectTimeoutMilli = connectTimeoutMilli;
            return this;
        }

        /**
         * 私有缓存文件名
         *
         * @param innerCacheName 默认 TDroid_Glide
         * @return
         */
        public Builder innerCacheName(String innerCacheName) {
            this.innerCacheName = innerCacheName;
            return this;
        }

        /**
         * 最大缓存大小
         *
         * @param innerCacheSize 默认256M 256 * 1024 * 1024 = 268435456
         * @return
         */
        public Builder innerCacheSize(int innerCacheSize) {
            this.innerCacheSize = innerCacheSize;
            return this;
        }

        /**
         * 是否开启http CacheControl 缓存
         *
         * @param httpCache 默认开启
         * @return
         */
        public Builder httpCache(boolean httpCache) {
            this.httpCache = httpCache;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }
}
