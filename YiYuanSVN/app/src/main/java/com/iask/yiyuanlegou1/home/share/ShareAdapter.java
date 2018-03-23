package com.iask.yiyuanlegou1.home.share;

import android.content.Context;
import android.content.Intent;
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
import com.iask.yiyuanlegou1.network.respose.product.ShareOrderBean;

import java.util.List;

public class ShareAdapter extends BaseAdapter {
    private List<ShareOrderBean> data;
    private Context context;
    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());

    public ShareAdapter(List<ShareOrderBean> data, Context context) {
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
        return data.get(position).getSdId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listview_order_share_item, parent, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.share_title);
            holder.tvNick = (TextView) convertView
                    .findViewById(R.id.nickName);
            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.share_content);
            holder.ivHead = (ImageView) convertView.findViewById(R.id.pic_url);
            holder.tvTime = (TextView) convertView.findViewById(R.id.past_time);
            holder.picScrollView = (LinearLayout) convertView.findViewById(R.id.pic_parent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ShareOrderBean bean = data.get(position);
        Glide.with(context).load(bean.getPhoto()).into(holder.ivHead);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvNick.setText(bean.getUserName());
        holder.tvContent.setText(bean.getContent());
        holder.tvTime.setText(bean.getTime());
        showPic(holder.picScrollView, bean.getImage());
//        holder.picScrollView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(context, ShareDetailActivity.class);
//                intent.putExtra("sdId", bean.getSdId());
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvNick;
        ImageView ivHead;
        TextView tvContent;
        TextView tvTime;
        LinearLayout picScrollView;
    }

    private void showPic(LinearLayout scrollView, List<String> urls) {
        ImageView iv1 = (ImageView) scrollView.findViewById(R.id.iv_share_1);
        ImageView iv2 = (ImageView) scrollView.findViewById(R.id.iv_share_2);
        ImageView iv3 = (ImageView) scrollView.findViewById(R.id.iv_share_3);
        Glide.with(context).load("").into(iv1);
        Glide.with(context).load("").into(iv2);
        Glide.with(context).load("").into(iv3);
        if (urls == null || urls.size() <= 0) {
            return;
        }
        if (urls.size() >= 1) {
            Glide.with(context).load(urls.get(0)).into(iv1);
        }
        if (urls.size() >= 2) {
            Glide.with(context).load(urls.get(1)).into(iv2);
        }
        if (urls.size() >= 3) {
            Glide.with(context).load(urls.get(2)).into(iv3);
        }
    }
}
