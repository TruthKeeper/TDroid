package com.tk.tdroid.utils;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

/**
 * <pre>
 *     author : DELL
 *     time   : 2019/3/11 16:29
 *     desc   : 字体工具
 * </pre>
 */
public class FontUtils {
    private FontUtils() {
        throw new IllegalStateException();
    }

    /**
     * 获取Text单行的高度
     *
     * @param context
     * @param textSp
     * @return
     */
    public static float getTextHeight(Context context, int textSp) {
        TextView textView = new TextView(context);
        textView.setTextSize(textSp);
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(spec, spec);
        return textView.getMeasuredHeight();
    }

}
