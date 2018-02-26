package com.tk.tdroid.router;

import java.util.Map;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/24
 *     desc   :
 * </pre>
 */
public interface IRouterTable {
    /**
     * 获取Activity映射表
     *
     * @return
     */
    Map<String, Class<?>> getActivityMap();

    /**
     * 获取Service映射表
     *
     * @return
     */
    Map<String, Class<?>> getServiceMap();

    /**
     * 获取Fragment映射表
     *
     * @return
     */
    Map<String, Class<?>> getFragmentMap();

    /**
     * 获取FragmentV4映射表
     *
     * @return
     */
    Map<String, Class<?>> getFragmentV4Map();

}
