package com.tk.tdroid.image.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.tk.tdroid.utils.Utils;

import java.security.MessageDigest;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/05
 *     desc   : xxxx描述
 * </pre>
 */
public class Blur extends BitmapTransformation {
    private static final String ID;
    private static final byte[] ID_BYTES;

    private static final int MAX_RADIUS = 25;
    private static final int DEFAULT_DOWN_SAMPLING = 1;

    static {
        ID = "com.tk.tdroid.image.glide.Blur";
        ID_BYTES = ID.getBytes(CHARSET);
    }

    private final int radius;
    private final int sampling;

    public Blur() {
        this(MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
    }

    public Blur(int radius) {
        this(radius, DEFAULT_DOWN_SAMPLING);
    }

    public Blur(int radius, int sampling) {
        this.radius = radius;
        this.sampling = sampling;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool bitmapPool, @NonNull Bitmap bitmap, int outWidth, int outHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int scaledWidth = width / sampling;
        int scaledHeight = height / sampling;

        Bitmap result = bitmapPool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        canvas.scale(1 / (float) sampling, 1 / (float) sampling);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        result = blur(bitmap, radius);

        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Blur blur = (Blur) o;

        if (radius != blur.radius) return false;
        return sampling == blur.sampling;

    }

    @Override
    public int hashCode() {
        int result = radius;
        result = 31 * result + sampling;
        return result;
    }

    /**
     * @param bitmap
     * @param radius
     * @return
     */
    public static Bitmap blur(Bitmap bitmap, int radius) {
        RenderScript rs = null;
        Allocation input = null;
        Allocation output = null;
        ScriptIntrinsicBlur blur = null;
        try {
            rs = RenderScript.create(Utils.getApp());
            rs.setMessageHandler(new RenderScript.RSMessageHandler());
            input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            output = Allocation.createTyped(rs, input.getType());
            blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

            blur.setInput(input);
            blur.setRadius(radius);
            blur.forEach(output);
            output.copyTo(bitmap);
        } finally {
            if (rs != null) {
                rs.destroy();
            }
            if (input != null) {
                input.destroy();
            }
            if (output != null) {
                output.destroy();
            }
            if (blur != null) {
                blur.destroy();
            }
        }

        return bitmap;
    }
}
