package com.tk.tdroid.widget.recycler.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/6
 *      desc : 视图策略
 * </pre>
 */

public abstract class Strategy<T> {
    protected int getItemViewType() {
        //默认布局Id
        return layoutId();
    }

    public abstract int layoutId();

    protected FasterHolder createHolder(ViewGroup parent) {
        //需要扩展时重写
        return new FasterHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId(), parent, false));
    }

    public void onBindViewHolder(FasterHolder holder, T data, List<Object> payloads) {
        onBindViewHolder(holder, data);
    }

    public abstract void onBindViewHolder(FasterHolder holder, T data);

    public interface OnItemStrategyClickListener<D> {
        void onItemClick(D data);
    }
}