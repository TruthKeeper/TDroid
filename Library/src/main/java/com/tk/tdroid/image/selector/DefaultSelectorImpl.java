package com.tk.tdroid.image.selector;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/7/26
 *     desc   : 系统的实现
 * </pre>
 */
public class DefaultSelectorImpl implements ISelector {
    private static final String TAG = "DefaultSelectorImpl";
    private DefaultSelectFragment fragment;

    public DefaultSelectorImpl(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        Fragment byTag = manager.findFragmentByTag(TAG);
        if (byTag == null) {
            fragment = new DefaultSelectFragment();
            manager.beginTransaction()
                    .add(fragment, TAG)
                    .commitNowAllowingStateLoss();
        } else {
            fragment = (DefaultSelectFragment) byTag;
        }
    }

    /**
     * 开启拍照
     *
     * @param requestCode
     * @param crop
     * @param singleCallback
     */
    @Override
    public void startCamera(int requestCode, boolean crop, @NonNull SingleCallback singleCallback) {
        fragment.startCamera(requestCode, crop, singleCallback);
    }

    /**
     * 开启相册单选
     *
     * @param requestCode
     * @param crop
     * @param singleCallback
     */
    @Override
    public void startSingleAlbum(int requestCode, boolean crop, @NonNull SingleCallback singleCallback) {
        fragment.startSingleAlbum(requestCode, crop, singleCallback);
    }

    /**
     * 开启多选相册
     *
     * @param requestCode
     * @param selectCount
     * @param multiCallback
     */
    @Override
    public void startMultiAlbum(int requestCode, int selectCount, @NonNull MultiCallback multiCallback) {
        fragment.startMultiAlbum(requestCode, selectCount, multiCallback);
    }
}
