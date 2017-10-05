package com.tk.tdroid.widget.image;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.tk.tdroid.utils.AppUtils;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/04
 *     desc   : xxxx描述
 * </pre>
 */
@GlideModule
public class GlideModuleImpl extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //默认ARGB_8888
        //设置图片内存缓存占用八分之一
        builder.setMemoryCache(new LruResourceCache((int) AppUtils.getMaxMemory() / 8))
                //200Mb磁盘缓存
                .setDiskCache(new InternalCacheDiskCacheFactory(context, "Glide", 200 * 1024 * 1024))
                .setDefaultRequestOptions(new RequestOptions()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .timeout(15_000));
    }
}
