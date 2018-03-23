package com.physicmaster.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.physicmaster.R;

import java.util.List;

/**
 * Created by huashigen on 2017/5/12.
 */

public class PullToRefreshLayout<T> extends LinearLayout {

    private PullupLoadingListView mLvRecord;
    private SwipeRefreshLayout container;
    private View emptyView;

    private OnRefreshListener onRefreshListener;

    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    private void init() {
        LinearLayout viewParent = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout
                .layout_pull_to_refresh, this);
        mLvRecord = (PullupLoadingListView) viewParent.findViewById(R.id.lv_record);
        container = (SwipeRefreshLayout) viewParent.findViewById(R.id.container);
        emptyView = viewParent.findViewById(R.id.rl_empty);

        container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshListener != null) {
                    container.setRefreshing(true);
                    onRefreshListener.onRefresh();
                }
            }
        });
        mLvRecord.setOnPullupListener(new PullupLoadingListView.onPullupListener() {
            @Override
            public void onPullup(int maxId) {
                if (onRefreshListener != null) {
                    onRefreshListener.onPullup(maxId);
                }
            }
        });
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onPullup(int maxId);
    }

    public ListView getListView() {
        return mLvRecord;
    }

    public void notifyData(int nextPageMaxId, List<T> items, boolean isUpdate) {
        if (null == items || 0 == items.size()) {
            emptyView.setVisibility(VISIBLE);
            if (!isUpdate) {
                container.setVisibility(GONE);
            } else {
                mLvRecord.setFooterStatus(true);
                mLvRecord.setLoadFinished(true);
            }
        } else {
            container.setVisibility(VISIBLE);
            mLvRecord.notifyData(nextPageMaxId);
            if (-1 == nextPageMaxId) {
                mLvRecord.setLoadFinished(true);
            } else {
                mLvRecord.setLoadFinished(false);
            }
        }
        container.setRefreshing(false);
    }

    public void stopRefresh() {
        container.setRefreshing(false);
    }
}
