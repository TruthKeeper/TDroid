package com.tk.widget.recycler;

import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

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
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (null == adapter) {
            return;
        }
        int count = adapter.getItemCount();
        if (count <= maxItem) {
            return;
        }
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(0);
                if (null == holder) {
                    return;
                }
                Rect decoration = new Rect();
                recyclerView.getDecoratedBoundsWithMargins(holder.itemView, decoration);
                int height = decoration.bottom;
                recyclerView.getLayoutParams().height = height * maxItem;
            }
        });
    }

}
