package com.physicmaster.modules.study.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.BaseFragment2;
import com.physicmaster.base.SplashActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.modules.explore.activity.MembersActivity;
import com.physicmaster.modules.guide.ExposureView;
import com.physicmaster.modules.guide.GuideDialogFragment2;
import com.physicmaster.modules.mine.fragment.dialogFragment.MedalYesDialogFragment;
import com.physicmaster.modules.study.activity.DriverHouseActivity;
import com.physicmaster.modules.study.fragment.dialogfragment.AdvertiseDialogFragment;
import com.physicmaster.modules.study.fragment.dialogfragment.BigGiftDialogFragment;
import com.physicmaster.modules.study.fragment.dialogfragment.CommonDialogFragment;
import com.physicmaster.modules.study.fragment.dialogfragment.DownDialogFragment;
import com.physicmaster.modules.study.fragment.dialogfragment.EnergyDialogFragment;
import com.physicmaster.modules.study.fragment.dialogfragment.SectionBoxDialogFragment;
import com.physicmaster.modules.study.fragment.dialogfragment.SignInDialogFragment;
import com.physicmaster.modules.study.fragment.widget.dynamicbg.DynamicBackgroundDrawable;
import com.physicmaster.modules.study.fragment.widget.dynamicbg.HeaderAndFooterWrapper;
import com.physicmaster.modules.study.fragment.widget.dynamicbg.MainPageRecyclerView;
import com.physicmaster.modules.study.fragment.widget.dynamicbg.SeaSceneDrawable;
import com.physicmaster.modules.study.fragment.widget.wave.WaveView;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.db.VideoManager;
import com.physicmaster.modules.videoplay.cache.service.DownloadService2;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse.DataBean.ChapterStudyBean.DeepListBean;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse.DataBean.ChapterStudyBean.ExcerciseVideoBean;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse.DataBean.ChapterStudyBean.PreviewListBean;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse.DataBean.ChapterStudyBean.ReviewListBean;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse.DataBean.ProgressCompletionBean.ProgressListBean;
import com.physicmaster.net.response.study.StudyingInfoResponse;
import com.physicmaster.net.response.user.GetMedalListResponse;
import com.physicmaster.net.service.excercise.GetChapterDetailsService;
import com.physicmaster.net.service.game.CheckMedalService;
import com.physicmaster.net.service.study.GetStudyingInfoService;
import com.physicmaster.utils.NetworkUtils;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.SelectChapter2View;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huashigen on 2017-06-28.
 */

public class StudyFragmentV2 extends BaseFragment2 implements View.OnClickListener {
    private MainPageRecyclerView recyclerView;
    private StudyAdapter studyAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private static final String TAG = "StudyFragmentV2";
    public static final String ON_SUBJECT_CHANGED = "on_subject_changed";
    private LocalBroadcastManager localBroadcastManager;
    private Logger logger = AndroidLogger.getLogger(TAG);
    private int scrollLen = 0;
    private LinearLayout llToolBar;
    private TextView tvChapter, tvCache, tvEnergy;
    private ImageView ivSelector;
    private RelativeLayout rlTitle;
    private ProgressBar energyBar;

    private String versionGrade;
    private boolean isNewGuideShow;
    private MediaPlayer waveMediaPlayer, bubbleMediaPlayer;
    private int playSound = 0;//0-播放海浪的声音,1-播放气泡的声音
    private boolean isSoundSwitch;
    private SeaSceneDrawable seaSceneDrawable;

    private List<PreviewListBean> previewList;
    private List<DeepListBean> deepList;
    private List<ReviewListBean> reviewList;
    //课程列表编辑加锁
    private Object listLock = new Object();
    private VideoManager videoManager;//用来查询章节是否全部缓存

    private String chapterId, chapterName, selectSubject;
    private int selectBookId;
    private boolean isAllowDownload = false;
    private SparseArrayCompat<String> titles;

    //尝试引导次数
    private int tryGuideCount = 3;

