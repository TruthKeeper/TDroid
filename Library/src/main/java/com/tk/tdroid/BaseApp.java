package com.tk.tdroid;

import android.app.Application;

import com.tk.tdroid.image.load.ImageLoader;
import com.tk.tdroid.utils.NetworkObservable;
import com.tk.tdroid.utils.Utils;

/**
 * <pre>
 *      author : TK
 *      time : 2017/9/21
 *      desc :
 * </pre>
 */

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        NetworkObservable.getInstance().init();
        ImageLoader.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        NetworkObservable.getInstance().recycle();
    }
}
