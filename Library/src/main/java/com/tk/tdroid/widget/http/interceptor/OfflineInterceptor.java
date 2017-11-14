package com.tk.tdroid.widget.http.interceptor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tk.tdroid.utils.NetworkUtils;

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
public final class OfflineInterceptor implements Interceptor {

    private static final String TAG = "OfflineInterceptor";
    /**
     * 离线缓存保留时长，默认永远
     */
    private int maxStale;

    public OfflineInterceptor() {
        this.maxStale = Integer.MAX_VALUE;
    }

    public OfflineInterceptor(int maxStale) {
        this.maxStale = maxStale;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtils.isConnected()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Log.d(TAG, "offline");
        }
        Response originalResponse = chain.proceed(request);
        if (!NetworkUtils.isConnected()) {
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return originalResponse;
    }
}