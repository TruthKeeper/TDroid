package com.tk.tdroid.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/12/21
 *     desc   : 网格RecyclerView
 * </pre>
 */
public class GridRecyclerView extends RecyclerView {

    private int divider;
    private int column;

    public GridRecyclerView(Context context) {
        this(context, null);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setHasFixedSize(true);
    }

    /**
     * 设置网格的配置
     *
     * @param divider 间隔
     * @param column  列数
     */
    public void setGrid(int divider, int column) {
        this.divider = divider;
        this.column = column;
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        setLayoutManager(layoutManager);

        FlexboxItemDecoration decoration = new FlexboxItemDecoration(getContext());
        decoration.setOrientation(FlexboxItemDecoration.BOTH);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.TRANSPARENT);
        drawable.setSize(divider, divider);
        decoration.setDrawable(drawable);
        addItemDecoration(decoration);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(new GridAdapterWrapper(adapter, divider, column));
    }

    /**
     * 包装体
     */
    private static class GridAdapterWrapper extends Adapter<ViewHolder> {
        private Adapter<ViewHolder> mAdapter;
        private int divider;
        private int column;

        private GridAdapterWrapper(Adapter<ViewHolder> mAdapter, int divider, int column) {
            this.mAdapter = mAdapter;
            this.divider = divider;
            this.column = column;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewHolder holder = mAdapter.onCreateViewHolder(parent, viewType);
            int availableW = parent.getMeasuredWidth() - parent.getPaddingLeft() - parent.getPaddingRight();
            float p = (availableW - divider * Math.max(column - 1, 0)) / column / 1F / parent.getMeasuredWidth();
            ((FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams()).setFlexBasisPercent(p);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            mAdapter.onBindViewHolder(holder, position);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
            mAdapter.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemCount() {
            return mAdapter.getItemCount();
        }

        @Override
        public int getItemViewType(int position) {
            return mAdapter.getItemViewType(position);
        }

        @Override
        public void setHasStableIds(boolean hasStableIds) {
            mAdapter.setHasStableIds(hasStableIds);
        }

        @Override
        public long getItemId(int position) {
            return mAdapter.getItemId(position);
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            mAdapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(@NonNull ViewHolder holder) {
            return mAdapter.onFailedToRecycleView(holder);
        }

        @Override
        public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
            mAdapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
            mAdapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void registerAdapterDataObserver(@NonNull AdapterDataObserver observer) {
            mAdapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(@NonNull AdapterDataObserver observer) {
            mAdapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            mAdapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
            mAdapter.onDetachedFromRecyclerView(recyclerView);
        }
    }
}
