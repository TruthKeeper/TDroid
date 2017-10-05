package com.tk.tdroid.widget.image;

import com.bumptech.glide.load.model.GlideUrl;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/04
 *     desc   : xxxx描述
 * </pre>
 */
public class CacheTokenUrl extends GlideUrl {
    private final String url;

    public CacheTokenUrl(String url) {
        super(url);
        this.url = url;
    }

    @Override
    public String getCacheKey() {
        String tokenStr = "?token=";
        int tokenIndex = url.indexOf(tokenStr);
        if (tokenIndex == -1) {
            return url;
        }
        return url.substring(0, tokenIndex);
    }

}
