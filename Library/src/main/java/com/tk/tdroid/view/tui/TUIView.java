package com.tk.tdroid.view.tui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.tk.tdroid.R;


/**
 * <pre>
 *      author : TK
 *      time : 2017/12/26
 *      desc :
 * </pre>
 */

public class TUIView extends View implements IView {
    private TUIHelper<IView> uiHelper;

    public TUIView(Context context) {
        super(context);
        initAttr(null);
    }

    public TUIView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public TUIView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TUIView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(attrs);
    }

    private void initAttr(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            uiHelper = new TUIHelper.Builder<IView>(this, null).build();
        } else {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TUIView);
            uiHelper = new TUIHelper.Builder<IView>(this, typedArray)
                    .bgSolidDefault(R.styleable.TUIView_tui_bg_solid_default)
                    .bgSolidPressed(R.styleable.TUIView_tui_bg_solid_pressed)
                    .bgSolidSelected(R.styleable.TUIView_tui_bg_solid_selected)
                    .bgSolidFocused(R.styleable.TUIView_tui_bg_solid_focused)
                    .bgSolidDisabled(R.styleable.TUIView_tui_bg_solid_disabled)

                    .bgStrokeDefault(R.styleable.TUIView_tui_bg_stroke_default)
                    .bgStrokePressed(R.styleable.TUIView_tui_bg_stroke_pressed)
                    .bgStrokeSelected(R.styleable.TUIView_tui_bg_stroke_selected)
                    .bgStrokeFocused(R.styleable.TUIView_tui_bg_stroke_focused)
                    .bgStrokeDisabled(R.styleable.TUIView_tui_bg_stroke_disabled)

                    .bgStrokeColorDefault(R.styleable.TUIView_tui_bg_strokeColor_default)
                    .bgStrokeColorPressed(R.styleable.TUIView_tui_bg_strokeColor_pressed)
                    .bgStrokeColorSelected(R.styleable.TUIView_tui_bg_strokeColor_selected)
                    .bgStrokeColorFocused(R.styleable.TUIView_tui_bg_strokeColor_focused)
                    .bgStrokeColorDisabled(R.styleable.TUIView_tui_bg_strokeColor_disabled)

                    .bgCorner(R.styleable.TUIView_tui_bg_corner)
                    .bgCornerTopLeft(R.styleable.TUIView_tui_bg_corner_topLeft)
                    .bgCornerTopRight(R.styleable.TUIView_tui_bg_corner_topRight)
                    .bgCornerBottomRight(R.styleable.TUIView_tui_bg_corner_bottomRight)
                    .bgCornerBottomLeft(R.styleable.TUIView_tui_bg_corner_bottomLeft)

                    .bgRipplePressed(R.styleable.TUIView_tui_bg_ripplePressed)

                    .bgGradientStartColorDefault(R.styleable.TUIView_tui_bg_gradientStartColor_default)
                    .bgGradientStartColorPressed(R.styleable.TUIView_tui_bg_gradientStartColor_pressed)
                    .bgGradientStartColorSelected(R.styleable.TUIView_tui_bg_gradientStartColor_selected)
                    .bgGradientStartColorFocused(R.styleable.TUIView_tui_bg_gradientStartColor_focused)
                    .bgGradientStartColorDisabled(R.styleable.TUIView_tui_bg_gradientStartColor_disabled)

                    .bgGradientCenterColorDefault(R.styleable.TUIView_tui_bg_gradientCenterColor_default)
                    .bgGradientCenterColorPressed(R.styleable.TUIView_tui_bg_gradientCenterColor_pressed)
                    .bgGradientCenterColorSelected(R.styleable.TUIView_tui_bg_gradientCenterColor_selected)
                    .bgGradientCenterColorFocused(R.styleable.TUIView_tui_bg_gradientCenterColor_focused)
                    .bgGradientCenterColorDisabled(R.styleable.TUIView_tui_bg_gradientCenterColor_disabled)

                    .bgGradientEndColorDefault(R.styleable.TUIView_tui_bg_gradientEndColor_default)
                    .bgGradientEndColorPressed(R.styleable.TUIView_tui_bg_gradientEndColor_pressed)
                    .bgGradientEndColorSelected(R.styleable.TUIView_tui_bg_gradientEndColor_selected)
                    .bgGradientEndColorFocused(R.styleable.TUIView_tui_bg_gradientEndColor_focused)
                    .bgGradientEndColorDisabled(R.styleable.TUIView_tui_bg_gradientEndColor_disabled)

                    .bgGradientDirectionDefault(R.styleable.TUIView_tui_bg_gradientDirection_default)
                    .bgGradientDirectionPressed(R.styleable.TUIView_tui_bg_gradientDirection_pressed)
                    .bgGradientDirectionSelected(R.styleable.TUIView_tui_bg_gradientDirection_selected)
                    .bgGradientDirectionFocused(R.styleable.TUIView_tui_bg_gradientDirection_focused)
                    .bgGradientDirectionDisabled(R.styleable.TUIView_tui_bg_gradientDirection_disabled)
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