package com.tk.tdroid.view.tui;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.StyleableRes;
import android.widget.TextView;

import com.tk.tdroid.utils.DrawableUtils;
import com.tk.tdroid.utils.SelectorFactory;

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

    private TUI uiView;

    private int bgSolidDefault;
    private int bgSolidPressed;
    private int bgSolidSelected;
    private int bgSolidFocused;
    private int bgSolidDisabled;

    private int bgStrokeDefault;
    private int bgStrokePressed;
    private int bgStrokeSelected;
    private int bgStrokeFocused;
    private int bgStrokeDisabled;

    private int bgStrokeColorDefault;
    private int bgStrokeColorPressed;
    private int bgStrokeColorSelected;
    private int bgStrokeColorFocused;
    private int bgStrokeColorDisabled;

    private int bgCorner;
    private int bgCornerTopLeft;
    private int bgCornerTopRight;
    private int bgCornerBottomRight;
    private int bgCornerBottomLeft;

    private int bgRipplePressed;

    private int bgGradientStartColorDefault;
    private int bgGradientStartColorPressed;
    private int bgGradientStartColorSelected;
    private int bgGradientStartColorFocused;
    private int bgGradientStartColorDisabled;

    private int bgGradientCenterColorDefault;
    private int bgGradientCenterColorPressed;
    private int bgGradientCenterColorSelected;
    private int bgGradientCenterColorFocused;
    private int bgGradientCenterColorDisabled;

    private int bgGradientEndColorDefault;
    private int bgGradientEndColorPressed;
    private int bgGradientEndColorSelected;
    private int bgGradientEndColorFocused;
    private int bgGradientEndColorDisabled;

    private int bgGradientDirectionDefault;
    private int bgGradientDirectionPressed;
    private int bgGradientDirectionSelected;
    private int bgGradientDirectionFocused;
    private int bgGradientDirectionDisabled;

    private int textColorDefault;
    private int textColorPressed;
    private int textColorSelected;
    private int textColorFocused;
    private int textColorDisabled;

    private int drawableLeftWidth;
    private int drawableLeftHeight;
    private int drawableLeftAlign;
    private Drawable drawableLeftDefault;
    private int drawableLeftTintDefault;
    private Drawable drawableLeftPressed;
    private int drawableLeftTintPressed;
    private Drawable drawableLeftSelected;
    private int drawableLeftTintSelected;
    private Drawable drawableLeftFocused;
    private int drawableLeftTintFocused;
    private Drawable drawableLeftDisabled;
    private int drawableLeftTintDisabled;

    private int drawableTopWidth;
    private int drawableTopHeight;
    private int drawableTopAlign;
    private Drawable drawableTopDefault;
    private int drawableTopTintDefault;
    private Drawable drawableTopPressed;
    private int drawableTopTintPressed;
    private Drawable drawableTopSelected;
    private int drawableTopTintSelected;
    private Drawable drawableTopFocused;
    private int drawableTopTintFocused;
    private Drawable drawableTopDisabled;
    private int drawableTopTintDisabled;

    private int drawableRightWidth;
    private int drawableRightHeight;
    private int drawableRightAlign;
    private Drawable drawableRightDefault;
    private int drawableRightTintDefault;
    private Drawable drawableRightPressed;
    private int drawableRightTintPressed;
    private Drawable drawableRightSelected;
    private int drawableRightTintSelected;
    private Drawable drawableRightFocused;
    private int drawableRightTintFocused;
    private Drawable drawableRightDisabled;
    private int drawableRightTintDisabled;

    private int drawableBottomWidth;
    private int drawableBottomHeight;
    private int drawableBottomAlign;
    private Drawable drawableBottomDefault;
    private int drawableBottomTintDefault;
    private Drawable drawableBottomPressed;
    private int drawableBottomTintPressed;
    private Drawable drawableBottomSelected;
    private int drawableBottomTintSelected;
    private Drawable drawableBottomFocused;
    private int drawableBottomTintFocused;
    private Drawable drawableBottomDisabled;
    private int drawableBottomTintDisabled;

    private Drawable srcDefault;
    private int srcTintDefault;
    private Drawable srcPressed;
    private int srcTintPressed;
    private Drawable srcSelected;
    private int srcTintSelected;
    private Drawable srcFocused;
    private int srcTintFocused;
    private Drawable srcDisabled;
    private int srcTintDisabled;

    private TUIHelper(Builder<TUI> builder) {
        uiView = builder.uiView;
        bgSolidDefault = builder.bgSolidDefault;
        bgSolidPressed = builder.bgSolidPressed;
        bgSolidSelected = builder.bgSolidSelected;
        bgSolidFocused = builder.bgSolidFocused;
        bgSolidDisabled = builder.bgSolidDisabled;

        bgStrokeDefault = builder.bgStrokeDefault;
        bgStrokePressed = builder.bgStrokePressed;
        bgStrokeSelected = builder.bgStrokeSelected;
        bgStrokeFocused = builder.bgStrokeFocused;
        bgStrokeDisabled = builder.bgStrokeDisabled;

        bgStrokeColorDefault = builder.bgStrokeColorDefault;
        bgStrokeColorPressed = builder.bgStrokeColorPressed;
        bgStrokeColorSelected = builder.bgStrokeColorSelected;
        bgStrokeColorFocused = builder.bgStrokeColorFocused;
        bgStrokeColorDisabled = builder.bgStrokeColorDisabled;

        bgCorner = builder.bgCorner;
        bgCornerTopLeft = builder.bgCornerTopLeft;
        bgCornerTopRight = builder.bgCornerTopRight;
        bgCornerBottomRight = builder.bgCornerBottomRight;
        bgCornerBottomLeft = builder.bgCornerBottomLeft;

        bgRipplePressed = builder.bgRipplePressed;

        bgGradientStartColorDefault = builder.bgGradientStartColorDefault;
        bgGradientStartColorPressed = builder.bgGradientStartColorPressed;
        bgGradientStartColorSelected = builder.bgGradientStartColorSelected;
        bgGradientStartColorFocused = builder.bgGradientStartColorFocused;
        bgGradientStartColorDisabled = builder.bgGradientStartColorDisabled;

        bgGradientCenterColorDefault = builder.bgGradientCenterColorDefault;
        bgGradientCenterColorPressed = builder.bgGradientCenterColorPressed;
        bgGradientCenterColorSelected = builder.bgGradientCenterColorSelected;
        bgGradientCenterColorFocused = builder.bgGradientCenterColorFocused;
        bgGradientCenterColorDisabled = builder.bgGradientCenterColorDisabled;

        bgGradientEndColorDefault = builder.bgGradientEndColorDefault;
        bgGradientEndColorPressed = builder.bgGradientEndColorPressed;
        bgGradientEndColorSelected = builder.bgGradientEndColorSelected;
        bgGradientEndColorFocused = builder.bgGradientEndColorFocused;
        bgGradientEndColorDisabled = builder.bgGradientEndColorDisabled;

        bgGradientDirectionDefault = builder.bgGradientDirectionDefault;
        bgGradientDirectionPressed = builder.bgGradientDirectionPressed;
        bgGradientDirectionSelected = builder.bgGradientDirectionSelected;
        bgGradientDirectionFocused = builder.bgGradientDirectionFocused;
        bgGradientDirectionDisabled = builder.bgGradientDirectionDisabled;

        textColorDefault = builder.textColorDefault;
        textColorPressed = builder.textColorPressed;
        textColorSelected = builder.textColorSelected;
        textColorFocused = builder.textColorFocused;
        textColorDisabled = builder.textColorDisabled;

        drawableLeftWidth = builder.drawableLeftWidth;
        drawableLeftHeight = builder.drawableLeftHeight;
        drawableLeftAlign = builder.drawableLeftAlign;
        drawableLeftDefault = builder.drawableLeftDefault;
        drawableLeftTintDefault = builder.drawableLeftTintDefault;
        drawableLeftPressed = builder.drawableLeftPressed;
        drawableLeftTintPressed = builder.drawableLeftTintPressed;
        drawableLeftSelected = builder.drawableLeftSelected;
        drawableLeftTintSelected = builder.drawableLeftTintSelected;
        drawableLeftFocused = builder.drawableLeftFocused;
        drawableLeftTintFocused = builder.drawableLeftTintFocused;
        drawableLeftDisabled = builder.drawableLeftDisabled;
        drawableLeftTintDisabled = builder.drawableLeftTintDisabled;

        drawableTopWidth = builder.drawableTopWidth;
        drawableTopHeight = builder.drawableTopHeight;
        drawableTopAlign = builder.drawableTopAlign;
        drawableTopDefault = builder.drawableTopDefault;
        drawableTopTintDefault = builder.drawableTopTintDefault;
        drawableTopPressed = builder.drawableTopPressed;
        drawableTopTintPressed = builder.drawableTopTintPressed;
        drawableTopSelected = builder.drawableTopSelected;
        drawableTopTintSelected = builder.drawableTopTintSelected;
        drawableTopFocused = builder.drawableTopFocused;
        drawableTopTintFocused = builder.drawableTopTintFocused;
        drawableTopDisabled = builder.drawableTopDisabled;
        drawableTopTintDisabled = builder.drawableTopTintDisabled;

        drawableRightWidth = builder.drawableRightWidth;
        drawableRightHeight = builder.drawableRightHeight;
        drawableRightAlign = builder.drawableRightAlign;
        drawableRightDefault = builder.drawableRightDefault;
        drawableRightTintDefault = builder.drawableRightTintDefault;
        drawableRightPressed = builder.drawableRightPressed;
        drawableRightTintPressed = builder.drawableRightTintPressed;
        drawableRightSelected = builder.drawableRightSelected;
        drawableRightTintSelected = builder.drawableRightTintSelected;
        drawableRightFocused = builder.drawableRightFocused;
        drawableRightTintFocused = builder.drawableRightTintFocused;
        drawableRightDisabled = builder.drawableRightDisabled;
        drawableRightTintDisabled = builder.drawableRightTintDisabled;

        drawableBottomWidth = builder.drawableBottomWidth;
        drawableBottomHeight = builder.drawableBottomHeight;
        drawableBottomAlign = builder.drawableBottomAlign;
        drawableBottomDefault = builder.drawableBottomDefault;
        drawableBottomTintDefault = builder.drawableBottomTintDefault;
        drawableBottomPressed = builder.drawableBottomPressed;
        drawableBottomTintPressed = builder.drawableBottomTintPressed;
        drawableBottomSelected = builder.drawableBottomSelected;
        drawableBottomTintSelected = builder.drawableBottomTintSelected;
        drawableBottomFocused = builder.drawableBottomFocused;
        drawableBottomTintFocused = builder.drawableBottomTintFocused;
        drawableBottomDisabled = builder.drawableBottomDisabled;
        drawableBottomTintDisabled = builder.drawableBottomTintDisabled;

        srcDefault = builder.srcDefault;
        srcTintDefault = builder.srcTintDefault;
        srcPressed = builder.srcPressed;
        srcTintPressed = builder.srcTintPressed;
        srcSelected = builder.srcSelected;
        srcTintSelected = builder.srcTintSelected;
        srcFocused = builder.srcFocused;
        srcTintFocused = builder.srcTintFocused;
        srcDisabled = builder.srcDisabled;
        srcTintDisabled = builder.srcTintDisabled;
    }

    public TUI getUiView() {
        return uiView;
    }

    public void bgSolidDefault(int bgSolidDefault) {
        this.bgSolidDefault = bgSolidDefault;
    }

    public void bgSolidPressed(int bgSolidPressed) {
        this.bgSolidPressed = bgSolidPressed;
    }

    public void bgSolidSelected(int bgSolidSelected) {
        this.bgSolidSelected = bgSolidSelected;
    }

    public void bgSolidFocused(int bgSolidFocused) {
        this.bgSolidFocused = bgSolidFocused;
    }

    public void bgSolidDisabled(int bgSolidDisabled) {
        this.bgSolidDisabled = bgSolidDisabled;
    }

    public void bgStrokeDefault(int bgStrokeDefault) {
        this.bgStrokeDefault = bgStrokeDefault;
    }

    public void bgStrokePressed(int bgStrokePressed) {
        this.bgStrokePressed = bgStrokePressed;
    }

    public void bgStrokeSelected(int bgStrokeSelected) {
        this.bgStrokeSelected = bgStrokeSelected;
    }

    public void bgStrokeFocused(int bgStrokeFocused) {
        this.bgStrokeFocused = bgStrokeFocused;
    }

    public void bgStrokeDisabled(int bgStrokeDisabled) {
        this.bgStrokeDisabled = bgStrokeDisabled;
    }

    public void bgStrokeColorDefault(int bgStrokeColorDefault) {
        this.bgStrokeColorDefault = bgStrokeColorDefault;
    }

    public void bgStrokeColorPressed(int bgStrokeColorPressed) {
        this.bgStrokeColorPressed = bgStrokeColorPressed;
    }

    public void bgStrokeColorSelected(int bgStrokeColorSelected) {
        this.bgStrokeColorSelected = bgStrokeColorSelected;
    }

    public void bgStrokeColorFocused(int bgStrokeColorFocused) {
        this.bgStrokeColorFocused = bgStrokeColorFocused;
    }

    public void bgStrokeColorDisabled(int bgStrokeColorDisabled) {
        this.bgStrokeColorDisabled = bgStrokeColorDisabled;
    }

    public void bgCorner(int bgCorner) {
        this.bgCorner = bgCorner;
    }

    public void bgCornerTopLeft(int bgCornerTopLeft) {
        this.bgCornerTopLeft = bgCornerTopLeft;
    }

    public void bgCornerTopRight(int bgCornerTopRight) {
        this.bgCornerTopRight = bgCornerTopRight;
    }

    public void bgCornerBottomRight(int bgCornerBottomRight) {
        this.bgCornerBottomRight = bgCornerBottomRight;
    }

    public void bgCornerBottomLeft(int bgCornerBottomLeft) {
        this.bgCornerBottomLeft = bgCornerBottomLeft;
    }

    public void bgRipplePressed(int bgRipplePressed) {
        this.bgRipplePressed = bgRipplePressed;
    }

    public void bgGradientStartColorDefault(int bgGradientStartColorDefault) {
        this.bgGradientStartColorDefault = bgGradientStartColorDefault;
    }

    public void bgGradientStartColorPressed(int bgGradientStartColorPressed) {
        this.bgGradientStartColorPressed = bgGradientStartColorPressed;
    }

    public void bgGradientStartColorSelected(int bgGradientStartColorSelected) {
        this.bgGradientStartColorSelected = bgGradientStartColorSelected;
    }

    public void bgGradientStartColorFocused(int bgGradientStartColorFocused) {
        this.bgGradientStartColorFocused = bgGradientStartColorFocused;
    }

    public void bgGradientStartColorDisabled(int bgGradientStartColorDisabled) {
        this.bgGradientStartColorDisabled = bgGradientStartColorDisabled;
    }

    public void bgGradientCenterColorDefault(int bgGradientCenterColorDefault) {
        this.bgGradientCenterColorDefault = bgGradientCenterColorDefault;
    }

    public void bgGradientCenterColorPressed(int bgGradientCenterColorPressed) {
        this.bgGradientCenterColorPressed = bgGradientCenterColorPressed;
    }

    public void bgGradientCenterColorSelected(int bgGradientCenterColorSelected) {
        this.bgGradientCenterColorSelected = bgGradientCenterColorSelected;
    }

    public void bgGradientCenterColorFocused(int bgGradientCenterColorFocused) {
        this.bgGradientCenterColorFocused = bgGradientCenterColorFocused;
    }

    public void bgGradientCenterColorDisabled(int bgGradientCenterColorDisabled) {
        this.bgGradientCenterColorDisabled = bgGradientCenterColorDisabled;
    }

    public void bgGradientEndColorDefault(int bgGradientEndColorDefault) {
        this.bgGradientEndColorDefault = bgGradientEndColorDefault;
    }

    public void bgGradientEndColorPressed(int bgGradientEndColorPressed) {
        this.bgGradientEndColorPressed = bgGradientEndColorPressed;
    }

    public void bgGradientEndColorSelected(int bgGradientEndColorSelected) {
        this.bgGradientEndColorSelected = bgGradientEndColorSelected;
    }

    public void bgGradientEndColorFocused(int bgGradientEndColorFocused) {
        this.bgGradientEndColorFocused = bgGradientEndColorFocused;
    }

    public void bgGradientEndColorDisabled(int bgGradientEndColorDisabled) {
        this.bgGradientEndColorDisabled = bgGradientEndColorDisabled;
    }

    public void bgGradientDirectionDefault(int bgGradientDirectionDefault) {
        this.bgGradientDirectionDefault = bgGradientDirectionDefault;
    }

    public void bgGradientDirectionPressed(int bgGradientDirectionPressed) {
        this.bgGradientDirectionPressed = bgGradientDirectionPressed;
    }

    public void bgGradientDirectionSelected(int bgGradientDirectionSelected) {
        this.bgGradientDirectionSelected = bgGradientDirectionSelected;
    }

    public void bgGradientDirectionFocused(int bgGradientDirectionFocused) {
        this.bgGradientDirectionFocused = bgGradientDirectionFocused;
    }

    public void bgGradientDirectionDisabled(int bgGradientDirectionDisabled) {
        this.bgGradientDirectionDisabled = bgGradientDirectionDisabled;
    }

    public void textColorDefault(int textColorDefault) {
        this.textColorDefault = textColorDefault;
    }

    public void textColorPressed(int textColorPressed) {
        this.textColorPressed = textColorPressed;
    }

    public void textColorSelected(int textColorSelected) {
        this.textColorSelected = textColorSelected;
    }

    public void textColorFocused(int textColorFocused) {
        this.textColorFocused = textColorFocused;
    }

    public void textColorDisabled(int textColorDisabled) {
        this.textColorDisabled = textColorDisabled;
    }

    public void drawableLeftWidth(int drawableLeftWidth) {
        this.drawableLeftWidth = drawableLeftWidth;
    }

    public void drawableLeftHeight(int drawableLeftHeight) {
        this.drawableLeftHeight = drawableLeftHeight;
    }

    public void drawableLeftAlign(int drawableLeftAlign) {
        this.drawableLeftAlign = drawableLeftAlign;
    }

    public void drawableLeftDefault(Drawable drawableLeftDefault) {
        this.drawableLeftDefault = drawableLeftDefault;
    }

    public void drawableLeftTintDefault(int drawableLeftTintDefault) {
        this.drawableLeftTintDefault = drawableLeftTintDefault;
    }

    public void drawableLeftPressed(Drawable drawableLeftPressed) {
        this.drawableLeftPressed = drawableLeftPressed;
    }

    public void drawableLeftTintPressed(int drawableLeftTintPressed) {
        this.drawableLeftTintPressed = drawableLeftTintPressed;
    }

    public void drawableLeftSelected(Drawable drawableLeftSelected) {
        this.drawableLeftSelected = drawableLeftSelected;
    }

    public void drawableLeftTintSelected(int drawableLeftTintSelected) {
        this.drawableLeftTintSelected = drawableLeftTintSelected;
    }

    public void drawableLeftFocused(Drawable drawableLeftFocused) {
        this.drawableLeftFocused = drawableLeftFocused;
    }

    public void drawableLeftTintFocused(int drawableLeftTintFocused) {
        this.drawableLeftTintFocused = drawableLeftTintFocused;
    }

    public void drawableLeftDisabled(Drawable drawableLeftDisabled) {
        this.drawableLeftDisabled = drawableLeftDisabled;
    }

    public void drawableLeftTintDisabled(int drawableLeftTintDisabled) {
        this.drawableLeftTintDisabled = drawableLeftTintDisabled;
    }

    public void drawableTopWidth(int drawableTopWidth) {
        this.drawableTopWidth = drawableTopWidth;
    }

    public void drawableTopHeight(int drawableTopHeight) {
        this.drawableTopHeight = drawableTopHeight;
    }

    public void drawableTopAlign(int drawableTopAlign) {
        this.drawableTopAlign = drawableTopAlign;
    }

    public void drawableTopDefault(Drawable drawableTopDefault) {
        this.drawableTopDefault = drawableTopDefault;
    }

    public void drawableTopTintDefault(int drawableTopTintDefault) {
        this.drawableTopTintDefault = drawableTopTintDefault;
    }

    public void drawableTopPressed(Drawable drawableTopPressed) {
        this.drawableTopPressed = drawableTopPressed;
    }

    public void drawableTopTintPressed(int drawableTopTintPressed) {
        this.drawableTopTintPressed = drawableTopTintPressed;
    }

    public void drawableTopSelected(Drawable drawableTopSelected) {
        this.drawableTopSelected = drawableTopSelected;
    }

    public void drawableTopTintSelected(int drawableTopTintSelected) {
        this.drawableTopTintSelected = drawableTopTintSelected;
    }

    public void drawableTopFocused(Drawable drawableTopFocused) {
        this.drawableTopFocused = drawableTopFocused;
    }

    public void drawableTopTintFocused(int drawableTopTintFocused) {
        this.drawableTopTintFocused = drawableTopTintFocused;
    }

    public void drawableTopDisabled(Drawable drawableTopDisabled) {
        this.drawableTopDisabled = drawableTopDisabled;
    }

    public void drawableTopTintDisabled(int drawableTopTintDisabled) {
        this.drawableTopTintDisabled = drawableTopTintDisabled;
    }

    public void drawableRightWidth(int drawableRightWidth) {
        this.drawableRightWidth = drawableRightWidth;
    }

    public void drawableRightHeight(int drawableRightHeight) {
        this.drawableRightHeight = drawableRightHeight;
    }

    public void drawableRightAlign(int drawableRightAlign) {
        this.drawableRightAlign = drawableRightAlign;
    }

    public void drawableRightDefault(Drawable drawableRightDefault) {
        this.drawableRightDefault = drawableRightDefault;
    }

    public void drawableRightTintDefault(int drawableRightTintDefault) {
        this.drawableRightTintDefault = drawableRightTintDefault;
    }

    public void drawableRightPressed(Drawable drawableRightPressed) {
        this.drawableRightPressed = drawableRightPressed;
    }

    public void drawableRightTintPressed(int drawableRightTintPressed) {
        this.drawableRightTintPressed = drawableRightTintPressed;
    }

    public void drawableRightSelected(Drawable drawableRightSelected) {
        this.drawableRightSelected = drawableRightSelected;
    }

    public void drawableRightTintSelected(int drawableRightTintSelected) {
        this.drawableRightTintSelected = drawableRightTintSelected;
    }

    public void drawableRightFocused(Drawable drawableRightFocused) {
        this.drawableRightFocused = drawableRightFocused;
    }

    public void drawableRightTintFocused(int drawableRightTintFocused) {
        this.drawableRightTintFocused = drawableRightTintFocused;
    }

    public void drawableRightDisabled(Drawable drawableRightDisabled) {
        this.drawableRightDisabled = drawableRightDisabled;
    }

    public void drawableRightTintDisabled(int drawableRightTintDisabled) {
        this.drawableRightTintDisabled = drawableRightTintDisabled;
    }

    public void drawableBottomWidth(int drawableBottomWidth) {
        this.drawableBottomWidth = drawableBottomWidth;
    }

    public void drawableBottomHeight(int drawableBottomHeight) {
        this.drawableBottomHeight = drawableBottomHeight;
    }

    public void drawableBottomAlign(int drawableBottomAlign) {
        this.drawableBottomAlign = drawableBottomAlign;
    }

    public void drawableBottomDefault(Drawable drawableBottomDefault) {
        this.drawableBottomDefault = drawableBottomDefault;
    }

    public void drawableBottomTintDefault(int drawableBottomTintDefault) {
        this.drawableBottomTintDefault = drawableBottomTintDefault;
    }

    public void drawableBottomPressed(Drawable drawableBottomPressed) {
        this.drawableBottomPressed = drawableBottomPressed;
    }

    public void drawableBottomTintPressed(int drawableBottomTintPressed) {
        this.drawableBottomTintPressed = drawableBottomTintPressed;
    }

    public void drawableBottomSelected(Drawable drawableBottomSelected) {
        this.drawableBottomSelected = drawableBottomSelected;
    }

    public void drawableBottomTintSelected(int drawableBottomTintSelected) {
        this.drawableBottomTintSelected = drawableBottomTintSelected;
    }

    public void drawableBottomFocused(Drawable drawableBottomFocused) {
        this.drawableBottomFocused = drawableBottomFocused;
    }

    public void drawableBottomTintFocused(int drawableBottomTintFocused) {
        this.drawableBottomTintFocused = drawableBottomTintFocused;
    }

    public void drawableBottomDisabled(Drawable drawableBottomDisabled) {
        this.drawableBottomDisabled = drawableBottomDisabled;
    }

    public void drawableBottomTintDisabled(int drawableBottomTintDisabled) {
        this.drawableBottomTintDisabled = drawableBottomTintDisabled;
    }

    public void srcDefault(Drawable srcDefault) {
        this.srcDefault = srcDefault;
    }

    public void srcTintDefault(int srcTintDefault) {
        this.srcTintDefault = srcTintDefault;
    }

    public void srcPressed(Drawable srcPressed) {
        this.srcPressed = srcPressed;
    }

    public void srcTintPressed(int srcTintPressed) {
        this.srcTintPressed = srcTintPressed;
    }

    public void srcSelected(Drawable srcSelected) {
        this.srcSelected = srcSelected;
    }

    public void srcTintSelected(int srcTintSelected) {
        this.srcTintSelected = srcTintSelected;
    }

    public void srcFocused(Drawable srcFocused) {
        this.srcFocused = srcFocused;
    }

    public void srcTintFocused(int srcTintFocused) {
        this.srcTintFocused = srcTintFocused;
    }

    public void srcDisabled(Drawable srcDisabled) {
        this.srcDisabled = srcDisabled;
    }

    public void srcTintDisabled(int srcTintDisabled) {
        this.srcTintDisabled = srcTintDisabled;
    }

    /**
     * 更新背景
     */
    public void updateBackground() {
        SelectorFactory factory = SelectorFactory.create();
        factory.defaultSolid(bgSolidDefault)
                .pressedSolid(bgSolidPressed)
                .selectedSolid(bgSolidSelected)
                .focusedSolid(bgSolidFocused)
                .disabledSolid(bgSolidDisabled);

        factory.defaultStroke(bgStrokeDefault, bgStrokeColorDefault)
                .pressedStroke(bgStrokePressed, bgStrokeColorPressed)
                .selectedStroke(bgStrokeSelected, bgStrokeColorSelected)
                .focusedStroke(bgStrokeFocused, bgStrokeColorFocused)
                .disabledStroke(bgStrokeDisabled, bgStrokeColorDisabled);

        if (bgCorner > 0) {
            factory.corner(bgCorner);
        } else {
            factory.corner(new float[]{bgCornerTopLeft, bgCornerTopLeft,
                    bgCornerTopRight, bgCornerTopRight,
                    bgCornerBottomRight, bgCornerBottomRight,
                    bgCornerBottomLeft, bgCornerBottomLeft});
        }

        factory.pressedRipple(bgRipplePressed);

        factory.defaultGradient(generateOrientation(bgGradientDirectionDefault), generateGradientColors(bgGradientStartColorDefault, bgGradientCenterColorDefault, bgGradientEndColorDefault))
                .pressedGradient(generateOrientation(bgGradientDirectionPressed), generateGradientColors(bgGradientStartColorPressed, bgGradientCenterColorPressed, bgGradientEndColorPressed))
                .selectedGradient(generateOrientation(bgGradientDirectionSelected), generateGradientColors(bgGradientStartColorSelected, bgGradientCenterColorSelected, bgGradientEndColorSelected))
                .focusedGradient(generateOrientation(bgGradientDirectionFocused), generateGradientColors(bgGradientStartColorFocused, bgGradientCenterColorFocused, bgGradientEndColorFocused))
                .disabledGradient(generateOrientation(bgGradientDirectionDisabled), generateGradientColors(bgGradientStartColorDisabled, bgGradientCenterColorDisabled, bgGradientEndColorDisabled));
        StateListDrawable stateListDrawable = factory.build();
        if (!DrawableUtils.isStateListDrawableEmpty(stateListDrawable)) {
            uiView.updateBackground(stateListDrawable);
        }
    }

    /**
     * 更新文字
     */
    public void updateText() {
        if (textColorDefault == SelectorFactory.INVALID) {
            return;
        }
        if (uiView instanceof ITextView) {
            ColorStateList colorStateList = new ColorStateList(SelectorFactory.STATES,
                    new int[]{textColorSelected == SelectorFactory.INVALID ? textColorDefault : textColorSelected,
                            textColorPressed == SelectorFactory.INVALID ? textColorDefault : textColorPressed,
                            textColorFocused == SelectorFactory.INVALID ? textColorDefault : textColorFocused,
                            textColorDisabled == SelectorFactory.INVALID ? textColorDefault : textColorDisabled,
                            textColorDefault});
            ((ITextView) uiView).updateText(colorStateList);
        }
    }


    /**
     * 更新Drawable
     */
    public void updateDrawable() {
        if (uiView instanceof ITextView) {
            Drawable[] drawables = new Drawable[4];

            drawables[0] = generateDrawable(tintWrapDrawable(drawableLeftDefault, drawableLeftTintDefault),
                    tintWrapDrawable(drawableLeftPressed, drawableLeftTintPressed),
                    tintWrapDrawable(drawableLeftSelected, drawableLeftTintSelected),
                    tintWrapDrawable(drawableLeftFocused, drawableLeftTintFocused),
                    tintWrapDrawable(drawableLeftDisabled, drawableLeftTintDisabled),
                    drawableLeftWidth, drawableLeftHeight);
            drawables[1] = generateDrawable(tintWrapDrawable(drawableTopDefault, drawableTopTintDefault),
                    tintWrapDrawable(drawableTopPressed, drawableTopTintPressed),
                    tintWrapDrawable(drawableTopSelected, drawableTopTintSelected),
                    tintWrapDrawable(drawableTopFocused, drawableTopTintFocused),
                    tintWrapDrawable(drawableTopDisabled, drawableTopTintDisabled),
                    drawableTopWidth, drawableTopHeight);
            drawables[2] = generateDrawable(tintWrapDrawable(drawableRightDefault, drawableRightTintDefault),
                    tintWrapDrawable(drawableRightPressed, drawableRightTintPressed),
                    tintWrapDrawable(drawableRightSelected, drawableRightTintSelected),
                    tintWrapDrawable(drawableRightFocused, drawableRightTintFocused),
                    tintWrapDrawable(drawableRightDisabled, drawableRightTintDisabled),
                    drawableRightWidth, drawableRightHeight);
            drawables[3] = generateDrawable(tintWrapDrawable(drawableBottomDefault, drawableBottomTintDefault),
                    tintWrapDrawable(drawableBottomPressed, drawableBottomTintPressed),
                    tintWrapDrawable(drawableBottomSelected, drawableBottomTintSelected),
                    tintWrapDrawable(drawableBottomFocused, drawableBottomTintFocused),
                    tintWrapDrawable(drawableBottomDisabled, drawableBottomTintDisabled),
                    drawableBottomWidth, drawableBottomHeight);

            ((ITextView) uiView).updateDrawable(drawables);
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
            switch (drawableLeftAlign) {
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
            switch (drawableTopAlign) {
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
            switch (drawableRightAlign) {
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
            switch (drawableBottomAlign) {
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
        Drawable src = generateDrawable(tintWrapDrawable(srcDefault, srcTintDefault),
                tintWrapDrawable(srcPressed, srcTintPressed),
                tintWrapDrawable(srcSelected, srcTintSelected),
                tintWrapDrawable(srcFocused, srcTintFocused),
                tintWrapDrawable(srcDisabled, srcTintDisabled),
                -1, -1);
        if (uiView instanceof TUIImageView && src != null) {
            ((TUIImageView) uiView).updateSrc(src);
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

        private int bgGradientDirectionDefault = 1;
        private int bgGradientDirectionPressed = 1;
        private int bgGradientDirectionSelected = 1;
        private int bgGradientDirectionFocused = 1;
        private int bgGradientDirectionDisabled = 1;

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
            this.bgGradientDirectionDefault = typedArray.getInt(bgGradientDirectionDefault, 1);
            return this;
        }

        public Builder<T> bgGradientDirectionPressed(@StyleableRes int bgGradientDirectionPressed) {
            this.bgGradientDirectionPressed = typedArray.getInt(bgGradientDirectionPressed, 1);
            return this;
        }

        public Builder<T> bgGradientDirectionSelected(@StyleableRes int bgGradientDirectionSelected) {
            this.bgGradientDirectionSelected = typedArray.getInt(bgGradientDirectionSelected, 1);
            return this;
        }

        public Builder<T> bgGradientDirectionFocused(@StyleableRes int bgGradientDirectionFocused) {
            this.bgGradientDirectionFocused = typedArray.getInt(bgGradientDirectionFocused, 1);
            return this;
        }

        public Builder<T> bgGradientDirectionDisabled(@StyleableRes int bgGradientDirectionDisabled) {
            this.bgGradientDirectionDisabled = typedArray.getInt(bgGradientDirectionDisabled, 1);
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
            this.drawableLeftDefault = typedArray.getDrawable(drawableLeftDefault);
            return this;
        }

        public Builder<T> drawableLeftTintDefault(@StyleableRes int drawableLeftTintDefault) {
            this.drawableLeftTintDefault = typedArray.getColor(drawableLeftTintDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableLeftPressed(@StyleableRes int drawableLeftPressed) {
            this.drawableLeftPressed = typedArray.getDrawable(drawableLeftPressed);
            return this;
        }

        public Builder<T> drawableLeftTintPressed(@StyleableRes int drawableLeftTintPressed) {
            this.drawableLeftTintPressed = typedArray.getColor(drawableLeftTintPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableLeftSelected(@StyleableRes int drawableLeftSelected) {
            this.drawableLeftSelected = typedArray.getDrawable(drawableLeftSelected);
            return this;
        }

        public Builder<T> drawableLeftTintSelected(@StyleableRes int drawableLeftTintSelected) {
            this.drawableLeftTintSelected = typedArray.getColor(drawableLeftTintSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableLeftFocused(@StyleableRes int drawableLeftFocused) {
            this.drawableLeftFocused = typedArray.getDrawable(drawableLeftFocused);
            return this;
        }

        public Builder<T> drawableLeftTintFocused(@StyleableRes int drawableLeftTintFocused) {
            this.drawableLeftTintFocused = typedArray.getColor(drawableLeftTintFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableLeftDisabled(@StyleableRes int drawableLeftDisabled) {
            this.drawableLeftDisabled = typedArray.getDrawable(drawableLeftDisabled);
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
            this.drawableTopDefault = typedArray.getDrawable(drawableTopDefault);
            return this;
        }

        public Builder<T> drawableTopTintDefault(@StyleableRes int drawableTopTintDefault) {
            this.drawableTopTintDefault = typedArray.getColor(drawableTopTintDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableTopPressed(@StyleableRes int drawableTopPressed) {
            this.drawableTopPressed = typedArray.getDrawable(drawableTopPressed);
            return this;
        }

        public Builder<T> drawableTopTintPressed(@StyleableRes int drawableTopTintPressed) {
            this.drawableTopTintPressed = typedArray.getColor(drawableTopTintPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableTopSelected(@StyleableRes int drawableTopSelected) {
            this.drawableTopSelected = typedArray.getDrawable(drawableTopSelected);
            return this;
        }

        public Builder<T> drawableTopTintSelected(@StyleableRes int drawableTopTintSelected) {
            this.drawableTopTintSelected = typedArray.getColor(drawableTopTintSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableTopFocused(@StyleableRes int drawableTopFocused) {
            this.drawableTopFocused = typedArray.getDrawable(drawableTopFocused);
            return this;
        }

        public Builder<T> drawableTopTintFocused(@StyleableRes int drawableTopTintFocused) {
            this.drawableTopTintFocused = typedArray.getColor(drawableTopTintFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableTopDisabled(@StyleableRes int drawableTopDisabled) {
            this.drawableTopDisabled = typedArray.getDrawable(drawableTopDisabled);
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
            this.drawableRightDefault = typedArray.getDrawable(drawableRightDefault);
            return this;
        }

        public Builder<T> drawableRightTintDefault(@StyleableRes int drawableRightTintDefault) {
            this.drawableRightTintDefault = typedArray.getColor(drawableRightTintDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableRightPressed(@StyleableRes int drawableRightPressed) {
            this.drawableRightPressed = typedArray.getDrawable(drawableRightPressed);
            return this;
        }

        public Builder<T> drawableRightTintPressed(@StyleableRes int drawableRightTintPressed) {
            this.drawableRightTintPressed = typedArray.getColor(drawableRightTintPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableRightSelected(@StyleableRes int drawableRightSelected) {
            this.drawableRightSelected = typedArray.getDrawable(drawableRightSelected);
            return this;
        }

        public Builder<T> drawableRightTintSelected(@StyleableRes int drawableRightTintSelected) {
            this.drawableRightTintSelected = typedArray.getColor(drawableRightTintSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableRightFocused(@StyleableRes int drawableRightFocused) {
            this.drawableRightFocused = typedArray.getDrawable(drawableRightFocused);
            return this;
        }

        public Builder<T> drawableRightTintFocused(@StyleableRes int drawableRightTintFocused) {
            this.drawableRightTintFocused = typedArray.getColor(drawableRightTintFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableRightDisabled(@StyleableRes int drawableRightDisabled) {
            this.drawableRightDisabled = typedArray.getDrawable(drawableRightDisabled);
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
            this.drawableBottomDefault = typedArray.getDrawable(drawableBottomDefault);
            return this;
        }

        public Builder<T> drawableBottomTintDefault(@StyleableRes int drawableBottomTintDefault) {
            this.drawableBottomTintDefault = typedArray.getColor(drawableBottomTintDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableBottomPressed(@StyleableRes int drawableBottomPressed) {
            this.drawableBottomPressed = typedArray.getDrawable(drawableBottomPressed);
            return this;
        }

        public Builder<T> drawableBottomTintPressed(@StyleableRes int drawableBottomTintPressed) {
            this.drawableBottomTintPressed = typedArray.getColor(drawableBottomTintPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableBottomSelected(@StyleableRes int drawableBottomSelected) {
            this.drawableBottomSelected = typedArray.getDrawable(drawableBottomSelected);
            return this;
        }

        public Builder<T> drawableBottomTintSelected(@StyleableRes int drawableBottomTintSelected) {
            this.drawableBottomTintSelected = typedArray.getColor(drawableBottomTintSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableBottomFocused(@StyleableRes int drawableBottomFocused) {
            this.drawableBottomFocused = typedArray.getDrawable(drawableBottomFocused);
            return this;
        }

        public Builder<T> drawableBottomTintFocused(@StyleableRes int drawableBottomTintFocused) {
            this.drawableBottomTintFocused = typedArray.getColor(drawableBottomTintFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> drawableBottomDisabled(@StyleableRes int drawableBottomDisabled) {
            this.drawableBottomDisabled = typedArray.getDrawable(drawableBottomDisabled);
            return this;
        }

        public Builder<T> drawableBottomTintDisabled(@StyleableRes int drawableBottomTintDisabled) {
            this.drawableBottomTintDisabled = typedArray.getColor(drawableBottomTintDisabled, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> srcDefault(@StyleableRes int srcDefault) {
            this.srcDefault = typedArray.getDrawable(srcDefault);
            return this;
        }

        public Builder<T> srcTintDefault(@StyleableRes int srcTintDefault) {
            this.srcTintDefault = typedArray.getColor(srcTintDefault, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> srcPressed(@StyleableRes int srcPressed) {
            this.srcPressed = typedArray.getDrawable(srcPressed);
            return this;
        }

        public Builder<T> srcTintPressed(@StyleableRes int srcTintPressed) {
            this.srcTintPressed = typedArray.getColor(srcTintPressed, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> srcSelected(@StyleableRes int srcSelected) {
            this.srcSelected = typedArray.getDrawable(srcSelected);
            return this;
        }

        public Builder<T> srcTintSelected(@StyleableRes int srcTintSelected) {
            this.srcTintSelected = typedArray.getColor(srcTintSelected, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> srcFocused(@StyleableRes int srcFocused) {
            this.srcFocused = typedArray.getDrawable(srcFocused);
            return this;
        }

        public Builder<T> srcTintFocused(@StyleableRes int srcTintFocused) {
            this.srcTintFocused = typedArray.getColor(srcTintFocused, SelectorFactory.INVALID);
            return this;
        }

        public Builder<T> srcDisabled(@StyleableRes int srcDisabled) {
            this.srcDisabled = typedArray.getDrawable(srcDisabled);
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
