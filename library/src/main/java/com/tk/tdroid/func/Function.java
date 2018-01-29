package com.tk.tdroid.func;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/29
 *     desc   : 通信执行单元
 * </pre>
 */
public interface Function<Param, Result> {
    Result call(Param params);
}
