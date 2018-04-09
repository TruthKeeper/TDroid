package com.tk.tdroid.bridge;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.tk.common.IService;

import java.util.Map;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/02/28
 *     desc   : 容器管理器，用于注册服务以供组件化场景下的跨模块调用
 * </pre>
 */
public final class ServiceManager {
    private final static Map<String, IService> mServiceMap = new ArrayMap<>();

    private ServiceManager() {
        throw new IllegalStateException();
    }

    /**
     * AutoRegister自动会在字节码中插入注册代码
     */
    public static void init() {
    }

    /**
     * 注册一个服务
     *
     * @param service
     * @param <Service>
     */
    public static <Service extends IService> void register(@NonNull Service service) {
        mServiceMap.put(service.getName(), service);
    }

    /**
     * 获取注册的服务
     *
     * @param serviceName
     * @param <Service>
     * @return
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <Service extends IService> Service get(@NonNull String serviceName) {
        final IService service = mServiceMap.get(serviceName);
        if (service == null) {
            throw new NullPointerException("Service is not registered !");
        }
        return (Service) service;
    }
}