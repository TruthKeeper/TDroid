package com.tk.tdroid.view.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tk.tdroid.utils.EmptyUtils;
import com.tk.tdroid.utils.ScreenUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/8/6
 *     desc   : 仿小红书的ViewPager
 * </pre>
 */
public class ImageViewPager extends ViewPager {
    private final SparseIntArray childHeight = new SparseIntArray();
    private int defaultHeight;

    public ImageViewPager(@NonNull Context context) {
        this(context, null);
    }

    public ImageViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (EmptyUtils.isEmpty(childHeight)
                        || position < 0
                        || position == childHeight.size() - 1
                        || defaultHeight == 0) {
                    return;
                }
                //计算ViewPager即将变化到的高度
                int currentPageHeight = childHeight.get(position);
                int nextPageHeight = childHeight.get(position + 1);
                if (currentPageHeight <= 0 || nextPageHeight <= 0) {
                    return;
                }
                int height = (int) (currentPageHeight * (1 - positionOffset)
                        + (nextPageHeight) * positionOffset);

                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = height;
                setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 绑定资源的宽高
     *
     * @param position
     * @param imageWidth
     * @param imageHeight
     */
    public void bindImage(int position, int imageWidth, int imageHeight) {
        float scale = (float) imageHeight / imageWidth;
        int height = (int) (scale * ScreenUtils.getScreenWidth());
        height = Math.max(height, 0);
        childHeight.put(position, height);
        if (position == 0 && defaultHeight == 0) {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = height;
            defaultHeight = height;
            setLayoutParams(params);
        }
    }

    /**
     * 设置数据源
     *
     * @param urls
     * @param loadImageListener
     */
    public void setDataByUrl(List<String> urls, OnLoadImageListener<String> loadImageListener) {
        setAdapter(new ImageAdapter<String>(urls, loadImageListener));
    }

    /**
     * 设置数据源
     *
     * @param data
     * @param loadImageListener
     */
    public <D> void setData(List<D> data, OnLoadImageListener<D> loadImageListener) {
        setAdapter(new ImageAdapter<D>(data, loadImageListener));
    }

    private static class ImageAdapter<T> extends PagerAdapter {
        private final LinkedList<ImageView> mCacheList = new LinkedList<>();
        private final OnLoadImageListener<T> loadImageListener;
        private final List<T> mData;

        ImageAdapter(List<T> data, OnLoadImageListener<T> loadImageListener) {
            this.loadImageListener = loadImageListener;
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView;
            if (mCacheList.isEmpty()) {
                imageView = new ImageView(container.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                //从缓存集合中取
                imageView = mCacheList.removeFirst();
            }
            if (loadImageListener != null) {
                loadImageListener.onLoadImage(imageView, mData.get(position), position);
            }
            imageView.setLayoutParams(new ViewPager.LayoutParams());
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            mCacheList.add((ImageView) object);
        }

    }

    public interface OnLoadImageListener<T> {
        void onLoadImage(ImageView imageView, T data, int position);
    }
}
