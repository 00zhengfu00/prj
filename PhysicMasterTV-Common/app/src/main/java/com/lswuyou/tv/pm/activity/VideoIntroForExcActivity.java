package com.lswuyou.tv.pm.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.cache.CachedPlayRecord;
import com.lswuyou.tv.pm.channel.login.LoginManager;
import com.lswuyou.tv.pm.channel.pay.TvChannelType;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.databean.VideoDataWrapper;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.response.CommonResponse;
import com.lswuyou.tv.pm.net.response.account.GetLoginCfgResponse;
import com.lswuyou.tv.pm.net.response.video.GetVideoInfoResponse;
import com.lswuyou.tv.pm.net.response.video.VideoDetaiInfo;
import com.lswuyou.tv.pm.net.service.AddToFavService;
import com.lswuyou.tv.pm.net.service.GetLoginCfgService;
import com.lswuyou.tv.pm.net.service.GetVideoInfoLoginService;
import com.lswuyou.tv.pm.net.service.GetVideoInfoUnLoginService;
import com.lswuyou.tv.pm.utils.Utils;
import com.lswuyou.tv.pm.view.PayDialog;
import com.lswuyou.tv.pm.view.TitleBarView;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识点讲解视频
 */
public class VideoIntroForExcActivity extends BaseActivity {
    private TitleBarView mTitleBarView;
    private Button btnCollect;
    private Button btnPlay;
    private ImageView ivPoster;
    private VideoDetaiInfo videoDetailInfo;
    private TextView tvVIP;
    private int videoId;
    private int chapterId;
    private int index;
    private VideoDataWrapper dataWrapper;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_video_intro_for_exc;
    }

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnCollect = (Button) findViewById(R.id.btn_collect);
        ivPoster = (ImageView) findViewById(R.id.iv_poster);
        tvVIP = (TextView) findViewById(R.id.tv_vip);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoDetailInfo != null) {
                    if (videoDetailInfo.canPlay == 1) {
                        Intent intent = new Intent(VideoIntroForExcActivity.this, VideoPlay2Activity.class);
                        intent.putExtra("videoId", videoDetailInfo.videoId);
                        intent.putExtra("chapterId", chapterId);
                        intent.putExtra("index", index);
                        intent.putExtra("videoList", dataWrapper);
                        startActivity(intent);
                        //将播放记录缓存到本地
                        cacheRecord(videoDetailInfo);
                    } else {
                        if (Utils.isUserLogined()) {
                            PayDialog payDialog = new PayDialog();
                            payDialog.show(getFragmentManager(), "pay");
                        } else {
                            doLogin(1);
                        }
                    }
                }
            }
        });
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isUserLogined()) {
                    addToFav(videoId);
                } else {
                    doLogin(0);
                }
            }
        });
        videoId = getIntent().getIntExtra("videoId", 0);
        chapterId = getIntent().getIntExtra("chapterId", 0);
        index = getIntent().getIntExtra("index", 0);
        dataWrapper = (VideoDataWrapper) getIntent().getSerializableExtra("videoList");
        getVideoPlayData();
    }

    private void doLogin(final int flag) {
        GetLoginCfgService service = new GetLoginCfgService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetLoginCfgResponse>() {
            @Override
            public void onGetData(GetLoginCfgResponse data) {
                try {
                    String loginType = data.data.loginCfgVo.loginType;
                    if (loginType.equals(TvChannelType.none.name())) {
                        Intent intent = new Intent(VideoIntroForExcActivity.this, UnLoginActivity.class);
                        startActivity(intent);
                    } else {
                        LoginManager.login(VideoIntroForExcActivity.this, loginType);
                    }
                } catch (Exception e) {
                    Toast.makeText(VideoIntroForExcActivity.this, "获取登录配置异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(VideoIntroForExcActivity.this, "获取登录配置信息失败！", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        service.post("", true);
    }

    /**
     * 缓存播放记录到本地
     *
     * @param videoDetailInfo
     */
    private void cacheRecord(VideoDetaiInfo videoDetailInfo) {
        Object objPlayRecord = CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .PLAY_RECORD, CachedPlayRecord.class);
        CachedPlayRecord records;
        if (objPlayRecord == null) {
            records = new CachedPlayRecord();
            List<VideoDetaiInfo> infos1 = new ArrayList<>();
            infos1.add(videoDetailInfo);
            records.videoDetaiInfoList = infos1;
        } else {
            records = (CachedPlayRecord) objPlayRecord;
            if (records.videoDetaiInfoList == null) {
                records.videoDetaiInfoList = new ArrayList<>();
            }
            if (!findExistRecord(videoDetailInfo.videoId, records.videoDetaiInfoList)) {
                records.videoDetaiInfoList.add(videoDetailInfo);
            }
        }
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.PLAY_RECORD, records);
    }

    /**
     * 查找播放记录是否已经被添加
     *
     * @param videoId
     * @param videoDetaiInfoList
     * @return
     */
    private boolean findExistRecord(int videoId, List<VideoDetaiInfo> videoDetaiInfoList) {
        if (videoDetaiInfoList == null) {
            return false;
        }
        for (int i = 0; i < videoDetaiInfoList.size(); i++) {
            if (videoId == videoDetaiInfoList.get(i).videoId) {
                return true;
            }
        }
        return false;
    }

    //获取视频详情
    private void getVideoPlayData() {
        OpenApiDataServiceBase service = null;
        if (Utils.isUserLogined()) {
            service = new GetVideoInfoLoginService(this);
        } else {
            service = new GetVideoInfoUnLoginService(this);
        }
        service.setCallback(new IOpenApiDataServiceCallback<GetVideoInfoResponse>() {
            @Override
            public void onGetData(final GetVideoInfoResponse data) {
                try {
                    VideoIntroForExcActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshUI(data.data.videoVo);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(VideoIntroForExcActivity.this, "获取视频详情失败！", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        if (Utils.isUserLogined()) {
            service.postAES("videoId=" + videoId, false);
        } else {
            service.post("videoId=" + videoId, false);
        }
    }

    private void refreshUI(VideoDetaiInfo info) {
        btnCollect.setVisibility(View.VISIBLE);
        if (info.isFav == 1) {
            btnCollect.setText("已收藏");
            btnCollect.setFocusable(false);
        }
        if (info.isVip == 1) {
            tvVIP.setVisibility(View.VISIBLE);
        }
        btnPlay.setVisibility(View.VISIBLE);
        btnPlay.requestFocus();
        videoDetailInfo = info;
        mTitleBarView.setBtnLeftStr(info.title);
        Glide.with(this).load(info.posterUrl).placeholder(R.mipmap.loading).error(R.mipmap.loading).into(ivPoster);
    }

    //加入收藏
    private void addToFav(int videoId) {
        AddToFavService service = new AddToFavService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                Toast.makeText(VideoIntroForExcActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
                btnCollect.setText("已收藏");
                btnCollect.setFocusable(false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(VideoIntroForExcActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        JSONArray array = new JSONArray();
        array.add(videoId);
        service.postAES("videoIds=" + array.toString(), false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
