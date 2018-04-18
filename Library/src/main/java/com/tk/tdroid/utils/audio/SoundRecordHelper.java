package com.tk.tdroid.utils.audio;

import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.tk.tdroid.rx.AsyncCall;
import com.tk.tdroid.utils.EmptyUtils;
import com.tk.tdroid.utils.FileUtils;
import com.tk.tdroid.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/2
 *      desc : 录制声音管理类
 * </pre>
 */

public final class SoundRecordHelper {
    /**
     * 声音输出文件格式
     */
    private static final String EXTENSION = ".aac";
    /**
     * 音量监听回调时间间隔，单位：毫秒
     */
    private static final int VOLUME_DELAY = 150;
    /**
     * 计时监听回调时间间隔，单位：毫秒
     */
    private static final int TIMER_DELAY = 100;
    /**
     * 录音器
     */
    private MediaRecorder recorder = null;
    /**
     * 是否录音中
     */
    private boolean isRecording = false;
    /**
     * 录音存放路径
     */
    private File dir = null;
    /**
     * 录音文件
     */
    private File soundFile = null;

    private OnVolumeListener onVolumeListener;
    private OnTimerListener onTimerListener;
    private Disposable volumeDisposable;
    private Disposable timerDisposable;

    private SoundRecordHelper(Builder builder) {
        dir = builder.dir;
        onVolumeListener = builder.onVolumeListener;
        onTimerListener = builder.onTimerListener;

        //打开扬声器
        SpeakerHelper.setSpeakerOn(true);

        recorder = new MediaRecorder();
        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        recorder.setAudioChannels(camcorderProfile.audioChannels);
        recorder.setAudioSamplingRate(camcorderProfile.audioSampleRate);
        recorder.setAudioEncodingBitRate(camcorderProfile.audioBitRate);
    }

    /**
     * 开始监听音量
     */
    private void startVolumeSubscribe() {
        if (volumeDisposable != null && !volumeDisposable.isDisposed()) {
            //已经在监听了
            return;
        }
        Observable.interval(0, VOLUME_DELAY, TimeUnit.MILLISECONDS)
                .compose(new AsyncCall<Long>())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        volumeDisposable = disposable;
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (isRecording && onVolumeListener != null) {
//                            int maxDb = (int) (20 * Math.log(0x7FFF / 400));
                            int maxDb = (int) (Math.log(0x6FFF / 400));
                            int db = (int) (Math.log(recorder.getMaxAmplitude() / 400));
                            onVolumeListener.onVolumeChange(Math.min(db * 100 / maxDb, 100));
                        }
                    }
                });
    }

    /**
     * 开始录音计时
     */
    private void startTimerSubscribe() {
        if (timerDisposable != null && !timerDisposable.isDisposed()) {
            timerDisposable.dispose();
        }
        Observable.interval(0, TIMER_DELAY, TimeUnit.MILLISECONDS)
                .compose(new AsyncCall<Long>())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        timerDisposable = disposable;
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (isRecording && onTimerListener != null) {
                            onTimerListener.onTimer(aLong);
                        }
                    }
                });
    }

    /**
     * 生成录音输出文件
     */
    private void generateOutput() {
        final String voiceFileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(System.currentTimeMillis()) + EXTENSION;
        //校验目录
        if (dir == null) {
            //为空则默认外部存储 or 内部缓存
            dir = new File(StorageUtils.getCachePath());
        }
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
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(soundFile.getAbsolutePath());
    }

    /**
     * 开始录音
     *
     * @return 成功开始 or not
     */
    public boolean startRecord() {
        //释放上次资源
        recorder.reset();
        generateOutput();
        try {
            recorder.prepare();
            recorder.start();

            isRecording = true;
            startTimerSubscribe();
            startVolumeSubscribe();
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
        if (timerDisposable != null && !timerDisposable.isDisposed()) {
            timerDisposable.dispose();
        }
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
        if (timerDisposable != null && !timerDisposable.isDisposed()) {
            timerDisposable.dispose();
        }
        if (soundFile.exists() && soundFile.isFile()) {
            return new SoundRecordResult(soundFile, getMediaDuring(soundFile.getAbsolutePath()));
        } else {
            return null;
        }
    }

    /**
     * 回收资源
     */
    public void release() {
        if (timerDisposable != null && !timerDisposable.isDisposed()) {
            timerDisposable.dispose();
        }
        if (volumeDisposable != null && !volumeDisposable.isDisposed()) {
            volumeDisposable.dispose();
        }
        if (null != recorder) {
            recorder.release();
            recorder = null;
        }
    }

    /**
     * @return 是否录音中
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * 获取音频文件的时长
     *
     * @param path
     * @return 单位：毫秒
     */
    public static long getMediaDuring(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            return Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        } catch (Exception e) {
        } finally {
            retriever.release();
        }
        return -1;
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

    /**
     * 音量监听器
     */
    public interface OnVolumeListener {
        /**
         * 音量变化的回调，间隔{@link SoundRecordHelper#VOLUME_DELAY 毫秒}回调
         *
         * @param percent
         */
        @MainThread
        void onVolumeChange(@IntRange(from = 0, to = 100) int percent);
    }

    /**
     * 定时器监听器
     */
    public interface OnTimerListener {
        /**
         * 开始计时后的计时器回调，间隔{@link SoundRecordHelper#TIMER_DELAY 毫秒}回调
         *
         * @param millisecond
         */
        @MainThread
        void onTimer(long millisecond);
    }

    public static final class Builder {
        private File dir;
        private OnVolumeListener onVolumeListener;
        private OnTimerListener onTimerListener;

        public Builder() {
        }

        /**
         * @param dirPath 录音文件存储目录
         * @return
         */
        public Builder dir(@Nullable String dirPath) {
            this.dir = EmptyUtils.isEmpty(dirPath) ? null : new File(dirPath);
            return this;
        }

        /**
         * @param dir 录音文件存储目录
         * @return
         */
        public Builder dir(@Nullable File dir) {
            this.dir = dir;
            return this;
        }

        /**
         * @param onVolumeListener 音量监听器
         * @return
         */
        public Builder onVolumeListener(@Nullable OnVolumeListener onVolumeListener) {
            this.onVolumeListener = onVolumeListener;
            return this;
        }

        /**
         * @param onTimerListener 计时监听器
         * @return
         */
        public Builder onTimerListener(@Nullable OnTimerListener onTimerListener) {
            this.onTimerListener = onTimerListener;
            return this;
        }

        public SoundRecordHelper build() {
            return new SoundRecordHelper(this);
        }
    }
}
