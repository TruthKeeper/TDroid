package com.tk.tdroid.bridge;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/29
 *     desc   : 事件接收器
 * </pre>
 */
public interface IReceiver<Event> {
    /**
     * 执行
     *
     * @param event
     */
    void call(Event event);
}
