package com.iask.yiyuanlegou1.home.timing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.respose.product.ProductBean;

import java.util.List;

public class TimingAdapter extends BaseAdapter {
    private List<ProductBean> data;
    private Context context;
    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());

    public TimingAdapter(List<ProductBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public boolean isEnabled(int position) {
//        if (position == 0) {
//            return false;
//        }
        return true;
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
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ProductBean bean = data.get(position);
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listview_soon_publish_item, parent, false);
            holder.tvProduct = (TextView) convertView.findViewById(R.id.goods_name);
            holder.tvWinnerNick = (TextView) convertView
                    .findViewById(R.id.nickName);
            holder.tvJoinNum = (TextView) convertView
                    .findViewById(R.id.parCount);
            holder.tvLuckeyNum = (TextView) convertView
                    .findViewById(R.id.lcode_num);
            holder.ivProduct = (ImageView) convertView
                    .findViewById(R.id.pic_url_view);
            holder.tvCountDown = (TextView) convertView
                    .findViewById(R.id.count_down_time);
            holder.tvAnounceTime = (TextView) convertView
                    .findViewById(R.id.complete_time);
            holder.countdownView = (LinearLayout) convertView
                    .findViewById(R.id.countdown_view);
            holder.winnerView = (LinearLayout) convertView
                    .findViewById(R.id.winner_view);
            holder.tvNper = (TextView) convertView.findViewById(R.id.tv_nper);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvProduct.setText(bean.getTitle());
        holder.tvNper.setText("第" + bean.getNper() + "期");
        Glide.with(context).load(bean.getCover()).into(holder.ivProduct);
        holder.countdownView.setVisibility(View.GONE);
        holder.winnerView.setVisibility(View.VISIBLE);
        holder.tvAnounceTime.setText(bean.getAnnounceTime());
        holder.tvWinnerNick.setText(bean.getUserName());
        holder.tvLuckeyNum.setText(bean.getCode());
        holder.tvJoinNum.setText(bean.getPartakeTimes() + "");

        return convertView;
    }

    class ViewHolder {
        TextView tvProduct;
        TextView tvWinnerNick;
        TextView tvJoinNum;
        TextView tvLuckeyNum;
        ImageView ivProduct;
        TextView tvCountDown;
        TextView tvAnounceTime;
        TextView tvNper;
        LinearLayout countdownView;
        LinearLayout winnerView;
    }
}