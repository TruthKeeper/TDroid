package com.tk.tdroid.rx;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
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
 *     desc   : 延迟订阅
 * </pre>
 */
public class DelayCall<T> implements ObservableTransformer<T, T>, SingleTransformer<T, T>, FlowableTransformer<T, T> {
    public static final int DELAY = 1_000;
    private int milliSecond;

    public DelayCall() {
        this.milliSecond = DELAY;
    }

    public DelayCall(int milliSecond) {
        this.milliSecond = milliSecond;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.delay(milliSecond, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return upstream.delay(milliSecond, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.delay(milliSecond, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}

