package com.tk.tdroid.image.selector;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/7/26
 *     desc   :
 * </pre>
 */
public interface ISelector {
    /**
     * 开启拍照
     *
     * @param requestCode
     * @param crop
     * @param singleCallback
     */
    void startCamera(int requestCode, boolean crop, @NonNull SingleCallback singleCallback);

    /**
     * 开启相册单选
     *
     * @param requestCode
     * @param crop
     * @param singleCallback
     */
    void startSingleAlbum(int requestCode, boolean crop, @NonNull SingleCallback singleCallback);

    /**
     * 开启多选相册
     *
     * @param requestCode
     * @param selectCount
     * @param multiCallback
     */
    void startMultiAlbum(int requestCode, @IntRange(from = 1) int selectCount, @NonNull MultiCallback multiCallback);
}
