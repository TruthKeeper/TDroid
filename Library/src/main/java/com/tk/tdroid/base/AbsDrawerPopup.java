package com.tk.tdroid.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.PopupWindow;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/9/17
 *     desc   : 抽屉式的{@link PopupWindow}
 * </pre>
 */
public abstract class AbsDrawerPopup extends PopupWindow {
    public enum Position {
        LEFT, TOP, RIGHT, BOTTOM
    }

    private final int mWidth;
    private final int mHeight;
    private final int mShowDuration;
    private final int mDismissDuration;
    private final Position mShowPosition;
    private final Position mDismissPosition;
    private final Interpolator mShowInterpolator;
    private final Interpolator mDismissInterpolator;

    public AbsDrawerPopup(Context context) {
        super(context);
        setContentView(LayoutInflater.from(context).inflate(getLayoutId(), null));

        mWidth = getWidth();
        mHeight = getHeight();
        mShowDuration = getShowDuration();
        mDismissDuration = getDismissDuration();
        mShowPosition = getShowPosition();
        mDismissPosition = getDismissPosition();
        mShowInterpolator = getShowInterpolator();
        mDismissInterpolator = getDismissInterpolator();

        setWidth(mWidth);
        setHeight(mHeight);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAnimationStyle(0);
    }

    @Override
    public void dismiss() {
        ViewPropertyAnimator animator = getContentView().animate()
                .setDuration(mDismissDuration)
                .setInterpolator(mDismissInterpolator)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        AbsDrawerPopup.super.dismiss();
                    }
                });

        switch (mDismissPosition) {
            case LEFT:
                animator.translationX(-mWidth);
                animator.translationY(0);
                break;
            case TOP:
                animator.translationX(0);
                animator.translationY(-mHeight);
                break;
            case RIGHT:
                animator.translationX(mWidth);
                animator.translationY(0);
                break;
            case BOTTOM:
                animator.translationX(0);
                animator.translationY(mHeight);
                break;
        }
        animator.start();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        showAnim();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        showAnim();
    }

    private void showAnim() {
        switch (mShowPosition) {
            case LEFT:
                getContentView().setTranslationX(-mWidth);
                getContentView().setTranslationY(0);
                break;
            case TOP:
                getContentView().setTranslationX(0);
                getContentView().setTranslationY(-mHeight);
                break;
            case RIGHT:
                getContentView().setTranslationX(mWidth);
                getContentView().setTranslationY(0);
                break;
            case BOTTOM:
                getContentView().setTranslationX(0);
                getContentView().setTranslationY(mHeight);
                break;
        }
        ViewPropertyAnimator animator = getContentView().animate()
                .setDuration(mShowDuration)
                .setInterpolator(mShowInterpolator);

        switch (mShowPosition) {
            case LEFT:
                animator.translationX(0);
                break;
            case TOP:
                animator.translationY(0);
                break;
            case RIGHT:
                animator.translationX(0);
                break;
            case BOTTOM:
                animator.translationY(0);
                break;
        }
        animator.start();
    }

    /**
     * 布局Id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 高度
     *
     * @return
     */
    public abstract int getHeight();

    /**
     * 宽度
     *
     * @return
     */
    public abstract int getWidth();

    /**
     * 进场的位置
     *
     * @return
     */
    public abstract Position getShowPosition();

    /**
     * 出场的位置
     *
     * @return
     */
    public abstract Position getDismissPosition();

    /**
     * 进场动画时长
     *
     * @return
     */
    public int getShowDuration() {
        return 300;
    }

    /**
     * 出场动画时长
     *
     * @return
     */
    public int getDismissDuration() {
        return 300;
    }

    /**
     * 进场动画的插值器
     *
     * @return
     */
    public Interpolator getShowInterpolator() {
        return new LinearInterpolator();
    }

    /**
     * 出场动画的插值器
     *
     * @return
     */
    public Interpolator getDismissInterpolator() {
        return new LinearInterpolator();
    }
}
