package com.tk.tdroid.viewloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/15
 *      desc : 不推荐在xml中配置
 * </pre>
 */

public class LoaderViewContainer extends FrameLayout {

    public LoaderViewContainer(@NonNull Context context) {
        this(context, null);
    }

    public LoaderViewContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoaderViewContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
