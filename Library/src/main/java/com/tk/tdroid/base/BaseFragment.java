package com.tk.tdroid.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tdroid.annotation.AutoInject;
import com.tdroid.annotation.SaveAndRestore;
import com.tk.tdroid.autoinject.AutoInjectHelper;
import com.tk.tdroid.event.Event;
import com.tk.tdroid.event.EventHelper;
import com.tk.tdroid.rx.RxUtils;
import com.tk.tdroid.rx.lifecycle.ExecuteTransformer;
import com.tk.tdroid.rx.lifecycle.FragmentLifecycleImpl;
import com.tk.tdroid.rx.lifecycle.ILifecycle;
import com.tk.tdroid.rx.lifecycle.ILifecycleProvider;
import com.tk.tdroid.rx.lifecycle.LifecycleTransformer;
import com.tk.tdroid.saverestore.SaveRestoreHelper;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc : <ol>Fragment基类
 *          <li>绑定Rx生命周期{@link BaseFragment#bindLifecycle(ILifecycle)} , {@link BaseFragment#bindOnDestroy()} , {@link BaseFragment#executeWhen(ILifecycle)}</li>
 *          <li>EventBus事件接收{@link BaseFragment#onEventReceived(Event)}</li>
 *      </ol>
 * </pre>
 */

public abstract class BaseFragment extends Fragment implements ILifecycleProvider, IFragmentProvider {
    private Subject<FragmentLifecycleImpl> lifecycleSubject = null;

    private final boolean bindLifecycleEnabled;
    private final boolean eventBusEnabled;
    private final boolean visibleObserverEnabled;
    private final boolean saveAndRestoreData;
    private final boolean autoInjectData;

    private boolean hasCreated;

    private View rootView;

    {
        bindLifecycleEnabled = bindLifecycleEnabled();
        eventBusEnabled = eventBusEnabled();
        visibleObserverEnabled = visibleObserverEnabled();
        if (bindLifecycleEnabled) {
            lifecycleSubject = PublishSubject.create();
        }
        saveAndRestoreData = saveAndRestoreData();
        autoInjectData = autoInjectData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!hasCreated) {
            //视图还未被创建
            return;
        }
        if (visibleObserverEnabled) {
            onVisibleChange(isVisibleToUser);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (visibleObserverEnabled) {
            onVisibleChange(!hidden);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onLifecycleNext(FragmentLifecycleImpl.ON_ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        onLifecycleNext(FragmentLifecycleImpl.ON_CREATE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (saveAndRestoreData) {
            SaveRestoreHelper.onSaveInstanceState(this, outState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (autoInjectData) {
            AutoInjectHelper.inject(this);
        }
        if (saveAndRestoreData) {
            SaveRestoreHelper.onRestoreInstanceState(this, savedInstanceState);
        }
        if (eventBusEnabled) {
            EventHelper.register(this);
        }
        onLifecycleNext(FragmentLifecycleImpl.PRE_INFLATE);
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    protected abstract int getLayoutId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onLifecycleNext(FragmentLifecycleImpl.ON_VIEW_CREATED);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onLifecycleNext(FragmentLifecycleImpl.ON_ACTIVITY_CREATED);
        hasCreated = true;
        if (getUserVisibleHint() && visibleObserverEnabled) {
            onVisibleChange(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        onLifecycleNext(FragmentLifecycleImpl.ON_START);
    }

    @Override
    public void onResume() {
        super.onResume();
        onLifecycleNext(FragmentLifecycleImpl.ON_RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        onLifecycleNext(FragmentLifecycleImpl.ON_PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        onLifecycleNext(FragmentLifecycleImpl.ON_STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (eventBusEnabled) {
            EventHelper.unregister(this);
        }
        onLifecycleNext(FragmentLifecycleImpl.ON_DESTROY_VIEW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onLifecycleNext(FragmentLifecycleImpl.ON_DESTROY);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onLifecycleNext(FragmentLifecycleImpl.ON_DETACH);
    }

    private void onLifecycleNext(FragmentLifecycleImpl lifecycle) {
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
        return RxUtils.bindLifecycle(lifecycleSubject, lifecycle);
    }

    /**
     * 绑定生命周期_销毁
     *
     * @return
     */
    @Override
    public <T> LifecycleTransformer<T> bindOnDestroy() {
        return RxUtils.bindLifecycle(lifecycleSubject, FragmentLifecycleImpl.ON_DESTROY_VIEW);
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

    /**
     * 用于{@link ViewPager}场景下的懒加载 , 重写
     * <ul>
     * <li>{@link ViewPager}场景下的懒加载</li>
     * <li>{@link FragmentTransaction#show(Fragment)} 和 {@link FragmentTransaction#hide(Fragment)} 的回调</li>
     * </ul>
     *
     * @param isVisible <br>{@code true} : 不可见 -> 可见<br>{@code false} : 可见 -> 不可见
     */
    public void onVisibleChange(boolean isVisible) {
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

    /**
     * 是否支持观察页面可见性变化 , 用于{@link ViewPager}场景下的懒加载 , 重写{@link BaseFragment#onVisibleChange(boolean)}
     *
     * @return
     */
    @Override
    public boolean visibleObserverEnabled() {
        return true;
    }

    /**
     * 是否自动恢复数据 {@link SaveAndRestore}修饰
     *
     * @return
     */
    @Override
    public boolean saveAndRestoreData() {
        return false;
    }

    /**
     * 是否自动读取携带数据 , 用{@link AutoInject}接收
     *
     * @return
     */
    @Override
    public boolean autoInjectData() {
        return false;
    }
}
