package com.tk.tdroid.widget.recycler.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tk.tdroid.widget.image.GlideApp;
import com.tk.tdroid.widget.image.NetCropUtils;

import java.io.File;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/11/6
 *     desc   : RecyclerView Holder封装
 * </pre>
 */
public class FasterHolder extends RecyclerView.ViewHolder {
    /**
     * 持有FasterAdapter的引用，考虑到业务场景也都是内部类隐式持有外部类引用
     */
    private FasterAdapter mAdapter = null;
    /**
     * 存放view对象
     */
    private SparseArray<View> mViews = null;
    /**
     * 放置额外的对象标记
     */
    private SparseArray<Object> mTags = null;

    public FasterHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    /**
     * 依附
     *
     * @param adapter
     */
    void attach(FasterAdapter adapter) {
        this.mAdapter = adapter;
    }

    /**
     * 获取持有的FasterAdapter引用
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <T> FasterAdapter<T> getAdapter() {
        return (FasterAdapter<T>) mAdapter;
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public final Context getContext() {
        return itemView.getContext();
    }

    /**
     * 获取集合中的位置
     *
     * @return
     */
    public final int getListPosition() {
        return getAdapterPosition() - getAdapter().getHeaderViewSpace();
    }

    /**
     * 通过id获取控件
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <T extends View> T findViewById(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (null == view) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置图像加载
     *
     * @param viewId
     * @param drawable
     * @return
     */
    public final FasterHolder setImage(@IdRes int viewId, Drawable drawable) {
        this.<ImageView>findViewById(viewId).setImageDrawable(drawable);
        return this;
    }

    /**
     * 设置图像加载
     *
     * @param viewId
     * @param url
     * @return
     */
    public final FasterHolder setImage(@IdRes int viewId, String url) {
        return setImage(viewId, url, false);
    }

    /**
     * 设置图像加载
     *
     * @param viewId
     * @param url
     * @param netCrop 网络库处理
     * @return
     */
    public final FasterHolder setImage(@IdRes int viewId, String url, boolean netCrop) {
        ImageView imageView = this.<ImageView>findViewById(viewId);
        GlideApp.with(itemView.getContext())
                .load(netCrop ? NetCropUtils.wrap(url, imageView) : url)
                .override(imageView)
                .into(imageView);
        return this;
    }


    /**
     * 设置图像加载
     *
     * @param viewId
     * @param url
     * @param placeholderId
     * @param errorId
     * @return
     */
    public final FasterHolder setImage(@IdRes int viewId, String url, @DrawableRes int placeholderId, @DrawableRes int errorId) {
        return setImage(viewId, url, placeholderId, errorId, false);

    }

    /**
     * 设置图像加载
     *
     * @param viewId
     * @param url
     * @param placeholderId
     * @param errorId
     * @param netCrop
     * @return
     */
    public final FasterHolder setImage(@IdRes int viewId, String url, @DrawableRes int placeholderId, @DrawableRes int errorId, boolean netCrop) {
        ImageView imageView = this.<ImageView>findViewById(viewId);
        GlideApp.with(itemView.getContext())
                .load(netCrop ? NetCropUtils.wrap(url, imageView) : url)
                .error(errorId)
                .placeholder(placeholderId)
                .override(imageView)
                .into(imageView);
        return this;
    }

    /**
     * 设置图像加载
     *
     * @param viewId
     * @param file
     * @return
     */
    public final FasterHolder setImage(@IdRes int viewId, File file) {
        ImageView imageView = this.<ImageView>findViewById(viewId);
        GlideApp.with(itemView.getContext())
                .load(file)
                .override(imageView)
                .into(imageView);
        return this;
    }

