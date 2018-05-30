package com.tk.tdroid.view.tui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.tk.tdroid.R;


/**
 * <pre>
 *      author : TK
 *      time : 2018/1/9
 *      desc :
 * </pre>
 */

@SuppressLint("AppCompatCustomView")
public class TUIEditText extends EditText implements ITextView {
    private TUIHelper<ITextView> uiHelper;

    public TUIEditText(Context context) {
        this(context, null);
    }

    public TUIEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,R.attr.editTextStyle);
    }

    public TUIEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    private void initAttr(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            uiHelper = new TUIHelper.Builder<ITextView>(this, null).build();
        } else {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TUIEditText);
            uiHelper = new TUIHelper.Builder<ITextView>(this, typedArray)
                    .bgSolidDefault(R.styleable.TUIEditText_tui_bg_solid_default)
                    .bgSolidPressed(R.styleable.TUIEditText_tui_bg_solid_pressed)
                    .bgSolidSelected(R.styleable.TUIEditText_tui_bg_solid_selected)
                    .bgSolidFocused(R.styleable.TUIEditText_tui_bg_solid_focused)
                    .bgSolidDisabled(R.styleable.TUIEditText_tui_bg_solid_disabled)

                    .bgStrokeDefault(R.styleable.TUIEditText_tui_bg_stroke_default)
                    .bgStrokePressed(R.styleable.TUIEditText_tui_bg_stroke_pressed)
                    .bgStrokeSelected(R.styleable.TUIEditText_tui_bg_stroke_selected)
                    .bgStrokeFocused(R.styleable.TUIEditText_tui_bg_stroke_focused)
                    .bgStrokeDisabled(R.styleable.TUIEditText_tui_bg_stroke_disabled)

                    .bgStrokeColorDefault(R.styleable.TUIEditText_tui_bg_strokeColor_default)
                    .bgStrokeColorPressed(R.styleable.TUIEditText_tui_bg_strokeColor_pressed)
                    .bgStrokeColorSelected(R.styleable.TUIEditText_tui_bg_strokeColor_selected)
                    .bgStrokeColorFocused(R.styleable.TUIEditText_tui_bg_strokeColor_focused)
                    .bgStrokeColorDisabled(R.styleable.TUIEditText_tui_bg_strokeColor_disabled)

                    .bgCorner(R.styleable.TUIEditText_tui_bg_corner)
                    .bgCornerTopLeft(R.styleable.TUIEditText_tui_bg_corner_topLeft)
                    .bgCornerTopRight(R.styleable.TUIEditText_tui_bg_corner_topRight)
                    .bgCornerBottomRight(R.styleable.TUIEditText_tui_bg_corner_bottomRight)
                    .bgCornerBottomLeft(R.styleable.TUIEditText_tui_bg_corner_bottomLeft)

                    .bgRipplePressed(R.styleable.TUIEditText_tui_bg_ripplePressed)

                    .bgGradientStartColorDefault(R.styleable.TUIEditText_tui_bg_gradientStartColor_default)
                    .bgGradientStartColorPressed(R.styleable.TUIEditText_tui_bg_gradientStartColor_pressed)
                    .bgGradientStartColorSelected(R.styleable.TUIEditText_tui_bg_gradientStartColor_selected)
                    .bgGradientStartColorFocused(R.styleable.TUIEditText_tui_bg_gradientStartColor_focused)
                    .bgGradientStartColorDisabled(R.styleable.TUIEditText_tui_bg_gradientStartColor_disabled)

                    .bgGradientCenterColorDefault(R.styleable.TUIEditText_tui_bg_gradientCenterColor_default)
                    .bgGradientCenterColorPressed(R.styleable.TUIEditText_tui_bg_gradientCenterColor_pressed)
                    .bgGradientCenterColorSelected(R.styleable.TUIEditText_tui_bg_gradientCenterColor_selected)
                    .bgGradientCenterColorFocused(R.styleable.TUIEditText_tui_bg_gradientCenterColor_focused)
                    .bgGradientCenterColorDisabled(R.styleable.TUIEditText_tui_bg_gradientCenterColor_disabled)

                    .bgGradientEndColorDefault(R.styleable.TUIEditText_tui_bg_gradientEndColor_default)
                    .bgGradientEndColorPressed(R.styleable.TUIEditText_tui_bg_gradientEndColor_pressed)
                    .bgGradientEndColorSelected(R.styleable.TUIEditText_tui_bg_gradientEndColor_selected)
                    .bgGradientEndColorFocused(R.styleable.TUIEditText_tui_bg_gradientEndColor_focused)
                    .bgGradientEndColorDisabled(R.styleable.TUIEditText_tui_bg_gradientEndColor_disabled)

                    .bgGradientDirectionDefault(R.styleable.TUIEditText_tui_bg_gradientDirection_default)
                    .bgGradientDirectionPressed(R.styleable.TUIEditText_tui_bg_gradientDirection_pressed)
                    .bgGradientDirectionSelected(R.styleable.TUIEditText_tui_bg_gradientDirection_selected)
                    .bgGradientDirectionFocused(R.styleable.TUIEditText_tui_bg_gradientDirection_focused)
                    .bgGradientDirectionDisabled(R.styleable.TUIEditText_tui_bg_gradientDirection_disabled)

                    .textColorDefault(R.styleable.TUIEditText_tui_textColor_default)
                    .textColorPressed(R.styleable.TUIEditText_tui_textColor_pressed)
                    .textColorSelected(R.styleable.TUIEditText_tui_textColor_selected)
                    .textColorFocused(R.styleable.TUIEditText_tui_textColor_focused)
                    .textColorDisabled(R.styleable.TUIEditText_tui_textColor_disabled)

                    .drawableLeftWidth(R.styleable.TUIEditText_tui_drawableLeftWidth)
                    .drawableLeftHeight(R.styleable.TUIEditText_tui_drawableLeftHeight)
                    .drawableLeftAlign(R.styleable.TUIEditText_tui_drawableLeftAlign)
                    .drawableLeftDefault(R.styleable.TUIEditText_tui_drawableLeft_default)
                    .drawableLeftTintDefault(R.styleable.TUIEditText_tui_drawableLeftTint_default)
                    .drawableLeftPressed(R.styleable.TUIEditText_tui_drawableLeft_pressed)
                    .drawableLeftTintPressed(R.styleable.TUIEditText_tui_drawableLeftTint_pressed)
                    .drawableLeftSelected(R.styleable.TUIEditText_tui_drawableLeft_selected)
                    .drawableLeftTintSelected(R.styleable.TUIEditText_tui_drawableLeftTint_selected)
                    .drawableLeftFocused(R.styleable.TUIEditText_tui_drawableLeft_focused)
                    .drawableLeftTintFocused(R.styleable.TUIEditText_tui_drawableLeftTint_focused)
                    .drawableLeftDisabled(R.styleable.TUIEditText_tui_drawableLeft_disabled)
                    .drawableLeftTintDisabled(R.styleable.TUIEditText_tui_drawableLeftTint_disabled)

                    .drawableTopWidth(R.styleable.TUIEditText_tui_drawableTopWidth)
                    .drawableTopHeight(R.styleable.TUIEditText_tui_drawableTopHeight)
                    .drawableTopAlign(R.styleable.TUIEditText_tui_drawableTopAlign)
                    .drawableTopDefault(R.styleable.TUIEditText_tui_drawableTop_default)
                    .drawableTopTintDefault(R.styleable.TUIEditText_tui_drawableTopTint_default)
                    .drawableTopPressed(R.styleable.TUIEditText_tui_drawableTop_pressed)
                    .drawableTopTintPressed(R.styleable.TUIEditText_tui_drawableTopTint_pressed)
                    .drawableTopSelected(R.styleable.TUIEditText_tui_drawableTop_selected)
                    .drawableTopTintSelected(R.styleable.TUIEditText_tui_drawableTopTint_selected)
                    .drawableTopFocused(R.styleable.TUIEditText_tui_drawableTop_focused)
                    .drawableTopTintFocused(R.styleable.TUIEditText_tui_drawableTopTint_focused)
                    .drawableTopDisabled(R.styleable.TUIEditText_tui_drawableTop_disabled)
                    .drawableTopTintDisabled(R.styleable.TUIEditText_tui_drawableTopTint_disabled)

                    .drawableRightWidth(R.styleable.TUIEditText_tui_drawableRightWidth)
                    .drawableRightHeight(R.styleable.TUIEditText_tui_drawableRightHeight)
                    .drawableRightAlign(R.styleable.TUIEditText_tui_drawableRightAlign)
                    .drawableRightDefault(R.styleable.TUIEditText_tui_drawableRight_default)
                    .drawableRightTintDefault(R.styleable.TUIEditText_tui_drawableRightTint_default)
                    .drawableRightPressed(R.styleable.TUIEditText_tui_drawableRight_pressed)
                    .drawableRightTintPressed(R.styleable.TUIEditText_tui_drawableRightTint_pressed)
                    .drawableRightSelected(R.styleable.TUIEditText_tui_drawableRight_selected)
                    .drawableRightTintSelected(R.styleable.TUIEditText_tui_drawableRightTint_selected)
                    .drawableRightFocused(R.styleable.TUIEditText_tui_drawableRight_focused)
                    .drawableRightTintFocused(R.styleable.TUIEditText_tui_drawableRightTint_focused)
                    .drawableRightDisabled(R.styleable.TUIEditText_tui_drawableRight_disabled)
                    .drawableRightTintDisabled(R.styleable.TUIEditText_tui_drawableRightTint_disabled)

                    .drawableBottomWidth(R.styleable.TUIEditText_tui_drawableBottomWidth)
                    .drawableBottomHeight(R.styleable.TUIEditText_tui_drawableBottomHeight)
                    .drawableBottomAlign(R.styleable.TUIEditText_tui_drawableBottomAlign)
                    .drawableBottomDefault(R.styleable.TUIEditText_tui_drawableBottom_default)
                    .drawableBottomTintDefault(R.styleable.TUIEditText_tui_drawableBottomTint_default)
                    .drawableBottomPressed(R.styleable.TUIEditText_tui_drawableBottom_pressed)
                    .drawableBottomTintPressed(R.styleable.TUIEditText_tui_drawableBottomTint_pressed)
                    .drawableBottomSelected(R.styleable.TUIEditText_tui_drawableBottom_selected)
                    .drawableBottomTintSelected(R.styleable.TUIEditText_tui_drawableBottomTint_selected)
                    .drawableBottomFocused(R.styleable.TUIEditText_tui_drawableBottom_focused)
                    .drawableBottomTintFocused(R.styleable.TUIEditText_tui_drawableBottomTint_focused)
                    .drawableBottomDisabled(R.styleable.TUIEditText_tui_drawableBottom_disabled)
                    .drawableBottomTintDisabled(R.styleable.TUIEditText_tui_drawableBottomTint_disabled)
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
