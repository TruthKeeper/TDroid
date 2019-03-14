package com.tk.tdroid.view.tui;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Px;
import android.support.annotation.StyleableRes;
import android.widget.TextView;

import com.tk.tdroid.utils.ConvertUtils;
import com.tk.tdroid.utils.DrawableUtils;
import com.tk.tdroid.utils.SelectorFactory;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/27
 *      desc : <ol>
 *          <li>支持shape背景的圆角、边框、渐变等配置</li>
 *          <li>支持ITextView字体颜色、drawable等配置</li>
 *      </ol>
 * </pre>
 */

public class TUIHelper<TUI extends IView> {
    private Builder<TUI> mBuilder;

    private TUIHelper(Builder<TUI> builder) {
        mBuilder = builder;
    }

    public TUI getUiView() {
        return mBuilder.uiView;
    }

    public TUIHelper<TUI> bgSolidDefault(@ColorInt int bgSolidDefault) {
        mBuilder.bgSolidDefault = bgSolidDefault;
        return this;
    }

    public TUIHelper<TUI> bgSolidPressed(@ColorInt int bgSolidPressed) {
        mBuilder.bgSolidPressed = bgSolidPressed;
        return this;
    }

    public TUIHelper<TUI> bgSolidSelected(@ColorInt int bgSolidSelected) {
        mBuilder.bgSolidSelected = bgSolidSelected;
        return this;
    }

    public TUIHelper<TUI> bgSolidFocused(@ColorInt int bgSolidFocused) {
        mBuilder.bgSolidFocused = bgSolidFocused;
        return this;
    }

    public TUIHelper<TUI> bgSolidDisabled(@ColorInt int bgSolidDisabled) {
        mBuilder.bgSolidDisabled = bgSolidDisabled;
        return this;
    }

    public TUIHelper<TUI> bgStrokeDefault(@Px int bgStrokeDefault) {
        mBuilder.bgStrokeDefault = bgStrokeDefault;
        return this;
    }

    public TUIHelper<TUI> bgStrokePressed(@Px int bgStrokePressed) {
        mBuilder.bgStrokePressed = bgStrokePressed;
        return this;
    }

    public TUIHelper<TUI> bgStrokeSelected(@Px int bgStrokeSelected) {
        mBuilder.bgStrokeSelected = bgStrokeSelected;
        return this;
    }

    public TUIHelper<TUI> bgStrokeFocused(@Px int bgStrokeFocused) {
        mBuilder.bgStrokeFocused = bgStrokeFocused;
        return this;
    }

    public TUIHelper<TUI> bgStrokeDisabled(@Px int bgStrokeDisabled) {
        mBuilder.bgStrokeDisabled = bgStrokeDisabled;
        return this;
    }

    public TUIHelper<TUI> bgStrokeColorDefault(@ColorInt int bgStrokeColorDefault) {
        mBuilder.bgStrokeColorDefault = bgStrokeColorDefault;
        return this;
    }

    public TUIHelper<TUI> bgStrokeColorPressed(@ColorInt int bgStrokeColorPressed) {
        mBuilder.bgStrokeColorPressed = bgStrokeColorPressed;
        return this;
    }

    public TUIHelper<TUI> bgStrokeColorSelected(@ColorInt int bgStrokeColorSelected) {
        mBuilder.bgStrokeColorSelected = bgStrokeColorSelected;
        return this;
    }

    public TUIHelper<TUI> bgStrokeColorFocused(@ColorInt int bgStrokeColorFocused) {
        mBuilder.bgStrokeColorFocused = bgStrokeColorFocused;
        return this;
    }

    public TUIHelper<TUI> bgStrokeColorDisabled(@ColorInt int bgStrokeColorDisabled) {
        mBuilder.bgStrokeColorDisabled = bgStrokeColorDisabled;
        return this;
    }

    public TUIHelper<TUI> bgCorner(@Px int bgCorner) {
        mBuilder.bgCorner = bgCorner;
        return this;
    }

    public TUIHelper<TUI> bgCornerTopLeft(@Px int bgCornerTopLeft) {
        mBuilder.bgCornerTopLeft = bgCornerTopLeft;
        return this;
    }

    public TUIHelper<TUI> bgCornerTopRight(@Px int bgCornerTopRight) {
        mBuilder.bgCornerTopRight = bgCornerTopRight;
        return this;
    }

    public TUIHelper<TUI> bgCornerBottomRight(@Px int bgCornerBottomRight) {
        mBuilder.bgCornerBottomRight = bgCornerBottomRight;
        return this;
    }

    public TUIHelper<TUI> bgCornerBottomLeft(@Px int bgCornerBottomLeft) {
        mBuilder.bgCornerBottomLeft = bgCornerBottomLeft;
        return this;
    }

    public TUIHelper<TUI> bgRipplePressed(@ColorInt int bgRipplePressed) {
        mBuilder.bgRipplePressed = bgRipplePressed;
        return this;
    }

    public TUIHelper<TUI> bgGradientStartColorDefault(@ColorInt int bgGradientStartColorDefault) {
        mBuilder.bgGradientStartColorDefault = bgGradientStartColorDefault;
        return this;
    }

    public TUIHelper<TUI> bgGradientStartColorPressed(@ColorInt int bgGradientStartColorPressed) {
        mBuilder.bgGradientStartColorPressed = bgGradientStartColorPressed;
        return this;
    }

    public TUIHelper<TUI> bgGradientStartColorSelected(@ColorInt int bgGradientStartColorSelected) {
        mBuilder.bgGradientStartColorSelected = bgGradientStartColorSelected;
        return this;
    }

    public TUIHelper<TUI> bgGradientStartColorFocused(@ColorInt int bgGradientStartColorFocused) {
        mBuilder.bgGradientStartColorFocused = bgGradientStartColorFocused;
        return this;
    }

    public TUIHelper<TUI> bgGradientStartColorDisabled(@ColorInt int bgGradientStartColorDisabled) {
        mBuilder.bgGradientStartColorDisabled = bgGradientStartColorDisabled;
        return this;
    }

    public TUIHelper<TUI> bgGradientCenterColorDefault(@ColorInt int bgGradientCenterColorDefault) {
        mBuilder.bgGradientCenterColorDefault = bgGradientCenterColorDefault;
        return this;
    }

    public TUIHelper<TUI> bgGradientCenterColorPressed(@ColorInt int bgGradientCenterColorPressed) {
        mBuilder.bgGradientCenterColorPressed = bgGradientCenterColorPressed;
        return this;
    }

    public TUIHelper<TUI> bgGradientCenterColorSelected(@ColorInt int bgGradientCenterColorSelected) {
        mBuilder.bgGradientCenterColorSelected = bgGradientCenterColorSelected;
        return this;
    }

    public TUIHelper<TUI> bgGradientCenterColorFocused(@ColorInt int bgGradientCenterColorFocused) {
        mBuilder.bgGradientCenterColorFocused = bgGradientCenterColorFocused;
        return this;
    }

    public TUIHelper<TUI> bgGradientCenterColorDisabled(@ColorInt int bgGradientCenterColorDisabled) {
        mBuilder.bgGradientCenterColorDisabled = bgGradientCenterColorDisabled;
        return this;
    }

    public TUIHelper<TUI> bgGradientEndColorDefault(@ColorInt int bgGradientEndColorDefault) {
        mBuilder.bgGradientEndColorDefault = bgGradientEndColorDefault;
        return this;
    }

    public TUIHelper<TUI> bgGradientEndColorPressed(@ColorInt int bgGradientEndColorPressed) {
        mBuilder.bgGradientEndColorPressed = bgGradientEndColorPressed;
        return this;
    }

    public TUIHelper<TUI> bgGradientEndColorSelected(@ColorInt int bgGradientEndColorSelected) {
        mBuilder.bgGradientEndColorSelected = bgGradientEndColorSelected;
        return this;
    }

    public TUIHelper<TUI> bgGradientEndColorFocused(@ColorInt int bgGradientEndColorFocused) {
        mBuilder.bgGradientEndColorFocused = bgGradientEndColorFocused;
        return this;
    }

