package com.tk.tdroid.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/03/13
 *     desc   : Drawable工具类
 * </pre>
 */
public final class DrawableUtils {
    private DrawableUtils() {
        throw new IllegalStateException();
    }

    /**
     * 获取Drawable , SVG的4.+兼容处理 , 代替{@link android.support.v7.widget.TintTypedArray}
     *
     * @param array
     * @param index
     * @return
     */
    @Nullable
    public static Drawable getTintDrawable(@NonNull TypedArray array, @StyleableRes int index) {
        int resourceId = array.getResourceId(index, -1);
        return resourceId == -1 ? null : AppCompatResources.getDrawable(Utils.getApp(), resourceId);
    }

    /**
     * 着色
     *
     * @param drawable
     * @param color
     */
    public static Drawable tint(@NonNull Drawable drawable, @ColorInt int color) {
        Drawable d = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTint(d, color);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        return d;
    }

    /**
     * 着色
     *
     * @param drawable
     * @param color
     * @param mode
     * @return
     */
    public static Drawable tint(@NonNull Drawable drawable, @ColorInt int color, @NonNull PorterDuff.Mode mode) {
        Drawable d = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintMode(d, mode);
        DrawableCompat.setTint(d, color);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        return d;
    }

    /**
     * 着色，注意states顺序
     * <pre>
     *     int[][] states = new int[2][];
     *     states[0] = new int[]{android.R.attr.state_pressed};
     *     states[1] = new int[]{};
     *
     *     new int[]{
     *     ContextCompat.getColor(v.getContext(), R.color.colorAccent),
     *     ContextCompat.getColor(v.getContext(), R.color.colorPrimaryDark)
     * }));
     * </pre>
     *
     * @param drawable
     * @param states
     * @param colors
     * @return StateListDrawable
     */
    public static Drawable tint(@NonNull Drawable drawable, @NonNull int[][] states, @NonNull int colors[]) {
        if (states.length != colors.length) {
            throw new IllegalArgumentException("states length error");
        }
        StateListDrawable listDrawable = new StateListDrawable();
        ColorStateList colorStateList = new ColorStateList(states, colors);
        for (int[] state : states) {
            listDrawable.addState(state, drawable);
        }
        Drawable result = DrawableCompat.wrap(listDrawable);
        DrawableCompat.setTintList(result, colorStateList);
        result.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
        return result;
    }

    /**
     * StateListDrawable XML中定义SVG的兼容方式
     *
     * @param context
     * @param defaultRes
     * @param pressedRes
     * @param selectedRes
     * @return
     */
    public static Drawable createStateListDrawable(@NonNull Context context,
                                                   @DrawableRes int defaultRes,
                                                   @DrawableRes int pressedRes,
                                                   @DrawableRes int selectedRes) {
        if (defaultRes == 0) {
            throw new IllegalArgumentException("defaultRes error");
        }
        StateListDrawable listDrawable = new StateListDrawable();
        if (selectedRes != 0) {
            listDrawable.addState(new int[]{android.R.attr.state_selected},
                    VectorDrawableCompat.create(context.getResources(), selectedRes, context.getTheme()));
        }
        if (pressedRes != 0) {
            listDrawable.addState(new int[]{android.R.attr.state_pressed},
                    VectorDrawableCompat.create(context.getResources(), pressedRes, context.getTheme()));
        }
        listDrawable.addState(new int[]{},
                VectorDrawableCompat.create(context.getResources(), defaultRes, context.getTheme()));
        listDrawable.setBounds(0, 0, listDrawable.getIntrinsicWidth(), listDrawable.getIntrinsicHeight());
        return listDrawable;
    }

    /**
     * StateListDrawable是否为空
     *
     * @param drawable
     * @return
     */
    public static boolean isStateListDrawableEmpty(@Nullable StateListDrawable drawable) {
        if (drawable == null) {
            return true;
        }
        DrawableContainer.DrawableContainerState state = ((DrawableContainer.DrawableContainerState) drawable.getConstantState());
        return state == null || state.getChildCount() < 1;
    }
}
