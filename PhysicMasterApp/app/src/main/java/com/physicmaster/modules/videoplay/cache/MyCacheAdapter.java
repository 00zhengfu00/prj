/* 
 * 系统名称：lswuyou
 * 类  名  称：ClassesAdapter.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-18 上午9:55:37
 * 功能说明： 班级ListView 适配器
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.modules.videoplay.cache;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.modules.videoplay.cache.bean.CourseInfo;


import java.util.List;

public class MyCacheAdapter extends BaseAdapter {
    private List<CourseInfo> list;
    private Context mContext;

    public MyCacheAdapter(List<CourseInfo> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.course_info_item,
                    parent, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.course_title);
            holder.tvCachedVideoNum = (TextView) convertView.findViewById(R.id.cached_video_num);
            holder.ivPoster = (ImageView) convertView.findViewById(R.id.iv_course_poster);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getName());
        holder.tvCachedVideoNum.setText("已缓存" + list.get(position).getDownloadedVideoNum() + "个");
        Glide.with(mContext).load(list.get(position).getPosterUrl()).into(holder.ivPoster);
        return convertView;
    }

    class ViewHolder {
        public TextView tvTitle;
        public TextView tvCachedVideoNum;
        public ImageView ivPoster;
    }
}
