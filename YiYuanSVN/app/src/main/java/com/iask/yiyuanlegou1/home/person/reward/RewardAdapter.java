package com.iask.yiyuanlegou1.home.person.reward;

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
import com.iask.yiyuanlegou1.network.respose.product.ObtainedItem;

import java.util.List;

public class RewardAdapter extends BaseAdapter {
    private List<ObtainedItem> obtainedItems;
    private Context context;

    public RewardAdapter(List<ObtainedItem> items, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_obtained_item,
                    parent, false);
            holder.ivProduct = (ImageView) convertView.findViewById(R.id.pic_url_view);
            holder.tvProductName = (TextView) convertView.findViewById(R.id.goods_name);
            holder.tvQihao = (TextView) convertView.findViewById(R.id.tv_nper);
            holder.tvState = (TextView) convertView.findViewById(R.id.state);
            holder.tvOrderNum = (TextView) convertView.findViewById(R.id.order_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ObtainedItem bean = obtainedItems.get(position);
        Glide.with(context).load(bean.getThumb()).into(holder.ivProduct);
        holder.tvState.setText(bean.getStatus());
        holder.tvQihao.setText(bean.getQishu() + "");
        holder.tvProductName.setText(bean.getTitle());
        holder.tvOrderNum.setText(bean.getCode());
        return convertView;
    }

    public class ViewHolder {
        TextView tvProductName;
        TextView tvQihao;
        TextView tvState;
        TextView tvOrderNum;
        ImageView ivProduct;
    }
}
