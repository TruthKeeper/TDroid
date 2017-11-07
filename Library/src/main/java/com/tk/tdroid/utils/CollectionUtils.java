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
 *     desc   : 集合处理工具
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

    public interface Search<T, D> {
        D apply(T t);
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
    public static <T> void removeRepeat(@NonNull Collection<T> collection) {
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
    public static <T> int searchFirst(@NonNull T[] objects, @NonNull T element) {
        int index = -1;
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
     * @param condition
     * @param search
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T, D> int searchFirst(@NonNull T[] objects, @NonNull D condition, @NonNull Search<T, D> search) {
        int index = -1;
        for (int i = 0; i < objects.length; i++) {
            if (search.apply(objects[i]).equals(condition)) {
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
    public static <T> int searchFirst(@NonNull List<T> list, @NonNull T element) {
        int index = -1;
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
     * @param condition
     * @param search
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T, D> int searchFirst(@NonNull List<T> list, @NonNull D condition, @NonNull Search<T, D> search) {
        int result = -1;
        for (int i = 0; i < list.size(); i++) {
            if (search.apply(list.get(i)).equals(condition)) {
                return i;
            }
        }
        return result;
    }

    /**
     * 获取集合内容
     *
     * @param iterator
     * @param divide
     * @return
     */
    public static String getContent(@Nullable Iterator iterator, String divide) {
        if (iterator == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (iterator.hasNext()) {
            sb.append(iterator.next());
            while (iterator.hasNext()) {
                sb.append(divide);
                sb.append(iterator.next());
            }
        }
        return sb.toString();
    }

    /**
     * 获取数组内容
     *
     * @param ts
     * @param divide
     * @param <T>
     * @return
     */
    public static <T> String getContent(@Nullable T[] ts, String divide) {
        if (ts == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (T t : ts) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(divide);
            }
            sb.append(t);
        }
        return sb.toString();
    }
}
