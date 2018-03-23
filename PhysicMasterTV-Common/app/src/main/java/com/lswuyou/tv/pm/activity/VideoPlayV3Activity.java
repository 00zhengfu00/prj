//package com.lswuyou.tv.pm.activity;
//
//import android.content.Intent;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Toast;
//
//import com.lswuyou.tv.pm.R;
//import com.lswuyou.tv.pm.common.CacheKeys;
//import com.lswuyou.tv.pm.common.CacheManager;
//import com.lswuyou.tv.pm.common.Constant;
//import com.lswuyou.tv.pm.databean.VideoDataWrapper;
//import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
//import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
//import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;
//import com.lswuyou.tv.pm.net.response.video.GetVideoPlayResponse;
//import com.lswuyou.tv.pm.net.service.GetVideoPlayLoginedService;
//import com.lswuyou.tv.pm.net.service.GetVideoPlayUnLoginService;
//import com.lswuyou.tv.pm.utils.UIUtils;
//import com.lswuyou.tv.pm.utils.Utils;
//import com.lswuyou.tv.pm.view.MenuPopupWindow;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
//import fm.jiecao.jcvideoplayer_lib.JCUserAction;
//import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
//import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
//import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
//
//
//public class VideoPlayV3Activity extends BaseActivity {
//
//    private File file;
//    private JCVideoPlayerStandard mVideoView;
//    private Toast toast;
//    private String quality;
//    private int chapterId;
//    private int videoId;
//    private long lastTimeMillis = 0;
//    private int index;
//    private List<VideoInfo> videoList;
//    private MenuPopupWindow menu;
//    private long playProgress = 0;
//
//    protected void findViewById() {
//        mVideoView = (JCVideoPlayerStandard) findViewById(R.id.videoView);
//        mVideoView.requestFocus();
//    }
//
//    protected void initView() {
//        Intent intent = getIntent();
//        videoId = intent.getIntExtra("videoId", 0);
//        chapterId = intent.getIntExtra("chapterId", 0);
//        index = intent.getIntExtra("index", 0);
//        VideoDataWrapper videoInfoWraper = (VideoDataWrapper) intent.getSerializableExtra("videoList");
//        if (videoInfoWraper != null) {
//            videoList = videoInfoWraper.videoList;
//        }
//        JCVideoPlayer.setJcUserAction(new MyUserActionStandard());
//        playVideo(videoId);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        JCVideoPlayer.releaseAllVideos();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //防止内存泄露
//        JCMediaManager.textureView = null;
//        JCMediaManager.savedSurfaceTexture = null;
//        JCVideoPlayer.setJcUserAction(null);
//    }
//
//
//    private void playVideo(int videoId) {
//        OpenApiDataServiceBase service = null;
//        if (Utils.isUserLogined()) {
//            service = new GetVideoPlayLoginedService(this);
//        } else {
//            service = new GetVideoPlayUnLoginService(this);
//        }
//        service.setCallback(new IOpenApiDataServiceCallback<GetVideoPlayResponse>() {
//            @Override
//            public void onGetData(GetVideoPlayResponse data) {
//                try {
//                    startPlay(data.data.videoM3u8Vo.m3u8Content, data.data.videoM3u8Vo.title);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onGetError(int errorCode, String errorMsg, Throwable error) {
//                Toast.makeText(VideoPlayV3Activity.this, errorMsg, Toast.LENGTH_SHORT).show();
//            }
//        });
//        //videoId
//        //videoQuality 1:全高清FHD  2:高清HD  3:标清SD   4:流畅LD
//        //先获取用户设置的视频清晰度，如果用户未设置，视频播放采用服务端推荐的清晰度
//        quality = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys
//                .VIDEO_QUELITY_TAG);
//        if (TextUtils.isEmpty(quality)) {
//            quality = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys
//                    .RECOMMEND_QUALITY);
//        }
//        if (TextUtils.isEmpty(quality) || !isQualitylegal(quality)) {
//            quality = "HD";
//        }
//        if (Utils.isUserLogined()) {
//            if (chapterId != 0) {
//                service.postAES("videoId=" + videoId + "&videoQuality=" + quality + "&chapterId=" + chapterId, true);
//            } else {
//                service.postAES("videoId=" + videoId + "&videoQuality=" + quality, true);
//            }
//        } else {
//            if (chapterId != 0) {
//                service.post("videoId=" + videoId + "&videoQuality=" + quality + "&chapterId=" + chapterId, true);
//            } else {
//                service.post("videoId=" + videoId + "&videoQuality=" + quality, true);
//            }
//        }
//    }
//
//    /**
//     * 判断服务端返回的清晰度控制字段是否合法
//     *
//     * @param quality
//     * @return
//     */
//    private boolean isQualitylegal(String quality) {
//        quality = quality.toUpperCase();
//        if (quality.equals("FHD") || quality.equals("HD") || quality.equals("SD") || quality
//                .equals("LD")) {
//            return true;
//        }
//        return false;
//    }
//
//    //开始播放视频
//    private void startPlay(final String info, final String title) {
//        play(info, title);
//    }
//
//    private void play(String info, String title) {
//        file = new File(this.getCacheDir(), "videoplay.m3u8");
//        if (file.exists()) {
//            file.delete();
//        }
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        FileOutputStream outputStream = null;
//        try {
//            outputStream = new FileOutputStream(file);
//            outputStream.write(info.getBytes());
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
//        mVideoView.setUp(file.getAbsolutePath(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, title);
//        mVideoView.startPlayLogic();
//    }
//
//    class MyUserActionStandard implements JCUserActionStandard {
//
//        @Override
//        public void onEvent(int type, String url, int screen, Object... objects) {
//            switch (type) {
//                case JCUserAction.ON_CLICK_START_ICON:
//                    Log.i("USER_EVENT", "ON_CLICK_START_ICON" + " title is : " + (objects.length
//                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
//                            screen);
//                    break;
//                case JCUserAction.ON_CLICK_START_ERROR:
//                    Log.i("USER_EVENT", "ON_CLICK_START_ERROR" + " title is : " + (objects.length
//                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
//                            screen);
//                    break;
//                case JCUserAction.ON_CLICK_START_AUTO_COMPLETE:
//                    Log.i("USER_EVENT", "ON_CLICK_START_AUTO_COMPLETE" + " title is : " +
//                            (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " " +
//                            "screen is : " + screen);
//                    break;
//                case JCUserAction.ON_CLICK_PAUSE:
//                    Log.i("USER_EVENT", "ON_CLICK_PAUSE" + " title is : " + (objects.length == 0
//                            ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
//                    break;
//                case JCUserAction.ON_CLICK_RESUME:
//                    Log.i("USER_EVENT", "ON_CLICK_RESUME" + " title is : " + (objects.length == 0
//                            ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
//                    break;
//                case JCUserAction.ON_SEEK_POSITION:
//                    Log.i("USER_EVENT", "ON_SEEK_POSITION" + " title is : " + (objects.length ==
//                            0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
//                    break;
//                case JCUserAction.ON_AUTO_COMPLETE:
//                    Log.i("USER_EVENT", "ON_AUTO_COMPLETE" + " title is : " + (objects.length ==
//                            0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
//                    UIUtils.showToast(VideoPlayV3Activity.this, "播放完成");
//                    finish();
//                    break;
//                case JCUserAction.ON_ENTER_FULLSCREEN:
//                    Log.i("USER_EVENT", "ON_ENTER_FULLSCREEN" + " title is : " + (objects.length
//                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
//                            screen);
//                    break;
//                case JCUserAction.ON_QUIT_FULLSCREEN:
//                    Log.i("USER_EVENT", "ON_QUIT_FULLSCREEN" + " title is : " + (objects.length
//                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
//                            screen);
//                    break;
//                case JCUserAction.ON_ENTER_TINYSCREEN:
//                    Log.i("USER_EVENT", "ON_ENTER_TINYSCREEN" + " title is : " + (objects.length
//                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
//                            screen);
//                    break;
//                case JCUserAction.ON_QUIT_TINYSCREEN:
//                    Log.i("USER_EVENT", "ON_QUIT_TINYSCREEN" + " title is : " + (objects.length
//                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
//                            screen);
//                    break;
//                case JCUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME:
//                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_VOLUME" + " title is : " + (objects
//                            .length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
//                            "" + screen);
//                    break;
//                case JCUserAction.ON_TOUCH_SCREEN_SEEK_POSITION:
//                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_POSITION" + " title is : " +
//                            (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " " +
//                            "screen is : " + screen);
//                    break;
//                case JCUserActionStandard.ON_CLICK_START_THUMB:
//                    Log.i("USER_EVENT", "ON_CLICK_START_THUMB" + " title is : " + (objects.length
//                            == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " +
//                            screen);
//                    break;
//                case JCUserActionStandard.ON_CLICK_BLANK:
//                    Log.i("USER_EVENT", "ON_CLICK_BLANK" + " title is : " + (objects.length == 0
//                            ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
//                    break;
//                default:
//                    Log.i("USER_EVENT", "unknow");
//                    break;
//            }
//        }
//    }
//
//    protected int getContentLayout() {
//        return R.layout.activity_video_play_v3;
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (toast == null) {
//            toast = Toast.makeText(this, R.string.repeat_click_exit, Toast.LENGTH_SHORT);
//        }
//        long currentTimeMillis = System.currentTimeMillis();
//        if (lastTimeMillis != 0) {
//            if (currentTimeMillis - lastTimeMillis < 3000) {
//                toast.cancel();
//                playProgress = mVideoView.getCurrentPosition();
//                if (JCVideoPlayer.backPress()) {
//                    return;
//                }
//                super.onBackPressed();
//            } else {
//                lastTimeMillis = currentTimeMillis;
//                toast.show();
//            }
//        } else {
//            lastTimeMillis = currentTimeMillis;
//            toast.show();
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_MENU) {
//            int videoNum;
//            if (videoList == null) {
//                videoNum = 1;
//            } else {
//                videoNum = videoList.size();
//            }
//            menu = new MenuPopupWindow(this, new MenuPopupWindow.OnMenuActionListener() {
//
//                @Override
//                public void onXuanjiSelected(int jishu) {
//                    //点击正在播放的选集不响应
//                    if (jishu != index) {
//                        index = jishu;
//                        playVideo(videoList.get(jishu).videoId);
//                    }
//                }
//
//                @Override
//                public void onQualitySelected(int quality) {
//                    playProgress = mVideoView.getCurrentPosition();
//                    saveQuality(quality);
//                    switchQuality(quality);
//                }
//            }, quality, videoNum, index);
//            DisplayMetrics metrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            menu.showPopupWindow(mVideoView, metrics.widthPixels);
//        } else if (isControlKeyCode(keyCode)) {
//            //控制播放器
//            mVideoView.onKeyDown(keyCode, event);
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        return super.onKeyUp(keyCode, event);
//    }
//
//    /**
//     * 是否是播放控制代码
//     *
//     * @param keyCode
//     * @return
//     */
//    private boolean isControlKeyCode(int keyCode) {
//        return keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN ||
//                keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER;
//    }
//
//    /**
//     * 缓存清晰度设置
//     */
//    private void saveQuality(int position) {
//        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys
//                .VIDEO_QUELITY, Constant.videoQualities[position]);
//        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys
//                .VIDEO_QUELITY_TAG, Constant.videoQualitiesTag[position]);
//    }
//
//    /**
//     * 切换视频播放清晰度
//     */
//    private void switchQuality(int quality) {
//        if (videoList != null) {
//            playVideo(videoList.get(index).videoId);
//        } else {
//            playVideo(videoId);
//        }
//    }
//}
