package com.iask.yiyuanlegou1.home.person.reward;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.network.respose.product.NotObtainedItem;
import com.iask.yiyuanlegou1.network.respose.product.ObtainedItem;

import java.util.List;

public class UnRewardAdapter extends BaseAdapter {
    private List<NotObtainedItem> obtainedItems;
    private Context context;

    public UnRewardAdapter(List<NotObtainedItem> items, Context context) {
        this.obtainedItems = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return obtainedItems.size();
    }

    @Override
    public Object getItem(int position) {
        return obtainedItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return obtainedItems.get(position).getItemId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_not_obtained_item,
                    parent, false);
            holder.ivProduct = (ImageView) convertView.findViewById(R.id.pic_url_view);
            holder.tvProductName = (TextView) convertView.findViewById(R.id.goods_name);
            holder.tvQihao = (TextView) convertView.findViewById(R.id.tv_nper);
            holder.tvCode = (TextView) convertView.findViewById(R.id.code);
            holder.tvAnnounceTime = (TextView) convertView.findViewById(R.id.complete_time);
            holder.tvNickName = (TextView) convertView.findViewById(R.id.nickName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NotObtainedItem bean = obtainedItems.get(position);
        Glide.with(context).load(bean.getThumb()).into(holder.ivProduct);
//        holder.tvState.setText(bean.getStatus());
        holder.tvQihao.setText(bean.getQishu() + "");
        holder.tvProductName.setText(bean.getTitle());
        holder.tvAnnounceTime.setText(bean.getQ_end_time());
        holder.tvCode.setText(bean.getQ_user_code());
        holder.tvNickName.setText(bean.getUserName());
        return convertView;
    }

    public class ViewHolder {
        TextView tvProductName;
        TextView tvNickName;
        TextView tvQihao;
        TextView tvCode;
        TextView tvAnnounceTime;
        ImageView ivProduct;
    }
}
