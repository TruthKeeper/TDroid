package com.tk.tdroid.utils;

import java.math.BigDecimal;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/20
 *     desc   : 大小转换工具
 * </pre>
 */

public class SizeUtils {
    private static final int BYTE = 1 << 10;

    private SizeUtils() {
        throw new IllegalStateException();
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String formatSize(double size) {
        return formatSize(size, 2);
    }

    /**
     * 格式化单位
     *
     * @param size
     * @param scale 保留几位小数
     * @return
     */
    public static String formatSize(double size, int scale) {
        double kiloByte = size / BYTE;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / BYTE;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(scale, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / BYTE;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(scale, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / BYTE;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(scale, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
