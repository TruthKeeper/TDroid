package com.tk.tdroid.utils;

import android.content.IntentFilter;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/25
 *      desc : 网络环境观察者
 * </pre>
 */

public class NetworkObservable {
    private static volatile NetworkObservable mNetworkObservable = null;
    private List<WeakReference<Observer>> observerList = null;
    private NetworkUtils.NetworkBroadcast mNetworkBroadcast = new NetworkUtils.NetworkBroadcast() {
        @Override
        public void onChange(NetworkUtils.NetworkEntry entry) {
            notifyObservers(entry);
        }
    };

    /**
     * 观察者
     */
    public interface Observer {
        void onNetworkChange(NetworkUtils.NetworkEntry entry);
    }

    private NetworkObservable() {
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static NetworkObservable getInstance() {
        if (mNetworkObservable == null) {
            synchronized (NetworkObservable.class) {
                if (mNetworkObservable == null) {
                    mNetworkObservable = new NetworkObservable();
                }
            }
        }
        return mNetworkObservable;
    }

    /**
     * 初始化
     */
    public void init() {
        if (mNetworkBroadcast != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            filter.addAction("android.net.wifi.STATE_CHANGE");
            Utils.getApp().registerReceiver(mNetworkBroadcast, filter);
        }
    }

    /**
     * 回收资源
     */
    public void recycle() {
        removeAllObservers();
        if (mNetworkBroadcast != null) {
            Utils.getApp().unregisterReceiver(mNetworkBroadcast);
            mNetworkBroadcast = null;
        }
    }

    /**
     * 添加观察者
     *
     * @param observer
     */
    public void addObserver(@Nullable Observer observer) {
        if (observer == null) {
            return;
        }
        if (observerList == null) {
            observerList = new ArrayList<>(4);
        }
        observerList.add(new WeakReference<Observer>(observer));
    }

    /**
     * 移除观察者
     *
     * @param observer
     */
    public void removeObserver(@Nullable final Observer observer) {
        if (observerList != null && observer != null) {
            CollectionUtils.removeIf(observerList, new CollectionUtils.Predicate<WeakReference<Observer>>() {
                @Override
                public boolean process(WeakReference<Observer> reference) {
                    return reference != null && reference.get() == observer;
                }
            });
        }
    }

    /**
     * 移除所有观察者
     */
    public void removeAllObservers() {
        if (observerList != null) {
            observerList.clear();
        }
    }

    /**
     * 通知所有观察者
     *
     * @param entry
     */
    private synchronized void notifyObservers(final NetworkUtils.NetworkEntry entry) {
        if (observerList != null) {
            for (WeakReference<Observer> reference : observerList) {
                if (reference != null && reference.get() != null) {
                    reference.get().onNetworkChange(entry);
                }
            }
        }
    }
}
