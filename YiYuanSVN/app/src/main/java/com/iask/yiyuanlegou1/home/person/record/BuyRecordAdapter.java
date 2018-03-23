package com.iask.yiyuanlegou1.home.person.record;

import android.content.Context;
import android.content.Intent;
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
import com.iask.yiyuanlegou1.home.main.product.ProductDetailActivity;
import com.iask.yiyuanlegou1.network.respose.product.BuyRecordBean;

import java.util.List;

public class BuyRecordAdapter extends BaseAdapter {
    private List<BuyRecordBean> buyRecordBeans;
    private Context context;

    public BuyRecordAdapter(List<BuyRecordBean> orders, Context context) {
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
        return buyRecordBeans.get(position).getItemId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_buy_record_item,
                    parent, false);
            holder.layoutIsDoing = (LinearLayout) convertView.findViewById(R.id.is_doing_status);
            holder.layoutPublishing = (LinearLayout) convertView.findViewById(R.id.publishing_item);
            holder.layoutPublished = (LinearLayout) convertView.findViewById(R.id.published_item);
            holder.ivProduct = (ImageView) convertView.findViewById(R.id.pic_url);
            holder.tvProductName = (TextView) convertView.findViewById(R.id.goods_name);
            holder.tvQihao = (TextView) convertView.findViewById(R.id.tv_qihao);
            holder.tvMyJoinCount = (TextView) convertView.findViewById(R.id.tv_join_num);
            holder.tvCheckDetail = (TextView) convertView.findViewById(R.id.tv_check_detail);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.goods_name);
//            holder.tvTotalCount = (TextView) convertView.findViewById(R.id.total_count);
//            holder.tvLeftCount = (TextView) convertView.findViewById(R.id.surplus_count);
//            holder.tvWinner = (TextView) convertView.findViewById(R.id.tv_winner);
//            holder.tvWinnerCount = (TextView) convertView.findViewById(R.id.tv_joinnum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BuyRecordBean bean = buyRecordBeans.get(position);
        Glide.with(context).load(bean.getCover()).into(holder.ivProduct);
        holder.tvMyJoinCount.setText(bean.getBuyTimes() + "");
        holder.tvTitle.setText(bean.getTitle());
        holder.tvQihao.setText("期数：" + bean.getNper() + "");

        holder.tvCheckDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        int status = bean.getStatus();
        if (status == 0) {
            holder.layoutIsDoing.setVisibility(View.VISIBLE);
            holder.layoutPublishing.setVisibility(View.GONE);
            holder.layoutPublished.setVisibility(View.GONE);
            TextView tvTotalCount = (TextView) holder.layoutIsDoing.findViewById(R.id.total_count);
            TextView tvSurplusCount = (TextView) holder.layoutIsDoing.findViewById(R.id
                    .surplus_count);
            tvTotalCount.setText(bean.getTotalNum() + "");
            tvSurplusCount.setText(bean.getPartakeNum() + "");
            ProgressBar progressBar = (ProgressBar) holder.layoutIsDoing.findViewById(R.id
                    .progress_rate);
            progressBar.setMax(bean.getTotalNum());
            progressBar.setProgress(bean.getPartakeNum());
//            final int id = bean.getId();
//            convertView.findViewById(R.id.append_buy).setOnClickListener(new View
//                    .OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.putExtra("id", id);
//                    intent.setClass(context, ProductDetailActivity.class);
//                    context.startActivity(intent);
//                }
//            });
        } else if (status == 1) {
            final int id = bean.getId();
            holder.layoutPublishing.setVisibility(View.VISIBLE);
            holder.layoutIsDoing.setVisibility(View.GONE);
            holder.layoutPublished.setVisibility(View.GONE);
//            convertView.findViewById(R.id.buy_again).setOnClickListener(new View
//                    .OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.putExtra("id", id);
//                    intent.setClass(context, ProductDetailActivity.class);
//                    context.startActivity(intent);
//                }
//            });
        } else if (status == 2) {
            holder.layoutPublished.setVisibility(View.VISIBLE);
            holder.layoutPublishing.setVisibility(View.GONE);
            holder.layoutIsDoing.setVisibility(View.GONE);
            TextView tvWinner = (TextView) holder.layoutPublished.findViewById(R.id.tv_winner);
            TextView tvJoinNum = (TextView) holder.layoutPublished.findViewById(R.id.tv_joinnum);

            tvWinner.setText(bean.getUserName());
            tvJoinNum.setText(bean.getPartakeTimes() + "");

//            final int newId = bean.getNewItemId();
//            convertView.findViewById(R.id.buy_again_2).setOnClickListener(new View
//                    .OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.putExtra("id", newId);
//                    intent.setClass(context, ProductDetailActivity.class);
//                    context.startActivity(intent);
//                }
//            });
        }
        return convertView;
    }

    public class ViewHolder {
        LinearLayout layoutIsDoing, layoutPublishing, layoutPublished;
        TextView tvProductName;
        TextView tvQihao;
        TextView tvMyJoinCount;
        TextView tvTitle;
        TextView tvCheckDetail;
        ImageView ivProduct;
    }
}
