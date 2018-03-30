package com.tk.tdroid.utils;

import java.math.BigDecimal;

/**
 * <pre>
 *      author : TK
 *      time : 2018/3/26
 *      desc : 精确计算工具类
 * </pre>
 */

public final class DoubleUtils {
    private DoubleUtils() {
        throw new IllegalStateException();
    }

    /**
     * 精确加法计算
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double add(double value1, double value2) {
        return BigDecimal.valueOf(value1).add(BigDecimal.valueOf(value2)).doubleValue();
    }

    /**
     * 精确减法运算
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double subtract(double value1, double value2) {
        return BigDecimal.valueOf(value1).subtract(BigDecimal.valueOf(value2)).doubleValue();
    }

    /**
     * 提供精确乘法运算的mul方法
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double multiply(double value1, double value2) {
        return BigDecimal.valueOf(value1).multiply(BigDecimal.valueOf(value2)).doubleValue();
    }

    /**
     * 精确的除法运算方法
     * 抛出异常说明结果为无限循环小数
     *
     * @param value1
     * @param value2
     * @return
     * @throws IllegalAccessException
     */
    public static double divide(double value1, double value2) throws IllegalAccessException {
        return BigDecimal.valueOf(value1).divide(BigDecimal.valueOf(value2)).doubleValue();
    }
}
