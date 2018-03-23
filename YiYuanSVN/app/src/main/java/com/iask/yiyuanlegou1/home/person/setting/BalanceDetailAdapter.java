package com.iask.yiyuanlegou1.home.person.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.service.account.BalanceDetailBean;

import java.util.List;

public class BalanceDetailAdapter extends BaseAdapter {
    private List<BalanceDetailBean> data;
    private Context context;
    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());

    public BalanceDetailAdapter(List<BalanceDetailBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listview_balance_record, parent, false);
            holder.tvBalance = (TextView) convertView.findViewById(R.id.tv_balance);
            holder.tvAction = (TextView) convertView.findViewById(R.id.tv_action);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BalanceDetailBean bean = data.get(position);
        int money = bean.getMoney().intValue();
        if (money > 0) {
            holder.tvBalance.setTextColor(context.getResources().getColor(R.color.pink));
        } else {
            holder.tvBalance.setTextColor(context.getResources().getColor(R.color
                    .text_default_color));
        }
        holder.tvBalance.setText(money + "");
        holder.tvAction.setText(bean.getContent());
        holder.tvTime.setText(bean.getFmtTime());
        return convertView;
    }

    class ViewHolder {
        TextView tvBalance;
        TextView tvAction;
        TextView tvTime;
    }
}
