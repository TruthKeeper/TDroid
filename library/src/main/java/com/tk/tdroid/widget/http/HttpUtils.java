package com.tk.tdroid.widget.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tk.tdroid.constants.HttpConstants;
import com.tk.tdroid.widget.http.interceptor.CustomCacheInterceptor;
import com.tk.tdroid.widget.http.interceptor.LogInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/05
 *     desc   : xxxx描述
 * </pre>
 */
public class HttpUtils {

    private static volatile Retrofit retrofit = null;
    private static volatile OkHttpClient okHttpClient = null;


    private HttpUtils() {
        throw new IllegalStateException();
    }

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(@NonNull Context context) {
        if (retrofit == null) {
            synchronized (HttpUtils.class) {
                if (retrofit == null) {
                    File httpCacheDirectory = new File(context.getCacheDir(), HttpConstants.CACHE_DIR);
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(HttpConstants.TIME_OUT, TimeUnit.SECONDS)
                            .cache(new Cache(httpCacheDirectory, HttpConstants.CACHE_SIZE))
                            //开启离线缓存策略，仅限GET
                            .addInterceptor(new CustomCacheInterceptor())
                            //日志
                            .addInterceptor(new LogInterceptor().setLevel(LogInterceptor.BODY));
                    okHttpClient = builder.build();

                    retrofit = new Retrofit.Builder()
//                            .baseUrl(BuilConfig.BASE_API_URL)
                            .client(okHttpClient)
//                            .addConverterFactory(CustomGsonFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
    }


    /**
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> service) {
        return retrofit == null ? null : retrofit.create(service);
    }

    /**
     * 获取OkHttpClient
     *
     * @return
     */
    public static OkHttpClient getClient() {
        return okHttpClient;
    }


}
