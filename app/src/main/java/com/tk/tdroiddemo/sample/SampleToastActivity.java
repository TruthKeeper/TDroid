package com.tk.tdroiddemo.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.utils.DensityUtil;
import com.tk.tdroid.utils.Toasty;
import com.tk.tdroiddemo.R;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/15
 *      desc :
 * </pre>
 */

public class SampleToastActivity extends BaseActivity implements View.OnClickListener {
    public Toasty.Config right;
    public Toasty.Config error;
    public Toasty.Config custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_toast);
        right = new Toasty.Config.Builder()
                .cornerRadius(Integer.MAX_VALUE)
                .backgroundColor(Color.GREEN)
                .textColor(Color.WHITE)
                .icon(ContextCompat.getDrawable(this, R.drawable.vector_right))
                .build();
        error = new Toasty.Config.Builder()
                .cornerRadius(Integer.MAX_VALUE)
                .backgroundColor(Color.RED)
                .textColor(Color.WHITE)
                .icon(ContextCompat.getDrawable(this, R.drawable.vector_error))
                .build();
        custom = new Toasty.Config.Builder()
                .cornerRadius(DensityUtil.dp2px(4))
                .backgroundColorRes(R.color.colorPrimary)
                .textColor(Color.WHITE)
                .icon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher))
                .alpha(200)
                .tintByTextColor(false)
                .build();

        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                Toasty.show("普通的Toast", null);
                break;
            case R.id.btn_2:
                Toasty.show("打印错误的Toast", error);
                break;
            case R.id.btn_3:
                Toasty.show("打印成功的Toast", right);
                break;
            case R.id.btn_4:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.show("其他Toast", custom);
                    }
                }).start();
                break;

        }
    }

}
