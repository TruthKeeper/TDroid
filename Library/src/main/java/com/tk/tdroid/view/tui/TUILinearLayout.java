package com.tk.tdroid.view.tui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.tk.tdroid.R;


/**
 * <pre>
 *      author : TK
 *      time : 2017/12/26
 *      desc :
 * </pre>
 */

public class TUILinearLayout extends LinearLayout implements IView {
    private TUIHelper<IView> uiHelper;
    private int dividerSize = 0;
    private int dividerColor = Color.TRANSPARENT;
    private int dividerBgColor = Color.TRANSPARENT;
    private int dividerPaddingStart = 0;
    private int dividerPaddingEnd = 0;

    public TUILinearLayout(Context context) {
        super(context);
        initAttr(null);
    }

    public TUILinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public TUILinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TUILinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(attrs);
    }

    private void initAttr(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            uiHelper = new TUIHelper.Builder<IView>(this, null).build();
        } else {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TUILinearLayout);
            uiHelper = new TUIHelper.Builder<IView>(this, typedArray)
                    .bgSolidDefault(R.styleable.TUILinearLayout_tui_bg_solid_default)
                    .bgSolidPressed(R.styleable.TUILinearLayout_tui_bg_solid_pressed)
                    .bgSolidSelected(R.styleable.TUILinearLayout_tui_bg_solid_selected)
                    .bgSolidFocused(R.styleable.TUILinearLayout_tui_bg_solid_focused)
                    .bgSolidDisabled(R.styleable.TUILinearLayout_tui_bg_solid_disabled)

                    .bgStrokeDefault(R.styleable.TUILinearLayout_tui_bg_stroke_default)
                    .bgStrokePressed(R.styleable.TUILinearLayout_tui_bg_stroke_pressed)
                    .bgStrokeSelected(R.styleable.TUILinearLayout_tui_bg_stroke_selected)
                    .bgStrokeFocused(R.styleable.TUILinearLayout_tui_bg_stroke_focused)
                    .bgStrokeDisabled(R.styleable.TUILinearLayout_tui_bg_stroke_disabled)

                    .bgStrokeColorDefault(R.styleable.TUILinearLayout_tui_bg_strokeColor_default)
                    .bgStrokeColorPressed(R.styleable.TUILinearLayout_tui_bg_strokeColor_pressed)
                    .bgStrokeColorSelected(R.styleable.TUILinearLayout_tui_bg_strokeColor_selected)
                    .bgStrokeColorFocused(R.styleable.TUILinearLayout_tui_bg_strokeColor_focused)
                    .bgStrokeColorDisabled(R.styleable.TUILinearLayout_tui_bg_strokeColor_disabled)

                    .bgCorner(R.styleable.TUILinearLayout_tui_bg_corner)
                    .bgCornerTopLeft(R.styleable.TUILinearLayout_tui_bg_corner_topLeft)
                    .bgCornerTopRight(R.styleable.TUILinearLayout_tui_bg_corner_topRight)
                    .bgCornerBottomRight(R.styleable.TUILinearLayout_tui_bg_corner_bottomRight)
                    .bgCornerBottomLeft(R.styleable.TUILinearLayout_tui_bg_corner_bottomLeft)

                    .bgRipplePressed(R.styleable.TUILinearLayout_tui_bg_ripplePressed)

                    .bgGradientStartColorDefault(R.styleable.TUILinearLayout_tui_bg_gradientStartColor_default)
                    .bgGradientStartColorPressed(R.styleable.TUILinearLayout_tui_bg_gradientStartColor_pressed)
                    .bgGradientStartColorSelected(R.styleable.TUILinearLayout_tui_bg_gradientStartColor_selected)
                    .bgGradientStartColorFocused(R.styleable.TUILinearLayout_tui_bg_gradientStartColor_focused)
                    .bgGradientStartColorDisabled(R.styleable.TUILinearLayout_tui_bg_gradientStartColor_disabled)

                    .bgGradientCenterColorDefault(R.styleable.TUILinearLayout_tui_bg_gradientCenterColor_default)
                    .bgGradientCenterColorPressed(R.styleable.TUILinearLayout_tui_bg_gradientCenterColor_pressed)
                    .bgGradientCenterColorSelected(R.styleable.TUILinearLayout_tui_bg_gradientCenterColor_selected)
                    .bgGradientCenterColorFocused(R.styleable.TUILinearLayout_tui_bg_gradientCenterColor_focused)
                    .bgGradientCenterColorDisabled(R.styleable.TUILinearLayout_tui_bg_gradientCenterColor_disabled)

                    .bgGradientEndColorDefault(R.styleable.TUILinearLayout_tui_bg_gradientEndColor_default)
                    .bgGradientEndColorPressed(R.styleable.TUILinearLayout_tui_bg_gradientEndColor_pressed)
                    .bgGradientEndColorSelected(R.styleable.TUILinearLayout_tui_bg_gradientEndColor_selected)
                    .bgGradientEndColorFocused(R.styleable.TUILinearLayout_tui_bg_gradientEndColor_focused)
                    .bgGradientEndColorDisabled(R.styleable.TUILinearLayout_tui_bg_gradientEndColor_disabled)

                    .bgGradientDirectionDefault(R.styleable.TUILinearLayout_tui_bg_gradientDirection_default)
                    .bgGradientDirectionPressed(R.styleable.TUILinearLayout_tui_bg_gradientDirection_pressed)
                    .bgGradientDirectionSelected(R.styleable.TUILinearLayout_tui_bg_gradientDirection_selected)
                    .bgGradientDirectionFocused(R.styleable.TUILinearLayout_tui_bg_gradientDirection_focused)
                    .bgGradientDirectionDisabled(R.styleable.TUILinearLayout_tui_bg_gradientDirection_disabled)
                    .build();

            uiHelper.updateBackground();

            dividerSize = typedArray.getDimensionPixelOffset(R.styleable.TUILinearLayout_tui_dividerSize, 0);
            dividerColor = typedArray.getColor(R.styleable.TUILinearLayout_tui_dividerColor, Color.TRANSPARENT);
            dividerBgColor = typedArray.getColor(R.styleable.TUILinearLayout_tui_dividerBgColor, Color.TRANSPARENT);
            dividerPaddingStart = typedArray.getDimensionPixelOffset(R.styleable.TUILinearLayout_tui_dividerPaddingStart, 0);
            dividerPaddingEnd = typedArray.getDimensionPixelOffset(R.styleable.TUILinearLayout_tui_dividerPaddingEnd, 0);
            typedArray.recycle();

            generateDivider();
        }
    }

    private void generateDivider() {
        int orientation = getOrientation();
        if (dividerSize <= 0) {
            super.setDividerDrawable(null);
            return;
        }
        GradientDrawable[] layers = new GradientDrawable[2];
        layers[0] = new GradientDrawable();
        layers[0].setSize(orientation == HORIZONTAL ? dividerSize : 0,
                orientation == HORIZONTAL ? 0 : dividerSize);
        layers[0].setColor(dividerBgColor);
        layers[1] = new GradientDrawable();
        layers[1].setSize(orientation == HORIZONTAL ? dividerSize : 0,
                orientation == HORIZONTAL ? 0 : dividerSize);
        layers[1].setColor(dividerColor);

        LayerDrawable drawable = new LayerDrawable(layers);
        drawable.setLayerInset(1,
                orientation == HORIZONTAL ? 0 : dividerPaddingStart,
                orientation == HORIZONTAL ? dividerPaddingStart : 0,
                orientation == HORIZONTAL ? 0 : dividerPaddingEnd,
                orientation == HORIZONTAL ? dividerPaddingEnd : 0);

        super.setDividerDrawable(drawable);
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        generateDivider();
    }

    public TUIHelper<IView> getUiHelper() {
        return uiHelper;
    }


    @Override
    public void updateBackground(@NonNull Drawable bg) {
        setBackground(bg);
    }

    @Override
    public void setDividerDrawable(Drawable divider) {
    }

    @Override
    public void setDividerPadding(int padding) {
    }
}
