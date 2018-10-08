package com.tk.tdroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.tk.tdroid.R;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/8/23
 *     desc   : 吸边的拖拽悬浮窗包裹容器
 * </pre>
 */
public class PlayGroundContainer extends FrameLayout {
    private ViewDragHelper dragHelper;
    private int viewId;

    public PlayGroundContainer(@NonNull Context context) {
        this(context, null);
    }

    public PlayGroundContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayGroundContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlayGroundContainer);
            viewId = typedArray.getResourceId(R.styleable.PlayGroundContainer_pgc_view, 0);
            typedArray.recycle();
        }

        dragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return viewId != 0 && viewId == child.getId();
            }

            public int getViewHorizontalDragRange(@NonNull View child) {
                return getWidth() - child.getWidth();
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return getHeight() - child.getHeight();
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                if (top > getHeight() - child.getHeight()) {
                    top = getHeight() - child.getHeight();
                } else if (top < 0) {
                    top = 0;
                }
                return top;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                if (left > getWidth() - child.getWidth()) {
                    left = getWidth() - child.getMeasuredWidth();
                } else if (left < 0) {
                    left = 0;
                }
                return left;
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                //吸边
                int centerX = releasedChild.getLeft() + releasedChild.getRight() >> 1;
                if (centerX > getWidth() >> 1) {
                    //->
                    dragHelper.smoothSlideViewTo(releasedChild,
                            getWidth() - releasedChild.getWidth(),
                            releasedChild.getTop());
                } else {
                    //<-
                    dragHelper.smoothSlideViewTo(releasedChild,
                            0,
                            releasedChild.getTop());
                }
                postInvalidate();
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                LayoutParams layoutParams = (LayoutParams) changedView.getLayoutParams();
                layoutParams.leftMargin = left;
                layoutParams.topMargin = top;
                layoutParams.rightMargin = 0;
                layoutParams.bottomMargin = 0;
                layoutParams.gravity = 0;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            postInvalidate();
        }
    }
}
