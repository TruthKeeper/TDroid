package com.tk.tdroid.view.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/20
 *      desc : 随条目高度自适应
 * </pre>
 */

public class FitItemViewPager extends DisableViewPager {
    public FitItemViewPager(Context context) {
        this(context, null);
    }

    public FitItemViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDisabled(false);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                requestLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 设置View最作为Adapter
     *
     * @param adapter
     */
    public void setViewAdapter(AbsViewAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getAdapter() != null && getAdapter().getCount() > 0) {
            int height;
            View view = null;
            if (getAdapter() instanceof AbsViewAdapter) {
                view = (View) getAdapter().instantiateItem(this, getCurrentItem());
            } else if (getAdapter() instanceof FragmentPagerAdapter) {
                Fragment fragment = ((FragmentPagerAdapter) getAdapter()).getItem(getCurrentItem());
                if (fragment.getView() == null) {
                    view = fragment.onCreateView(LayoutInflater.from(getContext()), this, null);
                } else {
                    view = fragment.getView();
                }
            } else if (getAdapter() instanceof FragmentStatePagerAdapter) {
                Fragment fragment = ((FragmentStatePagerAdapter) getAdapter()).getItem(getCurrentItem());
                if (fragment.getView() == null) {
                    view = fragment.onCreateView(LayoutInflater.from(getContext()), this, null);
                } else {
                    view = fragment.getView();
                }
            }
            if (view != null) {
                if (view.getLayoutParams() != null && view.getLayoutParams().height > 0) {
                    height = view.getLayoutParams().height;
                } else {
                    view.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    height = view.getMeasuredHeight();
                }
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public static abstract class AbsViewAdapter extends PagerAdapter {

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View item = getItem(container, position);
            boolean reMeasure = false;
            for (int i = 0; i < container.getChildCount(); i++) {
                if (container.getChildAt(i) == item) {
                    reMeasure = true;
                    break;
                }
            }
            if (!reMeasure) {
                container.addView(item);
            }
            return item;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        public abstract View getItem(ViewGroup container, int position);
    }
}
