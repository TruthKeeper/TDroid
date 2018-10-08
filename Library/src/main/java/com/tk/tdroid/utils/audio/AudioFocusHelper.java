package com.tk.tdroid.utils.audio;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;

import com.tk.tdroid.utils.Utils;

/**
 * <pre>
 *      author : TK
 *      time : 2017/9/30
 *      desc : 音频焦点辅助类
 * </pre>
 */

public class AudioFocusHelper {
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mListener;

    /**
     * 创建
     *
     * @param audioListener
     * @return
     */
    public static AudioFocusHelper create(@NonNull AudioListener audioListener) {
        return new AudioFocusHelper(audioListener);
    }

    private AudioFocusHelper(@NonNull final AudioListener audioListener) {
        mAudioManager = (AudioManager) Utils.getApp().getSystemService(Context.AUDIO_SERVICE);
        mListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN://长期占有
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT://短期占有
//                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                        //重新获取到焦点
                        audioListener.recoverFocus();
                        break;

                    case AudioManager.AUDIOFOCUS_LOSS:
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        //暂停操作
                        audioListener.lossFocus();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 请求音频焦点
     *
     * @return 是否获取到
     */
    public boolean requestAudioFocus() {
        return AudioManager.AUDIOFOCUS_GAIN == mAudioManager.requestAudioFocus(mListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

    /**
     * 暂停、播放完成或退到后台释放音频焦点
     */
    public void releaseAudioFocus() {
        mAudioManager.abandonAudioFocus(mListener);
    }

    public interface AudioListener {
        /**
         * 重新获取到焦点
         */
        void recoverFocus();

        /**
         * 失去焦点，被其他音频占用
         */
        void lossFocus();
    }
}
