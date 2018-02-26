package com.tk.tdroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.tk.tdroid.R;

import java.util.Arrays;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/19
 *     desc   : 包裹裁剪容器，支持Stroke , Thanks for <a href="https://github.com/GcsSloop/rclayout">rclayout</a>
 * </pre>
 */
public class CropLayout extends RelativeLayout {
    private final float[] mRadius = new float[8];
    private final Path mCropPath;
    private final Paint mPaint;
    private boolean mAsCircle = false;
    private boolean mStrokeAbove = false;
    private int mStrokeWidth;
    private int mStrokeColor;
    private final Region mAreaRegion;
    private RectF mLayer;

    public CropLayout(Context context) {
        this(context, null);
    }

    public CropLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CropLayout);
            mAsCircle = typedArray.getBoolean(R.styleable.CropLayout_cl_asCircle, false);
            mStrokeAbove = typedArray.getBoolean(R.styleable.CropLayout_cl_strokeAbove, false);
            mStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.CropLayout_cl_strokeWidth, 0);
            mStrokeColor = typedArray.getColor(R.styleable.CropLayout_cl_strokeColor, Color.WHITE);

            int radius = typedArray.getDimensionPixelOffset(R.styleable.CropLayout_cl_radius, Color.WHITE);
            if (radius > 0) {
                Arrays.fill(mRadius, radius);
            } else {
                int topLeftRadius = typedArray.getDimensionPixelOffset(R.styleable.CropLayout_cl_topLeftRadius, 0);
                int topRightRadius = typedArray.getDimensionPixelOffset(R.styleable.CropLayout_cl_topRightRadius, 0);
                int bottomRightRadius = typedArray.getDimensionPixelOffset(R.styleable.CropLayout_cl_bottomRightRadius, 0);
                int bottomLeftRadius = typedArray.getDimensionPixelOffset(R.styleable.CropLayout_cl_bottomLeftRadius, 0);
                mRadius[0] = mRadius[1] = topLeftRadius;
                mRadius[2] = mRadius[3] = topRightRadius;
                mRadius[4] = mRadius[5] = bottomRightRadius;
                mRadius[6] = mRadius[7] = bottomLeftRadius;
            }
            typedArray.recycle();
        }
        mCropPath = new Path();
        mAreaRegion = new Region();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(mLayer, null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mAreaRegion.contains((int) ev.getX(), (int) ev.getY())) {
            //不在区域中不响应事件
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }
}
