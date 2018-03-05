package com.tk.tdroid.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
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

public final class ForegroundObservable implements ComponentCallbacks2, Application.ActivityLifecycleCallbacks {
    private static volatile ForegroundObservable mForegroundObservable = null;

    private final Subject<Boolean> mForegroundObservableSubject = PublishSubject.create();
    private boolean isForeground = true;

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
        application.registerComponentCallbacks(this);
        application.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 回收
     *
     * @param application
     */
    public void recycle(@NonNull Application application) {
        application.unregisterComponentCallbacks(this);
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
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            isForeground = false;
            mForegroundObservableSubject.onNext(false);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!isForeground) {
            isForeground = true;
            mForegroundObservableSubject.onNext(true);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}