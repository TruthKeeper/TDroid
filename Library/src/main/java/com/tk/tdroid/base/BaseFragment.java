package com.tk.tdroid.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tdroid.annotation.AutoInject;
import com.tdroid.annotation.SaveAndRestore;
import com.tk.tdroid.autoinject.AutoInjectHelper;
import com.tk.tdroid.rx.RxUtils;
import com.tk.tdroid.rx.lifecycle.ExecuteTransformer;
import com.tk.tdroid.rx.lifecycle.FragmentLifecycleImpl;
import com.tk.tdroid.rx.lifecycle.ILifecycle;
import com.tk.tdroid.rx.lifecycle.ILifecycleProvider;
import com.tk.tdroid.rx.lifecycle.LifecycleTransformer;
import com.tk.tdroid.saverestore.SaveRestoreHelper;
import com.tk.tdroid.utils.EmptyUtils;

import java.util.List;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc : <ol>Fragment基类
 *          <li>绑定Rx生命周期{@link BaseFragment#bindLifecycle(ILifecycle)} , {@link BaseFragment#bindOnDestroy()} , {@link BaseFragment#executeWhen(ILifecycle)}</li>
 *          <li>支持观察页面可见性变化{@link BaseFragment#visibleObserverEnabled()}</li>
 *          <li>自动保存和恢复数据{@link BaseFragment#saveAndRestoreData()}</li>
 *          <li>自动注入携带数据{@link BaseFragment#autoInjectData()}</li>
 *      </ol>
 * </pre>
 */

public abstract class BaseFragment extends Fragment implements ILifecycleProvider, IFragmentProvider {
    private Subject<FragmentLifecycleImpl> lifecycleSubject = null;

    private final boolean bindLifecycleEnabled;
    private final boolean visibleObserverEnabled;
    private final boolean saveAndRestoreData;
    private final boolean autoInjectData;

    /**
     * 是否是第一次显示
     */
    private boolean isFirstVisible = true;
    /**
     * 视图是否创建
     */
    private boolean isViewCreated = false;
    /**
     * 当前是否显示给用户
     */
    private boolean currentVisible = false;

    private View rootView;

    {
        bindLifecycleEnabled = bindLifecycleEnabled();
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
        if (isViewCreated) {
            dispatchVisible(isVisibleToUser);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        dispatchVisible(!hidden);
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (autoInjectData) {
            AutoInjectHelper.inject(this);
        }
        if (saveAndRestoreData) {
            SaveRestoreHelper.onRestoreInstanceState(this, savedInstanceState);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onLifecycleNext(FragmentLifecycleImpl.ON_VIEW_CREATED);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onLifecycleNext(FragmentLifecycleImpl.ON_ACTIVITY_CREATED);
        isViewCreated = true;
        if (!isHidden() && getUserVisibleHint()) {
            dispatchVisible(true);
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
        if (!isFirstVisible
                && !isHidden()
                && getUserVisibleHint()) {
            dispatchVisible(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onLifecycleNext(FragmentLifecycleImpl.ON_PAUSE);
        if (getUserVisibleHint()) {
            dispatchVisible(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        onLifecycleNext(FragmentLifecycleImpl.ON_STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onLifecycleNext(FragmentLifecycleImpl.ON_DESTROY_VIEW);
        isViewCreated = false;
        isFirstVisible = true;
        currentVisible = false;
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
     * 分发
     *
     * @param visible
     */
    private void dispatchVisible(boolean visible) {
        if (!visibleObserverEnabled) {
            //不支持监听则return
            return;
        }
        if (getView() == null) {
            return;
        }
        if (currentVisible == visible) {
            //未变化则return
            return;
        }
        if (visible && isParentInvisible()) {
            //当前是嵌套的子Fragment，父未显示，子显示的情况就return
            return;
        }
        currentVisible = visible;
        if (visible) {
            if (isFirstVisible) {
                isFirstVisible = false;
                onFragmentFirstVisible();
            }
            onFragmentResume();
            dispatchChildVisibleState(true);
        } else {
            onFragmentPause();
            dispatchChildVisibleState(false);
        }
    }

    /**
     * 父Fragment是否不可见
     *
     * @return
     */
    private boolean isParentInvisible() {
        Fragment parent = getParentFragment();
        return parent != null && parent instanceof BaseFragment && !((BaseFragment) parent).isCurrentVisible();
    }

    /**
     * ViewPager嵌套ViewPager时的手动分发
     *
     * @param visible
     */
    private void dispatchChildVisibleState(boolean visible) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (!EmptyUtils.isEmpty(fragments)) {
            for (Fragment child : fragments) {
                if (child instanceof BaseFragment && !child.isHidden() && child.getUserVisibleHint()) {
                    ((BaseFragment) child).dispatchVisible(visible);
                }
            }
        }
    }

    /**
     * 当前是否可见
     *
     * @return
     */
    public boolean isCurrentVisible() {
        return currentVisible;
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
     * Fragment第一次显示
     */
    public void onFragmentFirstVisible() {
    }

    /**
     * Fragment可见了
     */
    public void onFragmentResume() {
    }

    /**
     * Fragment不可见了
     */
    public void onFragmentPause() {
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
     * 是否支持观察页面可见性变化 , 用于{@link ViewPager}等场景下的懒加载 ,
     * 重写
     * <ul>
     * <li>{@link BaseFragment#onFragmentFirstVisible()} </li>
     * <li>{@link BaseFragment#onFragmentResume()} </li>
     * <li>{@link BaseFragment#onFragmentPause()} </li>
     * </ul>
     *
     * @return
     */
    @Override
    public boolean visibleObserverEnabled() {
        return false;
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
