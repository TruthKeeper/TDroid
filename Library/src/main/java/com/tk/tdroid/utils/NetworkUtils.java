package com.tk.tdroid.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_2G;
import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_3G;
import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_4G;
import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_NO;
import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_UNKNOWN;
import static com.tk.tdroid.utils.NetworkUtils.NetworkType.NETWORK_WIFI;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/25
 *     desc   : 网络工具
 * </pre>
 */
public final class NetworkUtils {
    private NetworkUtils() {
        throw new IllegalStateException();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NETWORK_WIFI, NETWORK_4G, NETWORK_3G, NETWORK_2G, NETWORK_UNKNOWN, NETWORK_NO})
    public @interface NetworkType {
        int NETWORK_WIFI = 0x01;
        int NETWORK_4G = 0x02;
        int NETWORK_3G = 0x03;
        int NETWORK_2G = 0x04;
        int NETWORK_UNKNOWN = 0x05;
        int NETWORK_NO = 0x06;
    }

    /**
     * 网络状况
     */
    public static class NetworkEntry {
        final boolean networkOn;
        @NetworkUtils.NetworkType
        final int networkType;

        public NetworkEntry(boolean networkOn, int networkType) {
            this.networkOn = networkOn;
            this.networkType = networkType;
        }

        public boolean isNetworkOn() {
            return networkOn;
        }

        @NetworkUtils.NetworkType
        public int getNetworkType() {
            return networkType;
        }

        @Override
        public String toString() {
            return "NetworkEntry{" +
                    "networkOn=" + networkOn +
                    ", networkType=" + networkType +
                    '}';
        }
    }

    /**
     * 网络广播接收者
     */
    public static abstract class NetworkBroadcast extends BroadcastReceiver {
        private ConnectivityManager manager;
        private int lastNetworkType = -1;

        {
            manager = (ConnectivityManager) Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                int networkType = NETWORK_NO;
                if (info != null && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                        networkType = NETWORK_WIFI;
                    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        switch (info.getSubtype()) {
                            case TelephonyManager.NETWORK_TYPE_GSM:
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                networkType = NETWORK_2G;
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
                                networkType = NETWORK_3G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_IWLAN:
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                networkType = NETWORK_4G;
                                break;
                            default:
                                String subtypeName = info.getSubtypeName();
                                if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                        || subtypeName.equalsIgnoreCase("WCDMA")
                                        || subtypeName.equalsIgnoreCase("CDMA2000")) {
                                    networkType = NETWORK_3G;
                                } else {
                                    networkType = NETWORK_UNKNOWN;
                                }
                                break;
                        }
                    } else {
                        networkType = NETWORK_UNKNOWN;
                    }
                }
                if (networkType != lastNetworkType) {
                    onChange(new NetworkEntry(networkType != NETWORK_NO, networkType));
                }
                lastNetworkType = networkType;
            }
        }

        public abstract void onChange(final NetworkEntry entry);
    }

    /**
     * 网络是否连接畅通
     *
     * @return
     */
    public static boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 网络是否能连接上，异步ping
     *
     * @return
     */
    public static boolean isAvailableByPing() {
        return isAvailableByPing(null);
    }

    /**
     * 网络是否能连接上，异步ping
     *
     * @param ip
     * @return
     */
    public static boolean isAvailableByPing(@Nullable String ip) {
        if (EmptyUtils.isEmpty(ip)) {
            // 中国互联网公共解析
            ip = "1.2.4.8";
        }
        ShellUtils.CmdResult result = ShellUtils.executeCmd(String.format("ping -c 1 %s", ip), false);
        return result.getResult() == 0;
    }

    /**
     * 移动数据是否打开
     *
     * @return
     */
    public static boolean isMobileDataEnabled() {
        TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        Method method = null;
        try {
            method = tm.getClass().getDeclaredMethod("getDataEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(tm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 当前网络是否是4G，如果wifi开启了则为false
     *
     * @return
     */
    public static boolean is4G() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * wifi开关是否打开
     *
     * @return
     */
    @SuppressLint("WifiManagerLeak")
    public static boolean isWifiEnabled() {
        WifiManager wifiManager = (WifiManager) Utils.getApp().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 打开或关闭wifi开关
     *
     * @param
     */
    @SuppressLint("WifiManagerLeak")
    public static void setWifiEnabled(boolean enabled) {
        WifiManager wifiManager = (WifiManager) Utils.getApp().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            if (!enabled) {
                wifiManager.setWifiEnabled(false);
            }
        } else {
            if (enabled) {
                wifiManager.setWifiEnabled(true);
            }
        }
    }

    /**
     * wifi是否连接畅通
     *
     * @return
     */
    public static boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取当前网络类型
     *
     * @return 网络类型
     * <ul>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_WIFI   } </li>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_4G     } </li>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_3G     } </li>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_2G     } </li>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_UNKNOWN} </li>
     * <li>{@link NetworkUtils.NetworkType#NETWORK_NO     } </li>
     * </ul>
     */
    @NetworkType
    public static int getNetworkType() {
        int netType = NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo();
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
        return netType;
    }

    /**
     * 获取IP地址
     *
     * @param useIPv4 是否用IPv4
     * @return
     */
    public static String getIPAddress(final boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) {
                    continue;
                }
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取域名ip地址
     *
     * @param domain 域名 like www.baidu.com
     * @return ip地址
     */
    public static String getDomainAddress(final String domain) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取活动网络信息
     *
     * @return
     */
    private static NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }
}