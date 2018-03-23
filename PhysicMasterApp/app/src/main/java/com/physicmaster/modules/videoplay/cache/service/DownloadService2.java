package com.physicmaster.modules.videoplay.cache.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.physicmaster.common.Constant;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.db.VideoManager;
import com.physicmaster.modules.videoplay.download.DownloadManager;
import com.physicmaster.utils.NetworkUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huashigen on 2017/3/23.
 */

public class DownloadService2 extends Service {

    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());
    private ConcurrentLinkedQueue<String> videoList;
    private VideoManager videoManager;
    public static final String QUIT_DOWNLOADING = "quit_downloading";
    //运行状态0-未下载，1-正在下载，2-暂停下载
    public static final int STOPED = 0;
    public static final int RUNNING = 1;
    public static final int PAUSED = 2;
    public AtomicInteger downloadState = new AtomicInteger(STOPED);
    private int progress = 0;

    private DownloadManager dm;

    @Override
    public void onCreate() {
        super.onCreate();
        videoList = new ConcurrentLinkedQueue<>();
        videoManager = new VideoManager(this);
        dm = new DownloadManager(this, getDiskCacheDir(this, getPackageName()));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, intentFilter);

        //查询正在下载的任务
        List<VideoInfo> videoInfos = videoManager.getDownloadingVideos();
        if (videoInfos != null) {
            for (VideoInfo videoInfo : videoInfos) {
                videoList.add(videoInfo.getId());
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String videoId = intent.getStringExtra("videoId");
        if (!TextUtils.isEmpty(videoId)) {
            if (!videoList.contains(videoId)) {
                VideoInfo videoInfo = videoManager.getVideoInfo(videoId);
                //已下载则跳过
                if (videoInfo == null || videoInfo.getState() == VideoInfo.STATE_ADD) {
                    videoList.add(videoId);
                }
            }
        }
        List<String> videoIds = intent.getStringArrayListExtra("videoIds");
        if (videoIds != null && videoIds.size() > 0) {
            for (String id : videoIds) {
                if (!videoList.contains(id)) {
                    VideoInfo videoInfo = videoManager.getVideoInfo(id);
                    //已下载则跳过
                    if (videoInfo == null || videoInfo.getState() == VideoInfo.STATE_ADD) {
                        videoList.add(id);
                    }
                }
            }
        }
        if (getDownloadState() == STOPED) {
            setDownloadState(RUNNING);
            startDownload();
        } else if (getDownloadState() == PAUSED) {
            setDownloadState(RUNNING);
            dm.restartDownload();
        }
        return START_REDELIVER_INTENT;
    }

    /**
     * 启动下载
     */
    public void startDownload() {
        videoId = videoList.poll();
        //videoList为空列表，下载任务已完成
        if (TextUtils.isEmpty(videoId)) {
            setDownloadState(STOPED);
            return;
        }
        //通知下载页面更新状态
        if (onProgressListener2 != null) {
            onProgressListener2.onDownloadStart(videoId);
        }
        setDownloadState(RUNNING);
        dm.setListener(new DownloadManager.OnVideoDownloadedListener() {
            @Override
            public void onVideoDownloaded(ArrayMap<String, String> filePaths, String title) {
                logger.info("下载成功");
                mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_SUCC, filePaths));
            }

            @Override
            public void onVideoDownloadedFailed(String videoId) {
                logger.info("下载失败:" + videoId);
                //检查网络状况，如果已断网那么停止下载
                mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_FAIL, videoId));
            }

            @Override
            public void onProgress(int percent, String title) {
                progress = percent;
                //这里加这个限制的原因是
                //用户点击暂停下载，我们把service状态设置为暂停，其实线程池并没有立即暂停，启动的任务还在继续下载，所以还会继续更新进度
                //这样限制一下至少用户层面会感觉到已经暂停了，其实没有完全暂停
                if (getDownloadState() == RUNNING) {
                    if (onProgressListener1 != null) {
                        onProgressListener1.onProgress(percent, videoList.size(), title);
                    }
                    if (onProgressListener2 != null) {
                        onProgressListener2.onProgress(percent, videoList.size(), title);
                    }
                }
            }
        });
        //开始下载
        dm.startDownload(videoId);
    }

    /**
     * 设置下载状态
     *
     * @param state
     */
    private void setDownloadState(int state) {
        downloadState.set(state);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            logger.debug("onReceive: " + intent.getAction());
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                String networkState = NetworkUtils.getNetworkState(context);
                if (networkState.equals(Constant.NETTYPE_UNCONNECTED) || networkState.equals(Constant.NETTYPE_UNKNOWN)) {
                    //已断网-暂停下载
                    logger.debug("已断网，暂停下载");
                    setDownloadState(STOPED);
                    //通知下载页面更新状态
                    if (onProgressListener2 != null) {
                        onProgressListener2.onDownloadStop();
                        onProgressListener2.onVideoDownloadFailed(videoId);
                    }
                } else {
                    if (networkState.equals(Constant.NETTYPE_WIFI)) {
                        //切换到wifi环境，开始下载
                        if (getDownloadState() == PAUSED) {
                            logger.debug("已连到WiFi，当前下载状态是暂停，继续下载");
                            restartDownload();
                            //通知下载页面更新状态
                            if (onProgressListener2 != null) {
                                onProgressListener2.onDownloadStart(videoId);
                            }
                        } else if (getDownloadState() == STOPED) {
                            logger.debug("已连到WiFi，当前下载状态是停止，启动下载");
                            startDownload();
                        }
                    } else {
                        //切换到非wifi环境，暂停下载
                        logger.debug("已切换到非wifi环境，暂停下载");
                        pauseDownload();
                        //通知下载页面更新状态
                        if (onProgressListener2 != null) {
                            onProgressListener2.onDownloadStop();
                        }
                    }
                }
            } else if (intent.getAction().equals(QUIT_DOWNLOADING)) {
                removeAllTask();
            }
        }
    };
    private String videoId;


    /**
     * 重新开始下载任务
     */
    public void restartDownload() {
        setDownloadState(RUNNING);
        dm.restartDownload();
    }

    /**
     * 暂停下载任务
     */
    public void pauseDownload() {
        setDownloadState(PAUSED);
        dm.pauseDownload();
    }

    /**
     * 删除所有下载任务
     */
    public void removeAllTask() {
        setDownloadState(STOPED);
        videoList.clear();
        deleteCurTask();
    }

    /**
     * 删除任务
     *
     * @param deleteList
     * @param deleteDownloadingTask
     */
    public void deleteTask(List<String> deleteList, boolean deleteDownloadingTask) {
        //删除掉被选择的任务
        if (deleteList != null) {
            for (String s : deleteList) {
                videoList.remove(s);
            }
        }
        if (deleteDownloadingTask) {
            deleteCurTask();
        }
        if (videoList.isEmpty()) {
            setDownloadState(STOPED);
        }
    }

    /**
     * 删除当前正在下载的任务
     */
    private void deleteCurTask() {
        AsyncTask<Void, Void, Boolean> deleteTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return videoManager.deleteVideoByVideoId(videoId);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    startDownload();
                }
            }
        };
        deleteTask.execute();

    }

    /**
     * 获取当前下载进度
     *
     * @return
     */
    public int getProgress() {
        return progress;
    }

    /**
     * 获取当前下载状态
     *
     * @return
     */
    public int getDownloadState() {
        return downloadState.get();
    }


    /**
     * 获取当前正在下载的videoId
     *
     * @return
     */
    public String getCurrVideoId() {
        return videoId;
    }

    /**
     * 获取缓存路径
     *
     * @param context
     * @param uniqueName
     * @return
     */
    private File getDiskCacheDir(Context context, String uniqueName) {
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = Environment.getExternalStorageDirectory().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }


    //下载成功标志
    private static final int DOWNLOAD_SUCC = 0;
    //下载失败标志
    private static final int DOWNLOAD_FAIL = 1;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgType = msg.what;
            switch (msgType) {
                case DOWNLOAD_SUCC:
                    if (onProgressListener1 != null) {
                        onProgressListener1.onVideoDownloadSuccess();
                    }
                    startDownload();
                    break;
                case DOWNLOAD_FAIL:
                    String failVideoId = (String) msg.obj;
                    videoList.add(failVideoId);
                    //如果当前的失败状态不是因为设备网络异常导致的(如果设备网络异常会导致暂停)，那么继续下载下一个视频
                    String networkState = NetworkUtils.getNetworkState(DownloadService2.this);
                    if (!networkState.equals(Constant.NETTYPE_UNCONNECTED) && !networkState.equals(Constant.NETTYPE_UNKNOWN)) {
                        startDownload();
                    } else {
                        setDownloadState(STOPED);
                        if (onProgressListener2 != null) {
                            onProgressListener2.onDownloadStop();
                        }
                    }
                    if (onProgressListener2 != null) {
                        onProgressListener2.onVideoDownloadFailed(failVideoId);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //进度通知接口
    public interface OnProgressListener {
        public void onDownloadStart(String videoId);

        public void onDownloadStop();

        public void onProgress(int progress, int downloadingNum, String title);

        public void onVideoDownloadFailed(String videoId);

        public void onVideoDownloadSuccess();
    }

    //有两个地方需要监听:一个是我的缓存页面，另一个是正在缓存页面
    private OnProgressListener onProgressListener1;
    private OnProgressListener onProgressListener2;

    public void setProgressListener1(OnProgressListener onProgressListener) {
        onProgressListener1 = onProgressListener;
    }

    public void setProgressListener2(OnProgressListener onProgressListener) {
        onProgressListener2 = onProgressListener;
    }

    /**
     * 返回一个Binder对象
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public DownloadService2 getService() {
            return DownloadService2.this;
        }
    }
}
