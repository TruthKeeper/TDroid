package com.tk.tdroid.widget.recycler;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/28
 *     desc   : 网格内间隔线
 * </pre>
 */
public class GridDecoration extends RecyclerView.ItemDecoration {
    /**
     * 实际运算要>>1
     */
    private int divider;
    private ColorDrawable dividerDrawable;

    public GridDecoration(@ColorInt int dividerColor, @IntRange(from = 0) int divider) {
        this.divider = divider;
        this.dividerDrawable = new ColorDrawable(dividerColor);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
            final int position = parent.getChildAdapterPosition(view);
            final int spanCount = manager.getSpanCount();
            final int size = parent.getAdapter().getItemCount();

            final boolean borderLeft = isBorderLeft(position, spanCount, size);
            final boolean borderTop = isBorderTop(position, spanCount, size);
            final boolean borderRight = isBorderRight(position, spanCount, size);
            final boolean borderBottom = isBorderBottom(position, spanCount, size);

            final int left = borderLeft ? 0 : divider >> 1;
            final int top = borderTop ? 0 : divider >> 1;
            final int right = borderRight ? 0 : divider >> 1;
            final int bottom = borderBottom ? 0 : divider >> 1;
            outRect.set(left, top, right, bottom);
        }
    }

    private boolean isBorderLeft(int position, int spanCount, int size) {
        if (size == 0) {
            return false;
        }
        if (position % spanCount == 0) {
            return true;
        }
        return false;
    }

    private boolean isBorderTop(int position, int spanCount, int size) {
        if (size == 0) {
            return false;
        }
        if (position < spanCount) {
            return true;
        }
        return false;
    }

    private boolean isBorderRight(int position, int spanCount, int size) {
        if (size == 0) {
            return false;
        }
        if (position % spanCount == spanCount - 1 || position == size - 1) {
            return true;
        }
        return false;
    }

    private boolean isBorderBottom(int position, int spanCount, int size) {
        if (size == 0) {
            return false;
        }
        int last = (size / spanCount) * spanCount - 1;
        if (last == -1) {
            return true;
        }
        if (position > last || position >= size - spanCount) {
            return true;
        }
        return false;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
            final int spanCount = manager.getSpanCount();
            final int size = parent.getAdapter().getItemCount();
            int left;
            int top;
            int right;
            int bottom;

            final int childCount = manager.getChildCount();
            View child;
            final Rect decorationRect = new Rect();
            for (int i = 0; i < childCount; i++) {
                child = manager.getChildAt(i);
                manager.calculateItemDecorationsForChild(child, decorationRect);

                final int translationX = Math.round(ViewCompat.getTranslationX(child));
                final int translationY = Math.round(ViewCompat.getTranslationY(child));
                boolean borderLeft = isBorderLeft(i, spanCount, size);
                boolean borderTop = isBorderTop(i, spanCount, size);
                boolean borderRight = isBorderRight(i, spanCount, size);
                boolean borderBottom = isBorderBottom(i, spanCount, size);

                left = child.getLeft() - decorationRect.left + translationX;
                top = child.getTop() - decorationRect.top + translationY;
                right = child.getRight() + decorationRect.right + translationX;
                bottom = child.getBottom() + decorationRect.bottom + translationY;
                if (!borderLeft) {
                    dividerDrawable.setBounds(left,
                            borderTop ? child.getTop() : top,
                            child.getLeft(),
                            borderBottom ? child.getBottom() : bottom);
                    dividerDrawable.draw(c);
                }
                if (!borderTop) {
                    dividerDrawable.setBounds(borderLeft ? child.getLeft() : left,
                            top,
                            borderRight ? child.getRight() : right,
                            child.getTop());
                    dividerDrawable.draw(c);
                }
                if (!borderRight) {
                    dividerDrawable.setBounds(child.getRight(),
                            borderTop ? child.getTop() : top,
                            right,
                            borderBottom ? child.getBottom() : bottom);
                    dividerDrawable.draw(c);
                }
                if (!borderBottom) {
                    dividerDrawable.setBounds(borderLeft ? child.getLeft() : left,
                            child.getBottom(),
                            borderRight ? child.getRight() : right,
                            bottom);
                    dividerDrawable.draw(c);
                }
            }
        }
    }
}
