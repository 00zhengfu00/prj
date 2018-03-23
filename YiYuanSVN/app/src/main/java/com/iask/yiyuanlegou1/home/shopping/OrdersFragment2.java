package com.iask.yiyuanlegou1.home.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.account.LoginActivity;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.base.BaseFragment;
import com.iask.yiyuanlegou1.home.HomeActivity;
import com.iask.yiyuanlegou1.home.main.product.ProductDetailActivity;
import com.iask.yiyuanlegou1.home.main.product.pay.PayListActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.home.HomePageProductBean;
import com.iask.yiyuanlegou1.network.respose.product.CacheShoppingCarData;
import com.iask.yiyuanlegou1.network.respose.product.ShoppingCartBean;
import com.iask.yiyuanlegou1.network.respose.product.ShoppingCartResponse;
import com.iask.yiyuanlegou1.network.service.product.ShoppingCartService;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class OrdersFragment2 extends Fragment {
    private TitleBarView title;
    private PtrClassicFrameLayout mPtrFrame;
    private LinearLayout listView;
    private List<ShoppingCartBean> listBeas;
    private CacheShoppingCarData cacheShoppingCarData;
    private int totalPrice = 0;
    private Map<String, View> orderListView;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order, null);
        Object list = BaseApplication.getInstance().getShoppingCarData();
        if (list != null && ((CacheShoppingCarData) list).shoppingCartBeans != null && (
                (CacheShoppingCarData) list).shoppingCartBeans.size() > 0) {
            CacheShoppingCarData cacheShoppingCarData = (CacheShoppingCarData) list;
            int size = cacheShoppingCarData.shoppingCartBeans.size();
        }
        title = (TitleBarView) rootView.findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setTitleText(R.string.order_list);
        initPtrFrame(rootView);
        initListView(rootView);
        return rootView;
    }

    private void initPtrFrame(View rootView) {
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

    private void initListView(View rootView) {
        listView = (LinearLayout) rootView.findViewById(R.id.listview_content);
        listBeas = new ArrayList<ShoppingCartBean>();
        Button btnSettle = (Button) rootView.findViewById(R.id.settle_btn);
        btnSettle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = BaseApplication.getInstance().getUserId();
                if (!TextUtils.isEmpty(userId)) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PayListActivity.class);
                    intent.putExtra("pNum", listBeas.size());
                    intent.putExtra("pPrice", totalPrice);
                    Bundle bundle = new Bundle();
                    BuyInfoWrapper wrapper = new BuyInfoWrapper();
                    wrapper.buyInfos = getBuyInfo();
                    bundle.putSerializable("buyInfo", wrapper);
                    intent.putExtra("buyInfo", bundle);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
    }

    /**
     * 获取产品支付相关信息
     *
     * @return
     */
    private BuyInfo[] getBuyInfo() {
        BuyInfo[] buyInfos = new BuyInfo[listBeas.size()];
        for (int i = 0; i < listBeas.size(); i++) {
            BuyInfo info = new BuyInfo();
            info.id = listBeas.get(i).getId();
            info.num = listBeas.get(i).getBuyCount();
            buyInfos[i] = info;
        }
        return buyInfos;
    }

    /**
     * 更新数据
     */
    private void updateData() {
        getFreshData();
    }

    /**
     * 从缓存中获取订单数据
     */
    private void getCacheData(final View rootView) {
        Object list = BaseApplication.getInstance().getShoppingCarData();
        if (list != null && ((CacheShoppingCarData) list).shoppingCartBeans != null && (
                (CacheShoppingCarData) list).shoppingCartBeans.size() > 0) {
            cacheShoppingCarData = (CacheShoppingCarData) list;
            int size = cacheShoppingCarData.shoppingCartBeans.size();
            listBeas = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                listBeas.add(cacheShoppingCarData.shoppingCartBeans.get(i));
            }
//            if (size > listBeas.size()) {
//                int start = listBeas.size();
//                for (int i = 0; i < size; i++) {
//                    listBeas.add(cacheShoppingCarData.shoppingCartBeans.get(i));
//                }
//            } else {
//                for (int i = 0; i < size; i++) {
//                    ShoppingCartBean bean1 = listBeas.get(i);
//                    ShoppingCartBean bean2 = cacheShoppingCarData.shoppingCartBeans.get(i);
//                    bean1.setBuyCount(bean2.getBuyCount());
//                }
//            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rootView.findViewById(R.id.not_empty_view).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.list_empty_tips).setVisibility(View.GONE);
                    rootView.findViewById(R.id.settle_view).setVisibility(View.VISIBLE);
                    getFreshData();
                }
            });
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rootView.findViewById(R.id.list_empty_tips).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.not_empty_view).setVisibility(View.GONE);
                    mPtrFrame.refreshComplete();
                }
            });
        }
        Button btnBuy = (Button) rootView.findViewById(R.id.empty_tips_btn);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).showFragment(0);
            }
        });
    }

    /**
     * 刷新UI
     */
    private void refreshUI(final View rootView) {
        int start = 0;
        listView.removeAllViews();
        orderListView = new HashMap<String, View>();
        int size = listBeas.size();
        for (int i = 0; i < size; i++) {
            final View view = View.inflate(getActivity(), R.layout.listview_orderlist_item,
                    null);
            TextView tvGoodName = (TextView) view.findViewById(R.id.goods_name);
            TextView tvTotalCount = (TextView) view
                    .findViewById(R.id.total_count);
            TextView tvSurplusCount = (TextView) view
                    .findViewById(R.id.surplus_count);
            ImageView ivTitle = (ImageView) view
                    .findViewById(R.id.pic_url_view);
            ImageView sub_btn = (ImageView) view
                    .findViewById(R.id.sub_btn);
            ImageView add_btn = (ImageView) view
                    .findViewById(R.id.add_btn);
            ImageView delete_btn = (ImageView) view
                    .findViewById(R.id.buy_trailer);
            final EditText my_par_count = (EditText) view.findViewById(R.id.my_par_count);
            final ShoppingCartBean bean = listBeas.get(i);
            tvGoodName.setText(bean.getTitle());
            tvTotalCount.setText("总需：" + bean.getZongrenshu() + "人次");
            tvSurplusCount.setText(bean.getShenyurenshu() + "");
            int myCount = bean.getBuyCount();
            if (myCount > bean.getShenyurenshu()) {
                bean.setBuyCount(bean.getShenyurenshu());
            }
            my_par_count.setText((int) (bean.getBuyCount()) + "");
            Glide.with(getActivity()).load(bean.getThumb()).into(ivTitle);
            if (bean.getYunjiage().intValue() == 10) {
                view.findViewById(R.id.zone_10_tag).setVisibility(View.VISIBLE);
            }
            sub_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int buyCount = bean.getBuyCount() - 1;
                    if (buyCount < 1) {
                        buyCount = 1;
                    }
                    bean.setBuyCount(buyCount);
                    my_par_count.setText(buyCount + "");
                    setFooter(rootView);
                    cacheShoppingCarData.shoppingCartBeans = listBeas;
                }
            });
            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int buyCount = bean.getBuyCount() + 1;
                    if (buyCount > bean.getShenyurenshu()) {
                        buyCount = bean.getShenyurenshu();
                    }
                    bean.setBuyCount(buyCount);
                    my_par_count.setText(buyCount + "");
                    setFooter(rootView);
                    cacheShoppingCarData.shoppingCartBeans = listBeas;
                }
            });
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderListView.remove(bean.getId() + bean.getThumb());
                    listView.removeView(view);
                    listBeas.remove(bean);
                    cacheShoppingCarData.shoppingCartBeans = listBeas;
                    setFooter(rootView);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent
                            (HomeActivity.PRODUCT_NUM_CHANGED));
                }
            });

            my_par_count.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        int buyCount = Integer.parseInt(s.toString());
                        if (buyCount > bean.getShenyurenshu()) {
                            buyCount = bean.getShenyurenshu();
                            my_par_count.setText(buyCount + "");
                        } else {
                            if (buyCount < 1) {
                                buyCount = 1;
                                my_par_count.setText(buyCount + "");
                            }
                        }
                        bean.setBuyCount(buyCount);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getActivity(), "请输入数字！", Toast.LENGTH_SHORT).show();
                    }
                    cacheShoppingCarData.shoppingCartBeans = listBeas;
                    setFooter(rootView);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            orderListView.put(bean.getId() + bean.getThumb(), view);
            listView.addView(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                    intent.putExtra("id", bean.getId());
                    startActivity(intent);
                }
            });
            setFooter(rootView);
        }
    }

    /**
     * 在返回的商品列表中去人订单列表中的商品是否还存在
     *
     * @param id
     * @param beanNs
     * @return
     */
    private HomePageProductBean getExistProduct(int id, List<HomePageProductBean> beanNs) {
        for (int i = 0; i < beanNs.size(); i++) {
            if (id == beanNs.get(i).getId() && beanNs.get(i).getShenyurenshu() > 0) {
                return beanNs.get(i);
            }
        }
        return null;
    }

    /**
     * 刷新购物车
     */
    private void getFreshData() {
        List<Integer> ids = new ArrayList<Integer>();
        for (ShoppingCartBean bean : listBeas) {
            ids.add(bean.getId());
        }
        ShoppingCartService service = new ShoppingCartService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<ShoppingCartResponse>() {
            @Override
            public void onGetData(ShoppingCartResponse data) {
                try {
                    List<HomePageProductBean> beanNs = data.data.shopList;
                    if (data.data.shopList == null || data.data.shopList.size() == 0) {
                        listBeas.clear();
                    } else {
                        for (int i = 0; i < listBeas.size(); i++) {
                            final ShoppingCartBean beanO = listBeas.get(i);
                            HomePageProductBean hBean = getExistProduct(beanO.getId(), beanNs);
                            if (hBean == null) {
                                listBeas.remove(i);
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        listView.removeView(orderListView.get(beanO.getId() +
// beanO
//                                                .getThumb()));
//                                        orderListView.remove(beanO.getId() + "");
//                                    }
//                                });
                                i--;
                                continue;
                            }
                            beanO.setZongrenshu(beanNs.get(i).getZongrenshu());
                            beanO.setShenyurenshu(beanNs.get(i).getShenyurenshu());
                            beanO.setCanyurenshu(beanNs.get(i).getCanyurenshu());
                            beanO.setYunjiage(beanNs.get(i).getYunjiage());
                            int leftCount = beanNs.get(i).getShenyurenshu();
                            if (beanO.getBuyCount() > leftCount) {
                                beanO.setBuyCount(leftCount);
                            }
                        }
                    }
                    cacheShoppingCarData.shoppingCartBeans = listBeas;
                    BaseApplication.getInstance().setShoppingCarData(cacheShoppingCarData);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPtrFrame.refreshComplete();
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new
                                    Intent(HomeActivity.PRODUCT_NUM_CHANGED));
                            refreshUI(rootView);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("itemId=" + JSON.toJSONString(ids), false);
    }

    @Override
    public void onStart() {
        Object list = BaseApplication.getInstance().getShoppingCarData();
        if (list != null && ((CacheShoppingCarData) list).shoppingCartBeans != null && (
                (CacheShoppingCarData) list).shoppingCartBeans.size() > 0) {
            CacheShoppingCarData cacheShoppingCarData = (CacheShoppingCarData) list;
            int size = cacheShoppingCarData.shoppingCartBeans.size();
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        Object list = BaseApplication.getInstance().getShoppingCarData();
        if (list != null && ((CacheShoppingCarData) list).shoppingCartBeans != null && (
                (CacheShoppingCarData) list).shoppingCartBeans.size() > 0) {
            CacheShoppingCarData cacheShoppingCarData = (CacheShoppingCarData) list;
            int size = cacheShoppingCarData.shoppingCartBeans.size();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCacheData(rootView);
            }
        }).start();
        super.onResume();
    }

    private void setFooter(View rootView) {
        TextView tvProCount = (TextView) rootView.findViewById(R.id.order_price_label);
        TextView tvOrderPrice = (TextView) rootView.findViewById(R.id.order_price);

        tvProCount.setText("共" + listBeas.size() + "件商品,总计：");
        totalPrice = 0;
        for (ShoppingCartBean bean : listBeas) {
            totalPrice += bean.getBuyCount() * bean.getYunjiage().intValue();
        }
        if (totalPrice == 0) {
            rootView.findViewById(R.id.list_empty_tips).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.not_empty_view).setVisibility(View.GONE);
            mPtrFrame.refreshComplete();
        }
        tvOrderPrice.setText(totalPrice + "元");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        BaseApplication.getInstance().setShoppingCarData(cacheShoppingCarData);
    }
}