    public TUIHelper<TUI> bgGradientEndColorDisabled(@ColorInt int bgGradientEndColorDisabled) {
        mBuilder.bgGradientEndColorDisabled = bgGradientEndColorDisabled;
        return this;
    }

    public TUIHelper<TUI> bgGradientDirectionDefault(GradientDrawable.Orientation bgGradientDirectionDefault) {
        mBuilder.bgGradientDirectionDefault = bgGradientDirectionDefault;
        return this;
    }

    public TUIHelper<TUI> bgGradientDirectionPressed(GradientDrawable.Orientation bgGradientDirectionPressed) {
        mBuilder.bgGradientDirectionPressed = bgGradientDirectionPressed;
        return this;
    }

    public TUIHelper<TUI> bgGradientDirectionSelected(GradientDrawable.Orientation bgGradientDirectionSelected) {
        mBuilder.bgGradientDirectionSelected = bgGradientDirectionSelected;
        return this;
    }

    public TUIHelper<TUI> bgGradientDirectionFocused(GradientDrawable.Orientation bgGradientDirectionFocused) {
        mBuilder.bgGradientDirectionFocused = bgGradientDirectionFocused;
        return this;
    }

    public TUIHelper<TUI> bgGradientDirectionDisabled(GradientDrawable.Orientation bgGradientDirectionDisabled) {
        mBuilder.bgGradientDirectionDisabled = bgGradientDirectionDisabled;
        return this;
    }

    public TUIHelper<TUI> textColorDefault(@ColorInt int textColorDefault) {
        mBuilder.textColorDefault = textColorDefault;
        return this;
    }

    public TUIHelper<TUI> textColorPressed(@ColorInt int textColorPressed) {
        mBuilder.textColorPressed = textColorPressed;
        return this;
    }

    public TUIHelper<TUI> textColorSelected(@ColorInt int textColorSelected) {
        mBuilder.textColorSelected = textColorSelected;
        return this;
    }

    public TUIHelper<TUI> textColorFocused(@ColorInt int textColorFocused) {
        mBuilder.textColorFocused = textColorFocused;
        return this;
    }

    public TUIHelper<TUI> textColorDisabled(@ColorInt int textColorDisabled) {
        mBuilder.textColorDisabled = textColorDisabled;
        return this;
    }

    public TUIHelper<TUI> drawableLeftWidth(@Px int drawableLeftWidth) {
        mBuilder.drawableLeftWidth = drawableLeftWidth;
        return this;
    }

    public TUIHelper<TUI> drawableLeftHeight(@Px int drawableLeftHeight) {
        mBuilder.drawableLeftHeight = drawableLeftHeight;
        return this;
    }

    public TUIHelper<TUI> drawableLeftAlign(int drawableLeftAlign) {
        mBuilder.drawableLeftAlign = drawableLeftAlign;
        return this;
    }

    public TUIHelper<TUI> drawableLeftDefault(Drawable drawableLeftDefault) {
        mBuilder.drawableLeftDefault = drawableLeftDefault;
        return this;
    }

    public TUIHelper<TUI> drawableLeftTintDefault(@ColorInt int drawableLeftTintDefault) {
        mBuilder.drawableLeftTintDefault = drawableLeftTintDefault;
        return this;
    }

    public TUIHelper<TUI> drawableLeftPressed(Drawable drawableLeftPressed) {
        mBuilder.drawableLeftPressed = drawableLeftPressed;
        return this;
    }

    public TUIHelper<TUI> drawableLeftTintPressed(@ColorInt int drawableLeftTintPressed) {
        mBuilder.drawableLeftTintPressed = drawableLeftTintPressed;
        return this;
    }

    public TUIHelper<TUI> drawableLeftSelected(Drawable drawableLeftSelected) {
        mBuilder.drawableLeftSelected = drawableLeftSelected;
        return this;
    }

    public TUIHelper<TUI> drawableLeftTintSelected(@ColorInt int drawableLeftTintSelected) {
        mBuilder.drawableLeftTintSelected = drawableLeftTintSelected;
        return this;
    }

    public TUIHelper<TUI> drawableLeftFocused(Drawable drawableLeftFocused) {
        mBuilder.drawableLeftFocused = drawableLeftFocused;
        return this;
    }

    public TUIHelper<TUI> drawableLeftTintFocused(@ColorInt int drawableLeftTintFocused) {
        mBuilder.drawableLeftTintFocused = drawableLeftTintFocused;
        return this;
    }

    public TUIHelper<TUI> drawableLeftDisabled(Drawable drawableLeftDisabled) {
        mBuilder.drawableLeftDisabled = drawableLeftDisabled;
        return this;
    }

    public TUIHelper<TUI> drawableLeftTintDisabled(@ColorInt int drawableLeftTintDisabled) {
        mBuilder.drawableLeftTintDisabled = drawableLeftTintDisabled;
        return this;
    }

    public TUIHelper<TUI> drawableTopWidth(@Px int drawableTopWidth) {
        mBuilder.drawableTopWidth = drawableTopWidth;
        return this;
    }

    public TUIHelper<TUI> drawableTopHeight(@Px int drawableTopHeight) {
        mBuilder.drawableTopHeight = drawableTopHeight;
        return this;
    }

    public TUIHelper<TUI> drawableTopAlign(int drawableTopAlign) {
        mBuilder.drawableTopAlign = drawableTopAlign;
        return this;
    }

    public TUIHelper<TUI> drawableTopDefault(Drawable drawableTopDefault) {
        mBuilder.drawableTopDefault = drawableTopDefault;
        return this;
    }

    public TUIHelper<TUI> drawableTopTintDefault(@ColorInt int drawableTopTintDefault) {
        mBuilder.drawableTopTintDefault = drawableTopTintDefault;
        return this;
    }

    public TUIHelper<TUI> drawableTopPressed(Drawable drawableTopPressed) {
        mBuilder.drawableTopPressed = drawableTopPressed;
        return this;
    }

    public TUIHelper<TUI> drawableTopTintPressed(@ColorInt int drawableTopTintPressed) {
        mBuilder.drawableTopTintPressed = drawableTopTintPressed;
        return this;
    }

    public TUIHelper<TUI> drawableTopSelected(Drawable drawableTopSelected) {
        mBuilder.drawableTopSelected = drawableTopSelected;
        return this;
    }

    public TUIHelper<TUI> drawableTopTintSelected(@ColorInt int drawableTopTintSelected) {
        mBuilder.drawableTopTintSelected = drawableTopTintSelected;
        return this;
    }

    public TUIHelper<TUI> drawableTopFocused(Drawable drawableTopFocused) {
        mBuilder.drawableTopFocused = drawableTopFocused;
        return this;
    }

    public TUIHelper<TUI> drawableTopTintFocused(@ColorInt int drawableTopTintFocused) {
        mBuilder.drawableTopTintFocused = drawableTopTintFocused;
        return this;
    }

    public TUIHelper<TUI> drawableTopDisabled(Drawable drawableTopDisabled) {
        mBuilder.drawableTopDisabled = drawableTopDisabled;
        return this;
    }

    public TUIHelper<TUI> drawableTopTintDisabled(@ColorInt int drawableTopTintDisabled) {
        mBuilder.drawableTopTintDisabled = drawableTopTintDisabled;
        return this;
    }

    public TUIHelper<TUI> drawableRightWidth(@Px int drawableRightWidth) {
        mBuilder.drawableRightWidth = drawableRightWidth;
        return this;
    }

    public TUIHelper<TUI> drawableRightHeight(@Px int drawableRightHeight) {
        mBuilder.drawableRightHeight = drawableRightHeight;
        return this;
    }

    public TUIHelper<TUI> drawableRightAlign(int drawableRightAlign) {
        mBuilder.drawableRightAlign = drawableRightAlign;
        return this;
    }

    public TUIHelper<TUI> drawableRightDefault(Drawable drawableRightDefault) {
        mBuilder.drawableRightDefault = drawableRightDefault;
        return this;
    }

    public TUIHelper<TUI> drawableRightTintDefault(@ColorInt int drawableRightTintDefault) {
        mBuilder.drawableRightTintDefault = drawableRightTintDefault;
        return this;
    }

