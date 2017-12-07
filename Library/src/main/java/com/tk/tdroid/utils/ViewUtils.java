package com.tk.tdroid.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/1
 *      desc : View工具类
 * </pre>
 */

public final class ViewUtils {
    private ViewUtils() {
        throw new IllegalStateException();
    }

    /**
     * 父容器是否为空
     *
     * @param viewGroup
     * @return
     */
    public static boolean isEmpty(@Nullable ViewGroup viewGroup) {
        return viewGroup == null || viewGroup.getChildCount() == 0;
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
