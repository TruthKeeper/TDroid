package com.tk.tdroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.tk.tdroid.R;
import com.tk.tdroid.utils.DensityUtil;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/8/8
 *     desc   : 密码输入框
 * </pre>
 */
public class PasswordInputView extends AppCompatEditText {
    public static final int UNDERLINE_STYLE = 0;

    public static final int POINT_COLOR_DEFAULT = Color.BLACK;
    public static final int POINT_RADIUS_DEFAULT = 4;
    public static final int COUNT_DEFAULT = 6;

    public static final int UNDERLINE_COLOR_DEFAULT = Color.GRAY;
    public static final int UNDERLINE_THICKNESS_DEFAULT = 2;

    public static final int RECT_CORNER_DEFAULT = 2;
    public static final int RECT_STROKE_DEFAULT = 1;
    public static final int RECT_STROKE_COLOR_DEFAULT = Color.GRAY;

    private Paint mPaint = new Paint();
    private RectF mRect = new RectF();
    private DrawStyle mDrawStyle;
    private int mCount;
    private OnInputCompleteListener onInputCompleteListener;

    public PasswordInputView(Context context) {
        this(context, null);
    }

    public PasswordInputView(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public PasswordInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(attrs);
        init();
        setBackground(null);
        setCursorVisible(false);
        setSingleLine(true);
        setLongClickable(false);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == mCount && onInputCompleteListener != null) {
                    onInputCompleteListener.onInputComplete(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(null);
    }

    @Override
    public void setBackgroundColor(int color) {
    }

    @Override
    public void setBackgroundResource(int resId) {
    }

    @Override
    public void setLongClickable(boolean longClickable) {
        super.setLongClickable(false);
    }

    private void initAttributeSet(AttributeSet attrs) {
        int defaultRadius = DensityUtil.dp2px(getContext(), POINT_RADIUS_DEFAULT);
        if (attrs == null) {
            mCount = COUNT_DEFAULT;
            mDrawStyle = new UnderlineStyle(mRect,
                    POINT_COLOR_DEFAULT,
                    defaultRadius,
                    COUNT_DEFAULT,
                    UNDERLINE_COLOR_DEFAULT,
                    UNDERLINE_THICKNESS_DEFAULT);
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(COUNT_DEFAULT)});
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PasswordInputView);
        int style = typedArray.getInt(R.styleable.PasswordInputView_ppv_style, UNDERLINE_STYLE);
        int pointColor = typedArray.getColor(R.styleable.PasswordInputView_ppv_pointColor, POINT_COLOR_DEFAULT);
        int pointRadius = typedArray.getDimensionPixelOffset(R.styleable.PasswordInputView_ppv_pointRadius,
                defaultRadius);
        mCount = typedArray.getInt(R.styleable.PasswordInputView_ppv_count, COUNT_DEFAULT);
        if (style == UNDERLINE_STYLE) {
            mDrawStyle = new UnderlineStyle(mRect,
                    pointColor,
                    pointRadius,
                    mCount,
                    typedArray.getColor(R.styleable.PasswordInputView_ppv_underlineColor, UNDERLINE_COLOR_DEFAULT),
                    typedArray.getDimensionPixelOffset(R.styleable.PasswordInputView_ppv_underlineThickness, DensityUtil.dp2px(getContext(), UNDERLINE_THICKNESS_DEFAULT)));
        } else {
            mDrawStyle = new RectStyle(mRect,
                    pointColor,
                    pointRadius,
                    mCount,
                    typedArray.getDimensionPixelOffset(R.styleable.PasswordInputView_ppv_rectCorner, DensityUtil.dp2px(getContext(), RECT_CORNER_DEFAULT)),
                    typedArray.getDimensionPixelOffset(R.styleable.PasswordInputView_ppv_rectStroke, DensityUtil.dp2px(getContext(), RECT_STROKE_DEFAULT)),
                    typedArray.getColor(R.styleable.PasswordInputView_ppv_rectStrokeColor, RECT_STROKE_COLOR_DEFAULT));
        }
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mCount)});
        typedArray.recycle();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawStyle != null) {
            mDrawStyle.draw(canvas, mPaint, getText().length());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect.set(0, 0, w, h);
    }

    public void setOnInputCompleteListener(OnInputCompleteListener onInputCompleteListener) {
        this.onInputCompleteListener = onInputCompleteListener;
    }

    public static abstract class DrawStyle {
        RectF rectF;
        int pointColor;
        int pointRadius;
        int count;

        DrawStyle(RectF rectF, int pointColor, int pointRadius, int count) {
            this.rectF = rectF;
            this.pointColor = pointColor;
            this.pointRadius = pointRadius;
            this.count = count;
        }

        public abstract void draw(Canvas canvas, Paint paint, int textLength);
    }

    public static class UnderlineStyle extends DrawStyle {
        int underlineColor;
        int underlineThickness;

        UnderlineStyle(RectF rectF, int pointColor, int pointRadius, int count, int underlineColor, int underlineThickness) {
            super(rectF, pointColor, pointRadius, count);
            this.underlineColor = underlineColor;
            this.underlineThickness = underlineThickness;
        }

        @Override
        public void draw(Canvas canvas, Paint paint, int textLength) {
            float underlineWidth = rectF.width() / (count + 2);
            float padding = (rectF.width() - count * underlineWidth) / (count - 1);
            paint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < count; i++) {
                if (i < textLength) {
                    paint.setColor(pointColor);
                    canvas.drawCircle(underlineWidth / 2 + i * (underlineWidth + padding),
                            rectF.height() / 2,
                            pointRadius,
                            paint);
                }
                paint.setColor(underlineColor);
                canvas.drawRect(i * (underlineWidth + padding),
                        rectF.height() - underlineThickness,
                        underlineWidth + i * (underlineWidth + padding),
                        rectF.height(),
                        paint);
            }
        }
    }

    public static class RectStyle extends DrawStyle {
        int rectCorner;
        int rectStroke;
        int rectStrokeColor;
        RectF roundRectF;

        RectStyle(RectF rectF, int pointColor, int pointRadius, int count, int rectCorner, int rectStroke, int rectStrokeColor) {
            super(rectF, pointColor, pointRadius, count);
            this.rectCorner = rectCorner;
            this.rectStroke = rectStroke;
            this.rectStrokeColor = rectStrokeColor;
        }

        @Override
        public void draw(Canvas canvas, Paint paint, int textLength) {
            if (roundRectF == null) {
                roundRectF = new RectF(rectF);
            } else {
                roundRectF.set(rectF);
            }
            roundRectF.inset(rectStroke / 2, rectStroke / 2);
            float unitWidth = rectF.width() / count;
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(rectStroke);
            paint.setColor(rectStrokeColor);
            canvas.drawRoundRect(roundRectF, rectCorner, rectCorner, paint);
            for (int i = 0; i < count; i++) {
                if (i < textLength) {
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(pointColor);
                    canvas.drawCircle(unitWidth / 2 + i * unitWidth,
                            rectF.height() / 2,
                            pointRadius,
                            paint);
                }
                if (i != count - 1) {
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(rectStroke);
                    paint.setColor(rectStrokeColor);
                    canvas.drawLine(unitWidth + i * unitWidth,
                            rectStroke,
                            unitWidth + i * unitWidth,
                            rectF.height() - rectStroke,
                            paint);
                }
            }
        }
    }

    public interface OnInputCompleteListener {
        void onInputComplete(String passwordStr);
    }
}
