package com.tk.tdroid.bridge;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/8/16
 *     desc   : 接收到事件后自动反注册
 * </pre>
 */
public abstract class AutoCompleteReceiver implements IReceiver {

    /**
     * 接收到事件
     *
     * @param eventTag 事件唯一标识
     * @param event    传递空值或者JSON
     */
    @Override
    public void onEventReceive(@NonNull String eventTag, @Nullable String event) {
        EventManager.getInstance().removeReceiver(eventTag, this);
        onProcessEvent(eventTag, event);
    }

    /**
     * 处理事件
     *
     * @param eventTag 事件唯一标识
     * @param event    传递空值或者JSON
     */
    public abstract void onProcessEvent(@NonNull String eventTag, @Nullable String event);
}
