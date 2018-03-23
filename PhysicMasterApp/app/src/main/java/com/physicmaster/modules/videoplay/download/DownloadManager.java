package com.physicmaster.modules.videoplay.download;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.physicmaster.base.BaseApplication;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.modules.videoplay.cache.bean.TsFileInfo;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.db.TsFileManager;
import com.physicmaster.modules.videoplay.cache.db.VideoManager;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.Response;
import com.physicmaster.net.response.course.VideoDownloadInfoResponse;
import com.physicmaster.net.security.AESEncryption;
import com.physicmaster.net.service.video.VideoDownloadInfoService;
import com.physicmaster.utils.FileSizeUtil;
import com.physicmaster.utils.MD5;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huazai on 19/08/17.
 * 负责一个视频tsUrls的获取，ts文件的下载，数据库的存储等所有工作
 * 输出只有成功或失败两种结果
 */

public class DownloadManager {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    //线程超时时间为10s
    private static final long KEEP_ALIVE = 10L;
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    public static final int MESSAGE_DOWNLOAD_SUCC = 1;
    public static final int MESSAGE_DOWNLOAD_FAIL = 2;
    public static final int MESSAGE_SAVETODATABASE_FAIL = 3;

    private Logger logger = AndroidLogger.getLogger(DownloadManager.class.getSimpleName());
    private File downloadPath;
    private Context mContext;
    private TsFileManager tsFileManager;
    private VideoManager videoManager;
    private OnVideoDownloadedListener listener;

    //记录下载ts文件的剩余重试次数
    private Map<String, Integer> tsDownloadTasks;
    //重试次数
    private static final int RETRY_TIMES = 5;

