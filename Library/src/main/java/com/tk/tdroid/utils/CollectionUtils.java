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
 *     time   : 2017/9/13
 *     desc   : 集合处理工具类
 * </pre>
 */
public final class CollectionUtils {
    public interface Predicate<D> {
        /**
         * @param d
         * @return 是否执行
         */
        boolean process(D d);
    }

    private CollectionUtils() {
        throw new IllegalStateException();
    }

    /**
     * 集合条件移除
     *
     * @param collection
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> boolean removeIf(@Nullable Collection<T> collection, @Nullable Predicate<T> predicate) {
        boolean removed = false;
        if (EmptyUtils.isEmpty(collection) || predicate == null) {
            return removed;
        }
        final Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (predicate.process(iterator.next())) {
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
    public static <T> void removeRepeat(@Nullable Collection<T> collection) {
        if (EmptyUtils.isEmpty(collection)) {
            return;
        }
        collection.clear();
        collection.addAll(new HashSet<T>(collection));
    }

    /**
     * 数组中搜索元素第一次出现位置，equals
     *
     * @param objects
     * @param element
     * @return
     */
    public static <T> int searchFirstIndex(@Nullable T[] objects, @NonNull T element) {
        int index = -1;
        if (EmptyUtils.isEmpty(objects)) {
            return index;
        }
        for (int i = 0; i < objects.length; i++) {
            if (element.equals(objects[i])) {
                return i;
            }
        }
        return index;
    }

    /**
     * 数组中搜索元素第一次出现位置
     *
     * @param objects
     * @param predicate
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T, D> int searchFirstIndex(@Nullable T[] objects, @NonNull Predicate<T> predicate) {
        int index = -1;
        if (EmptyUtils.isEmpty(objects)) {
            return index;
        }
        for (int i = 0; i < objects.length; i++) {
            if (predicate.process(objects[i])) {
                return i;
            }
        }
        return index;
    }

    /**
     * 集合中搜索元素第一次出现位置，equals
     *
     * @param list
     * @param element
     * @return
     */
    public static <T> int searchFirstIndex(@Nullable List<T> list, @NonNull T element) {
        int index = -1;
        if (EmptyUtils.isEmpty(list)) {
            return index;
        }
        for (int i = 0; i < list.size(); i++) {
            if (element.equals(list.get(i))) {
                return i;
            }
        }
        return index;
    }

    /**
     * 集合中搜索元素第一次出现位置
     *
     * @param list
     * @param predicate
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T, D> int searchFirstIndex(@Nullable List<T> list, @NonNull Predicate<T> predicate) {
        int index = -1;
        if (EmptyUtils.isEmpty(list)) {
            return index;
        }
        for (int i = 0; i < list.size(); i++) {
            if (predicate.process(list.get(i))) {
                return i;
            }
        }
        return index;
    }

    /**
     * 获取集合内容
     *
     * @param iterable
     * @param divider
     * @return
     */
    public static String getContent(@Nullable Iterable iterable, String divider) {
        if (iterable == null) {
            return "";
        }
        Iterator iterator = iterable.iterator();
        StringBuilder sb = new StringBuilder();
        if (iterator.hasNext()) {
            sb.append(iterator.next());
            while (iterator.hasNext()) {
                sb.append(divider);
                sb.append(iterator.next());
            }
        }
        return sb.toString();
    }

    /**
     * 获取数组内容
     *
     * @param ts
     * @param divider
     * @param <T>
     * @return
     */
    public static <T> String getContent(@Nullable T[] ts, String divider) {
        if (EmptyUtils.isEmpty(ts)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (T t : ts) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(divider);
            }
            sb.append(t);
        }
        return sb.toString();
    }
}
