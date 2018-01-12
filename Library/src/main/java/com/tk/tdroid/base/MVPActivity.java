package com.tk.tdroid.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tk.tdroid.utils.ReflectUtils;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/19
 *      desc :
 * </pre>
 */

public class MVPActivity<P extends IContract.IPresenter> extends BaseActivity implements IContract.IPresenterView {
    public P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<P> presenter = ReflectUtils.getT(getClass(), 0);
        mPresenter = generatePresenter(presenter);
        if (mPresenter != null) {
            mPresenter.attach(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
            mPresenter = null;
        }
    }

    /**
     * 子类开启APT支持后可反射实例化
     *
     * @param pClass
     * @return
     */
    public P generatePresenter(@Nullable Class<P> pClass) {
        if (pClass == null) {
            return null;
        }
        try {
            return pClass.newInstance();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void closeImmediately() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void showToast(String tip) {

    }

    @Override
    public void showErrorToast(String tip) {

    }

    @Override
    public void showLoadingDialog(String tip) {

    }

    @Override
    public void dismissLoadingDialog() {

    }

}
