package com.tk.tdroid.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/1
 *      desc : {@link Toast} 打印工具
 *      <ol>支持常用的
 *      <li>字体、文本大小、文本颜色配置</li>
 *      <li>左侧Icon大小、间距、颜色、Tint配置</li>
 *      <li>整体颜色、圆角、圆角边框配置</li>
 *      </ol>
 * </pre>
 */

public class Toasty {
    /**
     * 最大显示3行
     */
    private static final int MAX_LINE = 3;
    private static final int DEFAULT_HORIZONTAL_PADDING = DensityUtil.dp2px(18);
    private static final int DEFAULT_VERTICAL_PADDING = DensityUtil.dp2px(12);
    private static final int DEFAULT_ICON_SIZE = DensityUtil.dp2px(24);
    private static final int DEFAULT_ICON_PADDING = DensityUtil.dp2px(6);

    @IntDef({LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    @interface Duration {
    }

    private static Toast mToast = null;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private Toasty() {
    }

    /**
     * 判断当前Toast是否可用
     *
     * @return 不可用时需用户手动开启 {@link IntentUtils#toNotifySetting()}
     */
    public static boolean checkEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return NotificationManagerCompat.from(Utils.getApp()).areNotificationsEnabled();
        } else {
            AppOpsManager mAppOps = (AppOpsManager) Utils.getApp().getSystemService(Context.APP_OPS_SERVICE);
            Class<? extends AppOpsManager> cls = mAppOps.getClass();
            try {
                Method method = cls.getDeclaredMethod("noteOpNoThrow", int.class, int.class, String.class);
                if (method.invoke(mAppOps, 11, Binder.getCallingUid(), Utils.getApp().getPackageName()).equals(AppOpsManager.MODE_ALLOWED)) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
            }
            return true;
        }
    }

    /**
     * 显示
     *
     * @param resId
     */
    public static void show(@StringRes int resId, @Nullable final Config config) {
        show(Utils.getApp().getString(resId), config);
    }

