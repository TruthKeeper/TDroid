package com.tk.tdroiddemo.mine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.tdroid.annotation.AutoInject;
import com.tdroid.annotation.Router;
import com.tk.tdroid.base.BaseFragment;
import com.tk.tdroid.constants.RouterConstants;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/11
 *     desc   : xxxx描述
 * </pre>
 */
@Router(path = RouterConstants.MINE_FRAGMENT)
public class MineFragment extends BaseFragment {
    @AutoInject(desc = "位置")
    String location;
    @AutoInject(desc = "用户Id")
    long userId;

    @Override
    public boolean autoInjectData() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.<TextView>findViewById(R.id.data).setText("位置：" + location + " 用户Id：" + userId);

    }
}
