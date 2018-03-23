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

import android.content.Intent;
import android.graphics.Color;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.activity.VideoIntroActivity;
import com.lswuyou.tv.pm.activity.VideoIntroForExcActivity;
import com.lswuyou.tv.pm.adapter.TvGridAdapter;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.CommonResponse;
import com.lswuyou.tv.pm.net.response.account.GetFavListResponse;
import com.lswuyou.tv.pm.net.response.account.GetFavListResponse.DataBean.BookListBean;
import com.lswuyou.tv.pm.net.response.homepage.ChapterListResponse;
import com.lswuyou.tv.pm.net.service.CancelFavService;
import com.lswuyou.tv.pm.net.service.GetFavListService;
import com.lswuyou.tv.pm.utils.UIUtils;
import com.lswuyou.tv.pm.view.EditRecordDialog;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class CollectFragment extends BrowseFragment {
    private static final String TAG = "CollectFragment";

    private static final int GRID_ITEM_WIDTH = 800;
    private static final int GRID_ITEM_HEIGHT = 300;

    private ArrayObjectAdapter mRowsAdapter;
    private DisplayMetrics mMetrics;
    private int subjectId;
    public List<BookListBean> bookList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        setupUIElements();

        loadData();

        setupEventListeners();
    }

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
        GetFavListService service = new GetFavListService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<GetFavListResponse>() {
            @Override
            public void onGetData(GetFavListResponse data) {
                bookList = data.data.bookList;
                ListRowPresenter listRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, false);
                listRowPresenter.setShadowEnabled(false);
                listRowPresenter.setKeepChildForeground(true);
                mRowsAdapter = new ArrayObjectAdapter(listRowPresenter);
                FavCardPresenter cardPresenter = new FavCardPresenter();
                cardPresenter.setOnMenuClickListener(new TvGridAdapter.OnMenuClickListener() {
                    @Override
                    public void onMenuClick(final int videoId) {
                        final EditRecordDialog editRecordDialog = new EditRecordDialog();
                        editRecordDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (v.getId() == R.id.btn_delete) {
                                    cancelFav("[" + videoId + "]");
                                    mRowsAdapter.clear();
                                } else {
                                    mRowsAdapter.clear();
                                    List<Integer> videoIds = getAllFavVideoId();
                                    cancelFav(JSON.toJSONString(videoIds));
                                }
                                editRecordDialog.dismiss();
                            }
                        });
                        editRecordDialog.show(getFragmentManager(), "editRecord");
                    }
                });
                fillData(cardPresenter);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getActivity(), errorMsg);
            }
        });
        service.postAES("subjectId=" + subjectId, false);
    }

    /**
     * 获取所有视频
     *
     * @return
     */
    private List<Integer> getAllFavVideoId() {
        List<Integer> videoIds = new ArrayList<>();
        for (int i = 0; i < bookList.size(); i++) {
            List<BookListBean.ItemListBean> itemList = bookList.get(i).itemList;
            for (BookListBean.ItemListBean itemListBean : itemList) {
                videoIds.add(itemListBean.videoId);
            }
        }
        return videoIds;
    }

    /**
     * 填充数据
     *
     * @param cardPresenter
     */
    private void fillData(FavCardPresenter cardPresenter) {
        for (int i = 0; i < bookList.size(); i++) {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
            List<BookListBean.ItemListBean> itemList = bookList.get(i).itemList;
            for (int i1 = 0; i1 < itemList.size(); i1++) {
                //最后一排
                if (i == bookList.size() - 1) {
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
            if (listRowAdapter.size() > 0) {
                HeaderItem header = new HeaderItem(i, bookList.get(i).name);
                ListRow listRow = new ListRow(header, listRowAdapter);
                mRowsAdapter.add(listRow);
            }
        }
        setAdapter(mRowsAdapter);
    }

    private void prepareBackgroundManager() {
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
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

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
        }
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(getResources().getColor(R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

    /**
     * 取消视频收藏
     */
    private void cancelFav(String videoIds) {
        CancelFavService service = new CancelFavService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                loadData();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getActivity(), errorMsg);
            }
        });
        service.postAES("videoIds=" + videoIds, false);
    }
}
