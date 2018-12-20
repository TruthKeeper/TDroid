package com.tk.tdroid.download;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.tk.tdroid.utils.FileUtils;
import com.tk.tdroid.utils.IntentUtils;
import com.tk.tdroid.utils.SharedPreferenceUtils;
import com.tk.tdroid.utils.Utils;

import java.io.File;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/12/20
 *     desc   :
 * </pre>
 */
public class APKDownloader {
    public static final String STR_DOWNLOAD_START = "开始后台下载最新版本~";
    public static final String STR_DOWNLOADING = "已在后台开始下载~";
    public static final String TASK_ID = "apk_download_task_id";
    public static final String PATH = "apk_download_path";

    /**
     * 开始下载
     *
     * @param url         下载链接
     * @param versionName 版本名称
     */
    public static void download(String url, String versionName) {
        Uri uri = Uri.parse(url);
        //下载的绝对路径
        String apkPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        //下载的文件名
        String apkName = uri.getLastPathSegment();
        //先检查本地是否已经有需要升级版本的安装包，如有就不需要再下载
        File apkFile = new File(apkPath, apkName);
        if (apkFile.exists()) {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
            if (info != null) {
                //本地的apk安装包与最新安装包的版本号是否一致
                if (versionName.equals(info.versionName)) {
                    IntentUtils.installApk(apkFile);
                    return;
                }
            }
        }
        //查询下载状态
        DownloadManager dm = (DownloadManager) Utils.getApp().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        long downloadTaskId = SharedPreferenceUtils.getLong(TASK_ID, -1);
        query.setFilterById(downloadTaskId);
        Cursor cur = dm.query(query);
        if (cur != null) {
            // 检查下载任务是否已经存在
            if (cur.moveToFirst()) {
                int columnIndex = cur.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int status = cur.getInt(columnIndex);
                if (DownloadManager.STATUS_PENDING == status
                        || DownloadManager.STATUS_RUNNING == status) {
                    cur.close();
                    Toast.makeText(Utils.getApp(), STR_DOWNLOADING, Toast.LENGTH_LONG).show();
                    return;
                }
            }
            cur.close();
        }
        Toast.makeText(Utils.getApp(), STR_DOWNLOAD_START, Toast.LENGTH_LONG).show();
        //删除老的安装包
        FileUtils.deleteFile(apkFile);
        //开始执行下载
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setVisibleInDownloadsUi(true);
        request.setMimeType("application/vnd.android.package-archive");
        //在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，直到用户点击该Notification或者消除该Notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置下载路径
        request.setDestinationUri(Uri.fromFile(apkFile));
        //开始下载
        downloadTaskId = dm.enqueue(request);
        //保存下载的Id和下载的路径
        SharedPreferenceUtils.putLong(TASK_ID, downloadTaskId);
        SharedPreferenceUtils.putString(PATH, apkFile.getAbsolutePath());
    }
}
