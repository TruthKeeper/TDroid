package com.tk.tdroid.autoinject;

import android.app.Activity;
import android.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/26
 *     desc   : xxxx描述
 * </pre>
 */
public final class AutoInjectHelper {
    private static final Map<String, IAutoInject> CACHE = new HashMap<>();
    private static final String SUFFIX = "_AutoInject";

    private AutoInjectHelper() {
        throw new IllegalStateException();
    }

    public static void inject(Activity activity) {
        injectByCache(activity);
    }

    public static void inject(Fragment fragment) {
        injectByCache(fragment);
    }

    public static void inject(android.support.v4.app.Fragment fragment) {
        injectByCache(fragment);
    }

    @SuppressWarnings("unchecked")
    private static void injectByCache(Object object) {
        try {
            String findClassName = object.getClass().getName() + SUFFIX;
            IAutoInject iAutoInject = CACHE.get(findClassName);
            if (iAutoInject == null) {
                Class<?> findClass = Class.forName(findClassName);
                iAutoInject = (IAutoInject) findClass.newInstance();
                CACHE.put(findClassName, iAutoInject);
            }
            //注入
            iAutoInject.inject(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
