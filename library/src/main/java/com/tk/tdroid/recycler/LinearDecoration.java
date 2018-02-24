package com.tk.tdroid.recycler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tk.tdroid.recycler.adapter.FasterAdapter;


/**
 * <pre>
 *      author : TK
 *      time : 2017/10/30
 *      desc : 线性、纵向分割线
 * </pre>
 */

public final class LinearDecoration extends RecyclerView.ItemDecoration {

    private Drawable drawable;
    private int dividerBgColor;
    private int dividerColor;

    private int leftDivider;
    private int leftDividerPaddingTop;
    private int leftDividerPaddingBottom;

    private int topDivider;
    private int topDividerPaddingLeft;
    private int topDividerPaddingRight;

    private int rightDivider;
    private int rightDividerPaddingTop;
    private int rightDividerPaddingBottom;

    private int bottomDivider;
    private int bottomDividerPaddingLeft;
    private int bottomDividerPaddingRight;

    private boolean ignoreStart;
    private boolean ignoreEnd;
    private int[] ignoreViewTypes;

    private LinearDecoration(Vertical vertical) {
        drawable = vertical.drawable;
        dividerBgColor = vertical.dividerBgColor;
        dividerColor = vertical.dividerColor;
        ignoreStart = vertical.ignoreStart;
        ignoreEnd = vertical.ignoreEnd;
        ignoreViewTypes = vertical.ignoreViewTypes;
        topDivider = vertical.topDivider;
        topDividerPaddingLeft = vertical.topDividerPaddingLeft;
        topDividerPaddingRight = vertical.topDividerPaddingRight;
        bottomDivider = vertical.bottomDivider;
        bottomDividerPaddingLeft = vertical.bottomDividerPaddingLeft;
        bottomDividerPaddingRight = vertical.bottomDividerPaddingRight;
    }

    private LinearDecoration(Horizontal horizontal) {
        drawable = horizontal.drawable;
        dividerBgColor = horizontal.dividerBgColor;
        dividerColor = horizontal.dividerColor;
        ignoreStart = horizontal.ignoreStart;
        ignoreEnd = horizontal.ignoreEnd;
        ignoreViewTypes = horizontal.ignoreViewTypes;
        leftDivider = horizontal.leftDivider;
        leftDividerPaddingTop = horizontal.leftDividerPaddingTop;
        leftDividerPaddingBottom = horizontal.leftDividerPaddingBottom;
        rightDivider = horizontal.rightDivider;
        rightDividerPaddingTop = horizontal.rightDividerPaddingTop;
        rightDividerPaddingBottom = horizontal.rightDividerPaddingBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (canDraw(view, parent)) {
            outRect.set(leftDivider, topDivider, rightDivider, bottomDivider);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            draw(c, parent);
        }
    }

