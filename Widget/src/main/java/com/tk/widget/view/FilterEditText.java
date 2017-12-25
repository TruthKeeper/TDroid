package com.tk.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import com.tk.tdroid.utils.BitUtils;
import com.tk.widget.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.tk.widget.view.FilterEditText.Flag.EMOJI;
import static com.tk.widget.view.FilterEditText.Flag.NEWLINE;
import static com.tk.widget.view.FilterEditText.Flag.SPACE;


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
    private InputFilter mFilter;

    public FilterEditText(Context context) {
        super(context);
    }

    public FilterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public FilterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FilterEditText);
        flags = array.getInt(R.styleable.FilterEditText_fet_filter, 0);
        array.recycle();
    }

    @Override
    public void setFilters(InputFilter[] filters) {
        //TextView的构造器会调用此方法
        if (mFilter == null) {
            mFilter = new InputFilter() {
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
            };
        }
        if (filters == null || filters.length == 0) {
            super.setFilters(new InputFilter[]{mFilter});
            return;
        }
        InputFilter[] wrapperFilter = Arrays.copyOf(filters, filters.length + 1);
        //末尾追加一个过滤器
        wrapperFilter[wrapperFilter.length - 1] = mFilter;
        super.setFilters(wrapperFilter);
    }

    /**
     * 过滤特定属性
     *
     * @param flags <ul>
     *              <li>Emoji : {@link Flag#EMOJI}</li>
     *              <li>空格 : {@link Flag#SPACE}</li>
     *              <li>换行 : {@link Flag#NEWLINE}</li>
     *              </ul>
     */

    public void addFilterFlag(@Flag int... flags) {
        this.flags = BitUtils.addFlag(this.flags, flags);
        setSingleLine(BitUtils.containsFlag(this.flags, Flag.NEWLINE));
    }

    /**
     * 移除禁用属性
     *
     * @param flag <ul>
     *             <li>Emoji : {@link Flag#EMOJI}</li>
     *             <li>空格 : {@link Flag#SPACE}</li>
     *             <li>换行 : {@link Flag#NEWLINE}</li>
     *             </ul>
     */
    public void clearFilterFlag(@Flag int flag) {
        this.flags = BitUtils.clearFlag(this.flags, flag);
        setSingleLine(BitUtils.containsFlag(this.flags, Flag.NEWLINE));
    }

    /**
     * 移除所有禁用属性
     */
    public void resetFilterFlag() {
        flags = 0;
        setSingleLine(false);
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
