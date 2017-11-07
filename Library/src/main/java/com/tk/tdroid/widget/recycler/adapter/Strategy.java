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

    /**
     * 选中
     *
     * @param holder
     * @param id
     */
    protected void select(FasterHolder holder, long id) {
        holder.getAdapter().getObjectArray().put(id, true);
    }

    /**
     * 反选
     *
     * @param holder
     * @param id
     */
    protected void unSelect(FasterHolder holder, long id) {
        holder.getAdapter().getObjectArray().delete(id);
    }

    /**
     * 是否选中
     *
     * @param holder
     * @param id
     * @return
     */
    protected boolean isSelect(FasterHolder holder, long id) {
        return holder.getAdapter().getObjectArray().get(id, false) == true;
    }

}