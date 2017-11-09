package com.tk.tdroid.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/9
 *      desc :
 * </pre>
 */

public final class ZipUtils {
    private ZipUtils() {
        throw new IllegalStateException();
    }

    /**
     * 压缩
     *
     * @param srcFile     源文件 or 源目录
     * @param distDirPath 输出目录 为空则在源文件目录下
     * @return 是否成功
     */
    public static boolean zipFile(@Nullable File srcFile, @Nullable String distDirPath) {
        if (!FileUtils.exist(srcFile)) {
            return false;
        }
        File outputZipDir;
        if (EmptyUtils.isEmpty(distDirPath)) {
            //为空则在源文件目录下
            outputZipDir = srcFile.getParentFile();
        } else {
            outputZipDir = new File(distDirPath);
            if (outputZipDir.exists()) {
                if (!outputZipDir.isDirectory()) {
                    outputZipDir.delete();
                }
            } else {
                outputZipDir.mkdirs();
            }
        }
        String zipName = generateZipName(srcFile);
        File outputZipFile = outputZipDir == null ? new File(zipName) : new File(outputZipDir, zipName);
        if (outputZipFile.exists()) {
            outputZipFile.delete();
        }
        try {
            return zipFile(srcFile, null, new ZipOutputStream(new FileOutputStream(outputZipFile)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 压缩
     *
     * @param srcFile  源文件 or 源目录
     * @param destFile 输出文件 为空则在源文件目录下, Ps:可以不携带.zip后缀
     * @return 是否成功
     */
    public static boolean zipFile(@Nullable File srcFile, @Nullable File destFile) {

        if (!FileUtils.exist(srcFile)) {
            return false;
        }
        File output;
        if (destFile == null) {
            //为空则在源文件目录下
            if (srcFile.getParentFile() == null) {
                output = new File(generateZipName(srcFile));
            } else {
                output = new File(srcFile.getParentFile(), generateZipName(srcFile));
            }
        } else {
            File destParent = destFile.getParentFile();
            if (destParent == null) {
                output = new File(generateZipName(destFile));
            } else {
                if (destParent.exists()) {
                    if (!destParent.isDirectory()) {
                        destParent.delete();
                        destParent.mkdirs();
                    }
                } else {
                    destParent.mkdirs();
                }
                output = new File(destParent, generateZipName(destFile));
            }
        }
        if (FileUtils.exist(output)) {
            output.delete();
        }
        try {
            return zipFile(srcFile, null, new ZipOutputStream(new FileOutputStream(output)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static boolean zipFile(@NonNull final File srcFile, @Nullable final String rootPath, @NonNull final ZipOutputStream zos) throws IOException {
        String root;
        if (EmptyUtils.isEmpty(rootPath)) {
            root = srcFile.getName();
        } else {
            root = rootPath + File.separator + srcFile.getName();
        }
        if (srcFile.isDirectory()) {
            File[] fileList = srcFile.listFiles();
            if (EmptyUtils.isEmpty(fileList)) {
                ZipEntry entry = new ZipEntry(root + File.separator);
                zos.putNextEntry(entry);
                zos.closeEntry();
            } else {
                for (File file : fileList) {
                    //递归
                    if (!zipFile(file, root, zos)) {
                        return false;
                    }
                }
            }
        } else {
            InputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(srcFile));
                ZipEntry entry = new ZipEntry(root);
                zos.putNextEntry(entry);
                byte buffer[] = new byte[FileIOUtils.BUFFER];
                int len;
                while ((len = is.read(buffer, 0, FileIOUtils.BUFFER)) != -1) {
                    zos.write(buffer, 0, len);
                }
                zos.flush();
                zos.closeEntry();
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
        return true;
    }

    /**
     * 解压文件
     *
     * @param srcFile     源文件
     * @param destDirPath 输出目录
     * @return
     */
    @Nullable
    public static List<File> unzipFile(@Nullable File srcFile, @Nullable String destDirPath) {
        if (srcFile == null) {
            return null;
        }
        //为空则在源文件目录下
        return unzipFile(srcFile, EmptyUtils.isEmpty(destDirPath) ? srcFile.getParentFile() : new File(destDirPath));

    }

    /**
     * 解压文件
     *
     * @param srcFile 源文件
     * @param destDir 输出目录
     * @return
     */
    @Nullable
    public static List<File> unzipFile(@Nullable File srcFile, @Nullable File destDir) {
        if (!FileUtils.exist(srcFile)) {
            return null;
        }
        if (destDir == null) {
            //为空则在源文件目录下
            destDir = srcFile.getParentFile();
        }
        List<File> zipFiles = new ArrayList<>();

        ZipEntry entry;
        String entryName;
        String filePath;
        File zipFile;

        ZipInputStream zis = null;
        OutputStream os = null;
        try {
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(srcFile)));

            while ((entry = zis.getNextEntry()) != null) {
                entryName = entry.getName();
                filePath = destDir.getAbsolutePath() + File.separator + entryName;
                zipFile = new File(filePath);
                zipFiles.add(zipFile);
                if (entry.isDirectory()) {
                    zipFile.mkdirs();
                } else {
                    FileUtils.createOrExistsDir(zipFile.getParentFile());
                    zipFile.createNewFile();

                    try {
                        os = new BufferedOutputStream(new FileOutputStream(zipFile), FileIOUtils.BUFFER);
                        byte buffer[] = new byte[FileIOUtils.BUFFER];
                        int len;
                        while ((len = zis.read(buffer, 0, FileIOUtils.BUFFER)) != -1) {
                            os.write(buffer, 0, len);
                        }
                        os.flush();
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zis);
        }
        return zipFiles;
    }

    /**
     * 生成新名称
     *
     * @param srcFile
     * @return
     */
    private static String generateZipName(File srcFile) {
        String fileName = srcFile.getName();
        int index = fileName.lastIndexOf(".");
        return index > 0 ? fileName.substring(0, index) : fileName + ".zip";
    }
}
