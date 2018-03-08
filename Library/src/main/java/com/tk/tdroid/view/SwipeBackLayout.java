package com.tk.tdroid.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tk.tdroid.utils.DensityUtil;

/**
 * <pre>
 *      author : TK
 *      time : 2018/3/2
 *      desc :
 * </pre>
 */

public class SwipeBackLayout extends FrameLayout {
    private final ViewDragHelper mViewDragHelper;
    private final int mXMinVelocity;
    private boolean mEdge = false;

    private OnSwipeBackListener onSwipeBackListener;

    public SwipeBackLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mXMinVelocity = DensityUtil.dp2px(400);
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //边界触摸时手动capture
                return !mEdge;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return Math.max(0, left);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return 0;
            }


            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild.getLeft() > getWidth() / 2 || xvel > mXMinVelocity) {
                    // 当滑动横坐标超过布局的一般宽度||滑动速率较大时，视为侧滑返回
                    mViewDragHelper.settleCapturedViewAt(getWidth(), 0);
                } else {
                    // 否则就还原
                    mViewDragHelper.settleCapturedViewAt(0, 0);
                }
                invalidate();
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mViewDragHelper.captureChildView(getChildAt(0), pointerId);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 1;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                //布局将要退出屏幕时
                if (left >= getWidth()) {
                    if (onSwipeBackListener != null) {
                        onSwipeBackListener.onSwipeBack();
                    }
                }
            }
        });
    }

    /**
     * 设置是否边界触摸侧滑返回
     *
     * @param edge
     */
    public void setEdge(boolean edge) {
        this.mEdge = edge;
        if (mEdge) {
            mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        }
    }

    /**
     * 依附
     * 注意：依附的Activity需要在在AndroidManifest.xml中配置android:theme="@style/SwipeBackTheme"
     * <pre>
     * <style name="SwipeBackTheme" parent="AppTheme">
     *      <item name="android:windowBackground">@android:color/transparent</item>
     *      <item name="android:colorBackgroundCacheHint">@null</item>
     *      <item name="android:windowIsTranslucent">true</item>
     * </style>
     * </pre>
     *
     * @param activity
     * @param edge     时候是触摸左边界才触发侧滑返回
     */
    public static void attach(@NonNull final Activity activity, boolean edge) {
        TypedValue value = new TypedValue();
        activity.getTheme().resolveAttribute(android.R.attr.windowBackground, value, true);

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
        decorView.removeView(decorChild);

        SwipeBackLayout layout = new SwipeBackLayout(activity);
        layout.setOnSwipeBackListener(new OnSwipeBackListener() {
            @Override
            public void onSwipeBack() {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        });
        decorChild.setBackgroundResource(value.resourceId);
        layout.addView(decorChild);
        layout.setEdge(edge);
        decorView.addView(layout);
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    public void setOnSwipeBackListener(OnSwipeBackListener onSwipeBackListener) {
        this.onSwipeBackListener = onSwipeBackListener;
    }

    public interface OnSwipeBackListener {
        void onSwipeBack();
    }
}
