package com.tk.tdroid.rx.lifecycle;

import android.support.annotation.NonNull;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc : 生命周期的提供者
 * </pre>
 */

public interface ILifecycleProvider {
    /**
     * 绑定生命周期
     *
     * @param lifecycle
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindLifecycle(@NonNull ILifecycle lifecycle);

    /**
     * 绑定生命周期_销毁
     *
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindOnDestroy();

    /**
     * 在指定生命周期或之后执行
     *
     * @param event
     */
    <T> ExecuteTransformer<T> executeWhen(@NonNull ILifecycle event);
}
