package com.tk.tdroid.image;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/22
 *      desc : 网络动态改变Url的裁剪操作
 * </pre>
 */

public interface ICrop {
    @NonNull
    String cropPath(@NonNull ImageView imageView, @NonNull String path);
}
