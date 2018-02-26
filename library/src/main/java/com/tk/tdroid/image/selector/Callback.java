package com.tk.tdroid.image.selector;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/29
 *     desc   : 图片选择回调
 * </pre>
 */
public interface Callback {
    void onSelect(@NonNull File file);
}
