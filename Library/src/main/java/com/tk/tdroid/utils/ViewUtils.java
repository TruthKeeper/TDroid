package com.tk.tdroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
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
    @MainThread
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
    @MainThread
    public static void appendText(@NonNull EditText editText, @Nullable CharSequence charSequence) {
        editText.append(charSequence);
        Selection.setSelection(editText.getText(), editText.length());
    }

    /**
     * 为TextView设置{@link TextView#getMaxLines()} 和 {@link TextUtils.TruncateAt#END} 的兼容处理
     *
     * @param textView
     */
    @MainThread
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
    @MainThread
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

    /**
     * 通过View的上下文获取到Activity
     *
     * @param view
     * @return
     */
    @Nullable
    public static Activity getActivity(@Nullable View view) {
        if (view == null) {
            return null;
        }
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            //5.0以下会被修饰为TintContextWrapper
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * 判断按钮是否可点击，通过校验{@link EditText}的内内容是否为空
     *
     * @param btn
     * @param views
     */
    @MainThread
    public static void checkBtnEnabledByEditText(@NonNull View btn, EditText... views) {
        if (!EmptyUtils.isEmpty(views)) {
            for (EditText view : views) {
                if (view.length() == 0) {
                    btn.setEnabled(false);
                    return;
                }
            }
        }
        btn.setEnabled(true);
    }

    /**
     * 判断清除按钮是否显示
     *
     * @param clearBtn
     * @param editText
     */
    @MainThread
    public static void checkClearBtnVisible(@NonNull View clearBtn, @NonNull EditText editText) {
        clearBtn.setVisibility(editText.length() > 0 && editText.hasFocus() ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 优化滚动问题 , 在调用{@link TabLayout#setupWithViewPager(ViewPager)}之前调用
     *
     * @param tabLayout
     */
    @MainThread
    public static void tabLayoutScrollCompat(@Nullable TabLayout tabLayout) {
        if (tabLayout == null) {
            return;
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        try {
            Field field = tabLayout.getClass().getDeclaredField("mPageChangeListener");
            field.setAccessible(true);
            field.set(tabLayout, new HookTabLayoutPageChangeListener(tabLayout));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置{@link EditText}无法弹出软键盘，但不影响其它功能
     *
     * @param editText
     */
    @MainThread
    public static void setEditTextNoSoftInput(@Nullable EditText editText) {
        if (editText == null) {
            return;
        }
        Class cls = editText.getClass();
        while (!cls.equals(TextView.class)) {
            cls = cls.getSuperclass();
            if (cls == null) {
                return;
            }
        }
        try {
            Field editorField = cls.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editor = editorField.get(editText);
            Class editorClass = editor.getClass();
            while (!editorClass.equals(Class.forName("android.widget.Editor"))) {
                editorClass = editorClass.getSuperclass();
                if (editorClass == null) {
                    return;
                }
            }
            Field mShowInput = editorClass.getDeclaredField("mShowSoftInputOnFocus");
            mShowInput.setAccessible(true);
            mShowInput.set(editor, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class HookTabLayoutPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {
        private boolean drag;

        HookTabLayoutPageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                drag = true;
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                drag = false;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (drag) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }
    }

    /**
     * 对{@link EditText}进行空格分隔
     */
    public static class SpaceSplitWatcher implements TextWatcher {
        private final EditText editText;
        private final int each;

        private boolean lock;
        private int lastSpaceCount;

        public SpaceSplitWatcher(@NonNull EditText editText) {
            this(editText, 4);
        }

        public SpaceSplitWatcher(@NonNull EditText editText, int each) {
            this.editText = editText;
            this.each = each;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            lastSpaceCount = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    lastSpaceCount++;
                }
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (lock) {
                lock = false;
                return;
            }
            lock = true;
            //获取光标位置
            int selectionIndex = editText.getSelectionEnd();
            //去除空格
            char[] chars = s.toString().replace(" ", "").toCharArray();
            int spaceCount = 0;
            // 每4个分组,加上空格组合成新的字符串
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                if (i % each == 0 && i != 0) {
                    builder.append(" ");
                    spaceCount++;
                }
                builder.append(chars[i]);
            }
            editText.setText(builder.toString());

            if (spaceCount > lastSpaceCount) {
                selectionIndex += (spaceCount - lastSpaceCount);
            }
            selectionIndex = Math.min(builder.length(), Math.max(0, selectionIndex));
            editText.setSelection(selectionIndex);
        }
    }
}