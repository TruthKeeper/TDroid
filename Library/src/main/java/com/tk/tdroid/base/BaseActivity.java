package com.tk.tdroid.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.tk.tdroid.rx.RxUtils;
import com.tk.tdroid.rx.lifecycle.ActivityLifecycleImpl;
import com.tk.tdroid.rx.lifecycle.ExecuteTransformer;
import com.tk.tdroid.rx.lifecycle.ILifecycle;
import com.tk.tdroid.rx.lifecycle.ILifecycleProvider;
import com.tk.tdroid.rx.lifecycle.LifecycleTransformer;
import com.tk.tdroid.widget.event.Event;
import com.tk.tdroid.widget.event.EventHelper;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc : <ol>Activity基类
 *          <li>绑定Rx生命周期{@link BaseActivity#bindLifecycle(ILifecycle)} , {@link BaseActivity#bindOnDestroy()} , {@link BaseActivity#executeWhen(ILifecycle)}</li>
 *          <li>EventBus事件接收{@link BaseActivity#onEventReceived(Event)}</li>
 *      </ol>
 * </pre>
 */

public class BaseActivity extends AppCompatActivity implements ILifecycleProvider, IActivityProvider {
    private Subject<ActivityLifecycleImpl> lifecycleSubject = null;

    private boolean bindLifecycleEnabled;
    private boolean eventBusEnabled;

    static {
        //SVG <Vector>的支持
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    {
        bindLifecycleEnabled = bindLifecycleEnabled();
        eventBusEnabled = eventBusEnabled();
        if (bindLifecycleEnabled) {
            lifecycleSubject = PublishSubject.create();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onLifecycleNext(ActivityLifecycleImpl.ON_CREATE);
        if (eventBusEnabled) {
            EventHelper.register(this);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        onLifecycleNext(ActivityLifecycleImpl.PRE_INFLATE);
        super.setContentView(layoutResID);
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
        if (eventBusEnabled) {
            EventHelper.unregister(this);
        }
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
     * EventBus事件接收
     * <br>
     * {@code Observable.just(event)}来转换成RxJava事件
     *
     * @param event
     */
    @Subscribe(sticky = true)
    public void onEventReceived(Event<?> event) {
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
     * 是否支持EventBus事件监听
     *
     * @return
     */
    @Override
    public boolean eventBusEnabled() {
        return true;
    }
}