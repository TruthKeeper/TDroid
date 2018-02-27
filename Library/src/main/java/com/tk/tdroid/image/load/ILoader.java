package com.tk.tdroid.image.load;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/12/22
 *     desc   : 图像加载
 * </pre>
 */
public interface ILoader {
    void init(@NonNull Config config);

    void load(@NonNull ImageView imageView, @NonNull ImageRequest request);

    void load(@NonNull LoaderCallback callback, @NonNull ImageRequest request);
}