    private WaveView waveView;
    private DynamicBackgroundDrawable dynamicBackgroundDrawable;
    private AsyncTask<Void, Void, Boolean> checkTask;
    private AsyncTask<String, Integer, List<String>> cacheTask;
    private SelectChapter2View selectChapterView;
    private RelativeLayout decorView;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_selector:
            case R.id.tv_chapter:
                if (TextUtils.isEmpty(tvChapter.getText())) {
                    UIUtils.showToast(getActivity(), "暂无数据");
                    return;
                }
                if (tvChapter.isSelected()) {
                    tvChapter.setSelected(false);
                    if (selectChapterView != null) {
                        selectChapterView.dismiss();
                        waveView.setVisibility(View.VISIBLE);
                        startAnimation(-1);
                    }
                    return;
                }
                showSelectChapter();
                break;
            case R.id.rl_energy:
                new EnergyDialogFragment().show(getFragmentManager(), "energy_dialog");
                break;
            case R.id.rl_sign:
                SignInDialogFragment fragment = new SignInDialogFragment();
                fragment.show(getFragmentManager(), "sign_dialog");
                break;
            case R.id.tv_cache:
                if (isAllowDownload) {
                    DownDialogFragment downDialogFragment = new DownDialogFragment();
                    downDialogFragment.setOnClickListener(() -> {
                        //非wifi环境弹出提示
                        String networkState = NetworkUtils.getNetworkState(getActivity());
                        if (networkState.equals(Constant.NETTYPE_UNCONNECTED)) {
                            UIUtils.showToast(getActivity(), "网络不可用");
                            return;
                        }
                        //非wifi环境下弹出提示
                        if (!networkState.equals(Constant.NETTYPE_WIFI)) {
                            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "继续缓存", (dialog1, which) -> {
                                BaseApplication.setNone_wifi_prompt_times
                                        (BaseApplication
                                                .getNone_wifi_prompt_times()
                                                + 1);
                                verifyStoragePermissions();
                            });
                            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", (dialog12, which) -> BaseApplication.setNone_wifi_prompt_times(0));
                            dialog.setOnCancelListener(dialog13 -> {
                            });
                            dialog.setTitle("您正在使用非wifi网络，缓存将产生流量费用");
                            dialog.show();
                        } else {
                            verifyStoragePermissions();
                        }
                    });
                    downDialogFragment.show(getFragmentManager(), "download");
                } else {
                    CommonDialogFragment buyFragment = new CommonDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "购买会员即可下载全部视频");
                    buyFragment.setArguments(bundle);
                    buyFragment.setOnActionBtnClickListener(() -> startActivity(new Intent(mContext, MembersActivity.class)));
                    buyFragment.show(getFragmentManager(), "buyMember");
                }
                break;
        }
    }

    private void showSelectChapter() {
        if (null == BaseApplication.getStartupDataBean()) {
            startActivity(new Intent(getActivity(), SplashActivity.class));
            return;
        }
        tvChapter.setSelected(true);
        decorView = rootView.findViewById(R.id.sl_chpt_container);
        Bundle bundleData = new Bundle();
        bundleData.putString("versionGrade", versionGrade);
        bundleData.putInt("bookId", selectBookId);
        bundleData.putString("selectSubject", selectSubject);
        bundleData.putParcelableArrayList("bookMenu", (ArrayList<? extends Parcelable>) BaseApplication.getStartupDataBean().bookMenu);
        selectChapterView = new SelectChapter2View(getActivity(), decorView, 0, 0, bundleData);
        selectChapterView.setOnItemSelectListener((id, name, bookId, vg, ss) -> {
            chapterId = id;
            chapterName = name;
            selectBookId = bookId;
            selectSubject = ss;
            versionGrade = vg;
            tvChapter.setSelected(false);
            tvChapter.setText(chapterName);
            waveView.setVisibility(View.VISIBLE);
            selectChapterView.dismiss();
            startAnimation(-1);
            getChapterDetails();
        });
        waveView.setVisibility(View.GONE);
        selectChapterView.show();
        startAnimation(1);
    }

    //动态获取文件读取权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public void verifyStoragePermissions() {
        // Check if we have write permission
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                startCache();
            }
        } else {
            startCache();
        }
    }

    /**
     * 检查本章视频是否全部下载
     */
    private synchronized void startCheckDownloadStateTask() {
        checkTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                if (isCancelled()) {
                    return false;
                }
                return checkChapterDownload();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (isCancelled()) {
                    return;
                }
                if (aBoolean) {
                    tvCache.setEnabled(false);
                    tvCache.setCompoundDrawables(null, null, null, null);
                    tvCache.setText("已缓存");
                } else {
                    tvCache.setEnabled(true);
                    Drawable drawable = getResources().getDrawable(R.mipmap.download);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tvCache.setCompoundDrawables(drawable, null, null, null);
                    tvCache.setText("缓存");
                }
            }

            @Override
            protected void onCancelled() {
                logger.debug("checkTask onCanceled");
                super.onCancelled();
            }
        };
        checkTask.execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (checkTask != null) {
            checkTask.cancel(true);
        }
        if (cacheTask != null) {
            cacheTask.cancel(true);
        }
    }

    /**
     * 缓存整章视频
     */
    private void startCache() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //sdcard状态是没有挂载的情况
            Toast.makeText(mContext, "sdcard不存在或未挂载", Toast.LENGTH_SHORT).show();
            return;
        }
        cacheTask = new AsyncTask<String, Integer, List<String>>() {
            @Override
            protected List<String> doInBackground(String... params) {
                if (isCancelled()) {
                    return null;
                }
                List<String> videoIds = new ArrayList<>();
                String userId = BaseApplication.getUserData().dtUserId;
                synchronized (listLock) {
                    if (previewList != null && previewList.size() > 0) {
                        for (PreviewListBean previewListBean : previewList) {
                            VideoInfo videoInfo = videoManager.getVideoInfo(previewListBean.videoId + "-" + userId);
                            if (null == videoInfo) {
                                videoIds.add(previewListBean.videoId + "-" + userId);
                                videoInfo = new VideoInfo();
                                videoInfo.setId(previewListBean.videoId + "-" + userId);
                                videoInfo.setUserId(userId);
                                videoInfo.setName(previewListBean.title);
                                videoInfo.setCreateDatetime(System.currentTimeMillis() + "");
                                videoInfo.setState(VideoInfo.STATE_ADD);
                                videoInfo.setPosterUrl(previewListBean.posterUrl);
                                videoInfo.setType(VideoInfo.TYPE_PREVIEW);
                                videoManager.addOrUpdateVideo(videoInfo);
                            }
                        }
                    }
                    if (deepList != null && deepList.size() > 0) {
                        for (DeepListBean deepListBean : deepList) {
                            VideoInfo videoInfo = videoManager.getVideoInfo(deepListBean.videoId + "-" + userId);
                            if (null == videoInfo) {
                                videoIds.add(deepListBean.videoId + "-" + userId);
                                videoInfo = new VideoInfo();
                                videoInfo.setId(deepListBean.videoId + "-" + userId);
                                videoInfo.setUserId(userId);
                                videoInfo.setName(deepListBean.title);
                                videoInfo.setCreateDatetime(System.currentTimeMillis() + "");
                                videoInfo.setState(VideoInfo.STATE_ADD);
                                videoInfo.setPosterUrl(deepListBean.posterUrl);
                                videoInfo.setType(VideoInfo.TYPE_DEEP);
                                videoManager.addOrUpdateVideo(videoInfo);
                            }
                        }
                    }
                    if (reviewList != null && reviewList.size() > 0) {
                        for (ReviewListBean reviewListBean : reviewList) {
                            List<ExcerciseVideoBean> videoItemList = reviewListBean.videoItemList;
                            if (videoItemList != null && videoItemList.size() > 0) {
                                for (ExcerciseVideoBean excerciseVideoBean : videoItemList) {
                                    VideoInfo videoInfo = videoManager.getVideoInfo(excerciseVideoBean.videoId + "-" + userId);
                                    if (null == videoInfo) {
                                        videoIds.add(excerciseVideoBean.videoId + "-" + userId);
                                        videoInfo = new VideoInfo();
                                        videoInfo.setId(excerciseVideoBean.videoId + "-" + userId);
                                        videoInfo.setUserId(userId);
                                        videoInfo.setName(excerciseVideoBean.title);
                                        videoInfo.setCreateDatetime(System.currentTimeMillis() + "");
                                        videoInfo.setState(VideoInfo.STATE_ADD);
                                        videoInfo.setPosterUrl(excerciseVideoBean.posterUrl);
                                        videoInfo.setType(VideoInfo.TYPE_REVIEW);
                                        videoManager.addOrUpdateVideo(videoInfo);
                                    }
                                }
                            }
                        }
                    }
                }
                return videoIds;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                if (isCancelled()) {
                    return;
                }
                Intent intent = new Intent(getActivity().getApplicationContext(), DownloadService2.class);
                intent.putStringArrayListExtra("videoIds", (ArrayList<String>) strings);
                getActivity().startService(intent);
//                Intent intent = new Intent(getActivity(), DownloadService.class);
//                intent.putStringArrayListExtra("videoIds", (ArrayList<String>) strings);
//                getActivity().startService(intent);
                UIUtils.showToast(getActivity(), "已加入缓存列表");
            }
        };
        cacheTask.execute("");
    }

    /**
     * 获取缓存信息
     */
    private boolean checkChapterDownload() {
        String userId = BaseApplication.getUserData().dtUserId;
        synchronized (listLock) {
            if (previewList != null && previewList.size() > 0) {
                for (PreviewListBean previewListBean : previewList) {
                    VideoInfo videoInfo = videoManager.getVideoInfo(previewListBean.videoId + "-" + userId);
                    if (null == videoInfo) {
                        return false;
                    }
                }
            }
            if (deepList != null && deepList.size() > 0) {
                for (DeepListBean deepListBean : deepList) {
                    VideoInfo videoInfo = videoManager.getVideoInfo(deepListBean.videoId + "-" + userId);
                    if (null == videoInfo) {
                        return false;
                    }
                }
            }
            if (reviewList != null && reviewList.size() > 0) {
                for (ReviewListBean reviewListBean : reviewList) {
                    List<ExcerciseVideoBean> videoItemList = reviewListBean.videoItemList;
                    if (videoItemList != null && videoItemList.size() > 0) {
                        for (ExcerciseVideoBean excerciseVideoBean : videoItemList) {
                            VideoInfo videoInfo = videoManager.getVideoInfo(excerciseVideoBean.videoId + "-" + userId);
                            if (null == videoInfo) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isAllowDownload = false;
        tvCache.setText("查询中");
        tvCache.setEnabled(false);
        if (isSoundSwitch) {
            if (0 == playSound) {
                waveMediaPlayer.pause();
            } else {
                bubbleMediaPlayer.pause();
            }
        }
    }

    /**
     * 获取动画
     *
     * @param type:0-顺时针翻转180度 1-逆时针翻转180度
     * @return
     */
    private void startAnimation(final int type) {
        float fromDegree;
        float toDegree;
        if (1 == type) {
            fromDegree = 0;
            toDegree = 180;
        } else {
            fromDegree = 180;
            toDegree = 0;
        }
        ObjectAnimator objAnimator = ObjectAnimator.ofFloat(ivSelector, "rotation", fromDegree, toDegree);
        objAnimator.setDuration(300);
        objAnimator.start();
    }

    @Override
    protected void initView(View view) {
        tvChapter = (TextView) view.findViewById(R.id.tv_chapter);
        ivSelector = (ImageView) view.findViewById(R.id.iv_selector);
        rlTitle = (RelativeLayout) view.findViewById(R.id.title);
        tvChapter.setOnClickListener(this);
        view.findViewById(R.id.rl_energy).setOnClickListener(this);
        view.findViewById(R.id.rl_sign).setOnClickListener(this);
        tvCache = (TextView) view.findViewById(R.id.tv_cache);
        tvEnergy = (TextView) view.findViewById(R.id.tv_energy);
        energyBar = (ProgressBar) view.findViewById(R.id.pb_energy);
        tvCache.setOnClickListener(this);
        ivSelector.setOnClickListener(this);

        recyclerView = (MainPageRecyclerView) view.findViewById(R.id.recyclerview);
        llToolBar = (LinearLayout) view.findViewById(R.id.ll_toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        previewList = new ArrayList<>();
        deepList = new ArrayList<>();
        reviewList = new ArrayList<>();
        videoManager = new VideoManager(getActivity());
        titles = new SparseArrayCompat<>();

        isNewGuideShow = SpUtils.getBoolean(mContext, "is_new_guide_show", false);
    }

    /**
     * 初始化recyclerview
     */
    private void initRecyclerView() {
        studyAdapter = new StudyAdapter(previewList, deepList, reviewList, titles, getFragmentManager());
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(studyAdapter);
        final View seaScene = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sea_scene, null);
        waveView = (WaveView) seaScene.findViewById(R.id.wave_view);
        waveView.setOnBoatClickListener(v -> startActivity(new Intent(getActivity(), DriverHouseActivity.class)));
        seaSceneDrawable = new SeaSceneDrawable(getResources());
        seaScene.setBackground(seaSceneDrawable);
        mHeaderAndFooterWrapper.addHeaderView(seaScene);
        recyclerView.setOnTopOverScrollListener(scrollY -> {
            if (scrollLen != 0) {
                //scrollLen不为零,云彩位置立即恢复原位
                seaSceneDrawable.moveClouds(-1);
            } else {
                seaSceneDrawable.moveClouds(scrollY);
            }
        });
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initRecyclerView();
        waveMediaPlayer = MediaPlayer.create(mContext, R.raw.wave);
        waveMediaPlayer.setLooping(true);
        bubbleMediaPlayer = MediaPlayer.create(mContext, R.raw.bubble);
        bubbleMediaPlayer.setLooping(true);

        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(subjectChangedReceiver, new IntentFilter(ON_SUBJECT_CHANGED));
    }

    //监听学科改变,将chapterId和chapterName置空或者点击我的课程的时候加载我的课程章节数据
    private BroadcastReceiver subjectChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("chapterId")) {
                chapterId = intent.getStringExtra("chapterId");
                chapterName = intent.getStringExtra("chapterName");
//                getMyChapterList();
            } else {
                chapterId = "";
                chapterName = "";
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_study_v2;
    }

    @Override
    public void fetchData() {

    }

    /**
     * 显示及隐藏toolbar
     *
     * @param visible
     */
    private void showToolBar(boolean visible) {
        if (visible) {
            if (llToolBar.getVisibility() == View.VISIBLE) {
                return;
            }
            AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.shade_in_from_top);
            llToolBar.setVisibility(View.VISIBLE);
            llToolBar.startAnimation(animationSet);
        } else {
            if (llToolBar.getVisibility() == View.INVISIBLE) {
                return;
            }
            AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.shade_out_to_top);
            llToolBar.setVisibility(View.INVISIBLE);
            llToolBar.startAnimation(animationSet);
        }
    }

    /**
     * 获取章节详情数据
     */
    private void getChapterDetails() {
        if (TextUtils.isEmpty(chapterId)) {
            return;
        }
        GetChapterDetailsService service = new GetChapterDetailsService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<GetChapterDetailsResponse>() {
            @Override
            public void onGetData(GetChapterDetailsResponse data) {
                refreshEnergy(data.data.energyValue, data.data.maxEnergyValue);
                if (1 == data.data.chapterStudy.downloadAllow) {
                    isAllowDownload = true;
                } else {
                    isAllowDownload = false;
                }
                synchronized (listLock) {
                    previewList.clear();
                    if (data.data.chapterStudy.previewList != null && data.data.chapterStudy.previewList.size() > 0) {
                        previewList.addAll(data.data.chapterStudy.previewList);
                    }
                    deepList.clear();
                    if (data.data.chapterStudy.deepList != null && data.data.chapterStudy.deepList.size() > 0) {
                        deepList.addAll(data.data.chapterStudy.deepList);
                    }
                    reviewList.clear();
                    if (data.data.chapterStudy.reviewList != null && data.data.chapterStudy.reviewList.size() > 0) {
                        reviewList.addAll(data.data.chapterStudy.reviewList);
                    }
                }
                calculatePosition();
                int totalSize = previewList.size() + deepList.size() + reviewList.size();
                if (totalSize >= 3) {
                    if (dynamicBackgroundDrawable == null) {
                        dynamicBackgroundDrawable = new DynamicBackgroundDrawable(getResources());
                    } else {
                        dynamicBackgroundDrawable.removeAllScenes();
                    }
                    dynamicBackgroundDrawable.attach(recyclerView);
                    dynamicBackgroundDrawable.setOnScrollListener(scrollRatio -> {
                        if (scrollRatio < 20.0F) {
                            showToolBar(true);
                            if (!waveMediaPlayer.isPlaying() && isSoundSwitch) {
                                waveMediaPlayer.start();
                            }
                            if (bubbleMediaPlayer.isPlaying()) {
                                bubbleMediaPlayer.pause();
                            }
                            playSound = 0;
                        } else {
                            showToolBar(false);
                            if (waveMediaPlayer.isPlaying()) {
                                waveMediaPlayer.pause();
                            }
                            if (!bubbleMediaPlayer.isPlaying() && isSoundSwitch) {
                                bubbleMediaPlayer.start();
                            }
                            playSound = 1;
                        }
                    });
                    if (!mHeaderAndFooterWrapper.hasFooter()) {
                        final View bottomView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sea_bottom, null);
                        mHeaderAndFooterWrapper.addFootView(bottomView);
                    }
                } else {
                    recyclerView.setBackgroundColor(getResources().getColor(R.color.startColor));
                    if (mHeaderAndFooterWrapper.hasFooter()) {
                        mHeaderAndFooterWrapper.removeFootView(0);
                    }
                }
                studyAdapter.setChapterId(chapterId);
                mHeaderAndFooterWrapper.notifyDataSetChanged();
                updateProgress(data.data.progressCompletion);
                dailyAction();
                startCheckDownloadStateTask();
                upgradeBoat(data.data.shipState);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getActivity(), errorMsg);
            }
        });
        service.postLogined("chapterId=" + chapterId, false);
    }

    /**
     * 获取正在学习的数据
     */
    private void getStudyingInfo() {
        GetStudyingInfoService service = new GetStudyingInfoService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<StudyingInfoResponse>() {
            @Override
            public void onGetData(StudyingInfoResponse data) {
                versionGrade = data.data.editionName + " " + data.data.bookName;
                chapterId = data.data.selectChapterId + "";
                selectBookId = data.data.bookId;
                selectSubject = data.data.subjectName;
                refreshShowingChapter(data.data.selectChapterName);
                getChapterDetails();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        service.postLogined("", false);
    }

    /**
     * 升级船的状态
     *
     * @param shipState
     */
    private void upgradeBoat(int shipState) {
        Bitmap bitmapBoat = null;
        boolean hasNewMsg = false;
        if (1 == shipState) {
            bitmapBoat = ((BitmapDrawable) getResources().getDrawable(R.mipmap.boat1)).getBitmap();
        } else if (2 == shipState) {
            bitmapBoat = ((BitmapDrawable) getResources().getDrawable(R.mipmap.boat1)).getBitmap();
            hasNewMsg = true;
        } else if (3 == shipState) {
            bitmapBoat = ((BitmapDrawable) getResources().getDrawable(R.mipmap.boat2)).getBitmap();
        } else if (4 == shipState) {
            bitmapBoat = ((BitmapDrawable) getResources().getDrawable(R.mipmap.boat2)).getBitmap();
            hasNewMsg = true;
        } else if (5 == shipState) {
            bitmapBoat = ((BitmapDrawable) getResources().getDrawable(R.mipmap.boat3)).getBitmap();
        } else if (6 == shipState) {
            bitmapBoat = ((BitmapDrawable) getResources().getDrawable(R.mipmap.boat3)).getBitmap();
            hasNewMsg = true;
        }
        waveView.upgradeBoat(bitmapBoat, hasNewMsg);
    }

    private void dailyAction() {
        if (!isNewGuideShow) {
            showGuideDialogFragment2();
            isNewGuideShow = true;
        } else {
            //隔天首次打开应用弹出签到窗口
            if (calculateIsNewDay()) {
                SignInDialogFragment fragment = new SignInDialogFragment();
                fragment.setOnDismissListener(() -> getMedalInfo());
                fragment.show(getFragmentManager(), "sign_dialog");
                StartupResponse.DataBean.Advertisement advertisement = BaseApplication.getStartupDataBean().popup;
                if (advertisement != null) {
                    setAdvertiseShown(false, advertisement.bannerId);
                }
            } else {
                if (null == BaseApplication.getStartupDataBean()) {
                    startActivity(new Intent(getActivity(), SplashActivity.class));
                    return;
                }
                StartupResponse.DataBean.Advertisement advertisement = BaseApplication.getStartupDataBean().popup;
                if (advertisement != null && !isAdvertiseShown(advertisement.bannerId)) {
                    if (!TextUtils.isEmpty(advertisement.popupImgUrl)) {
                        AdvertiseDialogFragment advertiseDialogFragment = new AdvertiseDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("imgUrl", advertisement.popupImgUrl);
                        bundle.putString("destUrl", advertisement.pageUrl);
                        advertiseDialogFragment.setArguments(bundle);
                        advertiseDialogFragment.show(getFragmentManager(), "advertise_dialog");
                        setAdvertiseShown(true, advertisement.bannerId);
                    }
                }
                getMedalInfo();
            }
        }
    }

    /**
     * 判断广告是否已展示
     *
     * @return
     */
    private boolean isAdvertiseShown(int adId) {
        int oldAdId = SpUtils.getInt(getActivity(), SpUtils.ADVERTISE_ID, -1);
        if (adId != oldAdId) {
            return false;
        }
        return SpUtils.getBoolean(getActivity(), SpUtils.ADVERTISE_SHOWN, false);
    }

    /**
     * 设置广告已展示
     *
     * @return
     */
    private void setAdvertiseShown(boolean bool, int adId) {
        SpUtils.putBoolean(getActivity(), SpUtils.ADVERTISE_SHOWN, bool);
        SpUtils.putInt(getActivity(), SpUtils.ADVERTISE_ID, adId);
    }

//    /**
//     * 获取可选章节列表
//     */
//    private void getChapterList() {
//        GetChapterListService service = new GetChapterListService(getActivity());
//        service.setCallback(new IOpenApiDataServiceCallback<GetChapterListResponse>() {
//            @Override
//            public void onGetData(GetChapterListResponse data) {
//                versionGrade = data.data.eduGrade;
//                chapterList = data.data.chapterList;
//                subject = data.data.subject;
//                if (TextUtils.isEmpty(chapterId)) {
//                    int chapterIdInt = data.data.selectChapterId;
//                    chapterId = chapterIdInt + "";
//                    if (chapterList != null && chapterList.size() > 0) {
//                        for (ChapterListBean chapterListBean : chapterList) {
//                            if (chapterListBean.chapterId == chapterIdInt) {
//                                chapterName = chapterListBean.name;
//                                refreshShowingChapter(chapterName);
//                                getChapterDetails();
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onGetError(int errorCode, String errorMsg, Throwable error) {
//                UIUtils.showToast(getActivity(), errorMsg);
//            }
//        });
//        service.postLogined("", false);
//    }

//    /**
//     * 获取我的课程章节数据
//     */
//    private void getMyChapterList() {
//        GetChapterList2Service service = new GetChapterList2Service(getActivity());
//        service.setCallback(new IOpenApiDataServiceCallback<GetChapterListResponse>() {
//            @Override
//            public void onGetData(GetChapterListResponse data) {
//                versionGrade = data.data.eduGrade;
//                chapterList = data.data.chapterList;
//                subject = data.data.subject;
//                int chapterIdInt = data.data.selectChapterId;
//                chapterId = chapterIdInt + "";
//                if (chapterList != null && chapterList.size() > 0) {
//                    for (ChapterListBean chapterListBean : chapterList) {
//                        if (chapterListBean.chapterId == chapterIdInt) {
//                            chapterName = chapterListBean.name;
//                            refreshShowingChapter(chapterName);
//                            getChapterDetails();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onGetError(int errorCode, String errorMsg, Throwable error) {
//                UIUtils.showToast(getActivity(), errorMsg);
//            }
//        });
//        service.postLogined("chapterId=" + chapterId, false);
//    }

    /**
     * 刷新显示章节信息
     */
    private void refreshShowingChapter(String name) {
        if (!TextUtils.isEmpty(name)) {
            tvChapter.setText(name);
        }
    }

    /**
     * 判断是否是新的一天第一次打开应用
     *
     * @return
     */
    private boolean calculateIsNewDay() {
        long lastDay = SpUtils.getLong(getContext(), SpUtils.LASTDAY_NUMBER, 0);
        long toDay = System.currentTimeMillis() / (1000 * 60 * 60 * 24);
        SpUtils.putLong(getContext(), SpUtils.LASTDAY_NUMBER, toDay);
        return toDay - lastDay >= 1;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 显示第二个引导页面
     */
    private void showGuideDialogFragment2() {
        if (tryGuideCount < 0) {
            return;
        }
        tryGuideCount--;
        Bundle bundle2 = new Bundle();
        int[] location = new int[2];
        tvChapter.getLocationOnScreen(location);
        ExposureView view21 = new ExposureView(location[0], location[1], location[0] + tvChapter.getWidth(), tvChapter.getHeight() + location[1]);
        bundle2.putParcelable("view1", view21);
        if (recyclerView.getChildAt(2) == null) {
            new Handler().postDelayed(() -> showGuideDialogFragment2(), 300);
            return;
        }
        View viewImage = recyclerView.getChildAt(2).findViewById(R.id.ll_container);
        int view2Left = viewImage.getLeft();
        int view2Top = recyclerView.getChildAt(2).getTop() + rootView.findViewById(R.id.title).getHeight();
        int view2Right = viewImage.getRight();
        int view2Bottom = recyclerView.getChildAt(2).getBottom() + rootView.findViewById(R.id.title).getHeight();

        view2Top += viewImage.getTop();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewImage.getLayoutParams();
        view2Bottom = view2Bottom - layoutParams.bottomMargin;
        ExposureView view22 = new ExposureView(view2Left, view2Top, view2Right, view2Bottom);
        bundle2.putParcelable("view2", view22);
        final GuideDialogFragment2 guideFragment2 = new GuideDialogFragment2(getActivity(), bundle2);
        guideFragment2.setPaintViewOnClickListener(v -> {
            guideFragment2.dismiss();
            showSelectChapter();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getStudyingInfo();
        isSoundSwitch = SpUtils.getBoolean(mContext, "isSoundSwitch", true);
        if (isSoundSwitch) {
            if (0 == playSound) {
                waveMediaPlayer.start();
            } else {
                bubbleMediaPlayer.start();
            }
        }
    }

    /**
     * 获取勋章信息
     */
    private void getMedalInfo() {
        final CheckMedalService service = new CheckMedalService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<GetMedalListResponse>() {

            @Override
            public void onGetData(GetMedalListResponse data) {
                List<GetMedalListResponse.DataBean.LevelBean> medalNewGetList = data.data.medalNewGetList;
                for (int i = 0; i < medalNewGetList.size(); i++) {
                    if (medalNewGetList.get(i).isClaimed == 1) {
                        MedalYesDialogFragment medalYesDialogFragment = new MedalYesDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("levelBean", medalNewGetList.get(i));
                        medalYesDialogFragment.setArguments(bundle);
                        medalYesDialogFragment.show(getFragmentManager(), "levelBean");
                    }
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                logger.debug("加载失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("", false);
    }

    /**
     * 刷新精力信息
     */
    private void refreshEnergy(int energyValue, int maxEnergyValue) {
        try {
            int progress = (int) ((float) energyValue / (float) maxEnergyValue * 100);
            energyBar.setProgress(progress);
            tvEnergy.setText(energyValue + "/" + maxEnergyValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新任务进度
     *
     * @param progressCompletionBean
     */
    private void updateProgress(GetChapterDetailsResponse.DataBean.ProgressCompletionBean progressCompletionBean) {
        final List<ProgressListBean> progressList = progressCompletionBean.progressList;
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        ImageView ivGift1 = (ImageView) rootView.findViewById(R.id.iv_gift_1);
        ImageView ivGift2 = (ImageView) rootView.findViewById(R.id.iv_gift_2);
        ImageView ivGift3 = (ImageView) rootView.findViewById(R.id.iv_gift_3);
        if (progressList != null && progressList.size() != 0) {
            final ProgressListBean gift1 = progressList.get(0);
            int progress = (int) (100 * (float) (progressCompletionBean.completeCount - gift1.completeCount) / (float) (progressCompletionBean.maxTargetCount - gift1.targetCount));
            progress = (progress <= 0) ? 0 : progress;
            progressBar.setProgress(progress);
            //如果进度达到领奖标准
            if (gift1.completeCount >= gift1.targetCount) {
                ivGift1.setBackgroundResource(R.drawable.gift_open_bg);
                if (gift1.isAward == 0) {
                    ivGift1.setImageResource(R.mipmap.xiaoliwuhe);
                } else {
                    ivGift1.setImageResource(R.mipmap.xiaoliwuhe_kai);
                }
                ivGift1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //如果未领奖
                        if (gift1.isAward == 0) {
                            //弹窗领取奖励
                            BigGiftDialogFragment fragment = new BigGiftDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data", progressList.get(0));
                            fragment.setArguments(bundle);
                            fragment.setListener(new BigGiftDialogFragment.OnGiftGotListener() {
                                @Override
                                public void onGiftGot() {
                                    getChapterDetails();
                                }
                            });
                            fragment.show(getFragmentManager(), "");
                        } else {
                            UIUtils.showToast(getActivity(), "您已领过奖品啦！");
                        }
                    }
                });
            } else {
                ivGift1.setImageResource(R.mipmap.xiaoliwuhe);
                ivGift1.setBackgroundResource(R.drawable.gift_close_bg);
                ivGift1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //如果未领奖
                        SectionBoxDialogFragment fragment = new SectionBoxDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("data", progressList.get(0));
                        bundle.putString("desc", "完成本章1/3课程可领取");
                        fragment.setArguments(bundle);
                        fragment.show(getFragmentManager(), "");
                    }
                });
            }
            final ProgressListBean gift2 = progressList.get(1);
            //如果进度达到领奖标准
            if (gift2.completeCount >= gift2.targetCount) {
                if (progress < 50) {
                    progressBar.setProgress(50);
                }
                ivGift2.setBackgroundResource(R.drawable.gift_open_bg);
                if (gift2.isAward == 0) {
                    ivGift2.setImageResource(R.mipmap.xiaoliwuhe);
                } else {
                    ivGift2.setImageResource(R.mipmap.xiaoliwuhe_kai);
                }
                ivGift2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //如果未领奖
                        if (gift2.isAward == 0) {
                            BigGiftDialogFragment fragment = new BigGiftDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data", progressList.get(1));
                            fragment.setArguments(bundle);
                            fragment.setListener(new BigGiftDialogFragment.OnGiftGotListener() {
                                @Override
                                public void onGiftGot() {
                                    getChapterDetails();
                                }
                            });
                            fragment.show(getFragmentManager(), "");
                        } else {
                            UIUtils.showToast(getActivity(), "您已领过奖品啦！");
                        }
                    }
                });
            } else {
                ivGift2.setImageResource(R.mipmap.xiaoliwuhe);
                ivGift2.setBackgroundResource(R.drawable.gift_close_bg);
                ivGift2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //如果未领奖
                        SectionBoxDialogFragment fragment = new SectionBoxDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("data", progressList.get(1));
                        bundle.putString("desc", "完成本章2/3课程可领取");
                        fragment.setArguments(bundle);
                        fragment.show(getFragmentManager(), "");
                    }
                });
            }

            final ProgressListBean gift3 = progressList.get(2);
            //如果进度达到领奖标准
            if (gift3.completeCount >= gift3.targetCount) {
                if (progress < 100) {
                    progressBar.setProgress(100);
                }
                ivGift3.setBackgroundResource(R.drawable.gift_open_bg);
                if (gift3.isAward == 0) {
                    ivGift3.setImageResource(R.mipmap.xiaoliwuhe);
                } else {
                    ivGift3.setImageResource(R.mipmap.xiaoliwuhe_kai);
                }
                ivGift3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //如果未领奖
                        if (gift3.isAward == 0) {
                            BigGiftDialogFragment fragment = new BigGiftDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data", progressList.get(2));
                            fragment.setArguments(bundle);
                            fragment.setListener(new BigGiftDialogFragment.OnGiftGotListener() {
                                @Override
                                public void onGiftGot() {
                                    getChapterDetails();
                                }
                            });
                            fragment.show(getFragmentManager(), "");
                        } else {
                            UIUtils.showToast(getActivity(), "您已领过奖品啦！");
                        }
                    }
                });
            } else {
                ivGift3.setImageResource(R.mipmap.xiaoliwuhe);
                ivGift3.setBackgroundResource(R.drawable.gift_close_bg);
                ivGift3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SectionBoxDialogFragment fragment = new SectionBoxDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("data", progressList.get(2));
                        bundle.putString("desc", "完成本章全部课程可领取");
                        fragment.setArguments(bundle);
                        fragment.show(getFragmentManager(), "");
                    }
                });
            }
        } else {
            progressBar.setProgress(0);
            ivGift1.setImageResource(R.mipmap.xiaoliwuhe);
            ivGift2.setImageResource(R.mipmap.xiaoliwuhe);
            ivGift3.setImageResource(R.mipmap.xiaoliwuhe);
            ivGift1.setBackgroundResource(R.drawable.gift_close_bg);
            ivGift2.setBackgroundResource(R.drawable.gift_close_bg);
            ivGift3.setBackgroundResource(R.drawable.gift_close_bg);
        }
    }

    /**
     * 计算每一个Item的位置
     */
    private void calculatePosition() {
        titles.clear();
        synchronized (listLock) {
            if (previewList != null && previewList.size() > 0) {
                for (int i = 0; i < previewList.size(); i++) {
                    previewList.get(i).position = i % 2;
                }
                titles.put(0, "导入课预习");
            }
            if (deepList != null && deepList.size() > 0) {
                if (previewList != null && previewList.size() > 0 && 0 == previewList.get(previewList.size() - 1).position) {
                    for (int i = 0; i < deepList.size(); i++) {
                        deepList.get(i).position = i % 2 + 1;
                    }
                } else {
                    for (int i = 0; i < deepList.size(); i++) {
                        deepList.get(i).position = i % 2;
                    }
                }
                if (titles.size() == 1) {
                    titles.put(previewList.size() + 1, "知识点精讲");
                } else {
                    titles.put(0, "知识点精讲");
                }
            }
            if (reviewList != null && reviewList.size() > 0) {
                if (deepList != null && deepList.size() > 0) {
                    int lastPosition = deepList.get(deepList.size() - 1).position;
                    lastPosition = (0 == lastPosition) ? 1 : 0;
                    for (int i = 0; i < reviewList.size(); i++) {
                        reviewList.get(i).position = i % 2 + lastPosition;
                    }
                } else {
                    if (previewList != null && previewList.size() > 0) {
                        int lastPosition = previewList.get(previewList.size() - 1).position;
                        lastPosition = (0 == lastPosition) ? 1 : 0;
                        for (int i = 0; i < reviewList.size(); i++) {
                            reviewList.get(i).position = i % 2 + lastPosition;
                        }
                    } else {
                        for (int i = 0; i < reviewList.size(); i++) {
                            reviewList.get(i).position = i % 2;
                        }
                    }
                }
                if (0 == titles.size()) {
                    titles.put(0, "必练题详解");
                } else if (1 == titles.size()) {
                    if (previewList != null && previewList.size() > 0) {
                        titles.put(previewList.size() + 1, "必练题详解");
                    } else {
                        titles.put(deepList.size() + 1, "必练题详解");
                    }

                } else if (2 == titles.size()) {
                    titles.put(previewList.size() + deepList.size() + 2, "必练题详解");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (localBroadcastManager != null && subjectChangedReceiver != null) {
            localBroadcastManager.unregisterReceiver(subjectChangedReceiver);
        }
        if (null != waveMediaPlayer) {
            waveMediaPlayer.release();
            waveMediaPlayer = null;
        }
        if (null != bubbleMediaPlayer) {
            bubbleMediaPlayer.release();
            bubbleMediaPlayer = null;
        }
    }
}
