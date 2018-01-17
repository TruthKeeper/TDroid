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

    /**
     * 是否触摸隐藏软键盘
     *
     * @return
     */
    boolean touchHideSoftKeyboard();

    /**
     * 是否自动恢复数据 {@link com.tdroid.annotation.Save}修饰
     *
     * @return
     */
    boolean saveData();

}
