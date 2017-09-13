package com.tk.tdroid.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/9/13
 *     desc   : 文件工具
 * </pre>
 */
public final class FileUtils {
    private FileUtils() {
        throw new IllegalStateException();
    }

    /**
     * 外置SD卡是否可用
     *
     * @param context
     * @return
     */
    public static boolean isSDEnable(@NonNull Context context) {
        String sdPath = getStoragePath(context, true);
        if (null == sdPath) {
            return false;
        } else {
            File sdFile = new File(sdPath);
            if (!sdFile.exists() || 0 == sdFile.length()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 得到内置/外置存储路径
     *
     * @param context
     * @param getSD
     * @return
     */
    public static String getStoragePath(@NonNull Context context, boolean getSD) {
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (getSD == removable) {
                    return path;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取剩余可用空间
     *
     * @param context
     * @return
     */
    public static String getEnableSpace(@NonNull Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize((totalBlocks - availableBlocks) * blockSize, 1);
    }

    /**
     * 获取缓存路径，优先磁盘
     *
     * @param context
     * @return
     */
    public static String getDiskCacheDir(@NonNull Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * 获得当前缓存大小
     *
     * @param context
     * @return
     */
    private static long getCacheSize(@NonNull Context context) {
        long cacheSize;
        cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return cacheSize;
    }

    /**
     * 获得当前缓存大小
     *
     * @param context
     * @return
     */
    public static String getCacheSizeFormat(@NonNull Context context) {
        return formatSize(getCacheSize(context), 2);
    }

    /**
     * 清理所有缓存
     *
     * @param context
     */
    public static void clearAllCache(@NonNull Context context) {
        deleteFiles(context.getCacheDir());
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            deleteFiles(context.getExternalCacheDir());
        }
    }

    /**
     * 删除指定文件或者目录下的所有文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFiles(@NonNull File file) {
        if (file.isDirectory()) {
            String[] children = file.list();
            if (children == null) {
                return false;
            }
            for (String name : children) {
                if (!deleteFiles(new File(file, name))) {
                    return false;
                }
            }
            return true;
        }
        return file.delete();
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @param scale
     * @return
     */
    public static String getFileSizeFormat(@NonNull File file, int scale) {
        return formatSize(getFolderSize(file), scale);
    }

    /**
     * 获取文件的大小
     *
     * @param file
     * @return
     */
    private static long getFolderSize(@NonNull File file) {
        long size = 0;
        File[] fileList = file.listFiles();
        for (File f : fileList) {
            if (f.isDirectory()) {
                size += getFolderSize(f);
            } else {
                size += f.length();
            }
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @param scale 保留几位小数
     * @return
     */
    public static String formatSize(double size, int scale) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(scale, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(scale, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(scale, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

}
