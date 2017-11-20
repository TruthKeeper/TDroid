package com.tk.tdroid.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/9/13
 *     desc   : 时间转换工具类
 * </pre>
 */

public final class TimeUtils {

    private static final int SECOND = 1_000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    private static final int WEEK = 7 * DAY;

    private static final String SECOND_STR = "秒";
    private static final String MINUTE_STR = "分";
    private static final String HOUR_STR = "小时";
    private static final String DAY_STR = "天";
    private static final String WEEK_STR = "周";

    private static final SimpleDateFormat exactDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private TimeUtils() {
        throw new IllegalStateException();
    }

    /**
     * 解析为详细日期
     *
     * @param millisecond
     * @return 2017-08-08 20:20:20
     */
    public static String formatExactDate(long millisecond) {
        return exactDate.format(new Date(millisecond));
    }

    /**
     * 解析成时 分
     *
     * @param millisecond
     * @return 12:34
     */
    public static String formatHourMinute(long millisecond) {
        if (millisecond < MINUTE) {
            return "00:00";
        }
        StringBuilder builder = new StringBuilder();
        if (millisecond < HOUR) {
            builder.append("00:");
            builder.append(complete(millisecond / MINUTE));
            return builder.toString();
        }
        builder.append(complete(millisecond / HOUR))
                .append(":")
                .append(complete((millisecond / MINUTE) % 60));
        return builder.toString();

    }

    /**
     * 解析成时 分 秒
     *
     * @param millisecond
     * @param fully       保持2个冒号分隔符 like 12:34:56
     * @return 12:34:56 or 12:34
     */
    public static String formatHourMinuteSecond(long millisecond, boolean fully) {
        if (millisecond < SECOND) {
            return fully ? "00:00:00" : "00:00";
        }
        StringBuilder builder = new StringBuilder();
        if (millisecond < MINUTE) {
            if (fully) {
                builder.append("00:00:");
            } else {
                builder.append("00:");
            }
            builder.append(complete(millisecond / SECOND));
            return builder.toString();
        }
        if (millisecond < HOUR) {
            if (fully) {
                builder.append("00:");
            }
            builder.append(complete(millisecond / MINUTE))
                    .append(":")
                    .append(complete((millisecond / SECOND) % 60));
            return builder.toString();
        }
        builder.append(complete(millisecond / HOUR))
                .append(":")
                .append(complete((millisecond / MINUTE) % 60))
                .append(":")
                .append(complete((millisecond / SECOND) % 60));
        return builder.toString();
    }

    /**
     * 解析成中文时间
     *
     * @param millisecond
     * @param withSecond  是否在最后携带秒数
     * @return 12小时34分钟（56秒）
     */
    public static String formatBySurplus(long millisecond, boolean withSecond) {
        StringBuilder builder = new StringBuilder();
        if (millisecond < MINUTE) {
            if (withSecond) {
                builder.append(millisecond / SECOND)
                        .append(SECOND_STR);
            } else {
                builder.append(1)
                        .append(MINUTE_STR);
            }
            return builder.toString();
        }
        if (millisecond < HOUR) {
            builder.append(millisecond / MINUTE)
                    .append(MINUTE_STR);
            if (withSecond) {
                builder.append((millisecond / SECOND) % 60)
                        .append(SECOND_STR);
            }
            return builder.toString();
        }
        if (millisecond < DAY) {
            builder.append(millisecond / HOUR)
                    .append(HOUR_STR)
                    .append((millisecond / MINUTE) % 60)
                    .append(MINUTE_STR);
            if (withSecond) {
                builder.append((millisecond / SECOND) % 60)
                        .append(SECOND_STR);
            }
            return builder.toString();
        }
        builder.append(millisecond / DAY)
                .append(DAY_STR)
                .append((millisecond / HOUR) % 24)
                .append(HOUR_STR)
                .append((millisecond / MINUTE) % 60)
                .append(MINUTE_STR);
        if (withSecond) {
            builder.append((millisecond / SECOND) % 60)
                    .append(SECOND_STR);
        }
        return builder.toString();
    }

    /**
     * 补全
     *
     * @param l
     * @return
     */
    private static String complete(long l) {
        if (l < 10) {
            return "0" + l;
        }
        return Long.toString(l);
    }

    /**
     * 解析成与当前的时间间隔
     *
     * @param millisecond 小于当前时间
     * @return
     */
    public static String formatSpanByNow(final long millisecond) {
        long now = new Date().getTime();
        long span = now - millisecond;

        if (span < 3 * MINUTE) {
            //3分钟内
            return "刚刚";
        }
        if (span < HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", span / MINUTE);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 获取当天时间
        long morning = calendar.getTimeInMillis();
        calendar.clear();
        if (millisecond >= morning) {
            return "今天 " + formatHourMinute(millisecond - morning);
        } else if (millisecond < morning && millisecond >= morning - DAY) {
            return "昨天 " + formatHourMinute(millisecond - (morning - DAY));
        } else {
            return formatExactDate(millisecond);
        }
    }
}
