package com.tk.tdroid;

import android.app.Application;

import com.tk.tdroid.utils.Utils;

/**
 * <pre>
 *      author : TK
 *      time : 2017/9/13
 *      desc :
 * </pre>
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
//        Logger.init(new Logger.Config()
//                .logPath(Environment.getExternalStorageDirectory() + File.separator + "test"));

//        Logger.d(TimeUtils.formatExactDate(new Date().getTime() + 1000 * 100 + 30 * 1000 * 60));
//        Logger.d(TimeUtils.formatHourMinute(1000 * 100 + 30 * 1000 * 60));
//        Logger.d(TimeUtils.formatHourMinuteSecond(1000 * 100 + 30 * 1000 * 60, true));
//        Logger.d(TimeUtils.formatBySurplus(4 * 60 * 60 * 1000 + 1000 * 100 + 30 * 1000 * 60, true));
//        Logger.d(TimeUtils.formatSpanByNow(new Date().getTime() - 1000 * 1000*10));
    }
}