    public TUIHelper<TUI> drawableRightPressed(Drawable drawableRightPressed) {
        mBuilder.drawableRightPressed = drawableRightPressed;
        return this;
    }

    public TUIHelper<TUI> drawableRightTintPressed(@ColorInt int drawableRightTintPressed) {
        mBuilder.drawableRightTintPressed = drawableRightTintPressed;
        return this;
    }

    public TUIHelper<TUI> drawableRightSelected(Drawable drawableRightSelected) {
        mBuilder.drawableRightSelected = drawableRightSelected;
        return this;
    }

    public TUIHelper<TUI> drawableRightTintSelected(@ColorInt int drawableRightTintSelected) {
        mBuilder.drawableRightTintSelected = drawableRightTintSelected;
        return this;
    }

    public TUIHelper<TUI> drawableRightFocused(Drawable drawableRightFocused) {
        mBuilder.drawableRightFocused = drawableRightFocused;
        return this;
    }

    public TUIHelper<TUI> drawableRightTintFocused(@ColorInt int drawableRightTintFocused) {
        mBuilder.drawableRightTintFocused = drawableRightTintFocused;
        return this;
    }

    public TUIHelper<TUI> drawableRightDisabled(Drawable drawableRightDisabled) {
        mBuilder.drawableRightDisabled = drawableRightDisabled;
        return this;
    }

    public TUIHelper<TUI> drawableRightTintDisabled(@ColorInt int drawableRightTintDisabled) {
        mBuilder.drawableRightTintDisabled = drawableRightTintDisabled;
        return this;
    }

    public TUIHelper<TUI> drawableBottomWidth(@Px int drawableBottomWidth) {
        mBuilder.drawableBottomWidth = drawableBottomWidth;
        return this;
    }

    public TUIHelper<TUI> drawableBottomHeight(@Px int drawableBottomHeight) {
        mBuilder.drawableBottomHeight = drawableBottomHeight;
        return this;
    }

    public TUIHelper<TUI> drawableBottomAlign(int drawableBottomAlign) {
        mBuilder.drawableBottomAlign = drawableBottomAlign;
        return this;
    }

    public TUIHelper<TUI> drawableBottomDefault(Drawable drawableBottomDefault) {
        mBuilder.drawableBottomDefault = drawableBottomDefault;
        return this;
    }

    public TUIHelper<TUI> drawableBottomTintDefault(@ColorInt int drawableBottomTintDefault) {
        mBuilder.drawableBottomTintDefault = drawableBottomTintDefault;
        return this;
    }

    public TUIHelper<TUI> drawableBottomPressed(Drawable drawableBottomPressed) {
        mBuilder.drawableBottomPressed = drawableBottomPressed;
        return this;
    }

    public TUIHelper<TUI> drawableBottomTintPressed(@ColorInt int drawableBottomTintPressed) {
        mBuilder.drawableBottomTintPressed = drawableBottomTintPressed;
        return this;
    }

    public TUIHelper<TUI> drawableBottomSelected(Drawable drawableBottomSelected) {
        mBuilder.drawableBottomSelected = drawableBottomSelected;
        return this;
    }

    public TUIHelper<TUI> drawableBottomTintSelected(@ColorInt int drawableBottomTintSelected) {
        mBuilder.drawableBottomTintSelected = drawableBottomTintSelected;
        return this;
    }

    public TUIHelper<TUI> drawableBottomFocused(Drawable drawableBottomFocused) {
        mBuilder.drawableBottomFocused = drawableBottomFocused;
        return this;
    }

    public TUIHelper<TUI> drawableBottomTintFocused(@ColorInt int drawableBottomTintFocused) {
        mBuilder.drawableBottomTintFocused = drawableBottomTintFocused;
        return this;
    }

    public TUIHelper<TUI> drawableBottomDisabled(Drawable drawableBottomDisabled) {
        mBuilder.drawableBottomDisabled = drawableBottomDisabled;
        return this;
    }

    public TUIHelper<TUI> drawableBottomTintDisabled(@ColorInt int drawableBottomTintDisabled) {
        mBuilder.drawableBottomTintDisabled = drawableBottomTintDisabled;
        return this;
    }

    public TUIHelper<TUI> srcDefault(Drawable srcDefault) {
        mBuilder.srcDefault = srcDefault;
        return this;
    }

    public TUIHelper<TUI> srcTintDefault(@ColorInt int srcTintDefault) {
        mBuilder.srcTintDefault = srcTintDefault;
        return this;
    }

    public TUIHelper<TUI> srcPressed(Drawable srcPressed) {
        mBuilder.srcPressed = srcPressed;
        return this;
    }

    public TUIHelper<TUI> srcTintPressed(@ColorInt int srcTintPressed) {
        mBuilder.srcTintPressed = srcTintPressed;
        return this;
    }

    public TUIHelper<TUI> srcSelected(Drawable srcSelected) {
        mBuilder.srcSelected = srcSelected;
        return this;
    }

    public TUIHelper<TUI> srcTintSelected(@ColorInt int srcTintSelected) {
        mBuilder.srcTintSelected = srcTintSelected;
        return this;
    }

    public TUIHelper<TUI> srcFocused(Drawable srcFocused) {
        mBuilder.srcFocused = srcFocused;
        return this;
    }

    public TUIHelper<TUI> srcTintFocused(@ColorInt int srcTintFocused) {
        mBuilder.srcTintFocused = srcTintFocused;
        return this;
    }

    public TUIHelper<TUI> srcDisabled(Drawable srcDisabled) {
        mBuilder.srcDisabled = srcDisabled;
        return this;
    }

    public TUIHelper<TUI> srcTintDisabled(@ColorInt int srcTintDisabled) {
        mBuilder.srcTintDisabled = srcTintDisabled;
        return this;
    }

    /**
     * 更新背景
     */
    public void updateBackground() {
        SelectorFactory factory = SelectorFactory.create();
        factory.defaultSolid(mBuilder.bgSolidDefault)
                .pressedSolid(mBuilder.bgSolidPressed)
                .selectedSolid(mBuilder.bgSolidSelected)
                .focusedSolid(mBuilder.bgSolidFocused)
                .disabledSolid(mBuilder.bgSolidDisabled);

        factory.defaultStroke(mBuilder.bgStrokeDefault, mBuilder.bgStrokeColorDefault)
                .pressedStroke(mBuilder.bgStrokePressed, mBuilder.bgStrokeColorPressed)
                .selectedStroke(mBuilder.bgStrokeSelected, mBuilder.bgStrokeColorSelected)
                .focusedStroke(mBuilder.bgStrokeFocused, mBuilder.bgStrokeColorFocused)
                .disabledStroke(mBuilder.bgStrokeDisabled, mBuilder.bgStrokeColorDisabled);

        if (mBuilder.bgCorner > 0) {
            factory.corner(mBuilder.bgCorner);
        } else {
            factory.corner(new float[]{mBuilder.bgCornerTopLeft, mBuilder.bgCornerTopLeft,
                    mBuilder.bgCornerTopRight, mBuilder.bgCornerTopRight,
                    mBuilder.bgCornerBottomRight, mBuilder.bgCornerBottomRight,
                    mBuilder.bgCornerBottomLeft, mBuilder.bgCornerBottomLeft});
        }

        factory.pressedRipple(mBuilder.bgRipplePressed);

        factory.defaultGradient(mBuilder.bgGradientDirectionDefault, generateGradientColors(mBuilder.bgGradientStartColorDefault, mBuilder.bgGradientCenterColorDefault, mBuilder.bgGradientEndColorDefault))
                .pressedGradient(mBuilder.bgGradientDirectionPressed, generateGradientColors(mBuilder.bgGradientStartColorPressed, mBuilder.bgGradientCenterColorPressed, mBuilder.bgGradientEndColorPressed))
                .selectedGradient(mBuilder.bgGradientDirectionSelected, generateGradientColors(mBuilder.bgGradientStartColorSelected, mBuilder.bgGradientCenterColorSelected, mBuilder.bgGradientEndColorSelected))
                .focusedGradient(mBuilder.bgGradientDirectionFocused, generateGradientColors(mBuilder.bgGradientStartColorFocused, mBuilder.bgGradientCenterColorFocused, mBuilder.bgGradientEndColorFocused))
                .disabledGradient(mBuilder.bgGradientDirectionDisabled, generateGradientColors(mBuilder.bgGradientStartColorDisabled, mBuilder.bgGradientCenterColorDisabled, mBuilder.bgGradientEndColorDisabled));
        StateListDrawable stateListDrawable = factory.build();
        if (!DrawableUtils.isStateListDrawableEmpty(stateListDrawable)) {
            mBuilder.uiView.updateBackground(stateListDrawable);
        }
    }

