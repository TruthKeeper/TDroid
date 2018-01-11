package com.tk.tdroid.utils;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/30
 *      desc : Fragment切换帮助类 , 通过{@link FragmentHelper#onSaveInstanceState(Bundle)}解决重叠问题
 * </pre>
 */

public class FragmentHelper {
    private static final String SAVE_STATE = "FragmentHelper_";
    @IdRes
    private int containerId;
    private FragmentData[] fragmentData = null;
    private FragmentManager manager = null;

    /**
     * 创建
     *
     * @param manager
     * @param containerId
     * @param savedInstanceState
     * @param fragmentData
     * @return
     */
    public static FragmentHelper create(@NonNull FragmentManager manager,
                                        @IdRes int containerId,
                                        @Nullable Bundle savedInstanceState,
                                        @NonNull FragmentData... fragmentData) {
        return new FragmentHelper(manager, containerId, savedInstanceState, fragmentData);
    }

    private FragmentHelper(@NonNull FragmentManager manager,
                           @IdRes int containerId,
                           @Nullable Bundle savedInstanceState,
                           @NonNull FragmentData... fragmentData) {
        this.manager = manager;
        this.containerId = containerId;
        this.fragmentData = fragmentData;

        if (savedInstanceState != null) {
            //从内存中获取实例
            for (int i = 0; i < fragmentData.length; i++) {
                Fragment fragment = manager.getFragment(savedInstanceState, SAVE_STATE + i);
                if (fragment != null) {
                    try {
                        fragmentData[i].fragment = fragment;
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 保存实例
     *
     * @param outState
     */
    public void onSaveInstanceState(@NonNull Bundle outState) {
        for (int i = 0; i < fragmentData.length; i++) {
            if (fragmentData[i].fragment != null && fragmentData[i].fragment.isAdded()) {
                manager.putFragment(outState, SAVE_STATE + i, fragmentData[i].fragment);
            }
        }
    }

    public void switchFragment(int index) {
        if (index < 0 || index >= fragmentData.length) {
            throw new IllegalArgumentException();
        }

        if (instanceFragment(index)) {
            boolean isAdded = fragmentData[index].fragment.isAdded();
            boolean isHidden = fragmentData[index].fragment.isHidden();
            if (isAdded && !isHidden) {
                return;
            }
            FragmentTransaction ft = manager.beginTransaction();
            hideOther(ft, index);
            if (isAdded) {
                ft.show(fragmentData[index].fragment);
            } else {
                ft.add(containerId, fragmentData[index].fragment, fragmentData[index].tag);
            }
            ft.commit();
        }
    }

    private void hideOther(FragmentTransaction ft, int index) {
        for (int i = 0; i < fragmentData.length; i++) {
            if (i != index
                    && fragmentData[i].fragment != null
                    && fragmentData[i].fragment.isAdded()
                    && !fragmentData[i].fragment.isHidden()) {
                ft.hide(fragmentData[i].fragment);
            }
        }
    }

    /**
     * 实例化:根据fragment、类实例化
     *
     * @param index
     * @return
     */
    private boolean instanceFragment(int index) {
        if (index < 0) {
            return false;
        }
        if (fragmentData[index].fragment != null) {
            return true;
        }
        if (fragmentData[index].cls == null) {
            return false;
        }
        try {
            fragmentData[index].fragment = fragmentData[index].cls.newInstance();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static class FragmentData {
        private Fragment fragment;
        private Class<? extends Fragment> cls;
        private String tag;

        private FragmentData() {
        }

        public static FragmentData create(@NonNull Class<? extends Fragment> cls) {
            return create(cls, null);
        }

        public static FragmentData create(@NonNull Class<? extends Fragment> cls, @Nullable String tag) {
            FragmentData fragmentData = new FragmentData();
            fragmentData.cls = cls;
            fragmentData.tag = tag;
            return fragmentData;
        }

        public static FragmentData create(@NonNull Fragment fragment) {
            return create(fragment, null);
        }

        public static FragmentData create(@NonNull Fragment fragment, @Nullable String tag) {
            FragmentData fragmentData = new FragmentData();
            fragmentData.fragment = fragment;
            fragmentData.tag = tag;
            return fragmentData;
        }
    }
}
