package com.tk.tdroid.rx;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tk.tdroid.rx.lifecycle.ExecuteTransformer;
import com.tk.tdroid.rx.lifecycle.ILifecycle;
import com.tk.tdroid.rx.lifecycle.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc :
 * </pre>
 */

public final class RxUtils {
    private RxUtils() {
        throw new IllegalStateException();
    }

    /**
     * 绑定生命周期
     *
     * @param subject
     * @param lifecycle
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindLifecycle(@NonNull final Observable<? extends ILifecycle> subject, @NonNull final ILifecycle lifecycle) {
        return new LifecycleTransformer<>(subject.filter(new Predicate<ILifecycle>() {
            @Override
            public boolean test(ILifecycle iLifecycle) throws Exception {
                return iLifecycle.equals(lifecycle);
            }
        }));
    }

    /**
     * 在指定生命周期或之后执行
     *
     * @param subject
     * @param lifecycle
     * @param <T>
     * @return
     */
    public static <T> ExecuteTransformer<T> executeWhen(@NonNull final Observable<? extends ILifecycle> subject, @NonNull final ILifecycle lifecycle) {
        return new ExecuteTransformer<T>(subject.filter(new Predicate<ILifecycle>() {
            @Override
            public boolean test(ILifecycle iLifecycle) throws Exception {
                return iLifecycle.equals(lifecycle);
            }
        }));
    }

    public static boolean checkMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Log.e("checkMainThread", "should be called on the main thread !");
            return false;
        }
        return true;
    }
}
