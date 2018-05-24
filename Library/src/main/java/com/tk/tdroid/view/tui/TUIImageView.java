package com.tk.tdroid.view.tui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tk.tdroid.R;


/**
 * <pre>
 *      author : TK
 *      time : 2017/12/26
 *      desc :
 * </pre>
 */

@SuppressLint("AppCompatCustomView")
public class TUIImageView extends ImageView implements IImageView {
    private TUIHelper<IView> uiHelper;

    public TUIImageView(Context context) {
        super(context);
        initAttr(null);
    }

    public TUIImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public TUIImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TUIImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(attrs);
    }

    private void initAttr(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            uiHelper = new TUIHelper.Builder<IView>(this, null).build();
        } else {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TUIImageView);
            uiHelper = new TUIHelper.Builder<IView>(this, typedArray)
                    .bgSolidDefault(R.styleable.TUIImageView_tui_bg_solid_default)
                    .bgSolidPressed(R.styleable.TUIImageView_tui_bg_solid_pressed)
                    .bgSolidSelected(R.styleable.TUIImageView_tui_bg_solid_selected)
                    .bgSolidFocused(R.styleable.TUIImageView_tui_bg_solid_focused)
                    .bgSolidDisabled(R.styleable.TUIImageView_tui_bg_solid_disabled)

                    .bgStrokeDefault(R.styleable.TUIImageView_tui_bg_stroke_default)
                    .bgStrokePressed(R.styleable.TUIImageView_tui_bg_stroke_pressed)
                    .bgStrokeSelected(R.styleable.TUIImageView_tui_bg_stroke_selected)
                    .bgStrokeFocused(R.styleable.TUIImageView_tui_bg_stroke_focused)
                    .bgStrokeDisabled(R.styleable.TUIImageView_tui_bg_stroke_disabled)

                    .bgStrokeColorDefault(R.styleable.TUIImageView_tui_bg_strokeColor_default)
                    .bgStrokeColorPressed(R.styleable.TUIImageView_tui_bg_strokeColor_pressed)
                    .bgStrokeColorSelected(R.styleable.TUIImageView_tui_bg_strokeColor_selected)
                    .bgStrokeColorFocused(R.styleable.TUIImageView_tui_bg_strokeColor_focused)
                    .bgStrokeColorDisabled(R.styleable.TUIImageView_tui_bg_strokeColor_disabled)

                    .bgCorner(R.styleable.TUIImageView_tui_bg_corner)
                    .bgCornerTopLeft(R.styleable.TUIImageView_tui_bg_corner_topLeft)
                    .bgCornerTopRight(R.styleable.TUIImageView_tui_bg_corner_topRight)
                    .bgCornerBottomRight(R.styleable.TUIImageView_tui_bg_corner_bottomRight)
                    .bgCornerBottomLeft(R.styleable.TUIImageView_tui_bg_corner_bottomLeft)

                    .bgRipplePressed(R.styleable.TUIImageView_tui_bg_ripplePressed)

                    .bgGradientStartColorDefault(R.styleable.TUIImageView_tui_bg_gradientStartColor_default)
                    .bgGradientStartColorPressed(R.styleable.TUIImageView_tui_bg_gradientStartColor_pressed)
                    .bgGradientStartColorSelected(R.styleable.TUIImageView_tui_bg_gradientStartColor_selected)
                    .bgGradientStartColorFocused(R.styleable.TUIImageView_tui_bg_gradientStartColor_focused)
                    .bgGradientStartColorDisabled(R.styleable.TUIImageView_tui_bg_gradientStartColor_disabled)

                    .bgGradientCenterColorDefault(R.styleable.TUIImageView_tui_bg_gradientCenterColor_default)
                    .bgGradientCenterColorPressed(R.styleable.TUIImageView_tui_bg_gradientCenterColor_pressed)
                    .bgGradientCenterColorSelected(R.styleable.TUIImageView_tui_bg_gradientCenterColor_selected)
                    .bgGradientCenterColorFocused(R.styleable.TUIImageView_tui_bg_gradientCenterColor_focused)
                    .bgGradientCenterColorDisabled(R.styleable.TUIImageView_tui_bg_gradientCenterColor_disabled)

                    .bgGradientEndColorDefault(R.styleable.TUIImageView_tui_bg_gradientEndColor_default)
                    .bgGradientEndColorPressed(R.styleable.TUIImageView_tui_bg_gradientEndColor_pressed)
                    .bgGradientEndColorSelected(R.styleable.TUIImageView_tui_bg_gradientEndColor_selected)
                    .bgGradientEndColorFocused(R.styleable.TUIImageView_tui_bg_gradientEndColor_focused)
                    .bgGradientEndColorDisabled(R.styleable.TUIImageView_tui_bg_gradientEndColor_disabled)

                    .bgGradientDirectionDefault(R.styleable.TUIImageView_tui_bg_gradientDirection_default)
                    .bgGradientDirectionPressed(R.styleable.TUIImageView_tui_bg_gradientDirection_pressed)
                    .bgGradientDirectionSelected(R.styleable.TUIImageView_tui_bg_gradientDirection_selected)
                    .bgGradientDirectionFocused(R.styleable.TUIImageView_tui_bg_gradientDirection_focused)
                    .bgGradientDirectionDisabled(R.styleable.TUIImageView_tui_bg_gradientDirection_disabled)

                    .srcDefault(R.styleable.TUIImageView_tui_src_default)
                    .srcTintDefault(R.styleable.TUIImageView_tui_srcTint_default)
                    .srcPressed(R.styleable.TUIImageView_tui_src_pressed)
                    .srcTintPressed(R.styleable.TUIImageView_tui_srcTint_pressed)
                    .srcSelected(R.styleable.TUIImageView_tui_src_selected)
                    .srcTintSelected(R.styleable.TUIImageView_tui_srcTint_selected)
                    .srcFocused(R.styleable.TUIImageView_tui_src_focused)
                    .srcTintFocused(R.styleable.TUIImageView_tui_srcTint_focused)
                    .srcDisabled(R.styleable.TUIImageView_tui_src_disabled)
                    .srcTintDisabled(R.styleable.TUIImageView_tui_srcTint_disabled)
                    .build();

            uiHelper.updateBackground();
            uiHelper.updateSrc();

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

    @Override
    public void updateSrc(@NonNull Drawable src) {
        setImageDrawable(src);
    }
}