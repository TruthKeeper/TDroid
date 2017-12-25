package com.tk.tdroid.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/11/20
 *     desc   : 封装事件类型
 * </pre>
 */
public class Event<T> {
    private String tag;
    private T body;

    public static Event<Void> with(@NonNull String tag) {
        return new Event<Void>(tag, null);
    }

    public static <E> Event<E> with(@NonNull String tag, @Nullable E body) {
        return new Event<E>(tag, body);
    }

    private Event(String tag, T body) {
        this.tag = tag;
        this.body = body;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Event{" +
                "tag='" + tag + '\'' +
                ", body=" + body +
                '}';
    }
}
