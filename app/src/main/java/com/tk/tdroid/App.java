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
//        Logger.init(new Logger.Config());
    }
}
