package com.tk.tdroid.recycler.adapter;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/03/1
 *     desc   : xxxx描述
 * </pre>
 */
public interface MultiType<Data> {
    /**
     * 数据对应的视图策略
     *
     * @param data
     * @return
     */
    Strategy<Data> bind(Data data);
}
