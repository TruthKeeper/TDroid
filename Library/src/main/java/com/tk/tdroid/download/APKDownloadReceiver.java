package com.tk.tdroid.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tk.tdroid.utils.AppUtils;
import com.tk.tdroid.utils.EmptyUtils;
import com.tk.tdroid.utils.IntentUtils;
import com.tk.tdroid.utils.SharedPreferenceUtils;
import com.tk.tdroid.utils.Utils;

import java.io.File;

/**
 * <pre>
 *     author : amin
 *     time   : 2018/12/20
 *     desc   : 下载完成的广播监听
 * </pre>
 */
public class APKDownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                && SharedPreferenceUtils.getLong(APKDownloader.TASK_ID, -1) == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)) {
            String path = SharedPreferenceUtils.getString(APKDownloader.PATH, null);
            if (!EmptyUtils.isEmpty(path)) {
                IntentUtils.installApk(new File(path));
            }
        }
    }
}
