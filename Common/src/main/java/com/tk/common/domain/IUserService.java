package com.tk.common.domain;


import com.tk.common.IService;

import io.reactivex.Observable;

/**
 * <pre>
 *      author : TK
 *      time : 2018/4/9
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
