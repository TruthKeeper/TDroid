package com.tk.mine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tk.tdroid.base.BaseFragment;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/11
 *     desc   : xxxx描述
 * </pre>
 */
public class MineFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
