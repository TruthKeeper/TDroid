package com.tk.tdroid.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/7
 *      desc : 图像压缩工具
 *      <ol>
 *          <li>压缩到File</li>
 *          <li>压缩到Bitmap</li>
 *          <li>压缩到Base64字符串</li>
 *      </ol>
 * </pre>
 */

public final class ImageCompressUtils {
    /**
     * 压缩成File
     *
     * @param config
     * @return
     */
    public static File compress2File(@NonNull final Config config) {
        Pair<Bitmap, byte[]> pair = compress(config);
        if (pair != null) {
            File outputDir = config.outputDir;
            if (config.outputDir == null || !FileUtils.createOrExistsDir(config.outputDir)) {
                outputDir = Utils.getApp().getCacheDir();
            }
            final String suffix = "." + config.compressFormat.name().toLowerCase();
            final long time = System.currentTimeMillis();
            final File file = new File(outputDir, (EmptyUtils.isEmpty(config.fileName) ? time : config.fileName) + suffix);
            if (FileIOUtils.writeBytesByNIO(file, pair.second, false)) {
                return file;
            }
        }
        return null;
    }

    /**
     * 压缩成Bitmap
     *
     * @param config
     * @return
     */
    public static Bitmap compress2Bitmap(@NonNull final Config config) {
        Pair<Bitmap, byte[]> pair = compress(config);
        return pair == null ? null : pair.first;
    }

    /**
     * 压缩成Base64字符串
     *
     * @param config
     * @return
     */
    public static String compress2Base64(@NonNull final Config config) {
        Pair<Bitmap, byte[]> pair = compress(config);
        return pair == null ? "" : Base64.encodeToString(pair.second, Base64.NO_WRAP);
    }

    private static Pair<Bitmap, byte[]> compress(@NonNull final Config config) {
        final File sourceFile = config.sourceFile;
        if (!FileUtils.exist(sourceFile)) {
            throw new RuntimeException("file not exist");
        }
        final int maxWidth = config.maxWidth;
        final int maxHeight = config.maxHeight;
        final Bitmap.CompressFormat compressFormat = config.compressFormat;
        final int maxSize = config.maxSize;
        final int quality = config.quality;

        //获取图片旋转角度
        int photoDegree = 0;
        photoDegree = ImageUtils.getImageDegree(sourceFile.getAbsolutePath());
        //获取原图信息
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);
        options.inJustDecodeBounds = false;
        //采样率缩放
        options.inSampleSize = getSampleSize(options, maxWidth, maxHeight);
        //获取缩放处理后的Bitmap
        Bitmap output = BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);
        if (0 != photoDegree) {
            Matrix matrix = new Matrix();
            matrix.postRotate(photoDegree);
            //旋转图片
            output = Bitmap.createBitmap(output, 0, 0, output.getWidth(), output.getHeight(), matrix, true);
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            if (quality <= 0 && maxSize > 0) {
                //高压模式
                int tempQuality = 100;
                output.compress(compressFormat, tempQuality, baos);
                while (baos.toByteArray().length > maxSize) {
                    baos.reset();
                    tempQuality -= 10;
                    tempQuality = Math.max(tempQuality, 20);
                    output.compress(compressFormat, quality, baos);
                }
            } else {
                //仅压缩一次
                output.compress(compressFormat, quality, baos);
            }
            baos.flush();
            return new Pair<>(output, baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(baos);
        }
        return null;
    }

    private static int getSampleSize(BitmapFactory.Options options, int imageWidth, int imageHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        while (((width / inSampleSize) > imageWidth)
                || ((height / inSampleSize) > imageHeight)) {
            //建议为2的幂
            inSampleSize *= 2;
        }
        return inSampleSize;
    }

    public static final class Config {
        private File sourceFile;
        private int maxSize;
        private int maxWidth;
        private int maxHeight;
        private Bitmap.CompressFormat compressFormat;
        private int quality;
        private File outputDir;
        private String fileName;

        private Config(Builder builder) {
            sourceFile = builder.sourceFile;
            maxSize = builder.maxSize;
            maxWidth = builder.maxWidth;
            maxHeight = builder.maxHeight;
            compressFormat = builder.compressFormat;
            quality = builder.quality;
            outputDir = builder.outputDir;
            fileName = builder.fileName;
        }

        public static final class Builder {
            private File sourceFile = null;
            private int maxSize = 256 * 1024;
            private int maxWidth = 720;
            private int maxHeight = 720;
            private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
            private int quality = 0;
            private File outputDir = null;
            private String fileName = null;

            public Builder(@NonNull File sourceFile) {
                this.sourceFile = sourceFile;
            }

            /**
             * {@link Builder#quality} <=0 && {@link Builder#maxSize} >0)时高压模式 , 不然就仅压缩一次
             *
             * @param maxSize 默认 :  256Kb
             * @return
             */
            public Builder maxSize(int maxSize) {
                this.maxSize = maxSize;
                return this;
            }

            /**
             * 图片的最大宽
             *
             * @param maxWidth 默认 720
             * @return
             */
            public Builder maxWidth(@IntRange(from = 0) int maxWidth) {
                this.maxWidth = maxWidth;
                return this;
            }

            /**
             * 图片的最大高
             *
             * @param maxHeight 默认 720
             * @return
             */
            public Builder maxHeight(@IntRange(from = 0) int maxHeight) {
                this.maxHeight = maxHeight;
                return this;
            }

            /**
             * 输出格式
             *
             * @param compressFormat 默认{@link  Bitmap.CompressFormat#JPEG}
             * @return
             */
            public Builder compressFormat(@NonNull Bitmap.CompressFormat compressFormat) {
                this.compressFormat = compressFormat;
                return this;
            }

            /**
             * {@link Builder#quality} <=0 && {@link Builder#maxSize} >0)时高压模式 , 不然就仅压缩一次
             *
             * @param quality 默认-1
             * @return
             */
            public Builder quality(@IntRange(from = 0, to = 100) int quality) {
                this.quality = quality;
                return this;
            }

            /**
             * 输出路径
             *
             * @param outputDir
             * @return
             */
            public Builder outputDir(@NonNull File outputDir) {
                this.outputDir = outputDir;
                return this;
            }

            /**
             * 输出文件名
             *
             * @param fileName 默认 : 时间戳
             * @return
             */
            public Builder fileName(@NonNull String fileName) {
                this.fileName = fileName;
                return this;
            }

            public Config build() {
                return new Config(this);
            }
        }
    }

}