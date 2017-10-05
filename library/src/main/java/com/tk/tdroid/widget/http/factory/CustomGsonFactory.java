package com.tk.tdroid.widget.http.factory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/05
 *     desc   : xxxx描述
 * </pre>
 */
public class CustomGsonFactory extends Converter.Factory {
    private final Gson gson;

    public static CustomGsonFactory create() {
        return create(new Gson());
    }

    public static CustomGsonFactory create(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        } else {
            return new CustomGsonFactory(gson);
        }
    }

    private CustomGsonFactory(Gson gson) {
        this.gson = gson;
    }

    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = this.gson.getAdapter(TypeToken.get(type));
        return new CustomResponseConverter<>(this.gson, adapter);
    }

    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = this.gson.getAdapter(TypeToken.get(type));
        return new CustomRequestConverter<>(this.gson, adapter);
    }
}
