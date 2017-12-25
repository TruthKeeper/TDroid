package com.tk.tdroid.http.progress;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/22
 *      desc : 进度回调 , 默认回调时间间隔{@link ProgressManager#DEFAULT_INTERVAL_TIME}
 * </pre>
 */

public interface ProgressListener {
    /**
     * 进度监听
     *
     * @param key       唯一标识 , 通常是url(一般场景) or header(key:{@link ProgressManager#PROGRESS_HEADER})的value的值 ,
     *                  用于同一url下的不同body的{@link retrofit2.http.POST}请求或者url会产生重定向
     * @param isRequest 请求 or 响应
     * @param info      实体
     */
    @MainThread
    void onProgress(String key, boolean isRequest, ProgressInfo info);

    /**
     * 错误回调
     *
     * @param key       唯一标识
     * @param isRequest 请求 or 响应
     * @param createAt  请求 or 响应 创建时间
     * @param e         异常
     */
    @WorkerThread
    void onError(String key, boolean isRequest, long createAt, Exception e);
}
