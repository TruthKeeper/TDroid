package com.tk.tdroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.tk.tdroid.R;

/**
 * <pre>
 *      author : TK
 *      time : 2018/11/21
 *      desc : 同心圆环控件
 * </pre>
 */
public class RingView extends View {
    /**
     * 圆心半径，百分比
     */
    public static final float RADIUS = 0.32F;
    /**
     * 圆环粗细，百分比
     */
    public static final float STROKE = 0.05F;
    private final Paint mPaint = new Paint();
    private float mStroke;
    private float mRadius;
    private boolean isChecked = false;

    public RingView(Context context) {
        super(context);
        init();
    }

    public RingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RingView);
        isChecked = array.getBoolean(R.styleable.RingView_rv_checked, isChecked);
        int color = array.getColor(R.styleable.RingView_rv_color, ContextCompat.getColor(context, Color.GRAY));
        array.recycle();
        mPaint.setColor(color);
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isChecked) {
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, (getWidth() - mStroke) / 2, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mRadius, mPaint);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, (getWidth() - mStroke) / 2, mPaint);
        }
    }

    public void setChecked(boolean checked) {
        if (isChecked != checked) {
            isChecked = checked;
            postInvalidate();
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStroke = w * STROKE;
        mRadius = w * RADIUS;
        mPaint.setStrokeWidth(mStroke);
    }
}
