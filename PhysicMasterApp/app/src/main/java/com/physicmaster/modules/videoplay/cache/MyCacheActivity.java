package com.physicmaster.modules.videoplay.cache;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.modules.videoplay.VideoPlayCacheActivity;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.db.VideoManager;
import com.physicmaster.modules.videoplay.cache.service.DownloadService2;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.video.CheckAuthResponse;
import com.physicmaster.net.service.video.CheckAuthService;
import com.physicmaster.utils.FileSizeUtil;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.TitleBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MyCacheActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvSpace;
    private ProgressBar pbSpace;
    //正在下载
    private LinearLayout llDownloading;
    private TextView tvProgress;
    private TextView tvPreview, tvJinjiang, tvReview;
    private View indicator1, indicator2, indicator3;
    private ProgressBar progressBarLoading;
    private TextView tvTitle;
    private TextView tvVideoTitle;

    private VideoManager videoManager;
    private VideoDownloadedAdapter adapter;
    private List<VideoInfoForShow> videoInfosForShow;
    private ListView lvDownloadedVideos;
    private TextView tvCachedNum;
    private int index = 0;

    //编辑状态
    private boolean editStatus = true;
    private boolean selectStatus = false;
    private Button btnSelectAll;
    private Button btnDelete;
    private TitleBuilder titleBuilder;
    private ImageView ivEmpty;

    //下载服务
    private DownloadService2 ds2;
    //连接下载服务
    private ServiceConnection dsConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ds2 = ((DownloadService2.MsgBinder) service).getService();
            ds2.setProgressListener1(new DownloadService2.OnProgressListener() {
                @Override
                public void onDownloadStart(String videoId) {

                }

                @Override
                public void onDownloadStop() {

                }

                @Override
                public void onProgress(int progress, int downloadingNum, String title) {
                    tvProgress.setText(progress + "%");
                    progressBarLoading.setProgress(progress);
                    tvTitle.setText("正在缓存（" + (downloadingNum + 1) + "）");
                    tvVideoTitle.setText(title);
                    //全部下载完毕后隐藏掉正在缓存布局
                    if (downloadingNum == 0) {
                        llDownloading.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onVideoDownloadFailed(String videoId) {

                }

                @Override
                public void onVideoDownloadSuccess() {
                    checkAllDownloadVideos(index);
                    checkAllDownloadVideosSpace();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void findViewById() {
        llDownloading = (LinearLayout) findViewById(R.id.ll_downloading);
        tvSpace = (TextView) findViewById(R.id.tv_space);
        pbSpace = (ProgressBar) findViewById(R.id.progress_space);
        btnSelectAll = (Button) findViewById(R.id.btn_select_all);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        ivEmpty = (ImageView) findViewById(R.id.iv_empty);
        initTitle();
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) llDownloading.findViewById(R.id.course_title);
        tvVideoTitle = (TextView) llDownloading.findViewById(R.id.tv_video_title);
        tvProgress = (TextView) llDownloading.findViewById(R.id.tv_progress);

        tvPreview = (TextView) findViewById(R.id.tv_preview);
        tvJinjiang = (TextView) findViewById(R.id.tv_jinjiang);
        tvReview = (TextView) findViewById(R.id.tv_review);
        tvPreview.setOnClickListener(this);
        tvJinjiang.setOnClickListener(this);
        tvReview.setOnClickListener(this);

        indicator1 = findViewById(R.id.indicator_1);
        indicator2 = findViewById(R.id.indicator_2);
        indicator3 = findViewById(R.id.indicator_3);

        progressBarLoading = (ProgressBar) findViewById(R.id.progress_loading);
        videoManager = new VideoManager(this);
        videoInfosForShow = new ArrayList<>();
        lvDownloadedVideos = (ListView) findViewById(R.id.lv_video_list);
        adapter = new VideoDownloadedAdapter(videoInfosForShow, MyCacheActivity.this);
        lvDownloadedVideos.setAdapter(adapter);
        tvCachedNum = (TextView) findViewById(R.id.tv_cached_num);
        videoManager = new VideoManager(this);

        //绑定DownloadService
        Intent dsIntent = new Intent(getApplicationContext(), DownloadService2.class);
        bindService(dsIntent, dsConn, BIND_AUTO_CREATE);

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelectStaus();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = ProgressDialog.show(MyCacheActivity.this,
                        "正在删除", "请等候……", true, false, null);
                final List<VideoInfoForShow> delVideoList = new ArrayList<VideoInfoForShow>();
                dialog.show();
                AsyncTask<String, Integer, Boolean> deleteTask = new AsyncTask<String, Integer,
                        Boolean>() {

                    @Override
                    protected Boolean doInBackground(String... params) {
                        for (VideoInfoForShow videoInfoForShow : videoInfosForShow) {
                            if (videoInfoForShow.getStatus() == 2) {
                                boolean rlt = videoManager.deleteVideoByVideoId(videoInfoForShow.getId());
                                if (!rlt) {
                                    return false;
                                }
                                delVideoList.add(videoInfoForShow);
                            }
                            videoInfoForShow.setStatus(0);
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        dialog.dismiss();
                        if (aBoolean) {
                            UIUtils.showToast(MyCacheActivity.this, "删除成功");
                            for (VideoInfoForShow videoInfoForShow : delVideoList) {
                                videoInfosForShow.remove(videoInfoForShow);
                            }
                            adapter.notifyDataSetChanged();
                            btnDelete.setText("删除");
                            toggle();
                            checkAllDownloadVideosSpace();
                            checkAllDownloadVideos(index);
                        } else {
                            UIUtils.showToast(MyCacheActivity.this, "删除失败");
                        }
                    }
                };
                deleteTask.execute("");
            }
        });
        lvDownloadedVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkVideoAuth(videoInfosForShow.get(position).getName(), videoInfosForShow.get(position).getId());
            }
        });
        deleteOldCache();
    }

    private void checkVideoAuth(final String title, final String videoId) {
        String realVideoId = "";
        if (videoId != null) {
            String[] splitString = videoId.split("-");
            if (splitString.length > 0) {
                realVideoId = splitString[0];
            }
        }
        final String uuid = UUID.randomUUID().toString();
        CheckAuthService service = new CheckAuthService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CheckAuthResponse>() {
            @Override
            public void onGetData(CheckAuthResponse data) {
                if (1 == data.data.canPlay && uuid.equals(data.data.rand)) {
                    Intent intent = new Intent(MyCacheActivity.this, VideoPlayCacheActivity.class);
                    intent.putExtra("videoId", videoId);
                    intent.putExtra("videoTitle", title);
                    startActivity(intent);
                } else {
                    UIUtils.showToast(MyCacheActivity.this, "您没有权限播放此视频");
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(MyCacheActivity.this, errorMsg);
            }
        });
        service.postLogined("videoId=" + realVideoId + "&rand=" + uuid, false);
    }

    private void toggleSelectStaus() {
        if (!selectStatus) {
            for (VideoInfoForShow videoInfoForShow : videoInfosForShow) {
                videoInfoForShow.setStatus(2);
            }
            btnSelectAll.setText("全不选");
            btnDelete.setText("删除(" + videoInfosForShow.size() + ")");
        } else {
            for (VideoInfoForShow videoInfoForShow : videoInfosForShow) {
                videoInfoForShow.setStatus(1);
            }
            btnSelectAll.setText("全选");
            btnDelete.setText("删除");
        }
        adapter.notifyDataSetChanged();
        selectStatus = !selectStatus;
    }

    /**
     * 右上角按钮状态
     */
    private void toggle() {
        if (videoInfosForShow == null || videoInfosForShow.size() == 0) {
            titleBuilder.setRightTextOrImageListener(null);
            titleBuilder.setRightText("");
            findViewById(R.id.ll_action).setVisibility(View.GONE);
            return;
        }
        if (editStatus) {
            titleBuilder.setRightText("取消");
            for (VideoInfoForShow videoInfoForShow : videoInfosForShow) {
                videoInfoForShow.setStatus(1);
            }
            findViewById(R.id.ll_action).setVisibility(View.VISIBLE);
            lvDownloadedVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int status = videoInfosForShow.get(position).getStatus();
                    if (status == 1) {
                        videoInfosForShow.get(position).setStatus(2);
                        int count = calSelecCount();
                        if (count != 0) {
                            btnDelete.setText("删除(" + count + ")");
                        } else {
                            btnDelete.setText("删除");
                        }
                        adapter.notifyDataSetChanged();
                    } else if (status == 2) {
                        videoInfosForShow.get(position).setStatus(1);
                        int count = calSelecCount();
                        if (count != 0) {
                            btnDelete.setText("删除(" + count + ")");
                        } else {
                            btnDelete.setText("删除");
                        }
                        adapter.notifyDataSetChanged();
                    } else {

                    }
                }
            });
        } else {
            titleBuilder.setRightText("编辑");
            btnDelete.setText("删除");
            for (VideoInfoForShow videoInfoForShow : videoInfosForShow) {
                videoInfoForShow.setStatus(0);
            }
            findViewById(R.id.ll_action).setVisibility(View.GONE);
            lvDownloadedVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    checkVideoAuth(videoInfosForShow.get(position).getName(), videoInfosForShow.get(position).getId());
                }
            });
        }
        adapter.notifyDataSetChanged();
        editStatus = !editStatus;
    }

    /**
     * 计算当前被选中的条目数目
     *
     * @return
     */
    private int calSelecCount() {
        int count = 0;
        for (VideoInfoForShow videoInfoForShow : videoInfosForShow) {
            if (videoInfoForShow.getStatus() == 2) {
                count++;
            }
        }
        return count;
    }

    private void selectTab(int index) {
        this.index = index;
        switch (index) {
            case 0:
                tvPreview.setSelected(true);
                indicator1.setBackgroundColor(getResources().getColor(R.color.colorTitleBlue));
                tvJinjiang.setSelected(false);
                indicator2.setBackgroundColor(getResources().getColor(R.color.transparent));
                tvReview.setSelected(false);
                indicator3.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
            case 1:
                tvPreview.setSelected(false);
                indicator1.setBackgroundColor(getResources().getColor(R.color.transparent));
                tvJinjiang.setSelected(true);
                indicator2.setBackgroundColor(getResources().getColor(R.color.colorTitleBlue));
                tvReview.setSelected(false);
                indicator3.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
            case 2:
                tvPreview.setSelected(false);
                indicator1.setBackgroundColor(getResources().getColor(R.color.transparent));
                tvJinjiang.setSelected(false);
                indicator2.setBackgroundColor(getResources().getColor(R.color.transparent));
                tvReview.setSelected(true);
                indicator3.setBackgroundColor(getResources().getColor(R.color.colorTitleBlue));
                break;
        }
        checkAllDownloadVideos(index);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_course_cache;
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        titleBuilder = new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }).setMiddleTitleText("缓存中心").setRightText("编辑").setRightTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggle();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkDownloadingVideos();
    }

    /**
     * 查询正在下载的视频
     */
    private void checkDownloadingVideos() {
        AsyncTask<String, Integer, List<VideoInfo>> checkDownloadingVideoTask = new
                AsyncTask<String, Integer, List<VideoInfo>>() {

                    @Override
                    protected List<VideoInfo> doInBackground(String... params) {
                        return videoManager.getDownloadingVideos();
                    }

                    @Override
                    protected void onPostExecute(List<VideoInfo> videoInfos) {
                        super.onPostExecute(videoInfos);
                        if (videoInfos != null && videoInfos.size() > 0) {
                            llDownloading.setVisibility(View.VISIBLE);
                            llDownloading.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(MyCacheActivity.this, VideoDownloadingActivity.class);
                                    startActivity(intent);
                                }
                            });
                            tvTitle.setText("正在缓存（" + videoInfos.size() + "）");
                            //找到正在缓存的视频
                            for (VideoInfo videoInfo : videoInfos) {
                                if (videoInfo.getState() == VideoInfo.STATE_DOWNLOADING) {
                                    tvVideoTitle.setText(videoInfo.getName());
                                    int progress = (int) ((float) videoInfo.getDownloadedTsFileNum() / (float) videoInfo.getTsFileNum() * 100);
                                    tvProgress.setText(progress + "%");
                                    progressBarLoading.setProgress(progress);
                                    break;
                                }
                            }
                        } else {
                            llDownloading.setVisibility(View.GONE);
                        }
                        checkAllDownloadVideosSpace();
                    }
                };
        checkDownloadingVideoTask.execute("");
    }

    /**
     * 获取sd卡可用空间
     *
     * @return
     */
    private long getSdcardUserableSpace() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //sdcard状态是没有挂载的情况
            return 0;
        }
        //得到sdcard的目录作为一个文件对象
        File sdcard_filedir = Environment.getExternalStorageDirectory();
        return sdcard_filedir.getUsableSpace();//获取文件目录对象剩余空间
    }

    /**
     * 获取sd卡总空间
     *
     * @return
     */
    private long getSdcardTotalSpace() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //sdcard状态是没有挂载的情况
            return 0;
        }
        //得到sdcard的目录作为一个文件对象
        File sdcard_filedir = Environment.getExternalStorageDirectory();
        return sdcard_filedir.getTotalSpace();
    }

    /**
     * 查询已下载课程信息
     */
    private void checkAllDownloadVideos(final int type) {
        final int videoType;
        if (type == 0) {
            videoType = 1;
        } else if (type == 1) {
            videoType = 3;
        } else {
            videoType = 2;
        }
        AsyncTask<String, Integer, List<VideoInfo>> checkDownloadedVideos = new
                AsyncTask<String, Integer, List<VideoInfo>>() {
                    @Override
                    protected List<VideoInfo> doInBackground(String... params) {
                        String userId = BaseApplication.getUserData().dtUserId;
                        return videoManager.getVideosByVideoType(videoType, VideoInfo.STATE_DOWNLOADED, userId);
                    }

                    @Override
                    protected void onPostExecute(List<VideoInfo> videoInfos) {
                        super.onPostExecute(videoInfos);
                        videoInfosForShow.clear();
                        if (videoInfos != null && videoInfos.size() > 0) {
                            ivEmpty.setVisibility(View.GONE);
                            lvDownloadedVideos.setVisibility(View.VISIBLE);
                            for (VideoInfo videoInfo : videoInfos) {
                                VideoInfoForShow videoInfoForShow = new VideoInfoForShow();
                                videoInfoForShow.setPosterUrl(videoInfo.getPosterUrl());
                                videoInfoForShow.setTsFileNum(videoInfo.getTsFileNum());
                                videoInfoForShow.setTotalSize(videoInfo.getTotalSize());
                                videoInfoForShow.setName(videoInfo.getName());
                                videoInfoForShow.setStatus(0);
                                videoInfoForShow.setCourseId(videoInfo.getCourseId());
                                videoInfoForShow.setId(videoInfo.getId());
                                videoInfoForShow.setUserId(videoInfo.getUserId());
                                videoInfoForShow.setDownloadedSize(videoInfo.getDownloadedSize());
                                videoInfoForShow.setCreateDatetime(videoInfo.getCreateDatetime());
                                videoInfosForShow.add(videoInfoForShow);
                            }
                            tvCachedNum.setText("已缓存（" + videoInfos.size() + "）");
                            tvCachedNum.setVisibility(View.VISIBLE);
                            titleBuilder.setRightText("编辑");
                            titleBuilder.setRightTextOrImageListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toggle();
                                }
                            });
                            btnDelete.setText("删除");
                            btnSelectAll.setText("全选");
                            selectStatus = false;
                            for (VideoInfoForShow videoInfoForShow : videoInfosForShow) {
                                videoInfoForShow.setStatus(0);
                            }
                            findViewById(R.id.ll_action).setVisibility(View.GONE);
                            lvDownloadedVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    checkVideoAuth(videoInfosForShow.get(position).getName(), videoInfosForShow.get(position).getId());
                                }
                            });
                            editStatus = true;
                        } else {
                            ivEmpty.setVisibility(View.VISIBLE);
                            lvDownloadedVideos.setVisibility(View.GONE);
                            tvCachedNum.setVisibility(View.GONE);
                            titleBuilder.setRightTextOrImageListener(null);
                            titleBuilder.setRightText("");
                            findViewById(R.id.ll_action).setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                };
        checkDownloadedVideos.execute("");
    }


    /**
     * 查询已下载课程信息
     */
    private void checkAllDownloadVideosSpace() {
        AsyncTask<String, Integer, List<VideoInfo>> checkDownloadedVideosSpaceTask = new
                AsyncTask<String, Integer,
                        List<VideoInfo>>() {
                    @Override
                    protected List<VideoInfo> doInBackground(String... params) {
                        String userId = BaseApplication.getUserData().dtUserId;
                        return videoManager.getVideosByUserId(userId, VideoInfo.STATE_DOWNLOADED);
                    }

                    @Override
                    protected void onPostExecute(List<VideoInfo> videoInfos) {
                        super.onPostExecute(videoInfos);
                        if (videoInfos != null) {
                            long totalSize = 0;
                            for (VideoInfo videoInfo : videoInfos) {
                                totalSize += videoInfo.getDownloadedSize();
                            }
                            long userableSpace = getSdcardUserableSpace();
                            tvSpace.setText("占用空间" + FileSizeUtil.FormetFileSize(totalSize) + "，可用空间" + FileSizeUtil.FormetFileSize(userableSpace));
                            float percent = (float) totalSize / (float) userableSpace;
                            int percentInt = (int) (percent * 100);
                            pbSpace.setProgress(percentInt);
                        } else {
                            tvSpace.setText("占用空间0GB，可用空间" + FileSizeUtil.FormetFileSize(getSdcardUserableSpace()));
                            pbSpace.setProgress(0);
                        }
                    }
                };
        checkDownloadedVideosSpaceTask.execute("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dsConn != null) {
            unbindService(dsConn);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_preview:
                selectTab(0);
                break;
            case R.id.tv_jinjiang:
                selectTab(1);
                break;
            case R.id.tv_review:
                selectTab(2);
                break;
        }
    }

    /**
     * 非会员删掉老缓存
     */
    private void deleteOldCache() {
        AsyncTask<String, Integer, Boolean> checkDownloadedVideos = new
                AsyncTask<String, Integer, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        List<VideoInfo> videoInfos = videoManager.getVideosByVideoType(0, VideoInfo.STATE_DOWNLOADED);
                        for (VideoInfo videoInfo : videoInfos) {
                            videoManager.deleteVideoByVideoId(videoInfo.getId());
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean bool) {
                        super.onPostExecute(bool);
                        selectTab(0);
                    }
                };
        checkDownloadedVideos.execute("");
    }
}
