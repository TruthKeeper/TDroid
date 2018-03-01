package com.tk.tdroid.dex;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tk.tdroid.utils.AppUtils;
import com.tk.tdroid.utils.IOUtils;
import com.tk.tdroid.utils.Utils;


/**
 * <pre>
 *      author : TK
 *      time : 2018/2/28
 *      desc : 多进程场景下加载Dex，同步数据
 * </pre>
 */

public class DexProvider extends ContentProvider {
    public static final Uri URI = Uri.parse("content://com.tk.tdroid.DexProvider/DexOpt");
    public static final String TABLE_NAME = "opt";
    public static final String VERSION_CODE = "versionCode";
    public static final String IS_DEX_OPT = "isDexOpt";

    private DexDBHelper dexDBHelper;

    /**
     * 查询是否加载Dex成功
     *
     * @return
     */
    public static boolean queryIfDexOpt() {
        Cursor cursor = Utils.getApp().getContentResolver().query(URI,
                null,
                VERSION_CODE + " =? AND " + IS_DEX_OPT + " =?",
                new String[]{Integer.toString(AppUtils.getVerCode()), "1"}, null);
        final boolean dexOpt = cursor != null && cursor.getCount() > 0;
        IOUtils.close(cursor);
        return dexOpt;
    }

    /**
     * 加载Dex成功
     */
    public static void dexOptSuccess() {
        ContentValues values = new ContentValues();
        values.put(VERSION_CODE, AppUtils.getVerCode());
        values.put(IS_DEX_OPT, 1);
        Utils.getApp().getContentResolver().insert(DexProvider.URI, values);
    }

    @Override
    public boolean onCreate() {
        dexDBHelper = new DexDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return dexDBHelper.getReadableDatabase().query(TABLE_NAME,
                projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (values != null) {
            long rowId = dexDBHelper.getWritableDatabase().insert(TABLE_NAME, null, values);
            if (rowId > 0) {
                getContext().getContentResolver().notifyChange(URI, null);
                return ContentUris.withAppendedId(uri, rowId);
            }
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = dexDBHelper.getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(URI, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = dexDBHelper.getWritableDatabase().update(TABLE_NAME, values, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(URI, null);
        }
        return count;
    }
}
