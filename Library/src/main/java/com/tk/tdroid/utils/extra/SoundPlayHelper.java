package com.tk.tdroid.utils.extra;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.tk.tdroid.utils.EmptyUtils;
import com.tk.tdroid.utils.Utils;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.tk.tdroid.utils.extra.SoundPlayHelper.Status.IDLE;
import static com.tk.tdroid.utils.extra.SoundPlayHelper.Status.PAUSE;
import static com.tk.tdroid.utils.extra.SoundPlayHelper.Status.PLAYING;
import static com.tk.tdroid.utils.extra.SoundPlayHelper.Status.PREPARE;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/3
 *      desc : 声音播放工具，播放一个声音
 *             有需要通过{@link AudioFocusHelper}来管理音频焦点竞争
 * </pre>
 */

public final class SoundPlayHelper {
    private static volatile SoundPlayHelper soundPlayHelper = null;

    @IntDef({IDLE, PREPARE, PLAYING, PAUSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
        int IDLE = 0x00;
        int PREPARE = 0x01;
        int PLAYING = 0x02;
        int PAUSE = 0x03;
    }

    private MediaPlayer mMediaPlayer = null;
    /**
     * 播放监听器
     */
    private List<OnPlayListener> mPlayListeners = null;
    /**
     * 播放历史记录
     */
    private LinkedList<Record> mPlayHistory = null;
    /**
     * 播放状态
     */
    @Status
    private int mStatus = Status.IDLE;

    /**
     * 获取单例
     *
     * @return
     */
    public static SoundPlayHelper getInstance() {
        if (null == soundPlayHelper) {
            synchronized (SoundPlayHelper.class) {
                if (null == soundPlayHelper) {
                    soundPlayHelper = new SoundPlayHelper();
                }
            }
        }
        return soundPlayHelper;
    }

    private SoundPlayHelper() {
        AudioManager audioManager = (AudioManager) Utils.getApp().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        //开启扬声器
        audioManager.setSpeakerphoneOn(true);
        mMediaPlayer = new MediaPlayer();
        //声音模式
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mStatus = Status.PLAYING;
                Record record = getLastPlayRecord();
                if (!EmptyUtils.isEmpty(mPlayListeners) && !EmptyUtils.isEmpty(record)) {
                    for (OnPlayListener listener : mPlayListeners) {
                        if (null != listener) {
                            listener.onPlayStart(new Record(record));
                        }
                    }
                }
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mStatus = Status.IDLE;
                Record record = getLastPlayRecord();
                if (!EmptyUtils.isEmpty(mPlayListeners) && !EmptyUtils.isEmpty(record)) {
                    for (OnPlayListener listener : mPlayListeners) {
                        if (null != listener) {
                            listener.onPlayEnd(new Record(record));
                        }
                    }
                }
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mStatus = Status.IDLE;
                Record record = getLastPlayRecord();
                if (!EmptyUtils.isEmpty(mPlayListeners) && !EmptyUtils.isEmpty(record)) {
                    for (OnPlayListener listener : mPlayListeners) {
                        if (null != listener) {
                            listener.onPlayFailure(new Record(record));
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * 播放资源
     *
     * @param url
     */
    public void play(@NonNull String url) {
        play(new Record(url, null));
    }

    /**
     * 播放资源
     *
     * @param record
     */
    public void play(@NonNull Record record) {
        try {
            Record lastRecord = getLastPlayRecord();
            if (lastRecord != null && lastRecord.getUrl().equals(record.getUrl()) && mStatus == Status.PAUSE) {
                //继续播放
                mMediaPlayer.start();
                mStatus = PLAYING;
                return;
            }
            //插入一条记录
            Record newRecord = new Record(record);
            addHistory(newRecord);
            //新的播放
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(record.getUrl());
            mMediaPlayer.prepareAsync();
            mStatus = PREPARE;
        } catch (IOException e) {
            e.printStackTrace();
            mStatus = IDLE;
        }
    }

    /**
     * 停止
     */
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mStatus = Status.PAUSE;
        }
    }

    /**
     * 暂停
     */
    public void stop() {
        mStatus = Status.IDLE;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.reset();
    }

    /**
     * 全局回收资源，不可逆
     */
    @Deprecated
    public void release() {
        mStatus = Status.IDLE;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        mMediaPlayer = null;
        if (!EmptyUtils.isEmpty(mPlayHistory)) {
            mPlayHistory.clear();
        }
        if (!EmptyUtils.isEmpty(mPlayListeners)) {
            mPlayListeners.clear();
        }
    }

    private void addHistory(Record record) {
        if (EmptyUtils.isEmpty(mPlayHistory)) {
            mPlayHistory = new LinkedList<>();
        }
        mPlayHistory.add(record);
    }

    public Record getLastPlayRecord() {
        return EmptyUtils.isEmpty(mPlayHistory) ? null : mPlayHistory.getLast();
    }

    public boolean isPlaying() {
        return mStatus == Status.PLAYING;
    }

    /**
     * 添加一个播放监听
     *
     * @param onPlayListener
     */
    public synchronized void addPlayListener(OnPlayListener onPlayListener) {
        if (onPlayListener == null) {
            return;
        }
        if (EmptyUtils.isEmpty(mPlayListeners)) {
            mPlayListeners = new ArrayList<>();
        }
        mPlayListeners.add(onPlayListener);
    }

    /**
     * 移除一个播放监听
     *
     * @param onPlayListener
     */
    public synchronized void removePlayListener(OnPlayListener onPlayListener) {
        if (onPlayListener == null) {
            return;
        }
        if (!EmptyUtils.isEmpty(mPlayListeners)) {
            mPlayListeners.remove(onPlayListener);
        }
    }

    /**
     * 移除所有播放监听
     */
    public synchronized void removeAllPlayListener() {
        if (!EmptyUtils.isEmpty(mPlayListeners)) {
            mPlayListeners.clear();
        }
    }

    public interface OnPlayListener {
        /**
         * @param record
         */
        void onPlayStart(Record record);

        /**
         * @param record
         */
        void onPlayEnd(Record record);

        /**
         * @param record
         */
        void onPlayFailure(Record record);
    }

    public static class Record {
        private String url;
        private String extra;

        public Record(String url) {
            this.url = url;
        }

        public Record(String url, String extra) {
            this.url = url;
            this.extra = extra;
        }

        public Record(Record record) {
            this.url = record.getUrl();
            this.extra = record.getExtra();
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }
    }
}
