package com.iask.yiyuanlegou1.home.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.home.HomeActivity;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.respose.home.HomePageProductBean;
import com.iask.yiyuanlegou1.network.respose.product.CacheShoppingCarData;
import com.iask.yiyuanlegou1.network.respose.product.ShoppingCartBean;
import com.iask.yiyuanlegou1.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private List<HomePageProductBean> data;
    private Context context;
    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());

    public ProductAdapter(List<HomePageProductBean> data, Context context) {
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
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listview_goods_item1, parent, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.goods_name);
            holder.tvTotalNum = (TextView) convertView.findViewById(R.id.total_count);
            holder.tvCurNum = (TextView) convertView.findViewById(R.id.surplus_count);
            holder.ivTitle = (ImageView) convertView.findViewById(R.id.ItemImgSrc);
            holder.ivZone10 = (ImageView) convertView.findViewById(R.id.zone_10_tag);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.goodsProgress);
            holder.btnAdd = (Button) convertView.findViewById(R.id.add_to_car);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final HomePageProductBean bean = data.get(position);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvTotalNum.setText(bean.getZongrenshu() + "");
        holder.tvCurNum.setText(bean.getShenyurenshu() + "");
        Glide.with(context).load(bean.getThumb()).into(holder.ivTitle);
        holder.progressBar.setMax(bean.getZongrenshu());
        holder.progressBar.setProgress(bean.getCanyurenshu());
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cacheOrder(bean);
                UIUtils.showToast(context, "加入成功~");
                LocalBroadcastManager.getInstance(context).sendBroadcast(new
                        Intent(HomeActivity.PRODUCT_NUM_CHANGED));
            }
        });
        if (bean.getYunjiage().intValue() == 10) {
            holder.ivZone10.setVisibility(View.VISIBLE);
        } else {
            holder.ivZone10.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvTotalNum;
        TextView tvCurNum;
        ImageView ivTitle;
        ImageView ivZone10;
        ProgressBar progressBar;
        Button btnAdd;
    }

    /**
     * 缓存订单
     *
     * @param productDetailBean
     */
    private void cacheOrder(HomePageProductBean productDetailBean) {
        CacheShoppingCarData cacheShoppingCarData;
        List<ShoppingCartBean> listBeas;
        Object list = BaseApplication.getInstance().getShoppingCarData();
        if (list == null) {
            cacheShoppingCarData = new CacheShoppingCarData();
            listBeas = new ArrayList<ShoppingCartBean>();
            cacheShoppingCarData.shoppingCartBeans = listBeas;
        } else {
            cacheShoppingCarData = (CacheShoppingCarData) list;
            listBeas = cacheShoppingCarData.shoppingCartBeans;
        }
        //查询缓存中有没有这个商品，有的话就加一，没有就增加一个记录
        int index = findOrder(listBeas, productDetailBean.getId());
        ShoppingCartBean bean;
        if (index != -1) {
            bean = listBeas.get(index);
            int addCount = 1;
            if (bean.getYunjiage().intValue() == 1) {
                addCount = 10;
            }
            bean.setBuyCount(bean.getBuyCount() + addCount);
            if (bean.getBuyCount() > bean.getShenyurenshu()) {
                bean.setBuyCount(bean.getShenyurenshu());
            }
        } else {
            bean = new ShoppingCartBean();
            bean.setTitle(productDetailBean.getTitle());
            bean.setCanyurenshu(productDetailBean.getCanyurenshu());
            bean.setId(productDetailBean.getId());
            bean.setShenyurenshu(productDetailBean.getShenyurenshu());
            bean.setYunjiage(productDetailBean.getYunjiage());
            bean.setZongrenshu(productDetailBean.getZongrenshu());
            bean.setThumb(productDetailBean.getThumb());
            int addCount = 1;
            if (bean.getYunjiage().intValue() == 1) {
                addCount = 10;
            }
            bean.setBuyCount(addCount);
            if (bean.getBuyCount() > bean.getShenyurenshu()) {
                bean.setBuyCount(bean.getShenyurenshu());
            }
            listBeas.add(bean);
        }
//        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.SHOPPING_CAR);
//        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.SHOPPING_CAR,
//                cacheShoppingCarData);
        cacheShoppingCarData.shoppingCartBeans = listBeas;
        BaseApplication.getInstance().setShoppingCarData(cacheShoppingCarData);
    }

    /**
     * 看是否缓存了这个id的商品
     *
     * @param listBeas
     * @param id
     * @return
     */
    private int findOrder(List<ShoppingCartBean> listBeas, int id) {
        for (int i = 0; i < listBeas.size(); i++) {
            if (listBeas.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
