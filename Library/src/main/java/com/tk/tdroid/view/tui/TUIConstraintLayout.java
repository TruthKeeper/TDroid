package com.tk.tdroid.view.tui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.tk.tdroid.R;


/**
 * <pre>
 *     author : amin
 *     time   : 2018/9/6
 *     desc   :
 * </pre>
 */
public class TUIConstraintLayout extends ConstraintLayout implements IView {
    private TUIHelper<IView> uiHelper;

    public TUIConstraintLayout(Context context) {
        super(context);
        initAttr(null);
    }

    public TUIConstraintLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public TUIConstraintLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    private void initAttr(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            uiHelper = new TUIHelper.Builder<IView>(this, null).build();
        } else {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TUIConstraintLayout);
            uiHelper = new TUIHelper.Builder<IView>(this, typedArray)
                    .bgSolidDefault(R.styleable.TUIConstraintLayout_tui_bg_solid_default)
                    .bgSolidPressed(R.styleable.TUIConstraintLayout_tui_bg_solid_pressed)
                    .bgSolidSelected(R.styleable.TUIConstraintLayout_tui_bg_solid_selected)
                    .bgSolidFocused(R.styleable.TUIConstraintLayout_tui_bg_solid_focused)
                    .bgSolidDisabled(R.styleable.TUIConstraintLayout_tui_bg_solid_disabled)

                    .bgStrokeDefault(R.styleable.TUIConstraintLayout_tui_bg_stroke_default)
                    .bgStrokePressed(R.styleable.TUIConstraintLayout_tui_bg_stroke_pressed)
                    .bgStrokeSelected(R.styleable.TUIConstraintLayout_tui_bg_stroke_selected)
                    .bgStrokeFocused(R.styleable.TUIConstraintLayout_tui_bg_stroke_focused)
                    .bgStrokeDisabled(R.styleable.TUIConstraintLayout_tui_bg_stroke_disabled)

                    .bgStrokeColorDefault(R.styleable.TUIConstraintLayout_tui_bg_strokeColor_default)
                    .bgStrokeColorPressed(R.styleable.TUIConstraintLayout_tui_bg_strokeColor_pressed)
                    .bgStrokeColorSelected(R.styleable.TUIConstraintLayout_tui_bg_strokeColor_selected)
                    .bgStrokeColorFocused(R.styleable.TUIConstraintLayout_tui_bg_strokeColor_focused)
                    .bgStrokeColorDisabled(R.styleable.TUIConstraintLayout_tui_bg_strokeColor_disabled)

                    .bgCorner(R.styleable.TUIConstraintLayout_tui_bg_corner)
                    .bgCornerTopLeft(R.styleable.TUIConstraintLayout_tui_bg_corner_topLeft)
                    .bgCornerTopRight(R.styleable.TUIConstraintLayout_tui_bg_corner_topRight)
                    .bgCornerBottomRight(R.styleable.TUIConstraintLayout_tui_bg_corner_bottomRight)
                    .bgCornerBottomLeft(R.styleable.TUIConstraintLayout_tui_bg_corner_bottomLeft)

                    .bgRipplePressed(R.styleable.TUIConstraintLayout_tui_bg_ripplePressed)

                    .bgGradientStartColorDefault(R.styleable.TUIConstraintLayout_tui_bg_gradientStartColor_default)
                    .bgGradientStartColorPressed(R.styleable.TUIConstraintLayout_tui_bg_gradientStartColor_pressed)
                    .bgGradientStartColorSelected(R.styleable.TUIConstraintLayout_tui_bg_gradientStartColor_selected)
                    .bgGradientStartColorFocused(R.styleable.TUIConstraintLayout_tui_bg_gradientStartColor_focused)
                    .bgGradientStartColorDisabled(R.styleable.TUIConstraintLayout_tui_bg_gradientStartColor_disabled)

                    .bgGradientCenterColorDefault(R.styleable.TUIConstraintLayout_tui_bg_gradientCenterColor_default)
                    .bgGradientCenterColorPressed(R.styleable.TUIConstraintLayout_tui_bg_gradientCenterColor_pressed)
                    .bgGradientCenterColorSelected(R.styleable.TUIConstraintLayout_tui_bg_gradientCenterColor_selected)
                    .bgGradientCenterColorFocused(R.styleable.TUIConstraintLayout_tui_bg_gradientCenterColor_focused)
                    .bgGradientCenterColorDisabled(R.styleable.TUIConstraintLayout_tui_bg_gradientCenterColor_disabled)

                    .bgGradientEndColorDefault(R.styleable.TUIConstraintLayout_tui_bg_gradientEndColor_default)
                    .bgGradientEndColorPressed(R.styleable.TUIConstraintLayout_tui_bg_gradientEndColor_pressed)
                    .bgGradientEndColorSelected(R.styleable.TUIConstraintLayout_tui_bg_gradientEndColor_selected)
                    .bgGradientEndColorFocused(R.styleable.TUIConstraintLayout_tui_bg_gradientEndColor_focused)
                    .bgGradientEndColorDisabled(R.styleable.TUIConstraintLayout_tui_bg_gradientEndColor_disabled)

                    .bgGradientDirectionDefault(R.styleable.TUIConstraintLayout_tui_bg_gradientDirection_default)
                    .bgGradientDirectionPressed(R.styleable.TUIConstraintLayout_tui_bg_gradientDirection_pressed)
                    .bgGradientDirectionSelected(R.styleable.TUIConstraintLayout_tui_bg_gradientDirection_selected)
                    .bgGradientDirectionFocused(R.styleable.TUIConstraintLayout_tui_bg_gradientDirection_focused)
                    .bgGradientDirectionDisabled(R.styleable.TUIConstraintLayout_tui_bg_gradientDirection_disabled)
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