package com.tk.tdroid.bridge;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/02/28
 *     desc   : 基类服务
 * </pre>
 */
public interface IService {
    /**
     * 获取服务的名称，作为{@link ServiceManager#get(String)}
     *
     * @return
     */
    String getName();
}
