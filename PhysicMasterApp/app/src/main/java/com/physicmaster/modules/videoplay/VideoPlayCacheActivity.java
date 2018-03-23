package com.physicmaster.modules.videoplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.db.VideoManager;
import com.physicmaster.net.security.AESEncryption;
import com.physicmaster.utils.MD5;
import com.physicmaster.utils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;

public class VideoPlayCacheActivity extends FragmentActivity {

    private File file;
    private JCVideoPlayerStandard mVideoView;
    private String videoId;
    private String videoTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        findViewById();
        initView();
    }

    protected void findViewById() {
        mVideoView = (JCVideoPlayerStandard) findViewById(R.id.videoView);
        mVideoView.setLikeBtnVisible(View.GONE);
        mVideoView.setDownloadBtnVisible(View.GONE);
        mVideoView.setOnBackBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    protected void initView() {
        Intent intent = getIntent();
        videoId = intent.getStringExtra("videoId");
        videoTitle = intent.getStringExtra("videoTitle");
        JCVideoPlayer.setJcUserAction(new MyUserActionStandard());
        refreshUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
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
     * 本地播放视频
     *
     * @param videoLocalPath
     */
    private void playLocal(String videoLocalPath, String title) {
        mVideoView.setUp(videoLocalPath, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, title);
        mVideoView.startPlayLogic();
    }

    private void refreshUI() {
        String videoPath = getDownloadedVideoPath(videoId + "");
        //如果未缓存->走网络播放
        if (TextUtils.isEmpty(videoPath)) {
            UIUtils.showToast(VideoPlayCacheActivity.this, "视频不存在");
        } else {
            //如果已缓存->走本地播放
            File file = new File(videoPath);
            if (file.exists()) {
                playLocal(videoPath, videoTitle);
            } else {
                UIUtils.showToast(VideoPlayCacheActivity.this, "视频不存在");
            }
        }
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
                String decryptStr = AESEncryption.decrypt(encryptStr.toString(), MD5.hexdigest(BaseApplication.getDeviceID()).substring(0, 16));
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
     * 查询视频是否已缓存
     *
     * @param videoId
     * @return
     */
    private VideoInfo checkVideoCached(String videoId) {
        VideoManager manager = new VideoManager(this);
        VideoInfo videoInfo = manager.getVideoInfo(videoId);
        if (videoInfo != null) {
            if (videoInfo.getState() == 2) {
                return videoInfo;
            }
        }
        return null;
    }

    private void play(String info, String title) {
        file = new File(this.getCacheDir(), "videoplay.m3u8");
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
        mVideoView.setOnBackBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mVideoView.startPlayLogic();
    }

    class MyUserActionStandard implements JCUserActionStandard {

        @Override
        public void onEvent(int type, String url, int screen, Object... objects) {
            switch (type) {
                case JCUserAction.ON_CLICK_START_ICON:
                    Log.i("USER_EVENT", "ON_CLICK_START_ICON" + " videoTitle is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_CLICK_START_ERROR:
                    Log.i("USER_EVENT", "ON_CLICK_START_ERROR" + " videoTitle is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_CLICK_START_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_CLICK_START_AUTO_COMPLETE" + " videoTitle is : " +
                            (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " " +
                            "screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_PAUSE:
                    Log.i("USER_EVENT", "ON_CLICK_PAUSE" + " videoTitle is : " + (objects.length == 0
                            ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_RESUME:
                    Log.i("USER_EVENT", "ON_CLICK_RESUME" + " videoTitle is : " + (objects.length == 0
                            ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_SEEK_POSITION" + " videoTitle is : " + (objects.length ==
                            0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_AUTO_COMPLETE" + " videoTitle is : " + (objects.length ==
                            0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    UIUtils.showToast(mContext, "播放完成");
                    break;
                case JCUserAction.ON_ENTER_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_FULLSCREEN" + " videoTitle is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_QUIT_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_FULLSCREEN" + " videoTitle is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_ENTER_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_TINYSCREEN" + " videoTitle is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_QUIT_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_TINYSCREEN" + " videoTitle is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_VOLUME" + " videoTitle is : " + (objects
                            .length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            "" + screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_POSITION" + " videoTitle is : " +
                            (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " " +
                            "screen is : " + screen);
                    break;
                case JCUserActionStandard.ON_CLICK_START_THUMB:
                    Log.i("USER_EVENT", "ON_CLICK_START_THUMB" + " videoTitle is : " + (objects.length
                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
                            screen);
                    break;
                case JCUserActionStandard.ON_CLICK_BLANK:
                    Log.i("USER_EVENT", "ON_CLICK_BLANK" + " videoTitle is : " + (objects.length == 0
                            ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                default:
                    Log.i("USER_EVENT", "unknow");
                    break;
            }
        }
    }

    protected int getContentLayout() {
        return R.layout.activity_video_play_v2;
    }
}
