package com.tk.tdroid.router;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.tk.tdroid.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/24
 *     desc   : xxxx描述
 * </pre>
 */
public final class TRouter {
    private static final String TAG = "TRouter";
    private static boolean LOG = true;
    private static final Map<String, Class<?>> activityMap = new HashMap<>();
    private static final Map<String, Class<?>> serviceMap = new HashMap<>();
    private static final Map<String, Class<?>> fragmentMap = new HashMap<>();
    private static final Map<String, Class<?>> fragmentV4Map = new HashMap<>();

    public static void enabledLog(boolean log) {
        TRouter.LOG = log;
    }

    /**
     * 初始化映射关系表
     *
     * @param table
     */
    public static void register(IRouterTable table) {
        activityMap.putAll(table.getActivityMap());
        serviceMap.putAll(table.getServiceMap());
        fragmentMap.putAll(table.getFragmentMap());
        fragmentV4Map.putAll(table.getFragmentV4Map());
    }

    public static RouterCell with(String path) {
        return new RouterCell(path);
    }


    @Nullable
    public static Class<?> findActivity(String path) {
        return activityMap.get(path);
    }

    @Nullable
    public static Class<?> findService(String path) {
        return serviceMap.get(path);
    }

    @Nullable
    public static Class<?> findFragment(String path) {
        return fragmentMap.get(path);
    }

    @Nullable
    public static Class<?> findFragmentV4(String path) {
        return fragmentV4Map.get(path);
    }

    @Nullable
    public static Fragment instanceFragment(String path) {
        try {
            Class<?> cls = fragmentMap.get(path);
            if (cls == null) {
                if (LOG) {
                    Log.e(TAG, "fragmentMap find null");
                }
                return null;
            }
            return (Fragment) cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static android.support.v4.app.Fragment instanceFragmentV4(String path) {
        try {
            Class<?> cls = fragmentV4Map.get(path);
            if (cls == null) {
                if (LOG) {
                    Log.e(TAG, "fragmentV4Map find null");
                }
                return null;
            }
            return (android.support.v4.app.Fragment) cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static void request(@Nullable final Context context, final int requestCode, @NonNull final RouterCell routerCell) {
        Class<?> actCls = findActivity(routerCell.routerPath);
        if (actCls == null) {
            if (LOG) {
                Log.e(TAG, "activityMap find null");
            }
            return;
        }
        Intent intent = new Intent();
        intent.putExtras(routerCell.getExtra())
                .setFlags(routerCell.getFlags());
        final Context callContext;
        if (context != null) {
            //尝试提取出Activity的上下文 , 可能来自View的
            Context activityContext = findActivityContext(context);
            if (activityContext != null) {
                callContext = activityContext;
            } else {
                callContext = Utils.getApp();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        } else {
            callContext = Utils.getApp();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        intent.setComponent(new ComponentName(callContext, actCls));
        if (Looper.getMainLooper() != Looper.myLooper()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    call(callContext, intent, requestCode, routerCell);
                }
            });
        } else {
            call(callContext, intent, requestCode, routerCell);
        }
    }

    private static void call(Context context, Intent intent, int requestCode, RouterCell routerCell) {
        if (requestCode != -1 && context instanceof Activity) {
            ActivityCompat.startActivityForResult((Activity) context, intent, requestCode, routerCell.getOptions());
        } else {
            ActivityCompat.startActivity(context, intent, routerCell.getOptions());
        }
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(routerCell.getEnterAnim(), routerCell.getExitAnim());
        }
    }

    /**
     * 提取Activity的上下文
     *
     * @param context
     * @return
     */
    private static Context findActivityContext(Context context) {
        Context tempContext = context;
        while (tempContext instanceof ContextWrapper) {
            if (tempContext instanceof Activity) {
                return tempContext;
            }
            //5.0以下会被修饰为TintContextWrapper
            tempContext = ((ContextWrapper) tempContext).getBaseContext();
        }
        return null;
    }
}
