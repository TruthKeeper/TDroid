package com.tk.tdroid.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MotionEvent;
import android.view.View;

import com.tdroid.annotation.AutoInject;
import com.tdroid.annotation.SaveAndRestore;
import com.tk.tdroid.autoinject.AutoInjectHelper;
import com.tk.tdroid.rx.RxUtils;
import com.tk.tdroid.rx.lifecycle.ActivityLifecycleImpl;
import com.tk.tdroid.rx.lifecycle.ExecuteTransformer;
import com.tk.tdroid.rx.lifecycle.ILifecycle;
import com.tk.tdroid.rx.lifecycle.ILifecycleProvider;
import com.tk.tdroid.rx.lifecycle.LifecycleTransformer;
import com.tk.tdroid.saverestore.SaveRestoreHelper;
import com.tk.tdroid.utils.SoftKeyboardUtils;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc : <ol>Activity基类
 *          <li>绑定Rx生命周期{@link BaseActivity#bindLifecycle(ILifecycle)} , {@link BaseActivity#bindOnDestroy()} , {@link BaseActivity#executeWhen(ILifecycle)}</li>
 *          <li>触摸隐藏软键盘{@link BaseActivity#touchHideSoftKeyboard()}</li>
 *          <li>自动保存和恢复数据{@link BaseActivity#saveAndRestoreData()}</li>
 *          <li>自动注入携带数据{@link BaseActivity#autoInjectData()}</li>
 *      </ol>
 * </pre>
 */

public class BaseActivity extends AppCompatActivity implements ILifecycleProvider, IActivityProvider {
    private Subject<ActivityLifecycleImpl> lifecycleSubject = null;

    private final boolean bindLifecycleEnabled;
    private final boolean touchHideSoftKeyboard;
    private final boolean saveAndRestoreData;
    private final boolean autoInjectData;

    static {
        //SVG <Vector>的支持
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    {
        bindLifecycleEnabled = bindLifecycleEnabled();
        if (bindLifecycleEnabled) {
            lifecycleSubject = PublishSubject.create();
        }
        touchHideSoftKeyboard = touchHideSoftKeyboard();
        saveAndRestoreData = saveAndRestoreData();
        autoInjectData = autoInjectData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (autoInjectData) {
            AutoInjectHelper.inject(this);
        }
        onLifecycleNext(ActivityLifecycleImpl.ON_CREATE);
    }

    @Override
    public void setContentView(int layoutResID) {
        onLifecycleNext(ActivityLifecycleImpl.PRE_INFLATE);
        super.setContentView(layoutResID);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (saveAndRestoreData) {
            SaveRestoreHelper.onSaveInstanceState(this, outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (saveAndRestoreData) {
            SaveRestoreHelper.onRestoreInstanceState(this, savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        onLifecycleNext(ActivityLifecycleImpl.ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLifecycleNext(ActivityLifecycleImpl.ON_RESUME);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onLifecycleNext(ActivityLifecycleImpl.ON_RESTART);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onLifecycleNext(ActivityLifecycleImpl.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onLifecycleNext(ActivityLifecycleImpl.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onLifecycleNext(ActivityLifecycleImpl.ON_DESTROY);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchHideSoftKeyboard) {
            SoftKeyboardUtils.delegateDispatchTouchEvent(this, ev, touchHideSoftKeyboardFilterViews());
        }
        return super.dispatchTouchEvent(ev);
    }


    private void onLifecycleNext(ActivityLifecycleImpl lifecycle) {
        if (bindLifecycleEnabled) {
            lifecycleSubject.onNext(lifecycle);
        }
    }

    /**
     * 绑定生命周期
     *
     * @param lifecycle
     * @return
     */
    @Override
    public <T> LifecycleTransformer<T> bindLifecycle(@NonNull ILifecycle lifecycle) {
        if (!bindLifecycleEnabled) {
            throw new IllegalStateException("bindLifecycleEnabled is false !");
        }
        return RxUtils.bindLifecycle(lifecycleSubject, lifecycle);
    }

    /**
     * 绑定生命周期_销毁
     *
     * @return
     */
    @Override
    public <T> LifecycleTransformer<T> bindOnDestroy() {
        if (!bindLifecycleEnabled) {
            throw new IllegalStateException("bindLifecycleEnabled is false !");
        }
        return RxUtils.bindLifecycle(lifecycleSubject, ActivityLifecycleImpl.ON_DESTROY);
    }

    /**
     * 在指定生命周期或之后执行
     *
     * @param event
     */
    @Override
    public <T> ExecuteTransformer<T> executeWhen(@NonNull ILifecycle event) {
        if (!bindLifecycleEnabled) {
            throw new IllegalStateException("bindLifecycleEnabled is false !");
        }
        return RxUtils.executeWhen(lifecycleSubject, event);
    }

    /**
     * 是否支持Rx生命周期
     *
     * @return
     */
    @Override
    public boolean bindLifecycleEnabled() {
        return true;
    }

    /**
     * 是否触摸隐藏软键盘
     *
     * @return
     */
    @Override
    public boolean touchHideSoftKeyboard() {
        return true;
    }

    /**
     * 触摸隐藏软键盘过滤View
     *
     * @return
     */
    protected View[] touchHideSoftKeyboardFilterViews() {
        return null;
    }

    /**
     * 是否自动保存和恢复数据 {@link SaveAndRestore}修饰
     *
     * @return
     */
    @Override
    public boolean saveAndRestoreData() {
        return false;
    }

    /**
     * 是否自动注入携带数据 , 用{@link AutoInject}接收
     *
     * @return
     */
    @Override
    public boolean autoInjectData() {
        return false;
    }
}
