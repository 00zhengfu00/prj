package org.wlf.filedownloader.file_download;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import org.wlf.filedownloader.base.BaseUrlFileInfo;
import org.wlf.filedownloader.base.Status;
import org.wlf.filedownloader.util.DateUtil;
import org.wlf.filedownloader.util.FileUtil;
import org.wlf.filedownloader.util.UrlUtil;

import java.io.File;
import java.util.Date;

/**
 * download file Model,synchronous with database table
 * <br/>
 * ts文件信息模型，跟数据库一致
 *
 * @author huashigen
 */
public class TsFileInfo extends BaseUrlFileInfo {

    /**
     * {@link TsFileInfo} database table info
     */
    public static final class Table {

        /**
         * table name
         */
        public static final String TABLE_NAME_OF_DOWNLOAD_FILE = "tb_download_file";

        /**
         * id field name
         */
        public static final String COLUMN_NAME_OF_FIELD_ID = "_id";
        /**
         * url field name
         */
        public static final String COLUMN_NAME_OF_FIELD_URL = "url";
        /**
         * downloadedSize field name
         */
        public static final String COLUMN_NAME_OF_FIELD_DOWNLOADED_SIZE = "downloaded_size";
        /**
         * fileSize field name
         */
        public static final String COLUMN_NAME_OF_FIELD_FILE_SIZE = "file_size";
        /**
         * eTag field name
         */
        public static final String COLUMN_NAME_OF_FIELD_E_TAG = "e_tag";
        /**
         * last modified datetime(in server) field name
         */
        public static final String COLUMN_NAME_OF_FIELD_LAST_MODIFIED = "last_modified";
        /**
         * acceptRangeType field name
         */
        public static final String COLUMN_NAME_OF_FIELD_ACCEPT_RANGE_TYPE = "accept_range_type";
        /**
         * fileSize field name
         */
        public static final String COLUMN_NAME_OF_FIELD_FILE_DIR = "file_dir";
        /**
         * tempFileName field name
         */
        public static final String COLUMN_NAME_OF_FIELD_TEMP_FILE_NAME = "temp_file_name";
        /**
         * fileName field name
         */
        public static final String COLUMN_NAME_OF_FIELD_FILE_NAME = "file_name";
        /**
         * status field name
         */
        public static final String COLUMN_NAME_OF_FIELD_STATUS = "status";
        /**
         * create download datetime
         */
        public static final String COLUMN_NAME_OF_FIELD_CREATE_DATETIME = "create_datetime";
        /**
         * 父id
         */
        public static final String COLUMN_NAME_OF_FIELD_PARENT_ID = "parent_id";

        /**
         * the sql to create table
         */
        public static final String getCreateTableSql() {

            String createTableSql = "CREATE TABLE IF NOT EXISTS " //
                    + TABLE_NAME_OF_DOWNLOAD_FILE //

                    + "(" + COLUMN_NAME_OF_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"//
                    + COLUMN_NAME_OF_FIELD_URL + " TEXT UNIQUE,"//
                    + COLUMN_NAME_OF_FIELD_DOWNLOADED_SIZE + " INTEGER,"//
                    + COLUMN_NAME_OF_FIELD_FILE_SIZE + " INTEGER,"//
                    + COLUMN_NAME_OF_FIELD_E_TAG + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_LAST_MODIFIED + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_ACCEPT_RANGE_TYPE + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_FILE_DIR + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_TEMP_FILE_NAME + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_FILE_NAME + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_STATUS + " INTEGER,"//
                    + COLUMN_NAME_OF_FIELD_PARENT_ID + " TEXT,"//
                    + COLUMN_NAME_OF_FIELD_CREATE_DATETIME + " TEXT" + ")";//

            return createTableSql;
        }
    }

    /**
     * temp file suffix
     */
    private static final String TEMP_FILE_SUFFIX = "temp";

    /**
     * id
     */
    private Integer mId;
    /**
     * downloadedSize
     */
    private long mDownloadedSize;
    /**
     * TempFileName
     */
    private String mTempFileName;
    /**
     * download status，ref{@link Status}
     */
    private int mStatus = Status.DOWNLOAD_STATUS_UNKNOWN;
    /**
     * 文件父Id
     */
    protected String mParentId;

    @SuppressWarnings("unused")
    private TsFileInfo() {
    }