    private void draw(Canvas c, RecyclerView parent) {
        final RecyclerView.LayoutManager manager = parent.getLayoutManager();
        final int childCount = parent.getLayoutManager().getChildCount();
        final Rect decorationRect = new Rect();
        int translationX;
        int translationY;
        int left;
        int top;
        int right;
        int bottom;
        View child;
        ColorDrawable realColorDrawable;
        LayerDrawable realLayerDrawable;
        for (int i = 0; i < childCount; i++) {
            child = manager.getChildAt(i);
            manager.calculateItemDecorationsForChild(child, decorationRect);

            translationX = Math.round(ViewCompat.getTranslationX(child));
            translationY = Math.round(ViewCompat.getTranslationY(child));

            left = child.getLeft() - leftDivider + translationX;
            top = child.getTop() - topDivider + translationY;
            right = child.getRight() + rightDivider + translationX;
            bottom = child.getBottom() + bottomDivider + translationY;
            if (canDraw(child, parent)) {
                if (leftDivider > 0) {
                    if (drawable != null) {
                        //直接绘制
                        drawable.setBounds(left, top + topDivider,
                                left + leftDivider, bottom - bottomDivider);
                        drawable.draw(c);
                    } else {
                        if (dividerBgColor == Color.TRANSPARENT) {
                            //纯色绘制
                            if (dividerColor != Color.TRANSPARENT) {
                                realColorDrawable = new ColorDrawable(dividerColor);
                                realColorDrawable.setBounds(left, top + topDivider,
                                        left + leftDivider, bottom - bottomDivider);
                                realColorDrawable.draw(c);
                            }
                        } else {
                            //Layer绘制
                            GradientDrawable bg = new GradientDrawable();
                            bg.setColor(dividerBgColor);
                            GradientDrawable divider = new GradientDrawable();
                            divider.setColor(dividerColor);
                            realLayerDrawable = new LayerDrawable(new Drawable[]{bg, divider});
                            realLayerDrawable.setLayerInset(1, 0, leftDividerPaddingTop, 0, leftDividerPaddingBottom);
                            realLayerDrawable.setBounds(left, top + topDivider,
                                    left + leftDivider, bottom - bottomDivider);
                            realLayerDrawable.draw(c);
                        }
                    }
                }
                if (topDivider > 0) {
                    if (drawable != null) {
                        //直接绘制
                        drawable.setBounds(left - leftDivider, top,
                                right - rightDivider, top + bottomDivider);
                        drawable.draw(c);
                    } else {
                        if (dividerBgColor == Color.TRANSPARENT) {
                            //纯色绘制
                            if (dividerColor != Color.TRANSPARENT) {
                                realColorDrawable = new ColorDrawable(dividerColor);
                                realColorDrawable.setBounds(left - leftDivider, top,
                                        right - rightDivider, top + bottomDivider);
                                realColorDrawable.draw(c);
                            }
                        } else {
                            //Layer绘制
                            GradientDrawable bg = new GradientDrawable();
                            bg.setColor(dividerBgColor);
                            GradientDrawable divider = new GradientDrawable();
                            divider.setColor(dividerColor);
                            realLayerDrawable = new LayerDrawable(new Drawable[]{bg, divider});
                            realLayerDrawable.setLayerInset(1, topDividerPaddingLeft, 0, topDividerPaddingRight, 0);
                            realLayerDrawable.setBounds(left - leftDivider, top,
                                    right - rightDivider, top + bottomDivider);
                            realLayerDrawable.draw(c);
                        }
                    }
                }
                if (rightDivider > 0) {
                    if (drawable != null) {
                        //直接绘制
                        drawable.setBounds(right - rightDivider, top + topDivider,
                                right, bottom - bottomDivider);
                        drawable.draw(c);
                    } else {
                        if (dividerBgColor == Color.TRANSPARENT) {
                            //纯色绘制
                            if (dividerColor != Color.TRANSPARENT) {
                                realColorDrawable = new ColorDrawable(dividerColor);
                                realColorDrawable.setBounds(right - rightDivider, top + topDivider,
                                        right, bottom - bottomDivider);
                                realColorDrawable.draw(c);
                            }
                        } else {
                            //Layer绘制
                            GradientDrawable bg = new GradientDrawable();
                            bg.setColor(dividerBgColor);
                            GradientDrawable divider = new GradientDrawable();
                            divider.setColor(dividerColor);
                            realLayerDrawable = new LayerDrawable(new Drawable[]{bg, divider});
                            realLayerDrawable.setLayerInset(1, 0, rightDividerPaddingTop, 0, rightDividerPaddingBottom);
                            realLayerDrawable.setBounds(right - rightDivider, top + topDivider,
                                    right, bottom - bottomDivider);
                            realLayerDrawable.draw(c);
                        }
                    }
                }
                if (bottomDivider > 0) {
                    if (drawable != null) {
                        //直接绘制
                        drawable.setBounds(left - leftDivider, bottom - bottomDivider,
                                right - rightDivider, bottom);
                        drawable.draw(c);
                    } else {
                        if (dividerBgColor == Color.TRANSPARENT) {
                            //纯色绘制
                            if (dividerColor != Color.TRANSPARENT) {
                                realColorDrawable = new ColorDrawable(dividerColor);
                                realColorDrawable.setBounds(left - leftDivider, bottom - bottomDivider,
                                        right - rightDivider, bottom);
                                realColorDrawable.draw(c);
                            }
                        } else {
                            //Layer绘制
                            GradientDrawable bg = new GradientDrawable();
                            bg.setColor(dividerBgColor);
                            GradientDrawable divider = new GradientDrawable();
                            divider.setColor(dividerColor);
                            realLayerDrawable = new LayerDrawable(new Drawable[]{bg, divider});
                            realLayerDrawable.setLayerInset(1, bottomDividerPaddingLeft, 0, bottomDividerPaddingRight, 0);
                            realLayerDrawable.setBounds(left - leftDivider, bottom - bottomDivider,
                                    right - rightDivider, bottom);
                            realLayerDrawable.draw(c);
                        }
                    }
                }
            }
        }
    }


