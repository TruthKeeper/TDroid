package com.tk.tdroid.result;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/18
 *     desc   : 回调结果
 * </pre>
 */
public class ResultInfo implements Parcelable {
    public final int requestCode;
    public final int resultCode;
    public final Intent data;
    public final boolean success;

    ResultInfo(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        this.success = resultCode == Activity.RESULT_OK;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "requestCode=" + requestCode +
                ", resultCode=" + resultCode +
                ", data=" + data +
                ", success=" + success +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultInfo that = (ResultInfo) o;

        if (requestCode != that.requestCode) return false;
        if (resultCode != that.resultCode) return false;
        if (success != that.success) return false;
        return data != null ? data.equals(that.data) : that.data == null;
    }

    @Override
    public int hashCode() {
        int result = requestCode;
        result = 31 * result + resultCode;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (success ? 1 : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.requestCode);
        dest.writeInt(this.resultCode);
        dest.writeParcelable(this.data, flags);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
    }

    protected ResultInfo(Parcel in) {
        this.requestCode = in.readInt();
        this.resultCode = in.readInt();
        this.data = in.readParcelable(Intent.class.getClassLoader());
        this.success = in.readByte() != 0;
    }

    public static final Creator<ResultInfo> CREATOR = new Creator<ResultInfo>() {
        @Override
        public ResultInfo createFromParcel(Parcel source) {
            return new ResultInfo(source);
        }

        @Override
        public ResultInfo[] newArray(int size) {
            return new ResultInfo[size];
        }
    };
}
