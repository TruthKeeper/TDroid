package com.tk.tdroid.utils;

import android.media.ExifInterface;
import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/12/7
 *     desc   : 图片工具类
 * </pre>
 */
public final class ImageUtils {
    private ImageUtils() {
        throw new IllegalStateException();
    }

    /**
     * 获取图像旋转角度
     *
     * @param path
     * @return
     */
    public static int getImageDegree(@NonNull String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
