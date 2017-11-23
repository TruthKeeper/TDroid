package com.tk.tdroiddemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.rx.AsyncCall;
import com.tk.tdroid.utils.SizeUtils;
import com.tk.tdroid.widget.http.HttpUtils;
import com.tk.tdroid.widget.http.progress.ProgressInfo;
import com.tk.tdroid.widget.http.progress.ProgressListener;
import com.tk.tdroid.widget.http.progress.ProgressManager;
import com.tk.tdroid.widget.image.GlideApp;
import com.tk.tdroiddemo.bean.GitHubUser;
import com.tk.tdroiddemo.http.SampleAPI;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;


public class MainActivity extends BaseActivity implements View.OnClickListener, ProgressListener {
    private String imageUrl = "https://raw.githubusercontent.com/TruthKeeper/Note/master/Http/OSI%E4%B8%83%E5%B1%82%E5%8D%8F%E8%AE%AE.png";
    private String httpUrl = "https://api.github.com/users/JakeWharton";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.change).setOnClickListener(this);
//        RuntimeUrlManager.getInstance().addBaseUrl("github", "https://www.google.com");

        ProgressManager.getInstance().addResponseListener(httpUrl, this);
        ProgressManager.getInstance().addResponseListener(imageUrl, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                HttpUtils.create(SampleAPI.class)
                        .getUserByBaseUrl("JakeWharton")
                        .compose(new AsyncCall<GitHubUser>())
                        .subscribe(new SingleObserver<GitHubUser>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(GitHubUser gitHubUser) {
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                break;
            case R.id.change:
//                RuntimeUrlManager.getInstance().addBaseUrl("github", "https://api.github.com");
                GlideApp.with(this)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                Log.e("onResourceReady", "success");
                            }
                        });
                break;
        }
    }


    /**
     * 进度监听
     *
     * @param url
     * @param isRequest
     * @param info
     */
    @Override
    public void onProgress(String url, boolean isRequest, ProgressInfo info) {
        StringBuilder builder = new StringBuilder();
        builder.append(isRequest ? "请求" : "响应");
        builder.append(url);
        builder.append(info.isFinish() ? "结束" : "中");
        builder.append(" ");
        builder.append(info.getContentLength());
        builder.append("\n");
        builder.append(info.getPercent());
        builder.append("%");
        builder.append(" ");
        builder.append(SizeUtils.formatSize(info.getSpeed()));
        builder.append("/s");
        Log.e("onProgress", builder.toString());
    }

    /**
     * 错误回调
     *
     * @param url
     * @param isRequest
     * @param createAt
     * @param e
     */
    @Override
    public void onError(String url, boolean isRequest, long createAt, Exception e) {
        Log.e("onError", url + " " + e.toString());
    }
}
