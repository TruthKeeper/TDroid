package com.tk.tdroid.utils;

import android.support.annotation.Nullable;

import java.io.Closeable;
import java.io.IOException;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/03/17
 *     desc   : IO工具
 * </pre>
 */
public final class IOUtils {
    private IOUtils() {
        throw new IllegalStateException();
    }

    /**
     * 关闭
     *
     * @param closeable
     */
    public static void close(@Nullable Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 关闭
     *
     * @param closeable
     */
    public static void close(@Nullable Closeable... closeable) {
        for (Closeable c : closeable) {
            close(c);
        }
    }

    /**
     * 安静关闭
     *
     * @param closeable
     */
    public static void closeQuietly(@Nullable Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 安静关闭
     *
     * @param closeable
     */
    public static void closeQuietly(@Nullable Closeable... closeable) {
        for (Closeable c : closeable) {
            closeQuietly(c);
        }
    }
}
