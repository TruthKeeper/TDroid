package com.tk.tdroid.http.progress;

import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/22
 *      desc : {@link ResponseBody}的封装
 * </pre>
 */

class ProgressResponseBody extends ResponseBody {
    private String httpUrlOrHeader;
    private Handler mHandler;
    private int mIntervalTime;
    private final ResponseBody mResponseBody;
    private final WeakReference<ProgressListener>[] mReferences;
    private final ProgressInfo mProgressInfo;
    private BufferedSource mBufferedSource;

    ProgressResponseBody(String httpUrlOrHeader, Handler handler, ResponseBody responseBody, WeakReference<ProgressListener>[] references, int intervalTime) {
        this.httpUrlOrHeader = httpUrlOrHeader;
        this.mResponseBody = responseBody;
        this.mReferences = references;
        this.mHandler = handler;
        this.mIntervalTime = intervalTime;
        this.mProgressInfo = new ProgressInfo(System.currentTimeMillis());
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            /**
             * 总共读入的字节
             */
            private long totalBytesRead = 0L;
            /**
             * 上次回调的时间
             */
            private long mLastTime = SystemClock.elapsedRealtime();
            private long tempSize = 0L;

            private boolean isFinish;

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = 0L;

                try {
                    bytesRead = super.read(sink, byteCount);
                } catch (SocketException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                    for (WeakReference<ProgressListener> reference : mReferences) {
                        if (reference != null && reference.get() != null) {
                            reference.get().onError(httpUrlOrHeader, false, mProgressInfo.getCreateAt(), e);
                        }
                    }
                    throw e;
                }

                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                tempSize += bytesRead != -1 ? bytesRead : 0;
                if (mProgressInfo.getContentLength() == 0) {
                    //避免重复调用
                    mProgressInfo.setContentLength(contentLength());
                } else if (bytesRead == -1) {
                    //响应头中可能没有Content-Length
                    mProgressInfo.setContentLength(totalBytesRead);
                }

                long curTime = SystemClock.elapsedRealtime();
                if (curTime - mLastTime >= mIntervalTime
                        || bytesRead == -1
                        || totalBytesRead == mProgressInfo.getContentLength()) {
                    //大于等于时间间隔，无读入，读入完毕时， 回调到主线程
                    final long finalBytesRead = bytesRead;
                    final long finalTempSize = tempSize;
                    final long finalTotalBytesRead = totalBytesRead;
                    final long finalIntervalTime = Math.max(curTime - mLastTime, 1);

                    for (final WeakReference<ProgressListener> reference : mReferences) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (reference != null && reference.get() != null && !isFinish) {
                                    isFinish = finalBytesRead == -1 || finalTotalBytesRead == mProgressInfo.getContentLength();
                                    mProgressInfo.setIntervalBytes(finalBytesRead != -1 ? finalTempSize : -1);
                                    mProgressInfo.setCurrentBytes(finalTotalBytesRead);
                                    mProgressInfo.setIntervalTime(finalIntervalTime);
                                    mProgressInfo.setFinish(isFinish);
                                    reference.get().onProgress(httpUrlOrHeader, false, mProgressInfo);
                                }
                            }
                        });
                    }
                }
                mLastTime = curTime;
                tempSize = 0;
                return bytesRead;
            }
        };
    }
}