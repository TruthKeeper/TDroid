package com.tk.tdroid.image.load;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.model.GlideUrl;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static android.widget.ImageView.ScaleType.FIT_CENTER;
import static com.tk.tdroid.image.load.ImageRequest.Priority.*;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/12/22
 *     desc   : 图片加载配置
 * </pre>
 */
public class ImageRequest {
    @IntDef({IMMEDIATE, HIGH, NORMAL, LOW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Priority {
        int IMMEDIATE = 0;
        int HIGH = 1;
        int NORMAL = 2;
        int LOW = 3;
    }

    private ImageRequest(Builder builder) {
        context = builder.context;
        width = builder.width;
        height = builder.height;
        scaleType = builder.scaleType;
        diskCache = builder.diskCache;
        memoryCache = builder.memoryCache;
        error = builder.error;
        placeholder = builder.placeholder;
        errorId = builder.errorId;
        placeholderId = builder.placeholderId;
        asBitmap = builder.asBitmap;
        priority = builder.priority;
        file = builder.file;
        glideUrl = builder.glideUrl;
        path = builder.path;
        uri = builder.uri;
        resId = builder.resId;
        netCrop = builder.netCrop;
        autoFitImageView = builder.autoFitImageView;
    }

    private Context context;
    private int width;
    private int height;
    private ImageView.ScaleType scaleType;

    private boolean diskCache;
    private boolean memoryCache;

    private Drawable error;
    private Drawable placeholder;
    private int errorId;
    private int placeholderId;

    private boolean asBitmap;
    private int priority;

    private File file;
    private GlideUrl glideUrl;
    private String path;
    private Uri uri;
    private int resId;

    private ICrop netCrop;
    private boolean autoFitImageView;

    public Context getContext() {
        return context;
    }

    public ICrop getNetCrop() {
        return netCrop;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageView.ScaleType getScaleType() {
        return scaleType;
    }

    public boolean isDiskCache() {
        return diskCache;
    }

    public boolean isMemoryCache() {
        return memoryCache;
    }

    public Drawable getError() {
        return error;
    }

    public Drawable getPlaceholder() {
        return placeholder;
    }

    public int getErrorId() {
        return errorId;
    }

    public int getPlaceholderId() {
        return placeholderId;
    }

    public boolean isAsBitmap() {
        return asBitmap;
    }

    public int getPriority() {
        return priority;
    }

    public File getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

    public Uri getUri() {
        return uri;
    }

    public int getResId() {
        return resId;
    }

    public GlideUrl getGlideUrl() {
        return glideUrl;
    }

    public boolean isAutoFitImageView() {
        return autoFitImageView;
    }

    public static final class Builder {
        private Context context;
        private int width = 0;
        private int height = 0;
        private ImageView.ScaleType scaleType = CENTER_CROP;
        private boolean diskCache = true;
        private boolean memoryCache = true;
        private Drawable error = null;
        private Drawable placeholder = null;
        private int errorId = 0;
        private int placeholderId = 0;
        private boolean asBitmap = true;
        private int priority = NORMAL;
        private File file = null;
        private GlideUrl glideUrl = null;
        private String path = null;
        private Uri uri = null;
        private int resId = 0;
        private ICrop netCrop = null;
        private boolean autoFitImageView = true;

        public Builder(@Nullable Context context) {
            this.context = context;
        }

        /**
         * Bitmap的宽高
         *
         * @param width
         * @param height
         * @return
         */
        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * 启用url转换裁剪压缩
         *
         * @param netCrop
         * @return
         */
        public Builder netCrop(ICrop netCrop) {
            this.netCrop = netCrop;
            return this;
        }

        /**
         * 调整ScaleType 默认CENTER_CROP
         *
         * @return
         */
        public Builder centerCenter() {
            this.scaleType = CENTER_CROP;
            return this;
        }

        /**
         * 调整ScaleType 默认CENTER_CROP
         *
         * @return
         */
        public Builder fitCenter() {
            this.scaleType = FIT_CENTER;
            return this;
        }

        /**
         * 是否开启磁盘缓存
         *
         * @param diskCache 默认开启
         * @return
         */
        public Builder diskCache(boolean diskCache) {
            this.diskCache = diskCache;
            return this;
        }

        /**
         * 是否开启内存缓存
         *
         * @param memoryCache 默认开启
         * @return
         */
        public Builder memoryCache(boolean memoryCache) {
            this.memoryCache = memoryCache;
            return this;
        }

        /**
         * 错误时的资源
         *
         * @param error
         * @return
         */
        public Builder error(@Nullable Drawable error) {
            this.error = error;
            return this;
        }

        /**
         * 错误时的资源
         *
         * @param errorId
         * @return
         */
        public Builder error(@DrawableRes int errorId) {
            this.errorId = errorId;
            return this;
        }

        /**
         * 占位的资源
         *
         * @param placeholder
         * @return
         */
        public Builder placeholder(@Nullable Drawable placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        /**
         * 占位的资源
         *
         * @param placeholderId
         * @return
         */
        public Builder placeholder(@DrawableRes int placeholderId) {
            this.placeholderId = placeholderId;
            return this;
        }

        /**
         * 是否作为静态Bitmap
         *
         * @param asBitmap 默认 true 支持GIF时设置为false
         * @return
         */
        public Builder asBitmap(boolean asBitmap) {
            this.asBitmap = asBitmap;
            return this;
        }

        /**
         * 加载优先级
         *
         * @param priority
         * @return
         */
        public Builder priority(@Priority int priority) {
            this.priority = priority;
            return this;
        }

        /**
         * 自动根据ImageView的宽高来配置Bitmap的大小以减少内存占用 , 优先级低于{@link Builder#size(int, int)}
         *
         * @param autoFitImageView 默认 true
         * @return
         */
        public Builder autoFitImageView(boolean autoFitImageView) {
            this.autoFitImageView = autoFitImageView;
            return this;
        }

        /**
         * 加载
         *
         * @param file 文件全路径
         * @return
         */
        public Builder load(@NonNull File file) {
            this.file = file;
            return this;
        }

        /**
         * 加载
         *
         * @param path 链接 or 文件全路径
         * @return
         */
        public Builder load(@NonNull String path) {
            this.path = path;
            return this;
        }

        /**
         * 加载
         *
         * @param uri 链接
         * @return
         */
        public Builder load(@NonNull Uri uri) {
            this.uri = uri;
            return this;
        }

        /**
         * 加载
         *
         * @param resId 资源Id SVG资源会有问题
         * @return
         */
        public Builder load(@DrawableRes int resId) {
            this.resId = resId;
            return this;
        }

        /**
         * 加载
         *
         * @param resId 资源Id
         * @return
         */
        public Builder load(@NonNull GlideUrl glideUrl) {
            this.glideUrl = glideUrl;
            return this;
        }

        /**
         * 加载
         *
         * @param imageView
         */
        public void into(@NonNull ImageView imageView) {
            ImageLoader.load(imageView, new ImageRequest(this));
        }

        /**
         * Bitmap 加载回调
         *
         * @param callback
         */
        public void into(@NonNull LoaderCallback callback) {
            ImageLoader.load(callback, new ImageRequest(this));
        }
    }
}
