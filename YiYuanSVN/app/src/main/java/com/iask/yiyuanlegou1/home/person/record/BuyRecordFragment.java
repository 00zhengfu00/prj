package com.iask.yiyuanlegou1.home.person.record;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseFragment;
import com.iask.yiyuanlegou1.home.main.product.ProductDetailActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.product.BuyRecordBean;
import com.iask.yiyuanlegou1.network.respose.product.BuyRecordResponse;
import com.iask.yiyuanlegou1.network.service.product.BuyRecordService;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class BuyRecordFragment extends BaseFragment {
    private PtrClassicFrameLayout mPtrFrame;
    private ListView listView;
    private View footer;
    private List<BuyRecordBean> buyRecordBeans;
    private BuyRecordAdapter adapter;
    private int type = 0;
    private long id = 0;
    /**
     * 记录第一行Item的数值
     */
    private int firstVisibleItemR;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_buy_record;
    }

    @Override
    protected void initView() {
        initPtrFrame();
        initListView();
        getData(type);
    }

    public void setType(int type) {
        this.type = type;
    }

    private void initPtrFrame() {
        mPtrFrame = (PtrClassicFrameLayout) rootView.findViewById(R.id.refresh_view_frame);
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
        listView = (ListView) rootView.findViewById(R.id.listView);
        footer = View.inflate(getActivity(), R.layout.footer_view, null);
        listView.addFooterView(footer);
        buyRecordBeans = new ArrayList<BuyRecordBean>();
        adapter = new BuyRecordAdapter(buyRecordBeans, getActivity());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当滑动到底部时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && firstVisibleItemR != 0) {
                    getData(type);
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
                intent.putExtra("id", (int) id);
                intent.setClass(getActivity(), ProductDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateData() {
        id = 0;
        getData(type);
    }

    private void getData(int type) {
        BuyRecordService service = new BuyRecordService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<BuyRecordResponse>() {
            @Override
            public void onGetData(BuyRecordResponse data) {
                mPtrFrame.refreshComplete();
                  try {
                    if (id == 0) {
                        buyRecordBeans.clear();
                        setFooterStatus(false);
                    }
                    if (data.data.recordList.size() > 0) {
                        buyRecordBeans.addAll(data.data.recordList);
                        id = buyRecordBeans.get(buyRecordBeans.size() - 1).getId();
                    } else {
                        setFooterStatus(true);
                    }
                    getActivity().runOnUiThread(new Runnable() {
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
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("id=" + id + "&type=" + type, false);
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
