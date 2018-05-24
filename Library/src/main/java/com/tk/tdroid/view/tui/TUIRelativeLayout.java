package com.tk.tdroid.view.tui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.tk.tdroid.R;


/**
 * <pre>
 *      author : TK
 *      time : 2017/12/26
 *      desc :
 * </pre>
 */

public class TUIRelativeLayout extends RelativeLayout implements IView {
    private TUIHelper<IView> uiHelper;

    public TUIRelativeLayout(Context context) {
        super(context);
        initAttr(null);
    }

    public TUIRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public TUIRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TUIRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(attrs);
    }

    private void initAttr(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            uiHelper = new TUIHelper.Builder<IView>(this, null).build();
        } else {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TUIRelativeLayout);
            uiHelper = new TUIHelper.Builder<IView>(this, typedArray)
                    .bgSolidDefault(R.styleable.TUIRelativeLayout_tui_bg_solid_default)
                    .bgSolidPressed(R.styleable.TUIRelativeLayout_tui_bg_solid_pressed)
                    .bgSolidSelected(R.styleable.TUIRelativeLayout_tui_bg_solid_selected)
                    .bgSolidFocused(R.styleable.TUIRelativeLayout_tui_bg_solid_focused)
                    .bgSolidDisabled(R.styleable.TUIRelativeLayout_tui_bg_solid_disabled)

                    .bgStrokeDefault(R.styleable.TUIRelativeLayout_tui_bg_stroke_default)
                    .bgStrokePressed(R.styleable.TUIRelativeLayout_tui_bg_stroke_pressed)
                    .bgStrokeSelected(R.styleable.TUIRelativeLayout_tui_bg_stroke_selected)
                    .bgStrokeFocused(R.styleable.TUIRelativeLayout_tui_bg_stroke_focused)
                    .bgStrokeDisabled(R.styleable.TUIRelativeLayout_tui_bg_stroke_disabled)

                    .bgStrokeColorDefault(R.styleable.TUIRelativeLayout_tui_bg_strokeColor_default)
                    .bgStrokeColorPressed(R.styleable.TUIRelativeLayout_tui_bg_strokeColor_pressed)
                    .bgStrokeColorSelected(R.styleable.TUIRelativeLayout_tui_bg_strokeColor_selected)
                    .bgStrokeColorFocused(R.styleable.TUIRelativeLayout_tui_bg_strokeColor_focused)
                    .bgStrokeColorDisabled(R.styleable.TUIRelativeLayout_tui_bg_strokeColor_disabled)

                    .bgCorner(R.styleable.TUIRelativeLayout_tui_bg_corner)
                    .bgCornerTopLeft(R.styleable.TUIRelativeLayout_tui_bg_corner_topLeft)
                    .bgCornerTopRight(R.styleable.TUIRelativeLayout_tui_bg_corner_topRight)
                    .bgCornerBottomRight(R.styleable.TUIRelativeLayout_tui_bg_corner_bottomRight)
                    .bgCornerBottomLeft(R.styleable.TUIRelativeLayout_tui_bg_corner_bottomLeft)

                    .bgRipplePressed(R.styleable.TUIRelativeLayout_tui_bg_ripplePressed)

                    .bgGradientStartColorDefault(R.styleable.TUIRelativeLayout_tui_bg_gradientStartColor_default)
                    .bgGradientStartColorPressed(R.styleable.TUIRelativeLayout_tui_bg_gradientStartColor_pressed)
                    .bgGradientStartColorSelected(R.styleable.TUIRelativeLayout_tui_bg_gradientStartColor_selected)
                    .bgGradientStartColorFocused(R.styleable.TUIRelativeLayout_tui_bg_gradientStartColor_focused)
                    .bgGradientStartColorDisabled(R.styleable.TUIRelativeLayout_tui_bg_gradientStartColor_disabled)

                    .bgGradientCenterColorDefault(R.styleable.TUIRelativeLayout_tui_bg_gradientCenterColor_default)
                    .bgGradientCenterColorPressed(R.styleable.TUIRelativeLayout_tui_bg_gradientCenterColor_pressed)
                    .bgGradientCenterColorSelected(R.styleable.TUIRelativeLayout_tui_bg_gradientCenterColor_selected)
                    .bgGradientCenterColorFocused(R.styleable.TUIRelativeLayout_tui_bg_gradientCenterColor_focused)
                    .bgGradientCenterColorDisabled(R.styleable.TUIRelativeLayout_tui_bg_gradientCenterColor_disabled)

                    .bgGradientEndColorDefault(R.styleable.TUIRelativeLayout_tui_bg_gradientEndColor_default)
                    .bgGradientEndColorPressed(R.styleable.TUIRelativeLayout_tui_bg_gradientEndColor_pressed)
                    .bgGradientEndColorSelected(R.styleable.TUIRelativeLayout_tui_bg_gradientEndColor_selected)
                    .bgGradientEndColorFocused(R.styleable.TUIRelativeLayout_tui_bg_gradientEndColor_focused)
                    .bgGradientEndColorDisabled(R.styleable.TUIRelativeLayout_tui_bg_gradientEndColor_disabled)

                    .bgGradientDirectionDefault(R.styleable.TUIRelativeLayout_tui_bg_gradientDirection_default)
                    .bgGradientDirectionPressed(R.styleable.TUIRelativeLayout_tui_bg_gradientDirection_pressed)
                    .bgGradientDirectionSelected(R.styleable.TUIRelativeLayout_tui_bg_gradientDirection_selected)
                    .bgGradientDirectionFocused(R.styleable.TUIRelativeLayout_tui_bg_gradientDirection_focused)
                    .bgGradientDirectionDisabled(R.styleable.TUIRelativeLayout_tui_bg_gradientDirection_disabled)
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
