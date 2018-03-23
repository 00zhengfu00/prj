package com.iask.yiyuanlegou1.home.timing;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.base.BaseFragment;
import com.iask.yiyuanlegou1.home.main.product.ProductDetailActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.product.CacheShoppingCarData;
import com.iask.yiyuanlegou1.network.respose.product.LatestPublishResponse;
import com.iask.yiyuanlegou1.network.respose.product.ProductBean;
import com.iask.yiyuanlegou1.network.service.product.LastedPublishProductService;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class TimingFragment extends BaseFragment {

    private TitleBarView title;
    private PtrClassicFrameLayout mPtrFrame;
    private ListView listView;
    private List<ProductBean> productBeans;
    private List<ProductBean> timingProductBeans;
    private TimingAdapter adapter;
    private View footer;
    private LinearLayout layoutContainer;
    /**
     * 记录第一行Item的数值
     */
    private int firstVisibleItemR;
    private int visibleItemCountR;
    private int totalItemCountR;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_show;
    }

    @Override
    protected void initView() {
        title = (TitleBarView) rootView.findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setTitleText(R.string.timing);
        initPtrFrame();
        initListView();
    }

    @Override
    public void onResume() {
        Object list = BaseApplication.getInstance().getShoppingCarData();
        if (list != null && ((CacheShoppingCarData) list).shoppingCartBeans != null && (
                (CacheShoppingCarData) list).shoppingCartBeans.size() > 0) {
            CacheShoppingCarData cacheShoppingCarData = (CacheShoppingCarData) list;
            int size = cacheShoppingCarData.shoppingCartBeans.size();
        }
        getData(0, true);
        super.onResume();
    }

    private void initPtrFrame() {
        mPtrFrame = (PtrClassicFrameLayout) rootView.findViewById(R.id.refresh_root);
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

    private void getData(final int index, boolean showDialog) {
        LastedPublishProductService service = new LastedPublishProductService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<LatestPublishResponse>() {
            @Override
            public void onGetData(LatestPublishResponse data) {
                mPtrFrame.refreshComplete();
                try {
                    if (index == 0) {
                        classifyData(data.data.publishList);
                        setFooterStatus(false);
                    } else {
                        if (data.data.publishList == null || data.data.publishList.size() <= 0) {
                            setFooterStatus(true);
                        } else {
                            classifyDataAdd(data.data.publishList);
                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {
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
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                mPtrFrame.refreshComplete();
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("itemId=" + index, showDialog);
    }

    /**
     * 将未揭晓的数据分离出来，放在listview头部
     *
     * @param publishList
     */
    private void classifyData(List<ProductBean> publishList) {
        productBeans.clear();
        timingProductBeans.clear();
        if (layoutContainer != null) {
            listView.removeHeaderView(layoutContainer);
        }
        for (ProductBean bean : publishList) {
            if (bean.getCountDown() > 0) {
                timingProductBeans.add(bean);
            } else {
                productBeans.add(bean);
            }
        }
        layoutContainer = (LinearLayout) View.inflate(getActivity(), R.layout
                .listview_header_container, null);
        layoutContainer.setLayoutParams(new AbsListView.LayoutParams(AbsListView
                .LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        for (int i = 0; i < timingProductBeans.size(); i++) {
            final ProductBean bean = timingProductBeans.get(i);
            View convertView = View.inflate(getActivity(), R.layout.listview_soon_publish_item,
                    null);
            ViewHolder holder = new ViewHolder();
            holder.tvProduct = (TextView) convertView.findViewById(R.id.goods_name);
            holder.tvWinnerNick = (TextView) convertView
                    .findViewById(R.id.nickName);
            holder.tvJoinNum = (TextView) convertView
                    .findViewById(R.id.parCount);
            holder.tvLuckeyNum = (TextView) convertView
                    .findViewById(R.id.lcode_num);
            holder.ivProduct = (ImageView) convertView
                    .findViewById(R.id.pic_url_view);
            holder.tvCountDown = (TextView) convertView
                    .findViewById(R.id.count_down_time);
            holder.tvAnounceTime = (TextView) convertView
                    .findViewById(R.id.complete_time);
            holder.countdownView = (LinearLayout) convertView
                    .findViewById(R.id.countdown_view);
            holder.winnerView = (LinearLayout) convertView
                    .findViewById(R.id.winner_view);
            holder.tvNper = (TextView) convertView.findViewById(R.id.tv_nper);
            holder.tvProduct.setText(bean.getTitle());
            holder.tvNper.setText("第" + bean.getNper() + "期");
            Glide.with(getActivity()).load(bean.getCover()).into(holder.ivProduct);
            holder.countdownView.setVisibility(View.VISIBLE);
            holder.winnerView.setVisibility(View.GONE);
            countDown(holder, bean);
            layoutContainer.addView(convertView);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ProductDetailActivity.class);
                    intent.putExtra("id", (int) bean.getId());
                    startActivity(intent);
                }
            });
        }
        listView.addHeaderView(layoutContainer);
        adapter = new TimingAdapter(productBeans, getActivity());
        listView.setAdapter(adapter);
    }

    /**
     * 将未揭晓的数据分离出来，放在listview头部
     *
     * @param publishList
     */
    private void classifyDataAdd(List<ProductBean> publishList) {
        int startCount = timingProductBeans.size();
        for (ProductBean bean : publishList) {
            if (bean.getCountDown() > 0) {
                timingProductBeans.add(bean);
            } else {
                productBeans.add(bean);
            }
        }
        if (timingProductBeans.size() == startCount) {
            return;
        }
        if (layoutContainer != null) {
            listView.removeHeaderView(layoutContainer);
        }
        layoutContainer = (LinearLayout) View.inflate(getActivity(), R.layout
                .listview_header_container, null);
        layoutContainer.setLayoutParams(new AbsListView.LayoutParams(AbsListView
                .LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        for (int i = 0; i < timingProductBeans.size(); i++) {
            final ProductBean bean = timingProductBeans.get(i);
            View convertView = View.inflate(getActivity(), R.layout.listview_soon_publish_item,
                    null);
            ViewHolder holder = new ViewHolder();
            holder.tvProduct = (TextView) convertView.findViewById(R.id.goods_name);
            holder.tvWinnerNick = (TextView) convertView
                    .findViewById(R.id.nickName);
            holder.tvJoinNum = (TextView) convertView
                    .findViewById(R.id.parCount);
            holder.tvLuckeyNum = (TextView) convertView
                    .findViewById(R.id.lcode_num);
            holder.ivProduct = (ImageView) convertView
                    .findViewById(R.id.pic_url_view);
            holder.tvCountDown = (TextView) convertView
                    .findViewById(R.id.count_down_time);
            holder.tvAnounceTime = (TextView) convertView
                    .findViewById(R.id.complete_time);
            holder.countdownView = (LinearLayout) convertView
                    .findViewById(R.id.countdown_view);
            holder.winnerView = (LinearLayout) convertView
                    .findViewById(R.id.winner_view);
            holder.tvNper = (TextView) convertView.findViewById(R.id.tv_nper);
            holder.tvProduct.setText(bean.getTitle());
            holder.tvNper.setText("第" + bean.getNper() + "期");
            Glide.with(getActivity()).load(bean.getCover()).into(holder.ivProduct);
            holder.countdownView.setVisibility(View.VISIBLE);
            holder.winnerView.setVisibility(View.GONE);
            countDown(holder, bean);
            layoutContainer.addView(convertView);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ProductDetailActivity.class);
                    intent.putExtra("id", (int) bean.getId());
                    startActivity(intent);
                }
            });
        }
        listView.addHeaderView(layoutContainer);
        adapter = new TimingAdapter(productBeans, getActivity());
        listView.setAdapter(adapter);
    }

    private void initListView() {
        listView = (ListView) rootView.findViewById(R.id.listview_content);
        footer = View.inflate(getActivity(), R.layout.footer_view, null);
        listView.addFooterView(footer);
        productBeans = new ArrayList<ProductBean>();
        timingProductBeans = new ArrayList<ProductBean>();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当滑动到底部时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (firstVisibleItemR + visibleItemCountR) >= totalItemCountR) {
                    ProductBean bean = null;
                    if (productBeans.size() > 0) {
                        int size = productBeans.size();
                        bean = productBeans.get(size - 1);
                    } else {
                        int size = timingProductBeans.size();
                        bean = timingProductBeans.get(size - 1);
                    }
                    getData(bean.getId(), false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                firstVisibleItemR = firstVisibleItem;
                visibleItemCountR = visibleItemCount;
                totalItemCountR = totalItemCount;
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
                intent.setClass(getActivity(), ProductDetailActivity.class);
                intent.putExtra("id", (int) id);
                startActivity(intent);
            }
        });
    }

    /**
     * 更新数据
     */
    private void updateData() {
        getData(0, false);
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

    private void countDown(final ViewHolder holder, final ProductBean bean) {
        CountDownTimer timer = new CountDownTimer(bean.getCountDown(), 10) {
            @Override
            public void onTick(long l) {
                int totalSecond = (int) (l / 1000);
                int minute = totalSecond / 60;
                int second = totalSecond % 60;
                String secondStr = (second >= 10) ? second + "" : "0" + second;
                int millis = (int) (l % 1000);
                holder.tvCountDown.setText("0" + minute + ":" + secondStr + ":" + millis);
            }

            @Override
            public void onFinish() {
                holder.countdownView.setVisibility(View.GONE);
                holder.winnerView.setVisibility(View.VISIBLE);
                holder.tvAnounceTime.setText(bean.getAnnounceTime());
                holder.tvWinnerNick.setText(bean.getUserName());
                holder.tvLuckeyNum.setText(bean.getCode());
                holder.tvJoinNum.setText(bean.getPartakeTimes() + "");
            }
        };
        timer.start();
    }

    class ViewHolder {
        TextView tvProduct;
        TextView tvWinnerNick;
        TextView tvJoinNum;
        TextView tvLuckeyNum;
        ImageView ivProduct;
        TextView tvCountDown;
        TextView tvAnounceTime;
        TextView tvNper;
        LinearLayout countdownView;
        LinearLayout winnerView;
    }
}
