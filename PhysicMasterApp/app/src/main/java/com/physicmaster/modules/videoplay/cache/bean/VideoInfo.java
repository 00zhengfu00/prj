package com.physicmaster.modules.videoplay.cache.bean;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.physicmaster.utils.UrlUtil;


/**
 * Created by huashigen on 2017/3/21.
 */

public class VideoInfo implements Parcelable {
    protected String id;
    protected String courseId;
    protected String name;
    protected long downloadedSize;
    protected long totalSize;
    protected int tsFileNum;
    protected int downloadedTsFileNum;
    protected String createDatetime;
    protected String expiresAtTime;
    protected String posterUrl;
    protected int order;
    protected int progress;
    private int state;//缓存状态0-等待缓存;1-缓存中;2-已缓存
    private int type;//视频类型,老版本只有知识点讲解视频和习题视频;新版本有预习视频,精讲视频,必练题视频
    private String userId;//用户id
    public static final int TYPE_PREVIEW = 1;
    public static final int TYPE_DEEP = 3;
    public static final int TYPE_REVIEW = 2;
    private String videoPath;//下载完视频的绝对路径
    public static final int STATE_ADD = 0;
    public static final int STATE_DOWNLOADING = 1;
    public static final int STATE_DOWNLOADED = 2;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getDownloadedTsFileNum() {
        return downloadedTsFileNum;
    }

