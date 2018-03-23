package org.wlf.filedownloader.file_download.db_recorder;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.wlf.filedownloader.db.BaseContentDbDao;
import org.wlf.filedownloader.file_download.TsFileInfo;

/**
 * Created by huashigen on 2017-08-22.
 */

public class TsFileDao extends BaseContentDbDao {
    public TsFileDao(SQLiteOpenHelper dbHelper) {
        super(dbHelper, TsFileInfo.Table.TABLE_NAME_OF_DOWNLOAD_FILE, TsFileInfo.Table.COLUMN_NAME_OF_FIELD_ID);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TsFileInfo.Table.getCreateTableSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}