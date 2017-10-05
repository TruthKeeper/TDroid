package com.tk.tdroid.widget.http.interceptor;

import android.support.annotation.NonNull;

import com.tk.tdroid.constants.HttpConstants;
import com.tk.tdroid.utils.NetworkUtils;
import com.tk.tdroid.utils.internal.Logger;
import com.tk.tdroid.utils.internal.Utils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/5
 *     desc   : 离线缓存策略，仅限GET
 * </pre>
 */
public class CustomCacheInterceptor implements Interceptor {
    private static final String TAG = "CustomCacheInterceptor";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtils.isNetAvailable(Utils.getApp())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Logger.d(TAG, "no network");
        }
        Response originalResponse = chain.proceed(request);
        if (NetworkUtils.isNetAvailable(Utils.getApp())) {
            //有网络无缓存
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + 0)
                    .build();
        } else {
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + HttpConstants.CACHE_DATE)
                    .build();
        }
    }


}
