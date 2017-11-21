package com.tk.tdroid.base;

import android.support.v4.view.ViewPager;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/20
 *      desc : Fragment提供者
 * </pre>
 */

interface IFragmentProvider {
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

    /**
     * 是否支持观察页面可见性变化 , 用于{@link ViewPager}场景下的懒加载 , 重写{@link BaseFragment#onVisibleChange(boolean)}
     *
     * @return
     */
    boolean visibleObserverEnabled();
}
