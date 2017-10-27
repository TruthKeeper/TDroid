package com.tk.tdroid.widget.http.factory;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/05
 *     desc   : xxxx描述
 * </pre>
 */
final class CustomResponseConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomResponseConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    public T convert(@NonNull ResponseBody value) throws IOException {
        JsonReader jsonReader = this.gson.newJsonReader(value.charStream());

        try {
            T t = adapter.read(jsonReader);
            return t;
        } finally {
            value.close();
        }
    }

}

