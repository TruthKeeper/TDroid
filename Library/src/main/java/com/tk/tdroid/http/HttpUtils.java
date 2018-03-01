package com.tk.tdroid.http;

import android.support.annotation.NonNull;

import com.tk.tdroid.http.interceptor.CookieInterceptor;
import com.tk.tdroid.http.interceptor.LogInterceptor;
import com.tk.tdroid.http.interceptor.OfflineInterceptor;
import com.tk.tdroid.http.progress.ProgressManager;
import com.tk.tdroid.utils.EmptyUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/05
 *     desc   : 网络请求工具
 * </pre>
 */
public final class HttpUtils {

    private static volatile Retrofit retrofit = null;
    private static volatile OkHttpClient okHttpClient = null;

    private HttpUtils() {
        throw new IllegalStateException();
    }

    /**
     * 初始化
     */
    public static void init(@NonNull HttpConfig httpConfig) {
        if (retrofit == null) {
            synchronized (HttpUtils.class) {
                if (retrofit == null) {
                    OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                            .connectTimeout(httpConfig.getConnectTimeoutMilli(), TimeUnit.MILLISECONDS);

                    List<Interceptor> networkInterceptors = httpConfig.getNetworkInterceptors();
                    if (!EmptyUtils.isEmpty(networkInterceptors)) {
                        for (Interceptor interceptor : networkInterceptors) {
                            okHttpBuilder.addNetworkInterceptor(interceptor);
                        }
                    }
                    //添加动态BaseUrl的支持 -> 将请求的Url进行修改，所以放在责任链第一位
                    okHttpBuilder = RuntimeUrlManager.getInstance().wrapper(okHttpBuilder);
                    //自定义的拦截器
                    List<Interceptor> interceptors = httpConfig.getInterceptors();
                    if (!EmptyUtils.isEmpty(interceptors)) {
                        for (Interceptor interceptor : interceptors) {
                            okHttpBuilder.addInterceptor(interceptor);
                        }
                    }
                    //对Set-Cookie进行Cookie持久化  TODO: 2017/11/14  待系统测试
                    if (httpConfig.isCookieEnabled()) {
                        okHttpBuilder.addInterceptor(new CookieInterceptor(httpConfig.getCookieSaveProvider(), httpConfig.getCookieLoadProvider()));
                    }
                    //输出网络日志
                    if (httpConfig.isLog()) {
                        okHttpBuilder.addInterceptor(new LogInterceptor());
                    }
                    //开启离线缓存策略，底层仅限GET
                    if (httpConfig.getOfflineCacheMaxStale() > 0) {
                        okHttpBuilder.addInterceptor(new OfflineInterceptor(httpConfig.getOfflineCacheMaxStale()));
                    }
                    //添加监听进度的支持
                    okHttpBuilder = ProgressManager.getInstance().wrapper(okHttpBuilder);
                    if (httpConfig.getCacheDir() != null && httpConfig.getCacheSize() > 0) {
                        //Http缓存Cache-Control
                        // TODO: 2017/11/14 更多缓存模式支持
                        okHttpBuilder.cache(new Cache(httpConfig.getCacheDir(), httpConfig.getCacheSize()));
                    }
                    if (httpConfig.isHttpsEnabled()) {
                        //Https的支持
                        okHttpBuilder = HttpsSupport.wrapper(okHttpBuilder, httpConfig.getHttpsCertificate(), httpConfig.getHttpsPassword());
                    }
                    okHttpClient = okHttpBuilder.build();
                    retrofit = new Retrofit.Builder()
                            .baseUrl(httpConfig.getBaseUrl())
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
    }

    /**
     * 构建
     *
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> service) {
        if (retrofit == null) {
            throw new IllegalStateException("HttpUtils must init !");
        }
        return retrofit.create(service);
    }

    /**
     * 获取OkHttpClient
     *
     * @return
     */
    public static OkHttpClient getClient() {
        if (okHttpClient == null) {
            throw new IllegalStateException("HttpUtils must init !");
        }
        return okHttpClient;
    }

    /**
     * 获取Retrofit
     *
     * @return
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            throw new IllegalStateException("HttpUtils must init !");
        }
        return retrofit;
    }
}
