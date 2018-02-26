package com.tk.tdroiddemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tk.tdroid.EventBusIndex;
import com.tk.tdroid.event.EventHelper;
import com.tk.tdroid.http.HttpConfig;
import com.tk.tdroid.http.HttpUtils;
import com.tk.tdroid.http.interceptor.CookieInterceptor;
import com.tk.tdroid.image.ImageLoader;
import com.tk.tdroid.router.Interceptor;
import com.tk.tdroid.router.RouterCell;
import com.tk.tdroid.router.TRouter;
import com.tk.tdroid.utils.NetworkRxObservable;
import com.tk.tdroid.utils.StorageUtils;
import com.tk.tdroid.utils.Utils;

import java.io.File;

import okhttp3.Headers;
import okhttp3.HttpUrl;


/**
 * <pre>
 *      author : TK
 *      time : 2017/9/13
 *      desc :
 * </pre>
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        //初始化路由
        TRouter.register(new com.apt.Mine.RouterTable());
        TRouter.register(new com.apt.Home.RouterTable());
        TRouter.addGlobalInterceptor(new Interceptor() {
            @Override
            public boolean intercept(RouterCell cell, Context context) {
                Log.d("TRouter", "路由：" + cell.routerPath);
                return false;
            }

            @Override
            public void onIntercepted(RouterCell cell, Context context) {

            }
        });
        //初始化ImageLoader
        ImageLoader.init();

        HttpConfig httpConfig = new HttpConfig.Builder()
                .baseUrl("https://www.baidu.com/")
//                .baseUrl("https://api.github.com/")
                .log(true)
                .connectTimeoutMilli(10_000)
                .offlineCacheMaxStale(Integer.MAX_VALUE)
                .cacheDir(new File(StorageUtils.getCachePath(), "http_cache"))
                .cacheSize(100 * 1024 * 1024)
                .cookieEnabled(true)
                .cookieLoadProvider(new CookieInterceptor.CookieLoadProvider() {
                    @Override
                    public boolean load(HttpUrl requestUrl, Headers requestHeaders) {
                        //默认只要之前缓存过Cookie就设置
                        return true;
                    }
                })
                .cookieSaveProvider(new CookieInterceptor.CookieSaveProvider() {
                    @Override
                    public boolean save(HttpUrl requestUrl, Headers requestHeaders) {
                        //默认只要有Set-Cookie就记录
                        return true;
                    }
                })
                .httpsEnabled(true)
                .httpsCertificate(getResources().openRawResource(R.raw.github))
                .httpsPassword("github_test")
                .build();

        HttpUtils.init(httpConfig);
        NetworkRxObservable.getInstance().init();
        EventHelper.init(new EventBusIndex());

//        Logger.init(new Logger.Builder()
//                .logPath(Environment.getExternalStorageDirectory() + File.separator + "test")
//                .build());

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        NetworkRxObservable.getInstance().recycle();
    }
}
