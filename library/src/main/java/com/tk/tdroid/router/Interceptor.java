package com.tk.tdroid.router;

import android.content.Context;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/02/23
 *     desc   : 路由拦截器
 * </pre>
 */
public interface Interceptor {
    /**
     * 是否需要拦截，即中断路由
     *
     * @param cell
     * @param context
     * @return
     */
    boolean intercept(RouterCell cell, Context context);

    /**
     * 拦截后的执行动作
     *
     * @param cell
     * @param context
     */
    void onIntercepted(RouterCell cell, Context context);
}
