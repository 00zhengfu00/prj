/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.lswuyou.tv.pm.leanback;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.activity.BuyMemberActivity;
import com.lswuyou.tv.pm.activity.ChapterDetailActivity;
import com.lswuyou.tv.pm.activity.UnLoginActivity;
import com.lswuyou.tv.pm.activity.VideoIntroActivity;
import com.lswuyou.tv.pm.activity.VideoIntroForExcActivity;
import com.lswuyou.tv.pm.activity.VideoPlay2Activity;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.fragment.UserFragment;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.net.response.course.AnotherBatchResponse;
import com.lswuyou.tv.pm.net.response.homepage.ChapterListResponse.DataBean.BookListBean;
import com.lswuyou.tv.pm.net.response.homepage.GetJingxuanListResponse;
import com.lswuyou.tv.pm.net.response.homepage.GetJingxuanListResponse.DataBean.ChoiceListBean;
import com.lswuyou.tv.pm.net.response.video.GetVideoPlayResponse;
import com.lswuyou.tv.pm.net.response.video.VideoPlayInfo;
import com.lswuyou.tv.pm.net.service.AnotherBatchLoginedService;
import com.lswuyou.tv.pm.net.service.AnotherBatchUnLoginService;
import com.lswuyou.tv.pm.net.service.GetJingxuanListLoginedService;
import com.lswuyou.tv.pm.net.service.GetJingxuanListUnLoginService;
import com.lswuyou.tv.pm.net.service.GetVideoPlayLoginedService;
import com.lswuyou.tv.pm.net.service.GetVideoPlayUnLoginService;
import com.lswuyou.tv.pm.utils.AnimationUtil;
import com.lswuyou.tv.pm.utils.UIUtils;
import com.lswuyou.tv.pm.utils.Utils;
import com.lswuyou.tv.pm.video.VideoView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class RecommendFragment extends BrowseFragment implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = "RecommendFragment";

    private static final int GRID_ITEM_WIDTH = 800;
    private static final int GRID_ITEM_HEIGHT = 300;

    private ArrayObjectAdapter mRowsAdapter;
    private int subjectId;
    public List<ChoiceListBean> choiceList;
    private List<BookListBean.ItemListBean> banner;
    private View headerView;
    private boolean play = true;
    private int curPlayIndex = 0;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        setupUIElements();

        loadData();

        setupEventListeners();

        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(onLoginSuccReceiver, new IntentFilter(UserFragment
                .USERINFO_UPDATE));
    }

    private BroadcastReceiver onLoginSuccReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mRowsAdapter != null) {
                mRowsAdapter.notifyArrayItemRangeChanged(0, 1);
            }
        }
    };

    @Override
    public void installTitleView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        setTitleView(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        subjectId = getArguments().getInt("subjectId", 0);
        OpenApiDataServiceBase service = null;
        if (BaseApplication.getLoginUserInfo() != null) {
            service = new GetJingxuanListLoginedService(getActivity());
        } else {
            service = new GetJingxuanListUnLoginService(getActivity());
        }
        service.setCallback(new IOpenApiDataServiceCallback<GetJingxuanListResponse>() {
            @Override
            public void onGetData(GetJingxuanListResponse data) {
                choiceList = data.data.choiceList;
                banner = data.data.banner;
                ListRowPresenter listRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, false);
                listRowPresenter.setShadowEnabled(false);
                listRowPresenter.setKeepChildForeground(false);
                mRowsAdapter = new ArrayObjectAdapter(listRowPresenter);

                GridItemPresenter mGridPresenter = new GridItemPresenter();
                ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
                gridRowAdapter.add(getResources().getString(R.string.grid_view));
                mRowsAdapter.add(new ListRow(gridRowAdapter));
                CardPresenter cardPresenter = new CardPresenter();
                for (ChoiceListBean choiceListBean : choiceList) {
                    if (null == choiceListBean.videoList || 0 == choiceListBean.videoList.size()) {
                        choiceList.remove(choiceListBean);
                    }
                }
                for (int i = 0; i < choiceList.size(); i++) {
                    List<BookListBean.ItemListBean> itemList = new ArrayList<>();
                    itemList.addAll(choiceList.get(i).videoList);
                    int rowCount = (itemList.size() % 5 == 0) ? itemList.size() / 5 : itemList.size() / 5 + 1;
                    if (0 == rowCount) {
                        rowCount = 1;
                    }
                    for (int i1 = 0; i1 < rowCount; i1++) {
                        int startI = 5 * i1;
                        int endI = (5 * (i1 + 1) > itemList.size()) ? itemList.size() : 5 * (i1 + 1);
                        List<BookListBean.ItemListBean> itemList5 = itemList.subList(startI, endI);
                        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                        boolean isLastRow = false;
                        if ((i == (choiceList.size() - 1)) && (i1 == (rowCount - 1))) {
                            isLastRow = true;
                        }
                        calPosition(itemList5, isLastRow, listRowAdapter);
                        if (itemList5.size() > 0) {
                            if (0 == i1) {
                                //第一行需要加一个标题
                                HeaderItem header = new HeaderItem(i, choiceList.get(i).subjectName);
                                ListRow listRow = new ListRow(header, listRowAdapter);
                                mRowsAdapter.add(listRow);
                            } else {
                                ListRow listRow = new ListRow(listRowAdapter);
                                mRowsAdapter.add(listRow);
                            }
                        }
                    }
                }
                setAdapter(mRowsAdapter);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
            }
        });
        if (BaseApplication.getLoginUserInfo() != null) {
            service.postAES("subjectId=" + subjectId, false);
        } else {
            service.post("subjectId=" + subjectId, false);
        }
    }

    /**
     * 播放推荐的第一个视频
     */
    private void playFirstVideo() {
        play = true;
        play(0);
        TextView tv1 = headerView.findViewById(R.id.tv_recommend_1);
        tv1.setBackgroundColor(getResources().getColor(R.color.text_still_color));
        tv1.setTextColor(getResources().getColor(R.color.color_2aa6b6));
    }

    /**
     * 计算位置
     *
     * @param itemList
     * @param lastRow
     * @param listRowAdapter
     */
    private void calPosition(List<BookListBean.ItemListBean> itemList, boolean lastRow, ArrayObjectAdapter listRowAdapter) {
        for (int i1 = 0; i1 < itemList.size(); i1++) {
            //最后一排
            if (lastRow) {
                //最后一排第一个
                if (i1 == 0) {
                    itemList.get(i1).postitionType = BookListBean.ItemListBean.FIRSTBOTTEM_ITEM;
                } else if (i1 == itemList.size() - 1) {//最后一排最后一个
                    itemList.get(i1).postitionType = BookListBean.ItemListBean.LASTBOTTEM_ITEM;
                } else {//最后一排其它
                    itemList.get(i1).postitionType = BookListBean.ItemListBean.BOTTOM_ITEM;
                }
            } else {//非最后一排
                //第一个item
                if (i1 == 0) {
                    itemList.get(i1).postitionType = BookListBean.ItemListBean.FIRST_ITEM;
                } else if (i1 == itemList.size() - 1) {//最后一个item
                    itemList.get(i1).postitionType = BookListBean.ItemListBean.LAST_ITEM;
                }
            }
            listRowAdapter.add(itemList.get(i1));
        }
    }

    private void setupUIElements() {
        setHeadersState(HEADERS_DISABLED);
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            BookListBean.ItemListBean info = (BookListBean.ItemListBean) item;
            if (info.chapterId != 0) {
                Intent intent = new Intent(getActivity(), ChapterDetailActivity.class);
                intent.putExtra("chapterId", info.chapterId);
                intent.putExtra("title", info.title + "");
                startActivity(intent);
            } else if (info.videoId != 0) {
                if (1 == info.tvVideoType) {
                    Intent intent = new Intent(getActivity(), VideoIntroActivity.class);
                    intent.putExtra("videoId", info.videoId);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), VideoIntroForExcActivity.class);
                    intent.putExtra("videoId", info.videoId);
                    startActivity(intent);
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recommend_1:
            case R.id.tv_recommend_2:
            case R.id.tv_recommend_3:
            case R.id.tv_recommend_4:
                Intent intent = new Intent(getActivity(), VideoPlay2Activity.class);
                intent.putExtra("videoId", banner.get(curPlayIndex).videoId);
                intent.putExtra("chapterId", banner.get(curPlayIndex).chapterId);
                startActivity(intent);
                break;
            case R.id.tv_get_new:
                TextView tv4 = headerView.findViewById(R.id.tv_recommend_4);
                tv4.setBackgroundResource(R.drawable.bg_textview_recommend);
                tv4.setTextColor(getResources().getColor(R.color.white));
                getAnotherBatch();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        resetState();
        switch (v.getId()) {
            case R.id.tv_recommend_1:
                play(0);
                break;
            case R.id.tv_recommend_2:
                play(1);
                break;
            case R.id.tv_recommend_3:
                play(2);
                break;
            case R.id.tv_recommend_4:
                play(3);
                break;
            case R.id.tv_get_new:
                TextView tv4 = headerView.findViewById(R.id.tv_recommend_4);
                tv4.setBackgroundColor(getResources().getColor(R.color.text_still_color));
                tv4.setTextColor(getResources().getColor(R.color.color_2aa6b6));
                break;
            default:
                break;
        }
    }

    /**
     * textview 恢复状态
     */
    private void resetState() {
        TextView tv1 = headerView.findViewById(R.id.tv_recommend_1);
        TextView tv2 = headerView.findViewById(R.id.tv_recommend_2);
        TextView tv3 = headerView.findViewById(R.id.tv_recommend_3);
        TextView tv4 = headerView.findViewById(R.id.tv_recommend_4);
        resetTextViewState(tv1);
        resetTextViewState(tv2);
        resetTextViewState(tv3);
        resetTextViewState(tv4);
    }

    /**
     * 单个TextView恢复状态
     *
     * @param textView
     */
    private void resetTextViewState(TextView textView) {
        textView.setBackgroundResource(R.drawable.bg_textview_recommend);
        textView.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * 开始播放
     */
    private void play(int index) {
        curPlayIndex = index;
        if (!play) {
            return;
        }
        int videoId = banner.get(index).videoId;
        int chapterId = banner.get(index).chapterId;
        ImageView ivPoster = headerView.findViewById(R.id.iv_poster);
        Glide.with(this).load(banner.get(index).poster).into(ivPoster);
        VideoView videoView = headerView.findViewById(R.id.videoView_recommend);
        ivPoster.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        getPlayData(videoId, chapterId);
    }

    //获取视频播放数据
    private void getPlayData(int videoId, int chapterId) {
        OpenApiDataServiceBase service = null;
        if (Utils.isUserLogined()) {
            service = new GetVideoPlayLoginedService(getActivity());
        } else {
            service = new GetVideoPlayUnLoginService(getActivity());
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
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        //videoId
        //videoQuality 1:全高清FHD  2:高清HD  3:标清SD   4:流畅LD
        //先获取用户设置的视频清晰度，如果用户未设置，视频播放采用服务端推荐的清晰度
        String quality = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys.VIDEO_QUELITY_TAG);
        if (TextUtils.isEmpty(quality)) {
            quality = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys.RECOMMEND_QUALITY);
        }
        if (TextUtils.isEmpty(quality) || !isQualitylegal(quality)) {
            quality = "HD";
        }
        if (Utils.isUserLogined()) {
            service.postAES("videoId=" + videoId + "&videoQuality=" + quality + "&chapterId=" + chapterId, true);
        } else {
            service.post("videoId=" + videoId + "&videoQuality=" + quality + "&chapterId=" + chapterId, true);
        }
    }

    //开始播放视频
    private void refreshUI(VideoPlayInfo info) {
        if (info.canPlay != 1) {
            Toast.makeText(getActivity(), "视频不可播放！", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(getActivity().getCacheDir(), "videoplay.m3u8");
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
        final VideoView mVideoView = headerView.findViewById(R.id.videoView_recommend);
        final ImageView ivPoster = headerView.findViewById(R.id.iv_poster);
        mVideoView.setSilent(true);
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_CONTAINER);
        mVideoView.setVideoPath(file.getAbsolutePath());
        mVideoView.start();
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mVideoView.setVisibility(View.VISIBLE);
                ivPoster.setVisibility(View.GONE);
            }
        });
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                playNext();
            }
        });
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VideoPlay2Activity.class);
                intent.putExtra("videoId", banner.get(curPlayIndex).videoId);
                intent.putExtra("chapterId", banner.get(curPlayIndex).chapterId);
                startActivity(intent);
            }
        });
        mVideoView.setSilent(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (headerView != null) {
            VideoView mVideoView = headerView.findViewById(R.id.videoView_recommend);
            if (headerView != null && mVideoView.isPlaying()) {
                mVideoView.pause();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (headerView == null) {
            return;
        }
        VideoView mVideoView = headerView.findViewById(R.id.videoView_recommend);
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
        }
    }

    /**
     * 播放下一个视频
     */
    private void playNext() {
        curPlayIndex++;
        TextView tv1 = headerView.findViewById(R.id.tv_recommend_1);
        TextView tv2 = headerView.findViewById(R.id.tv_recommend_2);
        TextView tv3 = headerView.findViewById(R.id.tv_recommend_3);
        TextView tv4 = headerView.findViewById(R.id.tv_recommend_4);
        if (curPlayIndex > 3) {
            curPlayIndex = 0;
        }
        //上一个播放的是第四个视频
        if (curPlayIndex == 0) {
            if (tv4.isFocused()) {
                tv1.setBackgroundResource(R.drawable.bg_textview_recommend);
                tv1.requestFocus();
            } else {
                tv4.setBackgroundResource(R.drawable.bg_textview_recommend);
                tv4.setTextColor(getResources().getColor(R.color.white));
                tv1.setBackgroundColor(getResources().getColor(R.color.text_still_color));
                tv1.setTextColor(getResources().getColor(R.color.color_2aa6b6));
            }
        } else if (curPlayIndex == 1) {//上一个播放的是第1个视频
            if (tv1.isFocused()) {
                tv2.setBackgroundResource(R.drawable.bg_textview_recommend);
                tv2.requestFocus();
            } else {
                tv1.setBackgroundResource(R.drawable.bg_textview_recommend);
                tv1.setTextColor(getResources().getColor(R.color.white));
                tv2.setBackgroundColor(getResources().getColor(R.color.text_still_color));
                tv2.setTextColor(getResources().getColor(R.color.color_2aa6b6));
            }
        } else if (curPlayIndex == 2) {//上一个播放的是第2个视频
            if (tv2.isFocused()) {
                tv3.setBackgroundResource(R.drawable.bg_textview_recommend);
                tv3.requestFocus();
            } else {
                tv2.setBackgroundResource(R.drawable.bg_textview_recommend);
                tv2.setTextColor(getResources().getColor(R.color.white));
                tv3.setBackgroundColor(getResources().getColor(R.color.text_still_color));
                tv3.setTextColor(getResources().getColor(R.color.color_2aa6b6));
            }
        } else if (curPlayIndex == 3) {//上一个播放的是第3个视频
            if (tv3.isFocused()) {
                tv4.setBackgroundResource(R.drawable.bg_textview_recommend);
                tv4.requestFocus();
            } else {
                tv3.setBackgroundResource(R.drawable.bg_textview_recommend);
                tv3.setTextColor(getResources().getColor(R.color.white));
                tv4.setBackgroundColor(getResources().getColor(R.color.text_still_color));
                tv4.setTextColor(getResources().getColor(R.color.color_2aa6b6));
            }
        }
        play(curPlayIndex);
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


    /**
     * headerView初始化数据
     */
    private void initData() {
        View rootView = headerView;
        if (banner != null && banner.size() > 0) {
            final TextView tv1, tv2, tv3, tv4, tvChange;
            tv1 = rootView.findViewById(R.id.tv_recommend_1);
            tv2 = rootView.findViewById(R.id.tv_recommend_2);
            tv3 = rootView.findViewById(R.id.tv_recommend_3);
            tv4 = rootView.findViewById(R.id.tv_recommend_4);
            tvChange = rootView.findViewById(R.id.tv_get_new);

            tv1.setText(banner.get(0).title);
            tv2.setText(banner.get(1).title);
            tv3.setText(banner.get(2).title);
            tv4.setText(banner.get(3).title);

            tv1.setOnClickListener(RecommendFragment.this);
            tv2.setOnClickListener(RecommendFragment.this);
            tv3.setOnClickListener(RecommendFragment.this);
            tv4.setOnClickListener(RecommendFragment.this);
            tvChange.setOnClickListener(RecommendFragment.this);

            tv1.setOnFocusChangeListener(RecommendFragment.this);
            tv2.setOnFocusChangeListener(RecommendFragment.this);
            tv3.setOnFocusChangeListener(RecommendFragment.this);
            tv4.setOnFocusChangeListener(RecommendFragment.this);
            tvChange.setOnFocusChangeListener(RecommendFragment.this);

            setOnKeyListener(tv1);
            setOnKeyListener(tv2);
            setOnKeyListener(tv3);
            tv4.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            AnimationUtil.leftRightShake(v);
                            return true;
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            v.setBackgroundColor(getResources().getColor(R.color.text_still_color));
                            ((TextView) v).setTextColor(getResources().getColor(R.color.color_2aa6b6));
                            ImageView ivPoster = headerView.findViewById(R.id.iv_poster);
                            ivPoster.setNextFocusRightId(R.id.tv_recommend_4);

                            VideoView videoView = headerView.findViewById(R.id.videoView_recommend);
                            videoView.setNextFocusRightId(R.id.tv_recommend_4);
                        }
                    }
                    return false;
                }
            });

        }
        final ImageView ivPoster = rootView.findViewById(R.id.iv_poster);
        final VideoView videoView = rootView.findViewById(R.id.videoView_recommend);
        ivPoster.setNextFocusRightId(R.id.tv_recommend_1);
        videoView.setNextFocusRightId(R.id.tv_recommend_1);
        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showToast(getActivity(), "点击了");
            }
        });
        ivPoster.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    setSelectedPosition(1, true);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    play = false;
                }
                return false;
            }
        });
        videoView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    setSelectedPosition(1, true);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    play = false;
                }
                return false;
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        LoginUserInfo loginUserInfo = BaseApplication.getLoginUserInfo();
        View rlUnlogin = rootView.findViewById(R.id.rl_unlogin);
        View rlLogined = rootView.findViewById(R.id.rl_logined);
        if (loginUserInfo != null) {
            rlLogined.setVisibility(View.VISIBLE);
            rlUnlogin.setVisibility(View.GONE);
            TextView tvUsername = rootView.findViewById(R.id.tv_username);
            RoundedImageView ivHead = rootView.findViewById(R.id.iv_head);
            TextView tvBuy = rootView.findViewById(R.id.tv_buy);
            Glide.with(getActivity()).load(loginUserInfo.portrait).into(ivHead);
            tvUsername.setText(loginUserInfo.nickname);
            tvBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BuyMemberActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
            tvBuy.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        return false;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        setSelectedPosition(1, true);
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        return true;
                    }
                    return false;
                }
            });
        } else {
            rlLogined.setVisibility(View.GONE);
            rlUnlogin.setVisibility(View.VISIBLE);
            final TextView tvVIP = rootView.findViewById(R.id.tv_vip);
            TextView tvLogin = rootView.findViewById(R.id.tv_login);
            tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UnLoginActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
            tvLogin.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        return false;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        return true;
                    }
                    return false;
                }
            });
            tvVIP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BuyMemberActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
            tvVIP.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        return false;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        setSelectedPosition(1, true);
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        return true;
                    }
                    return false;
                }
            });
        }
        playFirstVideo();
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_jingxuan_header, parent, false);
            ViewGroup.LayoutParams layoutParams = headerView.getLayoutParams();
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.y562);
            layoutParams.width = getResources().getDimensionPixelSize(R.dimen.x1920);
            headerView.setLayoutParams(layoutParams);
            headerView.setFocusable(false);
            headerView.setFocusableInTouchMode(false);
            return new ViewHolder(headerView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            initData();
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

    private void setOnKeyListener(final View view) {
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        AnimationUtil.leftRightShake(v);
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        v.setBackgroundColor(getResources().getColor(R.color.text_still_color));
                        ((TextView) v).setTextColor(getResources().getColor(R.color.color_2aa6b6));
                        ImageView ivPoster = headerView.findViewById(R.id.iv_poster);
                        ivPoster.setNextFocusRightId(view.getId());
                        VideoView videoView = headerView.findViewById(R.id.videoView_recommend);
                        videoView.setNextFocusRightId(view.getId());
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        play = true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK && 1 != requestCode) {
            return;
        }
    }

    /**
     * 精选换一批推荐
     */
    private void getAnotherBatch() {
        OpenApiDataServiceBase service = null;
        if (BaseApplication.getLoginUserInfo() != null) {
            service = new AnotherBatchLoginedService(getActivity());
        } else {
            service = new AnotherBatchUnLoginService(getActivity());
        }
        service.setCallback(new IOpenApiDataServiceCallback<AnotherBatchResponse>() {
            @Override
            public void onGetData(AnotherBatchResponse data) {
                banner.clear();
                banner.addAll(data.data.banner);
                if (banner != null && banner.size() > 0) {
                    initData();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getActivity(), errorMsg);
            }
        });
        if (BaseApplication.getLoginUserInfo() != null) {
            service.postAES("", false);
        } else {
            service.post("", false);
        }
    }
}
