package com.iask.yiyuanlegou1.home.main.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.network.respose.product.CalculateBean;

import java.util.List;

public class CalculateDetailAdapter extends BaseAdapter {
    private List<CalculateBean> calBeans;
    private Context context;

    public CalculateDetailAdapter(List<CalculateBean> calBeans, Context context) {
        this.calBeans = calBeans;
        this.context = context;
    }

    @Override
    public int getCount() {
        return calBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return calBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout
                    .calculate_detail_item_layout, parent, false);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tvTimeDivisor = (TextView) convertView.findViewById(R.id.tv_divisor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTime.setText(calBeans.get(position).partakeTime);
        holder.tvTimeDivisor.setText(calBeans.get(position).timeDivisor);
        holder.tvUserName.setText(calBeans.get(position).userName);
        return convertView;
    }

    public class ViewHolder {
        TextView tvTime;
        TextView tvTimeDivisor;
        TextView tvUserName;
    }

}
