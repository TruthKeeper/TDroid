package com.tk.tdroid.constants;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/05
 *     desc   : xxxx描述
 * </pre>
 */
public final class HttpConstants {

    /**
     * 缓存目录
     */
    public static final String CACHE_DIR = "HTTP_CACHE";
    /**
     * 离线缓存保留时长，1月
     */
    public static final int CACHE_DATE = 30 * 24 * 60 * 60;
    /**
     * 离线缓存大小
     */
    public static final int CACHE_SIZE = 10 * 1024 * 1024;
    /**
     * 10s 网络超时
     */
    public static final int TIME_OUT = 10;
    /**
     * 部分接口1S 延迟
     */
    public static final int HTTP_DELAY = 1_000;
    /**
     * Web端成功回调Code
     */
    public static final int SUCCESS_CODE = 200;
}
