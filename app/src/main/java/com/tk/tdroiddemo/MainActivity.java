package com.tk.tdroiddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tk.tdroid.rx.AsyncCall;
import com.tk.tdroid.widget.http.HttpUtils;
import com.tk.tdroiddemo.bean.GitHubUser;
import com.tk.tdroiddemo.http.SampleAPI;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
                        .compose(new AsyncCall<GitHubUser>())
                        .subscribe(new Observer<GitHubUser>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(GitHubUser value) {
                                Toast.makeText(MainActivity.this, value.getName(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case R.id.change:
//                RuntimeUrlManager.getInstance().addBaseUrl("github", "https://api.github.com");
                break;
        }
    }
}
