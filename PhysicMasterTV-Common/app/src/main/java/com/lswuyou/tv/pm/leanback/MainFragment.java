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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.activity.ChapterDetailActivity;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.response.homepage.ChapterListResponse;
import com.lswuyou.tv.pm.net.response.homepage.ChapterListResponse.DataBean.BookListBean;
import com.lswuyou.tv.pm.net.service.GetChapterListLoginedService;
import com.lswuyou.tv.pm.net.service.GetChapterListUnLoginService;
import com.lswuyou.tv.pm.utils.UIUtils;

import java.util.List;

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    private static final int GRID_ITEM_WIDTH = 800;
    private static final int GRID_ITEM_HEIGHT = 300;

    private ArrayObjectAdapter mRowsAdapter;
    private int subjectId;
    public List<BookListBean> bookList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        setupUIElements();

        loadData();

        setupEventListeners();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        OpenApiDataServiceBase service = null;
        if (BaseApplication.getLoginUserInfo() != null) {
            service = new GetChapterListLoginedService(getActivity());
        } else {
            service = new GetChapterListUnLoginService(getActivity());
        }
        service.setCallback(new IOpenApiDataServiceCallback<ChapterListResponse>() {
            @Override
            public void onGetData(ChapterListResponse data) {
                bookList = data.data.bookList;
                ListRowPresenter listRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, false);
                listRowPresenter.setShadowEnabled(false);
                listRowPresenter.setKeepChildForeground(true);
                mRowsAdapter = new ArrayObjectAdapter(listRowPresenter);
                CardPresenter cardPresenter = new CardPresenter();
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
                    HeaderItem header = new HeaderItem(i, bookList.get(i).name);
                    ListRow listRow = new ListRow(header, listRowAdapter);
                    mRowsAdapter.add(listRow);
                }
                setAdapter(mRowsAdapter);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getActivity(), errorMsg);
            }
        });
        if (BaseApplication.getLoginUserInfo() != null) {
            service.postAES("subjectId=" + subjectId, false);
        } else {
            service.post("subjectId=" + subjectId, false);
        }
    }

    private void setupUIElements() {
        setHeadersState(HEADERS_DISABLED);
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
//        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            BookListBean.ItemListBean info = (BookListBean.ItemListBean) item;
            Intent intent = new Intent(getActivity(), ChapterDetailActivity.class);
            intent.putExtra("chapterId", info.chapterId);
            intent.putExtra("title", info.title + "");
            startActivity(intent);
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

}
