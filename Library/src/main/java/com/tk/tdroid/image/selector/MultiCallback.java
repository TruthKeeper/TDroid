package com.tk.tdroid.image.selector;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/07/26
 *     desc   : 多选
 * </pre>
 */
public interface MultiCallback {
    void onSelect(@NonNull List<File> fileList);
}
