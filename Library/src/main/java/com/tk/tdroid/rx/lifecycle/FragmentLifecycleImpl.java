package com.tk.tdroid.rx.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc : 生命周期的事件_Fragment
 * </pre>
 */

public enum FragmentLifecycleImpl implements IContextLifecycle {
    /**
     * 开始回调{@link Fragment#onAttach(Context)}
     */
    ON_ATTACH,
    /**
     * 开始回调{@link Fragment#onCreate(Bundle)}
     */
    ON_CREATE,
    /**
     * 开始执行{@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    PRE_INFLATE,
    /**
     * 开始回调{@link Fragment#onViewCreated(View, Bundle)}
     */
    ON_VIEW_CREATED,
    /**
     * 开始回调{@link Fragment#onActivityCreated(Bundle)}
     */
    ON_ACTIVITY_CREATED,
    /**
     * 开始回调{@link Fragment#onStart()}
     */
    ON_START,
    /**
     * 开始回调{@link Fragment#onResume()}
     */
    ON_RESUME,
    /**
     * 开始回调{@link Fragment#onPause()}
     */
    ON_PAUSE,
    /**
     * 开始回调{@link Fragment#onStop()}
     */
    ON_STOP,
    /**
     * 开始回调{@link Fragment#onDestroyView()}
     */
    ON_DESTROY_VIEW,
    /**
     * 开始回调{@link Fragment#onDestroy()}
     */
    ON_DESTROY,
    /**
     * 开始回调{@link Fragment#onDetach()}
     */
    ON_DETACH,
}
