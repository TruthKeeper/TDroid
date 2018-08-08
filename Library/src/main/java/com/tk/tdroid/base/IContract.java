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
        void attach(V view);

        void detach();
    }

    interface IPresenterView {
        Context getContext();

        void close(boolean anim);

        void showToast(String tip);

        void showErrorToast(String tip);

        void showLoadingDialog(String tip);

        void dismissLoadingDialog();

        <T> LifecycleTransformer<T> bindLifecycle(ILifecycle lifecycle);

        <T> LifecycleTransformer<T> bindOnDestroy();

        <T> ExecuteTransformer<T> executeWhen(ILifecycle lifecycle);
    }

}
