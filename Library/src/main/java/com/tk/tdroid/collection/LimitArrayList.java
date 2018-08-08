package com.tk.tdroid.collection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/13
 *      desc :
 * </pre>
 */

public class LimitArrayList<E> extends ArrayList<E> {
    //限制256大小
    private int limit = 1 << 8;

    private OnRecycleListener onRecycleListener;

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E e) {
        boolean result = super.add(e);
        recycle();
        return result;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        recycle();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = super.addAll(c);
        recycle();
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean result = super.addAll(index, c);
        recycle();
        return result;
    }

    private synchronized void recycle() {
        int count = size() - limit;
        if (count > 0) {
            removeAll(new ArrayList<>(subList(0, count)));
            if (onRecycleListener != null) {
                onRecycleListener.onChange(count);
            }
        }
    }

    public void setOnRecycleListener(OnRecycleListener onRecycleListener) {
        this.onRecycleListener = onRecycleListener;
    }

    public interface OnRecycleListener {
        void onChange(int removeCount);
    }
}
