package com.physicmaster.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.physicmaster.R;

import java.util.List;

/**
 * Created by huashigen on 2017/5/12.
 */

public class PullupLoadingListView<T> extends ListView {

    private View footer;
    private int firstVisibleItemR;
    private int visibleItemCountR;
    private int nextMaxId;
    private boolean loadFinished = false;

    private onPullupListener onPullupListener;

    public PullupLoadingListView(Context context) {
        this(context, null);
    }

    public PullupLoadingListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullupLoadingListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnPullupListener(PullupLoadingListView.onPullupListener onPullupListener) {
        this.onPullupListener = onPullupListener;
    }

    //是否已加载完所有数据
    public void setLoadFinished(boolean end) {
        this.loadFinished = end;
        setFooterStatus(end);
    }

    private void init() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当滑动到底部时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && firstVisibleItemR != 0 && (firstVisibleItemR + visibleItemCountR) >=
                        getAdapter().getCount() && !loadFinished) {
                    if (onPullupListener != null) {
                        onPullupListener.onPullup(nextMaxId);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                firstVisibleItemR = firstVisibleItem;
                visibleItemCountR = visibleItemCount;
                if (footer != null) {
                    //判断可视Item是否能在当前页面完全显示
                    if (visibleItemCount == totalItemCount) {
                        // removeFooterView(footerView);
                        footer.setVisibility(View.GONE);//隐藏底部布局
                    } else {
                        // addFooterView(footerView);
                        footer.setVisibility(View.VISIBLE);//显示底部布局
                    }
                }
            }
        });
        footer = View.inflate(getContext(), R.layout.footer_view, null);
        addFooterView(footer);
    }

    public interface onPullupListener {
        void onPullup(int maxId);
    }

    //设置脚布局的显示状态
    public void setFooterState(int state) {
        footer.setVisibility(state);
    }

    public void setFooterStatus(boolean finished) {
        TextView tvNoMore = (TextView) footer.findViewById(R.id.tv_loading);
        ProgressBar bar = (ProgressBar) footer.findViewById(R.id.progress_loading);
        if (finished) {
            tvNoMore.setText("没有更多了");
            bar.setVisibility(View.GONE);
        } else {
            tvNoMore.setText("正在加载……");
            bar.setVisibility(View.VISIBLE);
        }
    }

    public void notifyData(int nextMaxId) {
        this.nextMaxId = nextMaxId;
    }
}
