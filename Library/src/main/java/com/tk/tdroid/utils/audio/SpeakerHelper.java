package com.tk.tdroid.utils.audio;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.PowerManager;

import com.tk.tdroid.utils.Utils;

/**
 * <pre>
 *      author : TK
 *      time : 2018/3/16
 *      desc : 扬声器帮助工具，支持屏幕自动息屏、亮屏
 * </pre>
 */

public final class SpeakerHelper {
    private final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] dis = event.values;
            if (0.0f == dis[0]) {
                // 靠近身体，自动切换成听筒
                setSpeakerOn(false);
                if (mWakeLock != null) {
                    //息屏
                    mWakeLock.acquire();
                }
            } else {
                setSpeakerOn(true);
                if (mWakeLock != null) {
                    //亮屏
                    mWakeLock.release();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private   PowerManager.WakeLock mWakeLock;

    private SpeakerHelper(boolean wakeLockEnabled) {
        if (wakeLockEnabled) {
            PowerManager manager = (PowerManager) Utils.getApp().getSystemService(Context.POWER_SERVICE);
            //PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK
            mWakeLock = manager.newWakeLock(32, getClass().getName());
            mWakeLock.setReferenceCounted(false);
        }
    }

    /**
     * @return
     */
    public static SpeakerHelper create() {
        return new SpeakerHelper(false);
    }

    /**
     * @param wakeLockEnabled 是否支持屏幕自动息屏、亮屏
     * @return
     */
    public static SpeakerHelper create(boolean wakeLockEnabled) {
        return new SpeakerHelper(wakeLockEnabled);
    }

    /**
     * <a href="http://blog.csdn.net/google_acmer/article/details/54141229">扬声器和听筒之间切换</a><br>
     * <a href="http://blog.csdn.net/wl332197858/article/details/51298868">扬声器和听筒之间切换</a>
     *
     * @param speakerOn
     */
    public static void setSpeakerOn(boolean speakerOn) {
        AudioManager audioManager = (AudioManager) Utils.getApp().getSystemService(Context.AUDIO_SERVICE);
        if (speakerOn) {
            audioManager.setSpeakerphoneOn(true);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            //设置音量，解决有些机型切换后没声音或者声音突然变大的问题
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
                    AudioManager.FX_KEY_CLICK);
        } else {
            audioManager.setSpeakerphoneOn(false);
            //5.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                //设置音量，解决有些机型切换后没声音或者声音突然变大的问题
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.FX_KEY_CLICK);
            } else {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.FX_KEY_CLICK);
            }
        }
    }

    /**
     * 开始监听
     */
    public void startMonitor() {
        SensorManager sensorManager = (SensorManager) Utils.getApp().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(mListener, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 停止监听
     */
    public void stopMonitor() {
        SensorManager sensorManager = (SensorManager) Utils.getApp().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(mListener, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }
}
