package com.tk.tdroid.utils;

import android.animation.ValueAnimator;

import java.lang.reflect.Field;

/**
 * @Author: AnimUtils
 * @CreateTime: 2019/3/5 14:52
 * @Description: 2019/3/5
 * @Date: ***
 */
public final class AnimUtils {
    private AnimUtils() {
        throw new IllegalStateException();
    }

    /**
     * 确保业务逻辑基于值动画来实现的场景可以确保继续
     * Ps:开发者选项中 动画缩放0x会关闭动画
     */
    public static void keeepValueAnim() {
        try {
            Field field = ValueAnimator.class.getDeclaredField("sDurationScale");
            field.setAccessible(true);
            if (field.getFloat(null) != 1) {
                field.setFloat(null, 1);
            }
        } catch (Exception e) {
        }
    }
}
