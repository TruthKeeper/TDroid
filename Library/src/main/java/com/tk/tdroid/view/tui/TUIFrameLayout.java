package com.tk.tdroid.view.tui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tk.tdroid.R;


/**
 * <pre>
 *      author : TK
 *      time : 2017/12/26
 *      desc :
 * </pre>
 */

public class TUIFrameLayout extends FrameLayout implements IView {
    private TUIHelper<IView> uiHelper;

    public TUIFrameLayout(Context context) {
        super(context);
        initAttr(null);
    }

    public TUIFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public TUIFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TUIFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(attrs);
    }

    private void initAttr(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            uiHelper = new TUIHelper.Builder<IView>(this, null).build();
        } else {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TUIFrameLayout);
            uiHelper = new TUIHelper.Builder<IView>(this, typedArray)
                    .bgSolidDefault(R.styleable.TUIFrameLayout_tui_bg_solid_default)
                    .bgSolidPressed(R.styleable.TUIFrameLayout_tui_bg_solid_pressed)
                    .bgSolidSelected(R.styleable.TUIFrameLayout_tui_bg_solid_selected)
                    .bgSolidFocused(R.styleable.TUIFrameLayout_tui_bg_solid_focused)
                    .bgSolidDisabled(R.styleable.TUIFrameLayout_tui_bg_solid_disabled)

                    .bgStrokeDefault(R.styleable.TUIFrameLayout_tui_bg_stroke_default)
                    .bgStrokePressed(R.styleable.TUIFrameLayout_tui_bg_stroke_pressed)
                    .bgStrokeSelected(R.styleable.TUIFrameLayout_tui_bg_stroke_selected)
                    .bgStrokeFocused(R.styleable.TUIFrameLayout_tui_bg_stroke_focused)
                    .bgStrokeDisabled(R.styleable.TUIFrameLayout_tui_bg_stroke_disabled)

                    .bgStrokeColorDefault(R.styleable.TUIFrameLayout_tui_bg_strokeColor_default)
                    .bgStrokeColorPressed(R.styleable.TUIFrameLayout_tui_bg_strokeColor_pressed)
                    .bgStrokeColorSelected(R.styleable.TUIFrameLayout_tui_bg_strokeColor_selected)
                    .bgStrokeColorFocused(R.styleable.TUIFrameLayout_tui_bg_strokeColor_focused)
                    .bgStrokeColorDisabled(R.styleable.TUIFrameLayout_tui_bg_strokeColor_disabled)

                    .bgCorner(R.styleable.TUIFrameLayout_tui_bg_corner)
                    .bgCornerTopLeft(R.styleable.TUIFrameLayout_tui_bg_corner_topLeft)
                    .bgCornerTopRight(R.styleable.TUIFrameLayout_tui_bg_corner_topRight)
                    .bgCornerBottomRight(R.styleable.TUIFrameLayout_tui_bg_corner_bottomRight)
                    .bgCornerBottomLeft(R.styleable.TUIFrameLayout_tui_bg_corner_bottomLeft)

                    .bgRipplePressed(R.styleable.TUIFrameLayout_tui_bg_ripplePressed)

                    .bgGradientStartColorDefault(R.styleable.TUIFrameLayout_tui_bg_gradientStartColor_default)
                    .bgGradientStartColorPressed(R.styleable.TUIFrameLayout_tui_bg_gradientStartColor_pressed)
                    .bgGradientStartColorSelected(R.styleable.TUIFrameLayout_tui_bg_gradientStartColor_selected)
                    .bgGradientStartColorFocused(R.styleable.TUIFrameLayout_tui_bg_gradientStartColor_focused)
                    .bgGradientStartColorDisabled(R.styleable.TUIFrameLayout_tui_bg_gradientStartColor_disabled)

                    .bgGradientCenterColorDefault(R.styleable.TUIFrameLayout_tui_bg_gradientCenterColor_default)
                    .bgGradientCenterColorPressed(R.styleable.TUIFrameLayout_tui_bg_gradientCenterColor_pressed)
                    .bgGradientCenterColorSelected(R.styleable.TUIFrameLayout_tui_bg_gradientCenterColor_selected)
                    .bgGradientCenterColorFocused(R.styleable.TUIFrameLayout_tui_bg_gradientCenterColor_focused)
                    .bgGradientCenterColorDisabled(R.styleable.TUIFrameLayout_tui_bg_gradientCenterColor_disabled)

                    .bgGradientEndColorDefault(R.styleable.TUIFrameLayout_tui_bg_gradientEndColor_default)
                    .bgGradientEndColorPressed(R.styleable.TUIFrameLayout_tui_bg_gradientEndColor_pressed)
                    .bgGradientEndColorSelected(R.styleable.TUIFrameLayout_tui_bg_gradientEndColor_selected)
                    .bgGradientEndColorFocused(R.styleable.TUIFrameLayout_tui_bg_gradientEndColor_focused)
                    .bgGradientEndColorDisabled(R.styleable.TUIFrameLayout_tui_bg_gradientEndColor_disabled)

                    .bgGradientDirectionDefault(R.styleable.TUIFrameLayout_tui_bg_gradientDirection_default)
                    .bgGradientDirectionPressed(R.styleable.TUIFrameLayout_tui_bg_gradientDirection_pressed)
                    .bgGradientDirectionSelected(R.styleable.TUIFrameLayout_tui_bg_gradientDirection_selected)
                    .bgGradientDirectionFocused(R.styleable.TUIFrameLayout_tui_bg_gradientDirection_focused)
                    .bgGradientDirectionDisabled(R.styleable.TUIFrameLayout_tui_bg_gradientDirection_disabled)
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