package com.tk.tdroiddemo.base;

import android.support.annotation.Nullable;

import com.apt.InstanceFactory;
import com.tk.tdroid.base.AbsPresenter;
import com.tk.tdroid.base.MVPActivity;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/19
 *      desc :
 * </pre>
 */

public class BaseMvpActivity<P extends AbsPresenter> extends MVPActivity<P> {
    /**
     * 子类开启APT支持后可通过{@link InstanceFactory}代替反射实例化
     *
     * @param pClass
     * @return
     */
    @Override
    public P generatePresenter(@Nullable Class<P> pClass) {
        return InstanceFactory.create(pClass);
    }
}
