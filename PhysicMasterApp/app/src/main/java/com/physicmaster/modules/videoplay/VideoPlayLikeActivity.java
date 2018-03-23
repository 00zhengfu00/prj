package com.physicmaster.modules.videoplay;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.study.fragment.dialogfragment.CommonDialogFragment;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.db.VideoManager;
import com.physicmaster.modules.videoplay.cache.service.DownloadService2;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.CommonResponse;
import com.physicmaster.net.response.Response;
import com.physicmaster.net.response.course.VideoDownloadInfoResponse;
import com.physicmaster.net.response.video.LikeVideoPlayResponse;
import com.physicmaster.net.response.video.LikeVideoPlayResponse.DataBean.VideoBean;
import com.physicmaster.net.security.AESEncryption;
import com.physicmaster.net.service.course.CollectVideoService;
import com.physicmaster.net.service.course.UnCollectVideoService;
import com.physicmaster.net.service.video.LikeVideoPlayService;
import com.physicmaster.net.service.video.VideoDownloadInfoService;
import com.physicmaster.utils.MD5;
import com.physicmaster.utils.NetworkUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class VideoPlayLikeActivity extends BaseActivity {
    private JCVideoPlayerStandard mVideoView;
    private String videoId;
    private int collectState = 0;
    private long startTime;
    private String videoQuality;
    private String videoTime;
    private int progress;
    private int questionCount = 0;
    private int pointValue;
    private File file;
    private LikeVideoPlayResponse.DataBean.VideoBean appVideoStudyVo;

    /**
     * 判断服务端返回的清晰度控制字段是否合法
     *
     * @param quality
     * @return
     */
    private boolean isQualitylegal(String quality) {
        quality = quality.toUpperCase();
        return quality.equals("FHD") || quality.equals("HD") || quality.equals("SD") || quality
                .equals("LD");
    }

    //开始播放视频
    private void startPlay(final String info, final String title) {
        String networkState = NetworkUtils.getNetworkState(this);
        if (networkState.equals(Constant.NETTYPE_UNCONNECTED)) {
            UIUtils.showToast(this, "网络不可用");
            return;
        }
        //非wifi环境下弹出提示
        if (!networkState.equals(Constant.NETTYPE_WIFI)) {
            int promptTimes = BaseApplication.getNone_wifi_prompt_times();
            //如果已经提醒超过两次将不再提醒
            if (promptTimes < 2) {
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "继续播放", new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BaseApplication.setNone_wifi_prompt_times(BaseApplication
                                .getNone_wifi_prompt_times() + 1);
                        play(info, title);
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BaseApplication.setNone_wifi_prompt_times(0);
                        VideoPlayLikeActivity.this.finish();
                    }
                });
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        VideoPlayLikeActivity.this.finish();
                    }
                });
                dialog.setTitle("您正在使用非wifi网络，播放将产生流量费用");
                dialog.show();
            } else {
                play(info, title);
            }
        } else {
            play(info, title);
        }
    }

    private void play(String info, String title) {
        file = new File(getCacheDir(), "videoplay.m3u8");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(info.getBytes());
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
        mVideoView.setUp(file.getAbsolutePath(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, title);
        mVideoView.setOnDownloadBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCache();
            }
        });
        mVideoView.setOnLikeBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectState == 0) {
                    likeVideo();
                } else {
                    unLikeVideo();
                }
            }
        });
        mVideoView.setOnBackBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mVideoView.startPlayLogic();
        startTime = System.currentTimeMillis();
    }

    /**
     * 缓存清晰度设置
     */
    private void saveQuality(int position) {
        //        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys
        //                .VIDEO_QUELITY, Constant.videoQualities[position]);
        //        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys
        //                .VIDEO_QUELITY_TAG, Constant.videoQualitiesTag[position]);
    }

    /**
     * 切换视频播放清晰度
     */
    private void switchQuality(int quality) {
        //        if (videoList != null) {
        //            getData(videoList.get(index).videoId);
        //        } else {
        //            getData(videoId);
        //        }
    }

    /**
     * 获取已缓存的视频路径
     *
     * @param videoId
     * @return
     */
    private String getDownloadedVideoPath(String videoId) {
        VideoInfo videoInfo = checkVideoCached(videoId);
        if (videoInfo != null) {
            File file = new File(videoInfo.getVideoPath());
            FileInputStream fileInputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;
            StringBuilder encryptStr = new StringBuilder();
            try {
                fileInputStream = new FileInputStream(file);
                inputStreamReader = new InputStreamReader(fileInputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    encryptStr.append(line);
                }
                String decryptStr = AESEncryption.decrypt(encryptStr.toString(), MD5.hexdigest
                        (BaseApplication.getDeviceID()).substring(0, 16));
                file = new File(getCacheDir(), "videoplay.m3u8");
                if (file.exists()) {
                    file.delete();
                }
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    outputStream.write(decryptStr.getBytes());
                    return file.getAbsolutePath();
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取视频播放数据
     */
    private void getVideoPlayData() {
        videoId = getIntent().getStringExtra("videoId");
        String networkState = NetworkUtils.getNetworkState(this);
        if (networkState.equals(Constant.NETTYPE_UNCONNECTED)) {
            String videoPath = getDownloadedVideoPath(videoId + "");
            if (!TextUtils.isEmpty(videoPath)) {
                playLocal(videoPath, "缓存视频");
            }
        } else {
            final LikeVideoPlayService service = new LikeVideoPlayService(this);
            final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
            service.setCallback(new IOpenApiDataServiceCallback<LikeVideoPlayResponse>() {

                @Override
                public void onGetData(LikeVideoPlayResponse data) {
                    appVideoStudyVo = data.data.video;
                    verifyReadStoragePermissions();
                    loadingDialog.dismissDialog();
                }

                @Override
                public void onGetError(int errorCode, String errorMsg, Throwable error) {
                    UIUtils.showToast(VideoPlayLikeActivity.this, errorMsg);
                    loadingDialog.dismissDialog();
                }
            });
            loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
                @Override
                public void onCancel() {
                    service.cancel();
                }
            });
            service.postLogined("videoId=" + videoId, false);
        }
    }

    /**
     * 本地播放视频
     *
     * @param videoLocalPath
     */
    private void playLocal(String videoLocalPath, String title) {
        mVideoView.setUp(videoLocalPath, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, title);
        mVideoView.setOnDownloadBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCache();
            }
        });
        mVideoView.setOnLikeBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectState == 0) {
                    likeVideo();
                } else {
                    unLikeVideo();
                }
            }
        });
        mVideoView.setOnBackBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mVideoView.startPlayLogic();
        startTime = System.currentTimeMillis();
    }

    private void refreshUI(VideoBean  appVideoStudyVo) {
        String videoPath = getDownloadedVideoPath(videoId + "");
        //如果未缓存->走网络播放
        if (TextUtils.isEmpty(videoPath)) {
            startPlay(appVideoStudyVo.m3u8Content, appVideoStudyVo.videoTitle);
        } else {
            //如果已缓存->走本地播放
            File file = new File(videoPath);
            if (file.exists()) {
                playLocal(videoPath, appVideoStudyVo.videoTitle);
            } else {
                startPlay(appVideoStudyVo.m3u8Content, appVideoStudyVo.videoTitle);
            }
        }
        collectState = 1;
        videoQuality = appVideoStudyVo.videoQuality;
        mVideoView.setLikeBtnState(true);

        AsyncTask<String, Integer, VideoInfo> checkTask = new AsyncTask<String, Integer,
                VideoInfo>() {
            @Override
            protected VideoInfo doInBackground(String... params) {
                return checkVideoCached(params[0]);
            }

            @Override
            protected void onPostExecute(VideoInfo videoInfo) {
                super.onPostExecute(videoInfo);
                if (videoInfo != null) {
                    mVideoView.setDownloadBtnState(true);
                } else {
                    mVideoView.setDownloadBtnState(false);
                }
            }
        };
        checkTask.execute(appVideoStudyVo.videoId + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void findViewById() {
        mVideoView = (JCVideoPlayerStandard) findViewById(R.id.videoView);
        JCVideoPlayer.setJcUserAction(new MyUserActionStandard());
    }

    @Override
    protected void initView() {
        getVideoPlayData();
    }

    /**
     * 缓存视频
     */
    private void confirmCache() {
        String networkState = NetworkUtils.getNetworkState(this);
        if (networkState.equals(Constant.NETTYPE_UNCONNECTED)) {
            UIUtils.showToast(this, "网络不可用");
            return;
        }
        if (!networkState.equals(Constant.NETTYPE_WIFI)) {
            int promptTimes = BaseApplication.getNone_wifi_prompt_times();
            //如果已经提醒超过两次将不再提醒
            if (promptTimes < 2) {
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "继续缓存", new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BaseApplication.setNone_wifi_prompt_times(BaseApplication
                                .getNone_wifi_prompt_times() + 1);
                        verifyWriteStoragePermissions();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BaseApplication.setNone_wifi_prompt_times(0);
                        VideoPlayLikeActivity.this.finish();
                    }
                });
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        VideoPlayLikeActivity.this.finish();
                    }
                });
                dialog.setTitle("您正在使用非wifi网络，缓存将产生流量费用");
                dialog.show();
            } else {
                verifyWriteStoragePermissions();
            }
        } else {
            verifyWriteStoragePermissions();
        }
    }

    /**
     * 检测视频是否可以缓存
     */
    private void getDownloadVideoInfo() {
        VideoDownloadInfoService service = new VideoDownloadInfoService(this);
        service.setCallback(new IOpenApiDataServiceCallback<VideoDownloadInfoResponse>() {
            @Override
            public void onGetData(VideoDownloadInfoResponse data) {
                if (data.data.videoDownloadVo != null) {
                    startCache();
                } else {
                    CommonDialogFragment buyFragment = new CommonDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "购买会员即可下载视频");
                    buyFragment.setArguments(bundle);
                    buyFragment.show(getSupportFragmentManager(), "buyMember");
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                if (Response.CODE_NO_PERMISSION == errorCode) {
                    CommonDialogFragment buyFragment = new CommonDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "购买会员即可下载视频");
                    buyFragment.setArguments(bundle);
                    buyFragment.show(getSupportFragmentManager(), "buyMember");
                } else {
                    UIUtils.showToast(VideoPlayLikeActivity.this, errorMsg);
                }
            }
        });
        String realVideoId = videoId.split("-")[0];
        service.postLogined("videoId=" + realVideoId, false);
    }

    private void startCache() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //sdcard状态是没有挂载的情况
            UIUtils.showToast(VideoPlayLikeActivity.this, "sdcard不存在或未挂载");
            return;
        }
        if (null == appVideoStudyVo) {
            UIUtils.showToast(VideoPlayLikeActivity.this, "视频信息不存在，无法缓存");
            return;
        }
        AsyncTask<String, Integer, Boolean> task = new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                VideoManager videoManager = new VideoManager(VideoPlayLikeActivity.this);
                VideoInfo videoInfoExist = videoManager.getVideoInfo(appVideoStudyVo.videoId + "-" +
                        BaseApplication.getUserData().dtUserId);
                if (videoInfoExist == null) {
                    VideoInfo videoInfo = new VideoInfo();
                    videoInfo.setCreateDatetime(System.currentTimeMillis() + "");
                    videoInfo.setId(appVideoStudyVo.videoId + "-" + BaseApplication.getUserData().dtUserId);
                    videoInfo.setName(appVideoStudyVo.videoTitle);
                    videoInfo.setPosterUrl(appVideoStudyVo.posterUrl);
                    videoInfo.setState(VideoInfo.STATE_ADD);
                    videoManager.addOrUpdateVideo(videoInfo);
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                UIUtils.showToast(VideoPlayLikeActivity.this, "已加入缓存列表");
                if (aBoolean) {
                    Intent intent = new Intent(VideoPlayLikeActivity.this, DownloadService2.class);
                    intent.putExtra("videoId", videoId + "-" + BaseApplication.getUserData().dtUserId);
                    startService(intent);
                }
            }
        };
        task.execute("");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_video_play_v2;
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    class MyUserActionStandard implements JCUserActionStandard {

        @Override
        public void onEvent(int type, String url, int screen, Object... objects) {
            switch (type) {
                case JCUserAction.ON_CLICK_START_ICON:
                    Log.i("USER_EVENT", "ON_CLICK_START_ICON" + " title is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_CLICK_START_ERROR:
                    Log.i("USER_EVENT", "ON_CLICK_START_ERROR" + " title is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_CLICK_START_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_CLICK_START_AUTO_COMPLETE" + " title is : " +
                            (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " " +
                            "screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_PAUSE:
                    Log.i("USER_EVENT", "ON_CLICK_PAUSE" + " title is : " + (objects.length == 0
                            ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_RESUME:
                    Log.i("USER_EVENT", "ON_CLICK_RESUME" + " title is : " + (objects.length == 0
                            ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_SEEK_POSITION" + " title is : " + (objects.length ==
                            0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_AUTO_COMPLETE" + " title is : " + (objects.length ==
                            0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    UIUtils.showToast(VideoPlayLikeActivity.this, "播放完成");
                    break;
                case JCUserAction.ON_ENTER_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_FULLSCREEN" + " title is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_QUIT_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_FULLSCREEN" + " title is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_ENTER_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_TINYSCREEN" + " title is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_QUIT_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_TINYSCREEN" + " title is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_VOLUME" + " title is : " + (objects
                            .length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            "" + screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_POSITION" + " title is : " +
                            (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " " +
                            "screen is : " + screen);
                    break;
                case JCUserActionStandard.ON_CLICK_START_THUMB:
                    Log.i("USER_EVENT", "ON_CLICK_START_THUMB" + " title is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserActionStandard.ON_CLICK_BLANK:
                    Log.i("USER_EVENT", "ON_CLICK_BLANK" + " title is : " + (objects.length == 0
                            ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                default:
                    Log.i("USER_EVENT", "unknow");
                    break;
            }
        }
    }

    /**
     * 收藏视频
     */
    private void likeVideo() {
        CollectVideoService service = new CollectVideoService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                UIUtils.showToast(VideoPlayLikeActivity.this, "收藏视频成功");
                collectState = 1;
                mVideoView.setLikeBtnState(true);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(VideoPlayLikeActivity.this, errorMsg);
            }
        });
        service.postLogined("videoId=" + videoId, false);
    }

    /**
     * 取消收藏视频
     */
    private void unLikeVideo() {
        UnCollectVideoService service = new UnCollectVideoService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                UIUtils.showToast(VideoPlayLikeActivity.this, "取消收藏视频成功");
                collectState = 0;
                mVideoView.setLikeBtnState(false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(VideoPlayLikeActivity.this, errorMsg);
            }
        });
        service.postLogined("videoId=" + videoId, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄露
        JCMediaManager.textureView = null;
        JCMediaManager.savedSurfaceTexture = null;
        JCVideoPlayer.setJcUserAction(null);
    }

    /**
     * 根据m3u8内容获取ts文件列表
     *
     * @param m3u8File
     * @return
     */
    private List<String> getTSList(File m3u8File) {
        List<String> tsList = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(m3u8File));
            String tempS = "";
            while ((tempS = bufferedReader.readLine()) != null) {
                if ((tempS.startsWith("http") || tempS.startsWith("https")) && tempS.endsWith("" +
                        ".ts")) {
                    tsList.add(tempS);
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tsList;
    }

    /**
     * 查询视频是否已缓存
     *
     * @param videoId
     * @return
     */
    private VideoInfo checkVideoCached(String videoId) {
        VideoManager manager = new VideoManager(this);
        VideoInfo videoInfo = manager.getVideoInfo(videoId + "-" + BaseApplication.getUserData()
                .dtUserId);
        if (videoInfo != null) {
            if (videoInfo.getState() == 2) {
                return videoInfo;
            }
        }
        return null;
    }


    //动态获取文件读取权限
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 8      * Checks if the app has permission to write to device storage
     * 9      *
     * 10      * If the app does not has permission then the user will be prompted to
     * 11      * grant permissions
     * 12      *
     * 13      * @param activity
     * 14
     */
    public void verifyWriteStoragePermissions() {
        // Check if we have write permission
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                        REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                getDownloadVideoInfo();
            }
        } else {
            getDownloadVideoInfo();
        }
    }

    /**
     * 8      * Checks if the app has permission to write to device storage
     * 9      *
     * 10      * If the app does not has permission then the user will be prompted to
     * 11      * grant permissions
     * 12      *
     * 13      * @param activity
     * 14
     */
    public void verifyReadStoragePermissions() {
        // Check if we have write permission
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                        REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                refreshUI(appVideoStudyVo);
            }
        } else {
            refreshUI(appVideoStudyVo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予成功,初始化
                getDownloadVideoInfo();
            } else {
                UIUtils.showToast(VideoPlayLikeActivity.this, "您拒绝了存储权限，无法缓存视频");
            }
        } else if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予成功,初始化
                refreshUI(appVideoStudyVo);
            } else {
                UIUtils.showToast(VideoPlayLikeActivity.this, "您拒绝了存储权限，无法播放缓存视频，即将播放网络视频");
                startPlay(appVideoStudyVo.m3u8Content, appVideoStudyVo.videoTitle);
                collectState = 1;
                videoQuality = appVideoStudyVo.videoQuality;
                if (collectState == 1) {
                    mVideoView.setLikeBtnState(true);
                } else {
                    mVideoView.setLikeBtnState(true);
                }

                AsyncTask<String, Integer, VideoInfo> checkTask = new AsyncTask<String, Integer,
                        VideoInfo>() {
                    @Override
                    protected VideoInfo doInBackground(String... params) {
                        return checkVideoCached(params[0]);
                    }

                    @Override
                    protected void onPostExecute(VideoInfo videoInfo) {
                        super.onPostExecute(videoInfo);
                        if (videoInfo != null) {
                            mVideoView.setDownloadBtnState(true);
                        } else {
                            mVideoView.setDownloadBtnState(false);
                        }
                    }
                };
                checkTask.execute(appVideoStudyVo.videoId + "");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}