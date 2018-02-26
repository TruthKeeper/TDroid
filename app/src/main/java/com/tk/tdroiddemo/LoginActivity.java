package com.tk.tdroiddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.router.TRouter;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/02/23
 *     desc   : 组件化
 * </pre>
 */
public class LoginActivity extends BaseActivity {
    public static final String ROUTER = "router";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void login(View view) {
        TRouter.resume(getIntent().getBundleExtra(ROUTER), this);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }

}
