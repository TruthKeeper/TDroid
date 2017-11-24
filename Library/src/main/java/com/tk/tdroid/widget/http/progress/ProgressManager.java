package com.tk.tdroid.widget.http.progress;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CheckResult;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.tk.tdroid.utils.EmptyUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
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
    /**
     * 在{@link retrofit2.Retrofit}中添加的{@link retrofit2.http.Header}名称
     */
    public static final String PROGRESS_HEADER = "tdroid_progress";
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

    /**
     * get singleInstance
     *
     * @return
     */
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
     * @param key      唯一标识 , 通常是url(一般场景) or header(key:{@link ProgressManager#PROGRESS_HEADER})的value的值 ,
     *                 用于同一url下的不同body的{@link retrofit2.http.POST}请求或者url会产生重定向
     * @param listener 监听器
     */
    public void addRequestListener(@NonNull String key, @NonNull ProgressListener listener) {
        addListener(mRequestListeners, key, listener);
    }

    /**
     * 监听响应进度
     *
     * @param key      唯一标识 , 通常是url(一般场景) or header(key:{@link ProgressManager#PROGRESS_HEADER})的value的值 ,
     *                 用于同一url下的不同body的{@link retrofit2.http.POST}请求或者url会产生重定向
     * @param listener 监听器
     */
    public void addResponseListener(@NonNull String key, @NonNull ProgressListener listener) {
        addListener(mResponseListeners, key, listener);
    }

    private void addListener(@NonNull Map<String, List<WeakReference<ProgressListener>>> map, @NonNull String httpUrlOrHeader, @NonNull ProgressListener listener) {
        List<WeakReference<ProgressListener>> references = map.get(httpUrlOrHeader);
        synchronized (httpUrlOrHeader) {
            if (references == null) {
                references = new LinkedList<>();
                map.put(httpUrlOrHeader, references);
            }
            references.add(new WeakReference<>(listener));
        }
    }

    /**
     * 移除监听
     *
     * @param key
     */
    public synchronized void removeListener(@NonNull String key) {
        recyclerListener(mResponseListeners.remove(key));
        recyclerListener(mRequestListeners.remove(key));
    }

    private void recyclerListener(List<WeakReference<ProgressListener>> references) {
        if (!EmptyUtils.isEmpty(references)) {
            for (WeakReference<ProgressListener> reference : references) {
                if (reference != null && reference.get() != null) {
                    reference.clear();
                }
            }
        }
    }

    /**
     * 解析成GlideUrl , 用于适配重定向
     *
     * @param url
     * @param key 唯一标识
     * @return
     */
    public static GlideUrl parseGlideUrl(@NonNull String url, @NonNull String key) {
        return new GlideUrl(url, new Headers() {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put(PROGRESS_HEADER, key);
                return map;
            }
        });
    }

    /**
     * 包装请求体
     *
     * @param request
     * @return
     */
    private Request wrapRequestBody(@NonNull Request request) {
        if (request.body() == null) {
            return request;
        }
        String key = null;
        String header = request.header(PROGRESS_HEADER);
        String url = request.url().toString();
        WeakReference[] referenceArray = null;
        //先获取自定义头 , 没有则通过url
        List<WeakReference<ProgressListener>> references = null;
        if (!EmptyUtils.isEmpty(header)) {
            references = mRequestListeners.get(header);
            key = header;
        }
        if (EmptyUtils.isEmpty(references)) {
            references = mRequestListeners.get(url);
            key = url;
        }

        if (!EmptyUtils.isEmpty(references)) {
            referenceArray = references.toArray(referenceArray = new WeakReference[references.size()]);
            return request.newBuilder()
                    .method(request.method(), new ProgressRequestBody(key, mHandler,
                            request.body(),
                            //固定内存空间
                            referenceArray,
                            mIntervalTime))
                    .build();
        }
        return request;
    }

    private Response wrapResponseBody(@NonNull Response response) {
        if (response.body() == null) {
            return response;
        }
        String key = null;
        String header = response.request().header(PROGRESS_HEADER);
        String url = response.request().url().toString();
        WeakReference[] referenceArray = null;
        //先获取自定义头 , 没有则通过url
        List<WeakReference<ProgressListener>> references = null;
        if (!EmptyUtils.isEmpty(header)) {
            references = mResponseListeners.get(header);
            key = header;
        }
        if (EmptyUtils.isEmpty(references)) {
            references = mResponseListeners.get(url);
            key = url;
        }

        if (!EmptyUtils.isEmpty(references)) {
            referenceArray = references.toArray(referenceArray = new WeakReference[references.size()]);
            return response.newBuilder()
                    .body(new ProgressResponseBody(key, mHandler,
                            response.body(),
                            //固定内存空间
                            referenceArray,
                            mIntervalTime))
                    .build();
        }
        return response;
    }
}
