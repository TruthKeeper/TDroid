package com.tk.tdroid.http;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/12/27
 *     desc   :
 * </pre>
 */
public class ServerResponse<T> {

    /**
     * code : 604
     * data : {}
     * msg : 无权操作:缺少head token
     */

    private String code;
    private T data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

}