    /**
     * 设置图像
     *
     * @param viewId
     * @param resId
     * @return
     */
    public final FasterHolder setImage(@IdRes int viewId, @DrawableRes int resId) {
        this.<ImageView>findViewById(viewId).setImageResource(resId);
        return this;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param resId
     * @return
     */
    public final FasterHolder setText(@IdRes int viewId, @StringRes int resId) {
        this.<TextView>findViewById(viewId).setText(resId);
        return this;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     * @return
     */
    public final FasterHolder setText(@IdRes int viewId, CharSequence text) {
        this.<TextView>findViewById(viewId).setText(text);
        return this;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     * @param nullText
     * @return
     */
    public final FasterHolder setTextOrNull(@IdRes int viewId, CharSequence text, CharSequence nullText) {
        this.<TextView>findViewById(viewId).setText(TextUtils.isEmpty(text) ? nullText : text);
        return this;
    }

    /**
     * 设置文本颜色
     *
     * @param viewId
     * @param colorRes
     * @return
     */
    public final FasterHolder setTextColorByRes(@IdRes int viewId, @ColorRes int colorRes) {
        this.<TextView>findViewById(viewId).setTextColor(ContextCompat.getColor(itemView.getContext(), colorRes));
        return this;
    }

    /**
     * 设置文本颜色
     *
     * @param viewId
     * @param textColor
     * @return
     */
    public final FasterHolder setTextColorByInt(@IdRes int viewId, @ColorInt int textColor) {
        this.<TextView>findViewById(viewId).setTextColor(textColor);
        return this;
    }

    /**
     * 设置文本大小
     *
     * @param viewId
     * @param textSize
     * @return
     */
    public final FasterHolder setTextSize(@IdRes int viewId, float textSize) {
        this.<TextView>findViewById(viewId).setTextSize(textSize);
        return this;
    }

    /**
     * 设置进度
     *
     * @param viewId
     * @param progress
     * @return
     */
    public final FasterHolder setProgress(@IdRes int viewId, int progress) {
        this.<ProgressBar>findViewById(viewId).setProgress(progress);
        return this;
    }

    /**
     * 设置是否可见
     *
     * @param viewId
     * @param visibility
     * @return
     */
    public final FasterHolder setVisibility(@IdRes int viewId, int visibility) {
        findViewById(viewId).setVisibility(visibility);
        return this;
    }

    /**
     * 设置是否被选中
     *
     * @param viewId
     * @param checked
     * @return
     */
    public final FasterHolder setChecked(@IdRes int viewId, boolean checked) {
        this.<CompoundButton>findViewById(viewId).setChecked(checked);
        return this;
    }

    /**
     * 设置是否被选中
     *
     * @param viewId
     * @param selected
     * @return
     */
    public final FasterHolder setSelected(@IdRes int viewId, boolean selected) {
        findViewById(viewId).setSelected(selected);
        return this;
    }

    /**
     * 设置是否可用
     *
     * @param viewId
     * @param enabled
     * @return
     */
    public final FasterHolder setEnabled(@IdRes int viewId, boolean enabled) {
        findViewById(viewId).setEnabled(enabled);
        return this;
    }

    /**
     * 设置点击监听
     *
     * @param viewId
     * @param listener
     * @return
     */
    public final FasterHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        findViewById(viewId).setOnClickListener(listener);
        return this;
    }

    /**
     * 设置长点击监听
     *
     * @param viewId
     * @param listener
     * @return
     */
    public final FasterHolder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        findViewById(viewId).setOnLongClickListener(listener);
        return this;
    }

    /**
     * 设置一个额外存储Tag
     *
     * @param tagKey
     * @param tag
     */
    public final FasterHolder setTag(int tagKey, Object tag) {
        if (null == mTags) {
            mTags = new SparseArray<>(2);
        }
        mTags.put(tagKey, tag);
        return this;
    }

    /**
     * 获取额外存储Tag
     *
     * @param tagKey
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <T> T getTag(int tagKey) {
        if (null == mTags) {
            return null;
        }
        return (T) mTags.get(tagKey);
    }

    /**
     * FasterHolder创建，可以扩展用于监听点击事件等等
     */
    protected void onCreate() {
    }

    /**
     * FasterHolder视图被回收时的回调
     */
    protected void onDetach() {
    }

    /**
     * FasterHolder资源被释放的回调
     */
    protected void onRecycle() {
    }
}