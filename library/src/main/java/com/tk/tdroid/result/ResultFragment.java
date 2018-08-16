package com.tk.tdroid.result;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/18
 *     desc   : xxxx描述
 * </pre>
 */
public class ResultFragment extends Fragment {
    private final Map<Integer, Callback> resultCallbacks = new ArrayMap<>();
    private final Map<Integer, Subject<ResultInfo>> resultRxCallbacks = new ArrayMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    void startForResult(Intent intent, int requestCode, Callback callback) {
        resultCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
    }

    void startForResult(Class<?> actCls, int requestCode, Callback callback) {
        resultCallbacks.put(requestCode, callback);
        startActivityForResult(new Intent(getContext(), actCls), requestCode);
    }

    Observable<ResultInfo> startForResult(final Intent intent, final int requestCode) {
        final Subject<ResultInfo> observable = PublishSubject.create();
        return observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                resultRxCallbacks.put(requestCode, observable);
                startActivityForResult(intent, requestCode);
            }
        });
    }

    Observable<ResultInfo> startForResult(final Class<?> actCls, final int requestCode) {
        final Subject<ResultInfo> observable = PublishSubject.create();
        return observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                resultRxCallbacks.put(requestCode, observable);
                startActivityForResult(new Intent(getContext(), actCls), requestCode);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Callback callback = resultCallbacks.remove(requestCode);
        if (callback != null) {
            callback.onActivityResult(new ResultInfo(requestCode, resultCode, data));
        }
        Subject<ResultInfo> observable = resultRxCallbacks.remove(requestCode);
        if (observable != null) {
            observable.onNext(new ResultInfo(requestCode, resultCode, data));
            observable.onComplete();
        }
    }
}
