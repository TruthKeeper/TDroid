package com.tk.tdroiddemo.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tdroid.annotation.SaveAndRestore;
import com.tk.tdroid.base.BaseFragment;
import com.tk.tdroiddemo.R;

import java.util.Random;

/**
 * <pre>
 *      author : TK
 *      time : 2018/1/17
 *      desc :
 * </pre>
 */

public class SampleSaveRestoreFragment extends BaseFragment {
    private TextView tvTip;
    @SaveAndRestore(gson = true)
    SampleSaveRestoreActivity.Sample sample;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sample_save_restore;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTip = view.findViewById(R.id.tv_tip);
        if (savedInstanceState != null) {
            String gsonStr = new Gson().toJson(sample);
            Log.e("SaveRestoreFragment", gsonStr);
            tvTip.setText(gsonStr);
        }
    }

    @Override
    public boolean saveAndRestoreData() {
        return true;
    }

    public void init() {
        sample = new SampleSaveRestoreActivity.Sample();
        sample.setId(new Random().nextInt(100));
        sample.setName("tempName");

        tvTip.setText(new Gson().toJson(sample));
    }
}
