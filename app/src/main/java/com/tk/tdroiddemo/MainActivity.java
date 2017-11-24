package com.tk.tdroiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroiddemo.sample.SampleHttpActivity;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                startActivity(new Intent(this, SampleHttpActivity.class));
                break;
            case R.id.btn_2:
                break;
        }
    }

}
