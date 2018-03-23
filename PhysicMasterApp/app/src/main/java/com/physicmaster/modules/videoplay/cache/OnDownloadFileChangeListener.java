package com.physicmaster.modules.videoplay.cache;

import android.os.Handler;
import android.os.Looper;

import com.physicmaster.modules.videoplay.cache.db.BaseContentDbHelper;


/**
 * the listener for listening the DownloadFile change
 *
 * @author wlf(Andy)
 * @datetime 2015-12-09 16:45 GMT+8
 * @email 411086563@qq.com
 */
public interface OnDownloadFileChangeListener {

    /**
     * an new DownloadFile created
     *
     * @param downloadFileInfo new DownloadFile created
     */
    void onDownloadFileCreated(BaseContentDbHelper.DownloadFileInfo downloadFileInfo);

    /**
     * an DownloadFile updated
     *
     * @param downloadFileInfo DownloadFile updated
     * @param type             the update type
     */
    void onDownloadFileUpdated(BaseContentDbHelper.DownloadFileInfo downloadFileInfo, Type type);

    /**
     * an DownloadFile deleted
     *
     * @param downloadFileInfo DownloadFile deleted
     */
    void onDownloadFileDeleted(BaseContentDbHelper.DownloadFileInfo downloadFileInfo);

    /**
     * Callback helper for main thread
     */
    class MainThreadHelper {

        /**
         * an new DownloadFile created
         *
         * @param downloadFileInfo new DownloadFile created
         */
        public static void onDownloadFileCreated(final BaseContentDbHelper.DownloadFileInfo downloadFileInfo, final
        OnDownloadFileChangeListener onDownloadFileChangeListener) {
            if (onDownloadFileChangeListener == null) {
                return;
            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (onDownloadFileChangeListener == null) {
                        return;
                    }
                    onDownloadFileChangeListener.onDownloadFileCreated(downloadFileInfo);
                }
            });
        }

        /**
         * an DownloadFile updated
         *
         * @param downloadFileInfo DownloadFile updated
         * @param type             the update type
         */
        public static void onDownloadFileUpdated(final BaseContentDbHelper.DownloadFileInfo downloadFileInfo, final Type type, final
        OnDownloadFileChangeListener onDownloadFileChangeListener) {
            if (onDownloadFileChangeListener == null) {
                return;
            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (onDownloadFileChangeListener == null) {
                        return;
                    }
                    onDownloadFileChangeListener.onDownloadFileUpdated(downloadFileInfo, type);
                }
            });
        }

        /**
         * an DownloadFile deleted
         *
         * @param downloadFileInfo DownloadFile deleted
         */
        public static void onDownloadFileDeleted(final BaseContentDbHelper.DownloadFileInfo downloadFileInfo, final
        OnDownloadFileChangeListener onDownloadFileChangeListener) {
            if (onDownloadFileChangeListener == null) {
                return;
            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (onDownloadFileChangeListener == null) {
                        return;
                    }
                    onDownloadFileChangeListener.onDownloadFileDeleted(downloadFileInfo);
                }
            });
        }
    }

    /**
     * DownloadFile Update Type
     */
    enum Type {

        /**
         * download status changed
         */
        DOWNLOAD_STATUS,
        /**
         * downloaded size changed
         */
        DOWNLOADED_SIZE,
        /**
         * save dir changed
         */
        SAVE_DIR,
        /**
         * save file name changed
         */
        SAVE_FILE_NAME,
        /**
         * other,except {@link #DOWNLOAD_STATUS},{@link #DOWNLOADED_SIZE},{@link #SAVE_DIR} and {@link #SAVE_FILE_NAME}
         */
        OTHER
    }
}
