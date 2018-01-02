package com.tk.tdroid.utils;

import java.text.DecimalFormat;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/12/25
 *     desc   : 数值转换工具
 * </pre>
 */

public final class NumberUtils {
    private static final DecimalFormat dfOne = new DecimalFormat("0.0");
    private static final DecimalFormat dfTwo = new DecimalFormat("0.00");

    private NumberUtils() {
        throw new IllegalStateException();
    }

    /**
     * 大于等于1k时换算成k单位
     * <ol>
     * <li>1234 -> 1.2k</li>
     * <li>12.345 -> 12.3</li>
     * </ol>
     *
     * @param num
     * @return
     */
    public static String keepK(double num) {
        return keepK(num, true, true);
    }

    /**
     * 大于等于1k时换算成k单位
     *
     * @param num            数值
     * @param lowerCase      是否小写
     * @param keepOneDecimal 是否保留1位小数
     * @return
     */
    public static String keepK(double num, boolean lowerCase, boolean keepOneDecimal) {
        if (1_000 > num) {
            return keepOneDecimal ? keepOneDecimal(num) : keepTwoDecimal(num);
        }
        return (keepOneDecimal ? keepOneDecimal(num / 1_000) : keepTwoDecimal(num / 1_000))
                + (lowerCase ? "k" : "K");
    }

    /**
     * 大于等于1k时换算成千单位
     *
     * @param num            数值
     * @param keepOneDecimal 是否保留1位小数
     * @return
     */
    public static String keepKInChinese(double num, boolean keepOneDecimal) {
        if (1_000 > num) {
            return keepOneDecimal ? keepOneDecimal(num) : keepTwoDecimal(num);
        }
        return (keepOneDecimal ? keepOneDecimal(num / 1_000) : keepTwoDecimal(num / 1_000))
                + "千";
    }

    /**
     * 大于等于1w时换算成w单位
     * <ol>
     * <li>12345 -> 1.2w</li>
     * <li>12.345 -> 12.3</li>
     * </ol>
     *
     * @param num
     * @return
     */
    public static String keepW(double num) {
        return keepW(num, true, true);
    }

    /**
     * 大于等于1w时换算成w单位
     *
     * @param num            数值
     * @param lowerCase      是否小写
     * @param keepOneDecimal 是否保留1位小数
     * @return
     */
    public static String keepW(double num, boolean lowerCase, boolean keepOneDecimal) {
        if (1_0000 > num) {
            return keepOneDecimal ? keepOneDecimal(num) : keepTwoDecimal(num);
        }
        return (keepOneDecimal ? keepOneDecimal(num / 1_0000) : keepTwoDecimal(num / 1_0000))
                + (lowerCase ? "w" : "W");
    }

    /**
     * 大于等于1w时换算成万单位
     *
     * @param num            数值
     * @param keepOneDecimal 是否保留1位小数
     * @return
     */
    public static String keepWInChinese(double num, boolean keepOneDecimal) {
        if (1_0000 > num) {
            return keepOneDecimal ? keepOneDecimal(num) : keepTwoDecimal(num);
        }
        return (keepOneDecimal ? keepOneDecimal(num / 1_0000) : keepTwoDecimal(num / 1_0000))
                + "万";
    }

    /**
     * 保留一位小数
     *
     * @param num 数值
     * @return
     */
    public static String keepOneDecimal(double num) {
        return dfOne.format(num);
    }

    /**
     * 保留两位小数
     *
     * @param num 数值
     * @return
     */
    public static String keepTwoDecimal(double num) {
        return dfTwo.format(num);
    }
}
