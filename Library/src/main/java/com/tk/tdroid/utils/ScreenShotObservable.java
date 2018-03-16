package com.tk.tdroid.utils;

import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *      author : TK
 *      time : 2018/3/15
 *      desc : 屏幕截屏监听可观察者
 * </pre>
 */

public class ScreenShotObservable {
    private static volatile ScreenShotObservable mScreenShotObservable = null;

    /**
     * 截屏文件目录
     */
    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap",
            "截图", "截屏"
    };
    /**
     * 时间阈值
     */
    private static final int THRESHOLD = 10_000;

    private final Subject<String> mSubject = PublishSubject.create();
    private MediaContentObserver mInnerMediaContentObserver;
    private MediaContentObserver mExternalMediaContentObserver;

    private ScreenShotObservable() {

    }

    public static ScreenShotObservable getInstance() {
        if (mScreenShotObservable == null) {
            synchronized (ScreenShotObservable.class) {
                if (mScreenShotObservable == null) {
                    mScreenShotObservable = new ScreenShotObservable();
                }
            }
        }
        return mScreenShotObservable;
    }

    /**
     * 初始化
     */
    public void init() {
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        if (mInnerMediaContentObserver == null) {
            mInnerMediaContentObserver = new MediaContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, mainHandler);
        }
        if (mExternalMediaContentObserver == null) {
            mExternalMediaContentObserver = new MediaContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mainHandler);
        }
        // 注册内容观察者
        Utils.getApp().getContentResolver().registerContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                false,
                mInnerMediaContentObserver);
        Utils.getApp().getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                false,
                mExternalMediaContentObserver);
    }

    /**
     * 获取到可观察者
     *
     * @param observer
     */
    public Observable<String> asObservable() {
        return mSubject;
    }

    /**
     * 媒体内容观察者类
     */
    private class MediaContentObserver extends ContentObserver {

        private final Uri mediaContentUri;

        MediaContentObserver(Uri contentUri, Handler handler) {
            super(handler);
            mediaContentUri = contentUri;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            processMediaContentChange(mediaContentUri);
        }

        private void processMediaContentChange(Uri contentUri) {
            Cursor cursor = null;
            try {
                //查询Db
                cursor = Utils.getApp().getContentResolver().query(contentUri,
                        new String[]{MediaStore.Files.FileColumns.DATA,
                                MediaStore.Images.ImageColumns.DATE_TAKEN,
                                MediaStore.Images.ImageColumns.WIDTH,
                                MediaStore.Images.ImageColumns.HEIGHT},
                        null,
                        null,
                        MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1");

                if (cursor == null || !cursor.moveToFirst()) {
                    return;
                }

                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                long dateTaken = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN));
                int width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH));
                int height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT));

                filterDataAndPost(path, dateTaken, width, height);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(cursor);
            }
        }

        /**
         * 过滤数据和发送
         *
         * @param path
         * @param dateTaken
         * @param width
         * @param height
         */
        private void filterDataAndPost(String path, long dateTaken, int width, int height) {
            if (EmptyUtils.isEmpty(path)) {
                return;
            }
            //路径包含字符串
            final String lowerPath = path.toLowerCase();
            for (int i = 0; i < KEYWORDS.length; i++) {
                if (lowerPath.contains(KEYWORDS[i])) {
                    break;
                }
                if (i == KEYWORDS.length - 1) {
                    return;
                }
            }
            //时间阈值
            if (Math.abs(System.currentTimeMillis() - dateTaken) > THRESHOLD) {
                return;
            }
            //屏幕大小
            if (width != Resources.getSystem().getDisplayMetrics().widthPixels
                    || height != Resources.getSystem().getDisplayMetrics().heightPixels) {
                return;
            }

            mSubject.onNext(path);
        }
    }
}
