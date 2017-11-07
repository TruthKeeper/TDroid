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