    public void setDownloadedTsFileNum(int downloadedTsFileNum) {
        this.downloadedTsFileNum = downloadedTsFileNum;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDownloadedSize() {
        return downloadedSize;
    }

    public void setDownloadedSize(long downloadedSize) {
        this.downloadedSize = downloadedSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public int getTsFileNum() {
        return tsFileNum;
    }

    public void setTsFileNum(int tsFileNum) {
        this.tsFileNum = tsFileNum;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getExpiresAtTime() {
        return expiresAtTime;
    }

    public void setExpiresAtTime(String expiresAtTime) {
        this.expiresAtTime = expiresAtTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public VideoInfo() {
    }

    public VideoInfo(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            String id = null;
            String courseId = null;
            String userId = null;
            String name = "";
            int tsFileNum = 0;
            int downloadedsFileNum = 0;
            long downloadedSize = 0L;
            long totalSize = 0L;
            String createDatetime = null;
            String expiresAtTime = null;
            int state = -1;
            int type = 0;
            String posterUrl = null;
            int order = -1;
            String videoPath = null;

            int columnIndex = -1;
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_ID);
            if (columnIndex != -1) {
                id = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_COURSE_ID);
            if (columnIndex != -1) {
                courseId = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_VIDEO_NAME);
            if (columnIndex != -1) {
                name = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_TSFILE_NUM);
            if (columnIndex != -1) {
                tsFileNum = cursor.getInt(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_TOTAL_SIZE);
            if (columnIndex != -1) {
                totalSize = cursor.getLong(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_DOWNLOADED_SIZE);
            if (columnIndex != -1) {
                downloadedSize = cursor.getLong(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_CREATE_DATETIME);
            if (columnIndex != -1) {
                createDatetime = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_EXPIRE_AT_TIME);
            if (columnIndex != -1) {
                expiresAtTime = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_POSTER_URL);
            if (columnIndex != -1) {
                posterUrl = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_ORDER);
            if (columnIndex != -1) {
                order = cursor.getInt(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_DOWNLOADED_TSFILE_NUM);
            if (columnIndex != -1) {
                downloadedsFileNum = cursor.getInt(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_STATE);
            if (columnIndex != -1) {
                state = cursor.getInt(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_TYPE);
            if (columnIndex != -1) {
                type = cursor.getInt(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_USER_ID);
            if (columnIndex != -1) {
                userId = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_VIDEO_PATH);
            if (columnIndex != -1) {
                videoPath = cursor.getString(columnIndex);
            }
            if (!TextUtils.isEmpty(id)) {
                // init fields
                this.id = id;
                this.courseId = courseId;
                this.name = name;
                this.downloadedSize = downloadedSize;
                this.totalSize = totalSize;
                this.tsFileNum = tsFileNum;
                this.createDatetime = createDatetime;
                this.expiresAtTime = expiresAtTime;
                this.posterUrl = posterUrl;
                this.order = order;
                this.videoPath = videoPath;
                this.downloadedTsFileNum = downloadedsFileNum;
                this.state = state;
                this.type = type;
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
        values.put(Table.COLUMN_NAME_OF_FIELD_ID, id);
        values.put(Table.COLUMN_NAME_OF_FIELD_COURSE_ID, courseId);
        values.put(Table.COLUMN_NAME_OF_FIELD_VIDEO_NAME, name);
        values.put(Table.COLUMN_NAME_OF_FIELD_DOWNLOADED_SIZE, downloadedSize);
        values.put(Table.COLUMN_NAME_OF_FIELD_TOTAL_SIZE, totalSize);
        values.put(Table.COLUMN_NAME_OF_FIELD_TSFILE_NUM, tsFileNum);
        values.put(Table.COLUMN_NAME_OF_FIELD_DOWNLOADED_TSFILE_NUM, downloadedTsFileNum);
        values.put(Table.COLUMN_NAME_OF_FIELD_CREATE_DATETIME, createDatetime);
        values.put(Table.COLUMN_NAME_OF_FIELD_EXPIRE_AT_TIME, expiresAtTime);
        values.put(Table.COLUMN_NAME_OF_FIELD_POSTER_URL, posterUrl);
        values.put(Table.COLUMN_NAME_OF_FIELD_STATE, state);
        values.put(Table.COLUMN_NAME_OF_FIELD_TYPE, type);
        values.put(Table.COLUMN_NAME_OF_FIELD_USER_ID, userId);
        values.put(Table.COLUMN_NAME_OF_FIELD_VIDEO_PATH, videoPath);
        return values;
    }

    /**
     * update DownloadFileInfo with new DownloadFileInfo
     *
     * @param videoInfo new DownloadFile
     */
    public void update(VideoInfo videoInfo) {
        if (!TextUtils.isEmpty(videoInfo.getId())) {
            this.id = videoInfo.id;
        }
        if (!TextUtils.isEmpty(videoInfo.courseId)) {
            this.courseId = videoInfo.courseId;
        }
        if (UrlUtil.isUrl(videoInfo.posterUrl)) {
            this.posterUrl = videoInfo.posterUrl;
        }
        if (videoInfo.tsFileNum >= 0 && videoInfo.tsFileNum != this
                .tsFileNum) {
            this.tsFileNum = videoInfo.tsFileNum;
        }
        if (videoInfo.downloadedTsFileNum >= 0 && videoInfo.downloadedTsFileNum != this
                .downloadedTsFileNum) {
            this.downloadedTsFileNum = videoInfo.downloadedTsFileNum;
        }
        if (!TextUtils.isEmpty(videoInfo.name)) {
            this.name = videoInfo.name;
        }
        if (videoInfo.downloadedSize >= 0 && videoInfo.downloadedSize != this.downloadedSize) {
            this.downloadedSize = videoInfo.downloadedSize;
        }
        if (videoInfo.totalSize >= 0 && videoInfo.totalSize != this.totalSize) {
            this.totalSize = videoInfo.totalSize;
        }
        if (!TextUtils.isEmpty(videoInfo.createDatetime)) {
            this.createDatetime = videoInfo.createDatetime;
        }
        if (!TextUtils.isEmpty(videoInfo.expiresAtTime)) {
            this.expiresAtTime = videoInfo.expiresAtTime;
        }
        if (!TextUtils.isEmpty(videoInfo.userId)) {
            this.userId = videoInfo.userId;
        }
        if (videoInfo.order != -1) {
            this.order = videoInfo.order;
        }
        if (videoInfo.state != -1 && videoInfo.state != this.state) {
            this.state = videoInfo.state;
        }
        if (videoInfo.type != -1 && videoInfo.type != this.type) {
            this.type = videoInfo.type;
        }
        if (!TextUtils.isEmpty(videoInfo.videoPath)) {
            this.videoPath = videoInfo.videoPath;
        }
    }

    public static final class Table {
        /**
         * table name
         */
        public static final String TABLE_NAME_OF_VIDEO_INFO = "tb_video_info";
        /**
         * id field name
         */
        public static final String COLUMN_NAME_OF_FIELD_ID = "_id";
        /**
         * courseId field name
         */
        public static final String COLUMN_NAME_OF_FIELD_COURSE_ID = "course_id";
        /**
         * videoName field name
         */
        public static final String COLUMN_NAME_OF_FIELD_VIDEO_NAME = "name";
        /**
         * downloadedSize field name
         */
        public static final String COLUMN_NAME_OF_FIELD_DOWNLOADED_SIZE = "downloaded_size";
        /**
         * totalSize field name
         */
        public static final String COLUMN_NAME_OF_FIELD_TOTAL_SIZE = "total_size";
        /**
         * tsfileNum field name
         */
        public static final String COLUMN_NAME_OF_FIELD_TSFILE_NUM = "tsfile_num";
        /**
         * downloadedTsFile field name
         */
        public static final String COLUMN_NAME_OF_FIELD_DOWNLOADED_TSFILE_NUM =
                "downloaded_tsfile_num";
        /**
         * create download datetime
         */
        public static final String COLUMN_NAME_OF_FIELD_CREATE_DATETIME = "create_datetime";
        /**
         * expire at datetime
         */
        public static final String COLUMN_NAME_OF_FIELD_EXPIRE_AT_TIME = "expire_at_time";
        /**
         * posterUrl field name
         */
        public static final String COLUMN_NAME_OF_FIELD_POSTER_URL = "poster_url";
        /**
         * cacheSate field name
         */
        public static final String COLUMN_NAME_OF_FIELD_STATE = "state";
        /**
         * videoType field name
         */
        public static final String COLUMN_NAME_OF_FIELD_TYPE = "type";
        /**
         * userId field name
         */
        public static final String COLUMN_NAME_OF_FIELD_USER_ID = "user_id";
        /**
         * order field name
         */
        public static final String COLUMN_NAME_OF_FIELD_ORDER = "video_order";
        /**
         * videoPath field name
         */
        public static final String COLUMN_NAME_OF_FIELD_VIDEO_PATH = "video_path";

        /**
         * the sql to create table
         */
        public static final String getCreateTableSql() {

            String createTableSql = "CREATE TABLE IF NOT EXISTS " //
                    + TABLE_NAME_OF_VIDEO_INFO //
                    + "(" + COLUMN_NAME_OF_FIELD_ID + " TEXT PRIMARY KEY,"//
                    + COLUMN_NAME_OF_FIELD_VIDEO_NAME + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_DOWNLOADED_SIZE + " BIGINT DEFAULT 0,"//
                    + COLUMN_NAME_OF_FIELD_COURSE_ID + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_TSFILE_NUM + " INTEGER DEFAULT 0,"//
                    + COLUMN_NAME_OF_FIELD_DOWNLOADED_TSFILE_NUM + " INTEGER DEFAULT 0,"//
                    + COLUMN_NAME_OF_FIELD_TOTAL_SIZE + " BIGINT DEFAULT 0,"//
                    + COLUMN_NAME_OF_FIELD_POSTER_URL + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_USER_ID + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_ORDER + " INTEGER,"//
                    + COLUMN_NAME_OF_FIELD_STATE + " INTEGER DEFAULT 0,"//
                    + COLUMN_NAME_OF_FIELD_TYPE + " INTEGER DEFAULT 0,"//
                    + COLUMN_NAME_OF_FIELD_VIDEO_PATH + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_EXPIRE_AT_TIME + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_CREATE_DATETIME + " TEXT" + ")";//

            return createTableSql;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.courseId);
        dest.writeString(this.name);
        dest.writeLong(this.downloadedSize);
        dest.writeLong(this.totalSize);
        dest.writeInt(this.tsFileNum);
        dest.writeInt(this.downloadedTsFileNum);
        dest.writeString(this.createDatetime);
        dest.writeString(this.expiresAtTime);
        dest.writeString(this.posterUrl);
        dest.writeInt(this.order);
        dest.writeInt(this.state);
        dest.writeInt(this.type);
        dest.writeString(this.videoPath);
        dest.writeString(this.userId);
    }

    protected VideoInfo(Parcel in) {
        this.id = in.readString();
        this.courseId = in.readString();
        this.name = in.readString();
        this.downloadedSize = in.readLong();
        this.totalSize = in.readLong();
        this.tsFileNum = in.readInt();
        this.downloadedTsFileNum = in.readInt();
        this.createDatetime = in.readString();
        this.expiresAtTime = in.readString();
        this.posterUrl = in.readString();
        this.order = in.readInt();
        this.state = in.readInt();
        this.type = in.readInt();
        this.videoPath = in.readString();
        this.userId = in.readString();
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>
            () {
        @Override
        public VideoInfo createFromParcel(Parcel source) {
            return new VideoInfo(source);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };
}