    /**
     * 更新文字
     */
    public void updateText() {
        if (mBuilder.textColorDefault == SelectorFactory.INVALID) {
            return;
        }
        if (mBuilder.uiView instanceof ITextView) {
            List<int[]> stateList = new ArrayList<>();
            List<Integer> colorList = new ArrayList<>();
            if (mBuilder.textColorSelected != SelectorFactory.INVALID) {
                stateList.add(SelectorFactory.STATES[0]);
                colorList.add(mBuilder.textColorSelected);
            }
            if (mBuilder.textColorPressed != SelectorFactory.INVALID) {
                stateList.add(SelectorFactory.STATES[1]);
                colorList.add(mBuilder.textColorPressed);
            }
            if (mBuilder.textColorFocused != SelectorFactory.INVALID) {
                stateList.add(SelectorFactory.STATES[2]);
                colorList.add(mBuilder.textColorFocused);
            }
            if (mBuilder.textColorDisabled != SelectorFactory.INVALID) {
                stateList.add(SelectorFactory.STATES[3]);
                colorList.add(mBuilder.textColorDisabled);
            }
            if (mBuilder.textColorDefault != SelectorFactory.INVALID) {
                stateList.add(SelectorFactory.STATES[4]);
                colorList.add(mBuilder.textColorDefault);
            }
            ((ITextView) mBuilder.uiView).updateText(new ColorStateList(
                    stateList.toArray(new int[stateList.size()][]),
                    ConvertUtils.convert(colorList.toArray(new Integer[stateList.size()]))));
        }
    }


    /**
     * 更新Drawable
     */
    public void updateDrawable() {
        if (mBuilder.uiView instanceof ITextView) {
            Drawable[] drawables = new Drawable[4];

            drawables[0] = generateDrawable(tintWrapDrawable(mBuilder.drawableLeftDefault, mBuilder.drawableLeftTintDefault),
                    tintWrapDrawable(mBuilder.drawableLeftPressed, mBuilder.drawableLeftTintPressed),
                    tintWrapDrawable(mBuilder.drawableLeftSelected, mBuilder.drawableLeftTintSelected),
                    tintWrapDrawable(mBuilder.drawableLeftFocused, mBuilder.drawableLeftTintFocused),
                    tintWrapDrawable(mBuilder.drawableLeftDisabled, mBuilder.drawableLeftTintDisabled),
                    mBuilder.drawableLeftWidth, mBuilder.drawableLeftHeight);
            drawables[1] = generateDrawable(tintWrapDrawable(mBuilder.drawableTopDefault, mBuilder.drawableTopTintDefault),
                    tintWrapDrawable(mBuilder.drawableTopPressed, mBuilder.drawableTopTintPressed),
                    tintWrapDrawable(mBuilder.drawableTopSelected, mBuilder.drawableTopTintSelected),
                    tintWrapDrawable(mBuilder.drawableTopFocused, mBuilder.drawableTopTintFocused),
                    tintWrapDrawable(mBuilder.drawableTopDisabled, mBuilder.drawableTopTintDisabled),
                    mBuilder.drawableTopWidth, mBuilder.drawableTopHeight);
            drawables[2] = generateDrawable(tintWrapDrawable(mBuilder.drawableRightDefault, mBuilder.drawableRightTintDefault),
                    tintWrapDrawable(mBuilder.drawableRightPressed, mBuilder.drawableRightTintPressed),
                    tintWrapDrawable(mBuilder.drawableRightSelected, mBuilder.drawableRightTintSelected),
                    tintWrapDrawable(mBuilder.drawableRightFocused, mBuilder.drawableRightTintFocused),
                    tintWrapDrawable(mBuilder.drawableRightDisabled, mBuilder.drawableRightTintDisabled),
                    mBuilder.drawableRightWidth, mBuilder.drawableRightHeight);
            drawables[3] = generateDrawable(tintWrapDrawable(mBuilder.drawableBottomDefault, mBuilder.drawableBottomTintDefault),
                    tintWrapDrawable(mBuilder.drawableBottomPressed, mBuilder.drawableBottomTintPressed),
                    tintWrapDrawable(mBuilder.drawableBottomSelected, mBuilder.drawableBottomTintSelected),
                    tintWrapDrawable(mBuilder.drawableBottomFocused, mBuilder.drawableBottomTintFocused),
                    tintWrapDrawable(mBuilder.drawableBottomDisabled, mBuilder.drawableBottomTintDisabled),
                    mBuilder.drawableBottomWidth, mBuilder.drawableBottomHeight);

            ((ITextView) mBuilder.uiView).updateDrawable(drawables);
        }
    }


    /**
     * 更新Drawable对齐位置
     *
     * @param textView
     */
    public void updateDrawableAlign(TextView textView) {
        final Drawable[] compoundDrawables = textView.getCompoundDrawables();
        final int availableWidth = textView.getMeasuredWidth() - textView.getPaddingLeft() - textView.getPaddingRight();
        final int availableHeight = textView.getMeasuredHeight() - textView.getPaddingTop() - textView.getPaddingBottom();
        if (null != compoundDrawables[0] && compoundDrawables[0].getBounds().height() < availableHeight) {
            int topDrawableH = compoundDrawables[1] == null ? 0 : compoundDrawables[1].getBounds().height();
            int bottomDrawableH = compoundDrawables[3] == null ? 0 : compoundDrawables[3].getBounds().height();
            int drawableTop = availableHeight / 2 - ((availableHeight - bottomDrawableH - topDrawableH) / 2 + topDrawableH);
            switch (mBuilder.drawableLeftAlign) {
                case 0:
                    drawableTop -= (availableHeight - compoundDrawables[0].getBounds().height() >> 1);
                    break;
                case 2:
                    drawableTop += (availableHeight - compoundDrawables[0].getBounds().height() >> 1);
                    break;
                default:
                    break;
            }
            compoundDrawables[0].setBounds(compoundDrawables[0].getBounds().left,
                    drawableTop,
                    compoundDrawables[0].getBounds().right,
                    compoundDrawables[0].getBounds().height() + drawableTop);
        }
        if (null != compoundDrawables[1] && compoundDrawables[1].getBounds().width() < availableWidth) {
            int leftDrawableW = compoundDrawables[0] == null ? 0 : compoundDrawables[0].getBounds().width();
            int rightDrawableW = compoundDrawables[2] == null ? 0 : compoundDrawables[2].getBounds().width();
            int drawableLeft = availableWidth / 2 - ((availableWidth - rightDrawableW - leftDrawableW) / 2 + leftDrawableW);
            switch (mBuilder.drawableTopAlign) {
                case 0:
                    drawableLeft -= (availableWidth - compoundDrawables[1].getBounds().width() >> 1);
                    break;
                case 2:
                    drawableLeft += (availableWidth - compoundDrawables[1].getBounds().width() >> 1);
                    break;
                default:
                    break;
            }
            compoundDrawables[1].setBounds(drawableLeft,
                    compoundDrawables[1].getBounds().top,
                    compoundDrawables[1].getBounds().width() + drawableLeft,
                    compoundDrawables[1].getBounds().bottom);
        }
        if (null != compoundDrawables[2] && compoundDrawables[2].getBounds().height() < availableHeight) {
            int topDrawableH = compoundDrawables[1] == null ? 0 : compoundDrawables[1].getBounds().height();
            int bottomDrawableH = compoundDrawables[3] == null ? 0 : compoundDrawables[3].getBounds().height();
            int drawableTop = availableHeight / 2 - ((availableHeight - bottomDrawableH - topDrawableH) / 2 + topDrawableH);
            switch (mBuilder.drawableRightAlign) {
                case 0:
                    drawableTop -= (availableHeight - compoundDrawables[0].getBounds().height() >> 1);
                    break;
                case 2:
                    drawableTop += (availableHeight - compoundDrawables[0].getBounds().height() >> 1);
                    break;
                default:
                    break;
            }
            compoundDrawables[2].setBounds(compoundDrawables[2].getBounds().left,
                    drawableTop,
                    compoundDrawables[2].getBounds().right,
                    compoundDrawables[2].getBounds().height() + drawableTop);
        }
        if (null != compoundDrawables[3] && compoundDrawables[3].getBounds().width() < availableWidth) {
            int leftDrawableW = compoundDrawables[0] == null ? 0 : compoundDrawables[0].getBounds().width();
            int rightDrawableW = compoundDrawables[2] == null ? 0 : compoundDrawables[2].getBounds().width();
            int drawableLeft = availableWidth / 2 - ((availableWidth - rightDrawableW - leftDrawableW) / 2 + leftDrawableW);
            switch (mBuilder.drawableBottomAlign) {
                case 0:
                    drawableLeft -= (availableWidth - compoundDrawables[1].getBounds().width() >> 1);
                    break;
                case 2:
                    drawableLeft += (availableWidth - compoundDrawables[1].getBounds().width() >> 1);
                    break;
                default:
                    break;
            }
            compoundDrawables[3].setBounds(drawableLeft,
                    compoundDrawables[3].getBounds().top,
                    compoundDrawables[3].getBounds().width() + drawableLeft,
                    compoundDrawables[3].getBounds().bottom);
        }
    }

