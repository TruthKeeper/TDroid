package com.tk.tdroid.rx.lifecycle;

import android.support.annotation.NonNull;

import org.reactivestreams.Publisher;

import java.util.concurrent.CancellationException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
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
import io.reactivex.functions.Function;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc : 生命周期转换
 * </pre>
 */

public class LifecycleTransformer<T> implements ObservableTransformer<T, T>, FlowableTransformer<T, T>,
        SingleTransformer<T, T>, MaybeTransformer<T, T>, CompletableTransformer {
    private final Observable<?> observable;

    public LifecycleTransformer(@NonNull Observable<?> observable) {
        this.observable = observable;
    }

    @Override
    public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
        return upstream.takeUntil(observable);
    }

    @Override
    public Publisher<T> apply(@NonNull Flowable<T> upstream) {
        return upstream.takeUntil(observable.toFlowable(BackpressureStrategy.LATEST));
    }

    @Override
    public SingleSource<T> apply(@NonNull Single<T> upstream) {
        return upstream.takeUntil(observable.firstOrError());
    }

    @Override
    public MaybeSource<T> apply(@NonNull Maybe<T> upstream) {
        return upstream.takeUntil(observable.firstElement());
    }

    @Override
    public CompletableSource apply(@NonNull Completable upstream) {
        return Completable.ambArray(upstream, observable.flatMapCompletable(new Function<Object, Completable>() {
            @Override
            public Completable apply(Object ignore) throws Exception {
                return Completable.error(new CancellationException());
            }
        }));
    }
}