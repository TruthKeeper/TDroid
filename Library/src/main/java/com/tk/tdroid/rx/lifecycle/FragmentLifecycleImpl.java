package com.tk.tdroid.rx.lifecycle;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc : 生命周期的事件_Fragment
 * </pre>
 */

public enum FragmentLifecycleImpl implements IContextLifecycle {
    ON_ATTACH,
    ON_CREATE,
    ON_CREATE_VIEW,
    ON_VIEW_CREATE,
    ON_ACTIVITY_CREATE,
    ON_START,
    ON_RESUME,
    ON_RESTART,
    ON_PAUSE,
    ON_STOP,
    ON_DESTROY_VIEW,
    ON_DESTROY,
    ON_DETACH,
}
