package com.tk.tdroid.base;

import com.tdroid.annotation.AutoInject;
import com.tdroid.annotation.SaveAndRestore;

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

    /**
     * 是否触摸隐藏软键盘
     *
     * @return
     */
    boolean touchHideSoftKeyboard();

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
