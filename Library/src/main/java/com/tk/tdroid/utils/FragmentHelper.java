package com.tk.tdroid.utils;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;

import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *     author : amin
 *     time   : 2019/2/15
 *     desc   :
 * </pre>
 */
public class FragmentHelper {
    /**
     * 标记前缀
     */
    private static final String KEY_PREFIX = "FragmentHelper_";
    /**
     * 依附的布局Id
     */
    @IdRes
    private int containerId;
    /**
     * 放置的Fragment数据
     */
    private Map<String, FragmentData> fragmentMap = null;
    /**
     * 管理器
     */
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
                                        @NonNull FragmentHelper.FragmentData... fragmentData) {
        return new FragmentHelper(manager, containerId, savedInstanceState, fragmentData);
    }

    private FragmentHelper(@NonNull FragmentManager manager,
                           @IdRes int containerId,
                           @Nullable Bundle savedInstanceState,
                           @NonNull FragmentHelper.FragmentData... fragmentData) {
        this.manager = manager;
        this.containerId = containerId;
        fragmentMap = new ArrayMap<>();
        for (FragmentData data : fragmentData) {
            //从内存中获取保存的实例
            if (savedInstanceState != null) {
                Fragment fragment = manager.getFragment(savedInstanceState, KEY_PREFIX + data.sign);
                if (fragment != null) {
                    data.fragment = fragment;
                    fragmentMap.put(data.sign, data);
                    continue;
                }
            }
            fragmentMap.put(data.sign, data);
        }
    }

    /**
     * 保存实例
     *
     * @param outState
     */
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Set<Map.Entry<String, FragmentData>> entrySet = fragmentMap.entrySet();
        for (Map.Entry<String, FragmentData> entry : entrySet) {
            if (entry.getValue() != null && entry.getValue().fragment.isAdded()) {
                manager.putFragment(outState, KEY_PREFIX + entry.getValue().sign, entry.getValue().fragment);
            }
        }
    }

    /**
     * 根据签名获取Fragment
     *
     * @param sign
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends Fragment> T getFragmentBySign(@NonNull String sign) {
        if (EmptyUtils.isEmpty(sign)) {
            return null;
        }
        FragmentData data = fragmentMap.get(sign);
        if (data != null) {
            return (T) data.fragment;
        }
        return null;
    }

    /**
     * 切换
     *
     * @param sign
     */
    public void switchFragment(@NonNull String sign) {
        FragmentData data = fragmentMap.get(sign);
        if (data == null) {
            return;
        }
        if (instanceFragment(data)) {
            boolean isAdded = data.fragment.isAdded();
            boolean isHidden = data.fragment.isHidden();
            if (isAdded && !isHidden) {
                return;
            }
            FragmentTransaction ft = manager.beginTransaction();
            hideOther(ft, sign);
            if (isAdded) {
                ft.show(data.fragment);
            } else {
                ft.add(containerId, data.fragment, data.tag);
            }
            ft.commit();
        }
    }

    /**
     * 拼接显示
     *
     * @param data
     * @param switchThis
     */
    public void appendFragment(@NonNull FragmentData data, boolean switchThis) {
        if (fragmentMap.get(data.sign) != null) {
            return;
        }
        fragmentMap.put(data.sign, data);
        if (switchThis) {
            switchFragment(data.sign);
        }

    }

    /**
     * 隐藏其他的Fragment
     *
     * @param ft
     * @param sign
     */
    private void hideOther(FragmentTransaction ft, @NonNull String sign) {
        Set<Map.Entry<String, FragmentData>> entrySet = fragmentMap.entrySet();
        for (Map.Entry<String, FragmentData> entry : entrySet) {
            if (!entry.getKey().equals(sign)
                    && entry.getValue().fragment != null
                    && entry.getValue().fragment.isAdded()
                    && !entry.getValue().fragment.isHidden()) {
                ft.hide(entry.getValue().fragment);
            }
        }
    }

    /**
     * 实例化Fragment
     *
     * @param data
     * @return
     */
    private boolean instanceFragment(FragmentData data) {
        if (data.fragment != null) {
            return true;
        }
        try {
            data.fragment = data.factory.create(data.sign);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static class FragmentData {
        private String sign;
        private Fragment fragment;
        private FragmentFactory factory;
        private String tag;

        private FragmentData() {
        }

        public static FragmentHelper.FragmentData create(@NonNull String sign, @NonNull FragmentFactory factory) {
            return create(sign, factory, null);
        }

        public static FragmentHelper.FragmentData create(@NonNull String sign, @NonNull FragmentFactory factory, @Nullable String tag) {
            FragmentHelper.FragmentData fragmentData = new FragmentHelper.FragmentData();
            fragmentData.sign = sign;
            fragmentData.factory = factory;
            fragmentData.tag = tag;
            return fragmentData;
        }

    }

    public interface FragmentFactory {
        Fragment create(String sign);
    }

}

