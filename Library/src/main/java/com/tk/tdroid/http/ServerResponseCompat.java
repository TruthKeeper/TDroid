package com.tk.tdroid.http;

import okhttp3.Response;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/12/27
 *     desc   :
 * </pre>
 */
public class ServerResponseCompat<T> extends ServerResponse<T> {
    private Response httpResponse;

    public Response getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(Response httpResponse) {
        this.httpResponse = httpResponse;
    }
}
