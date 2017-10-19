package com.tk.tdroid.widget.image;

import android.support.annotation.NonNull;

import com.bumptech.glide.load.model.GlideUrl;

import java.net.URL;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/04
 *     desc   : xxxx描述
 * </pre>
 */
public class CacheTokenUrl extends GlideUrl {
    public static final String TOKEN_KEY = "?token=";
    private final String url;
    private String tokenKey;

    public static CacheTokenUrl wrap(String url) {
        return new CacheTokenUrl(url, TOKEN_KEY);
    }

    public static CacheTokenUrl wrap(String url, String tokenKey) {
        return new CacheTokenUrl(url, tokenKey);
    }

    public CacheTokenUrl(@NonNull String url) {
        super(url);
        this.url = url;
        this.tokenKey = TOKEN_KEY;
    }

    public CacheTokenUrl(@NonNull String url, String tokenKey) {
        super(url);
        this.url = url;
        this.tokenKey = tokenKey;
    }

    public CacheTokenUrl(@NonNull URL url) {
        super(url);
        this.url = url.toString();
        this.tokenKey = TOKEN_KEY;
    }

    public CacheTokenUrl(@NonNull URL url, String tokenKey) {
        super(url);
        this.url = url.toString();
        this.tokenKey = tokenKey;
    }

    @Override
    public String getCacheKey() {
        int tokenIndex = url.indexOf(tokenKey);
        if (tokenIndex == -1) {
            return url;
        }
        //过滤token
        return url.substring(0, tokenIndex);
    }

}
