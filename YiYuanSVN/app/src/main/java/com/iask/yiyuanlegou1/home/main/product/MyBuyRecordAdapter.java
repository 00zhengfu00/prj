package com.iask.yiyuanlegou1.home.main.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.respose.account.MyBuyRecordBean;

import java.util.List;

public class MyBuyRecordAdapter extends BaseAdapter {
    private List<MyBuyRecordBean> data;
    private Context context;
    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());

    public MyBuyRecordAdapter(List<MyBuyRecordBean> data, Context context) {
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
                    R.layout.listview_my_buy_record_item, parent, false);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_record_time);
            holder.tvJoinNum = (TextView) convertView
                    .findViewById(R.id.tv_join_num);
            holder.layoutCodes = (LinearLayout) convertView
                    .findViewById(R.id.layout_codes);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyBuyRecordBean bean = data.get(position);
        holder.tvTime.setText(bean.getTime());
        holder.tvJoinNum.setText(bean.getNum()+"");
        showCodes(holder.layoutCodes, bean.getCode());
        return convertView;
    }

    class ViewHolder {
        TextView tvTime;
        TextView tvJoinNum;
        LinearLayout layoutCodes;
    }

    private void showCodes(LinearLayout layout, List<String> codes) {
        layout.removeAllViews();
        for (String code : codes) {
            TextView tvCode = new TextView(context);
            tvCode.setTextSize(14);
            tvCode.setTextColor(context.getResources().getColor(R.color.black));
            tvCode.setText(code);
            layout.addView(tvCode);
        }
    }
}
