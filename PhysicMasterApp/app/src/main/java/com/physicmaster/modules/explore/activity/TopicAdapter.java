package com.physicmaster.modules.explore.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.net.response.course.MemberDetailsResponse;

import java.util.List;

/**
 * Created by songrui on 17/7/31.
 */

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MemberDetailsResponse.DataBean.OrderDetailBean.GiftItemListBean> list;
    private int mSelectedItem = 0;
    private Context context;

    public TopicAdapter(Context context, List< MemberDetailsResponse.DataBean.OrderDetailBean.GiftItemListBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_topic_item,
                parent, false);
        TitleViewHolder viewHolder = new TitleViewHolder(itemView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TitleViewHolder) {
            final TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.rbTopic.setChecked(position == mSelectedItem);
            titleViewHolder.tvGrade.setText(list.get(position).title);
            Glide.with(context).load(list.get(position).imgUrl).into(titleViewHolder.ivContent);
            titleViewHolder.rbTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = position;
                    notifyItemRangeChanged(0, list.size());
                    titleViewHolder.rbTopic.setChecked(position == mSelectedItem);
                    if (selectTopic != null) {
                        selectTopic.select(list.get(position).giftItemId);
                    }
                }
            });
            titleViewHolder.ivContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = position;
                    notifyItemRangeChanged(0, list.size());
                    titleViewHolder.rbTopic.setChecked(position == mSelectedItem);
                    if (selectTopic != null) {
                        selectTopic.select(list.get(position).giftItemId);
                    }
                }
            });

        }
    }

    public interface SelectTopic {
        public void select(int id);
    }

    public SelectTopic selectTopic;

    public void setSelectTopicListen(SelectTopic selectTopic) {
        this.selectTopic = selectTopic;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        public ImageView   ivContent;
        public RadioButton rbTopic;
        public TextView    tvGrade;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ivContent = (ImageView) itemView.findViewById(R.id.iv_content);
            tvGrade = (TextView) itemView.findViewById(R.id.tv_grade);
            rbTopic = (RadioButton) itemView.findViewById(R.id.rb_topic);
        }

        //        @Override
        //        public void onClick(View v) {
        //            mSelectedItem = getAdapterPosition();
        //            notifyItemRangeChanged(0, list.size());
        //        }
    }
}
