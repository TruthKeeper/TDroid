package com.tk.tdroid.widget.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.tk.tdroid.utils.EmptyUtils;
import com.tk.tdroid.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/13
 *      desc : OkHttp Cookie管理工具
 * </pre>
 */

public final class CookieManager {
    private static volatile CookieManager mCookieManager = null;
    /**
     * 全局Cookie的内存记录
     * host -> cookie token -> cookie
     */
    private Map<String, ConcurrentHashMap<String, Cookie>> globalCookies;
    private CookiePreferenceProvider mProvider;

    /**
     * 获取单例
     *
     * @return
     */
    public static CookieManager getInstance() {
        return getInstance(null);
    }

    /**
     * 获取单例
     *
     * @param provider
     * @return
     */
    public static CookieManager getInstance(@Nullable CookiePreferenceProvider provider) {
        if (mCookieManager == null) {
            synchronized (CookieManager.class) {
                if (mCookieManager == null) {
                    mCookieManager = new CookieManager(provider);
                }
            }
        }
        return mCookieManager;
    }

    private CookieManager(@Nullable CookiePreferenceProvider provider) {
        mProvider = provider == null ? new DefaultProvider() : provider;
        Map<String, ConcurrentHashMap<String, Cookie>> map = mProvider.init();
        globalCookies = EmptyUtils.isEmpty(map) ? new ArrayMap<String, ConcurrentHashMap<String, Cookie>>(2) : map;
    }

    /**
     * 获取一个Cookie的对应键
     *
     * @param cookie
     * @return
     */
    public String getCookieKey(@NonNull Cookie cookie) {
        return cookie.name() + "$" + cookie.domain();
    }

    /**
     * 添加Cookie
     *
     * @param url
     * @param cookie
     */
    public void add(@NonNull HttpUrl url, @NonNull Cookie cookie) {
        final String key = url.host();
        ConcurrentHashMap<String, Cookie> hostCookieMap = globalCookies.get(key);
        if (hostCookieMap == null) {
            hostCookieMap = new ConcurrentHashMap<>();
            globalCookies.put(key, hostCookieMap);
        }

        String cookieKey = getCookieKey(cookie);
        //更新内存并持久化
        if (cookie.persistent()) {
            hostCookieMap.put(cookieKey, cookie);
        } else {
            //过期则移除
            hostCookieMap.remove(cookieKey);
        }
        mProvider.save(globalCookies);
    }

    /**
     * 添加Cookie List
     *
     * @param url
     * @param cookieList
     */
    public void add(@NonNull HttpUrl url, @NonNull List<Cookie> cookieList) {
        final String key = url.host();
        ConcurrentHashMap<String, Cookie> hostCookieMap = globalCookies.get(key);
        if (hostCookieMap == null) {
            hostCookieMap = new ConcurrentHashMap<>();
            globalCookies.put(key, hostCookieMap);
        }

        String cookieKey;
        for (Cookie cookie : cookieList) {
            cookieKey = getCookieKey(cookie);
            //更新内存并持久化
            if (cookie.persistent()) {
                hostCookieMap.put(cookieKey, cookie);
            } else {
                //过期则移除
                hostCookieMap.remove(cookieKey);
            }
        }
        mProvider.save(globalCookies);
    }

    /**
     * 获取Cookie
     *
     * @param url
     * @return
     */
    public List<Cookie> get(@NonNull HttpUrl url) {
        final List<Cookie> list = new ArrayList<>();
        final String key = url.host();
        final Map<String, Cookie> hostCookieMap = globalCookies.get(key);
        if (!EmptyUtils.isEmpty(hostCookieMap)) {
            list.addAll(hostCookieMap.values());
        }
        return list;
    }

    /**
     * 清除Cookie
     *
     * @param url
     * @param cookie
     * @return
     */
    public boolean removeCookie(@NonNull HttpUrl url, @NonNull Cookie cookie) {
        final String key = url.host();
        String cookieKey = getCookieKey(cookie);
        ConcurrentHashMap<String, Cookie> hostCookieMap = globalCookies.get(key);

        if (EmptyUtils.isEmpty(hostCookieMap)) {
            return false;
        } else {
            hostCookieMap.remove(cookieKey);
            mProvider.remove(cookieKey);
            return true;
        }
    }

    /**
     * 清除所有Cookie
     */
    public void removeAllCookie() {
        globalCookies.clear();
        mProvider.removeAll();
    }

    public interface CookiePreferenceProvider {
        Map<String, ConcurrentHashMap<String, Cookie>> init();

        void save(@NonNull Map<String, ConcurrentHashMap<String, Cookie>> map);

        void remove(@NonNull String cookieKey);

        void removeAll();
    }

    public static class DefaultProvider implements CookiePreferenceProvider {
        public static final String HOST_SP = "http_cookie_default_provider_host";
        public static final String COOKIE_SP = "http_cookie_default_provider_cookie";

