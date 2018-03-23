package com.physicmaster.modules.explore.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.net.response.explore.GetExploreResponse;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.widget.TitleBuilder;

import java.util.List;

public class NewVideoActivity extends BaseActivity {


    private ListView lvNewVideo;
    private RelativeLayout rlEmpty;
    private View footer;
    private List<GetExploreResponse.DataBean.NewVideoListBean> newVideoList;

    @Override
    protected void findViewById() {


        newVideoList = (List<GetExploreResponse.DataBean.NewVideoListBean>) getIntent().getSerializableExtra("newVideoList");

        lvNewVideo = (ListView) findViewById(R.id.lv_new_video);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_empty);
        initTitle();


    }

    @Override
    protected void initView() {
        if (null == newVideoList || newVideoList.size() == 0) {
            rlEmpty.setVisibility(View.VISIBLE);
            lvNewVideo.setVisibility(View.GONE);
        } else {
            rlEmpty.setVisibility(View.GONE);
            lvNewVideo.setVisibility(View.VISIBLE);
            NewVideoAdapter newVideoAdapter = new NewVideoAdapter();
            lvNewVideo.setAdapter(newVideoAdapter);
        }
    }

    private void initTitle() {
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("上新视频");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_new_video;
    }

    class NewVideoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newVideoList.size();
        }

        @Override
        public GetExploreResponse.DataBean.NewVideoListBean getItem(int position) {
            return newVideoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(NewVideoActivity.this,
                        R.layout.list_item_new_video, null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
                holder.ivContent = (ImageView) convertView
                        .findViewById(R.id.iv_content1);
                holder.ivPlay = (ImageView) convertView
                        .findViewById(R.id.iv_play);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final GetExploreResponse.DataBean.NewVideoListBean item = getItem(position);

            holder.tvTitle.setText(item.title + "");

            Glide.with(NewVideoActivity.this).load(item.posterUrl).placeholder(R.drawable.placeholder_gray).into(holder.ivContent);
            int maxHeight = ScreenUtils.get16_9ImageMaxHeight(NewVideoActivity.this, 20);
            holder.ivContent.setAdjustViewBounds(true);
            holder.ivContent.setMaxHeight(maxHeight);
            holder.ivContent.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.ivContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NewVideoActivity.this, VideoPlayActivity.class);
                    intent.putExtra("videoId", item.videoId + "");
                    startActivity(intent);
                }
            });
            holder.ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NewVideoActivity.this, VideoPlayActivity.class);
                    intent.putExtra("videoId", item.videoId + "");
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivContent;
        ImageView ivPlay;
        TextView tvTitle;

    }

}
