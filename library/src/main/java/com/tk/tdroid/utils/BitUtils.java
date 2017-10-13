package com.tk.tdroid.utils;

/**
 * <pre>
 *      author : TK
 *      time : 2017/9/28
 *      desc : 位操作工具类
 * </pre>
 */

public final class BitUtils {
    private BitUtils() {
        throw new IllegalStateException();
    }

    /**
     * 或操作
     *
     * @param source
     * @param flags
     * @return
     */
    public static int addFlag(int source, int... flags) {
        if (flags != null) {
            for (int flag : flags) {
                source |= flag;
            }
        }
        return source;
    }

    /**
     * 或操作
     *
     * @param source
     * @param flags
     * @return
     */
    public static int clearFlag(int source, int... flags) {
        if (flags != null) {
            for (int flag : flags) {
                source &= ~flag;
            }
        }
        return source;
    }

    /**
     * 是否包含
     *
     * @param source
     * @param flag
     * @return
     */
    public static boolean containsFlag(int source, int flag) {
        return (source & flag) == flag;
    }
}
