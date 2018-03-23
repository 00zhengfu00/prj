package org.wlf.filedownloader.file_download.db_recorder;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.wlf.filedownloader.VideoInfo;
import org.wlf.filedownloader.db.BaseContentDbDao;

import static android.graphics.PorterDuff.Mode.ADD;

/**
 * Created by huashigen on 2017/3/21.
 */

public class VideoDao extends BaseContentDbDao {
    public VideoDao(SQLiteOpenHelper dbHelper) {
        super(dbHelper, VideoInfo.Table.TABLE_NAME_OF_VIDEO_INFO, VideoInfo.Table
                .COLUMN_NAME_OF_FIELD_ID);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VideoInfo.Table.getCreateTableSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            String sql = "ALTER TABLE " + VideoInfo.Table.TABLE_NAME_OF_VIDEO_INFO + " ADD " +
                    VideoInfo.Table.COLUMN_NAME_OF_FIELD_EXPIRE_AT_TIME +
                    " TEXT";
            db.execSQL(sql);
        }
        if (oldVersion < 6) {
            String sqlAddVideoType = "ALTER TABLE " + VideoInfo.Table.TABLE_NAME_OF_VIDEO_INFO + " ADD " +
                    VideoInfo.Table.COLUMN_NAME_OF_FIELD_TYPE +
                    " INTEGER DEFAULT 0";
            String sqlAddUserId = "ALTER TABLE " + VideoInfo.Table.TABLE_NAME_OF_VIDEO_INFO + " ADD " +
                    VideoInfo.Table.COLUMN_NAME_OF_FIELD_USER_ID +
                    " TEXT";
            db.execSQL(sqlAddVideoType);
            db.execSQL(sqlAddUserId);
        }
    }
}
