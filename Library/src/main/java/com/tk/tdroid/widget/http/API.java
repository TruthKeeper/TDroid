package com.tk.tdroid.widget.http;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/05
 *     desc   : xxxx描述
 * </pre>
 */
public interface API {
    @POST("")
    Observable<Object> c(@Url String url);
}