    /**
     * 更新背景
     */
    public void updateSrc() {
        Drawable src = generateDrawable(tintWrapDrawable(mBuilder.srcDefault, mBuilder.srcTintDefault),
                tintWrapDrawable(mBuilder.srcPressed, mBuilder.srcTintPressed),
                tintWrapDrawable(mBuilder.srcSelected, mBuilder.srcTintSelected),
                tintWrapDrawable(mBuilder.srcFocused, mBuilder.srcTintFocused),
                tintWrapDrawable(mBuilder.srcDisabled, mBuilder.srcTintDisabled),
                -1, -1);
        if (mBuilder.uiView instanceof TUIImageView && src != null) {
            ((TUIImageView) mBuilder.uiView).updateSrc(src);
        }
    }

    private static Drawable generateDrawable(Drawable drawableDefault,
                                             Drawable drawablePressed,
                                             Drawable drawableSelected,
                                             Drawable drawableFocused,
                                             Drawable drawableDisabled,
                                             int width, int height) {
        if (drawableDefault == null) {
            return null;
        }
        final int w = width > 0 ? width : drawableDefault.getIntrinsicWidth();
        final int h = height > 0 ? height : drawableDefault.getIntrinsicHeight();
        StateListDrawable drawable = new StateListDrawable();

        if (drawableSelected != null) {
            drawableSelected.setBounds(0, 0, w, h);
            drawable.addState(SelectorFactory.STATES[0], drawableSelected);
        }
        if (drawablePressed != null) {
            drawablePressed.setBounds(0, 0, w, h);
            drawable.addState(SelectorFactory.STATES[1], drawablePressed);
        }
        if (drawableFocused != null) {
            drawableFocused.setBounds(0, 0, w, h);
            drawable.addState(SelectorFactory.STATES[2], drawableFocused);
        }
        if (drawableDisabled != null) {
            drawableDisabled.setBounds(0, 0, w, h);
            drawable.addState(SelectorFactory.STATES[3], drawableDisabled);
        }
        if (DrawableUtils.isStateListDrawableEmpty(drawable)) {
            //只有Default时
            drawableDefault.setBounds(0, 0, w, h);
            return drawableDefault;
        }
        drawable.addState(SelectorFactory.STATES[SelectorFactory.STATES.length - 1], drawableDefault);
        drawable.setBounds(0, 0, w, h);
        return drawable;
    }

    private static Drawable tintWrapDrawable(final Drawable drawable, int tintColor) {
        if (drawable != null && tintColor != SelectorFactory.INVALID) {
            return DrawableUtils.tint(drawable, tintColor);
        }
        return drawable;
    }

    private static GradientDrawable.Orientation generateOrientation(int direction) {
        switch (direction) {
            case 0:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case 1:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case 2:
                return GradientDrawable.Orientation.TL_BR;
            case 3:
                return GradientDrawable.Orientation.BL_TR;
            default:
                return GradientDrawable.Orientation.LEFT_RIGHT;
        }
    }

    private static int[] generateGradientColors(int startColor, int centerColor, int endColor) {
        if (startColor == SelectorFactory.INVALID && centerColor == SelectorFactory.INVALID && endColor == SelectorFactory.INVALID) {
            return null;
        }
        if (centerColor == SelectorFactory.INVALID) {
            return new int[]{startColor == SelectorFactory.INVALID ? Color.TRANSPARENT : startColor,
                    endColor == SelectorFactory.INVALID ? Color.TRANSPARENT : endColor};
        } else {
            return new int[]{startColor == SelectorFactory.INVALID ? Color.TRANSPARENT : startColor,
                    centerColor,
                    endColor == SelectorFactory.INVALID ? Color.TRANSPARENT : endColor};
        }
    }

    public static final class Builder<T extends IView> {
        private T uiView;
        private TypedArray typedArray;

        private int bgSolidDefault = SelectorFactory.INVALID;
        private int bgSolidPressed = SelectorFactory.INVALID;
        private int bgSolidSelected = SelectorFactory.INVALID;
        private int bgSolidFocused = SelectorFactory.INVALID;
        private int bgSolidDisabled = SelectorFactory.INVALID;

        private int bgStrokeDefault = 0;
        private int bgStrokePressed = 0;
        private int bgStrokeSelected = 0;
        private int bgStrokeFocused = 0;
        private int bgStrokeDisabled = 0;

        private int bgStrokeColorDefault = SelectorFactory.INVALID;
        private int bgStrokeColorPressed = SelectorFactory.INVALID;
        private int bgStrokeColorSelected = SelectorFactory.INVALID;
        private int bgStrokeColorFocused = SelectorFactory.INVALID;
        private int bgStrokeColorDisabled = SelectorFactory.INVALID;

        private int bgCorner = 0;
        private int bgCornerTopLeft = 0;
        private int bgCornerTopRight = 0;
        private int bgCornerBottomRight = 0;
        private int bgCornerBottomLeft = 0;

        private int bgRipplePressed = SelectorFactory.INVALID;

        private int bgGradientStartColorDefault = SelectorFactory.INVALID;
        private int bgGradientStartColorPressed = SelectorFactory.INVALID;
        private int bgGradientStartColorSelected = SelectorFactory.INVALID;
        private int bgGradientStartColorFocused = SelectorFactory.INVALID;
        private int bgGradientStartColorDisabled = SelectorFactory.INVALID;

        private int bgGradientCenterColorDefault = SelectorFactory.INVALID;
        private int bgGradientCenterColorPressed = SelectorFactory.INVALID;
        private int bgGradientCenterColorSelected = SelectorFactory.INVALID;
        private int bgGradientCenterColorFocused = SelectorFactory.INVALID;
        private int bgGradientCenterColorDisabled = SelectorFactory.INVALID;

        private int bgGradientEndColorDefault = SelectorFactory.INVALID;
        private int bgGradientEndColorPressed = SelectorFactory.INVALID;
        private int bgGradientEndColorSelected = SelectorFactory.INVALID;
        private int bgGradientEndColorFocused = SelectorFactory.INVALID;
        private int bgGradientEndColorDisabled = SelectorFactory.INVALID;

        private GradientDrawable.Orientation bgGradientDirectionDefault = TOP_BOTTOM;
        private GradientDrawable.Orientation bgGradientDirectionPressed = TOP_BOTTOM;
        private GradientDrawable.Orientation bgGradientDirectionSelected = TOP_BOTTOM;
        private GradientDrawable.Orientation bgGradientDirectionFocused = TOP_BOTTOM;
        private GradientDrawable.Orientation bgGradientDirectionDisabled = TOP_BOTTOM;

