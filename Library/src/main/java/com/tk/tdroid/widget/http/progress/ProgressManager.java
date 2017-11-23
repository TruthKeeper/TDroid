package com.tk.tdroid.widget.http.progress;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CheckResult;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.tk.tdroid.utils.EmptyUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/22
 *      desc : 思想来自开源项目 : <a href="https://github.com/JessYanCoding/ProgressManager">ProgressManager</a>
 * </pre>
 */

public final class ProgressManager {
    private static volatile ProgressManager mProgressManager = null;
    /**
     * 默认回调监听的间隔
     */
    private static final int DEFAULT_INTERVAL_TIME = 120;

    private final Map<String, List<WeakReference<ProgressListener>>> mRequestListeners = new ArrayMap<>();
    private final Map<String, List<WeakReference<ProgressListener>>> mResponseListeners = new ArrayMap<>();

    private int mIntervalTime = DEFAULT_INTERVAL_TIME;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private ProgressManager() {
    }

    public static ProgressManager getInstance() {
        if (mProgressManager == null) {
            synchronized (ProgressManager.class) {
                if (mProgressManager == null) {
                    mProgressManager = new ProgressManager();
                }
            }
        }
        return mProgressManager;
    }

    /**
     * 初始化配置
     *
     * @param builder
     * @return
     */
    @CheckResult
    public OkHttpClient.Builder wrapper(@NonNull OkHttpClient.Builder builder) {
        // TODO: 2017/11/23 为了支持缓存，不能使用 addNetworkInterceptor
        return builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                return wrapResponseBody(chain.proceed(wrapRequestBody(chain.request())));
            }
        });
    }

    /**
     * 设置 {@link ProgressListener#onProgress(String, boolean, ProgressInfo)} 每次回调的间隔时间
     *
     * @param intervalTime 单位毫秒
     */
    public void setIntervalTimeTime(@IntRange(from = 0) int intervalTime) {
        this.mIntervalTime = intervalTime;
    }

    /**
     * 监听请求进度
     *
     * @param url      作为唯一标识
     * @param listener 监听器
     */
    public void addRequestListener(@NonNull String url, @NonNull ProgressListener listener) {
        addListener(mRequestListeners, url, listener);
    }

    /**
     * 监听响应进度
     *
     * @param url      作为唯一标识
     * @param listener 监听器
     */
    public void addResponseListener(@NonNull String url, @NonNull ProgressListener listener) {
        addListener(mResponseListeners, url, listener);
    }

    private void addListener(@NonNull Map<String, List<WeakReference<ProgressListener>>> map, @NonNull String url, @NonNull ProgressListener listener) {
        List<WeakReference<ProgressListener>> references = map.get(url);
        synchronized (url) {
            if (references == null) {
                references = new LinkedList<>();
                map.put(url, references);
            }
            references.add(new WeakReference<>(listener));
        }
    }

    private Request wrapRequestBody(@NonNull Request request) {
        if (request.body() == null) {
            return request;
        }
        String url = request.url().toString();
        List<WeakReference<ProgressListener>> references = mRequestListeners.get(url);
        if (!EmptyUtils.isEmpty(references)) {
            return request.newBuilder()
                    .method(request.method(), new ProgressRequestBody(url, mHandler,
                            request.body(),
                            //固定内存空间
                            references.toArray(new WeakReference[references.size()]),
                            mIntervalTime))
                    .build();
        }
        return request;
    }

    private Response wrapResponseBody(@NonNull Response response) {
        if (response.body() == null) {
            return response;
        }
        String url = response.request().url().toString();
        List<WeakReference<ProgressListener>> references = mResponseListeners.get(url);
        if (!EmptyUtils.isEmpty(references)) {
            return response.newBuilder()
                    .body(new ProgressResponseBody(url, mHandler,
                            response.body(),
                            //固定内存空间
                            references.toArray(new WeakReference[references.size()]),
                            mIntervalTime))
                    .build();
        }
        return response;
    }
}
