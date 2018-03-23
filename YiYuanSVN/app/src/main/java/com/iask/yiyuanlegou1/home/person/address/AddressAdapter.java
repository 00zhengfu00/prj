/* 
 * 系统名称：lswuyou
 * 类  名  称：AreaAdapter.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-17 上午11:01:24
 * 功能说明： 
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.home.person.address;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.network.respose.account.AddressBean;

import java.util.List;

public class AddressAdapter extends BaseAdapter {
    private Context mContext;
    private List<AddressBean> list;

    public AddressAdapter(Context context, List<AddressBean> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_address_item,
                    parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tvMobile = (TextView) convertView.findViewById(R.id.tv_mobile);
            holder.tvDefault = (TextView) convertView.findViewById(R.id.tv_default_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AddressBean bean = list.get(position);
        holder.tvName.setText(bean.getShouhuoren());
        holder.tvAddress.setText(getString(bean.getSheng()) + getString(bean.getShi()) +
                getString(bean.getXian()) + getString(bean
                .getJiedao()));
        holder.tvMobile.setText(bean.getMobile());
        if (bean.getDefaul().equals("Y")) {
            holder.tvDefault.setVisibility(View.VISIBLE);
        } else {
            holder.tvDefault.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvAddress;
        TextView tvDefault;
        TextView tvMobile;
    }

    private String getString(String str) {
        if (TextUtils.isEmpty(str) || str.equals("null")) {
            return "";
        } else {
            return str;
        }
    }
}
