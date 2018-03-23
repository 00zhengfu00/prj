package com.iask.yiyuanlegou1.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.base.BaseFragmentActivity;
import com.iask.yiyuanlegou1.home.main.BuyFragment;
import com.iask.yiyuanlegou1.home.person.PersonFragment;
import com.iask.yiyuanlegou1.home.share.ShareFragment;
import com.iask.yiyuanlegou1.home.shopping.OrdersFragment;
import com.iask.yiyuanlegou1.home.shopping.OrdersFragment2;
import com.iask.yiyuanlegou1.home.timing.TimingFragment;
import com.iask.yiyuanlegou1.network.respose.product.CacheShoppingCarData;
import com.iask.yiyuanlegou1.network.respose.product.ShoppingCartBean;
import com.iask.yiyuanlegou1.utils.UIUtils;

import java.util.List;

public class HomeActivity extends BaseFragmentActivity {

    private FragmentTabHost mTabHost;
    private long lastTimeMillis = 0;
    private View item_main, item_classes, item_timing, item_shopping, item_person;
    private LocalBroadcastManager manager;
    public static final String PRODUCT_NUM_CHANGED = "product_num_changed";
    public static int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        item_main = LayoutInflater.from(this).inflate(R.layout.tab_item_view_main, null);
        item_classes = LayoutInflater.from(this).inflate(R.layout.tab_item_view_classes, null);
        item_timing = LayoutInflater.from(this).inflate(R.layout.tab_item_view_timing, null);
        item_shopping = LayoutInflater.from(this).inflate(R.layout.tab_item_view_shopping,
                null);
        item_person = LayoutInflater.from(this).inflate(R.layout.tab_item_view_person, null);
        mTabHost.addTab(mTabHost.newTabSpec("main").setIndicator(item_main), BuyFragment.class,
                null);
        mTabHost.addTab(mTabHost.newTabSpec("timing").setIndicator(item_timing), TimingFragment
                .class, null);
        mTabHost.addTab(mTabHost.newTabSpec("order").setIndicator(item_shopping), OrdersFragment2
                .class, null);
        mTabHost.addTab(mTabHost.newTabSpec("share").setIndicator(item_classes), ShareFragment
                .class, null);
        mTabHost.addTab(mTabHost.newTabSpec("person").setIndicator(item_person), PersonFragment
                .class, null);
        mTabHost.setBackgroundResource(R.color.white);

        manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(updateProductNumReceiver, new IntentFilter(PRODUCT_NUM_CHANGED));

        updateProductNum();

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("main")) {
                    index = 0;
                } else if (tabId.equals("timing")) {
                    index = 1;
                } else if (tabId.equals("order")) {
                    index = 2;
                } else if (tabId.equals("share")) {
                    index = 3;
                } else if (tabId.equals("person")) {
                    index = 4;
                }
            }
        });
//        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String tabId) {
//                switch (tabId) {
//                    case "main":
//                        index = 0;
//                        break;
//                    case "timing":
//                        index = 1;
//                        break;
//                    case "order":
//                        index = 2;
//                        break;
//                    case "share":
//                        index = 3;
//                        break;
//                    case "person":
//                        index = 4;
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        showFragment(index);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (updateProductNumReceiver != null) {
            manager.unregisterReceiver(updateProductNumReceiver);
        }
        super.onDestroy();
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void onBackPressed() {
        long currentTimeMillis = System.currentTimeMillis();
        if (lastTimeMillis != 0) {
            if (currentTimeMillis - lastTimeMillis < 3000) {
                BaseApplication.getInstance().saveCachedShoppingCarData();
                quitApplication();
            } else {
                lastTimeMillis = currentTimeMillis;
                UIUtils.showToast(this, R.string.repeat_click_exit);
            }
        } else {
            lastTimeMillis = currentTimeMillis;
            UIUtils.showToast(this, R.string.repeat_click_exit);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BaseApplication.getInstance().saveCachedShoppingCarData();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        BaseApplication.getInstance().saveCachedShoppingCarData();
    }

    public void showFragment(int index) {
        mTabHost.setCurrentTab(index);
    }

    private BroadcastReceiver updateProductNumReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateProductNum();
        }
    };

    private void updateProductNum() {
        CacheShoppingCarData cacheShoppingCarData;
        List<ShoppingCartBean> listBeas;
        Object list = BaseApplication.getInstance().getShoppingCarData();
        if (list != null) {
            cacheShoppingCarData = (CacheShoppingCarData) list;
            listBeas = cacheShoppingCarData.shoppingCartBeans;
            int count = 0;
            if (listBeas != null) {
                count = listBeas.size();
            }
            TextView tvOrderCount = (TextView) item_shopping.findViewById(R
                    .id.product_num);
            if (count > 0) {
                tvOrderCount.setVisibility(View.VISIBLE);
                tvOrderCount.setText(count + "");
            } else {
                tvOrderCount.setVisibility(View.GONE);
            }
        } else {
            TextView tvOrderCount = (TextView) item_shopping.findViewById(R
                    .id.product_num);
            tvOrderCount.setVisibility(View.GONE);
        }
    }
}
