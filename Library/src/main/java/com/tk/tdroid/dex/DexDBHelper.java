package com.tk.tdroid.dex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <pre>
 *      author : TK
 *      time : 2018/2/28
 *      desc : DB保存版本号和是否加载完的数据
 * </pre>
 */

class DexDBHelper extends SQLiteOpenHelper {

    DexDBHelper(Context context) {
        super(context, "TDroidDex.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + DexProvider.TABLE_NAME
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DexProvider.VERSION_CODE + " INTEGER,"
                + DexProvider.IS_DEX_OPT + " INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS dex");
        onCreate(db);
    }
}