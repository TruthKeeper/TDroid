package com.tk.tdroid.utils.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tk.tdroid.utils.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/9/18
 *     desc   :
 * </pre>
 */
public class AudioRecordHelper {
    /**
     * 默认采样率
     */
    public static final int SAMPLE_RATE = 44100;
    /**
     * 录音文件格式
     */
    public static final String AAC_SUFFIX = ".aac";
    /**
     * 监听音量的回调间隔
     */
    private static final int VOLUME_INTERVAL = 250;
    /**
     * 缓冲区大小
     */
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT);
    /**
     * 音量监听器
     */
    private final OnVolumeListener mOnVolumeListener;
    /**
     * 代理器
     */
    private SourceDataDelegate sourceDataDelegate;
    /**
     * 录音器
     */
    private AudioRecord mRecorder;
    /**
     * 编码器
     */
    private AudioEncoder mAudioEncoder;
    /**
     * 录音存放目录
     */
    private File mDir = null;
    /**
     * 输出的AAC文件
     */
    private File mAACOutputFile = null;
    /**
     * 录音的线程
     */
    private Thread mRecordThread;
    /**
     * 是否在录音中
     */
    private boolean mIsRecording = false;
    /**
     * 是否暂停录音
     */
    private boolean mIsPause = false;
    /**
     * 记录开始录音的时间
     */
    private long mStartTime;
    /**
     * 记录暂停时间
     */
    private long mPauseStartTime;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            if (mOnVolumeListener != null) {
                mOnVolumeListener.onVolumeChange(msg.what);
            }
        }
    };

    /**
     * @param dir 录音文件存储目录
     * @return
     */
    public static AudioRecordHelper create(@NonNull File dir) {
        return new AudioRecordHelper(dir, null);
    }

    /**
     * @param dir              录音文件存储目录
     * @param onVolumeListener 音量监听器
     * @return
     */
    public static AudioRecordHelper create(@NonNull File dir, @Nullable OnVolumeListener onVolumeListener) {
        return new AudioRecordHelper(dir, onVolumeListener);
    }

    private AudioRecordHelper(File dir, OnVolumeListener onVolumeListener) {
        this.mDir = dir == null ? Environment.getExternalStorageDirectory() : dir;
        this.mOnVolumeListener = onVolumeListener;
        mAudioEncoder = new AudioEncoder();
        initAudioRecorder();
    }

    /**
     * 初始化录音器
     */
    private void initAudioRecorder() {
        if (mRecorder == null) {
            //消除回声
            mRecorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    BUFFER_SIZE * 2);
        }
    }

    private void generateOutputFile() {
        final String outputFileName = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(System.currentTimeMillis()) + AAC_SUFFIX;
        if (mDir.exists()) {
            if (mDir.isFile()) {
                mDir.delete();
                mDir.mkdirs();
            }
        } else {
            mDir.mkdirs();
        }
        mAACOutputFile = new File(mDir, outputFileName);
        try {
            if (!mAACOutputFile.exists()) {
                mAACOutputFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始录音 or 继续录音
     */
    public void startRecord() {
        if (mIsPause) {
            //继续录制
            mIsPause = false;
            mStartTime -= (System.currentTimeMillis() - mPauseStartTime);
            return;
        }

        //停止之前的录音
        cancelRecord();
        initAudioRecorder();
        //记录开始时间
        mStartTime = System.currentTimeMillis();
        mIsRecording = true;
        //生成输出文件
        generateOutputFile();
        mAudioEncoder.prepare(mAACOutputFile);
        //初始化
        mRecorder.startRecording();
        //开启线程
        mRecordThread = new Thread(recordRunnable);
        mRecordThread.start();
    }

    /**
     * 是否在录音中
     *
     * @return
     */
    public boolean isRecording() {
        return mIsRecording;
    }

    /**
     * 是否录音暂停了
     *
     * @return
     */
    public boolean isPause() {
        return mIsPause;
    }

    /**
     * 暂停录音
     */
    public void pauseRecord() {
        if (!mIsRecording) {
            return;
        }
        mIsPause = true;
        mPauseStartTime = System.currentTimeMillis();
    }

    /**
     * 取消录音
     */
    public void cancelRecord() {
        if (!mIsRecording || mRecorder == null) {
            return;
        }
        mIsPause = false;
        mIsRecording = false;
        if (mRecorder.getState() == AudioRecord.RECORDSTATE_RECORDING) {
            mRecorder.stop();
        }
        mRecorder.release();
        mRecorder = null;
        try {
            mRecordThread.join();
            mRecordThread.interrupt();
            mHandler.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mAudioEncoder.close();
        }
        FileUtils.deleteFile(mAACOutputFile);
    }

    /**
     * 停止录音
     *
     * @return 录音文件的绝对路径
     */
    public AudioRecordResult finishRecord() {
        mIsPause = false;
        if (!mIsRecording || mRecorder == null) {
            return null;
        }
        mIsRecording = false;
        if (mRecorder.getState() == AudioRecord.RECORDSTATE_RECORDING) {
            mRecorder.stop();
        }
        mRecorder.release();
        mRecorder = null;
        try {
            mRecordThread.join();
            mRecordThread.interrupt();
            mHandler.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mAudioEncoder.close();
        }
        return mAACOutputFile == null ? null
                : new AudioRecordResult(mAACOutputFile, System.currentTimeMillis() - mStartTime);
    }

    /**
     * 录音线程
     */
    private final Runnable recordRunnable = new Runnable() {
        private long lastTime;

        @Override
        public void run() {
            try {
                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                int bytesRecord;
                byte[] buffer = new byte[BUFFER_SIZE];

                while (mIsRecording) {
                    bytesRecord = mRecorder.read(buffer, 0, BUFFER_SIZE);
                    if (bytesRecord <= 0 || mIsPause) {
                        continue;
                    }
                    long time = System.currentTimeMillis();
                    if (time - lastTime >= VOLUME_INTERVAL) {
                        lastTime = time;
                        mHandler.sendEmptyMessage(calculateVolumePercent(buffer));
                    }
                    //降噪
//                    for (int i = 0; i < buffer.length; i++) {
//                        buffer[i] >>= 1;
//                    }

                    mAudioEncoder.encode(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private int calculateVolumePercent(byte[] buffer) {
        double sumVolume = 0.0;
        double avgVolume = 0.0;
        for (int i = 0; i < buffer.length; i += 2) {
            int v1 = buffer[i] & 0xFF;
            int v2 = buffer[i + 1] & 0xFF;
            int temp = v1 + (v2 << 8);// 小端
            if (temp >= 0x8000) {
                temp = 0xffff - temp;
            }
            sumVolume += Math.abs(temp);
        }
        avgVolume = sumVolume / buffer.length / 2;
        int maxValue = 1000;
        return (int) Math.min(avgVolume * 100D / maxValue, 100);
    }

    /**
     * 设置代理
     *
     * @param sourceDataDelegate
     */
    public void setSourceDataDelegate(SourceDataDelegate sourceDataDelegate) {
        this.sourceDataDelegate = sourceDataDelegate;
    }

    /**
     * 音量监听器
     */
    public interface OnVolumeListener {
        /**
         * 音量的改变
         *
         * @param percent
         */
        void onVolumeChange(@IntRange(from = 0, to = 100) int percent);
    }

    /**
     * 录制的源数据代理
     */
    public interface SourceDataDelegate {
        /**
         * 录制过程中的回调
         *
         * @param data
         */
        void onRecord(byte[] data);
    }

    /**
     * 返回结果实体
     */
    public static class AudioRecordResult {
        public final File soundFile;
        public final long soundLength;

        AudioRecordResult(File soundFile, long soundLength) {
            this.soundFile = soundFile;
            this.soundLength = soundLength;
        }

        @Override
        public String toString() {
            return "AudioRecordResult{" +
                    "soundFile=" + soundFile.getAbsolutePath() +
                    ", soundLength=" + soundLength +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AudioRecordResult that = (AudioRecordResult) o;

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
}
