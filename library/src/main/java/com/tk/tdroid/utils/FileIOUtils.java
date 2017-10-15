package com.tk.tdroid.utils;

import android.support.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/07
 *     desc   : 文件IO流操作工具
 * </pre>
 */
public final class FileIOUtils {
    private FileIOUtils() {
        throw new IllegalStateException();
    }

    /**
     * 将输入流写入文件
     *
     * @param filePath 路径
     * @param stream   输入流
     * @return
     */
    public static boolean write(@NonNull String filePath, @NonNull InputStream stream) {
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
    public static boolean write(@NonNull String filePath, @NonNull InputStream stream, boolean append) {
        return write(new File(filePath), stream, false);
    }

    /**
     * 将输入流写入文件
     *
     * @param file   文件
     * @param stream     输入流
     * @param append 是否追加在文件末
     * @return
     */
    public static boolean write(@NonNull File file, @NonNull InputStream stream, boolean append) {
        if (!FileUtils.createOrExist(file)) {
            return false;
        }
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[4096];
            int length;
            while ((length = stream.read(data)) != -1) {
                os.write(data, 0, length);
            }
            os.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.closeQuietly(stream);
            IOUtils.closeQuietly(os);
        }
    }

}
