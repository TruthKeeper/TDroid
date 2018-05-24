package com.tk.tdroid.view.tui;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/27
 *      desc :
 * </pre>
 */

public interface ITextView extends IView {
    void updateText(@NonNull ColorStateList colorStateList);

    void updateDrawable(@NonNull Drawable[] drawables);
}
