package com.physicmaster.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.physicmaster.R;

/**
 * Created by huashigen on 2016/12/20.
 * 上拉加载封装
 */

public class PullUpToLoadListView extends ListView {
    private int firstVisibleItemR;
    private int visibleItemCountR;
    private View footer;
    private OnPullUpListener onPullUpListener;

    public PullUpToLoadListView(Context context) {
        this(context, null);
    }

    public PullUpToLoadListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullUpToLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public OnPullUpListener getOnPullUpListener() {
        return onPullUpListener;
    }

    public void setOnPullUpListener(OnPullUpListener onPullUpListener) {
        this.onPullUpListener = onPullUpListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }


    private void init() {
        footer = View.inflate(getContext(), R.layout.footer_view, null);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当滑动到底部时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && firstVisibleItemR != 0 && (firstVisibleItemR + visibleItemCountR) >=
                        getCount()) {
                    if (onPullUpListener != null) {
                        onPullUpListener.onPullUp();
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
    }

    public interface OnPullUpListener {
        void onPullUp();
    }
}
