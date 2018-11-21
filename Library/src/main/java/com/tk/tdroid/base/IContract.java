package com.tk.tdroid.base;

import android.content.Context;

import com.tk.tdroid.rx.lifecycle.ExecuteTransformer;
import com.tk.tdroid.rx.lifecycle.ILifecycle;
import com.tk.tdroid.rx.lifecycle.LifecycleTransformer;


/**
 * <pre>
 *     author : TK
 *     time   : 2017/05/26
 *     desc   : 基类契约
 * </pre>
 */
public interface IContract {
    interface IPresenter<V extends IPresenterView> {
        /**
         * 依附
         *
         * @param view
         */
        void attach(V view);

        /**
         * 解绑
         */
        void detach();

        /**
         * 绑定到View的生命周期，这里写是加了一重View层调用可能会导致的空指针
         *
         * @param lifecycle
         * @param <T>
         * @return
         */
        <T> LifecycleTransformer<T> bindLifecycle(ILifecycle lifecycle);

        /**
         * 绑定到View销毁时，这里写是加了一重View层调用可能会导致的空指针
         *
         * @param <T>
         * @return
         */
        <T> LifecycleTransformer<T> bindOnDestroy();

        /**
         * 到View某个生命周期再调度，这里写是加了一重View层调用可能会导致的空指针
         *
         * @param lifecycle
         * @param <T>
         * @return
         */
        <T> ExecuteTransformer<T> executeWhen(ILifecycle lifecycle);
    }

    interface IPresenterView {
        /**
         * 获取上下文
         *
         * @return
         */
        Context getContext();

        /**
         * 销毁
         *
         * @param anim 是否需要动画
         */
        void close(boolean anim);

        /**
         * 显示吐司
         *
         * @param tip
         */
        void showToast(String tip);

        /**
         * 显示错误吐司
         *
         * @param tip
         */
        void showErrorToast(String tip);

        /**
         * 显示loading
         *
         * @param tip
         */
        void showLoadingDialog(String tip);

        /**
         * 隐藏loading
         */
        void dismissLoadingDialog();

        /**
         * 绑定到View的生命周期
         *
         * @param lifecycle
         * @param <T>
         * @return
         */
        <T> LifecycleTransformer<T> bindLifecycle(ILifecycle lifecycle);

        /**
         * 绑定到View销毁时
         *
         * @param <T>
         * @return
         */
        <T> LifecycleTransformer<T> bindOnDestroy();

        /**
         * 到View某个生命周期再调度
         *
         * @param lifecycle
         * @param <T>
         * @return
         */
        <T> ExecuteTransformer<T> executeWhen(ILifecycle lifecycle);
    }

}
