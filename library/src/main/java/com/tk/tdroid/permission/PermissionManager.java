package com.tk.tdroid.permission;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/7
 *      desc : 权限获取工具By RxJava
 *      危险权限:
 * <ul> 通讯录 android.permission-group.CONTACTS
 * <li>android.permission.WRITE_CONTACTS</li>
 * <li>android.permission.GET_ACCOUNTS</li>
 * <li>android.permission.READ_CONTACTS</li>
 * </ul>
 * <ul> 通话 android.permission-group.PHONE
 * <li>android.permission.READ_CALL_LOG</li>
 * <li>android.permission.READ_PHONE_STATE</li>
 * <li>android.permission.CALL_PHONE</li>
 * <li>android.permission.WRITE_CALL_LOG</li>
 * <li>android.permission.USE_SIP</li>
 * <li>android.permission.PROCESS_OUTGOING_CALLS</li>
 * </ul>
 * <ul> 日历 android.permission-group.CALENDAR
 * <li>android.permission.READ_CALENDAR</li>
 * <li>android.permission.WRITE_CALENDAR</li>
 * </ul>
 * <ul> 相机 android.permission-group.CAMERA
 * <li>android.permission.CAMERA</li>
 * </ul>
 * <ul> 传感器 android.permission-group.SENSORS
 * <li>android.permission.BODY_SENSORS</li>
 * </ul>
 * <ul> 定位 android.permission-group.LOCATION
 * <li>android.permission.ACCESS_FINE_LOCATION</li>
 * <li>android.permission.ACCESS_COARSE_LOCATION</li>
 * </ul>
 * <ul> 存储 android.permission-group.STORAGE
 * <li>android.permission.READ_EXTERNAL_STORAGE</li>
 * <li>android.permission.WRITE_EXTERNAL_STORAGE</li>
 * </ul>
 * <ul> 麦克风 android.permission-group.MICROPHONE
 * <li>android.permission.RECORD_AUDIO</li>
 * </ul>
 * <ul> 短信 android.permission-group.SMS
 * <li>android.permission.READ_SMS</li>
 * <li>android.permission.RECEIVE_WAP_PUSH</li>
 * <li>android.permission.RECEIVE_MMS</li>
 * <li>android.permission.RECEIVE_SMS</li>
 * <li>android.permission.SEND_SMS</li>
 * <li>android.permission.READ_CELL_BROADCASTS</li>
 * </ul>
 * </pre>
 */

public final class PermissionManager {
    private static final String TAG = "PermissionManager";
    private RxPermissionFragment fragment;

    private PermissionManager(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        Fragment byTag = manager.findFragmentByTag(TAG);
        if (byTag == null) {
            fragment = new RxPermissionFragment();
            manager.beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            manager.executePendingTransactions();
        } else {
            fragment = (RxPermissionFragment) byTag;
        }
    }

    /**
     * 发起权限请求，对每个权限获取情况都关心时
     *
     * @param permissions
     * @return
     */
    public Observable<Permission> requestEach(@NonNull final String... permissions) {
        return fragment.requestEach(permissions);
    }

    /**
     * 发起权限请求，只对最终结果感兴趣时
     *
     * @param permissions
     * @return
     */
    public Single<Boolean> request(@NonNull final String... permissions) {
        return fragment.request(permissions);
    }

    /**
     * Get
     *
     * @param activity
     * @return
     */
    public static PermissionManager with(@NonNull Activity activity) {
        return new PermissionManager(activity);
    }
}