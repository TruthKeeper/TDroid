package com.tk.tdroid.utils;

import android.content.IntentFilter;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/20
 *      desc : 网络环境观察者 By RxJava
 * </pre>
 */

public final class NetworkObservable {
    private static volatile NetworkObservable mNetworkObservable = null;

    private final Subject<NetworkUtils.NetworkEntry> mNetworkEntrySubject = PublishSubject.create();
    private NetworkUtils.NetworkBroadcast mNetworkBroadcast = new NetworkUtils.NetworkBroadcast() {
        @Override
        public void onChange(NetworkUtils.NetworkEntry entry) {
            mNetworkEntrySubject.onNext(entry);
        }
    };


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
        if (mNetworkBroadcast != null) {
            Utils.getApp().unregisterReceiver(mNetworkBroadcast);
            mNetworkBroadcast = null;
        }
    }

    public Observable<NetworkUtils.NetworkEntry> asObservable() {
        return mNetworkEntrySubject;
    }
}