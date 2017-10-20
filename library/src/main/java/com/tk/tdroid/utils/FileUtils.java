package com.tk.tdroid.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/9/13
 *     desc   : 文件工具 <ul>
 *         <li>判断文件是否存在</li>
 *         <li>重命名文件</li>
 *         <li>判断目录是否存在，不存在则判断是否创建成功</li>
 *         <li>判断文件是否存在，不存在则判断是否创建成功</li>
 *         <li>复制文件、文件夹到目录</li>
 *         <li>复制文件到文件</li>
 *         <li>移动文件、文件夹到目录</li>
 *         <li>移动文件到文件</li>
 *         <li>删除文件、文件夹(支持过滤)</li>
 *         <li>计算文件、文件夹大小(支持过滤)</li>
 *     </ul>
 * </pre>
 */
public final class FileUtils {

    private FileUtils() {
        throw new IllegalStateException();
    }

    /**
     * 是否存在
     *
     * @param path
     * @return
     */
    public static boolean exist(@Nullable String path) {
        return !TextUtils.isEmpty(path) && exist(new File(path));
    }

    /**
     * 是否存在
     *
     * @param file
     * @return
     */
    public static boolean exist(@Nullable File file) {
        return file != null && file.exists();
    }

    /**
     * 重命名
     *
     * @param file
     * @param newName
     * @return
     */
    public static boolean rename(@NonNull File file, @NonNull String newName) {
        return file.renameTo(new File(file.getParent(), newName));
    }

    /**
     * 拷贝文件 or 文件夹到目录下
     * <br><b>递归</b>
     *
     * @param srcFile 源文件、文件夹
     * @param destDir 目标目录
     * @return
     */
    public static boolean copyFileToDir(@NonNull File srcFile, @NonNull File destDir) {
        return copyAndMove(srcFile, new File(destDir, srcFile.getName()), true, false);
    }

    /**
     * 拷贝文件 or 文件夹到目录下
     * <br><b>递归</b>
     *
     * @param srcFile 源文件、文件夹
     * @param destDir 目标目录
     * @param cover   是否覆盖
     * @return
     */
    public static boolean copyFileToDir(@NonNull File srcFile, @NonNull File destDir, boolean cover) {
        return copyAndMove(srcFile, new File(destDir, srcFile.getName()), cover, false);
    }

    /**
     * 拷贝文件到指定文件
     * <br><b>递归</b>
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @return
     */
    public static boolean copyFileToFile(@NonNull File srcFile, @NonNull File destFile) {
        return copyAndMove(srcFile, destFile, true, false);
    }

    /**
     * 拷贝文件到指定文件
     * <br><b>递归</b>
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @param cover    是否覆盖
     * @return
     */
    public static boolean copyFileToFile(@NonNull File srcFile, @NonNull File destFile, boolean cover) {
        return copyAndMove(srcFile, destFile, cover, false);
    }

    /**
     * 移动文件到指定文件
     * <br><b>递归</b>
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @return
     */
    public static boolean moveFileToFile(@NonNull File srcFile, @NonNull File destFile) {
        return copyAndMove(srcFile, destFile, true, true);
    }

    /**
     * 移动文件 or 文件夹到目录下
     * <br><b>递归</b>
     *
     * @param srcFile 源文件、文件夹
     * @param destDir 目标目录
     * @return
     */
    public static boolean moveFileToDir(@NonNull File srcFile, @NonNull File destDir) {
        return copyAndMove(srcFile, new File(destDir, srcFile.getName()), true, true);
    }

    /**
     * 移动拷贝 文件到文件
     * <br><b>递归</b>
     *
     * @param srcFile       源文件、文件夹
     * @param destFile      目标文件
     * @param cover         是否覆盖
     * @param deleteSrcFile 是否删除源文件、文件夹
     * @return
     */
    public static boolean copyAndMove(@NonNull File srcFile, @NonNull File destFile, boolean cover, boolean deleteSrcFile) {
        if (!exist(srcFile)) {
            //过滤：源文件不存在
            return false;
        }
        if (srcFile.equals(destFile)) {
            //过滤：当前目录下
            return false;
        }
        if (srcFile.isDirectory()) {
            File[] listFiles = srcFile.listFiles();
            for (File listFile : listFiles) {
                //迭代+递归
                if (!copyAndMove(listFile, destFile, cover, deleteSrcFile)) {
                    return false;
                }
            }
            return true;
        } else {
            if (destFile.exists() && !cover) {
                //已存在，并且无需覆盖
                return true;
            }
            try {
                //写入
                boolean write = FileIOUtils.writeByNIO(destFile, new FileInputStream(srcFile), false);
                if (deleteSrcFile) {
                    return write && deleteFile(srcFile);
                } else {
                    return write;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 删除文件 or 文件夹
     * <br><b>递归</b>
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(@Nullable File file) {
        return deleteFile(file, null);
    }

    /**
     * 删除文件 or 文件夹
     * <br><b>递归</b>
     *
     * @param file
     * @param filter 过滤器
     * @return
     */
    public static boolean deleteFile(@Nullable File file, @Nullable FileFilter filter) {
        if (!exist(file)) {
            return false;
        }
        if (filter != null && !filter.filter(file)) {
            return true;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (!deleteFile(f, filter)) {
                    return false;
                }
            }
            return true;
        }
        return file.delete();
    }

    /**
     * 获取文件的大小
     * <br><b>递归</b>
     *
     * @param file
     * @return
     */
    public static long getFileSize(@NonNull File file) {
        return getFileSize(file, null);
    }

    /**
     * 获取文件的大小
     * <br><b>递归</b>
     *
     * @param file
     * @param filter 过滤器
     * @return
     */
    public static long getFileSize(@NonNull File file, @Nullable FileFilter filter) {
        long size = 0;
        if (!exist(file)) {
            return size;
        }
        if (filter != null && !filter.filter(file)) {
            return size;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                size += getFileSize(f);
            }
        } else {
            size += file.length();
        }
        return size;
    }

    /**
     * 创建或者是否存在
     * <br><b>递归</b>
     *
     * @param file
     * @return
     */
    public static boolean createOrExistsFile(@Nullable File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.isFile();
        }
        //尝试创建Dir
        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建或者是否存在
     *
     * @param dir
     * @return
     */
    public static boolean createOrExistsDir(@Nullable File dir) {
        if (dir == null) {
            return false;
        }
        if (dir.exists()) {
            return dir.isDirectory();
        }
        return dir.mkdirs();
    }

    public interface FileFilter {
        /**
         * 是否通过过滤
         *
         * @param file
         * @return false：需要过滤
         */
        boolean filter(File file);
    }
}
