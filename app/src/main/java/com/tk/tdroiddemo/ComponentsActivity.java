package com.tk.tdroiddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tk.home.HomeFragment;
import com.tk.mine.MineFragment;
import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.utils.FragmentHelper;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/11
 *     desc   : 组件化
 * </pre>
 */
public class ComponentsActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    private RadioButton radioBtnHome;
    private RadioButton radioBtnMine;
    private FragmentHelper fragmentHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_components);
        radioGroup = findViewById(R.id.radio_group);
        radioBtnHome = findViewById(R.id.radio_btn_home);
        radioBtnMine = findViewById(R.id.radio_btn_mine);
        radioGroup.setOnCheckedChangeListener(this);

        fragmentHelper = FragmentHelper.create(getSupportFragmentManager(),
                R.id.container,
                savedInstanceState,
                FragmentHelper.FragmentData.create(HomeFragment.class),
                FragmentHelper.FragmentData.create(MineFragment.class));
        fragmentHelper.switchFragment(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_btn_home:
                fragmentHelper.switchFragment(0);
                break;
            case R.id.radio_btn_mine:
                fragmentHelper.switchFragment(1);
                break;
        }
    }
}
