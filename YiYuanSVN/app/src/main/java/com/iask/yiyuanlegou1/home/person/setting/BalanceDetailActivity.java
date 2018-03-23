package com.iask.yiyuanlegou1.home.person.setting;

import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.account.BalanceDetailResponse;
import com.iask.yiyuanlegou1.network.respose.account.UserInfo;
import com.iask.yiyuanlegou1.network.respose.pay.BalancePayResponse;
import com.iask.yiyuanlegou1.network.service.account.BalanceDetailBean;
import com.iask.yiyuanlegou1.network.service.account.BalanceDetailService;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class BalanceDetailActivity extends BaseActivity {
    private TitleBarView title;
    private PtrClassicFrameLayout mPtrFrame;
    private ListView listView;
    private List<BalanceDetailBean> balanceDetailBeans;
    private BalanceDetailAdapter adapter;
    private View footer;
    private String timeStamp = "";
    /**
     * 记录第一行Item的数值
     */
    private int firstVisibleItemR;

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setTitleText(R.string.balance_detail);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BalanceDetailActivity.this.finish();
            }
        });

        initPtrFrame();
        initListView();

        getData("");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_balance_detail;
    }

    private void initPtrFrame() {
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.refresh_root);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                // mPtrFrame.autoRefresh();
            }
        }, 100);
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.listview_content);
        View header = View.inflate(this, R.layout.listview_balance_record_header, null);
        footer = View.inflate(this, R.layout.footer_view, null);
        listView.addHeaderView(header);
        listView.addFooterView(footer);
        balanceDetailBeans = new ArrayList<BalanceDetailBean>();
        adapter = new BalanceDetailAdapter(balanceDetailBeans, this);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当滑动到底部时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && firstVisibleItemR != 0) {
                    getData(timeStamp);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                firstVisibleItemR = firstVisibleItem;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    private void getData(final String time) {
        BalanceDetailService service = new BalanceDetailService(this);
        service.setCallback(new IOpenApiDataServiceCallback<BalanceDetailResponse>() {
            @Override
            public void onGetData(BalanceDetailResponse data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                });
                try {
                    if (TextUtils.isEmpty(time)) {
                        balanceDetailBeans.clear();
                        setFooterStatus(false);
                    }
                    if (data.data.accountList != null && data.data.accountList.size() > 0) {
                        balanceDetailBeans.addAll(data.data.accountList);
                        timeStamp = balanceDetailBeans.get(balanceDetailBeans.size() - 1)
                                .getTimeStamp();
                    } else {
                        setFooterStatus(true);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, final String errorMsg, Throwable error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                        Toast.makeText(BalanceDetailActivity.this, errorMsg, Toast.LENGTH_SHORT)
                                .show();
                    }
                });

            }
        });
        service.post("time=" + time, false);
    }

    private void updateData() {
        getData("");
    }

    private void setFooterStatus(boolean finished) {
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
}
