package com.tk.tdroid.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_2G;
import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_3G;
import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_4G;
import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_NO;
import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_UNKNOWN;
import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_WIFI;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/25
 *      desc : 网络环境观察者
 * </pre>
 */

public class NetworkObservable {
    private static volatile NetworkObservable mNetworkObservable = null;

    private NetworkBroadcast mNetworkBroadcast = null;
    private ConnectivityManager connectivityManager = null;
    private List<WeakReference<Observer>> observerList = null;

    public class NetworkBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                if (connectivityManager == null) {
                    connectivityManager = (ConnectivityManager) Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
                }
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                int netType = NETWORK_NO;
                if (info != null && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                        netType = NETWORK_WIFI;
                    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        switch (info.getSubtype()) {
                            case TelephonyManager.NETWORK_TYPE_GSM:
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                netType = NETWORK_2G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            case TelephonyManager.NETWORK_TYPE_EHRPD:
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                                netType = NETWORK_3G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_IWLAN:
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                netType = NETWORK_4G;
                                break;
                            default:
                                String subtypeName = info.getSubtypeName();
                                if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                        || subtypeName.equalsIgnoreCase("WCDMA")
                                        || subtypeName.equalsIgnoreCase("CDMA2000")) {
                                    netType = NETWORK_3G;
                                } else {
                                    netType = NETWORK_UNKNOWN;
                                }
                                break;
                        }
                    } else {
                        netType = NETWORK_UNKNOWN;
                    }
                }
                notifyObservers(netType);
            }
        }
    }

    /**
     * 观察者
     */
    public interface Observer {
        void onNetworkChange(boolean networkOn, @NetworkUtils.NetworkType int networkType);
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
        if (mNetworkBroadcast == null) {
            mNetworkBroadcast = new NetworkBroadcast();
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
            observerList = new ArrayList<>(1 << 2);
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
     * @param networkType
     */
    public synchronized void notifyObservers(@NetworkUtils.NetworkType int networkType) {
        if (observerList != null) {
            for (WeakReference<Observer> reference : observerList) {
                if (reference != null && reference.get() != null) {
                    reference.get().onNetworkChange(networkType != NETWORK_NO, networkType);
                }
            }
        }
    }
}
