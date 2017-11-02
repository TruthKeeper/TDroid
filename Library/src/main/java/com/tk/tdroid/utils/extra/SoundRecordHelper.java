package com.tk.tdroid.utils.extra;

import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tk.tdroid.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/2
 *      desc : 录制声音管理类
 * </pre>
 */

public final class SoundRecordHelper {
    private static final String EXTENSION = ".aac";
    private static final int SLEEP = 200;
    private boolean life;
    /**
     * 录音器
     */
    private MediaRecorder recorder = null;
    /**
     * 是否录音中
     */
    private boolean isRecording = false;
    /**
     * 记录开始录音的时间
     */
    private long startTime;
    /**
     * 录音存放目录
     */
    private File dir = null;
    /**
     * 录音文件
     */
    private File soundFile = null;
    /**
     * 音量监听器
     */
    private OnVolumeListener onVolumeListener;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (onVolumeListener != null) {
                onVolumeListener.onVolumeChange(msg.what);
            }
        }
    };

    /**
     * @param dirPath 录音文件存储路径
     * @return
     */
    public static SoundRecordHelper create(@NonNull String dirPath) {
        return new SoundRecordHelper(new File(dirPath), null);
    }

    /**
     * @param dir 录音文件存储目录
     * @return
     */
    public static SoundRecordHelper create(@NonNull File dir) {
        return new SoundRecordHelper(dir, null);
    }

    /**
     * @param dirPath          录音文件存储路径
     * @param onVolumeListener 音量监听器
     * @return
     */
    public static SoundRecordHelper create(@NonNull String dirPath, @Nullable OnVolumeListener onVolumeListener) {
        return new SoundRecordHelper(new File(dirPath), onVolumeListener);
    }

    /**
     * @param dir              录音文件存储目录
     * @param onVolumeListener 音量监听器
     * @return
     */
    public static SoundRecordHelper create(@NonNull File dir, @Nullable OnVolumeListener onVolumeListener) {
        return new SoundRecordHelper(dir, onVolumeListener);
    }

    private SoundRecordHelper(File dir, OnVolumeListener onVolumeListener) {
        life = true;
        this.dir = dir;
        this.onVolumeListener = onVolumeListener;

        recorder = new MediaRecorder();
        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        recorder.setAudioChannels(camcorderProfile.audioChannels);
        recorder.setAudioSamplingRate(camcorderProfile.audioSampleRate);
        recorder.setAudioEncodingBitRate(camcorderProfile.audioBitRate);
        generateFile();
        if (onVolumeListener != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (life) {
                            handler.sendEmptyMessage(recorder.getMaxAmplitude() * 100 / 0x7FFF);
                            Thread.sleep(SLEEP);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    private void generateFile() {
        //输出的文件名
        final String voiceFileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .format(System.currentTimeMillis()) + EXTENSION;
        //校验目录
        if (dir.exists()) {
            if (dir.isFile()) {
                dir.delete();
                dir.mkdirs();
            }
        } else {
            dir.mkdirs();
        }

        soundFile = new File(dir, voiceFileName);
        if (soundFile.exists()) {
            soundFile.delete();
        }
        //兼容iOS的编码
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(soundFile.getAbsolutePath());
    }

    /**
     * 开始录音
     *
     * @return 成功开始
     */
    public boolean startRecord() {
        //释放上次资源
        recorder.reset();
        generateFile();
        try {
            recorder.prepare();
            recorder.start();
            isRecording = true;
            startTime = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 取消录音并删除
     */
    public void cancelRecord() {
        if (!isRecording) {
            return;
        }
        isRecording = false;
        recorder.reset();
        FileUtils.deleteFile(soundFile);
    }

    /**
     * 结束录音
     *
     * @return 录音结果
     */
    @Nullable
    public SoundRecordResult finishRecord() {
        if (!isRecording) {
            return null;
        }
        isRecording = false;
        recorder.reset();
        long soundLength = System.currentTimeMillis() - startTime;

        if (soundFile.exists() && soundFile.isFile()) {
            return new SoundRecordResult(soundFile, soundLength);
        } else {
            return null;
        }
    }

    /**
     * 回收资源
     */
    public void release() {
        life = false;
        handler.removeCallbacksAndMessages(null);
        if (null != recorder) {
            recorder.release();
            recorder = null;
        }
    }

    /**
     * 是否录音中
     *
     * @return
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * 返回结果实体
     */
    public static class SoundRecordResult {
        private File soundFile;
        private long soundLength;

        public SoundRecordResult(File soundFile, long soundLength) {
            this.soundFile = soundFile;
            this.soundLength = soundLength;
        }

        public File getSoundFile() {
            return soundFile;
        }

        public long getSoundLength() {
            return soundLength;
        }

        @Override
        public String toString() {
            return "SoundRecordResult{" +
                    "soundFile=" + soundFile.getAbsolutePath() +
                    ", soundLength=" + soundLength +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SoundRecordResult that = (SoundRecordResult) o;

            if (soundLength != that.soundLength) return false;
            return soundFile != null ? soundFile.equals(that.soundFile) : that.soundFile == null;

        }

        @Override
        public int hashCode() {
            int result = soundFile != null ? soundFile.hashCode() : 0;
            result = 31 * result + (int) (soundLength ^ (soundLength >>> 32));
            return result;
        }
    }

    public interface OnVolumeListener {
        void onVolumeChange(@IntRange(from = 0, to = 100) int percent);
    }
}
