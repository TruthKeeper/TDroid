package com.tk.tdroiddemo.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.utils.NetworkRxObservable;
import com.tk.tdroid.utils.NetworkUtils;
import com.tk.tdroid.utils.ViewLoader;
import com.tk.tdroiddemo.R;

import java.util.Random;

import io.reactivex.functions.Consumer;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/7
 *      desc :
 * </pre>
 */

public class SampleViewLoaderActivity extends BaseActivity {
    private Toolbar toolbar;
    private LinearLayout contentLayout;
    private ViewLoader viewLoader;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_view_loader);
        toolbar = findViewById(R.id.toolbar);
        contentLayout = findViewById(R.id.content_layout);
        View errorLayout = LayoutInflater.from(this).inflate(R.layout.viewloader_error_layout, contentLayout, false);
        errorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });
        viewLoader = ViewLoader.with(contentLayout)
                .emptyView(R.layout.viewloader_empty_layout)
                .errorView(errorLayout)
                .loadingView(R.layout.viewloader_loading_layout)
                .networkInvalidView(R.layout.viewloader_no_network_layout)
                .create();
        load();
        NetworkRxObservable.getInstance().asObservable()
                .subscribe(new Consumer<NetworkUtils.NetworkEntry>() {
                    @Override
                    public void accept(NetworkUtils.NetworkEntry entry) throws Exception {
                        if (entry.isNetworkOn()) {
                            if (viewLoader.getStatus() == ViewLoader.Status.networkInvalid) {
                                load();
                            }
                        } else {
                            if (viewLoader.getStatus() == ViewLoader.Status.loading) {
                                viewLoader.showNetworkInvalidView();
                            }
                        }
                    }
                });
    }

    private void load() {
        viewLoader.showLoadingView();
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int r = new Random().nextInt(3);
                if (r == 0) {
                    viewLoader.showEmptyView();
                } else if (r == 1) {
                    viewLoader.showErrorView();
                } else {
                    viewLoader.showContentView();
                }
            }
        }, 2000);
    }

}
