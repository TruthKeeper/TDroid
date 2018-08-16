package com.tk.tdroid.collection;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/13
 *      desc :
 * </pre>
 */

public class LimitArrayList<E> extends ArrayList<E> {
    /**
     * 默认限制256大小
     */
    private static final int DEFAULT_LIMIT = 1 << 8;
    private final int limit;

    private OnRecycleListener<E> onRecycleListener;

    public LimitArrayList() {
        limit = DEFAULT_LIMIT;
    }

    public LimitArrayList(int limit) {
        this.limit = limit;
    }

    public LimitArrayList(int initialCapacity, int limit) {
        super(initialCapacity);
        this.limit = limit;
    }

    public LimitArrayList(@NonNull Collection<? extends E> c, int limit) {
        super(c);
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
            final List<E> list = new ArrayList<>(subList(0, count));
            removeAll(list);
            if (onRecycleListener != null) {
                onRecycleListener.onRecycle(list);
            }
        }
    }

    public void setOnRecycleListener(OnRecycleListener<E> onRecycleListener) {
        this.onRecycleListener = onRecycleListener;
    }

    public interface OnRecycleListener<T> {
        void onRecycle(List<T> list);
    }
}
