package com.physicmaster.modules.videoplay.cache.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.physicmaster.modules.videoplay.cache.DownloadFileChangeConfiguration;
import com.physicmaster.modules.videoplay.cache.Log;
import com.physicmaster.modules.videoplay.cache.OnDownloadFileChangeListener;
import com.physicmaster.modules.videoplay.cache.Status;
import com.physicmaster.modules.videoplay.cache.bean.DetectUrlFileInfo;
import com.physicmaster.modules.videoplay.cache.file.DownloadFileMover;
import com.physicmaster.modules.videoplay.cache.intface.ContentDbDao;
import com.physicmaster.modules.videoplay.cache.intface.DownloadFileDeleter;
import com.physicmaster.modules.videoplay.cache.intface.DownloadFileRenamer;
import com.physicmaster.modules.videoplay.cache.intface.DownloadRecorder;
import com.physicmaster.utils.CollectionUtil;
import com.physicmaster.utils.ContentValuesUtil;
import com.physicmaster.utils.DownloadFileUtil;
import com.physicmaster.utils.FileUtil;
import com.physicmaster.utils.UrlUtil;
import com.physicmaster.modules.videoplay.cache.OnDownloadFileChangeListener.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * a class to manage and record download files cache
 * <br/>
 * 下载文件缓存器
 *
 * @author wlf(Andy)
 * @email 411086563@qq.com
 */
