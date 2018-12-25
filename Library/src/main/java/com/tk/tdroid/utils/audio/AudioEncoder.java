package com.tk.tdroid.utils.audio;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;

import com.tk.tdroid.utils.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/9/21
 *     desc   : 音频编码类
 * </pre>
 */
class AudioEncoder {
    /**
     * 类型
     */
    public static final String MIME_TYPE = "audio/mp4a-latm";
    private MediaCodec mMediaCodec;
    private final int mSampleRateType;

    private BufferedOutputStream mOutputStream;
    private ByteBuffer[] mInputBuffers = null;
    private ByteBuffer[] mOutputBuffers = null;

    private static SparseIntArray SAMPLE_RATE_TYPE;

    static {
        SAMPLE_RATE_TYPE = new SparseIntArray(16);
        SAMPLE_RATE_TYPE.put(96000, 0);
        SAMPLE_RATE_TYPE.put(88200, 1);
        SAMPLE_RATE_TYPE.put(64000, 2);
        SAMPLE_RATE_TYPE.put(48000, 3);
        SAMPLE_RATE_TYPE.put(44100, 4);
        SAMPLE_RATE_TYPE.put(32000, 5);
        SAMPLE_RATE_TYPE.put(24000, 6);
        SAMPLE_RATE_TYPE.put(22050, 7);
        SAMPLE_RATE_TYPE.put(16000, 8);
        SAMPLE_RATE_TYPE.put(12000, 9);
        SAMPLE_RATE_TYPE.put(11025, 10);
        SAMPLE_RATE_TYPE.put(8000, 11);
        SAMPLE_RATE_TYPE.put(7350, 12);
    }

    AudioEncoder() {
        this.mSampleRateType = getSampleRateType(AudioRecordHelper.SAMPLE_RATE);
    }

    /**
     * 初始化
     *
     * @param outputFile
     */
    void prepare(@NonNull File outputFile) {
        close();

        try {
            mMediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
            MediaFormat mediaFormat = MediaFormat.createAudioFormat(
                    MIME_TYPE,
                    AudioRecordHelper.SAMPLE_RATE,
                    2);
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE,
                    //比特率，决定录音文件的大小
                    32 * 1024);
            mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE,
                    MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE,
                    10 * 1024);

            mMediaCodec.configure(mediaFormat, null, null,
                    MediaCodec.CONFIGURE_FLAG_ENCODE);

            mMediaCodec.start();

            mInputBuffers = mMediaCodec.getInputBuffers();
            mOutputBuffers = mMediaCodec.getOutputBuffers();
            mOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭
     */
    public void close() {
        if (mMediaCodec == null) {
            return;
        }
        try {
            mMediaCodec.stop();
            mOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mMediaCodec.release();
            mMediaCodec = null;
            IOUtils.closeQuietly(mOutputStream);
        }
    }

    /**
     * 实时编码
     *
     * @param input
     */
    public synchronized void encode(byte[] input) {
        if (mMediaCodec == null) {
            return;
        }
        int inputBufferIndex = mMediaCodec.dequeueInputBuffer(-1);
        if (inputBufferIndex > 0) {
            ByteBuffer inputBuffer = mInputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(input);
            mMediaCodec.queueInputBuffer(inputBufferIndex,
                    0,
                    input.length,
                    System.nanoTime(),
                    0);
        }

        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 0);
        while (outputBufferIndex > 0) {
            int outBitsSize = bufferInfo.size;
            int outPacketSize = outBitsSize + 7;
            ByteBuffer outputBuffer = mOutputBuffers[outputBufferIndex];
            outputBuffer.position(bufferInfo.offset);
            outputBuffer.limit(bufferInfo.offset + outBitsSize);
            byte[] outData = new byte[outPacketSize];
            addADTStoPacket(mSampleRateType, outData, outPacketSize);
            outputBuffer.get(outData, 7, outBitsSize);
            outputBuffer.position(bufferInfo.offset);
            try {
                mOutputStream.write(outData, 0, outData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);
            outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 0);
        }
    }

    private static int getSampleRateType(int sampleRate) {
        return SAMPLE_RATE_TYPE.get(sampleRate);
    }

    /**
     * 添加ADTS头
     *
     * @param packet
     * @param packetLen
     */
    private static void addADTStoPacket(int sampleRateType, byte[] packet, int packetLen) {
        int profile = 2; // AAC LC
        int freqIdx = sampleRateType; // 44.1KHz
        int chanCfg = 2; // CPE
        // fill in ADTS data
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }
}
