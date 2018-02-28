package com.tk.tdroid.bridge;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.tk.tdroid.utils.EmptyUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/02/28
 *     desc   :  <ul>事件通信管理器，{@link String}标志对应多数个接收器
 *         <li>支持组件化跨模块场景下调用</li>
 *         <li>支持粘性事件</li>
 *     </ul>
 *
 * </pre>
 */
public final class EventManager {
    private static final String TAG = "EventManager";

    private static volatile EventManager mEventManager = null;
    private final Map<String, List<IReceiver>> mReceiverMap = new ArrayMap<>();
    private final Map<String, List<IReceiver>> mStickyReceiverMap = new ArrayMap<>();

    private final Map<String, Object> mStickyEventMap = new ArrayMap<>();

    private EventManager() {
    }

    public static EventManager getInstance() {
        if (mEventManager == null) {
            synchronized (EventManager.class) {
                if (mEventManager == null) {
                    mEventManager = new EventManager();
                }
            }
        }
        return mEventManager;
    }

    /**
     * 添加一个事件接收器
     *
     * @param eventTag
     * @param receiver
     * @return
     */
    public <Event> void addReceiver(@NonNull String eventTag, @NonNull IReceiver<Event> receiver) {
        putInMap(eventTag, receiver, mReceiverMap);
    }

    /**
     * 添加一个粘性事件接收器
     *
     * @param eventTag
     * @param receiver
     * @return
     */
    public <Event> void addStickyReceiver(@NonNull String eventTag, @NonNull IReceiver<Event> receiver) {
        putInMap(eventTag, receiver, mStickyReceiverMap);
        //是否有粘性事件立即通知到广播
        if (mStickyEventMap.containsKey(eventTag)) {
            try {
                receiver.call((Event) mStickyEventMap.get(eventTag));
            } catch (ClassCastException e) {
                Log.e(TAG, "Event in Receiver cannot be casted , please check , EventTag : " + eventTag);
                e.printStackTrace();
            } catch (Exception e) {
                Log.e(TAG, "Call Exception , EventTag : " + eventTag);
                e.printStackTrace();
            }
        }
    }

    /**
     * 移除一个事件接收器
     *
     * @param eventTag
     * @param receiver
     * @return
     */
    public <Event> void removeReceiver(@NonNull String eventTag, @NonNull IReceiver<Event> receiver) {
        List<IReceiver> receiverList = mReceiverMap.get(eventTag);
        if (receiverList != null) {
            receiverList.remove(receiver);
        }
    }

    /**
     * 移除标志下的所有事件接收器
     *
     * @param eventTag
     * @return
     */
    public void removeAllReceiver(@NonNull String eventTag) {
        mReceiverMap.remove(eventTag);
    }

    /**
     * 移除一个粘性事件接收器
     *
     * @param eventTag
     * @param receiver
     * @return
     */
    public <Event> void removeStickyReceiver(@NonNull String eventTag, @NonNull IReceiver<Event> receiver) {
        List<IReceiver> receiverList = mStickyReceiverMap.get(eventTag);
        if (receiverList != null) {
            receiverList.remove(receiver);
        }
    }

    /**
     * 移除标志下的所有粘性事件接收器
     *
     * @param eventTag
     * @return
     */
    public void removeAllStickyReceiver(@NonNull String eventTag) {
        mStickyReceiverMap.remove(eventTag);
    }

    /**
     * 获取粘性事件
     *
     * @return
     */
    @Nullable
    public void getStickyEvent(@NonNull String eventTag) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.get(eventTag);
        }
    }

    /**
     * 移除粘性事件
     *
     * @param eventTag
     * @return
     */
    public void removeStickyEvent(@NonNull String eventTag) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.remove(eventTag);
        }

    }

    /**
     * 移除所有粘性事件
     *
     * @return
     */
    public void removeAllStickyEvent() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }

    /**
     * 发送一个事件
     *
     * @param eventTag
     * @param param
     * @param <Param>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <Event> void post(@NonNull String eventTag, @Nullable Event event) {
        if (EmptyUtils.isEmpty(eventTag)) {
            return;
        }
        List<IReceiver> receiverList = mReceiverMap.get(eventTag);
        if (receiverList != null) {
            try {
                for (IReceiver<Event> receiver : receiverList) {
                    if (receiver != null) {
                        receiver.call(event);
                    }
                }
            } catch (ClassCastException e) {
                Log.e(TAG, "Event in Receiver cannot be casted , please check , EventTag : " + eventTag);
                e.printStackTrace();
            } catch (Exception e) {
                Log.e(TAG, "Call Exception , EventTag : " + eventTag);
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送一个粘性事件
     *
     * @param eventTag
     * @param event
     * @param <Event>
     */
    public <Event> void postSticky(@NonNull String eventTag, @Nullable Event event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(eventTag, event);
        }
        post(eventTag, event);
    }

    private static void putInMap(String eventTag, IReceiver receiver, Map<String, List<IReceiver>> map) {
        List<IReceiver> receiverList = map.get(eventTag);
        if (receiverList == null) {
            receiverList = new LinkedList<>();
            map.put(eventTag, receiverList);
        }
        receiverList.add(receiver);
    }

}