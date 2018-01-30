package com.tk.tdroid.image.selector;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


/**
 * <pre>
 *      author : TK
 *      time : 2018/1/30
 *      desc : 图片(拍照、相册、裁剪)选择器，注意在功能清单中配置
 *      android:configChanges="screenSize|orientation|keyboardHidden"
 *      android:screenOrientation="portrait"
 * </pre>
 */

public final class ImageSelector {
    private static final String TAG = "ImageSelector";
    private ImageSelectFragment fragment;
    private static final int DEFAULT_REQUEST_CAMERA = 10000;
    private static final int DEFAULT_REQUEST_ALBUM = 20000;

    private ImageSelector(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        Fragment byTag = manager.findFragmentByTag(TAG);
        if (byTag == null) {
            fragment = new ImageSelectFragment();
            manager.beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            manager.executePendingTransactions();
        } else {
            fragment = (ImageSelectFragment) byTag;
        }
    }

    /**
     * Get
     *
     * @param activity
     * @return
     */
    public static ImageSelector with(@NonNull FragmentActivity activity) {
        return new ImageSelector(activity);
    }

    /**
     * 调用相机拍照
     *
     * @param callback
     */
    public void startCamera(@NonNull Callback callback) {
        fragment.startCamera(DEFAULT_REQUEST_CAMERA, false, callback);
    }

    /**
     * 调用相机拍照
     *
     * @param crop     是否裁剪
     * @param callback
     */
    public void startCamera(boolean crop, @NonNull Callback callback) {
        fragment.startCamera(DEFAULT_REQUEST_CAMERA, crop, callback);
    }

    /**
     * 调用相机拍照
     *
     * @param requestCode
     * @param crop        是否裁剪
     * @param callback
     */
    public void startCamera(int requestCode, boolean crop, @NonNull Callback callback) {
        fragment.startCamera(requestCode, crop, callback);
    }

    /**
     * 本地相册选择
     *
     * @param callback
     */
    public void startAlbum(@NonNull Callback callback) {
        fragment.startAlbum(DEFAULT_REQUEST_ALBUM, false, callback);
    }

    /**
     * 本地相册选择
     *
     * @param crop     是否裁剪
     * @param callback
     */
    public void startAlbum(boolean crop, @NonNull Callback callback) {
        fragment.startAlbum(DEFAULT_REQUEST_ALBUM, crop, callback);
    }

    /**
     * 本地相册选择
     *
     * @param requestCode
     * @param crop        是否裁剪
     * @param callback
     */
    public void startAlbum(int requestCode, boolean crop, @NonNull Callback callback) {
        fragment.startAlbum(requestCode, crop, callback);
    }
}