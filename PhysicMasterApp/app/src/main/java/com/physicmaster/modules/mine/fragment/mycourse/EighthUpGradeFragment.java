package com.physicmaster.modules.mine.fragment.mycourse;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseFragment;

import java.util.ArrayList;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/25 14:11
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class EighthUpGradeFragment extends BaseFragment {

    private FragmentActivity mContext;
    private ArrayList<String> mCourseList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_eighth_up;
    }

    @Override
    protected void initView() {
        mContext = getActivity();


        GridView gvCourse = (GridView) rootView.findViewById(R.id.gv_course);

        mCourseList.add("热和能");
        mCourseList.add("物态变化");
        mCourseList.add("电与磁");
        mCourseList.add("生活用电");
        mCourseList.add("信息与传递");
        mCourseList.add("力");


        CourseAdapter courseAdapter = new CourseAdapter();
        gvCourse.setAdapter(courseAdapter);
    }

    class CourseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mCourseList.size();
        }

        @Override
        public String getItem(int position) {
            return mCourseList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(),
                        R.layout.grid_item_course, null);
                holder = new ViewHolder();
                holder.tvPrice = (TextView) convertView
                        .findViewById(R.id.tv_price);
                holder.tvCourse = (TextView) convertView
                        .findViewById(R.id.tv_course);
                holder.ivCourse = (ImageView) convertView
                        .findViewById(R.id.iv_course);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String item = getItem(position);

            holder.tvCourse.setText(item);


            return convertView;
        }
    }

    static class ViewHolder {
        TextView  tvCourse;
        TextView  tvPrice;
        ImageView ivCourse;
    }
}
