package com.tk.tdroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tk.tdroid.R;
import com.tk.tdroid.utils.DensityUtil;
import com.tk.tdroid.utils.DrawableUtils;


/**
 * <pre>
 *     author : TK
 *     time   : 2018/11/21
 *     desc   : 自定义{@link android.widget.RatingBar}
 *     <ol>
 *         <li>支持间距控制</li>
 *     </ol>
 * </pre>
 */
public class TRatingbar extends View {
    private static final PorterDuffXfermode MODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
    /**
     * 星星的宽高,默认:wrap_content
     */
    private int mStarSize = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 星星数量
     */
    private int mStarSum = 5;
    /**
     * 高亮的数量
     */
    private float mStarRating = 0;
    /**
     * 星星的间距
     */
    private int mStarPadding = DensityUtil.dp2px(5);
    /**
     * 默认的Drawable
     */
    private Drawable mStarHintDrawable;
    /**
     * 高亮的Drawable
     */
    private Drawable mStarHighDrawable;
    /**
     * 是否作为指示器，不响应点击事件
     */
    private boolean mIsIndicator = true;

    private Bitmap mStarHintBitmap;
    private Bitmap mStarHighBitmap;
    private Paint mPaint = new Paint();
    private Rect mRect = new Rect();
    private OnRatingChangeListener onRatingChangeListener;

    public TRatingbar(Context context) {
        super(context);
        init();
    }

    public TRatingbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TRatingbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TRatingbar);
        mStarSize = array.getDimensionPixelOffset(R.styleable.TRatingbar_tr_starSize, mStarSize);
        mStarRating = array.getFloat(R.styleable.TRatingbar_tr_starRating, mStarRating);
        mStarSum = array.getInteger(R.styleable.TRatingbar_tr_starSum, mStarSum);
        mStarPadding = array.getDimensionPixelOffset(R.styleable.TRatingbar_tr_starPadding, mStarPadding);
        mStarHintDrawable = DrawableUtils.getTintDrawable(array, R.styleable.TRatingbar_tr_starHint);
        mStarHighDrawable = DrawableUtils.getTintDrawable(array, R.styleable.TRatingbar_tr_starHigh);
        mIsIndicator = array.getBoolean(R.styleable.TRatingbar_tr_isIndicator, mIsIndicator);

        if (mStarHintDrawable == null || mStarHighDrawable == null) {
            throw new NullPointerException("StarDrawable is Null !");
        }
        if (mStarSize == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mStarSize = Math.min(Math.min(mStarHintDrawable.getIntrinsicWidth(), mStarHintDrawable.getIntrinsicHeight()),
                    Math.min(mStarHighDrawable.getIntrinsicWidth(), mStarHighDrawable.getIntrinsicHeight()));
        }
        mStarSum = Math.max(mStarSum, 1);
        mStarRating = Math.min(mStarSum, Math.max(mStarRating, 0));

        mStarHintBitmap = drawableToBitmap(mStarHintDrawable, mStarSize, mStarSize, mStarSize, mStarSize);
        mStarHighBitmap = drawableToBitmap(mStarHighDrawable, mStarSize, mStarSize, mStarSize, mStarSize);

        array.recycle();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setXfermode(MODE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mStarSum; i++) {
            if (mStarRating > i) {
                if (mStarRating < i + 1) {
                    //半星
                    mRect.top = getPaddingTop();
                    mRect.bottom = mStarHintBitmap.getHeight() + mRect.top;
                    mRect.left = i * (mStarHintBitmap.getWidth() + mStarPadding);
                    mRect.right = (i + 1) * mStarHintBitmap.getWidth() + i * mStarPadding;
                    canvas.drawBitmap(mStarHintBitmap, null, mRect, mPaint);

                    Bitmap off = drawableToBitmap(mStarHighDrawable, (int) ((mStarRating - i) * mStarSize), mStarSize, mStarSize, mStarSize);
                    mRect.top = getPaddingTop();
                    mRect.bottom = mStarHintBitmap.getHeight() + mRect.top;
                    mRect.left = i * (mStarHintBitmap.getWidth() + mStarPadding);
                    mRect.right = (int) (mStarRating * mStarHintBitmap.getWidth() + i * mStarPadding);
                    canvas.drawBitmap(off, null, mRect, mPaint);
                } else {
                    //全星
                    mRect.top = getPaddingTop();
                    mRect.bottom = mStarHintBitmap.getHeight() + mRect.top;
                    mRect.left = i * (mStarHintBitmap.getWidth() + mStarPadding);
                    mRect.right = (i + 1) * mStarHintBitmap.getWidth() + i * mStarPadding;
                    canvas.drawBitmap(mStarHighBitmap, null, mRect, null);
                }
            } else {
                //0星
                mRect.top = getPaddingTop();
                mRect.bottom = mStarHintBitmap.getHeight() + mRect.top;
                mRect.left = i * (mStarHintBitmap.getWidth() + mStarPadding);
                mRect.right = (i + 1) * mStarHintBitmap.getWidth() + i * mStarPadding;
                canvas.drawBitmap(mStarHintBitmap, null, mRect, null);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);

        int resultW = mStarSum * mStarSize + (mStarSum - 1) * mStarPadding;
        int resultH = mStarSize;
        setMeasuredDimension(modeW == MeasureSpec.EXACTLY ? sizeW : resultW + getPaddingLeft() + getPaddingRight(),
                modeH == MeasureSpec.EXACTLY ? sizeH : resultH + getPaddingTop() + getPaddingBottom());
    }


    /**
     * 设置几颗星
     *
     * @param rating
     */
    public void setRating(float rating) {
        if (rating != mStarRating && rating >= 0 && rating <= mStarSum) {
            mStarRating = rating;
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsIndicator) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                if (x < getPaddingLeft() || x > getWidth() || y < getPaddingTop() || y > getHeight()) {
                    return false;
                }
                float rating = (x - getPaddingLeft()) / (mStarSize + mStarPadding);
                int r;
                if (rating * 10 % 10 != 0) {
                    r = (int) (rating + 1);
                } else {
                    r = (int) rating;
                }
                //不允许0星
                if (r == 0) {
                    r++;
                } else if (r > mStarSum) {
                    r = mStarSum;
                }
                setRating(r);
                if (onRatingChangeListener != null) {
                    onRatingChangeListener.onRating(r);
                }

                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 裁剪
     *
     * @param drawable
     * @param canvasW
     * @param canvasH
     * @param drawableW
     * @param drawableH
     * @return
     */
    private static Bitmap drawableToBitmap(Drawable drawable, int canvasW, int canvasH,
                                           int drawableW, int drawableH) {
        Bitmap bitmap = Bitmap.createBitmap(canvasW, canvasH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawableW, drawableH);
        drawable.draw(canvas);
        return bitmap;
    }

    public interface OnRatingChangeListener {
        /**
         * 选中了
         *
         * @param ratingScore
         */
        void onRating(int ratingScore);
    }

    /**
     * 设置监听器
     *
     * @param onRatingChangeListener
     */
    public void setOnRatingListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    public float getRating() {
        return mStarRating;
    }
}
