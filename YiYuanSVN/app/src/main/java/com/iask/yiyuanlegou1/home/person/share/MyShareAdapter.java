package com.iask.yiyuanlegou1.home.person.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.network.respose.product.BuyRecordBean;
import com.iask.yiyuanlegou1.network.respose.product.MyShareBean;

import java.util.List;

public class MyShareAdapter extends BaseAdapter {
    private List<MyShareBean> buyRecordBeans;
    private Context context;

    public MyShareAdapter(List<MyShareBean> orders, Context context) {
        this.buyRecordBeans = orders;
        this.context = context;
    }

    @Override
    public int getCount() {
        return buyRecordBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return buyRecordBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return buyRecordBeans.get(position).getSdId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_my_share_item,
                    parent, false);
            holder.ivProduct = (ImageView) convertView.findViewById(R.id.pic_url_view);
            holder.tvProductName = (TextView) convertView.findViewById(R.id.goods_name);
            holder.tvQihao = (TextView) convertView.findViewById(R.id.tv_nper);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyShareBean bean = buyRecordBeans.get(position);
        Glide.with(context).load(bean.getPhoto()).into(holder.ivProduct);
        holder.tvProductName.setText(bean.getTitle());
        holder.tvQihao.setText(bean.getNper() + "");
        holder.tvContent.setText(bean.getContent());
        holder.tvTime.setText(bean.getTime());
        return convertView;
    }

    public class ViewHolder {
        TextView tvProductName;
        TextView tvQihao;
        TextView tvContent;
        TextView tvTime;
        ImageView ivProduct;
    }
}
