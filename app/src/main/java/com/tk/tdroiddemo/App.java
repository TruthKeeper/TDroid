package com.tk.tdroiddemo;

import android.app.Application;

import com.tk.tdroid.EventBusIndex;
import com.tk.tdroid.event.EventHelper;
import com.tk.tdroid.http.HttpConfig;
import com.tk.tdroid.http.HttpUtils;
import com.tk.tdroid.http.interceptor.CookieInterceptor;
import com.tk.tdroid.image.ImageLoader;
import com.tk.tdroid.utils.NetworkRxObservable;
import com.tk.tdroid.utils.StorageUtils;
import com.tk.tdroid.utils.Utils;

import java.io.File;

import okhttp3.Headers;
import okhttp3.HttpUrl;


/**
 * <pre>
 *      author : TK
 *      time : 2017/9/13
 *      desc :
 * </pre>
 */

public class App extends Application {
    @Override
    public void onTerminate() {
        super.onTerminate();
        NetworkRxObservable.getInstance().recycle();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        //初始化ImageLoader
        ImageLoader.init();

        HttpConfig httpConfig = new HttpConfig.Builder()
                .baseUrl("https://www.baidu.com/")
//                .baseUrl("https://api.github.com/")
                .log(true)
                .connectTimeoutMilli(10_000)
                .offlineCacheMaxStale(Integer.MAX_VALUE)
                .cacheDir(new File(StorageUtils.getCachePath(), "http_cache"))
                .cacheSize(100 * 1024 * 1024)
                .cookieEnabled(true)
                .cookieLoadProvider(new CookieInterceptor.CookieLoadProvider() {
                    @Override
                    public boolean load(HttpUrl requestUrl, Headers requestHeaders) {
                        //默认只要之前缓存过Cookie就设置
                        return true;
                    }
                })
                .cookieSaveProvider(new CookieInterceptor.CookieSaveProvider() {
                    @Override
                    public boolean save(HttpUrl requestUrl, Headers requestHeaders) {
                        //默认只要有Set-Cookie就记录
                        return true;
                    }
                })
                .httpsEnabled(true)
                .httpsCertificate(getResources().openRawResource(R.raw.github))
                .httpsPassword("github_test")
                .build();

        HttpUtils.init(httpConfig);
//        NetworkObservable.getInstance().init();
        NetworkRxObservable.getInstance().init();
//
        EventHelper.init(new EventBusIndex());

//        A a = new A(1, "321", true);

//        StorageUtils.getStoragePath(true);
//        StorageUtils.getStoragePath(false);

//        Logger.init(new Logger.Builder()
//                .logPath(Environment.getExternalStorageDirectory() + File.separator + "test")
//                .build());
//        Logger.e(getClass().getSimpleName(), "123", true);
//        Logger.json(Logger.Type.E,
//                Logger.getGlobalConfig().newBuilder()
//                        .tag("Json_Test")
//                        .logStackDepth(3)
//                        .build(),
//                "{\"name\":\"test\"}");
//        Logger.json(Logger.Type.I, null, "{\"name\":\"test\"}");


//        Logger.d(TimeUtils.formatExactDate(new Date().getTime() + 1000 * 100 + 30 * 1000 * 60));
//        Logger.d(TimeUtils.formatHourMinute(1000 * 100 + 30 * 1000 * 60));
//        Logger.d(TimeUtils.formatHourMinuteSecond(1000 * 100 + 30 * 1000 * 60, true));
//        Logger.d(TimeUtils.formatBySurplus(4 * 60 * 60 * 1000 + 1000 * 100 + 30 * 1000 * 60, true));
//        Logger.d(TimeUtils.formatSpanByNow(new Date().getTime() - 1000 * 1000*10));

//        ABC abc = InstanceFactory.create(ABC.class);


//        GlideApp.with(this)
//                .load(CacheTokenUrl.wrap(""))
//                .load(NetCropUtils.wrap())
//                .override(image)
//                .into(image);

//        try {
//            File file1 = new File(getCacheDir(), "t1.txt");
//            PrintWriter writer1 = new PrintWriter(file1);
//            writer1.write("111");
//            writer1.flush();
//            writer1.close();
//
//            File file2 = new File(getCacheDir(), "t2.txt");
//            PrintWriter writer2 = new PrintWriter(file2);
//            writer2.write("222222");
//            writer2.flush();
//            writer2.close();
//            //拷贝 file1 到 cache/t 文件夹下
//            Log.e("copy", FileUtils.copyFileToDir(file1, new File(getCacheDir(), "t")) + "");
//            //拷贝 file1 到 cache/t/tt.text 文件中
//            Log.e("copy", FileUtils.copyFileToFile(file1, new File(getCacheDir(), "t" + File.separator + "tt.txt")) + "");
//            //拷贝 file2 到 cache/t 文件夹下（不覆盖）
//            Log.e("copy", FileUtils.copyFileToDir(file2, new File(getCacheDir(), "t"), false) + "");
//            //拷贝 file2 到 cache/t/tt.text 文件中（覆盖）
//            Log.e("copy", FileUtils.copyFileToFile(file2, new File(getCacheDir(), "t" + File.separator + "tt.txt")) + "");
//            //移动file1 到 cache/tt 文件夹下
//            Log.e("move", FileUtils.moveFileToDir(file1, new File(getCacheDir(), "tt")) + "");
//            //移动cache/t/t1.text 到 cache/tt/tt.text 文件中
//            Log.e("move", FileUtils.moveFileToFile(new File(getCacheDir(), "t" + File.separator + "t1.txt"),
//                    new File(getCacheDir(), "tt" + File.separator + "tt.txt")) + "");
//
//            //结果：不存在
//            Log.e("result", file1.exists() + "");
//            //结果：不存在
//            Log.e("result", new File(getCacheDir(), "t" + File.separator + "t1.txt").exists() + "");
//            //长度为3而不为6
//            Log.e("result", new File(getCacheDir(), "tt" + File.separator + "tt.txt").length() + "");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


    }


//    private BitmapDrawable toDrawable(Context context, int resId, int width, int height) {
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        Drawable drawable = ContextCompat.getDrawable(context, resId);
//        drawable.setBounds(0, 0, width, height);
//        drawable.draw(canvas);
//        return new BitmapDrawable(context.getResources(), bitmap);
//    }


    //    @Instance
//    public static class ABC {
//    }
//    public static class A {
//        private int i;
//        private String j;
//        private boolean k;
//
//        @Logger(tag = "123")
//        public A(int i, String j, boolean k) {
//            this.i = i;
//            this.j = j;
//            this.k = k;
//        }
//    }
}