    private boolean canDraw(View view, RecyclerView parent) {
        RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
        if (existed(holder, ignoreViewTypes)) {
            //需要忽视
            return false;
        }
        if (ignoreStart && null != holder && holder.getAdapterPosition() == 0) {
            //是第一条
            return false;
        }
        if (ignoreEnd) {
            if (null != holder && holder.getAdapterPosition() == parent.getAdapter().getItemCount() - 1) {
                //是最后一条
                return false;
            }
            RecyclerView.ViewHolder nextHolder = parent.findViewHolderForAdapterPosition(holder.getAdapterPosition() + 1);
            if (null != nextHolder && existed(nextHolder, ignoreViewTypes)) {
                //适配ignoreViewTypes
                return false;
            }
        }
        return true;
    }

    private static boolean existed(@Nullable RecyclerView.ViewHolder holder, @Nullable int[] ignoreViewTypes) {
        if (null == holder || null == ignoreViewTypes) {
            return false;
        }
        for (int type : ignoreViewTypes) {
            if (holder.getItemViewType() == type) {
                return true;
            }
        }
        return false;
    }

    public static final class Vertical {
        private Drawable drawable = null;
        private int dividerBgColor = Color.TRANSPARENT;
        private int dividerColor = Color.TRANSPARENT;
        private boolean ignoreStart = false;
        private boolean ignoreEnd = true;
        //默认过滤FasterAdapter
        private int[] ignoreViewTypes = FasterAdapter.INNER_TYPE;

        private int topDivider = 0;
        private int topDividerPaddingLeft = 0;
        private int topDividerPaddingRight = 0;

        private int bottomDivider = 0;
        private int bottomDividerPaddingLeft = 0;
        private int bottomDividerPaddingRight = 0;


        public Vertical() {
        }

        /**
         * 分隔
         *
         * @param drawable
         * @return
         */
        public Vertical drawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        /**
         * 分隔线有Padding时
         *
         * @param dividerBgColor
         * @return
         */
        public Vertical dividerBgColor(@ColorInt int dividerBgColor) {
            this.dividerBgColor = dividerBgColor;
            return this;
        }

        /**
         * 分隔线色值
         *
         * @param dividerColor
         * @return
         */
        public Vertical dividerColor(@ColorInt int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        /**
         * 是否过滤Adapter第一项
         *
         * @param ignoreStart
         * @return
         */
        public Vertical ignoreStart(boolean ignoreStart) {
            this.ignoreStart = ignoreStart;
            return this;
        }

        /**
         * 是否过滤Adapter最后一项
         *
         * @param ignoreEnd
         * @return
         */
        public Vertical ignoreEnd(boolean ignoreEnd) {
            this.ignoreEnd = ignoreEnd;
            return this;
        }

        /**
         * 过滤指定类型的ItemViewType
         *
         * @param viewTypes {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
         * @return
         */
        public Vertical ignore(int... viewTypes) {
            this.ignoreViewTypes = viewTypes;
            return this;
        }

        /**
         * 上间隔
         *
         * @param topDivider 单位px
         * @return
         */
        public Vertical topDivider(int topDivider) {
            this.topDivider = topDivider;
            return this;
        }

        /**
         * 上间隔左Padding，默认透明，可配合{@link Vertical#dividerBgColor}
         *
         * @param topDividerPaddingLeft 单位px
         * @return
         */
        public Vertical topDividerPaddingLeft(int topDividerPaddingLeft) {
            this.topDividerPaddingLeft = topDividerPaddingLeft;
            return this;
        }

        /**
         * 上间隔右Padding，默认透明，可配合{@link Vertical#dividerBgColor}
         *
         * @param topDividerPaddingRight 单位px
         * @return
         */
        public Vertical topDividerPaddingRight(int topDividerPaddingRight) {
            this.topDividerPaddingRight = topDividerPaddingRight;
            return this;
        }

        /**
         * 下间隔
         *
         * @param bottomDivider 单位px
         * @return
         */
        public Vertical bottomDivider(int bottomDivider) {
            this.bottomDivider = bottomDivider;
            return this;
        }

        /**
         * 下间隔左Padding，默认透明，可配合{@link Vertical#dividerBgColor}
         *
         * @param bottomDividerPaddingLeft 单位px
         * @return
         */
        public Vertical bottomDividerPaddingLeft(int bottomDividerPaddingLeft) {
            this.bottomDividerPaddingLeft = bottomDividerPaddingLeft;
            return this;
        }

        /**
         * 下间隔右Padding，默认透明，可配合{@link Vertical#dividerBgColor}
         *
         * @param bottomDividerPaddingRight 单位px
         * @return
         */
        public Vertical bottomDividerPaddingRight(int bottomDividerPaddingRight) {
            this.bottomDividerPaddingRight = bottomDividerPaddingRight;
            return this;
        }

        public LinearDecoration build() {
            return new LinearDecoration(this);
        }
    }

