package com.tk.tdroid.widget.http.progress;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/22
 *      desc :
 * </pre>
 */

public interface ProgressListener {
    /**
     * 进度监听
     *
     * @param info
     */
    @MainThread
    void onProgress(String url, boolean isRequest, ProgressInfo info);

    /**
     * 错误回调
     *
     * @param createAt
     * @param e
     */
    @WorkerThread
    void onError(String url, boolean isRequest, long createAt, Exception e);
}
