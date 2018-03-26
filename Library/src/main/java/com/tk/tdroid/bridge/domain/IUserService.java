package com.tk.tdroid.bridge.domain;

import com.tk.tdroid.bridge.IService;

import io.reactivex.Observable;

/**
 * <pre>
 *      author : TK
 *      time : 2018/3/23
 *      desc :
 * </pre>
 */

public interface IUserService extends IService {
    boolean isLogin();

    Observable<Object> register();

    Observable<Object> login();

    Observable<Object> logout();

    Observable<Object> clearData();
}
