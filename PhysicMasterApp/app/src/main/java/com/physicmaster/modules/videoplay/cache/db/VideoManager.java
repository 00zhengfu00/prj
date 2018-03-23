package com.physicmaster.modules.videoplay.cache.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.physicmaster.modules.videoplay.cache.OnDownloadFileChangeListener;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.intface.ContentDbDao;
import com.physicmaster.utils.CollectionUtil;
import com.physicmaster.utils.ContentValuesUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2017/3/21.
 */

public class VideoManager {
    // db helper
    private DownloadFileDbHelper mDownloadFileDbHelper;

    private Object mModifyLock = new Object();// modify lock
    private Context context;

    public VideoManager(Context context) {
        this.context = context;
        mDownloadFileDbHelper = DBSQLHelper.getInstance(context);
    }

    /**
     * 从数据库获取视频
     *
     * @return
     */
    public List<VideoInfo> getVideos() {
        // if memory cache is empty,then init from database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return null;
        }

        // query all
        Cursor cursor = dao.query(null, null, null, null);
        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (CollectionUtil.isEmpty(videoInfos)) {
            return null;
        }
        return videoInfos;
    }

    /**
     * 从数据库获取正在缓存的视频
     *
     * @return
     */
    public List<VideoInfo> getDownloadingVideos() {
        // if memory cache is empty,then init from database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return null;
        }

        // query all
        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "<> " +
                VideoInfo.STATE_DOWNLOADED, null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_CREATE_DATETIME + " " +
                "ASC");
        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (CollectionUtil.isEmpty(videoInfos)) {
            return null;
        }
        return videoInfos;
    }

    /**
     * 根据courseId获取视频列表
     *
     * @param courseId
     * @return
     */
    public List<VideoInfo> getVideosByCourseId(String courseId, int state) {
        // read from database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return null;
        }
        // query 指定courseId和下载状态的video 按order升序排列
        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_COURSE_ID + "= ? AND" +
                        " " +
                        VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "= ?",
                new String[]{courseId, state + ""}, VideoInfo.Table.COLUMN_NAME_OF_FIELD_ORDER +
                        " ASC");
        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return videoInfos;
    }

    /**
     * 根据userId获取视频列表
     *
     * @param userId
     * @return
     */
    public List<VideoInfo> getVideosByUserId(String userId, int state) {
        // read from database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return null;
        }
        // query 指定courseId和下载状态的video 按order升序排列
        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_USER_ID + "= ? AND" +
                        " " +
                        VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "= ?",
                new String[]{userId, state + ""}, VideoInfo.Table.COLUMN_NAME_OF_FIELD_ORDER +
                        " ASC");
        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return videoInfos;
    }

    /**
     * 根据userId获取视频列表
     *
     * @param type
     * @return
     */
    public List<VideoInfo> getVideosByVideoType(int type, int state, String userId) {
        // read from database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return null;
        }
        // query 指定courseId和下载状态的video 按order升序排列
        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_TYPE + "= ? AND " +
                        VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "= ? AND " + VideoInfo.Table.COLUMN_NAME_OF_FIELD_USER_ID + "= ?",
                new String[]{type + "", state + "", userId}, VideoInfo.Table.COLUMN_NAME_OF_FIELD_ORDER +
                        " ASC");
        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return videoInfos;
    }

    /**
     * 根据courseId获取视频列表
     *
     * @param type
     * @return
     */
    public List<VideoInfo> getVideosByVideoType(int type, int state) {
        // read from database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return null;
        }
        // query 指定courseId和下载状态的video 按order升序排列
        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_TYPE + "= ? AND " +
                        VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "= ? ",
                new String[]{type + "", state + ""}, VideoInfo.Table.COLUMN_NAME_OF_FIELD_ORDER +
                        " ASC");
        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return videoInfos;
    }

    /**
     * 根据courseId获取视频列表
     *
     * @param courseId
     * @return
     */
    public List<VideoInfo> getDownloadedVideosByCourseId(String courseId) {
        // read from database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(VideoInfo.Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return null;
        }
        // query 指定courseId的video 按order升序排列
        Cursor cursor = dao.query(null, VideoInfo.Table.COLUMN_NAME_OF_FIELD_COURSE_ID + "= ? AND" +
                        " " +
                        VideoInfo.Table.COLUMN_NAME_OF_FIELD_STATE + "= " + VideoInfo
                        .STATE_DOWNLOADED,
                new String[]{courseId}, VideoInfo.Table.COLUMN_NAME_OF_FIELD_ORDER + " " +
                        "ASC");
        List<VideoInfo> videoInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return videoInfos;
    }

    /**
     * get DownloadFiles by database cursor
     *
     * @param cursor database cursor
     * @return DownloadFiles
     */
    private List<VideoInfo> getDownloadFilesFromCursor(Cursor cursor) {
        List<VideoInfo> downloadFileInfos = new ArrayList<VideoInfo>();
        while (cursor != null && cursor.moveToNext()) {
            VideoInfo downloadFileInfo = new VideoInfo(cursor);
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
    private boolean deleteCourseFile(VideoInfo VideoInfo) {

        if (VideoInfo == null) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(com.physicmaster.modules.videoplay.cache.bean.VideoInfo.Table.TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return false;
        }

        String id = VideoInfo.getId();

        synchronized (mModifyLock) {// lock
            int result = dao.delete(BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= ?", new
                    String[]{VideoInfo.getId() + ""});
            if (result == 1) {
                // succeed,update memory cache
                notifyDownloadFileDeleted(VideoInfo);
                return true;
            }
        }
        return false;
    }

    private void notifyDownloadFileDeleted(VideoInfo VideoInfo) {
        // notify observer
//        if (mDownloadFileChangeObserver != null) {
//            mDownloadFileChangeObserver.onDownloadFileDeleted(VideoInfo);
//        }
    }

    /**
     * add a new course
     */
    public boolean addOrUpdateVideo(VideoInfo videoInfo) {

        if (videoInfo == null) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(com.physicmaster.modules.videoplay.cache.bean.VideoInfo
                .Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return false;
        }

        ContentValues values = videoInfo.getContentValues();
        if (ContentValuesUtil.isEmpty(values)) {
            return false;
        }

        String videoId = videoInfo.getId() + "";

        VideoInfo VideoInfo1 = getVideoInfo(videoId);
        // exist the download file, update
        if (VideoInfo1 != null) {
            synchronized (mModifyLock) {// lock
                // update the downloadFileInfoExist with the new VideoInfo
                VideoInfo1.update(videoInfo);

                boolean isSucceed = updateDownloadFileInternal(VideoInfo1, false,
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
     * get a VideoInfo
     */
    public VideoInfo getVideoInfo(String videoId) {

        VideoInfo videoInfo = null;
        // find in database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(com.physicmaster.modules.videoplay.cache.bean.VideoInfo.Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return null;
        }

        Cursor cursor = dao.query(null, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= " +
                "?", new
                String[]{videoId}, null);
        if (cursor != null && cursor.moveToFirst()) {
            videoInfo = new VideoInfo(cursor);
        }

        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (videoInfo == null) {
            return null;
        }
        return videoInfo;
    }

    /**
     * update an exist DownloadFile
     */
    private boolean updateDownloadFileInternal(VideoInfo VideoInfo, boolean lockInternal,
                                               OnDownloadFileChangeListener.Type
                                                       notifyType) {

        if (VideoInfo == null) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(com.physicmaster.modules.videoplay.cache.bean.VideoInfo
                .Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return false;
        }

        ContentValues values = VideoInfo.getContentValues();
        if (ContentValuesUtil.isEmpty(values)) {
            return false;
        }

        if (lockInternal) {// need internal lock
            synchronized (mModifyLock) {// lock
                int result = dao.update(values, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID +
                        "= ?", new
                        String[]{VideoInfo.getId() + ""});
                if (result == 1) {
                    return true;
                }
            }
        } else {// not lock
            int result = dao.update(values, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= " +
                    "?", new
                    String[]{VideoInfo.getId() + ""});
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    public void deleteCourseFile(int courseId) throws Exception {
        VideoInfo videoInfo = getVideoInfo(courseId + "");
        if (videoInfo == null) {
            return;
        }
        boolean isSucceed = deleteVideoByVideoId(videoInfo.getId());
        if (!isSucceed) {
            // really deleted ?
            VideoInfo VideoInfo1 = getVideoInfo(videoInfo.getId() + "");
            if (VideoInfo1 == null) {
                // has been deleted
            } else {
                // rollback, none
                throw new Exception("delete failed !");
            }
        }
    }

    /**
     * delete a video by videoId
     */
    public boolean deleteVideoByVideoId(String videoId) {

        if (TextUtils.isEmpty(videoId)) {
            return false;
        }

        //1.删除video下的所有ts文件
        DownloadCacher downloadManager = new DownloadCacher(context);
//        List<DownloadFileInfo> infos = downloadManager.getDownloadInfoByParentId(videoId);
        boolean rlt = downloadManager.deleteDownloadFileByParentId(videoId);
        if (!rlt) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(com.physicmaster.modules.videoplay.cache.bean.VideoInfo
                .Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return false;
        }

        //2.删除video的m3u8文件
        VideoInfo videoInfo = getVideoInfo(videoId);
        if (videoInfo == null) {
            return true;
        }
        String m3u8Path = videoInfo.getVideoPath();
        if (!TextUtils.isEmpty(m3u8Path)) {
            File file = new File(m3u8Path);
            if (file.exists()) {
                file.delete();
            }
        }

        //3.删除video表中的对应video记录
        synchronized (mModifyLock) {// lock
            int result = dao.delete(VideoInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= ?", new
                    String[]{videoId});
            if (result == 1) {
                return true;
            }
            VideoInfo videoInfo1 = getVideoInfo(videoId);
            if (videoInfo1 == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * delete a video by videoId
     */
    private boolean deleteVideosByCourseId(String courseId) {
        //1.删除vide
        if (TextUtils.isEmpty(courseId)) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(com.physicmaster.modules.videoplay.cache.bean.VideoInfo
                .Table
                .TABLE_NAME_OF_VIDEO_INFO);
        if (dao == null) {
            return false;
        }

        synchronized (mModifyLock) {// lock
            int result = dao.delete(VideoInfo.Table.COLUMN_NAME_OF_FIELD_COURSE_ID + "= ?", new
                    String[]{courseId});
            if (result == 1) {
                return true;
            }
        }
        return false;
    }
}
