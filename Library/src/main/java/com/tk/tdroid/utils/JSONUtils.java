package com.tk.tdroid.utils;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <pre>
 *      author : TK
 *      time : 2018/3/2
 *      desc : JSON工具类
 * </pre>
 */

public final class JSONUtils {

    private JSONUtils() {
        throw new IllegalStateException();
    }

    /**
     * 解析某一条属性
     *
     * @param json
     * @param key
     * @return
     */
    public static String format(@Nullable String json, @Nullable String key) {
        if (EmptyUtils.isEmpty(json) || EmptyUtils.isEmpty(key)) {
            return "";
        }
        try {
            return new JSONObject(json).getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
