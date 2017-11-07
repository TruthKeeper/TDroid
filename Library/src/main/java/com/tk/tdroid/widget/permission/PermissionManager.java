package com.tk.tdroid.widget.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

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

public class PermissionManager {
    private static final String TAG = "RxPermissionFragment";
    private RxPermissionFragment fragment;

    private PermissionManager(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        Fragment byTag = manager.findFragmentByTag(TAG);
        if (byTag == null) {
            fragment = new RxPermissionFragment();
            manager.beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            //立即执行
            manager.executePendingTransactions();
        } else {
            fragment = (RxPermissionFragment) byTag;
        }
    }

    /**
     * 发起权限请求
     *
     * @param permissions
     */
    @TargetApi(Build.VERSION_CODES.M)
    public Observable<PermissionResult> request(@NonNull final String... permissions) {
        return Observable.just(true)
                .flatMap(new Function<Boolean, ObservableSource<PermissionResult>>() {
                    @Override
                    public ObservableSource<PermissionResult> apply(Boolean aBoolean) throws Exception {
                        fragment.request(permissions);
                        return fragment.getSubject();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Entry
     *
     * @param activity
     * @return
     */
    public static PermissionManager with(@NonNull FragmentActivity activity) {
        return new PermissionManager(activity);
    }

    /**
     * 当用户勾选不再提示且拒绝权限时
     *
     * @param activity
     * @return
     */
    public static void toSetting(@NonNull Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }
}