    /**
     * constructor of HttpDownloader, use {@link DetectUrlFileInfo} to create
     *
     * @param
     */
    public TsFileInfo(String mUrl, String mFileName, long mFileSize, String mFileDir, String mParentId) {
        this.mUrl = mUrl;
        this.mFileName = mFileName;
        this.mFileSize = mFileSize;
        this.mFileDir = mFileDir;
        // this.status = Status.DOWNLOAD_STATUS_WAITING;// download status
        this.mCreateDatetime = DateUtil.date2String_yyyy_MM_dd_HH_mm_ss(new Date());
        this.mParentId = mParentId;
    }

    /**
     * constructor of HttpDownloader, use {@link Cursor} to create
     *
     * @param cursor database cursor
     */
    public TsFileInfo(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            int id = -1;
            String url = null;
            long downloadedSize = 0;
            long fileSize = 0;
            String eTag = null;
            String lastModified = null;
            String acceptRangeType = null;
            String fileDir = null;
            String tempFileName = null;
            String fileName = null;
            int status = Status.DOWNLOAD_STATUS_UNKNOWN;
            String createDatetime = null;
            String parentId = null;

            int columnIndex = -1;
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_ID);
            if (columnIndex != -1) {
                id = cursor.getInt(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_URL);
            if (columnIndex != -1) {
                url = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_DOWNLOADED_SIZE);
            if (columnIndex != -1) {
                downloadedSize = cursor.getLong(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_FILE_SIZE);
            if (columnIndex != -1) {
                fileSize = cursor.getLong(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_E_TAG);
            if (columnIndex != -1) {
                eTag = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_LAST_MODIFIED);
            if (columnIndex != -1) {
                lastModified = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_ACCEPT_RANGE_TYPE);
            if (columnIndex != -1) {
                acceptRangeType = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_FILE_DIR);
            if (columnIndex != -1) {
                fileDir = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_TEMP_FILE_NAME);
            if (columnIndex != -1) {
                tempFileName = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_FILE_NAME);
            if (columnIndex != -1) {
                fileName = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_STATUS);
            if (columnIndex != -1) {
                status = cursor.getInt(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_CREATE_DATETIME);
            if (columnIndex != -1) {
                createDatetime = cursor.getString(columnIndex);
            }
            columnIndex = cursor.getColumnIndex(Table.COLUMN_NAME_OF_FIELD_PARENT_ID);
            if (columnIndex != -1) {
                parentId = cursor.getString(columnIndex);
            }
            if (id > 0 && !TextUtils.isEmpty(url)) {
                // init fields
                this.mId = id;
                this.mUrl = url;
                this.mDownloadedSize = downloadedSize;
                this.mFileSize = fileSize;
                this.mETag = eTag;
                this.mLastModified = lastModified;
                this.mAcceptRangeType = acceptRangeType;
                this.mFileDir = fileDir;
                this.mTempFileName = tempFileName;
                this.mFileName = fileName;
                this.mStatus = status;
                this.mCreateDatetime = createDatetime;
                this.mParentId = parentId;
            } else {
                throw new IllegalArgumentException("id or url from cursor illegal!");
            }
        } else {
            throw new NullPointerException("cursor illegal!");
        }
    }

    /**
     * update DownloadFileInfo with new DownloadFileInfo
     *
     * @param tsFileInfo new DownloadFile
     */
    public void update(TsFileInfo tsFileInfo) {
        if (tsFileInfo.mId != null && tsFileInfo.mId > 0) {
            this.mId = tsFileInfo.mId;
        }
        if (UrlUtil.isUrl(tsFileInfo.mUrl)) {
            this.mUrl = tsFileInfo.mUrl;
        }
        if (tsFileInfo.mDownloadedSize > 0 && tsFileInfo.mDownloadedSize != this
                .mDownloadedSize) {
            this.mDownloadedSize = tsFileInfo.mDownloadedSize;
        }
        if (tsFileInfo.mFileSize > 0 && tsFileInfo.mFileSize != this.mFileSize) {
            this.mFileSize = tsFileInfo.mFileSize;
        }
        if (!TextUtils.isEmpty(tsFileInfo.mETag)) {
            this.mETag = tsFileInfo.mETag;
        }
        if (!TextUtils.isEmpty(tsFileInfo.mLastModified)) {
            this.mLastModified = tsFileInfo.mLastModified;
        }
        if (!TextUtils.isEmpty(tsFileInfo.mAcceptRangeType)) {
            this.mAcceptRangeType = tsFileInfo.mAcceptRangeType;
        }
        if (FileUtil.isFilePath(tsFileInfo.mFileDir)) {
            this.mFileDir = tsFileInfo.mFileDir;
        }
        if (!TextUtils.isEmpty(tsFileInfo.mTempFileName)) {
            this.mTempFileName = tsFileInfo.mTempFileName;
        }
        if (!TextUtils.isEmpty(tsFileInfo.mFileName)) {
            this.mFileName = tsFileInfo.mFileName;
        }
        if (tsFileInfo.mStatus != this.mStatus) {
            this.mStatus = tsFileInfo.mStatus;
        }
        if (!TextUtils.isEmpty(tsFileInfo.mCreateDatetime)) {
            this.mCreateDatetime = tsFileInfo.mCreateDatetime;
        }
        if (!TextUtils.isEmpty(tsFileInfo.mParentId)) {
            this.mParentId = tsFileInfo.mParentId;
        }
    }

    /**
     * get ContentValues for all fields
     */
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(Table.COLUMN_NAME_OF_FIELD_URL, mUrl);
        values.put(Table.COLUMN_NAME_OF_FIELD_DOWNLOADED_SIZE, mDownloadedSize);
        values.put(Table.COLUMN_NAME_OF_FIELD_FILE_SIZE, mFileSize);
        values.put(Table.COLUMN_NAME_OF_FIELD_E_TAG, mETag);
        values.put(Table.COLUMN_NAME_OF_FIELD_LAST_MODIFIED, mLastModified);
        values.put(Table.COLUMN_NAME_OF_FIELD_ACCEPT_RANGE_TYPE, mAcceptRangeType);
        values.put(Table.COLUMN_NAME_OF_FIELD_FILE_DIR, mFileDir);
        values.put(Table.COLUMN_NAME_OF_FIELD_TEMP_FILE_NAME, mTempFileName);
        values.put(Table.COLUMN_NAME_OF_FIELD_FILE_NAME, mFileName);
        values.put(Table.COLUMN_NAME_OF_FIELD_STATUS, mStatus);
        values.put(Table.COLUMN_NAME_OF_FIELD_PARENT_ID, mParentId);
        values.put(Table.COLUMN_NAME_OF_FIELD_CREATE_DATETIME, mCreateDatetime);
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (!TextUtils.isEmpty(mUrl)) {
            if (o instanceof TsFileInfo) {
                TsFileInfo other = (TsFileInfo) o;
                return mUrl.equals(other.mUrl);
            }
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        if (!TextUtils.isEmpty(mUrl)) {
            return mUrl.hashCode();
        }
        return super.hashCode();
    }

    /**
     * set id
     *
     * @param id
     */
    public void setId(Integer id) {
        mId = id;
    }

    /**
     * set download size
     */
    public void setDownloadedSize(long downloadedSize) {
        this.mDownloadedSize = downloadedSize;
    }

    /**
     * set download status
     */
    public void setStatus(int status) {
        this.mStatus = status;
    }

    /**
     * set save file dir, use for package access
     */
    public void setFileDir(String fileDir) {
        this.mFileDir = fileDir;
    }

    /**
     * set file name, use for package access
     */
    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    // getters

    /**
     * get id
     *
     * @return return null means id illegal
     */
    public Integer getId() {
        return mId;
    }

    /**
     * get Downloaded Size
     *
     * @return Downloaded Size
     * @deprecated use {@link #getDownloadedSizeLong} instead
     */
    @Deprecated
    public int getDownloadedSize() {
        return (int) mDownloadedSize;
    }

    /**
     * get Downloaded Size
     *
     * @return Downloaded Size
     */
    public long getDownloadedSizeLong() {
        return mDownloadedSize;
    }

    /**
     * get TempFileName
     *
     * @return TempFileName
     */
    public String getTempFileName() {
        return mTempFileName;
    }

    /**
     * get download status
     *
     * @return download status,ref{@link Status}
     */
    public int getStatus() {
        return mStatus;
    }

    // other getters
    public String getParentId() {
        return mParentId;
    }

    /**
     * get TempFilePath
     *
     * @return TempFilePath
     */
    public String getTempFilePath() {
        return getFileDir() + File.separator + mTempFileName;
    }

    @Override
    public String toString() {
        return "DownloadFileInfo{" +
                "mId=" + mId +
                ", mDownloadedSize=" + mDownloadedSize +
                ", mTempFileName='" + mTempFileName + '\'' +
                ", mStatus=" + mStatus +
                ", mParentId=" + mParentId +
                "} " + super.toString();
    }

}
