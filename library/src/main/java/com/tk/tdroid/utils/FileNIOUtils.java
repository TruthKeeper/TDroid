package com.tk.tdroid.utils;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/07
 *     desc   : 文件NIO流操作工具
 * </pre>
 */
public final class FileNIOUtils {
    private FileNIOUtils() {
        throw new IllegalStateException();
    }

    /**
     * 将输入流写入文件
     *
     * @param filePath 路径
     * @param stream   输入流
     * @return
     */
    public static boolean write(@NonNull String filePath, @NonNull FileInputStream stream) {
        return write(new File(filePath), stream, false);
    }

    /**
     * 将输入流写入文件
     *
     * @param filePath 路径
     * @param stream   输入流
     * @param append   是否追加在文件末
     * @return
     */
    public static boolean write(@NonNull String filePath, @NonNull FileInputStream stream, boolean append) {
        return write(new File(filePath), stream, false);
    }

    /**
     * 将输入流写入文件
     *
     * @param file   文件
     * @param stream 输入流
     * @param append 是否追加在文件末
     * @return
     */
    public static boolean write(@NonNull File file, @NonNull FileInputStream stream, boolean append) {
        if (!FileUtils.createOrExistsFile(file)) {
            return false;
        }
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;

        try {
            inputChannel = stream.getChannel();
            outputChannel = new FileOutputStream(file, append).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.closeQuietly(inputChannel);
            IOUtils.closeQuietly(outputChannel);
        }
    }
}
