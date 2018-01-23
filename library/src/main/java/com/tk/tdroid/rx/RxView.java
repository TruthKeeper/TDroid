package com.tk.tdroid.rx;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Timed;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/12
 *     desc   : RxView
 * </pre>
 */
public final class RxView {
    private RxView() {
        throw new IllegalStateException();
    }

    private static final Object SIGN = new Object();

    /**
     * 点击事件监听
     *
     * @param view
     * @return
     */
    public static Observable<Object> click(@NonNull final View view) {
        return new ViewClickObservable(view);
    }

    /**
     * 长按事件监听
     *
     * @param view
     * @return
     */
    public static Observable<Object> longClick(@NonNull final View view) {
        return new ViewLongClickObservable(view);
    }

    /**
     * 文本变化监听
     *
     * @param editText
     * @return
     */
    public static Observable<Object> onTextChange(@NonNull final EditText editText) {
        return new TextChangeObservable(editText);
    }

    /**
     * 连击
     *
     * @param view
     * @param maxIntervalMilli 最大的连击间隔 单位:毫秒
     * @return
     */
    public static Observable<Integer> comboClick(@NonNull final View view, final long maxIntervalMilli) {
        return new ViewClickObservable(view)
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(Object o) throws Exception {
                        return 1;
                    }
                })
                .timestamp()
                .scan(new BiFunction<Timed<Integer>, Timed<Integer>, Timed<Integer>>() {
                    @Override
                    public Timed<Integer> apply(Timed<Integer> lastT, Timed<Integer> thisT) throws Exception {
                        if (thisT.time() - lastT.time() > maxIntervalMilli) {
                            //连击中断
                            return new Timed<>(1, thisT.time(), TimeUnit.MILLISECONDS);
                        }
                        return new Timed<>(lastT.value() + 1, thisT.time(), TimeUnit.MILLISECONDS);
                    }
                })
                .map(new Function<Timed<Integer>, Integer>() {
                    @Override
                    public Integer apply(Timed<Integer> timed) throws Exception {
                        return timed.value();
                    }
                });
    }

    private static final class ViewClickObservable extends Observable<Object> {
        private final View view;

        ViewClickObservable(@NonNull View view) {
            this.view = view;
        }

        @Override
        protected void subscribeActual(final Observer<? super Object> observer) {
            if (!RxUtils.checkMainThread()) {
                return;
            }
            observer.onSubscribe(new MainThreadDisposable() {
                {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isDisposed()) {
                                observer.onNext(SIGN);
                            }
                        }
                    });
                }

                @Override
                protected void onDispose() {
                    view.setOnClickListener(null);
                }
            });

        }
    }

    private static final class ViewLongClickObservable extends Observable<Object> {
        private final View view;

        ViewLongClickObservable(@NonNull View view) {
            this.view = view;
        }

        @Override
        protected void subscribeActual(final Observer<? super Object> observer) {
            if (!RxUtils.checkMainThread()) {
                return;
            }
            observer.onSubscribe(new MainThreadDisposable() {
                {
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (!isDisposed()) {
                                observer.onNext(SIGN);
                            }
                            return false;
                        }
                    });
                }

                @Override
                protected void onDispose() {
                    view.setOnLongClickListener(null);
                }
            });
        }
    }

    private static final class TextChangeObservable extends Observable<Object> {
        private final EditText editText;

        TextChangeObservable(@NonNull EditText editText) {
            this.editText = editText;
        }

        @Override
        protected void subscribeActual(final Observer<? super Object> observer) {
            if (!RxUtils.checkMainThread()) {
                return;
            }
            observer.onSubscribe(new MainThreadDisposable() {
                private TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!isDisposed()) {
                            observer.onNext(SIGN);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };

                {
                    editText.addTextChangedListener(watcher);
                }

                @Override
                protected void onDispose() {
                    editText.removeTextChangedListener(watcher);
                    watcher = null;
                }

            });
        }
    }
}
