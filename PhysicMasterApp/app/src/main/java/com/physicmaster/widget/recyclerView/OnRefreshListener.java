package com.physicmaster.widget.recyclerView;

import android.view.ViewGroup;

/**
 * @ explain:
 * @ author：xujun on 2016-8-9 12:14
 * @ email：gdutxiaoxu@163.com
 */
public interface OnRefreshListener {

    void onRefresh(ViewGroup viewGroup);

    void onLoadMore(ViewGroup viewGroup);
}
