package com.tk.tdroiddemo.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.utils.SizeUtils;
import com.tk.tdroid.http.progress.ProgressInfo;
import com.tk.tdroid.http.progress.ProgressListener;
import com.tk.tdroid.http.progress.ProgressManager;
import com.tk.tdroiddemo.R;

import retrofit2.http.POST;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/24
 *      desc :
 * </pre>
 */

public class SampleHttpActivity extends BaseActivity implements View.OnClickListener, ProgressListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_http);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        ProgressManager.getInstance().addResponseListener("https://api.github.com/users/JakeWharton", this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                SampleHttp.runtimeUrl_1(this);
                break;
            case R.id.btn_2:
                SampleHttp.runtimeUrl_2(this);
                break;
            case R.id.btn_3:
                SampleHttp.runtimeUrl_3(this);
                break;
            case R.id.btn_4:
                SampleHttp.runtimeUrl_4(this);
                break;

            case R.id.btn_5:
                SampleHttp.progress_1(this);
                break;
            case R.id.btn_6:
                SampleHttp.progress_2(this);
                break;
            case R.id.btn_7:
                SampleHttp.progress_3(this);
                break;
            case R.id.btn_8:
                SampleHttp.progress_4(this);
                break;
            case R.id.btn_9:
                SampleHttp.progress_5(this);
                break;
        }
    }

    /**
     * 进度监听
     *
     * @param key       唯一标识 , 通常是url(一般场景) or header(key:{@link ProgressManager#PROGRESS_HEADER})的value的值 ,
     *                  用于同一url下的不同body的{@link POST}请求或者url会产生重定向
     * @param isRequest 请求 or 响应
     * @param info      实体
     */
    @Override
    public void onProgress(String key, boolean isRequest, ProgressInfo info) {
        StringBuilder builder = new StringBuilder();
        builder.append(isRequest ? "请求" : "响应");
        builder.append("唯一标识:");
        builder.append(" ");
        builder.append(key);
        builder.append(" ");
        builder.append(info.isFinish() ? "结束" : "中");
        builder.append(" ");
        builder.append(info.getContentLength());
        builder.append("\n");
        builder.append(info.getPercent());
        builder.append("% ");
        builder.append(SizeUtils.formatSize(info.getSpeed()));
        builder.append("/s");
        Log.e("onProgress", builder.toString());
    }

    /**
     * 错误回调
     *
     * @param key       唯一标识
     * @param isRequest 请求 or 响应
     * @param createAt  请求 or 响应 创建时间
     * @param e         异常
     */
    @Override
    public void onError(String key, boolean isRequest, long createAt, Exception e) {
        StringBuilder builder = new StringBuilder();
        builder.append(isRequest ? "请求" : "响应");
        builder.append("唯一标识:");
        builder.append(" ");
        builder.append(key);
        builder.append("异常");
        builder.append(" ");
        builder.append(e);
        Log.e("onError", builder.toString());
    }
}
