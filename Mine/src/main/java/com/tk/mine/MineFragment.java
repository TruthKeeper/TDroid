package com.tk.mine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.tk.tdroid.aop.annotation.CheckNetwork;
import com.tk.tdroid.aop.annotation.Logger;
import com.tk.tdroid.base.BaseFragment;
import com.tk.tdroid.utils.Toasty;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/11
 *     desc   : xxxx描述
 * </pre>
 */
public class MineFragment extends BaseFragment {
    @Logger(type = Log.ERROR)
    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @CheckNetwork
            @Override
            public void onClick(View v) {
                Toasty.show("有网络", null);
            }
        });
    }
}
