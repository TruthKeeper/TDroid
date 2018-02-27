package com.tk.tdroid.func;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.tk.tdroid.utils.EmptyUtils;

import java.util.Map;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/29
 *     desc   : 通信执行管理器
 * </pre>
 */
public final class FunctionManager {
    private static final String TAG = "FunctionManager";

    private static volatile FunctionManager mFunctionManager = null;
    private final Map<String, Function> mFunctionMap = new ArrayMap<>();

    private FunctionManager() {
    }

    public static FunctionManager getInstance() {
        if (mFunctionManager == null) {
            synchronized (FunctionManager.class) {
                if (mFunctionManager == null) {
                    mFunctionManager = new FunctionManager();
                }
            }
        }
        return mFunctionManager;
    }


    /**
     * 注册一个方法
     *
     * @param functionName
     * @param function
     * @return
     */
    public FunctionManager register(@NonNull String functionName, @NonNull Function function) {
        mFunctionMap.put(functionName, function);
        return this;
    }

    /**
     * 反注册一个方法
     *
     * @param functionName
     * @return
     */
    @Deprecated
    public void unregister(@NonNull String functionName) {
        mFunctionMap.remove(functionName);
    }

    /**
     * 执行方法
     *
     * @param functionName
     * @param param
     * @param <Param>
     * @param <Result>     可以存放{@link io.reactivex.Observable} 来完成异步通信
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <Param, Result> Result call(@NonNull String functionName, @Nullable Param param) {
        if (EmptyUtils.isEmpty(functionName)) {
            return null;
        }
        Function<Param, Result> function = mFunctionMap.get(functionName);
        if (function != null) {
            try {
                return function.call(param);
            } catch (ClassCastException e) {
                Log.e(TAG, "Param in Function cannot be casted , please check register() , FunctionName : " + functionName);
                e.printStackTrace();
            } catch (Exception e) {
                Log.e(TAG, "call exception , FunctionName : " + functionName);
                e.printStackTrace();
            }
        }
        return null;
    }
}