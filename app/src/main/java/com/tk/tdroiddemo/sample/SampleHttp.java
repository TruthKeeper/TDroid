package com.tk.tdroiddemo.sample;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tk.tdroid.http.HttpUtils;
import com.tk.tdroid.http.RuntimeUrlManager;
import com.tk.tdroid.http.progress.ProgressManager;
import com.tk.tdroid.image.ImageLoader;
import com.tk.tdroid.image.LoaderCallback;
import com.tk.tdroid.image.glide.GlideApp;
import com.tk.tdroid.rx.AsyncCall;
import com.tk.tdroid.utils.Utils;
import com.tk.tdroiddemo.bean.GitHubUser;
import com.tk.tdroiddemo.bean.IpBean;
import com.tk.tdroiddemo.http.SampleAPI;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/24
 *      desc : Http的访问测试代码
 * </pre>
 */

public class SampleHttp {
    public static void runtimeUrl_1(SampleHttpActivity activity) {
        //默认在App中配置的BaseUrl是 https://www.baidu.com/

        //静态API改变BaseUrl
        HttpUtils.create(SampleAPI.class)
                .getUserByGitHub("JakeWharton")
                .compose(new AsyncCall<GitHubUser>())
                .compose(activity.bindOnDestroy())
                .subscribe(new SingleObserver<GitHubUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(GitHubUser gitHubUser) {
                        Toast.makeText(Utils.getApp(), gitHubUser.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public static void runtimeUrl_2(SampleHttpActivity activity) {
        //默认在App中配置的BaseUrl是 https://www.baidu.com/

        //动态改变BaseUrl
        HttpUtils.create(SampleAPI.class)
                .getUserByGitHub("https://api.github.com/", "JakeWharton")
                .compose(new AsyncCall<GitHubUser>())
                .compose(activity.bindOnDestroy())
                .subscribe(new SingleObserver<GitHubUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(GitHubUser gitHubUser) {
                        Toast.makeText(Utils.getApp(), gitHubUser.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public static void runtimeUrl_3(SampleHttpActivity activity) {
        //默认在App中配置的BaseUrl是 https://www.baidu.com/

        //动态改变BaseUrl
        RuntimeUrlManager.getInstance().addBaseUrl("GitHub_API", "https://api.github.com/");
        HttpUtils.create(SampleAPI.class)
                .getUserByGitHub_baseName("JakeWharton")
                .compose(new AsyncCall<GitHubUser>())
                .compose(activity.bindOnDestroy())
                .subscribe(new SingleObserver<GitHubUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(GitHubUser gitHubUser) {
                        Toast.makeText(Utils.getApp(), gitHubUser.getName(), Toast.LENGTH_SHORT).show();
                        RuntimeUrlManager.getInstance().removeBaseUrl("GitHub_API");
                    }

                    @Override
                    public void onError(Throwable e) {
                        RuntimeUrlManager.getInstance().removeBaseUrl("GitHub_API");
                    }
                });
    }

    public static void runtimeUrl_4(SampleHttpActivity activity) {
        //默认在App中配置的BaseUrl是 https://www.baidu.com/

        //动态改变BaseUrl
        RuntimeUrlManager.getInstance().addBaseUrl("GitHub", "https://api.github.com/");
        HttpUtils.create(SampleAPI.class)
                .getUserByGitHub_baseName("GitHub", "JakeWharton")
                .compose(new AsyncCall<GitHubUser>())
                .compose(activity.bindOnDestroy())
                .subscribe(new SingleObserver<GitHubUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(GitHubUser gitHubUser) {
                        Toast.makeText(Utils.getApp(), gitHubUser.getName(), Toast.LENGTH_SHORT).show();
                        RuntimeUrlManager.getInstance().removeBaseUrl("GitHub");
                    }

                    @Override
                    public void onError(Throwable e) {
                        RuntimeUrlManager.getInstance().removeBaseUrl("GitHub");
                    }
                });
    }

    public static void progress_1(SampleHttpActivity activity) {
        //默认在App中配置的BaseUrl是 https://www.baidu.com/

        //切换成新浪的
        RuntimeUrlManager.getInstance().setGlobalBaseUrl("http://int.dpool.sina.com.cn/");
        final String url = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=1.2.4.8";
        //监听Url
        ProgressManager.getInstance().addResponseListener(url, activity);
        HttpUtils.create(SampleAPI.class)
                .formatIp("1.2.4.8")
                .compose(new AsyncCall<IpBean>())
                .compose(activity.bindOnDestroy())
                .subscribe(new SingleObserver<IpBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(IpBean ipBean) {
                        Toast.makeText(Utils.getApp(), ipBean.getDistrict(), Toast.LENGTH_SHORT).show();
                        RuntimeUrlManager.getInstance().removeGlobalBaseUrl();
                        ProgressManager.getInstance().removeListener(url);
                    }

                    @Override
                    public void onError(Throwable e) {
                        RuntimeUrlManager.getInstance().removeGlobalBaseUrl();
                        ProgressManager.getInstance().removeListener(url);
                    }
                });
    }

    public static void progress_2(SampleHttpActivity activity) {
        //默认在App中配置的BaseUrl是 https://www.baidu.com/

        //切换成新浪的
        RuntimeUrlManager.getInstance().setGlobalBaseUrl("http://int.dpool.sina.com.cn/");
        final String header = "FormatIp_API";
        //监听Header
        ProgressManager.getInstance().addResponseListener(header, activity);
        HttpUtils.create(SampleAPI.class)
                .formatIpByHeader("1.2.4.8")
                .compose(new AsyncCall<IpBean>())
                .compose(activity.bindOnDestroy())
                .subscribe(new SingleObserver<IpBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(IpBean ipBean) {
                        Toast.makeText(Utils.getApp(), ipBean.getDistrict(), Toast.LENGTH_SHORT).show();
                        ProgressManager.getInstance().removeListener(header);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ProgressManager.getInstance().removeListener(header);

                    }
                });
    }

    public static void progress_3(SampleHttpActivity activity) {
        //默认在App中配置的BaseUrl是 https://www.baidu.com/

        //切换成新浪的
        RuntimeUrlManager.getInstance().setGlobalBaseUrl("http://int.dpool.sina.com.cn/");
        final String header = Long.toString(System.currentTimeMillis());
        //监听动态Header
        ProgressManager.getInstance().addResponseListener(header, activity);
        HttpUtils.create(SampleAPI.class)
                .formatIpByHeader("1.2.4.8", header)
                .compose(new AsyncCall<IpBean>())
                .compose(activity.bindOnDestroy())
                .subscribe(new SingleObserver<IpBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(IpBean ipBean) {
                        Toast.makeText(Utils.getApp(), ipBean.getDistrict(), Toast.LENGTH_SHORT).show();
                        ProgressManager.getInstance().removeListener(header);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ProgressManager.getInstance().removeListener(header);
                    }
                });
    }

    /**
     * 监听Glide , 有缓存则无回调
     *
     * @param activity
     */
    public static void progress_4(SampleHttpActivity activity) {
        final String imageUrl = "https://raw.githubusercontent.com/TruthKeeper/Note/master/Http/OSI%E4%B8%83%E5%B1%82%E5%8D%8F%E8%AE%AE.png";
        //监听动态Header
        ProgressManager.getInstance().addResponseListener(imageUrl, activity);
        ImageLoader.with(activity)
                .load(imageUrl)
                .memoryCache(false)
                .diskCache(false)
                .into(new LoaderCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        Toast.makeText(Utils.getApp(), "加载图像完毕", Toast.LENGTH_SHORT).show();
                        ProgressManager.getInstance().removeListener(imageUrl);
                    }
                });
    }

    /**
     * 监听Glide (连接重定向时), 有缓存则无回调
     *
     * @param activity
     */
    public static void progress_5(SampleHttpActivity activity) {
        //imageUrl_1 会重定向到imageUrl_2
        final String imageUrl_1 = "https://github.com/TruthKeeper/Note/raw/master/Http/OSI%E4%B8%83%E5%B1%82%E5%8D%8F%E8%AE%AE.png";
        final String imageUrl_2 = "https://raw.githubusercontent.com/TruthKeeper/Note/master/Http/OSI%E4%B8%83%E5%B1%82%E5%8D%8F%E8%AE%AE.png";

        final String key = imageUrl_1;
        //监听动态Header
        ProgressManager.getInstance().addResponseListener(key, activity);
        GlideUrl url = ProgressManager.parseGlideUrl(imageUrl_1, key);
        GlideApp.with(activity)
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        Toast.makeText(Utils.getApp(), "加载图像完毕", Toast.LENGTH_SHORT).show();
                        ProgressManager.getInstance().removeListener(key);
                    }
                });
    }
}
