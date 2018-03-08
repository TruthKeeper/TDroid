package com.tk.tdroid.rx;

import org.reactivestreams.Publisher;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/11/3
 *     desc   : 异步访问
 * </pre>
 */
public class AsyncCall<T> implements ObservableTransformer<T, T>, SingleTransformer<T, T>, FlowableTransformer<T, T>
        , MaybeTransformer<T, T>, CompletableTransformer {
    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
