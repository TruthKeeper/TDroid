package com.tk.tdroid.view.crop;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.tk.tdroid.R;


/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/19
 *     desc   : 包裹裁剪容器，支持Stroke , Thanks for <a href="https://github.com/GcsSloop/rclayout">rclayout</a>
 * </pre>
 */
public class CropLinearLayout extends LinearLayout {
    private CropLayoutHelper cropLayoutHelper;

    public CropLinearLayout(Context context) {
        this(context, null);
    }

    public CropLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs == null) {
            cropLayoutHelper = new CropLayoutHelper.Builder(null).build();
        } else {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CropLinearLayout);
            cropLayoutHelper = new CropLayoutHelper.Builder(typedArray)
                    .asCircle(R.styleable.CropLinearLayout_crop_asCircle)
                    .corner(R.styleable.CropLinearLayout_crop_corner)
                    .topLeftCorner(R.styleable.CropLinearLayout_crop_topLeftCorner)
                    .topRightCorner(R.styleable.CropLinearLayout_crop_topRightCorner)
                    .bottomRightCorner(R.styleable.CropLinearLayout_crop_bottomRightCorner)
                    .bottomLeftCorner(R.styleable.CropLinearLayout_crop_bottomLeftCorner)
                    .strokeWidth(R.styleable.CropLinearLayout_crop_strokeWidth)
                    .strokeColor(R.styleable.CropLinearLayout_crop_strokeColor)
                    .strokeAbove(R.styleable.CropLinearLayout_crop_strokeAbove)
                    .build();
            typedArray.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cropLayoutHelper.onSizeChanged(w, h);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        cropLayoutHelper.beforeDispatchDraw(canvas);
        super.dispatchDraw(canvas);
        cropLayoutHelper.afterDispatchDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return cropLayoutHelper.processTouchEvent(ev) && super.dispatchTouchEvent(ev);
    }
}
