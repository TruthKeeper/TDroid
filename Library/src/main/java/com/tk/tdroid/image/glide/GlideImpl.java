package com.tk.tdroid.image.glide;

import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tk.tdroid.image.load.Config;
import com.tk.tdroid.image.load.ILoader;
import com.tk.tdroid.image.load.ImageRequest;
import com.tk.tdroid.image.load.LoaderCallback;
import com.tk.tdroid.utils.EmptyUtils;
import com.tk.tdroid.utils.Utils;

import java.util.Map;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/12/22
 *     desc   : Glide ImageLoader实现
 * </pre>
 */
public class GlideImpl implements ILoader {

    @Override
    public void init(@NonNull Config config) {
        GlideModuleImpl.init(config);
    }

    @Override
    public void load(@NonNull ImageView imageView, @NonNull ImageRequest request) {
        GlideRequests glideRequests = GlideApp.with(request.getContext() == null ? Utils.getApp() : request.getContext());
        GlideRequest glideRequest = generateGlideRequest(glideRequests, request);

        Object model = null;
        if (request.getFile() != null) {
            model = request.getFile();
        } else if (!EmptyUtils.isEmpty(request.getPath())) {
            if (request.getNetCrop() != null) {
                model = request.getNetCrop().cropPath(imageView, request.getPath());
            } else {
                model = request.getPath();
            }
        } else if (request.getUri() != null) {
            if (request.getNetCrop() != null) {
                model = request.getNetCrop().cropPath(imageView, request.getUri().getPath());
            } else {
                model = request.getUri();
            }
        } else if (request.getResId() != 0) {
            model = request.getResId();
        } else if (request.getGlideUrl() != null) {
            if (request.getNetCrop() != null) {
                String url = request.getGlideUrl().toStringUrl();
                final Map<String, String> headers = request.getGlideUrl().getHeaders();
                url = request.getNetCrop().cropPath(imageView, url);
                model = new GlideUrl(url, new Headers() {
                    @Override
                    public Map<String, String> getHeaders() {
                        return headers;
                    }
                });
            } else {
                model = request.getGlideUrl();
            }
        } else {
            return;
        }

        glideRequest = glideRequest.load(model);
        if (request.isAutoFitImageView()) {
            if (imageView.getMeasuredWidth() > 0 && imageView.getMeasuredHeight() > 0) {
                glideRequest.override(imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
            } else if (imageView.getLayoutParams() != null && imageView.getLayoutParams().width > 0 && imageView.getLayoutParams().height > 0) {
                glideRequest.override(imageView.getLayoutParams().width, imageView.getLayoutParams().height);
            }
        }
        if (request.getWidth() > 0 && request.getHeight() > 0) {
            glideRequest.override(request.getWidth(), request.getHeight());
        }

        glideRequest.into(imageView);
    }

    @Override
    public void load(@NonNull LoaderCallback callback, @NonNull ImageRequest request) {
        GlideRequests glideRequests = GlideApp.with(request.getContext() == null ? Utils.getApp() : request.getContext());
        GlideRequest glideRequest = generateGlideRequest(glideRequests, request);

        Object model = null;
        if (request.getFile() != null) {
            model = request.getFile();
        } else if (!EmptyUtils.isEmpty(request.getPath())) {
            model = request.getPath();
        } else if (request.getUri() != null) {
            model = request.getUri();
        } else if (request.getResId() != 0) {
            model = request.getResId();
        } else if (request.getGlideUrl() != null) {
            model = request.getGlideUrl();
        } else {
            return;
        }

        glideRequest = glideRequest.load(model);
        glideRequest.into(new SimpleTarget<Object>() {
            @Override
            public void onResourceReady(Object resource, Transition transition) {
                if (resource instanceof BitmapDrawable) {
                    callback.onSuccess(((BitmapDrawable) resource).getBitmap());
                } else if (resource instanceof GifDrawable) {
                    callback.onSuccess(((GifDrawable) resource).getFirstFrame());
                }
            }
        });
    }

    private GlideRequest generateGlideRequest(GlideRequests glideRequests, ImageRequest request) {
        GlideRequest glideRequest;
        if (request.isAsBitmap()) {
            glideRequest = glideRequests.asBitmap();
        } else {
            glideRequest = glideRequests.asGif();
        }
        glideRequest.placeholder(request.getPlaceholderId())
                .placeholder(request.getPlaceholder())
                .error(request.getErrorId())
                .error(request.getError())
                .skipMemoryCache(!request.isMemoryCache())
                .diskCacheStrategy(request.isDiskCache() ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE);
        if (request.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            glideRequest.centerCrop();
        } else if (request.getScaleType() == ImageView.ScaleType.FIT_CENTER) {
            glideRequest.fitCenter();
        }
        glideRequest.priority(getPriority(request.getPriority()));
        return glideRequest;
    }

    private Priority getPriority(@ImageRequest.Priority int priority) {
        switch (priority) {
            case ImageRequest.Priority.HIGH:
                return Priority.HIGH;
            case ImageRequest.Priority.IMMEDIATE:
                return Priority.IMMEDIATE;
            case ImageRequest.Priority.LOW:
                return Priority.LOW;
            case ImageRequest.Priority.NORMAL:
                return Priority.NORMAL;
        }
        return Priority.NORMAL;
    }
}