    public static final class Horizontal {
        private Drawable drawable = null;
        private int dividerBgColor = Color.TRANSPARENT;
        private int dividerColor = Color.TRANSPARENT;
        private boolean ignoreStart = false;
        private boolean ignoreEnd = true;
        private int[] ignoreViewTypes = new int[]{};

        private int leftDivider = 0;
        private int leftDividerPaddingTop = 0;
        private int leftDividerPaddingBottom = 0;

        private int rightDivider = 0;
        private int rightDividerPaddingTop = 0;
        private int rightDividerPaddingBottom = 0;


        public Horizontal() {
        }

        /**
         * 分隔
         *
         * @param drawable
         * @return
         */
        public Horizontal drawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        /**
         * 分隔线有Padding时
         *
         * @param dividerBgColor
         * @return
         */
        public Horizontal dividerBgColor(@ColorInt int dividerBgColor) {
            this.dividerBgColor = dividerBgColor;
            return this;
        }

        /**
         * 分隔线色值
         *
         * @param dividerColor
         * @return
         */
        public Horizontal dividerColor(@ColorInt int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        /**
         * 是否过滤Adapter第一项
         *
         * @param ignoreStart
         * @return
         */
        public Horizontal ignoreStart(boolean ignoreStart) {
            this.ignoreStart = ignoreStart;
            return this;
        }

        /**
         * 是否过滤Adapter最后一项
         *
         * @param ignoreEnd
         * @return
         */
        public Horizontal ignoreEnd(boolean ignoreEnd) {
            this.ignoreEnd = ignoreEnd;
            return this;
        }

        /**
         * 过滤指定类型的ItemViewType
         *
         * @param viewTypes {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
         * @return
         */
        public Horizontal ignore(int... viewTypes) {
            this.ignoreViewTypes = viewTypes;
            return this;
        }

        /**
         * 左间隔
         *
         * @param leftDivider 单位px
         * @return
         */
        public Horizontal leftDivider(int leftDivider) {
            this.leftDivider = leftDivider;
            return this;
        }

        /**
         * 左间隔上Padding，默认透明，可配合{@link Vertical#dividerBgColor}
         *
         * @param leftDividerPaddingTop 单位px
         * @return
         */
        public Horizontal leftDividerPaddingTop(int leftDividerPaddingTop) {
            this.leftDividerPaddingTop = leftDividerPaddingTop;
            return this;
        }

        /**
         * 左间隔下Padding，默认透明，可配合{@link Vertical#dividerBgColor}
         *
         * @param leftDividerPaddingBottom 单位px
         * @return
         */
        public Horizontal leftDividerPaddingBottom(int leftDividerPaddingBottom) {
            this.leftDividerPaddingBottom = leftDividerPaddingBottom;
            return this;
        }

        /**
         * 右间隔
         *
         * @param rightDivider 单位px
         * @return
         */
        public Horizontal rightDivider(int rightDivider) {
            this.rightDivider = rightDivider;
            return this;
        }

        /**
         * 右间隔上Padding，默认透明，可配合{@link Vertical#dividerBgColor}
         *
         * @param rightDividerPaddingTop 单位px
         * @return
         */
        public Horizontal rightDividerPaddingTop(int rightDividerPaddingTop) {
            this.rightDividerPaddingTop = rightDividerPaddingTop;
            return this;
        }

        /**
         * 右间隔下Padding，默认透明，可配合{@link Vertical#dividerBgColor}
         *
         * @param rightDividerPaddingBottom 单位px
         * @return
         */
        public Horizontal rightDividerPaddingBottom(int rightDividerPaddingBottom) {
            this.rightDividerPaddingBottom = rightDividerPaddingBottom;
            return this;
        }

        public LinearDecoration build() {
            return new LinearDecoration(this);
        }
    }
}
