package com.lswuyou.tv.pm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.common.Constant;
import com.lswuyou.tv.pm.databean.VideoDataWrapper;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;
import com.lswuyou.tv.pm.net.response.video.GetVideoPlayResponse;
import com.lswuyou.tv.pm.net.response.video.VideoPlayInfo;
import com.lswuyou.tv.pm.net.service.GetVideoPlayLoginedService;
import com.lswuyou.tv.pm.net.service.GetVideoPlayUnLoginService;
import com.lswuyou.tv.pm.utils.Utils;
import com.lswuyou.tv.pm.video.MediaController;
import com.lswuyou.tv.pm.video.VideoView;
import com.lswuyou.tv.pm.view.MenuPopupWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class VideoPlay2Activity extends Activity {
    private VideoView mVideoView;
    private MediaController mediaController;
    private int videoId;
    private int chapterId;
    private View mBufferingIndicator;
    private long lastTimeMillis = 0;
    private Toast toast;
    private MenuPopupWindow menu;
    private String quality;
    private ProgressBar pb;
    private TextView downloadRateView, loadRateView;
    private int index;
    private List<VideoInfo> videoList;
    private long playProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVideoView = (VideoView) findViewById(R.id.videoView);
        pb = (ProgressBar) findViewById(R.id.probar);
        mediaController = new MediaController(this);
        videoId = getIntent().getIntExtra("videoId", 0);
        chapterId = getIntent().getIntExtra("chapterId", 0);
        index = getIntent().getIntExtra("index", 0);
        VideoDataWrapper videoInfoWraper = (VideoDataWrapper) getIntent().getSerializableExtra
                ("videoList");
        if (videoInfoWraper != null) {
            videoList = videoInfoWraper.videoList;
        }
        getData(videoId);
    }

    //获取视频播放数据
    private void getData(int videoId) {
        OpenApiDataServiceBase service = null;
        if (Utils.isUserLogined()) {
            service = new GetVideoPlayLoginedService(this);
        } else {
            service = new GetVideoPlayUnLoginService(this);
        }
        service.setCallback(new IOpenApiDataServiceCallback<GetVideoPlayResponse>() {
            @Override
            public void onGetData(GetVideoPlayResponse data) {
                try {
                    refreshUI(data.data.videoM3u8Vo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(VideoPlay2Activity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        //videoId
        //videoQuality 1:全高清FHD  2:高清HD  3:标清SD   4:流畅LD
        //先获取用户设置的视频清晰度，如果用户未设置，视频播放采用服务端推荐的清晰度
        quality = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys
                .VIDEO_QUELITY_TAG);
        if (TextUtils.isEmpty(quality)) {
            quality = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys
                    .RECOMMEND_QUALITY);
        }
        if (TextUtils.isEmpty(quality) || !isQualitylegal(quality)) {
            quality = "HD";
        }
        if (Utils.isUserLogined()) {
            if (chapterId != 0) {
                service.postAES("videoId=" + videoId + "&videoQuality=" + quality + "&chapterId=" + chapterId, true);
            } else {
                service.postAES("videoId=" + videoId + "&videoQuality=" + quality, true);
            }
        } else {
            if (chapterId != 0) {
                service.post("videoId=" + videoId + "&videoQuality=" + quality + "&chapterId=" + chapterId, true);
            } else {
                service.post("videoId=" + videoId + "&videoQuality=" + quality, true);
            }
        }
    }

    /**
     * 判断服务端返回的清晰度控制字段是否合法
     *
     * @param quality
     * @return
     */
    private boolean isQualitylegal(String quality) {
        quality = quality.toUpperCase();
        if (quality.equals("FHD") || quality.equals("HD") || quality.equals("SD") || quality
                .equals("LD")) {
            return true;
        }
        return false;
    }

    //开始播放视频
    private void refreshUI(VideoPlayInfo info) {
        if (info.canPlay != 1) {
            Toast.makeText(VideoPlay2Activity.this, "视频不可播放！", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(getCacheDir(), "videoplay.m3u8");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream outputStream = null;
        if (!file.exists()) {
            return;
        }
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(info.m3u8Content.getBytes());
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
        mVideoView.setVideoPath(file.getAbsolutePath());
        mVideoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(mVideoView);
        mBufferingIndicator = findViewById(R.id.buffering_indicator);
        mVideoView.setMediaBufferingIndicator(mBufferingIndicator);
        mVideoView.requestFocus();
        mVideoView.start();
        if (playProgress > 0) {
            mVideoView.seekTo(playProgress);
        }
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                Toast.makeText(VideoPlay2Activity.this, "播放完成，2秒后将退出播放界面！", Toast.LENGTH_SHORT)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exitPlay();
                    }
                }, 2000);
            }
        });
        mVideoView.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer mp) {
                Log.e("SeekComplete,Position:", mp.getCurrentPosition() / 1000 + "秒");
            }
        });
        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        if (mVideoView.isPlaying()) {
                            mVideoView.pause();
                            pb.setVisibility(View.VISIBLE);
                        }
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mVideoView.start();
                        pb.setVisibility(View.GONE);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 退出播放界面
     */
    private void exitPlay() {
        VideoPlay2Activity.this.finish();
    }

    @Override
    public void onBackPressed() {
        if (toast == null) {
            toast = Toast.makeText(this, R.string.repeat_click_exit, Toast.LENGTH_SHORT);
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (lastTimeMillis != 0) {
            if (currentTimeMillis - lastTimeMillis < 3000) {
                toast.cancel();
                playProgress = mVideoView.getCurrentPosition();
                super.onBackPressed();
            } else {
                lastTimeMillis = currentTimeMillis;
                toast.show();
            }
        } else {
            lastTimeMillis = currentTimeMillis;
            toast.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            int videoNum;
            if (videoList == null) {
                videoNum = 1;
            } else {
                videoNum = videoList.size();
            }
            menu = new MenuPopupWindow(this, new MenuPopupWindow.OnMenuActionListener() {

                @Override
                public void onXuanjiSelected(int jishu) {
                    //点击正在播放的选集不响应
                    if (jishu != index) {
                        index = jishu;
                        getData(videoList.get(jishu).videoId);
                    }
                }

                @Override
                public void onQualitySelected(int quality) {
                    playProgress = mVideoView.getCurrentPosition();
                    saveQuality(quality);
                    switchQuality(quality);
                }
            }, quality, videoNum, index);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            menu.showPopupWindow(mVideoView, metrics.widthPixels);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 缓存清晰度设置
     */
    private void saveQuality(int position) {
        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys
                .VIDEO_QUELITY, Constant.videoQualities[position]);
        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys
                .VIDEO_QUELITY_TAG, Constant.videoQualitiesTag[position]);
    }

    /**
     * 切换视频播放清晰度
     */
    private void switchQuality(int quality) {
        if (videoList != null) {
            getData(videoList.get(index).videoId);
        } else {
            getData(videoId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (menu != null) {
            menu.dismiss();
        }
        mVideoView.stopPlayback();
    }
}
