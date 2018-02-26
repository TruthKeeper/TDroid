package com.tk.tdroid.router;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.tk.tdroid.utils.EmptyUtils;
import com.tk.tdroid.utils.UrlUtils;
import com.tk.tdroid.utils.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/24
 *     desc   : 路由
 * </pre>
 */
public final class TRouter {
    private static final String TAG = "TRouter";
    /**
     * 路由的源Path，可通过{@link Intent#getStringExtra(String)}获取
     */
    public static final String RAW_PATH = "TRouter_Raw_Path";
    private static boolean LOG = true;
    private static final Map<String, Class<?>> activityMap = new HashMap<>();
    private static final Map<String, Class<?>> serviceMap = new HashMap<>();
    private static final Map<String, Class<?>> fragmentMap = new HashMap<>();
    private static final Map<String, Class<?>> fragmentV4Map = new HashMap<>();
    private static final List<Interceptor> globalInterceptors = new LinkedList<>();

    public static void enabledLog(boolean log) {
        TRouter.LOG = log;
    }

    /**
     * 添加全局拦截器
     *
     * @param interceptor
     */
    public static void addGlobalInterceptor(@NonNull Interceptor interceptor) {
        globalInterceptors.add(interceptor);
    }

    /**
     * 移除全局拦截器
     *
     * @param interceptor
     */
    public static void removeGlobalInterceptor(@NonNull Interceptor interceptor) {
        globalInterceptors.remove(interceptor);
    }

    /**
     * 清空全部全局拦截器
     */
    public static void clearAllGlobalInterceptor() {
        globalInterceptors.clear();
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

    /**
     * 获取一个路由
     *
     * @param path
     * @return
     */
    public static RouterCell with(String path) {
        return new RouterCell(path);
    }

    /**
     * 在拦截器中打包传递数据
     *
     * @param cell
     * @return
     */
    public static Bundle packageInInterceptor(RouterCell cell) {
        Bundle bundle = new Bundle();
        bundle.putString(RAW_PATH, cell.routerPath);
        bundle.putAll(cell.getExtra());
        return bundle;
    }

    /**
     * 继续路由
     *
     * @param bundle  from {@link TRouter#packageInInterceptor(RouterCell)}
     * @param context
     */
    public static void resume(Bundle bundle, Context context) {
        with(bundle.getString(RAW_PATH))
                .extra(bundle)
                .request(context);
    }

    /**
     * 获取Activity的路由Class
     *
     * @param path
     * @return
     */
    @Nullable
    public static Class<?> findActivity(String path) {
        return activityMap.get(path);
    }

    /**
     * 获取Service的路由Class
     *
     * @param path
     * @return
     */
    @Nullable
    public static Class<?> findService(String path) {
        return serviceMap.get(path);
    }

    /**
     * 获取Fragment的路由Class
     *
     * @param path
     * @return
     */
    @Nullable
    public static Class<?> findFragment(String path) {
        return fragmentMap.get(path);
    }

    /**
     * 获取Fragment的路由Class
     *
     * @param path
     * @return
     */
    @Nullable
    public static Class<?> findFragmentV4(String path) {
        return fragmentV4Map.get(path);
    }

    /**
     * 实例化Fragment
     *
     * @param path
     * @return
     */
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

    /**
     * 实例化Fragment
     *
     * @param path
     * @return
     */
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
        if (!EmptyUtils.isEmpty(globalInterceptors)) {
            for (Interceptor interceptor : globalInterceptors) {
                if (interceptor.intercept(routerCell, context)) {
                    if (LOG) {
                        Log.d(TAG, "global interceptor ! Path：" + routerCell.routerPath + " Interceptor：" + interceptor.getClass().getSimpleName());
                    }
                    interceptor.onIntercepted(routerCell, context);
                    return;
                }
            }
        }
        if (!EmptyUtils.isEmpty(routerCell.getInterceptors())) {
            for (Interceptor interceptor : routerCell.getInterceptors()) {
                if (interceptor.intercept(routerCell, context)) {
                    if (LOG) {
                        Log.d(TAG, "interceptor ! Path：" + routerCell.routerPath + " Interceptor：" + interceptor.getClass().getSimpleName());
                    }
                    interceptor.onIntercepted(routerCell, context);
                    return;
                }
            }
        }
        final Intent intent = new Intent();

        final Class<?> actCls;
        final Uri uri = Uri.parse(routerCell.routerPath);
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        if (EmptyUtils.isEmpty(queryParameterNames)) {
            actCls = findActivity(routerCell.routerPath);
        } else {
            //当做Url处理，并且将Query注入到Intent中,String类型
            for (String queryParameterName : queryParameterNames) {
                intent.putExtra(queryParameterName, uri.getQueryParameter(queryParameterName));
            }
            actCls = findActivity(UrlUtils.excludeQuery(routerCell.routerPath));
        }

        if (actCls == null) {
            if (LOG) {
                Log.e(TAG, "activityMap find null");
            }
            return;
        }
        //存放路由的源Path
        intent.putExtra(RAW_PATH, routerCell.routerPath);
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
