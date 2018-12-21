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

    /**
     * 内存不足或者关闭权限导致{@link android.app.Activity}非正常关闭时，是否重走LauncherActivity
     *
     * @return
     */
    boolean restartLauncherActivity();

    /**
     * 获取设计稿适配屏幕模式
     *
     * @return
     */
    DesignFit designFitMode();

    /**
     * 获取设计稿适配屏幕大小
     *
     * @return
     */
    int designSize();

    enum DesignFit {
        Width, Height
    }
}
