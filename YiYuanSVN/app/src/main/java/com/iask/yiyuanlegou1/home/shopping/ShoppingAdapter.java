package com.iask.yiyuanlegou1.home.shopping;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.respose.product.ShoppingCartBean;

import java.util.List;

public class ShoppingAdapter extends BaseAdapter {
    private List<ShoppingCartBean> data;
    private Context context;
    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());
    private OrderController orderController;

    public ShoppingAdapter(List<ShoppingCartBean> data, Context context, OrderController
            orderController) {
        this.data = data;
        this.context = context;
        this.orderController = orderController;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        final ShoppingCartBean bean = data.get(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listview_orderlist_item, parent, false);
        }
        holder.tvGoodName = (TextView) convertView.findViewById(R.id.goods_name);
        holder.tvTotalCount = (TextView) convertView
                .findViewById(R.id.total_count);
        holder.tvSurplusCount = (TextView) convertView
                .findViewById(R.id.surplus_count);
        holder.ivTitle = (ImageView) convertView
                .findViewById(R.id.pic_url_view);
        holder.sub_btn = (ImageView) convertView
                .findViewById(R.id.sub_btn);
        holder.add_btn = (ImageView) convertView
                .findViewById(R.id.add_btn);
        holder.delete_btn = (ImageView) convertView
                .findViewById(R.id.buy_trailer);
        final ViewHolder finalHolder = holder;
        holder.my_par_count = (EditText) convertView.findViewById(R.id.my_par_count);
        holder.add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = bean.getBuyCount();
                System.out.println("count:" + count);
                count++;
                if (count > bean.getShenyurenshu()) {
                    count = bean.getShenyurenshu();
                }
                finalHolder.tvSurplusCount.setText(count + "");
                orderController.onCountAdd(position);
                notifyDataSetChanged();
            }
        });
        holder.sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = bean.getBuyCount();
                System.out.println("count:" + count);
                count--;
                if (count <= 0) {
                    count = 1;
                }
                finalHolder.tvSurplusCount.setText(count + "");
                orderController.onCountSub(position);
                notifyDataSetChanged();
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderController.onOrderDelete(position);
                notifyDataSetChanged();
            }
        });
//        holder.my_par_count.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                try {
//                    int buyCount = Integer.parseInt(s.toString());
//                    if (buyCount > bean.getShenyurenshu()) {
//                        buyCount = bean.getShenyurenshu();
//                        finalHolder.my_par_count.setText(buyCount + "");
//                    }
//                    orderController.onTextChange(position, buyCount);
//                    bean.setBuyCount(buyCount);
////                    notifyDataSetChanged();
//                } catch (NumberFormatException e) {
//                    Toast.makeText(context, "请输入数字！", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        holder.tvGoodName.setText(bean.getTitle());
        holder.tvTotalCount.setText("总需：" + bean.getZongrenshu() + "人次");
        holder.tvSurplusCount.setText(bean.getShenyurenshu() + "");
        holder.my_par_count.setText((int) (bean.getBuyCount()) + "");
        Glide.with(context).load(bean.getThumb()).into(holder.ivTitle);

        if (bean.getYunjiage().intValue() == 10) {
            convertView.findViewById(R.id.zone_10_tag).setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvGoodName;
        TextView tvTotalCount;
        TextView tvSurplusCount;
        ImageView ivTitle;
        ImageView sub_btn;
        ImageView add_btn;
        ImageView delete_btn;
        ImageView zone_10_tag;
        EditText my_par_count;
    }

    public interface OrderController {
        public void onCountSub(int position);

        public void onCountAdd(int position);

        public void onOrderDelete(int position);

        public void onTextChange(int position, int count);
    }

    public void updataView(int posi, ListView listView) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        Log.e("visibleFirstPosi:", visibleFirstPosi + "");
        Log.e("visibleLastPosi:", visibleLastPosi + "");
        Log.e("position:", posi + "");
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
            View view = listView.getChildAt(posi);
            ViewHolder holder = (ViewHolder) view.getTag();
            EditText txt = holder.my_par_count;
            txt.setText(data.get(posi).getBuyCount() + "");
        }
    }
}
