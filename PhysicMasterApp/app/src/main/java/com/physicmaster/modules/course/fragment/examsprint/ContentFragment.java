package com.physicmaster.modules.course.fragment.examsprint;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseFragment;

import java.util.ArrayList;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/31 15:49
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class ContentFragment extends BaseFragment {

    private ListView        lvVideo;
    private FragmentActivity mContext;

    private ArrayList<String> mContentList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_content;
    }

    @Override
    protected void initView() {

        mContext = getActivity();

        lvVideo = (ListView) rootView.findViewById(R.id.lv_video);

        for (int i = 0; i < 10; i++) {
            mContentList.add("中考冲刺" + i);

        }


        ContextAdapter contextAdapter = new ContextAdapter();
        lvVideo.setAdapter(contextAdapter);
    }

    class ContextAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mContentList.size();
        }

        @Override
        public String getItem(int position) {
            return mContentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.list_item_content, null);
                holder = new ViewHolder();
                holder.llContent = (LinearLayout) convertView
                        .findViewById(R.id.ll_content);
                holder.tvContent = (TextView) convertView
                        .findViewById(R.id.tv_content);
                holder.tvTime = (TextView) convertView
                        .findViewById(R.id.tv_time);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String item = getItem(position);

            holder.tvContent.setText(item);

            return convertView;
        }
    }

    static class ViewHolder {
        TextView     tvContent;
        TextView     tvTime;
        LinearLayout llContent;

    }
}
