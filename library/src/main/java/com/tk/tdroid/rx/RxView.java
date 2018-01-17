package com.tk.tdroid.rx;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

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
