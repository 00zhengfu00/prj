package com.physicmaster.modules.videoplay.cache.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.physicmaster.modules.videoplay.cache.dao.CourseDao;
import com.physicmaster.modules.videoplay.cache.dao.VideoDao;
import com.physicmaster.modules.videoplay.cache.intface.ContentDbDao;

import java.util.List;

/**
 * DownloadFile DbHelper
 * <br/>
 * 下载文件数据库操作类
 *
 * @author wlf(Andy)
 * @email 411086563@qq.com
 * <p>
 * 变更：
 * DB_VERSION：3->4 CourseInfo 表增加userId字段  2017-4-6
 * DB_VERSION：4->5 VideoInfo 表增加expiresAtTime字段  2017-4-11
 */
public class DownloadFileDbHelper extends BaseContentDbHelper {

    private static final String DB_NAME = "download_file.db";
    private static final int DB_VERSION = 6;

    public DownloadFileDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    protected void onConfigContentDbDaos(List<ContentDbDao> contentDbDaos) {
        DownloadFileDao downloadFileDao = new DownloadFileDao(this);
        // config DownloadFileDao dao
        contentDbDaos.add(downloadFileDao);
        VideoDao videoDao = new VideoDao(this);
        contentDbDaos.add(videoDao);
        CourseDao courseDao = new CourseDao(this);
        contentDbDaos.add(courseDao);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }
}
