package com.tk.tdroid.utils;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/9/26
 *     desc   : 来电去电状态帮助器
 * </pre>
 */
public class PhoneStateHelper {
    private PhoneStateHelper() {
        throw new IllegalStateException();
    }

    /**
     * 注册来电广播，需要{@link Manifest.permission#READ_PHONE_STATE} 和 {@link Manifest.permission#PROCESS_OUTGOING_CALLS}
     *
     * @param context
     * @param receiver
     */
    public static void register(@NonNull Context context, @NonNull AbsPhoneStateChangedReceiver receiver) {
        IntentFilter callIntentFilter = new IntentFilter();
        callIntentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        callIntentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        context.registerReceiver(receiver, callIntentFilter);
    }

    /**
     * 注销来电广播
     *
     * @param context
     * @param receiver
     */
    public static void unregister(@NonNull Context context, @NonNull AbsPhoneStateChangedReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    public static abstract class AbsPhoneStateChangedReceiver extends BroadcastReceiver {
        private int lastState = TelephonyManager.CALL_STATE_IDLE;
        private long callStartTime;
        private boolean isIncoming;
        private String savedNumber;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())) {
                savedNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            } else if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction())) {
                String stateStr = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                int state = TelephonyManager.CALL_STATE_IDLE;
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    state = TelephonyManager.CALL_STATE_IDLE;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    state = TelephonyManager.CALL_STATE_RINGING;
                }
                dispatchCallStateChanged(context, state, number);
            }
        }

        private void dispatchCallStateChanged(Context context, int state, String number) {
            if (lastState == state) {
                return;
            }
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    isIncoming = true;
                    callStartTime = System.currentTimeMillis();
                    savedNumber = number;
                    onIncomingCallReceived(context, number, callStartTime);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    callStartTime = System.currentTimeMillis();
                    if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                        isIncoming = false;
                        onOutgoingCallStarted(context, savedNumber, callStartTime);
                    } else {
                        isIncoming = true;
                        onIncomingCallAnswered(context, savedNumber, callStartTime);
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                        onIncomingCallCanceled(context, savedNumber, callStartTime);
                    } else if (isIncoming) {
                        onIncomingCallEnded(context, savedNumber, callStartTime, System.currentTimeMillis());
                    } else {
                        onOutgoingCallEnded(context, savedNumber, callStartTime, System.currentTimeMillis());
                    }
                    break;
            }
            lastState = state;
        }

        /**
         * 接到了来电
         *
         * @param context
         * @param number        来电号码
         * @param callStartTime 来电的时间
         */
        public abstract void onIncomingCallReceived(Context context, String number, long callStartTime);

        /**
         * 接听了来电
         *
         * @param context
         * @param number        来电号码
         * @param callStartTime 接听的时间
         */
        public abstract void onIncomingCallAnswered(Context context, String number, long callStartTime);

        /**
         * 本次来电通话被结束了
         *
         * @param ctx
         * @param number        来电号码
         * @param callStartTime 接听的时间
         * @param callEndTime   结束的时间
         */
        public abstract void onIncomingCallEnded(Context ctx, String number, long callStartTime, long callEndTime);

        /**
         * 来电被取消了
         *
         * @param ctx
         * @param number        对方号码
         * @param callStartTime 来电的时间
         */
        public abstract void onIncomingCallCanceled(Context ctx, String number, long callStartTime);

        /**
         * 发起了通话
         *
         * @param ctx
         * @param number        对方号码
         * @param callStartTime 呼叫的时间
         */
        public abstract void onOutgoingCallStarted(Context ctx, String number, long callStartTime);

        /**
         * 发起的通话被结束
         *
         * @param ctx
         * @param number        对方号码
         * @param callStartTime 呼叫的时间
         * @param callEndTime   结束的时间
         */
        public abstract void onOutgoingCallEnded(Context ctx, String number, long callStartTime, long callEndTime);
    }
}
