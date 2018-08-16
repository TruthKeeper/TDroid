package com.tk.tdroid.result;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import io.reactivex.Observable;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/18
 *     desc   : 优雅的开启Activity回调 , 不使用{@link android.app.Activity#onActivityResult(int, int, Intent)}
 * </pre>
 */
public final class ActivityResultManager {
    private static final String TAG = "ActivityResultManager";
    private ResultFragment fragment;

    private ActivityResultManager(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        Fragment byTag = manager.findFragmentByTag(TAG);
        if (byTag == null) {
            fragment = new ResultFragment();
            manager.beginTransaction()
                    .add(fragment, TAG)
                    .commitNowAllowingStateLoss();
        } else {
            fragment = (ResultFragment) byTag;
        }
    }

    /**
     * Get
     *
     * @param activity
     * @return
     */
    public static ActivityResultManager with(@NonNull FragmentActivity activity) {
        return new ActivityResultManager(activity);
    }

    /**
     * 开启回调
     *
     * @param intent
     * @param requestCode
     * @param callback
     */
    public void startForResult(@NonNull Intent intent, int requestCode, @NonNull Callback callback) {
        fragment.startForResult(intent, requestCode, callback);
    }

    /**
     * 开启回调
     *
     * @param actCls
     * @param requestCode
     * @param callback
     */
    public void startForResult(@NonNull Class<?> actCls, int requestCode, @NonNull Callback callback) {
        fragment.startForResult(actCls, requestCode, callback);
    }

    /**
     * 开启回调By RxJava
     *
     * @param intent
     * @param requestCode
     * @return
     */
    public Observable<ResultInfo> startForResult(@NonNull Intent intent, int requestCode) {
        return fragment.startForResult(intent, requestCode);
    }

    /**
     * 开启回调By RxJava
     *
     * @param actCls
     * @param requestCode
     * @return
     */
    public Observable<ResultInfo> startForResult(@NonNull Class<?> actCls, int requestCode) {
        return fragment.startForResult(actCls, requestCode);
    }

}