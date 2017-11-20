package com.tk.tdroid.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
 *          <li>监听Rx生命周期</li>
 *          <li>回调事件接收</li>
 *      </ol>
 * </pre>
 */

public class RxActivity extends AppCompatActivity implements ILifecycleProvider {
    private Subject<ActivityLifecycleImpl> lifecycleSubject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityLifecycleImpl.ON_CREATE);
        EventHelper.register(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        lifecycleSubject.onNext(ActivityLifecycleImpl.PRE_INFLATE);
        super.setContentView(layoutResID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityLifecycleImpl.ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityLifecycleImpl.ON_RESUME);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        lifecycleSubject.onNext(ActivityLifecycleImpl.ON_RESTART);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecycleSubject.onNext(ActivityLifecycleImpl.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifecycleSubject.onNext(ActivityLifecycleImpl.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(ActivityLifecycleImpl.ON_DESTROY);
        EventHelper.unregister(this);
    }

    /**
     * 绑定生命周期
     *
     * @param lifecycle
     * @return
     */
    @Override
    public <T> LifecycleTransformer<T> bindLifecycle(@NonNull ILifecycle lifecycle) {
        return RxUtils.bindLifecycle(lifecycleSubject, lifecycle);
    }

    /**
     * 绑定生命周期_销毁
     *
     * @return
     */
    @Override
    public <T> LifecycleTransformer<T> bindOnDestroy() {
        return RxUtils.bindLifecycle(lifecycleSubject, ActivityLifecycleImpl.ON_DESTROY);
    }

    /**
     * 在指定生命周期或之后执行
     *
     * @param event
     */
    @Override
    public <T> ExecuteTransformer<T> executeWhen(@NonNull ILifecycle event) {
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
}
