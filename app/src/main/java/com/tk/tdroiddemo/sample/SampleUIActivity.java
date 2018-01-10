package com.tk.tdroiddemo.sample;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroiddemo.R;
import com.tk.tdroid.ui.TUIRelativeLayout;
import com.tk.tdroid.ui.TUITextView;

/**
 * <pre>
 *      author : TK
 *      time : 2018/1/2
 *      desc :
 * </pre>
 */

public class SampleUIActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TUIRelativeLayout rlContainer;
    private TUITextView tvSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_ui);
        toolbar = findViewById(R.id.toolbar);
        rlContainer = findViewById(R.id.rl_container);
        tvSelected = findViewById(R.id.tv_selected);
        rlContainer.setOnClickListener(this);
        tvSelected.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_container:
                rlContainer.setSelected(!rlContainer.isSelected());
                break;
            case R.id.tv_selected:
                tvSelected.setSelected(!tvSelected.isSelected());
                break;
        }
    }
}
