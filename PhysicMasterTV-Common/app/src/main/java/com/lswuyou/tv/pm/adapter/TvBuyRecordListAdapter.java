package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.account.GetUserAccountResponse.DataBean.HistoryOrdersBean;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;

import static android.R.attr.order;

public class TvBuyRecordListAdapter extends TvBaseAdapter {
    private List<HistoryOrdersBean> orders;
    private LayoutInflater inflater;

    public TvBuyRecordListAdapter(Context context, List<HistoryOrdersBean> appList) {
        this.inflater = LayoutInflater.from(context);
        this.orders = appList;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder = null;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.item_record_list, null);
            holder = new ViewHolder();
            holder.tvOrderNum = (TextView) contentView.findViewById(R.id.tv_order_num);
            holder.tvCourseName = (TextView) contentView.findViewById(R.id.tv_course_name);
            holder.tvPrice = (TextView) contentView.findViewById(R.id.tv_price);
            holder.tvBuyTime = (TextView) contentView.findViewById(R.id.tv_buy_time);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        HistoryOrdersBean order = orders.get(position);
        holder.tvBuyTime.setText(order.buyTime);
        holder.tvCourseName.setText(order.title);
        holder.tvOrderNum.setText(order.orderId);
        holder.tvPrice.setText(order.priceYuan);
        return contentView;
    }

    public void addItem(HistoryOrdersBean item) {
        orders.add(item);
    }

    public void clear() {
        orders.clear();
    }

    public void flush(List<HistoryOrdersBean> appListNew) {
        orders = appListNew;
    }


    static class ViewHolder {
        TextView tvOrderNum;
        TextView tvCourseName;
        TextView tvPrice;
        TextView tvBuyTime;
        TextView tvPayType;
        TextView tvState;
    }
}
