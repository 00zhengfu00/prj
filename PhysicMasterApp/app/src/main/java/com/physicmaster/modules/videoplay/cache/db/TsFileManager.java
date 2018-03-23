package com.physicmaster.modules.videoplay.cache.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.physicmaster.modules.videoplay.cache.OnDownloadFileChangeListener;
import com.physicmaster.modules.videoplay.cache.bean.TsFileInfo;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.intface.ContentDbDao;
import com.physicmaster.utils.CollectionUtil;
import com.physicmaster.utils.ContentValuesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2017/3/21.
 */

public class TsFileManager {
    // db helper
    private DownloadFileDbHelper mDownloadFileDbHelper;

    private Object mModifyLock = new Object();// modify lock
    private Context context;

    public TsFileManager(Context context) {
        this.context = context;
        mDownloadFileDbHelper = DBSQLHelper.getInstance(context);
    }

    /**
     * 从数据库获取视频
     *
     * @return
     */
    public List<TsFileInfo> getTsFiles() {
        // if memory cache is empty,then init from database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(TsFileInfo.Table.TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return null;
        }

        // query all
        Cursor cursor = dao.query(null, null, null, null);
        List<TsFileInfo> tsFileInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (CollectionUtil.isEmpty(tsFileInfos)) {
            return null;
        }
        return tsFileInfos;
    }

//    /**
//     * 从数据库获取正在缓存的视频
//     *
//     * @return
//     */
//    public List<VideoInfo> getDownloadingVideos() {
//        // if memory cache is empty,then init from database
//        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
//                .TABLE_NAME_OF_VIDEO_INFO);
//        if (dao == null) {
//            return null;
//        }
//
//        // query all
//        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "<> " +
//                VideoInfo.STATE_DOWNLOADED, null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + " " +
//                "DESC");
//        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
//        // close the cursor
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//
//        if (CollectionUtil.isEmpty(videoInfos)) {
//            return null;
//        }
//        return videoInfos;
//    }

//    /**
//     * 根据courseId获取视频列表
//     *
//     * @param courseId
//     * @return
//     */
//    public List<VideoInfo> getVideosByCourseId(String courseId, int state) {
//        // read from database
//        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
//                .TABLE_NAME_OF_VIDEO_INFO);
//        if (dao == null) {
//            return null;
//        }
//        // query 指定courseId和下载状态的video 按order升序排列
//        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_COURSE_ID + "= ? AND" +
//                        " " +
//                        VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "= ?",
//                new String[]{courseId, state + ""}, VideoInfo.Table.COLUMN_NAME_OF_FIELD_ORDER +
//                        " ASC");
//        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
//        // close the cursor
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//        return videoInfos;
//    }

//    /**
//     * 根据userId获取视频列表
//     *
//     * @param userId
//     * @return
//     */
//    public List<VideoInfo> getVideosByUserId(String userId, int state) {
//        // read from database
//        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
//                .TABLE_NAME_OF_VIDEO_INFO);
//        if (dao == null) {
//            return null;
//        }
//        // query 指定courseId和下载状态的video 按order升序排列
//        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_USER_ID + "= ? AND" +
//                        " " +
//                        VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "= ?",
//                new String[]{userId, state + ""}, VideoInfo.Table.COLUMN_NAME_OF_FIELD_ORDER +
//                        " ASC");
//        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
//        // close the cursor
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//        return videoInfos;
//    }

//    /**
//     * 根据courseId获取视频列表
//     *
//     * @param type
//     * @return
//     */
//    public List<VideoInfo> getVideosByVideoType(int type, int state, String userId) {
//        // read from database
//        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
//                .TABLE_NAME_OF_VIDEO_INFO);
//        if (dao == null) {
//            return null;
//        }
//        // query 指定courseId和下载状态的video 按order升序排列
//        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_TYPE + "= ? AND " +
//                        VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "= ? AND " + VideoInfo.Table.COLUMN_NAME_OF_FIELD_USER_ID + "= ?",
//                new String[]{type + "", state + "", userId}, VideoInfo.Table.COLUMN_NAME_OF_FIELD_ORDER +
//                        " ASC");
//        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
//        // close the cursor
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//        return videoInfos;
//    }

