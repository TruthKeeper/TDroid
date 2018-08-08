package com.tk.tdroid.image.selector;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/07/26
 *     desc   : 单选
 * </pre>
 */
public interface SingleCallback {
    void onSelect(@NonNull File file);
}
