package com.physicmaster.modules.study.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.net.response.course.VideoVo;
import com.physicmaster.widget.recyclerView.BaseRecyclerAdapter;
import com.physicmaster.widget.recyclerView.BaseRecyclerHolder;

import java.util.List;

/**
 * @ explain:
 * @ author：xujun on 2016/10/18 16:42
 * @ email：gdutxiaoxu@163.com
 */
public class ItemAdapter extends BaseRecyclerAdapter<VideoVo> {
    public ItemAdapter(Context context, List<VideoVo> datas) {
        super(context, R.layout.list_item_preview2, datas);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, VideoVo item, int position) {
        //设置视频标题
        TextView tvTitle = holder.getView(R.id.tv_review);
        tvTitle.setText(item.videoTitle);
        //视频是否播放完成
        ImageView ivVideo = holder.getView(R.id.iv_video);
        if (item.videoFinish == 1) {
            ivVideo.setSelected(true);
        } else {
            ivVideo.setSelected(false);
        }
        //习题是否完成
        ImageView ivExcersise = holder.getView(R.id.iv_excersise);
        ImageView ivStar1 = holder.getView(R.id.iv_xing1);
        ImageView ivStar2 = holder.getView(R.id.iv_xing2);
        ImageView ivStar3 = holder.getView(R.id.iv_xing3);
        RelativeLayout rlStars = holder.getView(R.id.rl_stars);
        LinearLayout llStar = holder.getView(R.id.ll_xing);
        TextView tvExcersiseNum = holder.getView(R.id.tv_topic);
        if (item.existExample == 0) {
            rlStars.setVisibility(View.GONE);
        }else {
            rlStars.setVisibility(View.VISIBLE);
        }
        if (item.starLevel == 0) {
            ivExcersise.setSelected(false);
            llStar.setVisibility(View.GONE);
            tvExcersiseNum.setVisibility(View.VISIBLE);
        } else {
            llStar.setVisibility(View.VISIBLE);
            tvExcersiseNum.setVisibility(View.GONE);
            if (item.starLevel == 1) {
                ivStar1.setImageResource(R.mipmap.jinxing);
                ivStar2.setImageResource(R.mipmap.huixing);
                ivStar3.setImageResource(R.mipmap.huixing);
                ivExcersise.setSelected(true);
            } else if (item.starLevel == 2) {
                ivStar1.setImageResource(R.mipmap.jinxing);
                ivStar2.setImageResource(R.mipmap.jinxing);
                ivStar3.setImageResource(R.mipmap.huixing);
                ivExcersise.setSelected(true);
            } else if (item.starLevel == 3) {
                ivStar1.setImageResource(R.mipmap.jinxing);
                ivStar2.setImageResource(R.mipmap.jinxing);
                ivStar3.setImageResource(R.mipmap.jinxing);
                ivExcersise.setSelected(true);
            }
        }

        //视频长度
        TextView tvVideoLen = holder.getView(R.id.tv_video);
        tvVideoLen.setText(item.timeLengthStr);
        //条目被选中
        View imageSelect = holder.getView(R.id.iv_bian);
        if (item.selected) {
            imageSelect.setVisibility(View.VISIBLE);
        } else {
            imageSelect.setVisibility(View.INVISIBLE);
        }
    }
}
