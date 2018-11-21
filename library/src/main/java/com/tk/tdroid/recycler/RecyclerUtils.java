package com.tk.tdroid.recycler;

import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/15
 *      desc :
 * </pre>
 */

public final class RecyclerUtils {
    private RecyclerUtils() {
        throw new IllegalStateException();
    }

    /**
     * 为RecyclerView设置最大高度
     *
     * @param recyclerView
     * @param maxItem
     */
    public static void setMaxHeight(@NonNull RecyclerView recyclerView, @IntRange(from = 0) int maxItem) {
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(0);
                if (null == holder) {
                    return;
                }
                int itemCount = Math.min(maxItem, recyclerView.getAdapter().getItemCount());
                ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                Rect decoration = new Rect();
                recyclerView.getDecoratedBoundsWithMargins(holder.itemView, decoration);
                int height = decoration.bottom;
                params.height = height * itemCount
                        + recyclerView.getPaddingTop() + recyclerView.getPaddingBottom();
                recyclerView.setLayoutParams(params);
            }
        });
    }

}
