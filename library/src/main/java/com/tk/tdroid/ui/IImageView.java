package com.tk.tdroid.ui;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * <pre>
 *      author : TK
 *      time : 2018/1/10
 *      desc :
 * </pre>
 */

public interface IImageView extends IView {
    void updateSrc(@NonNull Drawable src);
}