        private int textColorDefault = SelectorFactory.INVALID;
        private int textColorPressed = SelectorFactory.INVALID;
        private int textColorSelected = SelectorFactory.INVALID;
        private int textColorFocused = SelectorFactory.INVALID;
        private int textColorDisabled = SelectorFactory.INVALID;

        private int drawableLeftWidth = 0;
        private int drawableLeftHeight = 0;
        private int drawableLeftAlign = 1;
        private Drawable drawableLeftDefault = null;
        private int drawableLeftTintDefault = SelectorFactory.INVALID;
        private Drawable drawableLeftPressed = null;
        private int drawableLeftTintPressed = SelectorFactory.INVALID;
        private Drawable drawableLeftSelected = null;
        private int drawableLeftTintSelected = SelectorFactory.INVALID;
        private Drawable drawableLeftFocused = null;
        private int drawableLeftTintFocused = SelectorFactory.INVALID;
        private Drawable drawableLeftDisabled = null;
        private int drawableLeftTintDisabled = SelectorFactory.INVALID;

        private int drawableTopWidth = 0;
        private int drawableTopHeight = 0;
        private int drawableTopAlign = 1;
        private Drawable drawableTopDefault = null;
        private int drawableTopTintDefault = SelectorFactory.INVALID;
        private Drawable drawableTopPressed = null;
        private int drawableTopTintPressed = SelectorFactory.INVALID;
        private Drawable drawableTopSelected = null;
        private int drawableTopTintSelected = SelectorFactory.INVALID;
        private Drawable drawableTopFocused = null;
        private int drawableTopTintFocused = SelectorFactory.INVALID;
        private Drawable drawableTopDisabled = null;
        private int drawableTopTintDisabled = SelectorFactory.INVALID;

        private int drawableRightWidth = 0;
        private int drawableRightHeight = 0;
        private int drawableRightAlign = 1;
        private Drawable drawableRightDefault = null;
        private int drawableRightTintDefault = SelectorFactory.INVALID;
        private Drawable drawableRightPressed = null;
        private int drawableRightTintPressed = SelectorFactory.INVALID;
        private Drawable drawableRightSelected = null;
        private int drawableRightTintSelected = SelectorFactory.INVALID;
        private Drawable drawableRightFocused = null;
        private int drawableRightTintFocused = SelectorFactory.INVALID;
        private Drawable drawableRightDisabled = null;
        private int drawableRightTintDisabled = SelectorFactory.INVALID;

        private int drawableBottomWidth = 0;
        private int drawableBottomHeight = 0;
        private int drawableBottomAlign = 1;
        private Drawable drawableBottomDefault = null;
        private int drawableBottomTintDefault = SelectorFactory.INVALID;
        private Drawable drawableBottomPressed = null;
        private int drawableBottomTintPressed = SelectorFactory.INVALID;
        private Drawable drawableBottomSelected = null;
        private int drawableBottomTintSelected = SelectorFactory.INVALID;
        private Drawable drawableBottomFocused = null;
        private int drawableBottomTintFocused = SelectorFactory.INVALID;
        private Drawable drawableBottomDisabled = null;
        private int drawableBottomTintDisabled = SelectorFactory.INVALID;

        private Drawable srcDefault = null;
        private int srcTintDefault = SelectorFactory.INVALID;
        private Drawable srcPressed = null;
        private int srcTintPressed = SelectorFactory.INVALID;
        private Drawable srcSelected = null;
        private int srcTintSelected = SelectorFactory.INVALID;
        private Drawable srcFocused = null;
        private int srcTintFocused = SelectorFactory.INVALID;
        private Drawable srcDisabled = null;
        private int srcTintDisabled = SelectorFactory.INVALID;

        public Builder(T uiView, TypedArray typedArray) {
            this.uiView = uiView;
            this.typedArray = typedArray;
        }

