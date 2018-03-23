//package com.physicmaster.modules.videoplay.cache.service;
//
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.AsyncTask;
//import android.os.Binder;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.physicmaster.base.BaseApplication;
//import com.physicmaster.common.Constant;
//import com.physicmaster.common.cache.CacheKeys;
//import com.physicmaster.common.cache.CacheManager;
//import com.physicmaster.log.AndroidLogger;
//import com.physicmaster.log.Logger;
//import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
//import com.physicmaster.modules.videoplay.cache.db.DownloadCacher;
//import com.physicmaster.modules.videoplay.cache.db.VideoManager;
//import com.physicmaster.net.IOpenApiDataServiceCallback;
//import com.physicmaster.net.response.Response;
//import com.physicmaster.net.response.course.VideoDownloadInfoResponse;
//import com.physicmaster.net.response.user.UserDataResponse;
//import com.physicmaster.net.security.AESEncryption;
//import com.physicmaster.net.service.video.VideoDownloadInfoService;
//import com.physicmaster.utils.MD5;
//import com.physicmaster.utils.NetworkUtils;
//import com.physicmaster.utils.UIUtils;
//
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * Created by huashigen on 2017/3/23.
// */
//
//public class DownloadService extends Service implements OnFileDownloadStatusListener {
//
//    private static final String TAG = "DownloadService";
//    private ConcurrentLinkedQueue<String> videoList;
//    private Object videoLock, taskLock;
//    private AtomicInteger curDownloadingTsNum;
//    private VideoManager videoManager;
//    private DownloadCacher downloadManager;
//    //是否正在运行
//    private AtomicBoolean isRunning = new AtomicBoolean(false);
//    public static final String VIDEO_DOWNLOADED = "video_downloaded";
//    public static final String QUIT_DOWNLOADING = "quit_downloading";
//    private String m3u8String;
//    private int curTotalTsNum = -1;
//    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());
//    private static int downloadState2 = 0;//0-不暂停，1-用户暂停，2-网络原因暂停
//    public static final String PAUSE_DOWNLOAD = "pause_download";
//    public static final int ACTION_PAUSE_RESTART = 1;
//    public static final int ACTION_REMOVE_TASK = 2;
//    private List<String> pauseUrls = new ArrayList<>();
//    private String videoId;
//    public static boolean downloadState = true;
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d(TAG, "onReceive: " + intent.getAction());
//            if (intent.getAction().equals(PAUSE_DOWNLOAD)) {
//                int flag = intent.getIntExtra("flag", -1);
//                if (flag == ACTION_PAUSE_RESTART) {
//                    boolean isPause = intent.getBooleanExtra("isPause", false);
//                    if (isPause) {
//                        startOrPause(false, 1);
//                    } else {
//                        startOrPause(true, 0);
//                    }
//                } else if (flag == ACTION_REMOVE_TASK) {
//                    logger.debug("action:" + flag);
//                    List<String> deleteList = intent.getStringArrayListExtra("deleteList");
//                    if (deleteList != null) {
//                        for (String s : deleteList) {
//                            videoList.remove(s);
//                        }
//                    }
//                    boolean isRunningTaskDelete = intent.getBooleanExtra("isRunningTaskDelete", false);
//                    logger.debug("isRunningTaskDelete:" + isRunningTaskDelete);
//                    if (isRunningTaskDelete) {
//                        if (pauseUrls.size() > 0) {
//                            isRunning.set(false);
//                            pauseUrls.clear();
//                        } else {
//                            isRunning.set(false);
//                            getm3u8FroServer();
//                        }
//                    }
//                }
//            } else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
//                String networkState = NetworkUtils.getNetworkState(context);
//                if (networkState.equals(Constant.NETTYPE_UNCONNECTED) || networkState.equals(Constant.NETTYPE_UNKNOWN)) {
//                    //已断网-暂停下载
//                    startOrPause(false, 2);
//                    downloadState = false;
//                } else {
//                    if (networkState.equals(Constant.NETTYPE_WIFI)) {
//                        //切换到wifi环境，开始下载
//                        if (isRunning.get() && downloadState2 == 2) {
//                            startOrPause(true, 0);
//                            downloadState = true;
//                        }
//                    } else {
//                        //切换到非wifi环境，暂停下载
//                        if (isRunning.get()) {
//                            startOrPause(false, 2);
//                            downloadState = false;
//                        }
//                    }
//                }
//            } else if (intent.getAction().equals(QUIT_DOWNLOADING)) {
//                FileDownloader.pauseAll();
//                isRunning.set(false);
//            }
//        }
//    };
//
//    /**
//     * 开始或暂停下载
//     *
//     * @param start
//     */
//    private void startOrPause(boolean start, int state) {
//        if (start) {
//            if (downloadState2 == 0) {
//                return;
//            }
//            downloadState2 = state;
//            if (pauseUrls == null || pauseUrls.size() > 0) {
//                FileDownloader.reStart(pauseUrls, videoId);
//            } else {
//                getm3u8FroServer();
//            }
//        } else {
//            if (downloadState2 != 0) {
//                return;
//            }
//            downloadState2 = state;
//            pauseUrls.clear();
//            FileDownloader.pauseAll();
//        }
//    }
//
//    /**
//     * 返回一个Binder对象
//     */
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return new MsgBinder();
//    }
//
//    public class MsgBinder extends Binder {
//        /**
//         * 获取当前Service的实例
//         *
//         * @return
//         */
//        public DownloadService getService() {
//            return DownloadService.this;
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        String videoId = intent.getStringExtra("videoId");
//        if (!TextUtils.isEmpty(videoId)) {
//            if (!videoList.contains(videoId)) {
//                VideoInfo videoInfo = videoManager.getVideoInfo(videoId);
//                //已下载则跳过
//                if (videoInfo == null || videoInfo.getState() == VideoInfo.STATE_ADD) {
//                    videoList.add(videoId);
//                }
//            }
//        }
//        List<String> videoIds = intent.getStringArrayListExtra("videoIds");
//        if (videoIds != null && videoIds.size() > 0) {
//            for (String id : videoIds) {
//                if (!videoList.contains(id)) {
//                    VideoInfo videoInfo = videoManager.getVideoInfo(id);
//                    //已下载则跳过
//                    if (videoInfo == null || videoInfo.getState() == VideoInfo.STATE_ADD) {
//                        videoList.add(id);
//                    }
//                }
//            }
//        }
////        getm3u8FroServer();
//        return START_REDELIVER_INTENT;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        videoLock = new Object();
//        taskLock = new Object();
//        videoList = new ConcurrentLinkedQueue<>();
//        videoManager = new VideoManager(this);
//        downloadManager = new DownloadCacher(this);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(PAUSE_DOWNLOAD);
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        intentFilter.addAction(QUIT_DOWNLOADING);
//        registerReceiver(receiver, intentFilter);
//
//        //查询正在下载的任务
//        List<VideoInfo> videoInfos = videoManager.getDownloadingVideos();
//        if (videoInfos != null) {
//            for (VideoInfo videoInfo : videoInfos) {
//                if (videoInfo.getState() == VideoInfo.STATE_ADD || videoInfo.getState() == VideoInfo
//                        .STATE_DOWNLOADING) {
//                    videoList.add(videoInfo.getId());
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (receiver != null) {
//            unregisterReceiver(receiver);
//        }
//    }
//
//    @Override
//    public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {
//    }
//
//    @Override
//    public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
//    }
//
//    @Override
//    public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {
//    }
//
//    @Override
//    public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float
//            downloadSpeed, long remainingTime) {
//    }
//
//    @Override
//    public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {
//        logger.debug("onFileDownloadStatusPaused: ");
//        //暂停后立即重新启动下载
//        if (downloadState2 == 0) {
//            FileDownloader.reStart(downloadFileInfo.getUrl(), downloadFileInfo.getParentId());
//        }
//        synchronized (taskLock) {
//            pauseUrls.add(downloadFileInfo.getUrl());
//        }
//    }
//
//    @Override
//    public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
//        if (downloadFileInfo == null) {
//            logger.debug("onFileDownloadStatusCompleted: 下载异常");
//            return;
//        }
//        String videoId = downloadFileInfo.getParentId();
//        logger.debug("onFileDownloadStatusCompleted: ");
//        //1.更新ts表对应tsId在数据库中的状态-这个框架已经做了
//        synchronized (videoLock) {
//            //2.更新video表对应videoId的已下载ts数目字段,更新已下载文件大小
//            VideoInfo videoInfo = videoManager.getVideoInfo(videoId);
//            logger.debug("videoInfo: " + videoInfo);
//            if (videoInfo != null) {
//                videoInfo.setDownloadedSize(videoInfo.getDownloadedSize() + downloadFileInfo
//                        .getDownloadedSizeLong());
//                videoInfo.setDownloadedTsFileNum(videoInfo.getDownloadedTsFileNum() + 1);
//                //未下载ts文件数目减1
//                curDownloadingTsNum.decrementAndGet();
//                logger.debug("onFileDownloadStatusCompleted: 剩余TS文件:" + curDownloadingTsNum
//                        .intValue());
//
//                //m3u8 ts链接替换成本地地址
//                String fileName = downloadFileInfo.getFileName();
//                logger.debug("onFileDownloadStatusCompleted: " + fileName);
//                try {
//                    int num = Integer.parseInt(fileName.split("-")[1].split("\\.")[0]) - 1;
//                    String replaceStr = "###TS-" + num + "###";
////                        String md5Name = MD5.hexdigest(splitName[0]) + "." + splitName[1];
//                    String md5Name = MD5.hexdigest(downloadFileInfo.getFileName());
//                    File file = new File(downloadFileInfo.getFilePath());
//                    File newFile = new File(downloadFileInfo.getFileDir(), md5Name);
//                    file.renameTo(newFile);
//                    downloadManager.renameDownloadFile(downloadFileInfo.getUrl(), md5Name);
//                    m3u8String = m3u8String.replace(replaceStr, downloadFileInfo.getFileDir() + File.separator + md5Name);
//                } catch (Exception e) {
//                    UIUtils.showToast(DownloadService.this, "缓存异常");
//                    e.printStackTrace();
//                }
//                //3.如果video所有ts文件均已下载
//                if (curDownloadingTsNum.intValue() == 0) {
//                    //1.需要更新video表对应videoId的状态为已下载
//                    videoInfo.setState(VideoInfo.STATE_DOWNLOADED);
//                    videoInfo.setVideoPath(writeM3U8ToFile(m3u8String, videoId + ""));
//                    logger.debug("videoId=" + videoId + "视频已下载完成");
//                    isRunning.set(false);
//                    getm3u8FroServer();
//                }
//                videoManager.addOrUpdateVideo(videoInfo);
//                Intent intent = new Intent(VIDEO_DOWNLOADED);
//                float progress = (curTotalTsNum - curDownloadingTsNum.floatValue()) / (float)
//                        curTotalTsNum;
//                intent.putExtra("progress", progress);
//                intent.putExtra("videoTitle", videoInfo.getName());
//                intent.putExtra("downloadNum", videoList.size());
//                intent.putExtra("downloadedTsNum", curTotalTsNum - curDownloadingTsNum.intValue());
//                sendBroadcast(intent);
//            }
//        }
//    }
//
//    @Override
//    public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo,
//                                           FileDownloadStatusFailReason failReason) {
//        logger.debug("onFileDownloadStatusFailed: " + failReason.getOriginalCause());
//        failReason.printStackTrace();
//        if (downloadFileInfo != null) {
//            FileDownloader.reStart(downloadFileInfo.getUrl(), downloadFileInfo.getParentId());
//        }
//    }
//
//    /**
//     * 将替换后的m3u8字符串写入文件
//     *
//     * @param m3u8String
//     */
//    private String writeM3U8ToFile(String m3u8String, String fileName) {
//        String encryptStr = null;
//        try {
//            encryptStr = AESEncryption.encrypt(m3u8String.getBytes(), MD5.hexdigest
//                    (BaseApplication.getDeviceID()).substring(0, 16));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (!TextUtils.isEmpty(encryptStr)) {
//            m3u8String = encryptStr;
//        }
//        File m3u8File = new File(getFilesDir(), fileName
//                + ".m3u8");
//        if (m3u8File.exists()) {
//            m3u8File.delete();
//        }
//        try {
//            m3u8File.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        FileOutputStream outputStream = null;
//        try {
//            outputStream = new FileOutputStream(m3u8File);
//            outputStream.write(m3u8String.getBytes());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                outputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return m3u8File.getAbsolutePath();
//    }
//
//    //从服务端获取m3u8内容
//    private void getm3u8FroServer() {
//        if (isRunning.get()) {
//            Log.d(TAG, "任务正在进行。。。");
//            return;
//        }
//        videoId = videoList.poll();
//        if (TextUtils.isEmpty(videoId)) {
//            Log.d(TAG, "getm3u8FroServer: 下载任务已完成");
//            return;
//        }
//        VideoInfo videoInfo = videoManager.getVideoInfo(videoId);
//        //已下载则跳过
//        if (videoInfo != null && (videoInfo.getState() == VideoInfo.STATE_DOWNLOADED)) {
//            getm3u8FroServer();
//            return;
//        }
//        isRunning.set(true);
//        VideoDownloadInfoService service = new VideoDownloadInfoService(this);
//        service.setCallback(new IOpenApiDataServiceCallback<VideoDownloadInfoResponse>() {
//            @Override
//            public void onGetData(VideoDownloadInfoResponse data) {
//                curTotalTsNum = data.data.videoDownloadVo.tsUrls.size();
//                AsyncTask<VideoDownloadInfoResponse.DataBean.VideoDownloadVoBean, Integer, Boolean>
//                        taskCache = new AsyncTask<VideoDownloadInfoResponse.DataBean.VideoDownloadVoBean, Integer, Boolean>() {
//                    @Override
//                    protected Boolean doInBackground(VideoDownloadInfoResponse.DataBean.VideoDownloadVoBean... params) {
//                        startCache(params[0]);
//                        return true;
//                    }
//                };
//                taskCache.execute(data.data.videoDownloadVo);
//            }
//
//            @Override
//            public void onGetError(int errorCode, String errorMsg, Throwable error) {
//                //请求视频缓存信息失败，重新把videoId加到队尾
//                if (errorCode != Response.CODE_NO_PERMISSION) {
//                    videoList.add(videoId);
//                    isRunning.set(false);
//                    getm3u8FroServer();
//                }
//            }
//        });
//        String realVideoId = videoId.split("-")[0];
//        service.postLogined("videoId=" + realVideoId, false);
//    }
//
//    /**
//     * 开始缓存
//     *
//     * @param videoDownloadVoBean
//     */
//    private void startCache(VideoDownloadInfoResponse.DataBean.VideoDownloadVoBean videoDownloadVoBean) {
//        UserDataResponse.UserDataBean.LoginVoBean userDataBean = (UserDataResponse.UserDataBean
//                .LoginVoBean) CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
//                .USERINFO_LOGINVO, UserDataResponse.UserDataBean.LoginVoBean.class);
//        m3u8String = videoDownloadVoBean.m3u8Content;
//        //数据库添加一个video记录
//        VideoInfo videoInfo = videoManager.getVideoInfo(videoDownloadVoBean.videoId + "-" + userDataBean.dtUserId);
//        videoInfo.setTsFileNum(videoDownloadVoBean.tsUrls.size());
//        videoInfo.setDownloadedTsFileNum(0);
//        videoInfo.setCreateDatetime(System.currentTimeMillis() + "");
//        videoInfo.setExpiresAtTime(videoDownloadVoBean.expiresAtTime + "");
//        videoInfo.setDownloadedSize(0);
//        videoInfo.setName(videoDownloadVoBean.videoTitle);
//        videoInfo.setPosterUrl(videoDownloadVoBean.posterUrl);
//        videoInfo.setState(VideoInfo.STATE_ADD);
//        videoInfo.setType(videoDownloadVoBean.videoType);
////        videoInfo.setOrder(videoDownloadVoBean.videoOrder);
////        videoInfo.setCourseId(videoDownloadVoBean.courseId + "");
//        videoInfo.setUserId(userDataBean.dtUserId);
//        if (userDataBean != null) {
//            videoInfo.setId(videoDownloadVoBean.videoId + "-" + userDataBean.dtUserId);
//        }
//        videoInfo.setTsFileNum(videoDownloadVoBean.tsUrls.size());
//        videoManager.addOrUpdateVideo(videoInfo);
//        //开始下载
//        curDownloadingTsNum = new AtomicInteger(videoDownloadVoBean.tsUrls.size());
//        FileDownloader.registerDownloadStatusListener(DownloadService.this);
//        FileDownloader.start(videoDownloadVoBean.tsUrls, videoDownloadVoBean.videoId + "-" + userDataBean.dtUserId);
//        videoInfo.setState(VideoInfo.STATE_DOWNLOADING);
//        videoManager.addOrUpdateVideo(videoInfo);
//    }
//}
