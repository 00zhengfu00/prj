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
package com.physicmaster.modules.mine.activity.school;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.physicmaster.R;

import java.util.List;


public class AreaAdapter extends BaseAdapter {
	private Context    mContext;
	private List<Item> list;

	public AreaAdapter(Context context, List<Item> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_area, parent, false);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_select_area);
			holder.tvName.setText(list.get(position).name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.tvName.setText(list.get(position).name);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvName;
	}
}
