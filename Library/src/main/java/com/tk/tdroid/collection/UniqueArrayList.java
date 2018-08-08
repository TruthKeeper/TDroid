package com.tk.tdroid.collection;

import android.support.annotation.NonNull;

import com.tk.tdroid.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/7/30
 *     desc   : 去重的ArrayList
 * </pre>
 */
public class UniqueArrayList<E> extends ArrayList<E> {
    private IUnique<E> iUnique;

    public UniqueArrayList(int initialCapacity, IUnique<E> iUnique) {
        super(initialCapacity);
        this.iUnique = iUnique;
    }

    public UniqueArrayList(IUnique<E> iUnique) {
        this.iUnique = iUnique;
    }

    public UniqueArrayList(@NonNull Collection<? extends E> c, IUnique<E> iUnique) {
        super(c);
        this.iUnique = iUnique;
    }

    @Override
    public boolean add(E e) {
        add(size(), e);
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (iUnique == null) {
            super.add(index, element);
            return;
        }
        for (E data : this) {
            if (iUnique.getUnique(data).equals(iUnique.getUnique(element))) {
                return;
            }
        }
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addAll(size(), c);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (EmptyUtils.isEmpty(c)) {
            return false;
        }
        if (iUnique == null) {
            return super.addAll(index, c);
        }
        List<String> uniqueList = new ArrayList<>(size());
        for (E data : this) {
            uniqueList.add(iUnique.getUnique(data));
        }
        for (E e : c) {
            String u = iUnique.getUnique(e);
            if (!uniqueList.contains(u)) {
                uniqueList.add(u);
                add(index++, e);
            }
        }
        return true;
    }
}
