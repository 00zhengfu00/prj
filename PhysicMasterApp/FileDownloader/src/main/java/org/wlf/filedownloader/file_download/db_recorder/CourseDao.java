package org.wlf.filedownloader.file_download.db_recorder;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.wlf.filedownloader.CourseInfo;
import org.wlf.filedownloader.VideoInfo;
import org.wlf.filedownloader.db.BaseContentDbDao;

/**
 * Created by huashigen on 2017/3/21.
 */

public class CourseDao extends BaseContentDbDao {
    public CourseDao(SQLiteOpenHelper dbHelper) {
        super(dbHelper, CourseInfo.Table.TABLE_NAME_OF_COURSE_INFO, CourseInfo.Table.COLUMN_NAME_OF_FIELD_ID);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CourseInfo.Table.getCreateTableSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            String sql = "ALTER TABLE " + CourseInfo.Table.TABLE_NAME_OF_COURSE_INFO + " ADD " +
                    CourseInfo.Table.COLUMN_NAME_OF_FIELD_USER_ID +
                    " TEXT";
            db.execSQL(sql);
        }
    }
}
