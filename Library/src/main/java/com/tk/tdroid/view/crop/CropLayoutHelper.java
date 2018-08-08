package com.tk.tdroid.view.crop;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.StyleableRes;
import android.view.MotionEvent;

import com.tk.tdroid.utils.SelectorFactory;

import java.util.Arrays;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/5/24
 *     desc   :
 * </pre>
 */
public class CropLayoutHelper {
    private Builder mBuilder;

    private final float[] mRadius = new float[8];
    private final Path mCropPath;
    private final Paint mPaint;
    private boolean mAsCircle = false;
    private boolean mStrokeAbove = false;
    private int mStrokeWidth;
    private int mStrokeColor;
    private final Region mAreaRegion;
    private RectF mLayer;

    private CropLayoutHelper(Builder builder) {
        this.mBuilder = builder;
        mCropPath = new Path();
        mAreaRegion = new Region();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);

        if (mBuilder.corner > 0) {
            Arrays.fill(mRadius, mBuilder.corner);
        } else {
            mRadius[0] = mRadius[1] = mBuilder.topLeftCorner;
            mRadius[2] = mRadius[3] = mBuilder.topRightCorner;
            mRadius[4] = mRadius[5] = mBuilder.bottomRightCorner;
            mRadius[6] = mRadius[7] = mBuilder.bottomLeftCorner;
        }

    }

    public void onSizeChanged(int w, int h) {
        mLayer = new RectF(0, 0, w, h);
        mCropPath.reset();
        if (mAsCircle) {
            mCropPath.addCircle(w >> 1, h >> 1, Math.min(w, h) / 2, Path.Direction.CW);
            //空操作,使Path区域占满画布
            mCropPath.moveTo(-1, -1);
            mCropPath.moveTo(w + 1, h + 1);
        } else {
            mCropPath.addRoundRect(new RectF(0, 0, w, h), mRadius, Path.Direction.CW);
        }
        mAreaRegion.setPath(mCropPath, new Region(0, 0, w, h));
    }

    public void beforeDispatchDraw(Canvas canvas) {
        canvas.saveLayer(mLayer, null, Canvas.ALL_SAVE_FLAG);
    }

    public void afterDispatchDraw(Canvas canvas) {
        if (mStrokeWidth > 0) {
            mPaint.setStrokeWidth(mStrokeWidth * 2);
            mPaint.setStyle(Paint.Style.STROKE);
            if (mStrokeAbove) {
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                mPaint.setColor(mStrokeColor);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(mCropPath, mPaint);
            } else {
                //裁减掉Stroke的内容
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                mPaint.setColor(Color.WHITE);
                canvas.drawPath(mCropPath, mPaint);
                // 绘制描边
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                mPaint.setColor(mStrokeColor);
                canvas.drawPath(mCropPath, mPaint);
            }
        }
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mCropPath, mPaint);
        canvas.restore();
    }

    public boolean processTouchEvent(MotionEvent ev) {
        if (mAreaRegion.contains((int) ev.getX(), (int) ev.getY())) {
            //在区域中才响应事件
            return true;
        }
        return false;
    }

    public static final class Builder {
        private TypedArray typedArray;
        private boolean asCircle = false;
        private int corner = 0;
        private int topLeftCorner = 0;
        private int topRightCorner = 0;
        private int bottomRightCorner = 0;
        private int bottomLeftCorner = 0;
        private int strokeWidth = 0;
        private int strokeColor = SelectorFactory.INVALID;
        private boolean strokeAbove = false;

        public Builder(TypedArray typedArray) {
            this.typedArray = typedArray;
        }

        public Builder asCircle(@StyleableRes int asCircle) {
            this.asCircle = typedArray.getBoolean(asCircle, false);
            return this;
        }

        public Builder corner(@StyleableRes int corner) {
            this.corner = typedArray.getDimensionPixelOffset(corner, 0);
            return this;
        }

        public Builder topLeftCorner(@StyleableRes int topLeftCorner) {
            this.topLeftCorner = typedArray.getDimensionPixelOffset(topLeftCorner, 0);
            return this;
        }

        public Builder topRightCorner(@StyleableRes int topRightCorner) {
            this.topRightCorner = typedArray.getDimensionPixelOffset(topRightCorner, 0);
            return this;
        }

        public Builder bottomRightCorner(@StyleableRes int bottomRightCorner) {
            this.bottomRightCorner = typedArray.getDimensionPixelOffset(bottomRightCorner, 0);
            return this;
        }

        public Builder bottomLeftCorner(@StyleableRes int bottomLeftCorner) {
            this.bottomLeftCorner = typedArray.getDimensionPixelOffset(bottomLeftCorner, 0);
            return this;
        }

        public Builder strokeWidth(@StyleableRes int strokeWidth) {
            this.strokeWidth = typedArray.getDimensionPixelOffset(strokeWidth, 0);
            return this;
        }

        public Builder strokeColor(@StyleableRes int strokeColor) {
            this.strokeColor = typedArray.getColor(strokeColor, SelectorFactory.INVALID);
            return this;
        }

        public Builder strokeAbove(@StyleableRes int strokeAbove) {
            this.strokeAbove = typedArray.getBoolean(strokeAbove, false);
            return this;
        }

        public CropLayoutHelper build() {
            return new CropLayoutHelper(this);
        }
    }
}
