package com.tk.tdroid.rx.lifecycle;

import android.support.annotation.NonNull;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.BiFunction;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/17
 *      desc : 调度转换器
 * </pre>
 */

public class ExecuteTransformer<T> implements ObservableTransformer<T, T>, FlowableTransformer<T, T>,
        SingleTransformer<T, T>, MaybeTransformer<T, T> {
    private final Observable<?> observable;

    public ExecuteTransformer(@NonNull Observable<?> observable) {
        this.observable = observable;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.zipWith(observable, new BiFunction<T, Object, T>() {
            @Override
            public T apply(T t, Object o) throws Exception {
                return t;
            }
        });
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.zipWith(observable.toFlowable(BackpressureStrategy.LATEST), new BiFunction<T, Object, T>() {
            @Override
            public T apply(T t, Object o) throws Exception {
                return t;
            }
        });
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return upstream.zipWith(observable.firstOrError(), new BiFunction<T, Object, T>() {
            @Override
            public T apply(T t, Object o) throws Exception {
                return t;
            }
        });
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream.zipWith(observable.firstElement(), new BiFunction<T, Object, T>() {
            @Override
            public T apply(T t, Object o) throws Exception {
                return t;
            }
        });
    }
}
