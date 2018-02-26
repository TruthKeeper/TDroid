package com.tk.tdroid.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;

import com.tk.tdroid.R;


/**
 * <pre>
 *      author : TK
 *      time : 2017/12/26
 *      desc :
 * </pre>
 */

public class TUIButton extends Button implements ITextView {
    private TUIHelper<ITextView> uiHelper;

    public TUIButton(Context context) {
        super(context);
        initAttr(null);
    }

    public TUIButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public TUIButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    private void initAttr(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            uiHelper = new TUIHelper.Builder<ITextView>(this, null).build();
        } else {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TUIButton);
            uiHelper = new TUIHelper.Builder<ITextView>(this, typedArray)
                    .bgSolidDefault(R.styleable.TUIButton_tui_bg_solid_default)
                    .bgSolidPressed(R.styleable.TUIButton_tui_bg_solid_pressed)
                    .bgSolidSelected(R.styleable.TUIButton_tui_bg_solid_selected)
                    .bgSolidFocused(R.styleable.TUIButton_tui_bg_solid_focused)
                    .bgSolidDisabled(R.styleable.TUIButton_tui_bg_solid_disabled)

                    .bgStrokeDefault(R.styleable.TUIButton_tui_bg_stroke_default)
                    .bgStrokePressed(R.styleable.TUIButton_tui_bg_stroke_pressed)
                    .bgStrokeSelected(R.styleable.TUIButton_tui_bg_stroke_selected)
                    .bgStrokeFocused(R.styleable.TUIButton_tui_bg_stroke_focused)
                    .bgStrokeDisabled(R.styleable.TUIButton_tui_bg_stroke_disabled)

                    .bgStrokeColorDefault(R.styleable.TUIButton_tui_bg_strokeColor_default)
                    .bgStrokeColorPressed(R.styleable.TUIButton_tui_bg_strokeColor_pressed)
                    .bgStrokeColorSelected(R.styleable.TUIButton_tui_bg_strokeColor_selected)
                    .bgStrokeColorFocused(R.styleable.TUIButton_tui_bg_strokeColor_focused)
                    .bgStrokeColorDisabled(R.styleable.TUIButton_tui_bg_strokeColor_disabled)

                    .bgCorner(R.styleable.TUIButton_tui_bg_corner)
                    .bgCornerTopLeft(R.styleable.TUIButton_tui_bg_corner_topLeft)
                    .bgCornerTopRight(R.styleable.TUIButton_tui_bg_corner_topRight)
                    .bgCornerBottomRight(R.styleable.TUIButton_tui_bg_corner_bottomRight)
                    .bgCornerBottomLeft(R.styleable.TUIButton_tui_bg_corner_bottomLeft)

                    .bgRipplePressed(R.styleable.TUIButton_tui_bg_ripplePressed)

                    .bgGradientStartColorDefault(R.styleable.TUIButton_tui_bg_gradientStartColor_default)
                    .bgGradientStartColorPressed(R.styleable.TUIButton_tui_bg_gradientStartColor_pressed)
                    .bgGradientStartColorSelected(R.styleable.TUIButton_tui_bg_gradientStartColor_selected)
                    .bgGradientStartColorFocused(R.styleable.TUIButton_tui_bg_gradientStartColor_focused)
                    .bgGradientStartColorDisabled(R.styleable.TUIButton_tui_bg_gradientStartColor_disabled)

                    .bgGradientCenterColorDefault(R.styleable.TUIButton_tui_bg_gradientCenterColor_default)
                    .bgGradientCenterColorPressed(R.styleable.TUIButton_tui_bg_gradientCenterColor_pressed)
                    .bgGradientCenterColorSelected(R.styleable.TUIButton_tui_bg_gradientCenterColor_selected)
                    .bgGradientCenterColorFocused(R.styleable.TUIButton_tui_bg_gradientCenterColor_focused)
                    .bgGradientCenterColorDisabled(R.styleable.TUIButton_tui_bg_gradientCenterColor_disabled)

                    .bgGradientEndColorDefault(R.styleable.TUIButton_tui_bg_gradientEndColor_default)
                    .bgGradientEndColorPressed(R.styleable.TUIButton_tui_bg_gradientEndColor_pressed)
                    .bgGradientEndColorSelected(R.styleable.TUIButton_tui_bg_gradientEndColor_selected)
                    .bgGradientEndColorFocused(R.styleable.TUIButton_tui_bg_gradientEndColor_focused)
                    .bgGradientEndColorDisabled(R.styleable.TUIButton_tui_bg_gradientEndColor_disabled)

                    .bgGradientDirectionDefault(R.styleable.TUIButton_tui_bg_gradientDirection_default)
                    .bgGradientDirectionPressed(R.styleable.TUIButton_tui_bg_gradientDirection_pressed)
                    .bgGradientDirectionSelected(R.styleable.TUIButton_tui_bg_gradientDirection_selected)
                    .bgGradientDirectionFocused(R.styleable.TUIButton_tui_bg_gradientDirection_focused)
                    .bgGradientDirectionDisabled(R.styleable.TUIButton_tui_bg_gradientDirection_disabled)

                    .textColorDefault(R.styleable.TUIButton_tui_textColor_default)
                    .textColorPressed(R.styleable.TUIButton_tui_textColor_pressed)
                    .textColorSelected(R.styleable.TUIButton_tui_textColor_selected)
                    .textColorFocused(R.styleable.TUIButton_tui_textColor_focused)
                    .textColorDisabled(R.styleable.TUIButton_tui_textColor_disabled)

