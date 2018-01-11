package com.tk.tdroid.rx;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/09
 *     desc   : 全局Disposable管理器
 * </pre>
 */
public final class DisposableManager {
    private static volatile DisposableManager mDisposableManager = null;
    private final Map<Object, WeakReference<Disposable>> map = new ConcurrentHashMap<>();

    private DisposableManager() {

    }

    public static DisposableManager getInstance() {
        if (mDisposableManager == null) {
            synchronized (DisposableManager.class) {
                if (mDisposableManager == null) {
                    mDisposableManager = new DisposableManager();
                }
            }
        }
        return mDisposableManager;
    }

    /**
     * 存放
     *
     * @param tag
     * @param disposable
     */
    public void put(Object tag, Disposable disposable) {
        map.put(tag, new WeakReference<>(disposable));
    }

    /**
     * 移除
     *
     * @param tag
     */
    public void remove(Object tag) {
        map.remove(tag);
    }

    /**
     * 移除全部
     */
    public void removeAll() {
        map.clear();
    }

    /**
     * 中断
     *
     * @param tag
     */
    public void dispose(Object tag) {
        WeakReference<Disposable> reference = map.remove(tag);
        if (reference != null && reference.get() != null) {
            reference.get().dispose();
        }
    }

    public Disposable get(Object tag) {
        WeakReference<Disposable> reference = map.get(tag);
        return reference == null ? null : reference.get();
    }
}
