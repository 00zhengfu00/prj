package com.iask.yiyuanlegou1.home.person.reward;

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
import com.iask.yiyuanlegou1.network.respose.product.NotObtainedItem;
import com.iask.yiyuanlegou1.network.respose.product.NotObtainedItemResponse;
import com.iask.yiyuanlegou1.network.respose.product.ObtainedItem;
import com.iask.yiyuanlegou1.network.respose.product.ObtainedItemResponse;
import com.iask.yiyuanlegou1.network.service.product.NotObtainedItemService;
import com.iask.yiyuanlegou1.network.service.product.ObtainedItemService;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class UnRewardFragment extends BaseFragment {
    private PtrClassicFrameLayout mPtrFrame;
    private ListView listView;
    private View footer;
    private List<NotObtainedItem> notObtainedItems;
    private UnRewardAdapter adapter;
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
        notObtainedItems = new ArrayList<NotObtainedItem>();
        adapter = new UnRewardAdapter(notObtainedItems, getActivity());
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

    private void getData(int itemId) {
        NotObtainedItemService service = new NotObtainedItemService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<NotObtainedItemResponse>() {
            @Override
            public void onGetData(NotObtainedItemResponse data) {
                try {
                    if (id == 0) {
                        notObtainedItems.clear();
                        setFooterStatus(false);
                    }
                    if (data.data.notObtainedItem.size() > 0) {
                        notObtainedItems.addAll(data.data.notObtainedItem);
                        id = notObtainedItems.get(notObtainedItems.size() - 1).getItemId();
                    } else {
                        setFooterStatus(true);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPtrFrame.refreshComplete();
                            refreshUI();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, final String errorMsg, Throwable error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        service.post("itemId=" + id, false);
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
