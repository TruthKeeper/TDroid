package com.tk.tdroiddemo.aop.aspect;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tk.tdroid.utils.NetworkUtils;
import com.tk.tdroid.utils.Utils;
import com.tk.tdroiddemo.R;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/24
 *      desc : 校验网络切面
 * </pre>
 */
@Aspect
public class CheckNetworkAspect {
    private static final String ANNOTATION = "@com.tk.tdroiddemo.aop.annotation.CheckNetwork";
    private static volatile boolean enabled = true;
    private static final Handler MAIN = new Handler();

    /**
     * 被注解类修饰的类
     */
    @Pointcut("within(" + ANNOTATION + " *)")
    public void withinAnnotatedClass() {
    }

    /**
     * 被注解类修饰的类+除去synthetic修饰的方法
     */
    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    /**
     * 被注解类修饰的方法
     */
    @Pointcut("execution(" + ANNOTATION + " * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }

    /**
     * 处理切面
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("method()")
    public Object check(ProceedingJoinPoint joinPoint) throws Throwable {
        if (NetworkUtils.isConnected()) {
            return joinPoint.proceed();
        }
        //无网络不执行
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(Utils.getApp(), R.string.TDroid_no_network, Toast.LENGTH_SHORT).show();
        } else {
            MAIN.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Utils.getApp(), R.string.TDroid_no_network, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return null;
    }
}