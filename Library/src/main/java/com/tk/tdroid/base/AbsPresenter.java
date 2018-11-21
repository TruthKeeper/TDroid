package com.tk.tdroid.base;

import com.tk.tdroid.rx.lifecycle.ExecuteTransformer;
import com.tk.tdroid.rx.lifecycle.ILifecycle;
import com.tk.tdroid.rx.lifecycle.LifecycleTransformer;

import io.reactivex.Observable;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/19
 *      desc :
 * </pre>
 */

public class AbsPresenter<V extends IContract.IPresenterView> implements IContract.IPresenter<V> {
    public V mView;

    /**
     * 依附
     *
     * @param view
     */
    @Override
    public void attach(V view) {
        mView = view;
    }

    /**
     * 解绑
     */
    @Override
    public void detach() {
        mView = null;
    }

    /**
     * 绑定到View的生命周期，这里写是加了一重View层调用可能会导致的空指针
     *
     * @param lifecycle
     * @return
     */
    @Override
    public <T> LifecycleTransformer<T> bindLifecycle(ILifecycle lifecycle) {
        if (mView == null) {
            return new LifecycleTransformer<>(Observable.just(new Object()));
        }
        return mView.bindLifecycle(lifecycle);
    }

    /**
     * 绑定到View销毁时，这里写是加了一重View层调用可能会导致的空指针
     *
     * @return
     */
    @Override
    public <T> LifecycleTransformer<T> bindOnDestroy() {
        if (mView == null) {
            return new LifecycleTransformer<>(Observable.just(new Object()));
        }
        return mView.bindOnDestroy();
    }

    /**
     * 到View某个生命周期再调度，这里写是加了一重View层调用可能会导致的空指针
     *
     * @param lifecycle
     * @return
     */
    @Override
    public <T> ExecuteTransformer<T> executeWhen(ILifecycle lifecycle) {
        if (mView == null) {
            return new ExecuteTransformer<>(Observable.empty());
        }
        return mView.executeWhen(lifecycle);
    }
}
