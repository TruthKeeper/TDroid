package com.tk.tdroid.widget.http.progress;

import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/22
 *      desc : {@link RequestBody}的封装
 * </pre>
 */

class ProgressRequestBody extends RequestBody {
    private String url;
    private Handler mHandler;
    private int mIntervalTime;
    private final RequestBody mRequestBody;
    private final WeakReference<ProgressListener>[] mReferences;
    private final ProgressInfo mProgressInfo;
    private BufferedSink mBufferedSink;

    ProgressRequestBody(String url, Handler handler, RequestBody requestBody, WeakReference<ProgressListener>[] references, int intervalTime) {
        this.url = url;
        this.mRequestBody = requestBody;
        this.mReferences = references;
        this.mHandler = handler;
        this.mIntervalTime = intervalTime;
        this.mProgressInfo = new ProgressInfo(System.currentTimeMillis());
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return mRequestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(new CountingSink(sink));
        }
        try {
            mRequestBody.writeTo(mBufferedSink);
            mBufferedSink.flush();
        } catch (IOException e) {
            e.printStackTrace();
            for (WeakReference<ProgressListener> reference : mReferences) {
                if (reference != null && reference.get() != null) {
                    reference.get().onError(url, true, mProgressInfo.getCreateAt(), e);
                }
            }
            throw e;
        }
    }

    final class CountingSink extends ForwardingSink {
        /**
         * 总共写入的字节
         */
        private long totalBytesRead = 0L;
        /**
         * 上次回调的时间
         */
        private long mLastTime = SystemClock.elapsedRealtime();
        private long tempSize = 0L;

        CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            try {
                super.write(source, byteCount);
            } catch (SocketException e) {

            } catch (IOException e) {
                e.printStackTrace();
                for (WeakReference<ProgressListener> reference : mReferences) {
                    if (reference != null && reference.get() != null) {
                        reference.get().onError(url, true, mProgressInfo.getCreateAt(), e);
                    }
                }
                throw e;
            }
            if (mProgressInfo.getContentLength() == 0) {
                //避免重复调用
                mProgressInfo.setContentLength(contentLength());
            }
            totalBytesRead += byteCount;
            tempSize += byteCount;

            long curTime = SystemClock.elapsedRealtime();
            if (curTime - mLastTime >= mIntervalTime || totalBytesRead == mProgressInfo.getContentLength()) {
                //大于等于时间间隔或写入完毕时 回调到主线程
                final long finalTempSize = tempSize;
                final long finalTotalBytesRead = totalBytesRead;
                final long finalIntervalTime = Math.max(curTime - mLastTime, 1);
                for (final WeakReference<ProgressListener> reference : mReferences) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (reference != null && reference.get() != null) {
                                mProgressInfo.setIntervalBytes(finalTempSize);
                                mProgressInfo.setCurrentBytes(finalTotalBytesRead);
                                mProgressInfo.setIntervalTime(finalIntervalTime);
                                mProgressInfo.setFinish(finalTotalBytesRead == mProgressInfo.getContentLength());
                                reference.get().onProgress(url, true, mProgressInfo);
                            }
                        }
                    });
                }
            }
            mLastTime = curTime;
            tempSize = 0;
        }
    }
}