package com.physicmaster.modules.videoplay.cache.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.physicmaster.modules.videoplay.cache.OnDownloadFileChangeListener;
import com.physicmaster.modules.videoplay.cache.bean.CourseInfo;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.intface.ContentDbDao;
import com.physicmaster.utils.CollectionUtil;
import com.physicmaster.utils.ContentValuesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2017/3/21.
 */

public class CourseManager {
    // db helper
    private DownloadFileDbHelper mDownloadFileDbHelper;
    // download files memory cache
    private Object mModifyLock = new Object();// modify lock
    private Context context;

    public CourseManager(Context context) {
        this.context = context;
        mDownloadFileDbHelper = DBSQLHelper.getInstance(context);
    }

    /**
     * 从数据库读取获取课程
     *
     * @return
     */
    public List<CourseInfo> getCoursesBySubjectId(String subjectId, String userId) {
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(CourseInfo.Table
                .TABLE_NAME_OF_COURSE_INFO);
        if (dao == null) {
            return null;
        }

        // query all
        Cursor cursor = dao.query(null, CourseInfo.Table.COLUMN_NAME_OF_FIELD_SUBJECT_ID + " = ? AND "
                        + CourseInfo.Table.COLUMN_NAME_OF_FIELD_USER_ID + " = ? ",
                new String[]{subjectId, userId}, VideoInfo.Table
                        .COLUMN_NAME_OF_FIELD_CREATE_DATETIME +
                        " DESC");
        List<CourseInfo> downloadFileInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (CollectionUtil.isEmpty(downloadFileInfos)) {
            return null;
        }
        List<CourseInfo> courseInfos = new ArrayList<CourseInfo>();
        // cache in memory
        for (CourseInfo courseInfo : downloadFileInfos) {
            if (courseInfo.getDownloadedVideoNum() > 0) {
                courseInfos.add(courseInfo);
            }
        }
        return courseInfos;
    }

    /**
     * 根据科目ID从数据库读取获取课程
     *
     * @return
     */
    public List<CourseInfo> getAllCourses(String userId) {
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(CourseInfo.Table
                .TABLE_NAME_OF_COURSE_INFO);
        if (dao == null) {
            return null;
        }

        // query all
        Cursor cursor = dao.query(null, CourseInfo.Table.COLUMN_NAME_OF_FIELD_USER_ID + " = ?",
                new String[]{userId}, null);
        List<CourseInfo> downloadFileInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (CollectionUtil.isEmpty(downloadFileInfos)) {
            return null;
        }
        List<CourseInfo> courseInfos = new ArrayList<CourseInfo>();
        // cache in memory
        for (CourseInfo courseInfo : downloadFileInfos) {
            if (courseInfo.getDownloadedVideoNum() > 0) {
                courseInfos.add(courseInfo);
            }
        }
        return courseInfos;
    }

    /**
     * 根据科目ID从数据库读取获取课程
     *
     * @return
     */
    public void updateAllCoursesWithUserId(String userId) {
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(CourseInfo.Table
                .TABLE_NAME_OF_COURSE_INFO);
        if (dao == null) {
            return;
        }

        // query all
        Cursor cursor = dao.query(null, null, null, null);
        List<CourseInfo> downloadFileInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (CollectionUtil.isEmpty(downloadFileInfos)) {
            return;
        }
        for (CourseInfo courseInfo : downloadFileInfos) {
            if (TextUtils.isEmpty(courseInfo.getUserId())) {
                courseInfo.setUserId(userId);
            }
        }
    }

    /**
     * 根据状态从数据库读取获取课程
     *
     * @return
     */
    public List<CourseInfo> getCoursesByState(int state) {
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(CourseInfo.Table
                .TABLE_NAME_OF_COURSE_INFO);
        if (dao == null) {
            return null;
        }

        // query all
        Cursor cursor = dao.query(null, CourseInfo.Table.COLUMN_NAME_OF_FIELD_STATE + " = ?",
                new String[]{state + ""}, null);
        List<CourseInfo> downloadFileInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (CollectionUtil.isEmpty(downloadFileInfos)) {
            return null;
        }
        List<CourseInfo> courseInfos = new ArrayList<CourseInfo>();
        // cache in memory
        for (CourseInfo downloadFileInfo : downloadFileInfos) {
            courseInfos.add(downloadFileInfo);
        }
        return courseInfos;
    }

