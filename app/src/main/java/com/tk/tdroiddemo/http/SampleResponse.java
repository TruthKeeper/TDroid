package com.tk.tdroiddemo.http;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/11/14
 *     desc   : xxxx描述
 * </pre>
 */
public class SampleResponse<T> {
    private int code;
    private T data;
    private String msg;
    private long serverTime;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }
}
