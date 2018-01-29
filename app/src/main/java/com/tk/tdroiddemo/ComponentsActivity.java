package com.tk.tdroiddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.constants.RouterConstants;
import com.tk.tdroid.router.TRouter;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/11
 *     desc   : 组件化
 * </pre>
 */
public class ComponentsActivity extends BaseActivity implements View.OnClickListener {

    private Button btnHome;
    private Button btnMine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_components);
        btnHome = findViewById(R.id.btn_home);
        btnMine = findViewById(R.id.btn_mine);
        btnHome.setOnClickListener(this);
        btnMine.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //App工程可以直接获取到MineActivity对象，但此处演示路由跳转
            case R.id.btn_home:
                TRouter.with(RouterConstants.HOME_ACTIVITY)
                        .anim(R.anim.in_from_right, R.anim.out_to_left)
                        .request(this);
                break;
            case R.id.btn_mine:
                TRouter.with(RouterConstants.MINE_ACTIVITY)
                        .putString("nickName", "张三")
                        .putLong("userId", 233)
                        .putBoolean("gender", true)
                        .anim(R.anim.in_from_right, R.anim.out_to_left)
                        .request(this);
                break;
        }
    }
}
