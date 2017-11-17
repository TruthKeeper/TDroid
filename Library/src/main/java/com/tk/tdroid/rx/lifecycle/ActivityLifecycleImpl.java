package com.tk.tdroid.rx.lifecycle;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc : 生命周期的事件_Activity
 * </pre>
 */

public enum ActivityLifecycleImpl implements IContextLifecycle {
    ON_CREATE,
    PRE_INFLATE,
    ON_START,
    ON_RESUME,
    ON_RESTART,
    ON_PAUSE,
    ON_STOP,
    ON_DESTROY,
}
