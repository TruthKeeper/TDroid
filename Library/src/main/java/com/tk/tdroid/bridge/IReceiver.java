package com.tk.tdroid.bridge;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/29
 *     desc   : 事件接收器
 * </pre>
 */
public interface IReceiver {
    /**
     * 接收到事件
     *
     * @param eventTag 事件唯一标识
     * @param event    传递空值或者JSON
     */
    void onEventReceive(@NonNull String eventTag, @Nullable String event);
}