        private SharedPreferences hostSp;
        private SharedPreferences cookieSp;
        private final String hostSeparate = ",";
        private final String cookieSeparate = "|";

        DefaultProvider() {
            hostSp = Utils.getApp().getSharedPreferences(HOST_SP, Context.MODE_PRIVATE);
            cookieSp = Utils.getApp().getSharedPreferences(COOKIE_SP, Context.MODE_PRIVATE);
        }

        @Override
        public Map<String, ConcurrentHashMap<String, Cookie>> init() {
            final Map<String, ConcurrentHashMap<String, Cookie>> map = new ArrayMap<>();
            final Set<? extends Map.Entry<String, ?>> hostMapEntrySet = hostSp.getAll().entrySet();
            ConcurrentHashMap<String, Cookie> cookieMap;

            for (Map.Entry<String, ?> entry : hostMapEntrySet) {
                //Host对应的CookieKey数组
                String[] cookieKeyArray = ((String) entry.getValue()).split(hostSeparate);
                cookieMap = new ConcurrentHashMap<>();

                for (String cookieKey : cookieKeyArray) {
                    String cookieListString = cookieSp.getString(cookieKey, null);
                    if (!EmptyUtils.isEmpty(cookieListString)) {
                        String[] cookieStrArray = cookieListString.split(cookieSeparate);
                        //解析成Cookie
                        cookieMap.put(cookieKey, generate(cookieStrArray));
                    }
                }
                map.put(entry.getKey(), cookieMap);
            }
            return map;
        }

        @Override
        public void save(@NonNull Map<String, ConcurrentHashMap<String, Cookie>> map) {
            Set<Map.Entry<String, ConcurrentHashMap<String, Cookie>>> entrySet = map.entrySet();
            String cookieKey;
            SharedPreferences.Editor hostEditor = hostSp.edit();
            SharedPreferences.Editor cookieEditor = cookieSp.edit();
            StringBuilder hostBuilder = null;

            boolean firstTime = true;
            for (Map.Entry<String, ConcurrentHashMap<String, Cookie>> hostCookieEntry : entrySet) {
                Set<Map.Entry<String, Cookie>> cookieKeyEntrySet = hostCookieEntry.getValue().entrySet();
                hostBuilder = new StringBuilder();
                for (Map.Entry<String, Cookie> cookieEntry : cookieKeyEntrySet) {
                    if (firstTime) {
                        firstTime = false;
                    } else {
                        hostBuilder.append(hostSeparate);
                    }
                    //拼接一个Host下对应的所有CookieKey
                    hostBuilder.append(cookieEntry.getKey());
                    //持久化CookieKey对应的Cookie
                    saveCookie(cookieEditor, cookieEntry.getKey(), cookieEntry.getValue());
                }
                //持久化Host
                hostEditor.putString(hostCookieEntry.getKey(), hostBuilder.toString());
            }
            hostEditor.apply();
            cookieEditor.apply();
        }

        private void saveCookie(@NonNull SharedPreferences.Editor editor, @NonNull String cookieKey, @NonNull Cookie cookie) {
            StringBuilder builder = new StringBuilder();
            builder.append(cookie.name())
                    .append(cookieSeparate)
                    .append(cookie.value())
                    .append(cookieSeparate)
                    .append(cookie.expiresAt())
                    .append(cookieSeparate)
                    .append(cookie.domain())
                    .append(cookieSeparate)
                    .append(cookie.path())
                    .append(cookieSeparate)
                    .append(cookie.secure())
                    .append(cookieSeparate)
                    .append(cookie.httpOnly())
                    .append(cookieSeparate)
                    .append(cookie.hostOnly());
            editor.putString(cookieKey, builder.toString());
        }

        @Override
        public void remove(@NonNull String cookieKey) {
            cookieSp.edit().remove(cookieKey).apply();
        }

        @Override
        public void removeAll() {
            hostSp.edit().clear().apply();
            cookieSp.edit().clear().apply();
        }

        private Cookie generate(String[] cookieStrArray) {
            Cookie.Builder builder = new Cookie.Builder()
                    .name(cookieStrArray[0])
                    .value(cookieStrArray[1])
                    .expiresAt(Long.parseLong(cookieStrArray[2]))
                    .path(cookieStrArray[4]);
            boolean secure = Boolean.parseBoolean(cookieStrArray[5]);
            boolean httpOnly = Boolean.parseBoolean(cookieStrArray[6]);
            boolean hostOnly = Boolean.parseBoolean(cookieStrArray[7]);

            builder = hostOnly ? builder.hostOnlyDomain(cookieStrArray[3]) : builder.domain(cookieStrArray[3]);
            builder = secure ? builder.secure() : builder;
            builder = httpOnly ? builder.httpOnly() : builder;

            return builder.build();
        }
    }
}
