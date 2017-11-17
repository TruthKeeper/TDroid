package com.tk.tdroid.rx.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc : 生命周期的事件_Activity
 * </pre>
 */

public enum ActivityLifecycleImpl implements IContextLifecycle {
    /**
     * 开始回调{@link Activity#onCreate(Bundle)}
     */
    ON_CREATE,
    /**
     * 开始执行{@link AppCompatActivity#setContentView(int)}
     */
    PRE_INFLATE,
    /**
     * 开始回调{@link Activity#onStart()}
     */
    ON_START,
    /**
     * 开始回调{@link Activity#onResume()}
     */
    ON_RESUME,
    /**
     * 开始回调{@link Activity#onRestart()}
     */
    ON_RESTART,
    /**
     * 开始回调{@link Activity#onPause()}
     */
    ON_PAUSE,
    /**
     * 开始回调{@link Activity#onPause()}
     */
    ON_STOP,
    /**
     * 开始回调{@link Activity#onDestroy()}
     */
    ON_DESTROY,
}
