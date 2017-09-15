package com.tk.tdroid.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/9/13
 *     desc   : 值转换工具
 * </pre>
 */

public class FormatUtils {
    private static final SimpleDateFormat exactDate = new SimpleDateFormat("yyyy-MM-dd\tHH:mm:ss", Locale.getDefault());

    /**
     * 解析详细日期
     *
     * @param millisecond
     * @return
     */
    public static String formatExactDate(long millisecond) {
        return exactDate.format(new Date(millisecond));
    }

}
