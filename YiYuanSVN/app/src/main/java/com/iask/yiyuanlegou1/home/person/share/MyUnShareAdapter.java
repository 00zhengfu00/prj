package com.iask.yiyuanlegou1.home.person.share;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.network.respose.product.MyShareBean;
import com.iask.yiyuanlegou1.network.respose.product.MyUnShareBean;

import java.util.List;

public class MyUnShareAdapter extends BaseAdapter {
    private List<MyUnShareBean> myUnShareBeans;
    private Context context;

    public MyUnShareAdapter(List<MyUnShareBean> data, Context context) {
        this.myUnShareBeans = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return myUnShareBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return myUnShareBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myUnShareBeans.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_my_unshare_item,
                    parent, false);
            holder.ivProduct = (ImageView) convertView.findViewById(R.id.pic_url_view);
            holder.tvProductName = (TextView) convertView.findViewById(R.id.goods_name);
            holder.tvQihao = (TextView) convertView.findViewById(R.id.tv_nper);
            holder.tvNumber = (TextView) convertView.findViewById(R.id.lcode_num);
            holder.btnShare = (Button) convertView.findViewById(R.id.btn_share);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MyUnShareBean bean = myUnShareBeans.get(position);
        Glide.with(context).load(bean.getThumb()).into(holder.ivProduct);
        holder.tvProductName.setText(bean.getTitle());
        holder.tvQihao.setText(bean.getQishu() + "");
        holder.tvNumber.setText(bean.getQ_user_code() + "");
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context,ShareOrderActivity.class);
                intent.putExtra("itemId", bean.getId());
                intent.putExtra("qishu",bean.getQishu());
                intent.putExtra("sid",bean.getSid());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView tvProductName;
        TextView tvQihao;
        TextView tvNumber;
        Button btnShare;
        ImageView ivProduct;
    }
}
