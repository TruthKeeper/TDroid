package com.tk.tdroiddemo.http;

import com.tk.tdroid.widget.http.RuntimeUrlManager;
import com.tk.tdroid.widget.http.progress.ProgressManager;
import com.tk.tdroiddemo.bean.GitHubUser;
import com.tk.tdroiddemo.bean.IpBean;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/14
 *      desc :
 * </pre>
 */

public interface SampleAPI {
    /**
     * 静态API改变BaseUrl
     *
     * @param username
     * @return
     */
    @Headers({RuntimeUrlManager.BASE_URL + ":" + "https://api.github.com/"})
    @GET("users/{username}")
    Single<GitHubUser> getUserByGitHub(@Path("username") String username);

    /**
     * 动态改变BaseUrl
     *
     * @param username
     * @return
     */
    @Headers({RuntimeUrlManager.BASE_NAME + ":" + "GitHub_API"})
    @GET("users/{username}")
    Single<GitHubUser> getUserByGitHub_baseName(@Path("username") String username);

    /**
     * 动态改变BaseUrl
     *
     * @param baseUrl
     * @param username
     * @return
     */
    @GET("users/{username}")
    Single<GitHubUser> getUserByGitHub(@Header(RuntimeUrlManager.BASE_URL) String baseUrl, @Path("username") String username);

    /**
     * 动态改变BaseUrl
     *
     * @param baseName 自定义别名
     * @param username
     * @return
     */
    @GET("users/{username}")
    Single<GitHubUser> getUserByGitHub_baseName(@Header(RuntimeUrlManager.BASE_NAME) String baseName, @Path("username") String username);

    /**
     * 通过url监听进度
     *
     * @param ip
     * @return
     */
    @GET("iplookup/iplookup.php?format=json")
    Single<IpBean> formatIp(@Query("ip") String ip);

    /**
     * 静态API监听进度
     *
     * @param ip
     * @return
     */
    @Headers({ProgressManager.PROGRESS_HEADER + ":" + "FormatIp_API"})
    @GET("iplookup/iplookup.php?format=json")
    Single<IpBean> formatIpByHeader(@Query("ip") String ip);


    /**
     * 动态监听进度
     *
     * @param ip
     * @return
     */
    @GET("iplookup/iplookup.php?format=json")
    Single<IpBean> formatIpByHeader(@Query("ip") String ip, @Header(ProgressManager.PROGRESS_HEADER) String progressHeader);


}
