package com.tk.tdroid.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.util.Pair;

import java.util.Arrays;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/26
 *      desc : Selector 对应的{@link StateListDrawable}代码生成工厂
 * </pre>
 */

public final class SelectorFactory {
    public static final int[][] STATES = new int[][]{{android.R.attr.state_selected},
            {android.R.attr.state_pressed},
            {android.R.attr.state_focused},
            {-android.R.attr.state_enabled},
            {}};
    public static final int INVALID = -2;

    private float[][] radius = new float[STATES.length][8];
    private int[] solids = new int[]{
            Color.TRANSPARENT,
            Color.TRANSPARENT,
            Color.TRANSPARENT,
            Color.TRANSPARENT,
            Color.TRANSPARENT};
    private int[][] strokes = new int[STATES.length][2];
    private Pair<GradientDrawable.Orientation, int[]>[] gradients = new Pair[STATES.length];
    private int pressedRipple = INVALID;


    private SelectorFactory() {
    }

    /**
     * 创建一个实例
     *
     * @return
     */
    public static SelectorFactory create() {
        return new SelectorFactory();
    }

    /**
     * 设置全局圆角
     *
     * @param radius
     * @return
     */
    public SelectorFactory corner(float radius) {
        float[] r = new float[8];
        Arrays.fill(r, radius);
        Arrays.fill(this.radius, r);
        return this;
    }

    /**
     * 设置全局圆角
     *
     * @param radius 长度==8 依次是leftTop,leftTop,rightTop,rightTop,rightBottom,rightBottom,leftBottom,leftBottom
     * @return
     */
    public SelectorFactory corner(float[] radius) {
        if (radius != null && radius.length == 8) {
            Arrays.fill(this.radius, radius);
        }
        return this;
    }

    /**
     * 设置描边
     *
     * @param stroke
     * @param strokeColor
     * @return
     */
    public SelectorFactory defaultStroke(int stroke, @ColorInt int strokeColor) {
        strokes[STATES.length - 1] = new int[]{stroke, strokeColor};
        return this;
    }

    /**
     * 设置填充
     *
     * @param color
     * @return
     */
    public SelectorFactory defaultSolid(@ColorInt int color) {
        solids[STATES.length - 1] = color;
        return this;
    }

    /**
     * 设置线性渐变
     *
     * @param orientation
     * @param colors
     * @return
     */
    public SelectorFactory defaultGradient(GradientDrawable.Orientation orientation, @ColorInt int[] colors) {
        gradients[STATES.length - 1] = new Pair<>(orientation, colors);
        return this;
    }

    /**
     * 设置pressed按下涟漪效果 5.0+生效
     *
     * @param pressedRipple
     * @return
     */
    public SelectorFactory pressedRipple(@ColorInt int pressedRipple) {
        this.pressedRipple = pressedRipple;
        return this;
    }

    /**
     * 设置描边
     *
     * @param stroke
     * @param strokeColor
     * @return
     */
    public SelectorFactory pressedStroke(int stroke, @ColorInt int strokeColor) {
        strokes[1] = new int[]{stroke, strokeColor};
        return this;
    }

    /**
     * 设置填充
     *
     * @param color
     * @return
     */
    public SelectorFactory pressedSolid(@ColorInt int color) {
        solids[1] = color;
        return this;
    }

    /**
     * 设置线性渐变
     *
     * @param orientation
     * @param colors
     * @return
     */
    public SelectorFactory pressedGradient(GradientDrawable.Orientation orientation, @ColorInt int[] colors) {
        gradients[1] = new Pair<>(orientation, colors);
        return this;
    }

    /**
     * 设置描边
     *
     * @param stroke
     * @param strokeColor
     * @return
     */
    public SelectorFactory selectedStroke(int stroke, @ColorInt int strokeColor) {
        strokes[0] = new int[]{stroke, strokeColor};
        return this;
    }

    /**
     * 设置填充
     *
     * @param color
     * @return
     */
    public SelectorFactory selectedSolid(@ColorInt int color) {
        solids[0] = color;
        return this;
    }

