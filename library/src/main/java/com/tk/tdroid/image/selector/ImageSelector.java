package com.tk.tdroid.image.selector;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.tk.tdroid.base.Callback;
import com.tk.tdroid.permission.PermissionManager;
import com.tk.tdroid.utils.IntentUtils;
import com.tk.tdroid.utils.Toasty;

import io.reactivex.functions.Consumer;


/**
 * <pre>
 *      author : TK
 *      time : 2018/7/26
 *      desc : 图片(拍照、相册、裁剪)选择器，注意在功能清单中配置
 *      android:configChanges="screenSize|orientation|keyboardHidden"
 *      android:screenOrientation="portrait"
 * </pre>
 */

public final class ImageSelector implements ISelector {
    private final FragmentActivity activity;
    private ISelector iSelector;

    private ImageSelector(FragmentActivity activity) {
        this.activity = activity;
        iSelector = new DefaultSelectorImpl(activity);
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
     * 开启拍照
     *
     * @param requestCode
     * @param crop
     * @param singleCallback
     */
    @Override
    public void startCamera(int requestCode, boolean crop, @NonNull SingleCallback singleCallback) {
        checkCameraPermission(activity, new Callback<Void>() {
            @Override
            public void call(Void aVoid) {
                iSelector.startCamera(requestCode, crop, singleCallback);
            }
        });
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
        iSelector.startSingleAlbum(requestCode, crop, singleCallback);
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
        iSelector.startMultiAlbum(requestCode, selectCount, multiCallback);
    }

    private static void checkCameraPermission(FragmentActivity activity, Callback<Void> callback) {
        PermissionManager.with(activity)
                .request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean success) throws Exception {
                        if (success) {
                            if (null != callback) {
                                callback.call(null);
                            }
                        } else {
                            Toasty.show("缺少拍照权限，请手动开启", null);
                            IntentUtils.toSetting();
                        }
                    }
                });
    }
}