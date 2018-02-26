package com.tk.tdroid.saverestore;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/16
 *     desc   : 支持基本类型、数组、{@link android.os.Parcelable}、{@link java.io.Serializable}和{@link com.google.gson.Gson}
 *     详情见APT工程
 *
 * </pre>
 */
public final class SaveRestoreHelper {
    private static final Map<Class, ISaveRestore> CACHE = new HashMap<>();
    private static final String SUFFIX = "_SaveRestore";

    private SaveRestoreHelper() {
        throw new IllegalStateException();
    }

    public static <T> void onRestoreInstanceState(T object, Bundle savedInstanceState) {
        ISaveRestore<T> saveHelper = findSaveHelper(object);
        if (saveHelper != null) {
            saveHelper.restore(object, savedInstanceState);
        }
    }

    public static <T> void onSaveInstanceState(T object, Bundle outState) {
        ISaveRestore<T> saveHelper = findSaveHelper(object);
        if (saveHelper != null) {
            saveHelper.save(object, outState);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T> ISaveRestore<T> findSaveHelper(T object) {
        ISaveRestore iSaveRestore = CACHE.get(object.getClass());
        if (iSaveRestore == null) {
            try {
                Class<?> findClass = Class.forName(object.getClass().getName() + SUFFIX);
                iSaveRestore = (ISaveRestore) findClass.newInstance();
                CACHE.put(object.getClass(), iSaveRestore);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return iSaveRestore;
    }
}