    /**
     * 设置线性渐变
     *
     * @param orientation
     * @param colors
     * @return
     */
    public SelectorFactory selectedGradient(GradientDrawable.Orientation orientation, @ColorInt int[] colors) {
        gradients[0] = new Pair<>(orientation, colors);
        return this;
    }

    /**
     * 设置描边
     *
     * @param stroke
     * @param strokeColor
     * @return
     */
    public SelectorFactory focusedStroke(int stroke, @ColorInt int strokeColor) {
        strokes[2] = new int[]{stroke, strokeColor};
        return this;
    }

    /**
     * 设置填充
     *
     * @param color
     * @return
     */
    public SelectorFactory focusedSolid(@ColorInt int color) {
        solids[2] = color;
        return this;
    }

    /**
     * 设置线性渐变
     *
     * @param orientation
     * @param colors
     * @return
     */
    public SelectorFactory focusedGradient(GradientDrawable.Orientation orientation, @ColorInt int[] colors) {
        gradients[2] = new Pair<>(orientation, colors);
        return this;
    }


    /**
     * 设置描边
     *
     * @param stroke
     * @param strokeColor
     * @return
     */
    public SelectorFactory disabledStroke(int stroke, @ColorInt int strokeColor) {
        strokes[3] = new int[]{stroke, strokeColor};
        return this;
    }

    /**
     * 设置填充
     *
     * @param color
     * @return
     */
    public SelectorFactory disabledSolid(@ColorInt int color) {
        solids[3] = color;
        return this;
    }

    /**
     * 设置线性渐变
     *
     * @param orientation
     * @param colors
     * @return
     */
    public SelectorFactory disabledGradient(GradientDrawable.Orientation orientation, @ColorInt int[] colors) {
        gradients[3] = new Pair<>(orientation, colors);
        return this;
    }

    public StateListDrawable build() {
        StateListDrawable drawable = new StateListDrawable();

        GradientDrawable defaultD = generateShape(STATES.length - 1);
        GradientDrawable selectedD = generateShape(0);
        GradientDrawable pressedD = generateShape(1);
        GradientDrawable focusedD = generateShape(2);
        GradientDrawable disabledD = generateShape(3);

        if (pressedRipple != INVALID && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (selectedD != null) {
                drawable.addState(STATES[0], selectedD);
            }
            if (focusedD != null) {
                drawable.addState(STATES[2], focusedD);
            }
            if (disabledD != null) {
                drawable.addState(STATES[3], disabledD);
            }
            drawable.addState(STATES[STATES.length - 1],
                    new RippleDrawable(ColorStateList.valueOf(pressedRipple), defaultD, null));
        } else {
            if (selectedD != null) {
                drawable.addState(STATES[0], selectedD);
            }
            if (pressedD != null) {
                drawable.addState(STATES[1], pressedD);
            }
            if (focusedD != null) {
                drawable.addState(STATES[2], focusedD);
            }
            if (disabledD != null) {
                drawable.addState(STATES[3], disabledD);
            }
            if (defaultD != null) {
                drawable.addState(STATES[STATES.length - 1], defaultD);
            }
        }

        return drawable;
    }

    private GradientDrawable generateShape(int index) {
        GradientDrawable drawable = null;
        if (gradients[index] != null && gradients[index].first != null && gradients[index].second != null) {
            //渐变
            drawable = new GradientDrawable(gradients[index].first, gradients[index].second);
            drawable.setCornerRadii(radius[index]);
            if (strokes[index] != null) {
                drawable.setStroke(strokes[index][0], strokes[index][1]);
            }
        } else {
            boolean solid = solids[index] != SelectorFactory.INVALID;
            boolean stroke = strokes[index] != null && strokes[index][0] > 0 && strokes[index][1] != SelectorFactory.INVALID;
            if (solid || stroke) {
                //solid不为INVALID或者有Stroke视为需要一个图层
                drawable = new GradientDrawable();
                drawable.setCornerRadii(radius[index]);
                drawable.setColor(solids[index] == SelectorFactory.INVALID ? Color.TRANSPARENT : solids[index]);
                if (stroke) {
                    drawable.setStroke(strokes[index][0], strokes[index][1]);
                }
            }
        }
        return drawable;
    }
}
