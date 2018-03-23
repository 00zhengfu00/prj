package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.member.MemberItem;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;

public class TvMemListAdapter extends TvBaseAdapter {
    private List<MemberItem> orders;
    private LayoutInflater inflater;

    public TvMemListAdapter(Context context, List<MemberItem> appList) {
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
            contentView = inflater.inflate(R.layout.list_mem_item, null);
            holder = new ViewHolder();
            holder.tvDeadLine = (TextView) contentView.findViewById(R.id.tv_deadline);
            holder.tvOrinPrice = (TextView) contentView.findViewById(R.id.tv_price_orin);
            holder.tvPrice = (TextView) contentView.findViewById(R.id.tv_price);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        MemberItem memberItem = orders.get(position);
        holder.tvDeadLine.setText(memberItem.deadLine);
        holder.tvOrinPrice.setText(memberItem.orinPrice);
        holder.tvOrinPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.tvPrice.setText(memberItem.price);
        return contentView;
    }

    public void addItem(MemberItem item) {
        orders.add(item);
    }

    public void clear() {
        orders.clear();
    }

    public void flush(List<MemberItem> appListNew) {
        orders = appListNew;
    }

    static class ViewHolder {
        TextView tvDeadLine;
        TextView tvPrice;
        TextView tvOrinPrice;
    }
}
