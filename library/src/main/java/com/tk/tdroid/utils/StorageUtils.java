package com.tk.tdroid.utils;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/19
 *      desc : 存储工具类
 * </pre>
 */

public final class StorageUtils {

    private StorageUtils() {
        throw new IllegalStateException();
    }

    /**
     * 外置SD卡是否可用
     *
     * @return
     */
    public static boolean isSDEnable() {
        return !getStoragePath(true).isEmpty();
    }

    /**
     * 得到外置存储路径 or SD存储路径
     *
     * @param getSD 是否SD存储路径
     * @return
     */
    public static List<String> getStoragePath(boolean getSD) {
        StorageManager mStorageManager = (StorageManager) Utils.getApp().getSystemService(Context.STORAGE_SERVICE);
        List<String> list = new ArrayList<>();
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Method getState = storageVolumeClazz.getMethod("getState");

            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                String state = (String) getState.invoke(storageVolumeElement);
                if (state.equals("mounted")) {
                    if (removable) {
                        //外置SD
                        if (getSD) {
                            list.add(path);
                        }
                    } else {
                        //内置存储
                        if (!getSD) {
                            list.add(path);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取缓存路径，优先外置存储
     *
     * @return
     */
    public static String getCachePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外置存储可用
            return Utils.getApp().getExternalCacheDir().getAbsolutePath();
        } else {
            return Utils.getApp().getCacheDir().getAbsolutePath();
        }
    }
}
