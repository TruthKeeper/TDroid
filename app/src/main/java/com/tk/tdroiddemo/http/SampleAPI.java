package com.tk.tdroiddemo.http;

import com.tk.tdroid.widget.http.RuntimeUrlManager;
import com.tk.tdroiddemo.bean.GitHubUser;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/14
 *      desc :
 * </pre>
 */

public interface SampleAPI {
    @GET("users/{username}")
    Single<GitHubUser> getUserByBaseUrl(@Header(RuntimeUrlManager.BASE_URL) String baseUrl, @Path("username") String username);

    @Headers({RuntimeUrlManager.BASE_URL + ":" + "https://api.github.com/users/"})
    @GET("users/{username}")
    Single<GitHubUser> getUserByBaseUrl(@Path("username") String username);

    @GET("users/{username}")
    Single<GitHubUser> getUserByBaseName(@Header(RuntimeUrlManager.BASE_NAME) String baseName, @Path("username") String username);

    @Headers({RuntimeUrlManager.BASE_NAME + ":" + "github"})
    @GET("users/{username}")
    Single<GitHubUser> getUserByBaseName(@Path("username") String username);


}
