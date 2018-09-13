package com.tk.tdroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tk.tdroid.R;
import com.tk.tdroid.utils.DensityUtil;
import com.tk.tdroid.utils.EmptyUtils;

import java.util.List;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/8/29
 *     desc   : 仿微信九宫格图片
 *     <ol>
 *         <li>1张图：1行1列</li>
 *         <li>2张图：1行2列</li>
 *         <li>3张图：1行3列</li>
 *         <li>4张图：2行2列</li>
 *         <li>5张图：2行3列</li>
 *         <li>6张图：2行3列</li>
 *         <li>7张图：3行3列</li>
 *         <li>8张图：3行3列</li>
 *         <li>9张图：3行3列</li>
 *     </ol>
 * </pre>
 */
public class NineGridLayout extends ViewGroup {
    private int mRows;
    private int mColumns;
    private int mGridPadding = DensityUtil.dp2px(5);
    private float mSingleImageMinWidthPercent = 0.5F;
    private float mSingleImageMinHeightPercent = 0.5F;
    private float mSingleImageMaxWidthPercent = 1F;
    private float mSingleImageMaxHeightPercent = 1F;

    public NineGridLayout(Context context) {
        this(context, null);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridLayout);
            mGridPadding = typedArray.getDimensionPixelOffset(R.styleable.NineGridLayout_ngl_gridPadding, mGridPadding);
            mSingleImageMinWidthPercent = typedArray.getFloat(R.styleable.NineGridLayout_ngl_singleImageMinWidthPercent, mSingleImageMinWidthPercent);
            mSingleImageMinHeightPercent = typedArray.getFloat(R.styleable.NineGridLayout_ngl_singleImageMinHeightPercent, mSingleImageMinHeightPercent);
            mSingleImageMaxWidthPercent = typedArray.getFloat(R.styleable.NineGridLayout_ngl_singleImageMaxWidthPercent, mSingleImageMaxWidthPercent);
            mSingleImageMaxHeightPercent = typedArray.getFloat(R.styleable.NineGridLayout_ngl_singleImageMaxHeightPercent, mSingleImageMaxHeightPercent);

            mSingleImageMinWidthPercent = Math.min(1F, Math.max(mSingleImageMinWidthPercent, 0));
            mSingleImageMinHeightPercent = Math.min(1F, Math.max(mSingleImageMinHeightPercent, 0));
            mSingleImageMaxWidthPercent = Math.min(1F, Math.max(mSingleImageMaxWidthPercent, 0));
            mSingleImageMaxHeightPercent = Math.min(1F, Math.max(mSingleImageMaxHeightPercent, 0));
            mSingleImageMaxWidthPercent = Math.max(mSingleImageMinWidthPercent, mSingleImageMaxWidthPercent);
            mSingleImageMaxHeightPercent = Math.max(mSingleImageMinHeightPercent, mSingleImageMaxHeightPercent);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        generateChildCount(count);
        if (count == 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        View view;
        if (count == 1) {
            view = getChildAt(0);
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            int minWidth = (int) (mSingleImageMinWidthPercent * parentWidth);
            int minHeight = (int) (mSingleImageMinHeightPercent * parentWidth);
            int maxWidth = (int) (mSingleImageMaxWidthPercent * parentWidth);
            int maxHeight = (int) (mSingleImageMaxHeightPercent * parentWidth);

            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable == null) {
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                setMeasuredDimension(minWidth, minHeight);
                return;
            }
            int childWidth = drawable.getIntrinsicWidth();
            int childHeight = drawable.getIntrinsicHeight();

            if (childWidth > maxWidth) {
                //缩小
                childHeight = (int) (childHeight / 1F / childWidth * maxWidth);
                childWidth = maxWidth;
            } else if (childWidth < minWidth) {
                //放大
                childHeight = (int) (childHeight / 1F / childWidth * minWidth);
                childWidth = minWidth;
            }
            childHeight = Math.max(Math.min(childHeight, maxHeight), minHeight);

            measureChild(view,
                    MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));

            setMeasuredDimension(childWidth, childHeight);
            return;
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int unitSize = (width - 2 * mGridPadding) / 3;
        for (int i = 0; i < count; i++) {
            view = getChildAt(i);
            measureChild(view, MeasureSpec.makeMeasureSpec(unitSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(unitSize, MeasureSpec.EXACTLY));
        }

        int height = mRows * unitSize + Math.max(mRows - 1, 0) * mGridPadding;

        setMeasuredDimension(width, height);
    }

    private void generateChildCount(int childCount) {
        if (childCount <= 3) {
            mRows = 1;
            mColumns = childCount;
        } else if (childCount <= 6) {
            mRows = 2;
            if (childCount == 4) {
                mColumns = 2;
            } else {
                mColumns = 3;
            }
        } else {
            mRows = 3;
            mColumns = 3;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        if (count == 1) {
            getChildAt(0).layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
            return;
        }

        for (int i = 0; i < count; i++) {
            //取到当前处于几行几列
            int columnNum = i % mColumns;
            int rowNum = i / mColumns;
            int unitSize = (getMeasuredWidth() - 2 * mGridPadding) / 3;
            l = (unitSize + mGridPadding) * columnNum;
            t = (unitSize + mGridPadding) * rowNum;
            r = l + unitSize;
            b = t + unitSize;

            getChildAt(i).layout(l, t, r, b);
        }
    }

    /**
     * 设置内容数据
     *
     * @param data
     * @param loadListener
     * @param <T>
     */
    public <T> void setContent(final List<T> data, final OnContentListener<T> loadListener) {
        if (EmptyUtils.isEmpty(data)) {
            removeAllViews();
            return;
        }
        int count = getChildCount();
        boolean refresh = false;
        if (data.size() < count) {
            //删除
            removeViewsInLayout(data.size(), count - data.size());
            refresh = true;
        } else if (data.size() > count) {
            //增加
            int insertNum = data.size() - count;
            for (int i = 0; i < insertNum; i++) {
                addViewInLayout(generateImageView(), -1, generateDefaultLayoutParams());
                refresh = true;
            }
        }
        if (refresh) {
            requestLayout();
            invalidate();
        }
        count = getChildCount();
        for (int i = 0; i < count; i++) {
            final int position = i;
            final ImageView view = (ImageView) getChildAt(position);
            if (loadListener != null) {
                loadListener.onLoadImage(view, data.get(position), position);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadListener.onClickImage(view, data.get(position), position, data);
                    }
                });
            }
        }
    }

    protected ImageView generateImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    public interface OnContentListener<T> {
        /**
         * 加载数据 , 显式设置Drawable
         *
         * @param imageView
         * @param data
         * @param position
         */
        void onLoadImage(ImageView imageView, T data, int position);

        /**
         * 点击了ImageView
         *
         * @param imageView
         * @param data
         * @param position
         * @param list
         */
        void onClickImage(ImageView imageView, T data, int position, List<T> list);
    }
}