package com.tk.tdroid.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tk.tdroid.rx.RxUtils;
import com.tk.tdroid.rx.lifecycle.ExecuteTransformer;
import com.tk.tdroid.rx.lifecycle.FragmentLifecycleImpl;
import com.tk.tdroid.rx.lifecycle.ILifecycle;
import com.tk.tdroid.rx.lifecycle.ILifecycleProvider;
import com.tk.tdroid.rx.lifecycle.LifecycleTransformer;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/16
 *      desc : 监听生命周期的Fragment基类
 * </pre>
 */

public class LifecycleFragment extends Fragment implements ILifecycleProvider {
    private Subject<FragmentLifecycleImpl> lifecycleSubject = PublishSubject.create();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifecycleSubject.onNext(FragmentLifecycleImpl.ON_ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentLifecycleImpl.ON_CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(FragmentLifecycleImpl.PRE_INFLATE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentLifecycleImpl.ON_VIEW_CREATED);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lifecycleSubject.onNext(FragmentLifecycleImpl.ON_ACTIVITY_CREATED);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentLifecycleImpl.ON_START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentLifecycleImpl.ON_RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        lifecycleSubject.onNext(FragmentLifecycleImpl.ON_PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleSubject.onNext(FragmentLifecycleImpl.ON_STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifecycleSubject.onNext(FragmentLifecycleImpl.ON_DESTROY_VIEW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(FragmentLifecycleImpl.ON_DESTROY);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifecycleSubject.onNext(FragmentLifecycleImpl.ON_DETACH);
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
}
