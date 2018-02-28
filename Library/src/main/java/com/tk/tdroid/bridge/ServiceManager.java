package com.tk.tdroid.bridge;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.Map;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/02/28
 *     desc   : 容器单例，用于注册服务以供组件化场景下的跨模块调用
 * </pre>
 */
public final class ServiceManager {
    private static volatile ServiceManager mServiceManager = null;
    private final Map<Class<?>, IService> mServiceMap = new ArrayMap<>();

    private ServiceManager() {
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static ServiceManager getInstance() {
        if (mServiceManager == null) {
            synchronized (ServiceManager.class) {
                if (mServiceManager == null) {
                    mServiceManager = new ServiceManager();
                }
            }
        }
        return mServiceManager;
    }

    /**
     * 注册一个服务
     *
     * @param cls
     * @param service
     * @return
     */
    public <Service extends IService> ServiceManager register(@NonNull Class<Service> cls, @NonNull Service service) {
        mServiceMap.put(cls, service);
        return this;
    }

    /**
     * 获取注册的服务
     *
     * @param cls
     * @param <Service>
     * @return
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public <Service> Service get(@NonNull Class<Service> cls) {
        final IService service = mServiceMap.get(cls);
        if (service == null) {
            throw new NullPointerException("Service is not registered !");
        }
        return (Service) service;
    }
}