    private static final ThreadFactory mThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread#" + mCount.getAndIncrement());
        }
    };
    private static final LinkedBlockingDeque<Runnable> RUNNABLES = new LinkedBlockingDeque<>();
    private static final PausableThreadPoolExecutor THREAD_POOL_EXXCUTOR = new PausableThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, RUNNABLES,
            mThreadFactory);

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgWhat = msg.what;
            switch (msgWhat) {
                case MESSAGE_DOWNLOAD_FAIL:
                    DownloadFailResult failResult = (DownloadFailResult) msg.obj;
                    Integer leftRetryTimes = tsDownloadTasks.get(failResult.key);
                    //可能是上一个video的ts文件下载失败的通知
                    if (leftRetryTimes == null) {
                        return;
                    }
                    tsDownloadTasks.put(failResult.key, --leftRetryTimes);
                    if (leftRetryTimes >= 0) {
                        addDownloadTask(failResult.key, failResult.url, failResult.videoId);
                        logger.debug("key=" + failResult.key + "下载失败，加入重试任务");
                    } else {
                        //如果此ts文件重试次数都用完了，那么宣告整个视频都下载失败
                        if (listener != null) {
                            listener.onVideoDownloadedFailed(downloadTask.videoId);
                        }
                    }
                    break;
                case MESSAGE_DOWNLOAD_SUCC:
                    DownloadSuccResult result = (DownloadSuccResult) msg.obj;
                    //可能会有上一个视频的ts任务下载成功后通知过来，这里直接忽略掉
                    if (!result.videoId.equals(downloadTask.videoId)) {
                        return;
                    }
                    ArrayMap<String, String> tsUrlsMap = downloadTask.tsUrlsMap;
                    tsUrlsMap.put(result.key, result.filePath);
                    long tsFileSize = msg.getData().getLong("tsFileSize", 0);
                    downloadTask.totalSize += tsFileSize;
                    tsDownloadTasks.remove(result.key);
                    if (downloadTask.curTotalTsNum.decrementAndGet() == 0) {
                        //video全部ts文件下载成功,更新数据库video表
                        completeTask();
                    }
                    if (listener != null) {
                        int totalSize = downloadTask.tsUrlsMap.size();
                        float fPercent = (totalSize - downloadTask.curTotalTsNum.get()) / (float) totalSize;
                        listener.onProgress((int) (fPercent * 100), downloadTask.videoTitle);
                    }
                    logger.debug("剩余ts文件数量：" + downloadTask.curTotalTsNum.get());
                    break;
                case MESSAGE_SAVETODATABASE_FAIL:
                    break;
                default:
                    break;
            }
        }
    };
    private DownloadTask downloadTask;

    public DownloadManager(Context context, File downloadPath) {
        this.mContext = context;
        this.downloadPath = downloadPath;
        init(context);
    }

    public DownloadManager(Context context, String downloadPath) {
        this.mContext = context;
        this.downloadPath = new File(downloadPath);
        init(context);
    }

    private void init(Context context) {
        tsFileManager = new TsFileManager(context);
        videoManager = new VideoManager(context);
        tsDownloadTasks = new HashMap<>();
    }

    public void setListener(OnVideoDownloadedListener listener) {
        this.listener = listener;
    }

    /**
     * 完成一个下载任务的收尾工作
     */
    private void completeTask() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.debug("视频下载成功");
                //将m3u8字符串中的ts网络地址替换为本地文件地址
                downloadTask.replaceM3u8String();
                //将m3u8内容写入文件，更新video表
                VideoInfo videoInfo = videoManager.getVideoInfo(downloadTask.videoId);
                if (videoInfo != null) {
                    videoInfo.setState(VideoInfo.STATE_DOWNLOADED);
                    videoInfo.setDownloadedSize(downloadTask.totalSize);
                    videoInfo.setVideoPath(writeM3U8ToFile(downloadTask.m3u8String, downloadTask.videoId));
                    videoManager.addOrUpdateVideo(videoInfo);
                    if (listener != null) {
                        listener.onVideoDownloaded(null, downloadTask.videoTitle);
                    }
                }
            }
        };
        THREAD_POOL_EXXCUTOR.execute(runnable);
    }


    /**
     * 将替换后的m3u8字符串写入文件
     *
     * @param m3u8String
     */
    private String writeM3U8ToFile(String m3u8String, String fileName) {
        String encryptStr = null;
        try {
            encryptStr = AESEncryption.encrypt(m3u8String.getBytes(), MD5.hexdigest
                    (BaseApplication.getDeviceID()).substring(0, 16));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(encryptStr)) {
            m3u8String = encryptStr;
        }
        File m3u8File = new File(mContext.getFilesDir(), fileName
                + ".m3u8");
        if (m3u8File.exists()) {
            m3u8File.delete();
        }
        try {
            m3u8File.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(m3u8File);
            outputStream.write(m3u8String.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return m3u8File.getAbsolutePath();
    }

    /**
     * 获取文件url的md5值
     *
     * @param url
     * @return
     */
    public String hashKeyFromUrl(String url) {
        String cacheKey;
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = byteToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes
     * @return
     */
    private String byteToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 0) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * @param fileUrl
     * @return 文件下载完成后的绝对路径
     */
    public String downloadFile(String fileUrl) {
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        String fileName = hashKeyFromUrl(fileUrl);
        File file = new File(downloadPath, fileName);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        try {
            URL url = new URL(fileUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            OutputStream outputStream = new FileOutputStream(file);
            out = new BufferedOutputStream(outputStream);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            //如果下载出现异常，删除异常文件
            if (file.exists()) {
                file.delete();
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 开始缓存一个视频
     *
     * @param videoId
     */
    public void startDownload(String videoId) {
        getm3u8FroServer(videoId);
    }

    /**
     * 从服务端获取m3u8内容
     */
    private void getm3u8FroServer(final String videoId) {
        if (TextUtils.isEmpty(videoId)) {
            logger.debug("videoId is null");
            return;
        }
        VideoDownloadInfoService service = new VideoDownloadInfoService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<VideoDownloadInfoResponse>() {
            @Override
            public void onGetData(final VideoDownloadInfoResponse data) {
                final AsyncTask<VideoDownloadInfoResponse.DataBean.VideoDownloadVoBean, Integer, Boolean>
                        taskCache = new AsyncTask<VideoDownloadInfoResponse.DataBean.VideoDownloadVoBean, Integer, Boolean>() {
                    @Override
                    protected Boolean doInBackground(VideoDownloadInfoResponse.DataBean.VideoDownloadVoBean... videoDownloadVoBean) {
                        downloadTask = new DownloadTask(videoDownloadVoBean[0].m3u8Content, videoId, videoDownloadVoBean[0].tsUrls, videoDownloadVoBean[0].videoTitle,
                                BaseApplication.getUserData().dtUserId);
                        startDownload();
                        return true;
                    }
                };
                taskCache.execute(data.data.videoDownloadVo);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                //请求视频缓存信息失败，重新把videoId加到队尾
                if (errorCode != Response.CODE_NO_PERMISSION) {
                }
            }
        });
        String realVideoId = videoId.split("-")[0];
        service.postLogined("videoId=" + realVideoId, false);
    }

    /**
     * 启动下载ts文件列表
     */
    public void startDownload() {
        logger.debug("start download");
        //记录重试下载次数的变量清空
        tsDownloadTasks.clear();
        ArrayMap<String, String> tsUrlsMap = downloadTask.tsUrlsMap;
        Iterator keys = tsUrlsMap.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = tsUrlsMap.get(key);
            String fileName = hashKeyFromUrl(value);
            if (addTsFileToDatabase(value, fileName, downloadTask.videoId)) {
                tsDownloadTasks.put(key, RETRY_TIMES);
                addDownloadTask(key, value, downloadTask.videoId);
                logger.debug("下载任务数量：" + RUNNABLES.size());
            }
        }
    }

    /**
     * 暂停下载
     */
    public void pauseDownload() {
        THREAD_POOL_EXXCUTOR.pause();
    }

    /**
     * 重新启动下载
     */
    public void restartDownload() {
        THREAD_POOL_EXXCUTOR.resume();
    }

    /**
     * 添加一个下载任务到线程池
     *
     * @param key
     * @param fileUrl
     */
    public void addDownloadTask(final String key, final String fileUrl, final String videoId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String filePath = downloadFile(fileUrl);
                if (TextUtils.isEmpty(filePath)) {
                    //下载失败->发送失败消息
                    DownloadFailResult result = new DownloadFailResult(key, fileUrl, videoId);
                    Message msgSucc = mHandler.obtainMessage(MESSAGE_DOWNLOAD_FAIL, result);
                    msgSucc.sendToTarget();
                    logger.debug("filePath is null, download fail");
                    return;
                }
                File file = new File(filePath);
                if (!file.exists()) {
                    // 下载失败->发送失败消息
                    mHandler.sendEmptyMessage(MESSAGE_DOWNLOAD_FAIL);
                    logger.debug("file not exist ,download fail");
                    return;
                }
                //下载成功->将文件位置保存到数据库
                boolean succ = updateTsFileToDatabase(fileUrl, file.length(), filePath);
                if (succ) {
                    DownloadSuccResult result = new DownloadSuccResult(key, filePath, videoId);
                    Bundle data = new Bundle();
                    long size = (long) FileSizeUtil.getFileOrFilesSize(filePath, FileSizeUtil.SIZETYPE_B);
                    data.putLong("tsFileSize", size);
                    Message msgSucc = mHandler.obtainMessage(MESSAGE_DOWNLOAD_SUCC, result);
                    msgSucc.setData(data);
                    msgSucc.sendToTarget();
                } else {
                    mHandler.sendEmptyMessage(MESSAGE_SAVETODATABASE_FAIL);
                }
            }
        };
        THREAD_POOL_EXXCUTOR.execute(runnable);
    }

    /**
     * 数据库添加一个ts文件记录
     *
     * @param mUrl
     * @param mFileName
     * @param mVideoId
     * @return
     */
    private boolean addTsFileToDatabase(String mUrl, String mFileName, String mVideoId) {
        TsFileInfo tsFileInfo = new TsFileInfo(mUrl, mFileName, 0L, null, mVideoId);
        boolean succ = tsFileManager.addOrUpdateVideo(tsFileInfo);
        return succ;
    }

    /**
     * 更新一个ts记录
     *
     * @param mUrl
     * @param fileSize
     * @param fileDir
     * @return
     */
    private boolean updateTsFileToDatabase(String mUrl, long fileSize, String fileDir) {
        TsFileInfo tsFileInfo = tsFileManager.getTsFileInfoByUrl(mUrl);
        if (null == tsFileInfo) {
            //数据库记录已被删除，说明用户已经删掉这个任务，那么把这个ts文件也删掉
            File tsFile = new File(fileDir);
            if (tsFile.exists()) {
                tsFile.delete();
            }
            return false;
        }
        tsFileInfo.setFileDir(fileDir);
        tsFileInfo.setDownloadedSize(fileSize);
        boolean succ = tsFileManager.addOrUpdateVideo(tsFileInfo);
        return succ;
    }

    private boolean updateVideoInfo() {
        return true;
    }

    public interface OnVideoDownloadedListener {
        public void onVideoDownloaded(ArrayMap<String, String> filePaths, String title);

        public void onVideoDownloadedFailed(String videoId);

        public void onProgress(int percent, String videoTitle);
    }

    private static class DownloadSuccResult {
        public DownloadSuccResult(String key, String filePath, String videoId) {
            this.key = key;
            this.filePath = filePath;
            this.videoId = videoId;
        }

        public String key;
        public String filePath;
        public String videoId;
    }

    private static class DownloadFailResult {
        public DownloadFailResult(String key, String url, String videoId) {
            this.key = key;
            this.url = url;
            this.videoId = videoId;
        }

        public String key;
        public String url;
        public String videoId;
    }

    //主要功能用在重试机制上
    private static class DownloadTsTask {
        public DownloadTsTask(String key, String url) {
            this.key = key;
            this.url = url;
        }

        public int retryCount = 3;
        public String key;
        public String url;
    }
}
