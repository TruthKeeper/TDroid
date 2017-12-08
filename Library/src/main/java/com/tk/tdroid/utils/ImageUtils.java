package com.tk.tdroid.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.io.ByteArrayOutputStream;
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
    /**
     * 通过Drawable来获取Bitmap
     *
     * @param resId
     * @return
     */
    public static Bitmap drawable2Bitmap(@DrawableRes int resId) {
        return drawable2Bitmap(ContextCompat.getDrawable(Utils.getApp(), resId));
    }

    /**
     * 通过Drawable来获取Bitmap
     *
     * @param resId
     * @param outputWidth
     * @param outputHeight
     * @return
     */
    public static Bitmap drawable2Bitmap(@DrawableRes int resId, int outputWidth, int outputHeight) {
        Drawable drawable = ContextCompat.getDrawable(Utils.getApp(), resId);
        Bitmap bitmap = Bitmap.createBitmap(outputWidth, outputHeight, drawable.getOpacity() != PixelFormat.OPAQUE
                ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, outputWidth, outputHeight);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 通过Drawable来获取Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(@NonNull Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(w, h, drawable.getOpacity() != PixelFormat.OPAQUE
                ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 通过Drawable来获取Bitmap
     *
     * @param drawable
     * @param outputWidth
     * @param outputHeight
     * @return
     */
    public static Bitmap drawable2Bitmap(@NonNull Drawable drawable, int outputWidth, int outputHeight) {
        Bitmap bitmap = Bitmap.createBitmap(outputWidth, outputHeight, drawable.getOpacity() != PixelFormat.OPAQUE
                ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, outputWidth, outputHeight);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 通过Bitmap来获取Drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(@NonNull Bitmap bitmap) {
        return new BitmapDrawable(Utils.getApp().getResources(), bitmap);
    }

    /**
     * 通过Bitmap来获取字节数组
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2Bytes(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        IOUtils.closeQuietly(baos);
        return bytes;
    }

    /**
     * 通过字节数组来获取Bitmap
     *
     * @param bytes
     * @return
     */
    public static Bitmap bytes2Bitmap(@NonNull byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
