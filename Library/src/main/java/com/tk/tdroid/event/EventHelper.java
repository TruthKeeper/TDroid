package com.tk.tdroid.event;


import android.support.annotation.Keep;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/11/20
 *     desc   : 事件总线工具类
 * </pre>
 */
public final class EventHelper {
    private static final String TAG = "EventHelper";
    public static boolean EVENT_LOG = true;

    private EventHelper() {
        throw new IllegalStateException();
    }

    /**
     * 初始化
     *
     * @param aptIndex
     */
//    public static void init(@NonNull SubscriberInfoIndex aptIndex) {
//        EventBus.builder()
//                .addIndex(aptIndex)
//                .installDefaultEventBus();
//    }

    /**
     * 注册
     *
     * @param object
     */
//    public static void register(Object object) {
//        EventBus.getDefault().register(object);
//    }

    /**
     * 反注册
     *
     * @param object
     */
//    public static void unregister(Object object) {
//        EventBus.getDefault().unregister(object);
//    }

    /**
     * 发送Tag
     *
     * @param tag 消息唯一标识
     */
//    public static void postByTag(@NonNull String tag) {
//        Event<Void> event = Event.with(tag);
//        if (EVENT_LOG) {
//            Log.d(TAG + "-Post", generateLogBody(event, true));
//        }
//        EventBus.getDefault().post(event);
//    }

    /**
     * 发送事件
     *
     * @param tag  消息唯一标识
     * @param body 消息内容 , 用@{@link Keep}修饰则打印JSON，否则则通过{@link Object#toString()}
     */
//    public static void post(@NonNull String tag, @Nullable Object body) {
//        Event event = Event.with(tag, body);
//        if (EVENT_LOG) {
//            Log.d(TAG + "-Post", generateLogBody(event, true));
//        }
//        EventBus.getDefault().post(event);
//    }

    /**
     * 发送粘性Tag
     *
     * @param tag 消息唯一标识
     */
//    public static void postStickyByTag(@NonNull String tag) {
//        Event<Void> event = Event.with(tag);
//        if (EVENT_LOG) {
//            Log.d(TAG + "-Post", generateLogBody(event, true));
//        }
//        EventBus.getDefault().postSticky(event);
//    }

    /**
     * 发送粘性事件
     *
     * @param tag  消息唯一标识
     * @param body 消息内容 , 用@{@link Keep}修饰则打印JSON，否则则通过{@link Object#toString()}
     */
//    public static void postSticky(@NonNull String tag, @Nullable Object body) {
//        Event event = Event.with(tag, body);
//        if (EVENT_LOG) {
//            Log.d(TAG + "-PostSticky", generateLogBody(event, true));
//        }
//        EventBus.getDefault().postSticky(event);
//    }

    /**
     * 生成日志体
     *
     * @param event
     * @return
     */
//    public static String generateLogBody(@NonNull Event<?> event, boolean innnerStack) {
//        StackTraceElement targetElement = Thread.currentThread().getStackTrace()[innnerStack ? 4 : 3];
//        String threadName = Thread.currentThread().getName();
//        String fileName = targetElement.getFileName();
//        String className = targetElement.getClassName();
//        String methodName = targetElement.getMethodName();
//        int lineNumber = targetElement.getLineNumber();
//        String header = String.format(Locale.getDefault(), "%s, %s(%s:%d), %s", threadName, methodName, fileName, lineNumber, className);
//        String eventSrt = null;
//        Object eventBody = event.getBody();
//        if (eventBody != null) {
//            Keep annotation = eventBody.getClass().getAnnotation(Keep.class);
//            if (annotation != null) {
//                //Gson打印
//                eventSrt = new Gson().toJson(eventBody);
//            } else {
//                eventSrt = eventBody.toString();
//            }
//        }
//        return header + "\n" + (EmptyUtils.isEmpty(eventSrt) ? "null" : eventSrt);
//    }

    /**
     * 清理粘性事件
     */
//    public static void removeAllStickyEvents() {
//        EventBus.getDefault().removeAllStickyEvents();
//    }
}