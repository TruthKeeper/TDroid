package com.tk.tdroid.save;

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
public final class SaveHelper {
    private static final Map<Class, ISaveHelper> CACHE = new HashMap<>();
    private static final String SUFFIX = "_Save";

    private SaveHelper() {
        throw new IllegalStateException();
    }

    public static <T> void onRestoreInstanceState(T object, Bundle savedInstanceState) {
        ISaveHelper<T> saveHelper = findSaveHelper(object);
        if (saveHelper != null) {
            saveHelper.restore(object, savedInstanceState);
        }
    }

    public static <T> void onSaveInstanceState(T object, Bundle outState) {
        ISaveHelper<T> saveHelper = findSaveHelper(object);
        if (saveHelper != null) {
            saveHelper.save(object, outState);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T> ISaveHelper<T> findSaveHelper(T object) {
        ISaveHelper iSaveHelper = CACHE.get(object.getClass());
        if (iSaveHelper == null) {
            try {
                Class<?> findClass = Class.forName(object.getClass().getName() + SUFFIX);
                iSaveHelper = (ISaveHelper) findClass.newInstance();
                CACHE.put(object.getClass(), iSaveHelper);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return iSaveHelper;
    }
}
