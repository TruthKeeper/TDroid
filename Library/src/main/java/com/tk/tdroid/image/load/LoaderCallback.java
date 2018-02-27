package com.tk.tdroid.image.load;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/12/22
 *     desc   : Bitmap加载回调
 * </pre>
 */
public interface LoaderCallback {
    /**
     * 加载成功的回调
     *
     * @param bitmap
     */
    void onSuccess(@NonNull Bitmap bitmap);
}
