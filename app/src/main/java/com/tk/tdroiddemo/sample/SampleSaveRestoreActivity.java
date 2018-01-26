package com.tk.tdroiddemo.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tdroid.annotation.SaveAndRestore;
import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.utils.CollectionUtils;
import com.tk.tdroid.utils.ConvertUtils;
import com.tk.tdroiddemo.R;

import java.util.Random;

/**
 * <pre>
 *      author : TK
 *      time : 2018/1/16
 *      desc :
 * </pre>
 */

public class SampleSaveRestoreActivity extends BaseActivity {
    private TextView tvTip;
    private Button btnConfirm;
    @SaveAndRestore
    String content;
    @SaveAndRestore
    int[] ids;
    @SaveAndRestore(gson = true)
    Sample sample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_save_restore);
        tvTip = findViewById(R.id.tv_tip);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample = new Sample();
                sample.setId(new Random().nextInt(100));
                sample.setName("tempName");
                ids = new int[10];
                for (int i = 0; i < ids.length; i++) {
                    ids[i] = new Random().nextInt(100);
                }
                tvTip.setText(new Gson().toJson(sample));
                tvTip.append("\n");
                tvTip.append(CollectionUtils.getContent(ConvertUtils.convert(ids), ","));

                ((SampleSaveRestoreFragment) getSupportFragmentManager().findFragmentById(R.id.fragment)).init();
            }
        });
    }

    @Override
    public boolean saveAndRestoreData() {
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String gsonStr = new Gson().toJson(sample);
            Log.e("SaveRestoreActivity", gsonStr);
            tvTip.setText(gsonStr);
            if (ids != null) {
                String arrayStr = CollectionUtils.getContent(ConvertUtils.convert(ids), ",");
                Log.e("SaveRestoreActivity", arrayStr);
                tvTip.append("\n");
                tvTip.append(arrayStr);
            }
        }
    }

    public static class Sample {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
