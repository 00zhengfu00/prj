package com.physicmaster.modules.videoplay.cache;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.db.VideoManager;
import com.physicmaster.modules.videoplay.cache.service.DownloadService2;
import com.physicmaster.utils.NetworkUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class VideoDownloadingActivity extends BaseActivity {
    private VideoManager videoManager;
    private ListView lvDownloadingVideos;
    private List<VideoInfoForShow> videoInfosForShow;
    private VideoDownloadingAdapter adapter;
    private TitleBuilder titleBuilder;
    //编辑状态
    private boolean editStatus = true;
    private boolean selectStatus = false;
    private Button btnSelectAll;
    private Button btnDelete;
    private TextView tvHeader;

    //下载服务
    private DownloadService2 ds2;
    //连接下载服务
    private ServiceConnection dsConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            ds2 = ((DownloadService2.MsgBinder) service).getService();
            ds2.setProgressListener2(new DownloadService2.OnProgressListener() {
                @Override
                public void onDownloadStart(String videoId) {
                    //找到当前正在下载的item
                    VideoInfoForShow curDownloadItem = null;
                    //找到当前正在下载的item的position
                    int position = 0;
                    for (int i = 0; i < videoInfosForShow.size(); i++) {
                        if (videoInfosForShow.get(i).getId().equals(videoId)) {
                            curDownloadItem = videoInfosForShow.get(i);
                            position = i;
                            break;
                        }
                    }
                    //将该位置的item移除
                    if (position != 0) {
                        videoInfosForShow.remove(position);
                        //将这个item移到列表头部
                        if (curDownloadItem != null) {
                            videoInfosForShow.add(0, curDownloadItem);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0);
                    tvHeader.setText("下载中");
                }

                @Override
                public void onDownloadStop() {
                    tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.start, 0, 0, 0);
                    tvHeader.setText("暂停中");
                }

                @Override
                public void onProgress(int progress, int downloadingNum, String title) {
                    if (videoInfosForShow == null || videoInfosForShow.size() == 0) {
                        return;
                    }
                    videoInfosForShow.get(0).setProgress(progress);
                    if (progress == 100) {
                        videoInfosForShow.remove(0);
                    }
                    adapter.notifyDataSetChanged();
                    if (videoInfosForShow.size() == 0) {
                        lvDownloadingVideos.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onVideoDownloadFailed(String videoId) {
                }

                @Override
                public void onVideoDownloadSuccess() {

                }
            });
            //根据DownloadService的下载状态设置页面状态
            final int state = ds2.getDownloadState();
            if (state == DownloadService2.RUNNING) {
                tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0);
                tvHeader.setText("下载中");
            } else {
                tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.start, 0, 0, 0);
                tvHeader.setText("暂停中");
            }
            tvHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int state = ds2.getDownloadState();
                    if (state == DownloadService2.RUNNING) {
                        ds2.pauseDownload();
                        tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.start, 0, 0, 0);
                        tvHeader.setText("暂停中");
                    } else {
                        String networkState = NetworkUtils.getNetworkState(VideoDownloadingActivity.this);
                        if (networkState.equals(Constant.NETTYPE_UNCONNECTED) || networkState.equals(Constant.NETTYPE_UNKNOWN)) {
                            //已断网-暂停下载
                            UIUtils.showToast(VideoDownloadingActivity.this, "网络好像出问题了");
                            return;
                        } else {
                            if (networkState.equals(Constant.NETTYPE_WIFI)) {
                                //wifi环境，开始下载
                                tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0);
                                tvHeader.setText("下载中");
                                if (state == DownloadService2.PAUSED) {
                                    ds2.restartDownload();
                                } else {
                                    ds2.startDownload();
                                }
                            } else {
                                //非wifi环境，弹出提示
                                AlertDialog dialog = new AlertDialog.Builder(VideoDownloadingActivity.this).create();
                                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "继续缓存", new
                                        DialogInterface
                                                .OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int
                                                    which) {
                                                BaseApplication.setNone_wifi_prompt_times
                                                        (BaseApplication
                                                                .getNone_wifi_prompt_times()
                                                                + 1);
                                                tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0);
                                                tvHeader.setText("下载中");
                                                if (state == DownloadService2.PAUSED) {
                                                    ds2.restartDownload();
                                                } else {
                                                    ds2.startDownload();
                                                }
                                            }
                                        });
                                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new
                                        DialogInterface
                                                .OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int
                                                    which) {
                                                BaseApplication.setNone_wifi_prompt_times(0);
                                            }
                                        });
                                dialog.setOnCancelListener(new DialogInterface
                                        .OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                    }
                                });
                                dialog.setTitle("您正在使用非wifi网络，缓存将产生流量费用");
                                dialog.show();
                            }
                        }
                    }
                }
            });
            //判断当前正在下载的videoId是否等于队列第一个videoId，如果不等于，就把videoItem移到队尾
            String videoId = ds2.getCurrVideoId();
            if (TextUtils.isEmpty(videoId)) {
                return;
            }
            if (videoInfosForShow != null) {
                //找到当前正在下载的item
                VideoInfoForShow curDownloadItem = null;
                //找到当前正在下载的item的position
                int position = 0;
                for (int i = 0; i < videoInfosForShow.size(); i++) {
                    if (videoInfosForShow.get(i).getId().equals(videoId)) {
                        curDownloadItem = videoInfosForShow.get(i);
                        position = i;
                        break;
                    }
                }
                //将该位置的item移除
                if (position != 0) {
                    videoInfosForShow.remove(position);
                    //将这个item移到列表头部
                    if (curDownloadItem != null) {
                        videoInfosForShow.add(0, curDownloadItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void findViewById() {
        lvDownloadingVideos = (ListView) findViewById(R.id.lv_downloading);
        btnSelectAll = (Button) findViewById(R.id.btn_select_all);
        btnDelete = (Button) findViewById(R.id.btn_delete);
    }

    /**
     * 绑定下载服务
     */
    private void bindService() {
        Intent dsIntent = new Intent(getApplicationContext(), DownloadService2.class);
        bindService(dsIntent, dsConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void initView() {
        initTitle();
        addHeader();

        bindService();

        videoManager = new VideoManager(this);
        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelectStaus();
            }
        });
        lvDownloadingVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == ds2) {
                    UIUtils.showToast(VideoDownloadingActivity.this, "下载服务不可用，请稍后再试……");
                    bindService();
                    return;
                }
                final ProgressDialog dialog = ProgressDialog.show(VideoDownloadingActivity.this,
                        "正在删除", "请等候……", true, false, null);
                final List<VideoInfoForShow> delVideoList = new ArrayList<>();
                dialog.show();
                final AsyncTask<String, Integer, Boolean> deleteTask = new AsyncTask<String, Integer,
                        Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        if (isCancelled()) {
                            return false;
                        }
                        boolean isDownloadingTaskDelete = false;
                        for (int i = 0; i < videoInfosForShow.size(); i++) {
                            VideoInfoForShow videoInfoForShow = videoInfosForShow.get(i);
                            if (videoInfoForShow.getStatus() == 2) {
                                if (i == 0) {
                                    isDownloadingTaskDelete = true;
                                }
                                boolean rlt = videoManager.deleteVideoByVideoId(videoInfoForShow
                                        .getId());
                                if (!rlt) {
                                    return false;
                                }
                                delVideoList.add(videoInfoForShow);
                            }
                        }
                        return isDownloadingTaskDelete;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if (isCancelled()) {
                            return;
                        }
                        dialog.dismiss();
                        List<String> videIds = new ArrayList<>();
                        UIUtils.showToast(VideoDownloadingActivity.this, "删除成功");
                        for (VideoInfoForShow videoInfoForShow : delVideoList) {
                            videIds.add(videoInfoForShow.getId());
                            videoInfosForShow.remove(videoInfoForShow);
                        }
                        ds2.deleteTask(videIds, aBoolean);
                        toggle();
                        adapter.notifyDataSetChanged();
                    }
                };
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        deleteTask.cancel(true);
                    }
                });
                deleteTask.execute("");
            }
        });
    }

    /**
     * 给listview添加暂停和开始的头布局
     */
    private void addHeader() {
        tvHeader = new TextView(this);
        tvHeader.setTextColor(getResources().getColor(R.color.colorCache));
        tvHeader.setText("下载中");
        tvHeader.setTextSize(18);
        tvHeader.setPadding(0, getResources().getDimensionPixelSize(R.dimen.dimen_10), 0,
                getResources().getDimensionPixelSize(R.dimen.dimen_10));
        tvHeader.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.dimen_10));
        tvHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0);
        lvDownloadingVideos.addHeaderView(tvHeader);
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

    @Override
    protected int getContentLayout() {
        return R.layout.activity_video_downloading;
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
                }).setRightText("编辑").setRightTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggle();
                    }
                }).setMiddleTitleText("正在缓存");
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
            findViewById(R.id.divide_line).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_action).setVisibility(View.VISIBLE);
            lvDownloadingVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    position = position - 1;
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
            findViewById(R.id.divide_line).setVisibility(View.GONE);
            findViewById(R.id.ll_action).setVisibility(View.GONE);
            lvDownloadingVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                }
            });
        }
        adapter.notifyDataSetChanged();
        editStatus = !editStatus;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                            videoInfosForShow = new ArrayList<>();
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
                            adapter = new VideoDownloadingAdapter(videoInfosForShow,
                                    VideoDownloadingActivity.this);
                            lvDownloadingVideos.setAdapter(adapter);
                            //判断当前正在下载的videoId是否等于队列第一个videoId，如果不等于，就把videoItem移到队尾
                            if (null == ds2) {
                                return;
                            }
                            String videoId = ds2.getCurrVideoId();
                            if (TextUtils.isEmpty(videoId)) {
                                return;
                            }
                            for (VideoInfoForShow videoInfoForShow : videoInfosForShow) {
                                if (!videoInfoForShow.getId().equals(videoId)) {
                                    videoInfosForShow.remove(videoInfoForShow);
                                    videoInfoForShow.setProgress(0);
                                    videoInfosForShow.add(videoInfoForShow);
                                    break;
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                };
        checkDownloadingVideoTask.execute("");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ds2 != null) {
            unbindService(dsConn);
        }
    }
}
