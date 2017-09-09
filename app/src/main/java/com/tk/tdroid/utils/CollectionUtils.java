package com.tk.tdroid.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/03/16
 *     desc   : 集合处理工具
 * </pre>
 */
public final class CollectionUtils {
    public interface Predicate<T> {
        boolean removeConfirm(T t);
    }

    private CollectionUtils() {
        throw new IllegalStateException();
    }

    /**
     * 判断是否为空集合
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(@Nullable List<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * 判断是否为空数组
     *
     * @param ts
     * @return
     */
    public static <T> boolean isEmpty(@Nullable T[] ts) {
        return ts == null || ts.length == 0;
    }

    /**
     * 集合条件移除
     *
     * @param collection
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> boolean removeIf(@NonNull Collection<T> collection, @NonNull Predicate<T> predicate) {
        boolean removed = false;
        final Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (predicate.removeConfirm(iterator.next())) {
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * 去重，需要重写hashcode&&equals保证准确性
     *
     * @param collection
     * @param <T>
     */
    public static <T> void removeRepeat(@NonNull Collection<T> collection) {
        collection.clear();
        collection.addAll(new HashSet<T>(collection));
    }

    /**
     * 数组中搜索元素
     *
     * @param objects
     * @param element
     * @return
     */
    public static <T> int search(@NonNull T[] objects, @NonNull T element) {
        int e = -1;
        for (int w = 0; w < objects.length; w++) {
            if (element.equals(objects[w])) {
                return w;
            }
        }
        return e;
    }
}
