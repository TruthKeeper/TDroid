package com.tk.tdroid.utils;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/9/13
 *     desc   : SharedPreference工具类
 *              配置过多时分割文件等注意事项 -> https://zhuanlan.zhihu.com/p/22913991
 * </pre>
 */
public final class SharedPreferenceUtils {
    private static final String DEFAULT = "TDroid_Default_SP";

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(@NonNull String key, int defaultValue) {
        return getInt(DEFAULT, key, defaultValue);
    }

    /**
     * @param name
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(@NonNull String name, String key, int defaultValue) {
        return Utils.getApp().getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static float getFloat(@NonNull String key, float defaultValue) {
        return getFloat(DEFAULT, key, defaultValue);
    }

    /**
     * @param name
     * @param key
     * @param defaultValue
     * @return
     */
    public static float getFloat(@NonNull String name, String key, float defaultValue) {
        return Utils.getApp().getSharedPreferences(name, Context.MODE_PRIVATE).getFloat(key, defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLong(String key, long defaultValue) {
        return getLong(DEFAULT, key, defaultValue);
    }

    /**
     * @param name
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLong(@NonNull String name, String key, long defaultValue) {
        return Utils.getApp().getSharedPreferences(name, Context.MODE_PRIVATE).getLong(key, defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return getBoolean(DEFAULT, key, defaultValue);
    }

    /**
     * @param name
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(@NonNull String name, String key, boolean defaultValue) {
        return Utils.getApp().getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(@NonNull String key, String defaultValue) {
        return getString(DEFAULT, key, defaultValue);
    }

    /**
     * @param name
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(@NonNull String name, String key, String defaultValue) {
        return Utils.getApp().getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defaultValue);
    }


    /**
     * @param key
     * @param value
     * @return
     */
    public static boolean putInt(@NonNull String key, int value) {
        return putInt(DEFAULT, key, value);
    }

    /**
     * @param name
     * @param key
     * @param value
     * @return
     */
    public static boolean putInt(@NonNull String name, @NonNull String key, int value) {
        return Utils.getApp().getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit().putInt(key, value).commit();
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public static boolean putFloat(@NonNull String key, float value) {
        return putFloat(DEFAULT, key, value);
    }

    /**
     * @param name
     * @param key
     * @param value
     * @return
     */
    public static boolean putFloat(@NonNull String name, @NonNull String key, float value) {
        return Utils.getApp().getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit().putFloat(key, value).commit();
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public static boolean putLong(@NonNull String key, long value) {
        return putLong(DEFAULT, key, value);
    }

    /**
     * @param name
     * @param key
     * @param value
     * @return
     */
    public static boolean putLong(@NonNull String name, @NonNull String key, long value) {
        return Utils.getApp().getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit().putLong(key, value).commit();
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public static boolean putBoolean(@NonNull String key, boolean value) {
        return putBoolean(DEFAULT, key, value);
    }

    /**
     * @param name
     * @param key
     * @param value
     * @return
     */
    public static boolean putBoolean(@NonNull String name, @NonNull String key, boolean value) {
        return Utils.getApp().getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit().putBoolean(key, value).commit();
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public static boolean putString(@NonNull String key, String value) {
        return putString(DEFAULT, key, value);
    }

    /**
     * @param name
     * @param key
     * @param value
     * @return
     */
    public static boolean putString(@NonNull String name, @NonNull String key, String value) {
        return Utils.getApp().getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit().putString(key, value).commit();
    }
}
