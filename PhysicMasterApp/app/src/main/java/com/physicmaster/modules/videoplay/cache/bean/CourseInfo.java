package com.physicmaster.modules.videoplay.cache.bean;


import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.physicmaster.utils.UrlUtil;

/**
 * Created by huashigen on 2017/3/21.
 */

public class CourseInfo {
    private String mId;
    private int mSubjectId;
    private String mName;
    private int mVideoNum;
    private int mDownloadedVideoNum;
    private long mTotalSize;
    private String mCreateDatetime;
    private String mPosterUrl;
    private int state;//缓存状态0-未缓存;1-缓存中;2-已缓存
    private String userId;//用户id

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getId() {
        return mId;
    }

    public int getSubjectId() {
        return mSubjectId;
    }

    public String getName() {
        return mName;
    }

    public int getVideoNum() {
        return mVideoNum;
    }

    public int getDownloadedVideoNum() {
        return mDownloadedVideoNum;
    }

    public long getTotalSize() {
        return mTotalSize;
    }

    public String getCreateDatetime() {
        return mCreateDatetime;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setSubjectId(int mSubjectId) {
        this.mSubjectId = mSubjectId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setVideoNum(int mVideoNum) {
        this.mVideoNum = mVideoNum;
    }

    public void setDownloadedVideoNum(int mDownloadedVideoNum) {
        this.mDownloadedVideoNum = mDownloadedVideoNum;
    }

    public void setTotalSize(long mTotalSize) {
        this.mTotalSize = mTotalSize;
    }

    public void setCreateDatetime(String mCreateDatetime) {
        this.mCreateDatetime = mCreateDatetime;
    }

    public void setPosterUrl(String mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }

    public static final class Table {
        /**
         * table name
         */
        public static final String TABLE_NAME_OF_COURSE_INFO = "tb_course_info";
        /**
         * id field name
         */
        public static final String COLUMN_NAME_OF_FIELD_ID = "_id";
        /**
         * subjectId field name
         */
        public static final String COLUMN_NAME_OF_FIELD_SUBJECT_ID = "subject_id";
        /**
         * videoName field name
         */
        public static final String COLUMN_NAME_OF_FIELD_COURSE_NAME = "name";
        /**
         * downloadedVideoNum field name
         */
        public static final String COLUMN_NAME_OF_FIELD_DOWNLOADED_VIDEO_NUM =
                "downloaded_video_num";
        /**
         * totalSize field name
         */
        public static final String COLUMN_NAME_OF_FIELD_TOTAL_SIZE = "total_size";
        /**
         * videoNum field name
         */
        public static final String COLUMN_NAME_OF_FIELD_VIDEO_NUM = "video_num";
        /**
         * create download datetime
         */
        public static final String COLUMN_NAME_OF_FIELD_CREATE_DATETIME = "create_datetime";
        /**
         * posterUrl field name
         */
        public static final String COLUMN_NAME_OF_FIELD_POSTER_URL = "poster_url";
        /**
         * cacheSate field name
         */
        public static final String COLUMN_NAME_OF_FIELD_STATE = "state";
        /**
         * userId field name
         */
        public static final String COLUMN_NAME_OF_FIELD_USER_ID = "user_id";

        /**
         * the sql to create table
         */
        public static final String getCreateTableSql() {
            String createTableSql = "CREATE TABLE IF NOT EXISTS " //
                    + TABLE_NAME_OF_COURSE_INFO //
                    + "(" + COLUMN_NAME_OF_FIELD_ID + " TEXT PRIMARY KEY,"//
                    + COLUMN_NAME_OF_FIELD_COURSE_NAME + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_DOWNLOADED_VIDEO_NUM + " INTEGER,"//
                    + COLUMN_NAME_OF_FIELD_SUBJECT_ID + " INTEGER,"//
                    + COLUMN_NAME_OF_FIELD_VIDEO_NUM + " INTEGER,"//
                    + COLUMN_NAME_OF_FIELD_TOTAL_SIZE + " BIGINT,"//
                    + COLUMN_NAME_OF_FIELD_POSTER_URL + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_STATE + " INTEGER,"//
                    + COLUMN_NAME_OF_FIELD_USER_ID + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_CREATE_DATETIME + " TEXT" + ")";//
            return createTableSql;
        }
    }

    public CourseInfo() {
    }

    public CourseInfo(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            String id = null;
            int subjectId = -1;
            String name = "";
            int videoNum = 0;
            int downloadedVideoNum = 0;
            long totalSize = 0;
            String createDatetime = null;
            String posterUrl = null;
            int state = -1;
            int columnIndex = -1;
            String userId = null;
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_ID);
            if (columnIndex != -1) {
                id = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_SUBJECT_ID);
            if (columnIndex != -1) {
                subjectId = cursor.getInt(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_COURSE_NAME);
            if (columnIndex != -1) {
                name = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_DOWNLOADED_VIDEO_NUM);
            if (columnIndex != -1) {
                downloadedVideoNum = cursor.getInt(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_TOTAL_SIZE);
            if (columnIndex != -1) {
                totalSize = cursor.getLong(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_VIDEO_NUM);
            if (columnIndex != -1) {
                videoNum = cursor.getInt(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_CREATE_DATETIME);
            if (columnIndex != -1) {
                createDatetime = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_POSTER_URL);
            if (columnIndex != -1) {
                posterUrl = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_USER_ID);
            if (columnIndex != -1) {
                userId = cursor.getString(columnIndex);
            }
            if (!TextUtils.isEmpty(id)) {
                // init fields
                this.mId = id;
                this.mSubjectId = subjectId;
                this.mName = name;
                this.mVideoNum = videoNum;
                this.mDownloadedVideoNum = downloadedVideoNum;
                this.mTotalSize = totalSize;
                this.mCreateDatetime = createDatetime;
                this.mPosterUrl = posterUrl;
                this.userId = userId;
            } else {
                throw new IllegalArgumentException("id from cursor illegal!");
            }
        } else {
            throw new NullPointerException("cursor illegal!");
        }
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(Table.COLUMN_NAME_OF_FIELD_ID, mId);
        values.put(Table.COLUMN_NAME_OF_FIELD_SUBJECT_ID, mSubjectId);
        values.put(Table.COLUMN_NAME_OF_FIELD_COURSE_NAME, mName);
        values.put(Table.COLUMN_NAME_OF_FIELD_VIDEO_NUM, mVideoNum);
        values.put(Table.COLUMN_NAME_OF_FIELD_DOWNLOADED_VIDEO_NUM, mDownloadedVideoNum);
        values.put(Table.COLUMN_NAME_OF_FIELD_TOTAL_SIZE, mTotalSize);
        values.put(Table.COLUMN_NAME_OF_FIELD_CREATE_DATETIME, mCreateDatetime);
        values.put(Table.COLUMN_NAME_OF_FIELD_POSTER_URL, mPosterUrl);
        values.put(Table.COLUMN_NAME_OF_FIELD_USER_ID, userId);
        return values;
    }

    /**
     * update DownloadFileInfo with new DownloadFileInfo
     *
     * @param courseInfo new DownloadFile
     */
    public void update(CourseInfo courseInfo) {
        if (!TextUtils.isEmpty(courseInfo.mId)) {
            this.mId = courseInfo.mId;
        }
        if (courseInfo.mSubjectId > 0 && courseInfo.mSubjectId != this.mSubjectId) {
            this.mSubjectId = courseInfo.mSubjectId;
        }
        if (UrlUtil.isUrl(courseInfo.mPosterUrl)) {
            this.mPosterUrl = courseInfo.mPosterUrl;
        }
        if (courseInfo.mVideoNum > 0 && courseInfo.mVideoNum != this
                .mVideoNum) {
            this.mVideoNum = courseInfo.mVideoNum;
        }
        if (courseInfo.mDownloadedVideoNum > 0 && courseInfo.mDownloadedVideoNum != this
                .mDownloadedVideoNum) {
            this.mDownloadedVideoNum = courseInfo.mDownloadedVideoNum;
        }
        if (!TextUtils.isEmpty(courseInfo.mName)) {
            this.mName = courseInfo.mName;
        }
        if (courseInfo.mTotalSize > 0) {
            this.mTotalSize = courseInfo.mTotalSize;
        }
        if (!TextUtils.isEmpty(courseInfo.mCreateDatetime)) {
            this.mCreateDatetime = courseInfo.mCreateDatetime;
        }
        if (!TextUtils.isEmpty(courseInfo.userId)) {
            this.userId = courseInfo.userId;
        }
    }
}
