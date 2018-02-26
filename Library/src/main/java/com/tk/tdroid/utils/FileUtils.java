package com.tk.tdroid.utils;

import android.annotation.TargetApi;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.tk.tdroid.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/9/13
 *     desc   : 文件工具类 <ul>
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
    /**
     * 后缀和开发类型的映射关系
     */
    private static final String[][] MIME_TYPE = {
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".aac", "audio/x-mpeg"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"},
            {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"},
            {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}};

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
        return !EmptyUtils.isEmpty(path) && exist(new File(path));
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
     * 获取文件的类型
     *
     * @param file
     * @return
     */
    public static String getMIMEType(@NonNull File file) {
        return getMIMEType(file.getName());
    }

    /**
     * 获取文件的类型
     *
     * @param fileName
     * @return
     */
    public static String getMIMEType(@NonNull String fileName) {
        String type = MIME_TYPE[MIME_TYPE.length - 1][1];
        //获取后缀名前的分隔符"."在fName中的位置。
        int index = fileName.lastIndexOf(".");
        if (index < 0) {
            return type;
        }
        //获取文件的后缀名
        String end = fileName.substring(index, fileName.length()).toLowerCase();
        if (EmptyUtils.isEmpty(end)) {
            return type;
        }
        //找到对应的MIME类型。
        for (String[] types : MIME_TYPE) {
            if (end.equals(types[0])) {
                return types[1];
            }
        }
        return type;
    }

    /**
     * 适配Android 7.0的文件访问权限
     *
     * @param file
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static Uri wrapperFile(@NonNull File file) {
        //通过FileProvider创建一个content类型的Uri
        return FileProvider.getUriForFile(Utils.getApp(),
                Utils.getApp().getString(R.string.TDroid_file_provider),
                file);
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

    /**
     * 改变系统默认的{@link android.content.SharedPreferences} 存储位置 , 卸载后数据的保留
     *
     * @param file {@code new File(Environment.getExternalStorageDirectory(), "TestDir" )}
     */
    public static void changeSPPath(File file) {
        Field field = null;
        try {
            field = ContextWrapper.class.getDeclaredField("mBase");
            field.setAccessible(true);
            Object obj = field.get(Utils.getApp());
            field = obj.getClass().getDeclaredField("mPreferencesDir");
            field.setAccessible(true);
            createOrExistsDir(file);
            //改变ContextImpl的引用
            field.set(obj, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
