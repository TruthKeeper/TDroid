package com.tk.tdroid.http;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/12/27
 *     desc   : 支持包裹Call
 * </pre>
 */
public class CallWrapperFactory extends CallAdapter.Factory {
    public static CallWrapperFactory create() {
        return new CallWrapperFactory();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        final CallAdapter<Object, Object> adapter = (CallAdapter<Object, Object>) retrofit.nextCallAdapter(this, returnType, annotations);
        return new CallAdapter<Object, Object>() {
            @Override
            public Type responseType() {
                return adapter.responseType();
            }

            @Override
            public Object adapt(Call<Object> call) {
                return adapter.adapt(new CallWrapper<>(call));
            }
        };
    }
}