//    /**
//     * 根据courseId获取视频列表
//     *
//     * @param type
//     * @return
//     */
//    public List<VideoInfo> getVideosByVideoType(int type, int state) {
//        // read from database
//        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
//                .TABLE_NAME_OF_VIDEO_INFO);
//        if (dao == null) {
//            return null;
//        }
//        // query 指定courseId和下载状态的video 按order升序排列
//        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_TYPE + "= ? AND " +
//                        VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "= ? ",
//                new String[]{type + "", state + ""}, VideoInfo.Table.COLUMN_NAME_OF_FIELD_ORDER +
//                        " ASC");
//        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
//        // close the cursor
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//        return videoInfos;
//    }
//
//    /**
//     * 根据courseId获取视频列表
//     *
//     * @param courseId
//     * @return
//     */
//    public List<VideoInfo> getDownloadedVideosByCourseId(String courseId) {
//        // read from database
//        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
//                .TABLE_NAME_OF_VIDEO_INFO);
//        if (dao == null) {
//            return null;
//        }
//        // query 指定courseId的video 按order升序排列
//        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_COURSE_ID + "= ? AND" +
//                        " " +
//                        VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "= " + VideoInfo
//                        .STATE_DOWNLOADED,
//                new String[]{courseId}, VideoInfo.Table.COLUMN_NAME_OF_FIELD_ORDER + " " +
//                        "ASC");
//        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
//        // close the cursor
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//        return videoInfos;
//    }

    /**
     * get DownloadFiles by database cursor
     *
     * @param cursor database cursor
     * @return DownloadFiles
     */
    private List<TsFileInfo> getDownloadFilesFromCursor(Cursor cursor) {
        List<TsFileInfo> downloadFileInfos = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            TsFileInfo downloadFileInfo = new TsFileInfo(cursor);
            if (downloadFileInfo == null) {
                continue;
            }
            downloadFileInfos.add(downloadFileInfo);
        }
        return downloadFileInfos;
    }

    /**
     * delete a DownloadFile
     */
    private boolean deleteTsFile(TsFileInfo tsFileInfo) {

        if (tsFileInfo == null) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(TsFileInfo.Table.TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return false;
        }

        int id = tsFileInfo.getId();

        synchronized (mModifyLock) {// lock
            int result = dao.delete(BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= ?", new String[]{tsFileInfo.getId() + ""});
            if (result == 1) {
                // succeed,update memory cache
                return true;
            }
        }
        return false;
    }

    /**
     * add a new tsFile
     */
    public boolean addOrUpdateVideo(TsFileInfo tsFileInfo) {

        if (tsFileInfo == null) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(TsFileInfo
                .Table
                .TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return false;
        }

        ContentValues values = tsFileInfo.getContentValues();
        if (ContentValuesUtil.isEmpty(values)) {
            return false;
        }

        String tsFileId = tsFileInfo.getUrl();

        TsFileInfo tsFileInfo1 = getTsFileInfoByUrl(tsFileId);
        // exist the download file, update
        if (tsFileInfo1 != null) {
            synchronized (mModifyLock) {// lock
                // update the downloadFileInfoExist with the new VideoInfo
                tsFileInfo1.update(tsFileInfo);

                boolean isSucceed = updateDownloadFileInternal(tsFileInfo1, false,
                        OnDownloadFileChangeListener.Type.OTHER);

                if (!isSucceed) {
                    // not concern
                }
                return true;
            }
        }

        // insert new one in db
        synchronized (mModifyLock) {// lock
            long id = dao.insert(values);
            if (id != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * get a tsFileInfo
     */
    public TsFileInfo getTsFileInfo(String tsFileInfoId) {

        TsFileInfo tsFileInfo = null;
        // find in database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return null;
        }

        Cursor cursor = dao.query(null, TsFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= " +
                "?", new
                String[]{tsFileInfoId}, null);
        if (cursor != null && cursor.moveToFirst()) {
            tsFileInfo = new TsFileInfo(cursor);
        }

        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (tsFileInfo == null) {
            return null;
        }
        return tsFileInfo;
    }

    /**
     * get a tsFileInfo
     */
    public TsFileInfo getTsFileInfoByUrl(String tsFileUrl) {

        TsFileInfo tsFileInfo = null;
        // find in database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(TsFileInfo.Table.TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return null;
        }

        Cursor cursor = dao.query(null, TsFileInfo.Table.COLUMN_NAME_OF_FIELD_URL + "= " +
                "?", new
                String[]{tsFileUrl}, null);
        if (cursor != null && cursor.moveToFirst()) {
            tsFileInfo = new TsFileInfo(cursor);
        }

        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (tsFileInfo == null) {
            return null;
        }
        return tsFileInfo;
    }

    /**
     * update an exist DownloadFile
     */
    private boolean updateDownloadFileInternal(TsFileInfo tsFileInfo, boolean lockInternal,
                                               OnDownloadFileChangeListener.Type
                                                       notifyType) {
        if (tsFileInfo == null) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(TsFileInfo
                .Table
                .TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return false;
        }

        ContentValues values = tsFileInfo.getContentValues();
        if (ContentValuesUtil.isEmpty(values)) {
            return false;
        }

        if (lockInternal) {// need internal lock
            synchronized (mModifyLock) {// lock
                int result = dao.update(values, TsFileInfo.Table.COLUMN_NAME_OF_FIELD_ID +
                        "= ?", new
                        String[]{tsFileInfo.getId() + ""});
                if (result == 1) {
                    return true;
                }
            }
        } else {// not lock
            int result = dao.update(values, TsFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= " +
                    "?", new
                    String[]{tsFileInfo.getId() + ""});
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

//    public void deleteCourseFile(int courseId) throws Exception {
//        VideoInfo videoInfo = getTsFileInfo(courseId + "");
//        if (videoInfo == null) {
//            return;
//        }
//        boolean isSucceed = deleteVideoByVideoId(videoInfo.getId());
//        if (!isSucceed) {
//            // really deleted ?
//            VideoInfo VideoInfo1 = getTsFileInfo(videoInfo.getId() + "");
//            if (VideoInfo1 == null) {
//                // has been deleted
//            } else {
//                // rollback, none
//                throw new Exception("delete failed !");
//            }
//        }
//    }


    /**
     * delete a video by videoId
     */
    private boolean deleteTsFileByTsFileId(String tsFileId) {
        //1.删除vide
        if (TextUtils.isEmpty(tsFileId)) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo
                .Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return false;
        }

        synchronized (mModifyLock) {// lock
            int result = dao.delete(TsFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= ?", new String[]{tsFileId});
            if (result == 1) {
                return true;
            }
        }
        return false;
    }
}
