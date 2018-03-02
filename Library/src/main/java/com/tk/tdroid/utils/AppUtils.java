package com.tk.tdroid.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.tk.tdroid.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/09/09
 *     desc   : App工具类
 * </pre>
 */
public final class AppUtils {
    private static boolean isDebug = BuildConfig.DEBUG;
    private static final String DEVICE_SP = "DEVICE_SP";
    private static final String DEVICE_ID = "device_id";

    private AppUtils() {
        throw new IllegalStateException();
    }

    /**
     * 得到软件版本号
     *
     * @return
     */
    public static int getVerCode() {
        int verCode = -1;
        try {
            String packageName = Utils.getApp().getPackageName();
            verCode = Utils.getApp().getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 得到软件版本名称
     *
     * @return
     */
    public static String getVerName() {
        String verName = "";
        try {
            String packageName = Utils.getApp().getPackageName();
            verName = Utils.getApp().getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 当前是否是主进程
     *
     * @return
     */
    public static boolean isMainProcess() {
        ActivityManager activityManager = ((ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> infoList = activityManager.getRunningAppProcesses();
        String processName = Utils.getApp().getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : infoList) {
            if (myPid == info.pid && processName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 主进程是否存活
     *
     * @return
     */
    public static boolean isMainProcessLive() {
        return isProcessRunning(Utils.getApp().getPackageName());
    }

    /**
     * 进程是否存活
     *
     * @param processName
     * @return
     */
    public static boolean isProcessRunning(String processName) {
        ActivityManager activityManager = ((ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> infoList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infoList) {
            if (processName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测服务是否运行
     *
     * @param className 类名
     * @return 是否运行的状态
     */
    public static boolean isServiceRunning(@NonNull String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : servicesList) {
            if (className.equals(info.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 是否安装了某应用
     *
     * @param packageName
     * @return
     */
    public static boolean isApplicationAvailable(@NonNull String packageName) {
        final PackageManager packageManager = Utils.getApp().getPackageManager();
        List<PackageInfo> infoList = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : infoList) {
            if (packageInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取应用运行的最大内存
     *
     * @return 最大内存，单位 B
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * 获取手机系统SDK版本号
     *
     * @return 例如21
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 开发是否是debug模式
     *
     * @return
     */
    public static boolean isDebug() {
        return isDebug;
    }

    /**
     * 应用是否是debug版本
     *
     * @return
     */
    public static boolean isApplicationDebug() {
        if (Utils.getApp().getApplicationInfo() != null) {
            return (Utils.getApp().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
        throw new RuntimeException("Application is null");
    }

    /**
     * 获取meta-data数据
     *
     * @param metaDataName
     * @return
     */
    public static String getAppMetaData(@NonNull String metaDataName) {
        try {
            PackageManager packageManager = Utils.getApp().getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(Utils.getApp().getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(metaDataName, null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取meta-data数据
     *
     * @param activityFullName Activity全类名
     * @param metaDataName
     * @return
     */
    public static String getActivityMetaData(@NonNull String activityFullName, @NonNull String metaDataName) {
        try {
            ComponentName componentName = new ComponentName(Utils.getApp().getPackageName(), activityFullName);
            PackageManager packageManager = Utils.getApp().getPackageManager();
            ActivityInfo info = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA);
            return info.metaData.getString(metaDataName, null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取meta-data数据
     *
     * @param serviceFullName Service全类名
     * @param metaDataName
     * @return
     */
    public static String getServiceMetaData(@NonNull String serviceFullName, @NonNull String metaDataName) {
        try {
            ComponentName componentName = new ComponentName(Utils.getApp().getPackageName(), serviceFullName);
            PackageManager packageManager = Utils.getApp().getPackageManager();
            ServiceInfo info = packageManager.getServiceInfo(componentName, PackageManager.GET_META_DATA);
            return info.metaData.getString(metaDataName, null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取meta-data数据
     *
     * @param broadcastReceiverFullName 广播接收者全类名
     * @param metaDataName
     * @return
     */
    public static String getBroadcastReceiverMetaData(@NonNull String broadcastReceiverFullName, @NonNull String metaDataName) {
        try {
            ComponentName componentName = new ComponentName(Utils.getApp().getPackageName(), broadcastReceiverFullName);
            PackageManager packageManager = Utils.getApp().getPackageManager();
            ActivityInfo info = packageManager.getReceiverInfo(componentName, PackageManager.GET_META_DATA);
            return info.metaData.getString(metaDataName, null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备Id，需要权限android.permission.READ_PHONE_STATE
     *
     * @return
     */
    public static String getDeviceId() {
        final SharedPreferences preferences = Utils.getApp().getSharedPreferences(DEVICE_SP, Context.MODE_PRIVATE);
        final String id = preferences.getString(DEVICE_ID, null);
        if (id != null) {
            return id;
        } else {
            final String androidId = Settings.Secure.getString(Utils.getApp().getContentResolver(), Settings.Secure.ANDROID_ID);
            UUID uuid;
            try {
                if (EmptyUtils.isEmpty(androidId) || androidId.equals("9774d56d682e549c")) {
                    String deviceId = null;
                    if (ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        deviceId = ((TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    }
                    uuid = EmptyUtils.isEmpty(deviceId) ? UUID.randomUUID()
                            : UUID.nameUUIDFromBytes(deviceId.getBytes("utf8"));
                } else {
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            String uuidStr = uuid.toString();
            preferences.edit().putString(DEVICE_ID, uuidStr).apply();
            return uuidStr;
        }
    }
}