public class DownloadCacher implements DownloadRecorder, DownloadFileMover, DownloadFileDeleter,
        DownloadFileRenamer {

    private static final String TAG = DownloadCacher.class.getSimpleName();

    // db helper
    private DownloadFileDbHelper mDownloadFileDbHelper;

    // download files memory cache
//    private Map<String, DownloadFileInfo> mDownloadFileInfoMap = new HashMap<String,
//            DownloadFileInfo>();

    private Object mModifyLock = new Object();// modify lock

    // download file change observer
    private DownloadFileChangeObserver mDownloadFileChangeObserver;

    // --------------------------------------lifecycle &
    // others--------------------------------------

    /**
     * constructor of DownloadFileCacher
     *
     * @param context Context
     */
    public DownloadCacher(Context context) {
        mDownloadFileDbHelper = new DownloadFileDbHelper(context);
        mDownloadFileChangeObserver = new DownloadFileChangeObserver();
        initDownloadFileInfoMapFromDb();
    }

    /**
     * read all DownloadFiles from database
     */
    private void initDownloadFileInfoMapFromDb() {
        // read from database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(BaseContentDbHelper.DownloadFileInfo.Table
                .TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return;
        }

        // query all
        Cursor cursor = dao.query(null, null, null, null);
        List<BaseContentDbHelper.DownloadFileInfo> downloadFileInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (CollectionUtil.isEmpty(downloadFileInfos)) {
            return;
        }

        // cache in memory
        for (BaseContentDbHelper.DownloadFileInfo downloadFileInfo : downloadFileInfos) {
            if (!DownloadFileUtil.isLegal(downloadFileInfo)) {
                continue;
            }
            synchronized (mModifyLock) {// lock
//                mDownloadFileInfoMap.put(downloadFileInfo.getUrl(), downloadFileInfo);
            }
        }
    }

    public List<BaseContentDbHelper.DownloadFileInfo> getDownloadInfoByParentId(String parentId) {
        // read from database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(BaseContentDbHelper.DownloadFileInfo.Table
                .TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return null;
        }

        // query all
        Cursor cursor = dao.query(null, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_PARENT_ID + "= ?", new
                String[]{parentId}, null);
        List<BaseContentDbHelper.DownloadFileInfo> downloadFileInfos = getDownloadFilesFromCursor(cursor);
        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (CollectionUtil.isEmpty(downloadFileInfos)) {
            return null;
        }

        // cache in memory
        for (BaseContentDbHelper.DownloadFileInfo downloadFileInfo : downloadFileInfos) {
            if (!DownloadFileUtil.isLegal(downloadFileInfo)) {
                continue;
            }
            synchronized (mModifyLock) {// lock
//                mDownloadFileInfoMap.put(downloadFileInfo.getUrl(), downloadFileInfo);
            }
        }
        return downloadFileInfos;
    }

    /**
     * check the download file status
     */
    private void checkDownloadFileStatus(BaseContentDbHelper.DownloadFileInfo downloadFileInfo) {

        // FIXME this codes below may case performance problem

        try {
            if (!DownloadFileUtil.isLegal(downloadFileInfo) || downloadFileInfo
                    .getDownloadedSizeLong() <= 0) {
                return;// not need to check
            }

            // check whether file exist
            String saveFilePath = downloadFileInfo.getFilePath();
            String tempFilePath = downloadFileInfo.getTempFilePath();

            File saveFile = null;
            File tempFile = null;

            if (FileUtil.isFilePath(saveFilePath)) {
                saveFile = new File(saveFilePath);
            }
            if (FileUtil.isFilePath(tempFilePath)) {
                tempFile = new File(tempFilePath);
            }

            // if the status not exist, may be now available
            if (downloadFileInfo.getStatus() == Status.DOWNLOAD_STATUS_FILE_NOT_EXIST) {
                boolean handled = false;
                // try to recovery complete status
                if (saveFile != null && saveFile.length() == downloadFileInfo
                        .getDownloadedSizeLong() &&
                        downloadFileInfo.getDownloadedSizeLong() == downloadFileInfo
                                .getFileSizeLong()) {
                    // file completed
                    Log.d(TAG, "checkDownloadFileStatus，文件已下载完，但当前状态为文件不存在，需要更改状态为已下载完成，url:" +
                            downloadFileInfo
                                    .getUrl());

                    handled = updateDownloadFileWithStatus(downloadFileInfo, Status
                            .DOWNLOAD_STATUS_COMPLETED);
                }

                if (!handled) {
                    // try to recovery error status
                    if ((tempFile != null && tempFile.exists() && tempFile.length() > 0) ||
                            (saveFile != null &&
                                    saveFile.exists() && saveFile.length() > 0)) {
                        // file error
                        Log.d(TAG,
                                "checkDownloadFileStatus，文件未下载完/下载文件出现问题，但当前状态为文件不存在，需要更改状态为下载出错，url:" +
                                        downloadFileInfo.getUrl());

                        handled = updateDownloadFileWithStatus(downloadFileInfo, Status
                                .DOWNLOAD_STATUS_ERROR);
                    }
                }
            } else {

                boolean fileExist = true;

                // completed status
                if (DownloadFileUtil.isCompleted(downloadFileInfo)) {
                    if (saveFile != null) {
                        // save file not exists
                        if (!saveFile.exists()) {
                            // check whether tempFile size equals total file size
                            if (tempFile != null && tempFile.exists() && tempFile.length() ==
                                    downloadFileInfo
                                            .getDownloadedSizeLong() && downloadFileInfo
                                    .getDownloadedSizeLong() ==
                                    downloadFileInfo.getFileSizeLong()) {
                                // however the temp file is finished, so ignore
                                // FIXME whether need to move tempFile to save file ?
                            } else {
                                Log.d(TAG,
                                        "checkDownloadFileStatus，文件下载完成，但是文件不存在了，需要更改状态为文件不存在，url:" +
                                                downloadFileInfo.getUrl());

                                fileExist = false;
                            }
                        }
                    }
                }
                // other status
                else {
                    if (tempFile != null && tempFile.exists() && tempFile.length() > 0) {
                        // temp file exist, so ignore
                    } else {

                        Log.d(TAG, "checkDownloadFileStatus，文件没有下载完成，但是文件不存在了，需要更改状态为文件不存在，url:"
                                + downloadFileInfo
                                .getUrl());

                        fileExist = false;
                    }
                }

                if (!fileExist) {
                    // file not exist
                    updateDownloadFileWithStatus(downloadFileInfo, Status
                            .DOWNLOAD_STATUS_FILE_NOT_EXIST);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // can ignore
        }
    }

    /**
     * release the cacher
     */
    public void release() {
        synchronized (mModifyLock) {// lock
            // free memory cache
//            mDownloadFileInfoMap.clear();
            mDownloadFileChangeObserver.release();
            // close the database
            if (mDownloadFileDbHelper != null) {
                mDownloadFileDbHelper.close();
            }
        }
    }

    // --------------------------------------register & unregister
    // listeners--------------------------------------

    /**
     * register a DownloadFileChangeListener with Configuration
     *
     * @param onDownloadFileChangeListener    OnDownloadFileChangeListener impl
     * @param downloadFileChangeConfiguration Configuration for the OnDownloadFileChangeListener
     *                                        impl
     * @since 0.3.0
     */
    public void registerDownloadFileChangeListener(OnDownloadFileChangeListener
                                                           onDownloadFileChangeListener,
                                                   DownloadFileChangeConfiguration
                                                           downloadFileChangeConfiguration) {
        mDownloadFileChangeObserver.addOnDownloadFileChangeListener(onDownloadFileChangeListener,
                downloadFileChangeConfiguration);
    }

    /**
     * unregister an OnDownloadFileChangeListener
     *
     * @param onDownloadFileChangeListener registered OnDownloadFileChangeListener impl
     */
    public void unregisterDownloadFileChangeListener(OnDownloadFileChangeListener
                                                             onDownloadFileChangeListener) {
        mDownloadFileChangeObserver.removeOnDownloadFileChangeListener
                (onDownloadFileChangeListener);
    }

    // --------------------------------------notify caller--------------------------------------

    /**
     * notifyDownloadFileCreated
     */
    private void notifyDownloadFileCreated(BaseContentDbHelper.DownloadFileInfo downloadFileInfo) {
        // notify observer
        if (mDownloadFileChangeObserver != null) {
            mDownloadFileChangeObserver.onDownloadFileCreated(downloadFileInfo);
        }
    }

    /**
     * notifyDownloadFileUpdated
     */
    private void notifyDownloadFileUpdated(BaseContentDbHelper.DownloadFileInfo downloadFileInfo, OnDownloadFileChangeListener.Type type) {
        // notify observer
        if (mDownloadFileChangeObserver != null) {
            mDownloadFileChangeObserver.onDownloadFileUpdated(downloadFileInfo, type);
        }
    }

    /**
     * notifyDownloadFileDeleted
     */
    private void notifyDownloadFileDeleted(BaseContentDbHelper.DownloadFileInfo downloadFileInfo) {
        // notify observer
        if (mDownloadFileChangeObserver != null) {
            mDownloadFileChangeObserver.onDownloadFileDeleted(downloadFileInfo);
        }
    }

    // --------------------------------------CRUD, both memory and
    // database--------------------------------------

    /**
     * add a new DownloadFile
     */
    public boolean addDownloadFile(BaseContentDbHelper.DownloadFileInfo downloadFileInfo) {

        if (!DownloadFileUtil.isLegal(downloadFileInfo)) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(BaseContentDbHelper.DownloadFileInfo.Table
                .TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return false;
        }

        ContentValues values = downloadFileInfo.getContentValues();
        if (ContentValuesUtil.isEmpty(values)) {
            return false;
        }

        String url = downloadFileInfo.getUrl();

        BaseContentDbHelper.DownloadFileInfo downloadFileInfoExist = getDownloadFile(url);
        // exist the download file, update
        if (DownloadFileUtil.isLegal(downloadFileInfoExist) && downloadFileInfoExist !=
                downloadFileInfo) {

            OnDownloadFileChangeListener.Type type = OnDownloadFileChangeListener.Type.OTHER;
            int changeCount = 0;

            if (downloadFileInfoExist.getStatus() != downloadFileInfo.getStatus()) {
                changeCount++;
                type = Type.DOWNLOAD_STATUS;
            }
            if (downloadFileInfoExist.getDownloadedSizeLong() != downloadFileInfo
                    .getDownloadedSizeLong()) {
                changeCount++;
                type = Type.DOWNLOADED_SIZE;
            }
            if (downloadFileInfoExist.getFileDir() != null && !downloadFileInfoExist.getFileDir()
                    .equals
                            (downloadFileInfo.getFileDir())) {
                changeCount++;
                type = Type.SAVE_DIR;
            }
            if (downloadFileInfoExist.getFileName() != null && !downloadFileInfoExist.getFileName
                    ().equals
                    (downloadFileInfo.getFileName())) {
                changeCount++;
                type = Type.SAVE_FILE_NAME;
            }

            if (changeCount > 1) {
                type = Type.OTHER;
            }

            synchronized (mModifyLock) {// lock
                // update the downloadFileInfoExist with the new downloadFileInfo
                downloadFileInfoExist.update(downloadFileInfo);

                boolean isSucceed = updateDownloadFileInternal(downloadFileInfoExist, false, type);

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
                // succeed, update memory cache
                downloadFileInfo.setId(new Integer((int) id));
//                mDownloadFileInfoMap.put(url, downloadFileInfo);
                // notify caller
                notifyDownloadFileCreated(downloadFileInfo);
                return true;
            }
        }
        return false;
    }

    /**
     * update an exist DownloadFile
     */
    private boolean updateDownloadFileInternal(BaseContentDbHelper.DownloadFileInfo downloadFileInfo, boolean
            lockInternal, Type
                                                       notifyType) {

        if (!DownloadFileUtil.isLegal(downloadFileInfo)) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(BaseContentDbHelper.DownloadFileInfo.Table
                .TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return false;
        }

        ContentValues values = downloadFileInfo.getContentValues();
        if (ContentValuesUtil.isEmpty(values)) {
            return false;
        }

        String url = downloadFileInfo.getUrl();

        if (lockInternal) {// need internal lock
            synchronized (mModifyLock) {// lock
                int result = dao.update(values, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID +
                        "= ?", new
                        String[]{downloadFileInfo.getId() + ""});
                if (result == 1) {
                    // succeed, update memory cache
//                    if (mDownloadFileInfoMap.containsKey(url)) {
//                        DownloadFileInfo downloadFileInfoInMap = mDownloadFileInfoMap.get(url);
//                        downloadFileInfoInMap.update(downloadFileInfo);
//                    } else {
//                        mDownloadFileInfoMap.put(url, downloadFileInfo);
//                    }
                    // notify caller
                    notifyDownloadFileUpdated(downloadFileInfo, notifyType);
                    return true;
                }
            }
        } else {// not lock
            int result = dao.update(values, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= " +
                    "?", new
                    String[]{downloadFileInfo.getId() + ""});
            if (result == 1) {
                // succeed,update memory cache
//                if (mDownloadFileInfoMap.containsKey(url)) {
//                    DownloadFileInfo downloadFileInfoInMap = mDownloadFileInfoMap.get(url);
//                    downloadFileInfoInMap.update(downloadFileInfo);
//                } else {
//                    mDownloadFileInfoMap.put(url, downloadFileInfo);
//                }
                // notify caller
                notifyDownloadFileUpdated(downloadFileInfo, notifyType);
                return true;
            }
        }
        return false;
    }

    /**
     * update a DownloadFile with status
     */
    private boolean updateDownloadFileWithStatus(BaseContentDbHelper.DownloadFileInfo downloadFileInfo, int newStatus) {
        synchronized (mModifyLock) {// lock
            int oldStatus = downloadFileInfo.getStatus();
            downloadFileInfo.setStatus(newStatus);
            boolean isSucceed = updateDownloadFileInternal(downloadFileInfo, false, Type
                    .DOWNLOAD_STATUS);
            if (!isSucceed) {
                // rollback
                downloadFileInfo.setStatus(oldStatus);
            } else {
                return true;
            }
            return false;
        }
    }

    /**
     * delete a DownloadFile
     */
    private boolean deleteDownloadFile(BaseContentDbHelper.DownloadFileInfo downloadFileInfo) {

        if (!DownloadFileUtil.isLegal(downloadFileInfo)) {
            return false;
        }

        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(BaseContentDbHelper.DownloadFileInfo.Table
                .TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return false;
        }

        String url = downloadFileInfo.getUrl();

        synchronized (mModifyLock) {// lock
            int result = dao.delete(BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_ID + "= ?", new
                    String[]{downloadFileInfo.getId() + ""});
            if (result == 1) {
                // succeed,update memory cache
//                mDownloadFileInfoMap.remove(url);
                // notify caller
                notifyDownloadFileDeleted(downloadFileInfo);
                return true;
            } else {
                // try to delete by url
                result = dao.delete(BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_URL + "= ?", new String[]{url + ""});
                if (result == 1) {
                    // succeed, update memory cache
//                    mDownloadFileInfoMap.remove(url);
                    // notify caller
                    notifyDownloadFileDeleted(downloadFileInfo);
                    return true;
                }
            }
        }
        return false;
    }

    // --------------------------------------getters--------------------------------------

    /**
     * get DownloadFile by savePath
     *
     * @param savePath            the path of the file saved in
     * @param includeTempFilePath true means try use the savePath as temp file savePath if can
     *                            not get DownloadFile
     *                            by savePath
     * @return DownloadFile
     */
    public BaseContentDbHelper.DownloadFileInfo getDownloadFileBySavePath(String savePath, boolean
            includeTempFilePath) {

        // FIXME this codes below may case performance problem

        if (!FileUtil.isFilePath(savePath)) {
            return null;
        }

        BaseContentDbHelper.DownloadFileInfo downloadFileInfo = null;

        // look up the memory cache
//        Set<Entry<String, DownloadFileInfo>> set = mDownloadFileInfoMap.entrySet();
//        Iterator<Entry<String, DownloadFileInfo>> iterator = set.iterator();
//        while (iterator.hasNext()) {
//            Entry<String, DownloadFileInfo> entry = iterator.next();
//            if (entry == null) {
//                continue;
//            }
//            DownloadFileInfo info = entry.getValue();
//            if (info == null) {
//                continue;
//            }
//            String filePath = info.getFilePath();
//            if (TextUtils.isEmpty(filePath)) {
//                continue;
//            }
//
//            if (filePath.equals(savePath)) {
//                downloadFileInfo = info;// find in memory cache
//                break;
//            }
//        }

        // find in memory cache
        if (downloadFileInfo == null) {
            // try to find in database
            ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(BaseContentDbHelper.DownloadFileInfo.Table
                    .TABLE_NAME_OF_DOWNLOAD_FILE);
            if (dao == null) {
                return null;
            }

            int separatorIndex = savePath.lastIndexOf(File.separator);
            if (separatorIndex == -1) {// not a file path
                return null;
            }

            String fileSaveDir = savePath.substring(0, separatorIndex);
            String fileSaveName = savePath.substring(separatorIndex + 1, savePath.length());

            Cursor cursor = dao.query(null, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_FILE_DIR
                    + "= ? AND " +
                    BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_FILE_NAME + "= ?", new
                    String[]{fileSaveDir,
                    fileSaveName}, null);
            if (cursor != null && cursor.moveToFirst()) {
                downloadFileInfo = new BaseContentDbHelper.DownloadFileInfo(cursor);
            }

            // close the cursor
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            if (downloadFileInfo == null) {
                if (includeTempFilePath) {
                    // try use temp file to query
                    cursor = dao.query(null, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_FILE_DIR
                            + "= ? AND " +
                            BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_TEMP_FILE_NAME + "= ?", new
                            String[]{fileSaveDir,
                            fileSaveName}, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        downloadFileInfo = new BaseContentDbHelper.DownloadFileInfo(cursor);
                    }

                    // close the cursor
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }

            if (downloadFileInfo == null) {
                return null;
            }

            String url = downloadFileInfo.getUrl();
            if (UrlUtil.isUrl(url)) {
                synchronized (mModifyLock) {// lock
                    // cache in memory
//                    mDownloadFileInfoMap.put(url, downloadFileInfo);
//                    downloadFileInfo = mDownloadFileInfoMap.get(url);
                }
            }
        }

        checkDownloadFileStatus(downloadFileInfo);

        return downloadFileInfo;
    }

    /**
     * get DownloadFiles by database cursor
     *
     * @param cursor database cursor
     * @return DownloadFiles
     */
    private List<BaseContentDbHelper.DownloadFileInfo> getDownloadFilesFromCursor(Cursor cursor) {
        List<BaseContentDbHelper.DownloadFileInfo> downloadFileInfos = new ArrayList<BaseContentDbHelper.DownloadFileInfo>();
        while (cursor != null && cursor.moveToNext()) {
            BaseContentDbHelper.DownloadFileInfo downloadFileInfo = new BaseContentDbHelper.DownloadFileInfo(cursor);
            if (downloadFileInfo == null) {
                continue;
            }
            downloadFileInfos.add(downloadFileInfo);
        }
        return downloadFileInfos;
    }

    // ---------------------------interface impl---------------------------

    @Override
    public BaseContentDbHelper.DownloadFileInfo getDownloadFile(String url) {

        BaseContentDbHelper.DownloadFileInfo downloadFileInfo = getDownloadFileInternal(url);

        if (downloadFileInfo == null && UrlUtil.isUrl(url)) {
            downloadFileInfo = getDownloadFileInternal(url.trim());
        }

        return downloadFileInfo;
    }

    /**
     * get a DownloadFile
     */
    private BaseContentDbHelper.DownloadFileInfo getDownloadFileInternal(String url) {

        BaseContentDbHelper.DownloadFileInfo downloadFileInfo = null;

//        if (mDownloadFileInfoMap.get(url) != null) {
        // return memory cache
//            downloadFileInfo = mDownloadFileInfoMap.get(url);
//        } else {
        // find in database
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(BaseContentDbHelper.DownloadFileInfo.Table
                .TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return null;
        }

        Cursor cursor = dao.query(null, BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_URL + "= " +
                "?", new
                String[]{url}, null);
        if (cursor != null && cursor.moveToFirst()) {
            downloadFileInfo = new BaseContentDbHelper.DownloadFileInfo(cursor);
        }

        // close the cursor
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (downloadFileInfo == null) {
            return null;
        }

        String downloadUrl = downloadFileInfo.getUrl();
        if (UrlUtil.isUrl(downloadUrl)) {
            synchronized (mModifyLock) {// lock
                // cache in memory
//                    mDownloadFileInfoMap.put(downloadUrl, downloadFileInfo);
//                    downloadFileInfo = mDownloadFileInfoMap.get(url);
            }
        }
//        }

        checkDownloadFileStatus(downloadFileInfo);

        return downloadFileInfo;
    }

    @Override
    public List<BaseContentDbHelper.DownloadFileInfo> getDownloadFiles() {

        // if memory cache is empty,then init from database
//        if (MapUtil.isEmpty(mDownloadFileInfoMap)) {
//            initDownloadFileInfoMapFromDb();
//        }

        // if this time the memory cache is not empty,return the cache
//        if (!MapUtil.isEmpty(mDownloadFileInfoMap)) {
//            List<DownloadFileInfo> downloadFileInfos = new ArrayList<DownloadFileInfo>
//                    (mDownloadFileInfoMap.values());

        // check status
//            if (!CollectionUtil.isEmpty(downloadFileInfos)) {
//                for (DownloadFileInfo downloadFileInfo : downloadFileInfos) {
//                    checkDownloadFileStatus(downloadFileInfo);
//                }
//            }
//            return downloadFileInfos;
//        }

        // otherwise return empty list
        return new ArrayList<BaseContentDbHelper.DownloadFileInfo>();
    }

    @Override
    public BaseContentDbHelper.DownloadFileInfo createDownloadFileInfo(DetectUrlFileInfo detectUrlFileInfo) {
        if (!DownloadFileUtil.isLegal(detectUrlFileInfo)) {
            return null;
        }
        BaseContentDbHelper.DownloadFileInfo downloadFileInfo = new BaseContentDbHelper.DownloadFileInfo(detectUrlFileInfo);
        // add to cache
        boolean isSucceed = addDownloadFile(downloadFileInfo);
        if (isSucceed) {
            return downloadFileInfo;
        }
        return null;
    }

    @Override
    public void recordStatus(String url, int status, int increaseSize) throws Exception {

        Log.i(TAG, TAG + ".recordStatus 记录状态：status：" + status + "，increaseSize：" + increaseSize
                + "，url：" + url);

        BaseContentDbHelper.DownloadFileInfo downloadFileInfo = getDownloadFile(url);
        if (!DownloadFileUtil.isLegal(downloadFileInfo)) {
            return;
        }

        synchronized (mModifyLock) {// lock

            int oldStatus = downloadFileInfo.getStatus();
            long oldDownloadedSize = downloadFileInfo.getDownloadedSizeLong();

            boolean isStatusChange = (status != downloadFileInfo.getStatus());
            if (!isStatusChange && increaseSize <= 0) {
                return;
            }

            Type type = Type.OTHER;
            int changeCount = 0;

            if (isStatusChange) {
                downloadFileInfo.setStatus(status);
                changeCount++;
                type = Type.DOWNLOAD_STATUS;
            }
            if (increaseSize > 0) {
                downloadFileInfo.setDownloadedSize(downloadFileInfo.getDownloadedSizeLong() +
                        increaseSize);
                changeCount++;
                type = Type.DOWNLOADED_SIZE;
            }
            if (changeCount > 1) {
                type = Type.OTHER;
            }
            boolean isSucceed = updateDownloadFileInternal(downloadFileInfo, false, type);
            if (!isSucceed) {
                downloadFileInfo.setStatus(oldStatus);
                downloadFileInfo.setDownloadedSize(oldDownloadedSize);
                throw new Exception("record failed !");
            }
        }
    }

    @Override
    public void resetDownloadFile(String url, boolean deleteMode) throws Exception {
        BaseContentDbHelper.DownloadFileInfo downloadFileInfo = getDownloadFile(url);
        if (!DownloadFileUtil.isLegal(downloadFileInfo)) {
            return;
        }

        if (!deleteMode) {
            synchronized (mModifyLock) {// lock
                long oldDownloadedSize = downloadFileInfo.getDownloadedSizeLong();
                downloadFileInfo.setDownloadedSize(0);// reset download size
                boolean isSucceed = updateDownloadFileInternal(downloadFileInfo, false, Type
                        .DOWNLOADED_SIZE);
                if (!isSucceed) {
                    // rollback
                    downloadFileInfo.setDownloadedSize(oldDownloadedSize);
                    throw new Exception("reset failed !");
                }
            }
        } else {
            // delete the download file
            deleteDownloadFile(url);
        }
    }

    @Override
    public void resetDownloadSize(String url, long downloadSize) throws Exception {
        BaseContentDbHelper.DownloadFileInfo downloadFileInfo = getDownloadFile(url);
        if (!DownloadFileUtil.isLegal(downloadFileInfo)) {
            return;
        }

        if (downloadSize < 0 || downloadSize > downloadFileInfo.getFileSizeLong()) {
            throw new Exception("the download size nee to set is illegal !");
        }

        synchronized (mModifyLock) {// lock
            long oldDownloadedSize = downloadFileInfo.getDownloadedSizeLong();
            downloadFileInfo.setDownloadedSize(downloadSize);// reset download size with the new
            // download size
            boolean isSucceed = updateDownloadFileInternal(downloadFileInfo, false, Type
                    .DOWNLOADED_SIZE);
            if (!isSucceed) {
                // rollback
                downloadFileInfo.setDownloadedSize(oldDownloadedSize);
                throw new Exception("reset downloadSize failed !");
            }
        }
    }

    @Override
    public void moveDownloadFile(String url, String newDirPath) throws Exception {
        BaseContentDbHelper.DownloadFileInfo downloadFileInfo = getDownloadFile(url);
        if (!DownloadFileUtil.isLegal(downloadFileInfo)) {
            throw new Exception("download file doest not exist or illegal !");
        }
        synchronized (mModifyLock) {// lock
            String oldFileDir = downloadFileInfo.getFileDir();
            downloadFileInfo.setFileDir(newDirPath);
            boolean isSucceed = updateDownloadFileInternal(downloadFileInfo, false, Type.SAVE_DIR);
            if (!isSucceed) {
                // rollback
                downloadFileInfo.setFileDir(oldFileDir);
                throw new Exception("move failed !");
            }
        }
    }

    @Override
    public void deleteDownloadFile(String url) throws Exception {
        BaseContentDbHelper.DownloadFileInfo downloadFileInfo = getDownloadFile(url);
        if (!DownloadFileUtil.isLegal(downloadFileInfo)) {
            throw new Exception("download file doest not exist or illegal !");
        }
        boolean isSucceed = deleteDownloadFile(downloadFileInfo);
        if (!isSucceed) {
            // really deleted ?
            BaseContentDbHelper.DownloadFileInfo DownloadFileInfoDeleted = getDownloadFileInternal(downloadFileInfo
                    .getUrl());
            if (DownloadFileInfoDeleted == null) {
                // has been deleted
            } else {
                // rollback, none
                throw new Exception("delete failed !");
            }
        }
    }

    /**
     * delete file by parentId
     *
     * @param parentId
     * @throws Exception
     */
    public boolean deleteDownloadFileByParentId(String parentId) {
        ContentDbDao dao = mDownloadFileDbHelper.getContentDbDao(BaseContentDbHelper.DownloadFileInfo.Table
                .TABLE_NAME_OF_DOWNLOAD_FILE);
        if (dao == null) {
            return false;
        }
        //1.删除指定parentId的ts文件
        List<BaseContentDbHelper.DownloadFileInfo> fileInfoList = getDownloadInfoByParentId(parentId);
        if (fileInfoList != null) {
            for (BaseContentDbHelper.DownloadFileInfo downloadFileInfo : fileInfoList) {
                String path = downloadFileInfo.getFilePath();
                if (path != null) {
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
        //2.删除数据库记录
        synchronized (mModifyLock) {// lock
            int result = dao.delete(BaseContentDbHelper.DownloadFileInfo.Table.COLUMN_NAME_OF_FIELD_PARENT_ID + "= ?", new
                    String[]{parentId});
            if (result >= 1) {
                return true;
            }
            List<BaseContentDbHelper.DownloadFileInfo> fileInfoListExist = getDownloadInfoByParentId(parentId);
            if (fileInfoListExist == null || fileInfoListExist.size() == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void renameDownloadFile(String url, String newFileName) throws Exception {
        BaseContentDbHelper.DownloadFileInfo downloadFileInfo = getDownloadFile(url);
        if (downloadFileInfo == null) {
            throw new Exception("download file doest not exist or illegal !");
        }
        synchronized (mModifyLock) {// lock
            String oldFileName = downloadFileInfo.getFileName();
            downloadFileInfo.setFileName(newFileName);
            boolean isSucceed = updateDownloadFileInternal(downloadFileInfo, false, Type
                    .SAVE_FILE_NAME);
            if (!isSucceed) {
                // rollback
                downloadFileInfo.setFileName(oldFileName);
                throw new Exception("rename failed !");
            }
        }
    }
}