                    .drawableLeftWidth(R.styleable.TUIButton_tui_drawableLeftWidth)
                    .drawableLeftHeight(R.styleable.TUIButton_tui_drawableLeftHeight)
                    .drawableLeftAlign(R.styleable.TUIButton_tui_drawableLeftAlign)
                    .drawableLeftDefault(R.styleable.TUIButton_tui_drawableLeft_default)
                    .drawableLeftTintDefault(R.styleable.TUIButton_tui_drawableLeftTint_default)
                    .drawableLeftPressed(R.styleable.TUIButton_tui_drawableLeft_pressed)
                    .drawableLeftTintPressed(R.styleable.TUIButton_tui_drawableLeftTint_pressed)
                    .drawableLeftSelected(R.styleable.TUIButton_tui_drawableLeft_selected)
                    .drawableLeftTintSelected(R.styleable.TUIButton_tui_drawableLeftTint_selected)
                    .drawableLeftFocused(R.styleable.TUIButton_tui_drawableLeft_focused)
                    .drawableLeftTintFocused(R.styleable.TUIButton_tui_drawableLeftTint_focused)
                    .drawableLeftDisabled(R.styleable.TUIButton_tui_drawableLeft_disabled)
                    .drawableLeftTintDisabled(R.styleable.TUIButton_tui_drawableLeftTint_disabled)

                    .drawableTopWidth(R.styleable.TUIButton_tui_drawableTopWidth)
                    .drawableTopHeight(R.styleable.TUIButton_tui_drawableTopHeight)
                    .drawableTopAlign(R.styleable.TUIButton_tui_drawableTopAlign)
                    .drawableTopDefault(R.styleable.TUIButton_tui_drawableTop_default)
                    .drawableTopTintDefault(R.styleable.TUIButton_tui_drawableTopTint_default)
                    .drawableTopPressed(R.styleable.TUIButton_tui_drawableTop_pressed)
                    .drawableTopTintPressed(R.styleable.TUIButton_tui_drawableTopTint_pressed)
                    .drawableTopSelected(R.styleable.TUIButton_tui_drawableTop_selected)
                    .drawableTopTintSelected(R.styleable.TUIButton_tui_drawableTopTint_selected)
                    .drawableTopFocused(R.styleable.TUIButton_tui_drawableTop_focused)
                    .drawableTopTintFocused(R.styleable.TUIButton_tui_drawableTopTint_focused)
                    .drawableTopDisabled(R.styleable.TUIButton_tui_drawableTop_disabled)
                    .drawableTopTintDisabled(R.styleable.TUIButton_tui_drawableTopTint_disabled)

                    .drawableRightWidth(R.styleable.TUIButton_tui_drawableRightWidth)
                    .drawableRightHeight(R.styleable.TUIButton_tui_drawableRightHeight)
                    .drawableRightAlign(R.styleable.TUIButton_tui_drawableRightAlign)
                    .drawableRightDefault(R.styleable.TUIButton_tui_drawableRight_default)
                    .drawableRightTintDefault(R.styleable.TUIButton_tui_drawableRightTint_default)
                    .drawableRightPressed(R.styleable.TUIButton_tui_drawableRight_pressed)
                    .drawableRightTintPressed(R.styleable.TUIButton_tui_drawableRightTint_pressed)
                    .drawableRightSelected(R.styleable.TUIButton_tui_drawableRight_selected)
                    .drawableRightTintSelected(R.styleable.TUIButton_tui_drawableRightTint_selected)
                    .drawableRightFocused(R.styleable.TUIButton_tui_drawableRight_focused)
                    .drawableRightTintFocused(R.styleable.TUIButton_tui_drawableRightTint_focused)
                    .drawableRightDisabled(R.styleable.TUIButton_tui_drawableRight_disabled)
                    .drawableRightTintDisabled(R.styleable.TUIButton_tui_drawableRightTint_disabled)

                    .drawableBottomWidth(R.styleable.TUIButton_tui_drawableBottomWidth)
                    .drawableBottomHeight(R.styleable.TUIButton_tui_drawableBottomHeight)
                    .drawableBottomAlign(R.styleable.TUIButton_tui_drawableBottomAlign)
                    .drawableBottomDefault(R.styleable.TUIButton_tui_drawableBottom_default)
                    .drawableBottomTintDefault(R.styleable.TUIButton_tui_drawableBottomTint_default)
                    .drawableBottomPressed(R.styleable.TUIButton_tui_drawableBottom_pressed)
                    .drawableBottomTintPressed(R.styleable.TUIButton_tui_drawableBottomTint_pressed)
                    .drawableBottomSelected(R.styleable.TUIButton_tui_drawableBottom_selected)
                    .drawableBottomTintSelected(R.styleable.TUIButton_tui_drawableBottomTint_selected)
                    .drawableBottomFocused(R.styleable.TUIButton_tui_drawableBottom_focused)
                    .drawableBottomTintFocused(R.styleable.TUIButton_tui_drawableBottomTint_focused)
                    .drawableBottomDisabled(R.styleable.TUIButton_tui_drawableBottom_disabled)
                    .drawableBottomTintDisabled(R.styleable.TUIButton_tui_drawableBottomTint_disabled)

                    .build();

            uiHelper.updateBackground();
            uiHelper.updateText();
            uiHelper.updateDrawable();

            typedArray.recycle();
        }
    }

    public TUIHelper<ITextView> getUiHelper() {
        return uiHelper;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        uiHelper.updateDrawableAlign(this);
    }

    @Override
    public void updateBackground(@NonNull Drawable bg) {
        setBackground(bg);
    }

    @Override
    public void updateText(@NonNull ColorStateList colorStateList) {
        setTextColor(colorStateList);
    }

    @Override
    public void updateDrawable(@NonNull Drawable[] drawables) {
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }
}
