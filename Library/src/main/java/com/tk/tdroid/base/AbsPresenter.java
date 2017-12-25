package com.tk.tdroid.base;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/19
 *      desc :
 * </pre>
 */

public class AbsPresenter<V extends IContract.IPresenterView> implements IContract.IPresenter<V> {
    public V mView;

    @Override
    public void attach(V view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }
}
