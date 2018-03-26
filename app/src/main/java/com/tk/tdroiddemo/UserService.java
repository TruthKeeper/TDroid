package com.tk.tdroiddemo;

import com.tk.tdroid.bridge.IService;
import com.tk.tdroid.bridge.domain.IUserService;

import io.reactivex.Observable;

/**
 * <pre>
 *      author : TK
 *      time : 2018/3/23
 *      desc :
 * </pre>
 */

public class UserService implements IUserService, IService {
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public Observable<Object> register() {
        return Observable.just(true);
    }

    @Override
    public Observable<Object> login() {
        return Observable.just(true);
    }

    @Override
    public Observable<Object> logout() {
        return Observable.just(true);
    }

    @Override
    public Observable<Object> clearData() {
        return Observable.just(true);
    }
}
