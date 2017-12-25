package com.tk.tdroid.http;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.tk.tdroid.utils.EmptyUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/14
 *      desc : Retrofit BaseUrl动态配置<ul>
 *          <li>1.Retrofit动态代理接口配置，自定义头{@link RuntimeUrlManager#BASE_URL} , 优先级最高</li>
 *          <li>2.Retrofit动态代理接口配置，自定义头{@link RuntimeUrlManager#BASE_NAME} , 优先级第二 ,
 *          通过键值对存放BaseUrl : {@link RuntimeUrlManager#addBaseUrl(String, String)} , 清除 : {@link RuntimeUrlManager#removeBaseUrl(String)}</li>
 *          <li>3.改变Retrofit的全局BaseUrl : {@link RuntimeUrlManager#setGlobalBaseUrl(String)},优先级次之,
 *          清除全局BaseUrl : {@link RuntimeUrlManager#removeGlobalBaseUrl()}</li>
 *          <li>4.Retrofit默认BaseUrl配置 , 优先级最低</li>
 *      </ul>
 * </pre>
 */

public final class RuntimeUrlManager {

    public static final String BASE_URL = "base_url";
    public static final String BASE_NAME = "base_name";
    public static final String BASE_GLOBAL = "base_global";

    private static volatile RuntimeUrlManager mRuntimeUrlManager = null;
    private final Map<String, HttpUrl> baseNameMap = new ConcurrentHashMap<>();
    private boolean enabled = true;

    private RuntimeUrlManager() {
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static RuntimeUrlManager getInstance() {
        if (mRuntimeUrlManager == null) {
            synchronized (RuntimeUrlManager.class) {
                if (mRuntimeUrlManager == null) {
                    mRuntimeUrlManager = new RuntimeUrlManager();
                }
            }
        }
        return mRuntimeUrlManager;
    }

    /**
     * 初始化
     *
     * @param builder
     * @return
     */
    @CheckResult
    public OkHttpClient.Builder wrapper(OkHttpClient.Builder builder) {
        return builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                if (enabled) {
                    return chain.proceed(processRequest(chain.request()));
                }
                return chain.proceed(chain.request());
            }
        });
    }

    /**
     * 是否启用
     *
     * @param enabled
     */
    public void enabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 设置全局BaseUrl
     *
     * @param globalBaseUrl
     */
    public void setGlobalBaseUrl(@NonNull String globalBaseUrl) {
        HttpUrl url = HttpUrl.parse(globalBaseUrl);
        if (url == null) {
            throw new IllegalArgumentException("url error");
        } else {
            baseNameMap.put(BASE_GLOBAL, url);
        }
    }

    /**
     * 移除全局BaseUrl
     */
    public void removeGlobalBaseUrl() {
        baseNameMap.remove(BASE_GLOBAL);
    }

    /**
     * 添加自定义的BaseUrl
     *
     * @param baseName
     * @param baseUrl
     */
    public void addBaseUrl(@NonNull String baseName, @NonNull String baseUrl) {
        HttpUrl url = HttpUrl.parse(baseUrl);
        if (url == null) {
            throw new IllegalArgumentException("url error");
        } else {
            baseNameMap.put(baseName, url);
        }
    }

    /**
     * 移除自定义的BaseUrl
     *
     * @param baseName
     */
    public void removeBaseUrl(@NonNull String baseName) {
        baseNameMap.remove(baseName);
    }

    /**
     * 处理请求
     *
     * @param request
     * @return
     */
    private Request processRequest(Request request) {
        String header = request.header(BASE_URL);
        HttpUrl httpUrl;
        if (EmptyUtils.isEmpty(header)) {
            header = request.header(BASE_NAME);
            if (EmptyUtils.isEmpty(header)) {
                //全局BaseUrl
                httpUrl = baseNameMap.get(BASE_GLOBAL);
            } else {
                //map中记录的
                httpUrl = baseNameMap.get(header);
            }
        } else {
            //API中直接定义的BaseUrl
            httpUrl = parseUrl(header);
        }
        if (httpUrl == null) {
            return request;
        }
        HttpUrl fullHttpUrl = request.url()
                .newBuilder()
                .scheme(httpUrl.scheme())
                .host(httpUrl.host())
                .port(httpUrl.port())
                .build();
        return request.newBuilder()
                .removeHeader(BASE_URL)
                .removeHeader(BASE_NAME)
                .url(fullHttpUrl)
                .build();
    }

    /**
     * 解析
     *
     * @param url
     * @return
     */
    private static HttpUrl parseUrl(@NonNull String url) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            throw new IllegalArgumentException("url error");
        } else {
            return httpUrl;
        }
    }
}