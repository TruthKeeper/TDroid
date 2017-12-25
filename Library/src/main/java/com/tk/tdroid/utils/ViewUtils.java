package com.tk.tdroid.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/1
 *      desc : View工具类
 * </pre>
 */

public final class ViewUtils {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private ViewUtils() {
        throw new IllegalStateException();
    }

    /**
     * 父容器是否为空
     *
     * @param viewGroup
     * @return
     */
    public static boolean isEmpty(@Nullable ViewGroup viewGroup) {
        return viewGroup == null || viewGroup.getChildCount() == 0;
    }

    /**
     * 设置文本、并且将光标置为末尾
     *
     * @param editText
     * @param charSequence
     */
    public static void setText(@NonNull EditText editText, @Nullable CharSequence charSequence) {
        editText.setText(charSequence);
        Selection.setSelection(editText.getText(), editText.length());
    }

    /**
     * 拼接文本、并且将光标置为末尾
     *
     * @param editText
     * @param charSequence
     */
    public static void appendText(@NonNull EditText editText, @Nullable CharSequence charSequence) {
        editText.append(charSequence);
        Selection.setSelection(editText.getText(), editText.length());
    }

    /**
     * 为TextView设置{@link TextView#getMaxLines()} 和 {@link TextUtils.TruncateAt#END} 的兼容处理
     *
     * @param textView
     */
    public static void setMaxLinesEnd(@NonNull final TextView textView) {
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int maxLines = textView.getMaxLines();
                if (textView.getLineCount() > maxLines) {
                    final int lineEndIndex = textView.getLayout().getLineEnd(maxLines - 1);
                    // - 3 显示效果不太好
                    final String text = textView.getText().subSequence(0, lineEndIndex - 1) + "...";
                    textView.setText(text);
                }
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * 动态生成View的Id 兼容{@link View#generateViewId()}
     *
     * @return
     */
    public static int generateId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}