        public Builder<T> bgSolidDefault(@StyleableRes int bgSolidDefault) {
            this.bgSolidDefault = typedArray.getColor(bgSolidDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgSolidPressed(@StyleableRes int bgSolidPressed) {
            this.bgSolidPressed = typedArray.getColor(bgSolidPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgSolidSelected(@StyleableRes int bgSolidSelected) {
            this.bgSolidSelected = typedArray.getColor(bgSolidSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgSolidFocused(@StyleableRes int bgSolidFocused) {
            this.bgSolidFocused = typedArray.getColor(bgSolidFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgSolidDisabled(@StyleableRes int bgSolidDisabled) {
            this.bgSolidDisabled = typedArray.getColor(bgSolidDisabled, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgStrokeDefault(@StyleableRes int bgStrokeDefault) {
            this.bgStrokeDefault = typedArray.getDimensionPixelOffset(bgStrokeDefault, 0);
            return this;
        }

        public Builder<T> bgStrokePressed(@StyleableRes int bgStrokePressed) {
            this.bgStrokePressed = typedArray.getDimensionPixelOffset(bgStrokePressed, 0);
            return this;
        }

        public Builder<T> bgStrokeSelected(@StyleableRes int bgStrokeSelected) {
            this.bgStrokeSelected = typedArray.getDimensionPixelOffset(bgStrokeSelected, 0);
            return this;
        }

        public Builder<T> bgStrokeFocused(@StyleableRes int bgStrokeFocused) {
            this.bgStrokeFocused = typedArray.getDimensionPixelOffset(bgStrokeFocused, 0);
            return this;
        }

        public Builder<T> bgStrokeDisabled(@StyleableRes int bgStrokeDisabled) {
            this.bgStrokeDisabled = typedArray.getDimensionPixelOffset(bgStrokeDisabled, 0);
            return this;
        }

        public Builder<T> bgStrokeColorDefault(@StyleableRes int bgStrokeColorDefault) {
            this.bgStrokeColorDefault = typedArray.getColor(bgStrokeColorDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgStrokeColorPressed(@StyleableRes int bgStrokeColorPressed) {
            this.bgStrokeColorPressed = typedArray.getColor(bgStrokeColorPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgStrokeColorSelected(@StyleableRes int bgStrokeColorSelected) {
            this.bgStrokeColorSelected = typedArray.getColor(bgStrokeColorSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgStrokeColorFocused(@StyleableRes int bgStrokeColorFocused) {
            this.bgStrokeColorFocused = typedArray.getColor(bgStrokeColorFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgStrokeColorDisabled(@StyleableRes int bgStrokeColorDisabled) {
            this.bgStrokeColorDisabled = typedArray.getColor(bgStrokeColorDisabled, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgCorner(@StyleableRes int bgCorner) {
            this.bgCorner = typedArray.getDimensionPixelOffset(bgCorner, 0);
            return this;
        }

        public Builder<T> bgCornerTopLeft(@StyleableRes int bgCornerTopLeft) {
            this.bgCornerTopLeft = typedArray.getDimensionPixelOffset(bgCornerTopLeft, 0);
            return this;
        }

        public Builder<T> bgCornerTopRight(@StyleableRes int bgCornerTopRight) {
            this.bgCornerTopRight = typedArray.getDimensionPixelOffset(bgCornerTopRight, 0);
            return this;
        }

        public Builder<T> bgCornerBottomRight(@StyleableRes int bgCornerBottomRight) {
            this.bgCornerBottomRight = typedArray.getDimensionPixelOffset(bgCornerBottomRight, 0);
            return this;
        }

        public Builder<T> bgCornerBottomLeft(@StyleableRes int bgCornerBottomLeft) {
            this.bgCornerBottomLeft = typedArray.getDimensionPixelOffset(bgCornerBottomLeft, 0);
            return this;
        }

        public Builder<T> bgRipplePressed(@StyleableRes int bgRipplePressed) {
            this.bgRipplePressed = typedArray.getColor(bgRipplePressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientStartColorDefault(@StyleableRes int bgGradientStartColorDefault) {
            this.bgGradientStartColorDefault = typedArray.getColor(bgGradientStartColorDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientStartColorPressed(@StyleableRes int bgGradientStartColorPressed) {
            this.bgGradientStartColorPressed = typedArray.getColor(bgGradientStartColorPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientStartColorSelected(@StyleableRes int bgGradientStartColorSelected) {
            this.bgGradientStartColorSelected = typedArray.getColor(bgGradientStartColorSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientStartColorFocused(@StyleableRes int bgGradientStartColorFocused) {
            this.bgGradientStartColorFocused = typedArray.getColor(bgGradientStartColorFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientStartColorDisabled(@StyleableRes int bgGradientStartColorDisabled) {
            this.bgGradientStartColorDisabled = typedArray.getColor(bgGradientStartColorDisabled, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientCenterColorDefault(@StyleableRes int bgGradientCenterColorDefault) {
            this.bgGradientCenterColorDefault = typedArray.getColor(bgGradientCenterColorDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientCenterColorPressed(@StyleableRes int bgGradientCenterColorPressed) {
            this.bgGradientCenterColorPressed = typedArray.getColor(bgGradientCenterColorPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientCenterColorSelected(@StyleableRes int bgGradientCenterColorSelected) {
            this.bgGradientCenterColorSelected = typedArray.getColor(bgGradientCenterColorSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientCenterColorFocused(@StyleableRes int bgGradientCenterColorFocused) {
            this.bgGradientCenterColorFocused = typedArray.getColor(bgGradientCenterColorFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientCenterColorDisabled(@StyleableRes int bgGradientCenterColorDisabled) {
            this.bgGradientCenterColorDisabled = typedArray.getColor(bgGradientCenterColorDisabled, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientEndColorDefault(@StyleableRes int bgGradientEndColorDefault) {
            this.bgGradientEndColorDefault = typedArray.getColor(bgGradientEndColorDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientEndColorPressed(@StyleableRes int bgGradientEndColorPressed) {
            this.bgGradientEndColorPressed = typedArray.getColor(bgGradientEndColorPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientEndColorSelected(@StyleableRes int bgGradientEndColorSelected) {
            this.bgGradientEndColorSelected = typedArray.getColor(bgGradientEndColorSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientEndColorFocused(@StyleableRes int bgGradientEndColorFocused) {
            this.bgGradientEndColorFocused = typedArray.getColor(bgGradientEndColorFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientEndColorDisabled(@StyleableRes int bgGradientEndColorDisabled) {
            this.bgGradientEndColorDisabled = typedArray.getColor(bgGradientEndColorDisabled, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> bgGradientDirectionDefault(@StyleableRes int bgGradientDirectionDefault) {
            this.bgGradientDirectionDefault = generateOrientation(typedArray.getInt(bgGradientDirectionDefault, 0));
            return this;
        }

        public Builder<T> bgGradientDirectionPressed(@StyleableRes int bgGradientDirectionPressed) {
            this.bgGradientDirectionPressed = generateOrientation(typedArray.getInt(bgGradientDirectionPressed, 0));
            return this;
        }

        public Builder<T> bgGradientDirectionSelected(@StyleableRes int bgGradientDirectionSelected) {
            this.bgGradientDirectionSelected = generateOrientation(typedArray.getInt(bgGradientDirectionSelected, 0));
            return this;
        }

        public Builder<T> bgGradientDirectionFocused(@StyleableRes int bgGradientDirectionFocused) {
            this.bgGradientDirectionFocused = generateOrientation(typedArray.getInt(bgGradientDirectionFocused, 0));
            return this;
        }

        public Builder<T> bgGradientDirectionDisabled(@StyleableRes int bgGradientDirectionDisabled) {
            this.bgGradientDirectionDisabled = generateOrientation(typedArray.getInt(bgGradientDirectionDisabled, 0));
            return this;
        }

        public Builder<T> textColorDefault(@StyleableRes int textColorDefault) {
            this.textColorDefault = typedArray.getColor(textColorDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> textColorPressed(@StyleableRes int textColorPressed) {
            this.textColorPressed = typedArray.getColor(textColorPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> textColorSelected(@StyleableRes int textColorSelected) {
            this.textColorSelected = typedArray.getColor(textColorSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> textColorFocused(@StyleableRes int textColorFocused) {
            this.textColorFocused = typedArray.getColor(textColorFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> textColorDisabled(@StyleableRes int textColorDisabled) {
            this.textColorDisabled = typedArray.getColor(textColorDisabled, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableLeftWidth(@StyleableRes int drawableLeftWidth) {
            this.drawableLeftWidth = typedArray.getDimensionPixelOffset(drawableLeftWidth, 0);
            return this;
        }

        public Builder<T> drawableLeftHeight(@StyleableRes int drawableLeftHeight) {
            this.drawableLeftHeight = typedArray.getDimensionPixelOffset(drawableLeftHeight, 0);
            return this;
        }

        public Builder<T> drawableLeftAlign(@StyleableRes int drawableLeftAlign) {
            this.drawableLeftAlign = typedArray.getInt(drawableLeftAlign, 1);
            return this;
        }

        public Builder<T> drawableLeftDefault(@StyleableRes int drawableLeftDefault) {
            this.drawableLeftDefault = DrawableUtils.getTintDrawable(typedArray, drawableLeftDefault);
            return this;
        }

        public Builder<T> drawableLeftTintDefault(@StyleableRes int drawableLeftTintDefault) {
            this.drawableLeftTintDefault = typedArray.getColor(drawableLeftTintDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableLeftPressed(@StyleableRes int drawableLeftPressed) {
            this.drawableLeftPressed = DrawableUtils.getTintDrawable(typedArray, drawableLeftPressed);
            return this;
        }

        public Builder<T> drawableLeftTintPressed(@StyleableRes int drawableLeftTintPressed) {
            this.drawableLeftTintPressed = typedArray.getColor(drawableLeftTintPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableLeftSelected(@StyleableRes int drawableLeftSelected) {
            this.drawableLeftSelected = DrawableUtils.getTintDrawable(typedArray, drawableLeftSelected);
            return this;
        }

        public Builder<T> drawableLeftTintSelected(@StyleableRes int drawableLeftTintSelected) {
            this.drawableLeftTintSelected = typedArray.getColor(drawableLeftTintSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableLeftFocused(@StyleableRes int drawableLeftFocused) {
            this.drawableLeftFocused = DrawableUtils.getTintDrawable(typedArray, drawableLeftFocused);
            return this;
        }

        public Builder<T> drawableLeftTintFocused(@StyleableRes int drawableLeftTintFocused) {
            this.drawableLeftTintFocused = typedArray.getColor(drawableLeftTintFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableLeftDisabled(@StyleableRes int drawableLeftDisabled) {
            this.drawableLeftDisabled = DrawableUtils.getTintDrawable(typedArray, drawableLeftDisabled);
            return this;
        }

        public Builder<T> drawableLeftTintDisabled(@StyleableRes int drawableLeftTintDisabled) {
            this.drawableLeftTintDisabled = typedArray.getColor(drawableLeftTintDisabled, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableTopWidth(@StyleableRes int drawableTopWidth) {
            this.drawableTopWidth = typedArray.getDimensionPixelOffset(drawableTopWidth, 0);
            return this;
        }

        public Builder<T> drawableTopHeight(@StyleableRes int drawableTopHeight) {
            this.drawableTopHeight = typedArray.getDimensionPixelOffset(drawableTopHeight, 0);
            return this;
        }

        public Builder<T> drawableTopAlign(@StyleableRes int drawableTopAlign) {
            this.drawableTopAlign = typedArray.getInt(drawableTopAlign, 1);
            return this;
        }

        public Builder<T> drawableTopDefault(@StyleableRes int drawableTopDefault) {
            this.drawableTopDefault = DrawableUtils.getTintDrawable(typedArray, drawableTopDefault);
            return this;
        }

        public Builder<T> drawableTopTintDefault(@StyleableRes int drawableTopTintDefault) {
            this.drawableTopTintDefault = typedArray.getColor(drawableTopTintDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableTopPressed(@StyleableRes int drawableTopPressed) {
            this.drawableTopPressed = DrawableUtils.getTintDrawable(typedArray, drawableTopPressed);
            return this;
        }

        public Builder<T> drawableTopTintPressed(@StyleableRes int drawableTopTintPressed) {
            this.drawableTopTintPressed = typedArray.getColor(drawableTopTintPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableTopSelected(@StyleableRes int drawableTopSelected) {
            this.drawableTopSelected = DrawableUtils.getTintDrawable(typedArray, drawableTopSelected);
            return this;
        }

        public Builder<T> drawableTopTintSelected(@StyleableRes int drawableTopTintSelected) {
            this.drawableTopTintSelected = typedArray.getColor(drawableTopTintSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableTopFocused(@StyleableRes int drawableTopFocused) {
            this.drawableTopFocused = DrawableUtils.getTintDrawable(typedArray, drawableTopFocused);
            return this;
        }

        public Builder<T> drawableTopTintFocused(@StyleableRes int drawableTopTintFocused) {
            this.drawableTopTintFocused = typedArray.getColor(drawableTopTintFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableTopDisabled(@StyleableRes int drawableTopDisabled) {
            this.drawableTopDisabled = DrawableUtils.getTintDrawable(typedArray, drawableTopDisabled);
            return this;
        }

        public Builder<T> drawableTopTintDisabled(@StyleableRes int drawableTopTintDisabled) {
            this.drawableTopTintDisabled = typedArray.getColor(drawableTopTintDisabled, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableRightWidth(@StyleableRes int drawableRightWidth) {
            this.drawableRightWidth = typedArray.getDimensionPixelOffset(drawableRightWidth, 0);
            return this;
        }

        public Builder<T> drawableRightHeight(@StyleableRes int drawableRightHeight) {
            this.drawableRightHeight = typedArray.getDimensionPixelOffset(drawableRightHeight, 0);
            return this;
        }

        public Builder<T> drawableRightAlign(@StyleableRes int drawableRightAlign) {
            this.drawableRightAlign = typedArray.getInt(drawableRightAlign, 1);
            return this;
        }

        public Builder<T> drawableRightDefault(@StyleableRes int drawableRightDefault) {
            this.drawableRightDefault = DrawableUtils.getTintDrawable(typedArray, drawableRightDefault);
            return this;
        }

        public Builder<T> drawableRightTintDefault(@StyleableRes int drawableRightTintDefault) {
            this.drawableRightTintDefault = typedArray.getColor(drawableRightTintDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableRightPressed(@StyleableRes int drawableRightPressed) {
            this.drawableRightPressed = DrawableUtils.getTintDrawable(typedArray, drawableRightPressed);
            return this;
        }

        public Builder<T> drawableRightTintPressed(@StyleableRes int drawableRightTintPressed) {
            this.drawableRightTintPressed = typedArray.getColor(drawableRightTintPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableRightSelected(@StyleableRes int drawableRightSelected) {
            this.drawableRightSelected = DrawableUtils.getTintDrawable(typedArray, drawableRightSelected);
            return this;
        }

        public Builder<T> drawableRightTintSelected(@StyleableRes int drawableRightTintSelected) {
            this.drawableRightTintSelected = typedArray.getColor(drawableRightTintSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableRightFocused(@StyleableRes int drawableRightFocused) {
            this.drawableRightFocused = DrawableUtils.getTintDrawable(typedArray, drawableRightFocused);
            return this;
        }

        public Builder<T> drawableRightTintFocused(@StyleableRes int drawableRightTintFocused) {
            this.drawableRightTintFocused = typedArray.getColor(drawableRightTintFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableRightDisabled(@StyleableRes int drawableRightDisabled) {
            this.drawableRightDisabled = DrawableUtils.getTintDrawable(typedArray, drawableRightDisabled);
            return this;
        }

        public Builder<T> drawableRightTintDisabled(@StyleableRes int drawableRightTintDisabled) {
            this.drawableRightTintDisabled = typedArray.getColor(drawableRightTintDisabled, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableBottomWidth(@StyleableRes int drawableBottomWidth) {
            this.drawableBottomWidth = typedArray.getDimensionPixelOffset(drawableBottomWidth, 0);
            return this;
        }

        public Builder<T> drawableBottomHeight(@StyleableRes int drawableBottomHeight) {
            this.drawableBottomHeight = typedArray.getDimensionPixelOffset(drawableBottomHeight, 0);
            return this;
        }

        public Builder<T> drawableBottomAlign(@StyleableRes int drawableBottomAlign) {
            this.drawableBottomAlign = typedArray.getInt(drawableBottomAlign, 1);
            return this;
        }

        public Builder<T> drawableBottomDefault(@StyleableRes int drawableBottomDefault) {
            this.drawableBottomDefault = DrawableUtils.getTintDrawable(typedArray, drawableBottomDefault);
            return this;
        }

        public Builder<T> drawableBottomTintDefault(@StyleableRes int drawableBottomTintDefault) {
            this.drawableBottomTintDefault = typedArray.getColor(drawableBottomTintDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableBottomPressed(@StyleableRes int drawableBottomPressed) {
            this.drawableBottomPressed = DrawableUtils.getTintDrawable(typedArray, drawableBottomPressed);
            return this;
        }

        public Builder<T> drawableBottomTintPressed(@StyleableRes int drawableBottomTintPressed) {
            this.drawableBottomTintPressed = typedArray.getColor(drawableBottomTintPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableBottomSelected(@StyleableRes int drawableBottomSelected) {
            this.drawableBottomSelected = DrawableUtils.getTintDrawable(typedArray, drawableBottomSelected);
            return this;
        }

        public Builder<T> drawableBottomTintSelected(@StyleableRes int drawableBottomTintSelected) {
            this.drawableBottomTintSelected = typedArray.getColor(drawableBottomTintSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableBottomFocused(@StyleableRes int drawableBottomFocused) {
            this.drawableBottomFocused = DrawableUtils.getTintDrawable(typedArray, drawableBottomFocused);
            return this;
        }

        public Builder<T> drawableBottomTintFocused(@StyleableRes int drawableBottomTintFocused) {
            this.drawableBottomTintFocused = typedArray.getColor(drawableBottomTintFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableBottomDisabled(@StyleableRes int drawableBottomDisabled) {
            this.drawableBottomDisabled = DrawableUtils.getTintDrawable(typedArray, drawableBottomDisabled);
            return this;
        }

        public Builder<T> drawableBottomTintDisabled(@StyleableRes int drawableBottomTintDisabled) {
            this.drawableBottomTintDisabled = typedArray.getColor(drawableBottomTintDisabled, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> srcDefault(@StyleableRes int srcDefault) {
            this.srcDefault = DrawableUtils.getTintDrawable(typedArray, srcDefault);
            return this;
        }

        public Builder<T> srcTintDefault(@StyleableRes int srcTintDefault) {
            this.srcTintDefault = typedArray.getColor(srcTintDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> srcPressed(@StyleableRes int srcPressed) {
            this.srcPressed = DrawableUtils.getTintDrawable(typedArray, srcPressed);
            return this;
        }

        public Builder<T> srcTintPressed(@StyleableRes int srcTintPressed) {
            this.srcTintPressed = typedArray.getColor(srcTintPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> srcSelected(@StyleableRes int srcSelected) {
            this.srcSelected = DrawableUtils.getTintDrawable(typedArray, srcSelected);
            return this;
        }

        public Builder<T> srcTintSelected(@StyleableRes int srcTintSelected) {
            this.srcTintSelected = typedArray.getColor(srcTintSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> srcFocused(@StyleableRes int srcFocused) {
            this.srcFocused = DrawableUtils.getTintDrawable(typedArray, srcFocused);
            return this;
        }

        public Builder<T> srcTintFocused(@StyleableRes int srcTintFocused) {
            this.srcTintFocused = typedArray.getColor(srcTintFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> srcDisabled(@StyleableRes int srcDisabled) {
            this.srcDisabled = DrawableUtils.getTintDrawable(typedArray, srcDisabled);
            return this;
        }

        public Builder<T> srcTintDisabled(@StyleableRes int srcTintDisabled) {
            this.srcTintDisabled = typedArray.getColor(srcTintDisabled, SelectorFactory.INVALID);
            return this;
        }

        public TUIHelper<T> build() {
            return new TUIHelper<T>(this);
        }
    }
}
