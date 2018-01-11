package com.tk.tdroid.aop.aspect;

import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import com.tk.tdroid.utils.EmptyUtils;
import com.tk.tdroid.aop.annotation.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/23
 *      desc : 借鉴自Jake Wharton hugo https://github.com/JakeWharton/hugo/blob/master/hugo-runtime/src/main/java/hugo/weaving/internal/Hugo.java
 * </pre>
 */
@Aspect
public class LoggerAspect {
    // TODO: 2018/1/11 手动添加
    public static LoggerAspect aspectOf() {
        return new LoggerAspect();
    }

    public static final String TAG = "Logger";
    private static final String ANNOTATION = "@com.tk.tdroid.aop.annotation.Logger";
    private static volatile boolean enabled = true;

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
     * 被注解类修饰的类+除去synthetic修饰的构造方法
     */
    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    /**
     * 被注解类修饰的方法
     */
    @Pointcut("execution(" + ANNOTATION + " * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }

    /**
     * 被注解类修饰的构造方法
     */
    @Pointcut("execution(" + ANNOTATION + " *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {
    }

    /**
     * 处理切面
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("method() || constructor()")
    public Object loggerAndProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);

        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        long lengthMillis = (stopNanos - startNanos) / 1000 / 1000;

        exitMethod(joinPoint, result, lengthMillis);
        return result;
    }

    /**
     * 方法前
     *
     * @param joinPoint
     */
    @SuppressWarnings("WrongConstant")
    private static void enterMethod(JoinPoint joinPoint) {
        if (!enabled) {
            return;
        }
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        Logger logger = null;
        int type = Log.DEBUG;
        String tag = TAG;

        if (codeSignature instanceof MethodSignature) {
            logger = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Logger.class);
        } else if (codeSignature instanceof ConstructorSignature) {
            logger = (Logger) ((ConstructorSignature) joinPoint.getSignature()).getConstructor().getAnnotation(Logger.class);
        }
        if (logger != null) {
            type = logger.type();
            tag = EmptyUtils.isEmpty(logger.tag()) ? TAG : logger.tag();
        }

        Class<?> cls = codeSignature.getDeclaringType();
        String methodName = codeSignature.getName();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        StringBuilder builder = new StringBuilder("\u2192 ");
        builder.append(methodName)
                .append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            //拼接参数信息
            builder.append(parameterNames[i]).append('=');
            builder.append(Strings.toString(parameterValues[i]));
        }
        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }
        Log.println(type, tag + asTag(cls), builder.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final String section = builder.toString().substring(2);
            Trace.beginSection(section);
        }
    }

    /**
     * 方法后
     *
     * @param joinPoint
     */
    @SuppressWarnings("WrongConstant")
    private static void exitMethod(JoinPoint joinPoint, Object result, long lengthMillis) {
        if (!enabled) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        Logger logger = null;
        int type = Log.DEBUG;
        String tag = TAG;
        boolean hasReturnType = false;

        if (codeSignature instanceof MethodSignature) {
            logger = ((MethodSignature) codeSignature).getMethod().getAnnotation(Logger.class);
            hasReturnType = ((MethodSignature) codeSignature).getReturnType() != void.class;
        } else if (codeSignature instanceof ConstructorSignature) {
            logger = (Logger) ((ConstructorSignature) joinPoint.getSignature()).getConstructor().getAnnotation(Logger.class);
        }
        if (logger != null) {
            type = logger.type();
            tag = EmptyUtils.isEmpty(logger.tag()) ? TAG : logger.tag();
        }

        Class<?> cls = codeSignature.getDeclaringType();
        String methodName = codeSignature.getName();

        StringBuilder builder = new StringBuilder("\u2190 ")
                .append(methodName)
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(Strings.toString(result));
        }

        Log.println(type, tag + asTag(cls), builder.toString());
    }

    private static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            //匿名类时，递归定义的类
            return asTag(cls.getEnclosingClass());
        }
        return ": " + cls.getSimpleName();
    }

    private static final class Strings {
        public static String toString(Object obj) {
            if (obj == null) {
                return "null";
            }
            if (obj instanceof CharSequence) {
                return '"' + printableToString(obj.toString()) + '"';
            }

            Class<?> cls = obj.getClass();
            if (Byte.class == cls) {
                return byteToString((Byte) obj);
            }

            if (cls.isArray()) {
                return arrayToString(cls.getComponentType(), obj);
            }
            return obj.toString();
        }

        private static String printableToString(String string) {
            int length = string.length();
            StringBuilder builder = new StringBuilder(length);
            for (int i = 0; i < length; ) {
                int codePoint = string.codePointAt(i);
                switch (Character.getType(codePoint)) {
                    case Character.CONTROL:
                    case Character.FORMAT:
                    case Character.PRIVATE_USE:
                    case Character.SURROGATE:
                    case Character.UNASSIGNED:
                        switch (codePoint) {
                            case '\n':
                                builder.append("\\n");
                                break;
                            case '\r':
                                builder.append("\\r");
                                break;
                            case '\t':
                                builder.append("\\t");
                                break;
                            case '\f':
                                builder.append("\\f");
                                break;
                            case '\b':
                                builder.append("\\b");
                                break;
                            default:
                                builder.append("\\u").append(String.format("%04x", codePoint).toUpperCase(Locale.US));
                                break;
                        }
                        break;
                    default:
                        builder.append(Character.toChars(codePoint));
                        break;
                }
                i += Character.charCount(codePoint);
            }
            return builder.toString();
        }

        private static String arrayToString(Class<?> cls, Object obj) {
            if (byte.class == cls) {
                return byteArrayToString((byte[]) obj);
            }
            if (short.class == cls) {
                return Arrays.toString((short[]) obj);
            }
            if (char.class == cls) {
                return Arrays.toString((char[]) obj);
            }
            if (int.class == cls) {
                return Arrays.toString((int[]) obj);
            }
            if (long.class == cls) {
                return Arrays.toString((long[]) obj);
            }
            if (float.class == cls) {
                return Arrays.toString((float[]) obj);
            }
            if (double.class == cls) {
                return Arrays.toString((double[]) obj);
            }
            if (boolean.class == cls) {
                return Arrays.toString((boolean[]) obj);
            }
            return arrayToString((Object[]) obj);
        }


        private static String byteArrayToString(byte[] bytes) {
            StringBuilder builder = new StringBuilder("[");
            for (int i = 0; i < bytes.length; i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                builder.append(byteToString(bytes[i]));
            }
            return builder.append(']').toString();
        }

        private static String byteToString(Byte b) {
            if (b == null) {
                return "null";
            }
            return "0x" + String.format("%02x", b).toUpperCase(Locale.US);
        }

        private static String arrayToString(Object[] array) {
            StringBuilder buf = new StringBuilder();
            arrayToString(array, buf, new HashSet<Object[]>());
            return buf.toString();
        }

        private static void arrayToString(Object[] array, StringBuilder builder, Set<Object[]> seen) {
            if (array == null) {
                builder.append("null");
                return;
            }

            seen.add(array);
            builder.append('[');
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    builder.append(", ");
                }

                Object element = array[i];
                if (element == null) {
                    builder.append("null");
                } else {
                    Class elementClass = element.getClass();
                    if (elementClass.isArray() && elementClass.getComponentType() == Object.class) {
                        Object[] arrayElement = (Object[]) element;
                        if (seen.contains(arrayElement)) {
                            builder.append("[...]");
                        } else {
                            arrayToString(arrayElement, builder, seen);
                        }
                    } else {
                        builder.append(toString(element));
                    }
                }
            }
            builder.append(']');
            seen.remove(array);
        }
    }
}
