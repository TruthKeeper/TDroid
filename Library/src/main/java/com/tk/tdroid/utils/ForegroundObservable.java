package com.tk.tdroid.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *      author : TK
 *      time : 2018/3/5
 *      desc : App前后台切换观察者 By RxJava
 * </pre>
 */

public final class ForegroundObservable implements Application.ActivityLifecycleCallbacks {
    private static volatile ForegroundObservable mForegroundObservable = null;

    private final Subject<Boolean> mForegroundObservableSubject = PublishSubject.create();
    private boolean isForeground = true;
    /**
     * 位于前台的 Activity 的数目
     */
    private int foregroundCount = 0;
    /**
     * 缓冲计数器，记录 configChanges 的状态
     */
    private int bufferCount = 0;

    private ForegroundObservable() {
    }

    public static ForegroundObservable getInstance() {
        if (mForegroundObservable == null) {
            synchronized (ForegroundObservable.class) {
                if (mForegroundObservable == null) {
                    mForegroundObservable = new ForegroundObservable();
                }
            }
        }
        return mForegroundObservable;
    }

    /**
     * 初始化
     *
     * @param application
     */
    public void init(@NonNull Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 回收
     *
     * @param application
     */
    public void recycle(@NonNull Application application) {
        application.unregisterActivityLifecycleCallbacks(this);
    }

    /**
     * 获取可观察者
     *
     * @return
     */
    public Observable<Boolean> asObservable() {
        return mForegroundObservableSubject;
    }

    /**
     * 当前是否处于前台
     *
     * @return
     */
    public boolean isForeground() {
        return isForeground;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (foregroundCount <= 0) {
            isForeground = true;
            mForegroundObservableSubject.onNext(true);
        }
        if (bufferCount < 0) {
            bufferCount++;
        } else {
            foregroundCount++;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity.isChangingConfigurations()) {
            // 是 configChanges 的情况，操作缓冲计数器
            bufferCount--;
        } else {
            foregroundCount--;
            if (foregroundCount <= 0) {
                isForeground = false;
                mForegroundObservableSubject.onNext(false);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}