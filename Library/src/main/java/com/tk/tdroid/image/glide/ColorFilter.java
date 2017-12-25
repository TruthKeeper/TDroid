package com.tk.tdroid.image.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/05
 *     desc   : xxxx描述
 * </pre>
 */
public class ColorFilter extends BitmapTransformation {
    private static final String ID;
    private static final byte[] ID_BYTES;

    static {
        ID = "com.tk.tdroid.image.glide.ColorFilter";
        ID_BYTES = ID.getBytes(CHARSET);
    }

    private final int color;

    public ColorFilter(int color) {
        this.color = color;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool bitmapPool, @NonNull Bitmap bitmap, int outWidth, int outHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap result = bitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        result.setHasAlpha(true);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0, 0, paint);

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

        ColorFilter that = (ColorFilter) o;

        return color == that.color;

    }

    @Override
    public int hashCode() {
        return color;
    }
}
