package com.lswuyou.tv.pm.activity;

import android.view.View;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.adapter.TvBuyRecordListAdapter;
import com.lswuyou.tv.pm.net.response.account.GetUserAccountResponse;
import com.lswuyou.tv.pm.net.response.account.GetUserAccountResponse.DataBean.HistoryOrdersBean;
import com.lswuyou.tv.pm.view.TitleBarView;

import java.util.List;

import reco.frame.tv.view.TvListView;

public class BuyRecordActivity extends BaseActivity {
    private TitleBarView mTitleBarView;
    private TvListView tlv_list;
    private TvBuyRecordListAdapter adapter;
    private List<HistoryOrdersBean> buyRecords;

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
        tlv_list = (TvListView) findViewById(R.id.record_list);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(0, R.string.buyRecord);
        mTitleBarView.setBtnRight(0, R.string.buyRecord);
        GetUserAccountResponse.DataBean userAccountInfo = (GetUserAccountResponse.DataBean) getIntent().getSerializableExtra("buyRecords");
        if (userAccountInfo == null) {
            return;
        }
        buyRecords = userAccountInfo.historyOrders;
        load();
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_buy_record;
    }

    private void load() {
        if (buyRecords == null || buyRecords.size() == 0) {
            return;
        }
        adapter = new TvBuyRecordListAdapter(this, buyRecords);
        tlv_list.setAdapter(adapter);

        tlv_list.setOnItemSelectListener(new TvListView.OnItemSelectListener() {

            @Override
            public void onItemSelect(View item, int position) {
            }

            @Override
            public void onItemDisSelect(View item, int position) {

            }
        });

        tlv_list.setOnItemClickListener(new TvListView.OnItemClickListener() {

            @Override
            public void onItemClick(View item, int position) {

            }
        });

    }
}
