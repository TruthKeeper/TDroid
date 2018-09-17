package com.tk.tdroid.rx;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tk.tdroid.utils.SoftKeyboardUtils;

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
        return click(view, true);
    }

    /**
     * 点击事件监听
     *
     * @param view
     * @param disposable 是否可被中断
     * @return
     */
    public static Observable<Object> click(@NonNull final View view, final boolean disposable) {
        return new ViewClickObservable(view, disposable);
    }

    /**
     * 长按事件监听
     *
     * @param view
     * @return
     */
    public static Observable<Object> longClick(@NonNull final View view) {
        return longClick(view, true);
    }

    /**
     * 长按事件监听
     *
     * @param view
     * @param disposable 是否可被中断
     * @return
     */
    public static Observable<Object> longClick(@NonNull final View view, final boolean disposable) {
        return new ViewLongClickObservable(view, disposable);
    }

    /**
     * 文本变化监听
     *
     * @param editText
     * @return
     */
    public static Observable<Object> onTextChange(@NonNull final EditText editText) {
        return onTextChange(editText, true);
    }

    /**
     * 文本变化监听
     *
     * @param editText
     * @param disposable 是否可被中断
     * @return
     */
    public static Observable<Object> onTextChange(@NonNull final EditText editText, final boolean disposable) {
        return new TextChangeObservable(editText, disposable);
    }

    /**
     * 联想搜索
     *
     * @param editText
     * @return
     */
    public static Observable<String> editSearch(@NonNull final EditText editText) {
        return editSearch(editText, true);
    }

    /**
     * 联想搜索
     *
     * @param editText
     * @param disposable 是否可被中断
     * @return
     */
    public static Observable<String> editSearch(@NonNull final EditText editText, final boolean disposable) {
        return new EditSearchObservable(editText, disposable)
                .debounce(500, TimeUnit.MILLISECONDS);
    }

    /**
     * 连击
     *
     * @param view
     * @param maxIntervalMilli 最大的连击间隔 单位:毫秒
     * @return
     */
    public static Observable<Integer> comboClick(@NonNull final View view, final long maxIntervalMilli) {
        return comboClick(new ViewClickObservable(view, true), maxIntervalMilli);
    }

    /**
     * 连击
     *
     * @param view
     * @param maxIntervalMilli 最大的连击间隔 单位:毫秒
     * @param disposable       是否可被中断
     * @return
     */
    public static Observable<Integer> comboClick(@NonNull final View view, final long maxIntervalMilli, final boolean disposable) {
        return comboClick(new ViewClickObservable(view, disposable), maxIntervalMilli);
    }

    /**
     * 连击
     *
     * @param observable
     * @param maxIntervalMilli 最大的连击间隔 单位:毫秒
     * @return
     */
    public static <T> Observable<Integer> comboClick(@NonNull final Observable<T> observable, final long maxIntervalMilli) {
        return observable.map(new Function<Object, Integer>() {
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
        private final boolean disposable;

        ViewClickObservable(@NonNull View view, boolean disposable) {
            this.view = view;
            this.disposable = disposable;
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
                    if (disposable) {
                        view.setOnClickListener(null);
                    }
                }
            });

        }
    }

    private static final class ViewLongClickObservable extends Observable<Object> {
        private final View view;
        private final boolean disposable;

        ViewLongClickObservable(@NonNull View view, boolean disposable) {
            this.view = view;
            this.disposable = disposable;
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
                    if (disposable) {
                        view.setOnLongClickListener(null);
                    }
                }
            });
        }
    }

    private static final class TextChangeObservable extends Observable<Object> {
        private final EditText editText;
        private final boolean disposable;

        TextChangeObservable(@NonNull EditText editText, boolean disposable) {
            this.editText = editText;
            this.disposable = disposable;
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
                    if (disposable) {
                        editText.removeTextChangedListener(watcher);
                        watcher = null;
                    }
                }

            });
        }
    }

    private static final class EditSearchObservable extends Observable<String> {
        private final EditText editText;
        private final boolean disposable;

        EditSearchObservable(@NonNull EditText editText, boolean disposable) {
            this.editText = editText;
            this.disposable = disposable;
        }

        @Override
        protected void subscribeActual(final Observer<? super String> observer) {
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
                            observer.onNext(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
                private TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            editText.clearFocus();
                            SoftKeyboardUtils.hideSoftKeyboard(editText, true);
                            if (!isDisposed()) {
                                observer.onNext(editText.getText().toString());
                            }
                        }
                        return false;
                    }
                };

                {
                    editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    editText.addTextChangedListener(watcher);
                    editText.setOnEditorActionListener(actionListener);
                }

                @Override
                protected void onDispose() {
                    if (disposable) {
                        editText.removeTextChangedListener(watcher);
                        editText.setOnEditorActionListener(null);
                        watcher = null;
                        actionListener = null;
                    }
                }
            });
        }
    }
}
