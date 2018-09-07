package com.tk.tdroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.tk.tdroid.R;
import com.tk.tdroid.utils.BitUtils;
import com.tk.tdroid.view.tui.TUIEditText;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.tk.tdroid.view.FilterEditText.Flag.EMOJI;
import static com.tk.tdroid.view.FilterEditText.Flag.NEWLINE;
import static com.tk.tdroid.view.FilterEditText.Flag.SPACE;


/**
 * <pre>
 *      author : TK
 *      time : 2017/11/21
 *      desc : 过滤指定格式的{@link TUIEditText}
 * </pre>
 */

public class FilterEditText extends TUIEditText {
    /**
     * 保留几位小数的过滤器
     */
    public static class KeepDecimal implements TextWatcher {
        private final EditText editText;
        private final int num;

        public KeepDecimal(EditText editText) {
            this(editText, 2);
        }

        public KeepDecimal(EditText editText, int num) {
            this.editText = editText;
            editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
            this.num = num;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String string = s.toString();
            if (string.contains(".")) {
                if (string.length() - 1 - string.indexOf(".") > num) {
                    //去除超过保留位数的
                    string = string.substring(0, string.indexOf(".") + num + 1);

                    editText.setText(string);
                    editText.setSelection(editText.length());
                }
            }
            if (string.startsWith(".")) {
                string = "0" + string;

                editText.setText(string);
                editText.setSelection(2);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

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
        if (BitUtils.containsFlag(this.flags, NEWLINE)) {
            setSingleLine(true);
        }
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
        String str = "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)";
//        String str = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
        Pattern pattern = Pattern.compile(str,
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        return pattern.matcher(source).find();
    }
}
