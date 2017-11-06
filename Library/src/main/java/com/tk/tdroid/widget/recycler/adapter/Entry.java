package com.tk.tdroid.widget.recycler.adapter;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/6
 *      desc : {@link FasterAdapter#mList}数据封装
 * </pre>
 */

public class Entry<T> {
    /**
     * 数据实体
     */
    private T data;
    /**
     * 视图策略
     */
    private Strategy<T> strategy;

    public Entry(T data, Strategy<T> strategy) {
        this.data = data;
        this.strategy = strategy;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Strategy<T> getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy<T> strategy) {
        this.strategy = strategy;
    }
}
