package com.tk.tdroid.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/2/5
 *     desc   : Bar工具类
 * </pre>
 */
public final class BarUtils {
    private static final String TAG_FAKE = "TAG_FAKE";

    /**
     * 设置状态栏颜色，默认纯色
     *
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(@NonNull Activity activity, @ColorInt int color) {
        setStatusBarColor(activity, color, 255);
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param color
     * @param alpha
     */
    public static void setStatusBarColor(@NonNull Activity activity,
                                         @ColorInt int color,
                                         @IntRange(from = 0, to = 255) int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0+
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateNewColor(color, alpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4.+
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            fitStatusFakeView(activity, color, alpha);
            setRootViewFit(activity, true);
        }
    }

    /**
     * <pre>
     *     适用于根布局为DrawerLayout
     *     设置状态栏颜色，默认纯色
     * <pre/>
     * @param activity
     * @param drawerLayout
     * @param color
     */
    public static void setStatusBarColorInDrawer(@NonNull Activity activity,
                                                 @NonNull DrawerLayout drawerLayout,
                                                 @ColorInt int color) {
        setStatusBarColorInDrawer(activity, drawerLayout, color, 255);
    }

    /**
     * <pre>
     *     适用于根布局为DrawerLayout
     *     设置状态栏颜色，默认纯色
     * <pre/>
     * @param activity
     * @param drawerLayout
     * @param color
     * @param alpha
     */
    public static void setStatusBarColorInDrawer(@NonNull Activity activity,
                                                 @NonNull DrawerLayout drawerLayout,
                                                 @ColorInt int color,
                                                 @IntRange(from = 0, to = 255) int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0+
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateNewColor(color, alpha));
            setDrawerFit(drawerLayout);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4.+
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            fitStatusFakeView(activity, color, alpha);
            ((ViewGroup.MarginLayoutParams) drawerLayout.getLayoutParams()).topMargin = getStatusBarHeight(activity);
            setDrawerFit(drawerLayout);
        }
    }

    /**
     * 使状态栏半透明
     *
     * @param activity
     */
    public static void setTranslucent(@NonNull Activity activity) {
        setTranslucent(activity, Color.WHITE, 0);
    }

    /**
     * 使状态栏半透明
     *
     * @param activity
     * @param color
     * @param alpha
     */
    public static void setTranslucent(@NonNull Activity activity,
                                      @ColorInt int color,
                                      @IntRange(from = 0, to = 255) int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0+
            activity.getWindow().setStatusBarColor(calculateNewColor(color, alpha));
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            setRootViewFit(activity, false);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4+
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            fitStatusFakeView(activity, color, alpha);
            setRootViewFit(activity, false);
        }
    }

    /**
     * <pre>
     *     适用于根布局为DrawerLayout
     *     使状态栏半透明，默认完全透明
     * <pre/>
     * @param activity
     * @param drawerLayout
     */
    public static void setTranslucentInDrawer(@NonNull Activity activity, @NonNull DrawerLayout drawerLayout) {
        setTranslucentInDrawer(activity, drawerLayout, Color.WHITE, 0);
    }

    /**
     * <pre>
     *     适用于根布局为DrawerLayout
     *     使状态栏半透明，默认完全透明
     * <pre/>
     * @param activity
     * @param drawerLayout
     * @param color
     * @param alpha
     */
    public static void setTranslucentInDrawer(@NonNull Activity activity,
                                              @NonNull DrawerLayout drawerLayout,
                                              @ColorInt int color,
                                              @IntRange(from = 0, to = 255) int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0+
            activity.getWindow().setStatusBarColor(calculateNewColor(color, alpha));
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            setRootViewFit(activity, false);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4+
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            fitStatusFakeView(activity, color, alpha);
//            setRootViewFit(activity, false);
            setDrawerFit(drawerLayout);
        }
    }

    /**
     * 使状态栏半透明，标题View向下偏移
     *
     * @param activity
     * @param statusBarAlpha
     */
    public static void setTranslucentOffset(@NonNull final Activity activity,
                                            @NonNull final View needOffsetView,
                                            @ColorInt final int color,
                                            @IntRange(from = 0, to = 255) final int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        //Post来无缝切换
        activity.findViewById(android.R.id.content).post(new Runnable() {
            @Override
            public void run() {
                setTranslucent(activity, color, statusBarAlpha);
                ((ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams()).topMargin = getStatusBarHeight(activity);
            }
        });

    }

    /**
     * Fragment中设置状态栏颜色，默认纯色
     *
     * @param activity
     * @param fragment
     * @param color
     */
    public static void setStatusBarColorInFragment(@NonNull Activity activity, @NonNull Fragment fragment, @ColorInt int color) {
        setStatusBarColorInFragment(activity, fragment, color, 255);
    }

    /**
     * Fragment中设置状态栏颜色
     *
     * @param activity
     * @param fragment
     * @param color
     * @param statusBarAlpha
     */
    public static void setStatusBarColorInFragment(@NonNull final Activity activity,
                                                   @NonNull final Fragment fragment,
                                                   @ColorInt final int color,
                                                   @IntRange(from = 0, to = 255) final int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setStatusBarColor(activity, color, statusBarAlpha);
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        contentView.setPadding(0, 0, 0, 0);
        //设置头部偏移
        if (fragment.getView() != null) {
            fragment.getView().setPadding(0, getStatusBarHeight(activity), 0, 0);
        } else {
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    if (fragment.getView() != null) {
                        fragment.getView().setPadding(0, getStatusBarHeight(activity), 0, 0);
                    }
                }
            });
        }
    }


    /**
     * 添加一个自适应FakeView
     *
     * @param activity
     * @param color
     * @param alpha
     * @param alpha
     */
    private static void fitStatusFakeView(Activity activity, @ColorInt int color, int alpha) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeView = decorView.findViewWithTag(TAG_FAKE);
        if (fakeView == null) {
            //add
            fakeView = generateFakeView(activity, color, alpha, true);
            decorView.addView(fakeView);
        } else {
            //refresh
            fakeView.setBackgroundColor(calculateNewColor(color, alpha));
        }
    }

    /**
     * 添加一个自适应FakeView
     *
     * @param activity
     * @param color
     * @param alpha
     * @param alpha
     */
    private static void fitFakeView(Activity activity, @ColorInt int color, int alpha) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeView = decorView.findViewWithTag(TAG_FAKE);
        if (fakeView == null) {
            //add
            fakeView = generateFakeView(activity, color, alpha, false);
            decorView.addView(fakeView);
        } else {
            //refresh
            fakeView.setBackgroundColor(calculateNewColor(color, alpha));
        }
    }

    /**
     * 适配DrawerLayout
     *
     * @param drawerLayout
     */
    private static void setDrawerFit(DrawerLayout drawerLayout) {
        drawerLayout.setFitsSystemWindows(false);
        drawerLayout.getChildAt(0).setFitsSystemWindows(false);
        ((ViewGroup) drawerLayout.getChildAt(0)).setClipToPadding(true);
        drawerLayout.getChildAt(1).setFitsSystemWindows(false);
    }

    /**
     * 设置根布局相关参数
     *
     * @param activity
     */
    private static void setRootViewFit(Activity activity, boolean fitsSystemWindow) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                if (fitsSystemWindow) {
                    childView.setFitsSystemWindows(true);
                    ((ViewGroup) childView).setClipToPadding(true);
                } else {
                    childView.setFitsSystemWindows(false);
                }
            }
        }
    }

    /**
     * 生成一个和状态栏大小相同的矩形
     *
     * @param activity
     * @param color
     * @param alpha
     * @return
     */
    private static View generateFakeView(Activity activity, @ColorInt int color, int alpha, boolean top) {
        View fakeView = new View(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        if (top) {
            params.gravity = Gravity.TOP;
        } else {
            params.gravity = Gravity.BOTTOM;
        }
        fakeView.setLayoutParams(params);
        fakeView.setBackgroundColor(calculateNewColor(color, alpha));
        fakeView.setTag(TAG_FAKE);
        return fakeView;
    }


    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(@NonNull Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 计算状态栏颜色
     *
     * @param color
     * @param alpha
     * @return
     */
    public static int calculateNewColor(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
}