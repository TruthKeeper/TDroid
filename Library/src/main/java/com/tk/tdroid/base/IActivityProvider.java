package com.tk.tdroid.base;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/20
 *      desc : Activity提供者
 * </pre>
 */

interface IActivityProvider {
    /**
     * 是否支持Rx生命周期
     *
     * @return
     */
    boolean bindLifecycleEnabled();

    /**
     * 是否支持EventBus事件监听
     *
     * @return
     */
    boolean eventBusEnabled();
}
