package com.iask.yiyuanlegou1.home.main.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.product.AnnounceHistoryBean;
import com.iask.yiyuanlegou1.network.respose.product.AnnounceHistoryReponse;
import com.iask.yiyuanlegou1.network.service.product.AnnounceHistoryService;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class HistoryRecordActivity extends BaseActivity {

    private TitleBarView title;
    private PtrClassicFrameLayout mPtrFrame;
    private ListView listView;
    private List<AnnounceHistoryBean> announceHistoryBeans;
    private int itemId = 0;
    private int sid;
    private View footer;
    private HistoryRecordAdapter adapter;
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
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryRecordActivity.this.finish();
            }
        });
        title.setTitleText(R.string.history_record);

        sid = getIntent().getIntExtra("sid", 0);

        initPtrFrame();
        initListView();

        getData();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_history_record;
    }

    private void initPtrFrame() {
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.refresh_view_frame);
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


    /**
     * listView 相关设置
     */
    @SuppressLint("NewApi")
    private void initListView() {
        listView = (ListView) findViewById(R.id.listView);
        footer = View.inflate(this, R.layout.footer_view, null);
        listView.addFooterView(footer);
        announceHistoryBeans = new ArrayList<AnnounceHistoryBean>();
        adapter = new HistoryRecordAdapter(announceHistoryBeans, this);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当滑动到底部时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && firstVisibleItemR != 0) {
                    getData();
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
                Intent intent = new Intent();
                intent.setClass(HistoryRecordActivity.this, ProductDetailActivity.class);
                intent.putExtra("id",(int)id);
                startActivity(intent);
            }
        });
    }

    private void updateData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.refreshComplete();
            }
        }, 2000);
    }

    private void getData() {
        AnnounceHistoryService service = new AnnounceHistoryService(this);
        service.setCallback(new IOpenApiDataServiceCallback<AnnounceHistoryReponse>() {
            @Override
            public void onGetData(AnnounceHistoryReponse data) {
                mPtrFrame.refreshComplete();
                try {
                    if (itemId == 0) {
                        announceHistoryBeans.clear();
                        setFooterStatus(false);
                    }
                    if (data.data.shopList != null && data.data.shopList.size() > 0) {
                        announceHistoryBeans.addAll(data.data.shopList);
                        itemId = announceHistoryBeans.get(announceHistoryBeans.size() - 1).getId();
                    } else {
                        setFooterStatus(true);
                    }
                    HistoryRecordActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshUI();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                mPtrFrame.refreshComplete();
                Toast.makeText(HistoryRecordActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("sid=" + sid + "&itemId=" + itemId, false);
    }

    private void refreshUI() {
        adapter.notifyDataSetChanged();
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
