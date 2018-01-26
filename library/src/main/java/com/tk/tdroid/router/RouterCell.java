package com.tk.tdroid.router;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;


/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/25
 *     desc   : 路由的执行单元
 * </pre>
 */
public class RouterCell {
    /**
     * 路由标志
     */
    public final String routerPath;
    private int flags = 0;
    private Bundle extra = new Bundle();
    private int enterAnim = 0;
    private int exitAnim = 0;
    /**
     * 过场动画
     */
    private Bundle options;

    RouterCell(String routerPath) {
        this.routerPath = routerPath;
    }

    public RouterCell flags(int flags) {
        this.flags |= flags;
        return this;
    }

    int getFlags() {
        return flags;
    }

    public RouterCell extra(Bundle extra) {
        this.extra = extra;
        return this;
    }

    public Bundle getExtra() {
        return extra;
    }

    public RouterCell putInt(@Nullable String key, int value) {
        this.extra.putInt(key, value);
        return this;
    }

    public RouterCell putDouble(@Nullable String key, double value) {
        this.extra.putDouble(key, value);
        return this;
    }

    public RouterCell putFloat(@Nullable String key, float value) {
        this.extra.putFloat(key, value);
        return this;
    }

    public RouterCell putLong(@Nullable String key, long value) {
        this.extra.putLong(key, value);
        return this;
    }

    public RouterCell putBoolean(@Nullable String key, boolean value) {
        this.extra.putBoolean(key, value);
        return this;
    }

    public RouterCell putString(@Nullable String key, String value) {
        this.extra.putString(key, value);
        return this;
    }

    public RouterCell anim(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    int getEnterAnim() {
        return enterAnim;
    }

    int getExitAnim() {
        return exitAnim;
    }

    public RouterCell options(Bundle options) {
        this.options = options;
        return this;
    }

    Bundle getOptions() {
        return options;
    }

    public void request() {
        TRouter.request(null, -1, this);
    }

    public void request(Context context) {
        TRouter.request(context, -1, this);
    }

    public void request(Activity activity) {
        TRouter.request(activity, -1, this);
    }

    public void request(Activity activity, int requestCode) {
        TRouter.request(activity, requestCode, this);
    }
}