    /**
     * 显示
     *
     * @param text
     */
    public static void show(@Nullable CharSequence text, @Nullable final Config config) {
        final Config realConfig = config == null ? new Config.Builder().build() : config;
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(text, realConfig);
                }
            });
        } else {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = new Toast(Utils.getApp());
            if (realConfig.customView != null) {
                mToast.setView(realConfig.customView);
            } else {
                TextView view = generateTextView(text, realConfig);
                view.setPadding(realConfig.horizontalPadding, realConfig.verticalPadding,
                        realConfig.horizontalPadding, realConfig.verticalPadding);
                //手动测量
                int measureSize = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                view.measure(measureSize, measureSize);
                view.setBackground(generateShape(view, realConfig));
                mToast.setView(view);
            }
            mToast.setDuration(realConfig.duration);
            mToast.show();
        }
    }

    private static GradientDrawable generateShape(View view, Toasty.Config config) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(Math.min(config.cornerRadius, view.getMeasuredHeight() >> 1));
        gradientDrawable.setColor(config.backgroundColor);
        gradientDrawable.setStroke(config.strokeWidth, config.strokeColor);
        gradientDrawable.setAlpha(config.alpha);
        return gradientDrawable;
    }

    private static TextView generateTextView(CharSequence text, Toasty.Config config) {
        TextView textView = new TextView(Utils.getApp());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(config.textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.textSizeSp);
        textView.setTypeface(config.typeface);
        textView.setMaxLines(MAX_LINE);
        textView.setEllipsize(TextUtils.TruncateAt.END);

        if (config.icon != null) {
            if (!EmptyUtils.isEmpty(text)) {
                textView.setCompoundDrawablePadding(config.iconPadding);
            }
            Drawable icon = DrawableCompat.wrap(config.icon.mutate());
            if (config.tintByTextColor) {
                DrawableCompat.setTint(icon, config.textColor);
            }
            icon.setBounds(0, 0, config.iconWidth, config.iconHeight);
            textView.setCompoundDrawables(icon, null, null, null);
        }

        return textView;
    }

    public static class Config {
        private final int textColor;
        private final int textSizeSp;
        private final Typeface typeface;

        private final Drawable icon;
        private final int iconWidth;
        private final int iconHeight;
        private final int iconPadding;
        private final boolean tintByTextColor;

        private final int backgroundColor;
        private final int strokeColor;
        private final int strokeWidth;
        private final int cornerRadius;
        private final int alpha;

        private final int horizontalPadding;
        private final int verticalPadding;

        private final View customView;
        private final int duration;

        private Config(Builder builder) {
            textColor = builder.textColor;
            textSizeSp = builder.textSizeSp;
            typeface = builder.typeface;

            icon = builder.icon;
            iconWidth = builder.iconWidth;
            iconHeight = builder.iconHeight;
            iconPadding = builder.iconPadding;
            tintByTextColor = builder.tintByTextColor;

            backgroundColor = builder.backgroundColor;
            strokeColor = builder.strokeColor;
            strokeWidth = builder.strokeWidth;
            cornerRadius = builder.cornerRadius;
            alpha = builder.alpha;

            horizontalPadding = builder.horizontalPadding;
            verticalPadding = builder.verticalPadding;

            customView = builder.customView;
            duration = builder.duration;
        }

        public static final class Builder {
            private int textColor = Color.WHITE;
            private int textSizeSp = 16;
            private Typeface typeface = Typeface.DEFAULT;

            private Drawable icon = null;
            private int iconWidth = DEFAULT_ICON_SIZE;
            private int iconHeight = DEFAULT_ICON_SIZE;
            private int iconPadding = DEFAULT_ICON_PADDING;
            private boolean tintByTextColor = true;

            private int backgroundColor = 0xFF494949;
            private int strokeColor = Color.TRANSPARENT;
            private int strokeWidth = 0;
            private int cornerRadius = Integer.MAX_VALUE;
            private int alpha = 200;

            private int horizontalPadding = DEFAULT_HORIZONTAL_PADDING;
            private int verticalPadding = DEFAULT_VERTICAL_PADDING;

            private View customView = null;
            private int duration = LENGTH_SHORT;

            /**
             * @param textColor 文字颜色
             * @return
             */
            public Builder textColor(@ColorInt int textColor) {
                this.textColor = textColor;
                return this;
            }

            /**
             * @param textColorRes {@code @color}资源
             * @return
             */
            public Builder textColorRes(@ColorRes int textColorRes) {
                this.textColor = ContextCompat.getColor(Utils.getApp(), textColorRes);
                return this;
            }

            /**
             * @param textSizeSp 字体大小 单位sp
             * @return
             */
            public Builder textSizeSp(int textSizeSp) {
                this.textSizeSp = textSizeSp;
                return this;
            }

            /**
             * @param typeface 字体
             * @return
             */
            public Builder typeface(@Nullable Typeface typeface) {
                this.typeface = typeface;
                return this;
            }

            /**
             * @param icon 左边的icon
             * @return
             */
            public Builder icon(@Nullable Drawable icon) {
                this.icon = icon;
                return this;
            }

            /**
             * @param bitmap 左边的icon
             * @return
             */
            public Builder icon(@Nullable Bitmap bitmap) {
                this.icon = bitmap == null ? null : new BitmapDrawable(Utils.getApp().getResources(), bitmap);
                return this;
            }

            /**
             * @param iconWidth  图标宽 单位:px 默认 {@link Toasty#DEFAULT_ICON_SIZE}
             * @param iconHeight 图标高 单位:px 默认 {@link Toasty#DEFAULT_ICON_SIZE}
             * @return
             */
            public Builder iconSize(@IntRange(from = 0) int iconWidth, @IntRange(from = 0) int iconHeight) {
                this.iconWidth = iconWidth;
                this.iconHeight = iconHeight;
                return this;
            }

            /**
             * @param iconPadding 图标间距 单位:px 默认 {@link Toasty#DEFAULT_ICON_PADDING}
             * @return
             */
            public Builder iconPadding(@IntRange(from = 0) int iconPadding) {
                this.iconPadding = iconPadding;
                return this;
            }

            /**
             * @param tintByTextColor 是否对icon进行字体颜色的Tint着色处理 默认:true
             * @return
             */
            public Builder tintByTextColor(boolean tintByTextColor) {
                this.tintByTextColor = tintByTextColor;
                return this;
            }

            /**
             * @param backgroundColor 背景颜色
             * @return
             */
            public Builder backgroundColor(@ColorInt int backgroundColor) {
                this.backgroundColor = backgroundColor;
                return this;
            }

            /**
             * @param backgroundColorRes {@code @color}资源
             * @return
             */
            public Builder backgroundColorRes(@ColorRes int backgroundColorRes) {
                this.backgroundColor = ContextCompat.getColor(Utils.getApp(), backgroundColorRes);
                return this;
            }

            /**
             * @param strokeColor 边框颜色
             * @return
             */
            public Builder strokeColor(@ColorInt int strokeColor) {
                this.strokeColor = strokeColor;
                return this;
            }

            /**
             * @param strokeColorRes {@code @color}资源
             * @return
             */
            public Builder strokeColorRes(@ColorRes int strokeColorRes) {
                this.strokeColor = ContextCompat.getColor(Utils.getApp(), strokeColorRes);
                return this;
            }

            /**
             * @param strokeWidth 边框粗细 单位:sp
             * @return
             */
            public Builder strokeWidth(int strokeWidth) {
                this.strokeWidth = strokeWidth;
                return this;
            }

            /**
             * @param cornerRadius 圆角大小 单位:px 最大为{@link Toast}的一半高度
             * @return
             */
            public Builder cornerRadius(@IntRange(from = 0) int cornerRadius) {
                this.cornerRadius = cornerRadius;
                return this;
            }


            /**
             * @param alpha 背景的透明度 默认 不透明
             * @return
             */
            public Builder alpha(@IntRange(from = 0, to = 255) int alpha) {
                this.alpha = alpha;
                return this;
            }

            /**
             * @param horizontalPadding Toast横向的Padding 默认 {@link Toasty#DEFAULT_HORIZONTAL_PADDING }
             * @return
             */
            public Builder horizontalPadding(@IntRange(from = 0) int horizontalPadding) {
                this.horizontalPadding = horizontalPadding;
                return this;
            }

            /**
             * @param verticalPadding Toast纵向的Padding 默认 {@link Toasty#DEFAULT_VERTICAL_PADDING }
             * @return
             */
            public Builder verticalPadding(@IntRange(from = 0) int verticalPadding) {
                this.verticalPadding = verticalPadding;
                return this;
            }

            /**
             * @param customView 完全自定义的View
             * @return
             */
            public Builder customView(View customView) {
                this.customView = customView;
                return this;
            }

            /**
             * @param duration 显示时长 默认 {@link Toast#LENGTH_SHORT}
             * @return
             */
            public Builder duration(@Duration int duration) {
                this.duration = duration;
                return this;
            }

            public Config build() {
                return new Config(this);
            }
        }
    }
}
