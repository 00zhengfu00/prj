package com.physicmaster.modules.explore.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.course.NewVideoPlayResponse;
import com.physicmaster.net.service.video.NewVideoPlayDetailService;
import com.physicmaster.utils.NetworkUtils;
import com.physicmaster.utils.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class VideoPlayActivity extends BaseActivity {

    private File                  file;
    private JCVideoPlayerStandard mVideoView;

    protected void findViewById() {
        mVideoView = (JCVideoPlayerStandard) findViewById(R.id.videoView);
        mVideoView.setLikeBtnVisible(View.GONE);
        mVideoView.setDownloadBtnVisible(View.GONE);
    }

    protected void initView() {
        Intent intent = getIntent();
        String videoId = intent.getStringExtra("videoId");

        JCVideoPlayer.setJcUserAction(new MyUserActionStandard());
        playVideo(videoId);
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


    private void playVideo(String videoId) {
        final NewVideoPlayDetailService service = new NewVideoPlayDetailService(VideoPlayActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<NewVideoPlayResponse>() {

            @Override
            public void onGetData(NewVideoPlayResponse data) {
                NewVideoPlayResponse.DataBean.NewVideoBean newVideo = data.data.newVideo;

                startPlay(newVideo.m3u8Content, newVideo.videoTitle);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(VideoPlayActivity.this, errorMsg);
            }
        });
        service.postLogined("videoId=" + videoId, false);
    }

    //开始播放视频
    private void startPlay(final String info, final String title) {
        String networkState = NetworkUtils.getNetworkState(VideoPlayActivity.this);
        if (networkState.equals(Constant.NETTYPE_UNCONNECTED)) {
            UIUtils.showToast(VideoPlayActivity.this, "网络不可用");
            return;
        }
        //非wifi环境下弹出提示
        if (!networkState.equals(Constant.NETTYPE_WIFI)) {
            int promptTimes = BaseApplication.getNone_wifi_prompt_times();
            //如果已经提醒超过两次将不再提醒
            if (promptTimes < 2) {
                AlertDialog dialog = new AlertDialog.Builder(VideoPlayActivity.this).create();
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
                        VideoPlayActivity.this.finish();
                    }
                });
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        VideoPlayActivity.this.finish();
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
                    UIUtils.showToast(VideoPlayActivity.this, "播放完成");
                    finish();
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

    protected int getContentLayout() {
        return R.layout.activity_video_play_v2;
    }
}
