package com.iask.yiyuanlegou1.home.main.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.respose.product.ProductClassifyListBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ProductClassifyListAdapter extends BaseAdapter {
    private List<ProductClassifyListBean> data;
    private Context context;
    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());

    public ProductClassifyListAdapter(List<ProductClassifyListBean> data, Context context) {
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
                    R.layout.listview_product_classify_item, parent, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_classify_name);
            holder.ivHead = (RoundedImageView) convertView
                    .findViewById(R.id.iv_classify_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProductClassifyListBean bean = data.get(position);
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        RoundedImageView ivHead;
    }
}
