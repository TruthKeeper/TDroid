package com.tk.tdroid.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.tdroid.annotation.AutoInject;
import com.tdroid.annotation.SaveAndRestore;

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
     * 是否支持观察页面可见性变化 , 回调{@link BaseFragment#onVisibleChange(boolean)}
     * <ul>
     * <li>{@link ViewPager}场景下的懒加载</li>
     * <li>{@link FragmentTransaction#show(Fragment)} 和 {@link FragmentTransaction#hide(Fragment)} 的回调</li>
     * </ul>
     *
     * @return
     */
    boolean visibleObserverEnabled();

    /**
     * 是否自动保存和恢复数据 {@link SaveAndRestore}修饰
     *
     * @return
     */
    boolean saveAndRestoreData();

    /**
     * 是否自动注入携带数据 , 用{@link AutoInject}接收
     *
     * @return
     */
    boolean autoInjectData();
}
