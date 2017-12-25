package com.tk.tdroid.image.glide;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/05
 *     desc   : xxxx描述
 * </pre>
 */

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.MessageDigest;

import static com.tk.tdroid.image.glide.RoundCorner.CornerType.ALL;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.BOTTOM;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.BOTTOM_LEFT;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.BOTTOM_RIGHT;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.LEFT;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.OTHER_BOTTOM_LEFT;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.OTHER_BOTTOM_RIGHT;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.OTHER_TOP_LEFT;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.OTHER_TOP_RIGHT;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.RIGHT;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.TOP;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.TOP_LEFT;
import static com.tk.tdroid.image.glide.RoundCorner.CornerType.TOP_RIGHT;

public class RoundCorner extends BitmapTransformation {
    private static final String ID;
    private static final byte[] ID_BYTES;

    static {
        ID = "com.tk.tdroid.image.glide.RoundCorner";
        ID_BYTES = ID.getBytes(CHARSET);
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ALL, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
            TOP, BOTTOM, LEFT, RIGHT, OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT})
    public @interface CornerType {
        int ALL = 0x01;
        int TOP_LEFT = 0x02;
        int TOP_RIGHT = 0x03;
        int BOTTOM_LEFT = 0x04;
        int BOTTOM_RIGHT = 0x05;
        int TOP = 0x06;
        int BOTTOM = 0x07;
        int LEFT = 0x08;
        int RIGHT = 0x09;
        int OTHER_TOP_LEFT = 0x0a;
        int OTHER_TOP_RIGHT = 0x0b;
        int OTHER_BOTTOM_LEFT = 0x0c;
        int OTHER_BOTTOM_RIGHT = 0x0d;
    }

    private final int radius;
    private final int margin;
    @CornerType
    private final int cornerType;

    public RoundCorner(int radius) {
        this(radius, 0, ALL);
    }

    public RoundCorner(int radius, int margin) {
        this(radius, margin, ALL);
    }

    public RoundCorner(int radius, int margin, @CornerType int cornerType) {
        this.radius = radius;
        this.margin = margin;
        this.cornerType = cornerType;
    }


    @Override
    protected Bitmap transform(@NonNull BitmapPool bitmapPool, @NonNull Bitmap bitmap, int outWidth, int outHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap result = bitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        result.setHasAlpha(true);

        Canvas canvas = new Canvas(result);
        Path path = new Path();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        drawRoundRect(canvas, path, paint, width, height);

        return result;
    }

    private void drawRoundRect(final Canvas canvas, final Path path, final Paint paint, final float width, final float height) {
        float right = width - margin;
        float bottom = height - margin;
        float[] radiusArray = null;
        switch (cornerType) {
            case ALL:
                radiusArray = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
                break;
            case TOP_LEFT:
                radiusArray = new float[]{radius, radius, 0, 0, 0, 0, 0, 0};
                break;
            case TOP_RIGHT:
                radiusArray = new float[]{0, 0, radius, radius, 0, 0, 0, 0};
                break;
            case BOTTOM_LEFT:
                radiusArray = new float[]{0, 0, 0, 0, 0, 0, radius, radius};
                break;
            case BOTTOM_RIGHT:
                radiusArray = new float[]{0, 0, 0, 0, radius, radius, 0, 0};
                break;
            case TOP:
                radiusArray = new float[]{radius, radius, radius, radius, 0, 0, 0, 0};
                break;
            case BOTTOM:
                radiusArray = new float[]{0, 0, 0, 0, radius, radius, radius, radius};
                break;
            case LEFT:
                radiusArray = new float[]{radius, radius, 0, 0, 0, 0, radius, radius};
                break;
            case RIGHT:
                radiusArray = new float[]{0, 0, radius, radius, radius, radius, 0, 0};
                break;
            case OTHER_TOP_LEFT:
                radiusArray = new float[]{0, 0, radius, radius, radius, radius, radius, radius};
                break;
            case OTHER_TOP_RIGHT:
                radiusArray = new float[]{radius, radius, 0, 0, radius, radius, radius, radius};
                break;
            case OTHER_BOTTOM_LEFT:
                radiusArray = new float[]{radius, radius, radius, radius, radius, radius, 0, 0};
                break;
            case OTHER_BOTTOM_RIGHT:
                radiusArray = new float[]{radius, radius, radius, radius, 0, 0, radius, radius};
                break;

        }
        path.addRoundRect(new RectF(margin, margin, right, bottom), radiusArray, Path.Direction.CW);
        canvas.drawPath(path, paint);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoundCorner that = (RoundCorner) o;

        if (radius != that.radius) return false;
        if (margin != that.margin) return false;
        return cornerType == that.cornerType;
    }

    @Override
    public int hashCode() {
        int result = radius;
        result = 31 * result + margin;
        result = 31 * result + cornerType;
        return result;
    }
}
