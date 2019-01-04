package com.tk.tdroid.http;


import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/12/27
 *     desc   : Call的执行完毕后将结果封装到返回体里
 * </pre>
 */
class CallWrapper<T> implements Call<T> {
    private final Call<T> mRealCall;

    CallWrapper(Call<T> call) {
        this.mRealCall = call;
    }

    @Override
    public Response<T> execute() throws IOException {
        Response<T> response = mRealCall.execute();
        T body = response.body();
        if (body instanceof ServerResponseCompat) {
            ((ServerResponseCompat) body).setHttpResponse(response.raw());
        }
        return response;
    }

    @Override
    public void enqueue(Callback<T> callback) {
        mRealCall.enqueue(callback);
    }

    @Override
    public boolean isExecuted() {
        return mRealCall.isExecuted();
    }

    @Override
    public void cancel() {
        mRealCall.cancel();
    }

    @Override
    public boolean isCanceled() {
        return mRealCall.isCanceled();
    }

    @Override
    public Call<T> clone() {
        return new CallWrapper<>(mRealCall.clone());
    }

    @Override
    public Request request() {
        return mRealCall.request();
    }
}