    /**
     * get DownloadFiles by database cursor
     *
     * @param cursor database cursor
     * @return DownloadFiles
     */
    private List<CourseInfo> getDownloadFilesFromCursor(Cursor cursor) {
        List<CourseInfo> downloadFileInfos = new ArrayList<CourseInfo>();
        while (cursor != null && cursor.moveToNext()) {
            CourseInfo downloadFileInfo = new CourseInfo(cursor);
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
    private boolean deleteCourseFile(CourseInfo courseInfo) {

        if (courseInfo == null) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(CourseInfo.Table
                .TABLE_NAME_OF_COURSE_INFO);
        if (dao == null) {
            return false;
        }

        String id = courseInfo.getId();

        synchronized (mModifyLock) {// lock
            int result = dao.delete(BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= ?", new
                    String[]{courseInfo.getId() + ""});
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * add a new course
     */
    public boolean addOrUpdateCourse(CourseInfo courseInfo) {

        if (courseInfo == null) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(CourseInfo.Table
                .TABLE_NAME_OF_COURSE_INFO);
        if (dao == null) {
            return false;
        }

        ContentValues values = courseInfo.getContentValues();
        if (ContentValuesUtil.isEmpty(values)) {
            return false;
        }

        String courseId = courseInfo.getId();

        CourseInfo courseInfoExist = getCourseInfo(courseId);
        // exist the download file, update
        if (courseInfoExist != null) {
//            if (UrlUtil.isUrl(courseInfo.getPosterUrl())) {
//                courseInfoExist.setPosterUrl(courseInfo.getPosterUrl());
//            }
//            if (courseInfo.getVideoNum() > 0 ) {
//                courseInfoExist.setVideoNum(courseInfo.getVideoNum());
//            }
//            if (courseInfo.getDownloadedVideoNum() > 0 ) {
//                courseInfoExist.setDownloadedVideoNum(courseInfo.getDownloadedVideoNum());
//            }
//            if (!TextUtils.isEmpty(courseInfo.getName())) {
//                courseInfoExist.setName(courseInfo.getName());
//            }
//            if (courseInfo.getTotalSize()>0L) {
//                courseInfoExist.setTotalSize(courseInfo.getTotalSize());
//            }
            synchronized (mModifyLock) {// lock
//                courseInfoExist.update(courseInfo);
                boolean isSucceed = updateDownloadFileInternal(courseInfo, false,
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
     * get a DownloadFile
     */
    public CourseInfo getCourseInfo(String courseId) {

        CourseInfo courseInfo = null;

        // find in database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(CourseInfo.Table
                .TABLE_NAME_OF_COURSE_INFO);
        if (dao == null) {
            return null;
        }

        Cursor cursor = dao.query(null, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= " +
                "?", new
                String[]{courseId + ""}, null);
        if (cursor != null && cursor.moveToFirst()) {
            courseInfo = new CourseInfo(cursor);
        }

        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (courseInfo == null) {
            return null;
        }
        return courseInfo;
    }

    /**
     * update an exist DownloadFile
     */
    private boolean updateDownloadFileInternal(CourseInfo courseInfo, boolean lockInternal,
                                               OnDownloadFileChangeListener.Type
                                                       notifyType) {

        if (courseInfo == null) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(CourseInfo.Table
                .TABLE_NAME_OF_COURSE_INFO);
        if (dao == null) {
            return false;
        }

        ContentValues values = courseInfo.getContentValues();
        if (ContentValuesUtil.isEmpty(values)) {
            return false;
        }

        if (lockInternal) {// need internal lock
            synchronized (mModifyLock) {// lock
                int result = dao.update(values, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID +
                        "= ?", new
                        String[]{courseInfo.getId() + ""});
                if (result == 1) {
                    return true;
                }
            }
        } else {// not lock
            int result = dao.update(values, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= " +
                    "?", new
                    String[]{courseInfo.getId() + ""});
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    public void deleteCourseFile(String courseId) throws Exception {
        CourseInfo courseInfo = getCourseInfo(courseId);
        if (courseInfo == null) {
            return;
        }
        boolean isSucceed = deleteCourse(courseInfo);
        if (!isSucceed) {
            // really deleted ?
            CourseInfo courseInfo1 = getCourseInfo(courseInfo.getId());
            if (courseInfo1 == null) {
                // has been deleted
            } else {
                // rollback, none
                throw new Exception("delete failed !");
            }
        }
    }

    /**
     * delete a course
     */
    public boolean deleteCourse(CourseInfo courseInfo) {

        if (courseInfo == null) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(CourseInfo.Table
                .TABLE_NAME_OF_COURSE_INFO);
        if (dao == null) {
            return false;
        }
        //1.删除course下的所有video
        VideoManager videoManager = new VideoManager(context);
        List<VideoInfo> videoInfos = videoManager.getVideosByCourseId(courseInfo.getId(),
                VideoInfo.STATE_DOWNLOADED);
        for (VideoInfo videoInfo : videoInfos) {
            boolean rlt = videoManager.deleteVideoByVideoId(videoInfo.getId());
            if (!rlt) {
                return false;
            }
        }
        //2.删除course表中对应记录
        synchronized (mModifyLock) {// lock
            int result = dao.delete(CourseInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= ?", new
                    String[]{courseInfo.getId() + ""});
            if (result == 1) {
                return true;
            }
            CourseInfo courseInfo1 = getCourseInfo(courseInfo.getId());
            return courseInfo1 == null;
        }
    }
}
