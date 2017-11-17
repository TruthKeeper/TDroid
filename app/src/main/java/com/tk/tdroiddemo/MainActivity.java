package com.tk.tdroiddemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tk.tdroid.base.LifecycleActivity;
import com.tk.tdroid.rx.AsyncCall;
import com.tk.tdroid.rx.lifecycle.ActivityLifecycleImpl;
import com.tk.tdroid.widget.http.HttpUtils;
import com.tk.tdroiddemo.bean.GitHubUser;
import com.tk.tdroiddemo.http.SampleAPI;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;


public class MainActivity extends LifecycleActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.change).setOnClickListener(this);
//        RuntimeUrlManager.getInstance().addBaseUrl("github", "https://www.google.com");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                HttpUtils.create(SampleAPI.class)
                        .getUserByBaseUrl("JakeWharton")
                        .compose(executeWhen(ActivityLifecycleImpl.ON_PAUSE))
                        .compose(new AsyncCall<GitHubUser>())
                        .subscribe(new SingleObserver<GitHubUser>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(GitHubUser gitHubUser) {
                                Log.e("onPause", gitHubUser.getName());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                break;
            case R.id.change:
//                RuntimeUrlManager.getInstance().addBaseUrl("github", "https://api.github.com");
                break;
        }
    }
}
