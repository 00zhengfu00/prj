package com.physicmaster.modules.study.fragment.section;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseFragment;
import com.physicmaster.modules.study.activity.exercise.BreakthoughFinishActivity;

import java.util.ArrayList;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/11/3 13:19
 * 功能说明 :预习
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class BeforehandFragment extends BaseFragment {

    private ListView         lvBeforehand;
    private FragmentActivity mContext;

    private ArrayList<String> mBeforehandList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_review;

    }

    @Override
    protected void initView() {
        mContext = getActivity();

        lvBeforehand = (ListView) rootView.findViewById(R.id.lv_review);


        mBeforehandList.add("温度");
        mBeforehandList.add("融化和凝固");
        mBeforehandList.add("汽化和液化");
        mBeforehandList.add("升华和凝华");

        BeforehandAdapter beforehandAdapter = new BeforehandAdapter();
        lvBeforehand.setAdapter(beforehandAdapter);

        lvBeforehand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mContext.startActivity(new Intent(mContext, BreakthoughFinishActivity.class));
            }
        });
    }

    class BeforehandAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBeforehandList.size();
        }

        @Override
        public String getItem(int position) {
            return mBeforehandList.get(position);
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
                        R.layout.list_item_preview, null);
                holder = new ViewHolder();
                holder.rlReview = (RelativeLayout) convertView
                        .findViewById(R.id.rl_review);
                holder.tvVideo = (TextView) convertView
                        .findViewById(R.id.tv_video);
                holder.tvReview = (TextView) convertView
                        .findViewById(R.id.tv_review);
                holder.ivBian = (ImageView) convertView
                        .findViewById(R.id.iv_bian);
                holder.ivXing1 = (ImageView) convertView
                        .findViewById(R.id.iv_xing1);
                holder.ivXing2 = (ImageView) convertView
                        .findViewById(R.id.iv_xing2);
                holder.ivXing3 = (ImageView) convertView
                        .findViewById(R.id.iv_xing3);
                holder.ivTopic = (ImageView) convertView
                        .findViewById(R.id.iv_topic);
                holder.tvTopic = (TextView) convertView
                        .findViewById(R.id.tv_topic);
                holder.ivVideo = (ImageView) convertView
                        .findViewById(R.id.iv_video);
                holder.llXing = (LinearLayout) convertView
                        .findViewById(R.id.ll_xing);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ivVideo.setEnabled(false);
            holder.tvTopic.setVisibility(View.GONE);
            holder.llXing.setVisibility(View.VISIBLE);
            String item = getItem(position);

            holder.tvReview.setText(item);

            return convertView;
        }
    }

    static class ViewHolder {
        TextView       tvReview;
        ImageView      ivBian;
        ImageView      ivXing1;
        ImageView      ivXing2;
        ImageView      ivXing3;
        TextView       tvTopic;
        TextView       tvVideo;
        ImageView      ivVideo;
        ImageView      ivTopic;
        RelativeLayout rlReview;
        LinearLayout   llXing;


    }
}
