package com.physicmaster.modules.study.activity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.game.GoldRecrodResponse;
import com.physicmaster.net.service.game.GoldRecordService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.PullToRefreshLayout;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends BaseActivity {

    private static final String TAG = "RecordActivity";
    private List<GoldRecrodResponse.DataBean.TransactionListBean> mRecordList;
    private RecordAdapter recordAdapter;
    private PullToRefreshLayout pullToRefreshLayout;

    private Logger logger = AndroidLogger.getLogger();
    @Override
    protected void findViewById() {
        initTitle();
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showGoldRecord();
            }

            @Override
            public void onPullup(int maxId) {
                updateGoldRecord(maxId);
            }
        });
        mRecordList = new ArrayList<>();
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this)
                .setLeftImageRes(R.mipmap.fanhui)
                .setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("金币记录");
    }

    @Override
    protected void initView() {
        showGoldRecord();
    }

    private void updateGoldRecord(int maxId) {
        final GoldRecordService service = new GoldRecordService(RecordActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GoldRecrodResponse>() {
            @Override
            public void onGetData(GoldRecrodResponse data) {
                mRecordList.addAll(data.data.transactionList);
                pullToRefreshLayout.notifyData(data.data.nextPageMaxId, data.data
                        .transactionList, true);
                recordAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                logger.debug("获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(RecordActivity.this, errorMsg);
            }
        });
        service.postLogined("maxId=" + maxId, false);
    }

    private void showGoldRecord() {
        GoldRecordService service = new GoldRecordService(RecordActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GoldRecrodResponse>() {
            @Override
            public void onGetData(GoldRecrodResponse data) {
                logger.debug("获取成功：onGetData: " + data.msg);
                mRecordList.clear();
                mRecordList.addAll(data.data.transactionList);
                recordAdapter = new RecordAdapter();
                pullToRefreshLayout.getListView().setAdapter(recordAdapter);
                pullToRefreshLayout.notifyData(data.data.nextPageMaxId, data.data
                        .transactionList, false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                logger.debug("获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(RecordActivity.this, errorMsg);
            }
        });
        service.postLogined("", false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_record;
    }

    class RecordAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mRecordList.size();
        }

        @Override
        public GoldRecrodResponse.DataBean.TransactionListBean getItem(int position) {
            return mRecordList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(RecordActivity.this,
                        R.layout.list_item_record, null);
                holder = new ViewHolder();
                holder.tvRecord = (TextView) convertView
                        .findViewById(R.id.tv_record);
                holder.ivGold = (ImageView) convertView
                        .findViewById(R.id.iv_gold);
                holder.tvTime = (TextView) convertView
                        .findViewById(R.id.tv_time);
                holder.tvGold = (TextView) convertView
                        .findViewById(R.id.tv_gold);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            GoldRecrodResponse.DataBean.TransactionListBean item = getItem(position);
            holder.tvRecord.setText(item.description + "");
            holder.tvGold.setText("金币" + item.coinValue);
            holder.tvTime.setText(item.createTime + "");
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvRecord;
        ImageView ivGold;
        TextView tvTime;
        TextView tvGold;
    }
}
