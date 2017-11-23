package com.tk.tdroid.widget.http.progress;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntRange;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/22
 *      desc :
 * </pre>
 */

public class ProgressInfo implements Parcelable {
    /**
     * 创建{@link okhttp3.Request}或{@link okhttp3.Response}的时间
     */
    private long createAt;
    /**
     * 数据总长度 , 当服务端{@link okhttp3.Response}头无Content-Length时会返回-1 , <br>例如 {@code https://api.github.com/users/JakeWharton}
     */
    private long contentLength;
    /**
     * 当前已上传或下载的总长度
     */
    private long currentBytes;
    /**
     * 距离上次回调的时间间隔(毫秒)
     */
    private long intervalTime;
    /**
     * 距离上次回调的时间间隔内上传或下载的byte长度
     */
    private long intervalBytes;
    /**
     * 是否结束
     */
    private boolean finish;

    public long getCreateAt() {
        return createAt;
    }

    void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public long getContentLength() {
        return contentLength;
    }

    void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    void setCurrentBytes(long currentBytes) {
        this.currentBytes = currentBytes;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public long getIntervalBytes() {
        return intervalBytes;
    }

    void setIntervalBytes(long intervalBytes) {
        this.intervalBytes = intervalBytes;
    }

    public boolean isFinish() {
        return finish;
    }

    void setFinish(boolean finish) {
        this.finish = finish;
    }

    /**
     * 获取百分比进度
     *
     * @return
     */
    @IntRange(from = 0, to = 100)
    public int getPercent() {
        if (contentLength > 0) {
            return (int) (currentBytes * 100 / contentLength);
        }
        return 0;
    }

    /**
     * 获取速度
     *
     * @return 每秒的字节数
     */
    public long getSpeed() {
        return currentBytes * 1000 / (System.currentTimeMillis() - createAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.contentLength);
        dest.writeLong(this.currentBytes);
        dest.writeLong(this.intervalTime);
        dest.writeLong(this.intervalBytes);
        dest.writeLong(this.createAt);
        dest.writeByte(this.finish ? (byte) 1 : (byte) 0);
    }

    public ProgressInfo(long createAt) {
        this.createAt = createAt;
    }

    protected ProgressInfo(Parcel in) {
        this.contentLength = in.readLong();
        this.currentBytes = in.readLong();
        this.intervalTime = in.readLong();
        this.intervalBytes = in.readLong();
        this.createAt = in.readLong();
        this.finish = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ProgressInfo> CREATOR = new Parcelable.Creator<ProgressInfo>() {
        @Override
        public ProgressInfo createFromParcel(Parcel source) {
            return new ProgressInfo(source);
        }

        @Override
        public ProgressInfo[] newArray(int size) {
            return new ProgressInfo[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProgressInfo that = (ProgressInfo) o;

        if (createAt != that.createAt) return false;
        if (contentLength != that.contentLength) return false;
        if (currentBytes != that.currentBytes) return false;
        if (intervalTime != that.intervalTime) return false;
        if (intervalBytes != that.intervalBytes) return false;
        return finish == that.finish;
    }

    @Override
    public int hashCode() {
        int result = (int) (createAt ^ (createAt >>> 32));
        result = 31 * result + (int) (contentLength ^ (contentLength >>> 32));
        result = 31 * result + (int) (currentBytes ^ (currentBytes >>> 32));
        result = 31 * result + (int) (intervalTime ^ (intervalTime >>> 32));
        result = 31 * result + (int) (intervalBytes ^ (intervalBytes >>> 32));
        result = 31 * result + (finish ? 1 : 0);
        return result;
    }
}
