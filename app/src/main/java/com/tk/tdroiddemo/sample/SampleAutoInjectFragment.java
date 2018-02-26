package com.tk.tdroiddemo.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.tdroid.annotation.AutoInject;
import com.tk.tdroid.base.BaseFragment;
import com.tk.tdroid.utils.CollectionUtils;
import com.tk.tdroiddemo.R;

/**
 * <pre>
 *      author : TK
 *      time : 2018/1/26
 *      desc :
 * </pre>
 */

public class SampleAutoInjectFragment extends BaseFragment {
    @AutoInject
    String[] tags;
    private TextView tv;

    @Override
    public boolean autoInjectData() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sample_auto_inject;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tv = view.findViewById(R.id.tv);
        tv.setText("Fragment：tags = " + CollectionUtils.getContent(tags, ","));
    }
}
