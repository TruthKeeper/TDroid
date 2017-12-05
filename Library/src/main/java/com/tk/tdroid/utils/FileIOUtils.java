package com.tk.tdroid.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import okio.BufferedSource;
import okio.Okio;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/10/07
 *     desc   : 文件IO流操作工具类
 *              写：NIO效率 > IO > okio
 *              读：NIO效率 > okio > IO okio
 * </pre>
 */
public final class FileIOUtils {
    /**
     * 8192
     */
    public static final int BUFFER = 1 << 13;
    private static final String LINE_SEP = System.getProperty("line.separator");

    private FileIOUtils() {
        throw new IllegalStateException();
    }

    /**
     * 将输入流写入文件
     *
     * @param filePath 路径
     * @param stream   输入流
     * @param append   是否追加在文件末
     * @return
     */
    public static boolean writeByIO(@NonNull String filePath, @NonNull InputStream stream, boolean append) {
        return writeByIO(new File(filePath), stream, append);
    }


    /**
     * 将输入流写入文件
     *
     * @param file   目标文件
     * @param stream 输入流
     * @param append 是否追加在文件末
     * @return
     */
    public static boolean writeByIO(@NonNull File file, @NonNull InputStream stream, boolean append) {
        if (!FileUtils.createOrExistsFile(file)) {
            return false;
        }
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append), BUFFER);
            byte data[] = new byte[BUFFER];
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
            IOUtils.closeQuietly(stream, os);
        }
    }

    /**
     * 将字节数组写入文件
     *
     * @param filePath 路径
     * @param bytes    字节数组
     * @param append   是否追加在文件末
     * @return
     */
    public static boolean writeBytesByIO(@NonNull String filePath, @NonNull byte[] bytes, boolean append) {
        return writeBytesByIO(new File(filePath), bytes, append);
    }


    /**
     * 将字节数组写入文件
     *
     * @param file   目标文件
     * @param bytes  字节数组
     * @param append 是否追加在文件末
     * @return
     */
    public static boolean writeBytesByIO(@NonNull File file, @NonNull byte[] bytes, boolean append) {
        if (!FileUtils.createOrExistsFile(file)) {
            return false;
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file, append);
            os.write(bytes);
            os.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    /**
     * 将字符串写入文件
     *
     * @param filePath 路径
     * @param text     字符串
     * @param append   是否追加在文件末
     * @return
     */
    public static boolean writeStringByIO(@NonNull String filePath, @NonNull String text, boolean append) {
        return writeStringByIO(new File(filePath), text, append);
    }

    /**
     * 将字符串写入文件
     *
     * @param file   目标文件
     * @param text   字符串
     * @param append 是否追加在文件末
     * @return
     */
    public static boolean writeStringByIO(@NonNull File file, @NonNull String text, boolean append) {
        if (!FileUtils.createOrExistsFile(file)) {
            return false;
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, append), BUFFER);
            writer.write(text);
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * 将输入流写入文件
     *
     * @param filePath 路径
     * @param stream   输入流
     * @param append   是否追加在文件末
     * @return
     */
    public static boolean writeByNIO(@NonNull String filePath, @NonNull FileInputStream stream, boolean append) {
        return writeByNIO(new File(filePath), stream, append);
    }

    /**
     * 将输入流写入文件
     *
     * @param file   文件
     * @param stream 输入流
     * @param append 是否追加在文件末
     * @return
     */
    public static boolean writeByNIO(@NonNull File file, @NonNull FileInputStream stream, boolean append) {
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
            IOUtils.closeQuietly(inputChannel, outputChannel);
        }
    }

    /**
     * 将字节数组写入文件
     *
     * @param filePath 路径
     * @param bytes    字节数组
     * @param append   是否追加在文件末
     * @return
     */
    public static boolean writeBytesByNIO(@NonNull String filePath, @NonNull byte[] bytes, boolean append) {
        return writeBytesByNIO(new File(filePath), bytes, append);
    }


    /**
     * 将字节数组写入文件
     *
     * @param file   目标文件
     * @param bytes  字节数组
     * @param append 是否追加在文件末
     * @return
     */
    public static boolean writeBytesByNIO(@NonNull File file, @NonNull byte[] bytes, boolean append) {
        if (!FileUtils.createOrExistsFile(file)) {
            return false;
        }
        FileChannel fileChannel = null;
        try {
            fileChannel = new FileOutputStream(file, append).getChannel();
            fileChannel.write(ByteBuffer.wrap(bytes));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.closeQuietly(fileChannel);
        }
    }

    /**
     * 将字符串写入文件
     *
     * @param filePath 路径
     * @param text     字符串
     * @return
     */
    public static boolean writeStringByNIO(@NonNull String filePath, @NonNull String text, boolean append) {
        return writeStringByNIO(new File(filePath), text, append);
    }

    /**
     * 将字符串写入文件
     *
     * @param file 目标文件
     * @param text 字符串
     * @return
     */
    public static boolean writeStringByNIO(@NonNull File file, @NonNull String text, boolean append) {
        if (!FileUtils.createOrExistsFile(file)) {
            return false;
        }
        FileChannel outputChannel = null;

        try {
            ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());
            outputChannel = new FileOutputStream(file, append).getChannel();
            outputChannel.write(buffer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.closeQuietly(outputChannel);
        }
    }

    /**
     * 读取文件到字符串中
     *
     * @param filePath 路径
     * @return
     */
    public static String readStringByIO(@NonNull String filePath) {
        return readStringByIO(new File(filePath), null);
    }

    /**
     * 读取文件到字符串中
     *
     * @param filePath    路径
     * @param charsetName 编码格式
     * @return
     */
    public static String readStringByIO(@NonNull String filePath, @Nullable String charsetName) {
        return readStringByIO(new File(filePath), charsetName);
    }

    /**
     * 读取文件到字符串中
     *
     * @param file 源文件
     * @return
     */
    public static String readStringByIO(@NonNull File file) {
        return readStringByIO(file, null);
    }

    /**
     * 读取文件到字符串中
     *
     * @param file        源文件
     * @param charsetName 编码格式
     * @return
     */
    public static String readStringByIO(@NonNull File file, @Nullable String charsetName) {
        if (!FileUtils.exist(file)) {
            return null;
        }
        BufferedReader reader = null;
        try {
            StringBuilder builder = new StringBuilder();
            if (EmptyUtils.isEmpty(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)), BUFFER);
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName), BUFFER);
            }
            String line;
            if ((line = reader.readLine()) != null) {
                builder.append(line);
                while ((line = reader.readLine()) != null) {
                    builder.append(LINE_SEP).append(line);
                }
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * 读取文件到字符串中
     *
     * @param filePath 路径
     * @return
     */
    public static byte[] readBytesByIO(@NonNull String filePath) {
        return readBytesByIO(new File(filePath));
    }

    /**
     * 读取文件到字节数组中
     *
     * @param file 文件路径
     * @return
     */
    public static byte[] readBytesByIO(@NonNull File file) {
        if (!FileUtils.exist(file)) {
            return null;
        }
        FileInputStream fis = null;
        ByteArrayOutputStream os = null;
        try {
            fis = new FileInputStream(file);
            os = new ByteArrayOutputStream();
            byte[] b = new byte[BUFFER];
            int len;
            while ((len = fis.read(b)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(fis, os);
        }
    }

    /**
     * 读取文件到字符串中
     *
     * @param filePath 路径
     * @return
     */
    public static byte[] readBytesByNIO(@NonNull String filePath) {
        return readBytesByNIO(new File(filePath));
    }

    /**
     * 读取文件到字节数组中
     *
     * @param file 文件路径
     * @return
     */
    public static byte[] readBytesByNIO(@NonNull File file) {
        if (!FileUtils.exist(file)) {
            return null;
        }
        FileChannel fileChannel = null;
        try {
            fileChannel = new FileInputStream(file).getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
            while (true) {
                if (!((fileChannel.read(byteBuffer)) > 0)) break;
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(fileChannel);
        }
    }

    /**
     * 读取文件到字符串UTF-8中
     *
     * @param filePath 路径
     * @return
     */
    public static String readStringByOKIO(@NonNull String filePath) {
        return readStringByOKIO(new File(filePath));
    }


    /**
     * 读取文件到字符串UTF-8中
     *
     * @param file 源文件
     * @return
     */
    public static String readStringByOKIO(@NonNull File file) {
        if (!FileUtils.exist(file)) {
            return null;
        }
        BufferedSource buffer = null;
        try {
            buffer = Okio.buffer(Okio.source(file));
            return buffer.readUtf8();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(buffer);
        }
    }
}
