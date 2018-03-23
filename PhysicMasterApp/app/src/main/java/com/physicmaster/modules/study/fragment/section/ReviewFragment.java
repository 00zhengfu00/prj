package com.physicmaster.modules.study.fragment.section;

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
import com.physicmaster.modules.study.fragment.dialogfragment.SelectStudyDialogFragment;

import java.util.ArrayList;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/31 15:49
 * 功能说明 :复习
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class ReviewFragment extends BaseFragment {

    private ListView         lvReview;
    private FragmentActivity mContext;

    private ArrayList<String> mReviewList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_review;
    }

    @Override
    protected void initView() {

        mContext = getActivity();

        lvReview = (ListView) rootView.findViewById(R.id.lv_review);


        mReviewList.add("基础经典习题");
        mReviewList.add("夯实经典习题");
        mReviewList.add("提高经典习题");


        ReviewAdapter reviewAdapter = new ReviewAdapter();
        lvReview.setAdapter(reviewAdapter);


        lvReview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                new SelectStudyDialogFragment()
                        .show(getFragmentManager(), "select_study_dialog");

            }
        });
    }

    class ReviewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mReviewList.size();
        }

        @Override
        public String getItem(int position) {
            return mReviewList.get(position);
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
                        R.layout.list_item_review, null);
                // convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_review, null,false);
                holder = new ViewHolder();
                holder.rlReview = (RelativeLayout) convertView
                        .findViewById(R.id.rl_review);
//                holder.llXing = (LinearLayout) convertView
//                        .findViewById(R.id.ll_xing);
                holder.tvTopic = (TextView) convertView
                        .findViewById(R.id.tv_topic);
                holder.tvVideo = (TextView) convertView
                        .findViewById(R.id.tv_video);
                holder.tvReview = (TextView) convertView
                        .findViewById(R.id.tv_review);
                holder.ivBian = (ImageView) convertView
                        .findViewById(R.id.iv_bian);
                holder.ivTopic = (ImageView) convertView
                        .findViewById(R.id.iv_topic);
                holder.ivVideo = (ImageView) convertView
                        .findViewById(R.id.iv_video);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ivTopic.setEnabled(false);
//            holder.llXing.setVisibility(View.GONE);
            holder.tvTopic.setVisibility(View.VISIBLE);
            String item = getItem(position);

            holder.tvReview.setText(item);

            return convertView;
        }
    }

    static class ViewHolder {
        TextView       tvReview;
        ImageView      ivBian;
        TextView       tvVideo;
        ImageView      ivVideo;
        ImageView      ivTopic;
        TextView       tvTopic;
        RelativeLayout rlReview;
        LinearLayout   llXing;

    }
}
