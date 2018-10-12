package com.tk.tdroid.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.tk.tdroid.R;
import com.tk.tdroid.utils.DensityUtil;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/10/10
 *     desc   :支持圆角，阴影颜色、扩散程度、偏移和智能取色的{@link ImageView}
 * </pre>
 */
@SuppressLint("AppCompatCustomView")
public class ShadowImageView extends ImageView implements Palette.PaletteAsyncListener {
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int SHADOW_INSET = 1;

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mShadowPaint = new Paint();

    private BitmapShader mBitmapShader;
    private Bitmap mBitmap;
    private RectF mBitmapRect;
    private RectF mShadowRect;

    private int mCorner = 0;
    private boolean mShadowEnabled = true;
    private int mShadowPadding = DensityUtil.dp2px(15);
    private boolean mShadowColorSmart = true;
    private int mShadowColor = -2;
    private int mShadowRadius = DensityUtil.dp2px(6);
    private int mShadowOffsetX = 0;
    private int mShadowOffsetY = DensityUtil.dp2px(4);
    /**
     * 智能取色的异步任务
     */
    private AsyncTask mPaletteAsyncTask;
    private boolean prepare;
    private boolean preparePending;

    public ShadowImageView(Context context) {
        super(context);
        init();
    }

    public ShadowImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowImageView);
        mCorner = typedArray.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_corner, mCorner);
        mShadowEnabled = typedArray.getBoolean(R.styleable.ShadowImageView_siv_shadowEnabled, mShadowEnabled);
        mShadowPadding = typedArray.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_shadowPadding, mShadowPadding);
        TypedValue value = new TypedValue();
        boolean has = typedArray.getValue(R.styleable.ShadowImageView_siv_shadowColor, value);
        if (has) {
            switch (value.type) {
                case TypedValue.TYPE_INT_DEC:
                    mShadowColorSmart = value.data == 1;
                    break;
                case TypedValue.TYPE_FIRST_COLOR_INT:
                case TypedValue.TYPE_INT_COLOR_RGB8:
                case TypedValue.TYPE_INT_COLOR_ARGB4:
                case TypedValue.TYPE_INT_COLOR_RGB4:
                    mShadowColorSmart = false;
                    mShadowColor = value.data;
                    break;
            }
        }

        mShadowRadius = typedArray.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_shadowRadius, mShadowRadius);
        mShadowOffsetX = typedArray.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_shadowOffsetX, mShadowOffsetX);
        mShadowOffsetY = typedArray.getDimensionPixelOffset(R.styleable.ShadowImageView_siv_shadowOffsetY, mShadowOffsetY);
        typedArray.recycle();
        if (mShadowEnabled) {
            super.setPadding(mShadowPadding, mShadowPadding, mShadowPadding, mShadowPadding);
        }
        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setAntiAlias(true);
        mShadowPaint.setDither(true);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setColor(Color.WHITE);

        prepare = true;
        if (preparePending) {
            refreshUI();
            preparePending = false;
        }

    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {

    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {

    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelShadowColorSmartThread();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }
        if (mShadowEnabled) {
            canvas.drawRoundRect(mShadowRect, mCorner, mCorner, mShadowPaint);
        }
        canvas.drawRoundRect(mBitmapRect, mCorner, mCorner, mBitmapPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mBitmapRect == null) {
            mBitmapRect = new RectF();
        }
        if (mShadowRect == null) {
            mShadowRect = new RectF();
        }
        mBitmapRect.set(mShadowPadding,
                mShadowPadding,
                w - mShadowPadding,
                h - mShadowPadding);

        mShadowRect.set(mBitmapRect);
        mShadowRect.inset(SHADOW_INSET, SHADOW_INSET);
        refreshUI();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    private void initializeBitmap() {
        mBitmap = getBitmapFromDrawable(getDrawable());
        refreshUI();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void refreshUI() {
        if (!prepare) {
            preparePending = true;
            return;
        }
        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null) {
            invalidate();
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapPaint.setShader(mBitmapShader);
        updateShaderMatrix();
        cancelShadowColorSmartThread();
        if (mShadowEnabled) {
            if (mShadowColorSmart) {
                //截取显示的BitMap区域，CenterCrop模式，优化性能
                int bitmapWidth = mBitmap.getWidth();
                int bitmapHeight = mBitmap.getHeight();
                int viewWidth = getWidth() - mShadowPadding * 2;
                int viewHeight = getHeight() - mShadowPadding * 2;
                float scale;
                float dx = 0;
                float dy = 0;

                if (bitmapWidth * viewHeight > viewWidth * bitmapHeight) {
                    scale = viewHeight / (float) bitmapHeight;
                    dx = (viewWidth - bitmapWidth * scale) * 0.5f;
                } else {
                    scale = viewWidth / (float) bitmapWidth;
                    dy = (viewHeight - bitmapHeight * scale) * 0.5f;
                }
                int regionL = (int) Math.abs(dx / scale);
                int regionT = (int) Math.abs(dy / scale);
                int regionR = bitmapWidth - (int) Math.abs(dx / scale);
                int regionB = bitmapHeight - (int) Math.abs(dy / scale);

                mPaletteAsyncTask = Palette.from(mBitmap)
                        .setRegion(regionL, regionT, regionR, regionB)
                        .generate(this);
            } else if (mShadowColor != -2) {
                mShadowPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, mShadowColor);
            }
        }
        invalidate();
    }

    private void cancelShadowColorSmartThread() {
        if (mPaletteAsyncTask != null && !mPaletteAsyncTask.isCancelled()) {
            mPaletteAsyncTask.cancel(true);
        }
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();
        int padding = mShadowEnabled ? mShadowPadding : 0;

        int viewWidth = getWidth() - padding * 2;
        int viewHeight = getHeight() - padding * 2;

        if (bitmapWidth * viewHeight > viewWidth * bitmapHeight) {
            scale = viewHeight / (float) bitmapHeight;
            dx = (viewWidth - bitmapWidth * scale) * 0.5f;
        } else {
            scale = viewWidth / (float) bitmapWidth;
            dy = (viewHeight - bitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + padding, (int) (dy + 0.5f) + padding);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    /**
     * 设置圆角
     *
     * @param corner
     */
    public void setCorner(@Px int corner) {
        this.mCorner = corner;
        refreshUI();
    }

    /**
     * 设置阴影是否显示
     *
     * @param shadowEnabled
     */
    public void setShadowEnabled(boolean shadowEnabled) {
        this.mShadowEnabled = shadowEnabled;
        if (shadowEnabled) {
            super.setPadding(mShadowPadding, mShadowPadding, mShadowPadding, mShadowPadding);
            if (mBitmapRect != null) {
                mBitmapRect.set(mShadowPadding,
                        mShadowPadding,
                        getWidth() - mShadowPadding,
                        getHeight() - mShadowPadding);

                mShadowRect.set(mBitmapRect);
                mShadowRect.inset(SHADOW_INSET, SHADOW_INSET);
            }
        } else {
            super.setPadding(0, 0, 0, 0);
            if (mBitmapRect != null) {
                mBitmapRect.set(0,
                        0,
                        getWidth(),
                        getHeight());
                mShadowRect.set(mBitmapRect);
            }
        }
        refreshUI();
    }

    /**
     * 设置为阴影预留的Padding
     *
     * @param shadowPadding
     */
    public void setShadowPadding(@Px int shadowPadding) {
        this.mShadowPadding = shadowPadding;
        if (mShadowEnabled) {
            super.setPadding(mShadowPadding, mShadowPadding, mShadowPadding, mShadowPadding);
            if (mBitmapRect != null) {
                mBitmapRect.set(mShadowPadding,
                        mShadowPadding,
                        getWidth() - mShadowPadding,
                        getHeight() - mShadowPadding);
                mShadowRect.set(mBitmapRect);
                mShadowRect.inset(SHADOW_INSET, SHADOW_INSET);
            }
        }
        refreshUI();
    }

    /**
     * 手动设置阴影颜色是否智能取色
     *
     * @param shadowColorSmart
     */
    public void setShadowColorSmart(boolean shadowColorSmart) {
        this.mShadowColorSmart = shadowColorSmart;
        refreshUI();
    }

    /**
     * 设置阴影颜色
     *
     * @param shadowColor
     */
    public void setShadowColor(@ColorInt int shadowColor) {
        this.mShadowColor = shadowColor;
        refreshUI();
    }

    /**
     * 设置阴影扩散半径
     *
     * @param shadowRadius
     */
    public void setShadowRadius(@Px int shadowRadius) {
        this.mShadowRadius = shadowRadius;
        refreshUI();
    }

    /**
     * 设置阴影x轴偏移
     *
     * @param shadowOffsetX
     */
    public void setShadowOffsetX(@Px int shadowOffsetX) {
        this.mShadowOffsetX = shadowOffsetX;
        refreshUI();
    }

    /**
     * 设置阴影y轴偏移
     *
     * @param shadowOffsetY
     */
    public void setShadowOffsetY(@Px int shadowOffsetY) {
        this.mShadowOffsetY = shadowOffsetY;
        refreshUI();
    }

    @Override
    public void onGenerated(@NonNull Palette palette) {
        if (!prepare) {
            return;
        }
        Palette.Swatch swatch = palette.getDominantSwatch();
        if (swatch == null) {
            mShadowColor = Color.GRAY;
        } else {
            mShadowColor = swatch.getRgb();
        }
        mShadowPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, mShadowColor);
        invalidate();
    }
}
