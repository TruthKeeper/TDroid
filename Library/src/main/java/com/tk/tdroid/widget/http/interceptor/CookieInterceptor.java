package com.tk.tdroid.widget.http.interceptor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tk.tdroid.widget.http.CookieManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/10
 *      desc :
 * </pre>
 */

public final class CookieInterceptor implements Interceptor {
    private CookieSaveProvider cookieSaveProvider;
    private CookieLoadProvider cookieLoadProvider;

    public CookieInterceptor() {
    }

    public CookieInterceptor(@Nullable CookieSaveProvider cookieSaveProvider, @Nullable CookieLoadProvider cookieLoadProvider) {
        this.cookieSaveProvider = cookieSaveProvider;
        this.cookieLoadProvider = cookieLoadProvider;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();
        if (cookieLoadProvider == null || cookieLoadProvider.load(originalRequest.url(), originalRequest.headers())) {
            List<Cookie> cookieList = CookieManager.getInstance().get(originalRequest.url());
            for (Cookie cookie : cookieList) {
                builder.addHeader("Cookie", cookie.toString());
            }
        }
        Request cookieRequest = builder.build();

        Response response = chain.proceed(cookieRequest);
        if (cookieSaveProvider == null || cookieSaveProvider.save(originalRequest.url(), originalRequest.headers())) {
            List<Cookie> setCookieList = Cookie.parseAll(originalRequest.url(), response.headers());
            for (Cookie cookie : setCookieList) {
                CookieManager.getInstance().add(originalRequest.url(), cookie);
            }
        }
        return response;
    }

    public void setCookieLoadProvider(CookieLoadProvider cookieLoadProvider) {
        this.cookieLoadProvider = cookieLoadProvider;
    }

    public void setCookieSaveProvider(CookieSaveProvider cookieSaveProvider) {
        this.cookieSaveProvider = cookieSaveProvider;
    }

    public interface CookieSaveProvider {
        boolean save(HttpUrl requestUrl, Headers requestHeaders);
    }

    public interface CookieLoadProvider {
        boolean load(HttpUrl requestUrl, Headers requestHeaders);
    }
}
