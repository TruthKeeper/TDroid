package com.tk.tdroid.view;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import com.tk.tdroid.utils.BitUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;

import static com.tk.tdroid.view.FilterEditText.Flag.*;


/**
 * <pre>
 *      author : TK
 *      time : 2017/11/21
 *      desc : 过滤指定格式的{@link AppCompatEditText}
 * </pre>
 */

public class FilterEditText extends AppCompatEditText {
    @IntDef({EMOJI, SPACE, NEWLINE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flag {
        int EMOJI = 1;
        int SPACE = 1 << 1;
        int NEWLINE = 1 << 2;
    }

    private int flags = 0;

    public FilterEditText(Context context) {
        super(context);
    }

    public FilterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (BitUtils.containsFlag(flags, Flag.SPACE) && source.toString().contains(" ")) {
                    return "";
                }
                if (BitUtils.containsFlag(flags, EMOJI) && containsEmoji(source)) {
                    return "";
                }
                return source;
            }
        }});
    }

    /**
     * 禁用特定模式
     *
     * @param flag <ul>
     *             <li>Emoji : {@link Flag#EMOJI}</li>
     *             <li>空格 : {@link Flag#SPACE}</li>
     *             <li>换行 : {@link Flag#NEWLINE}</li>
     *             </ul>
     */
    public void setDisabled(@Flag int... flag) {
        flags = BitUtils.addFlag(flags, flag);
        if (BitUtils.containsFlag(flags, Flag.NEWLINE)) {
            setSingleLine(true);
        }
    }

    /**
     * 是否有emoji表情
     *
     * @param source
     * @return
     */
    private static boolean containsEmoji(CharSequence source) {
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        return pattern.matcher(source).find();
    }
}
