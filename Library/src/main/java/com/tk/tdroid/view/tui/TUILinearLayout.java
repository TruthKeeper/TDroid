package com.tk.tdroid.view.tui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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

            typedArray.recycle();
        }
    }

    public TUIHelper<IView> getUiHelper() {
        return uiHelper;
    }

    @Override
    public void updateBackground(@NonNull Drawable bg) {
        setBackground(bg);
    }
}
