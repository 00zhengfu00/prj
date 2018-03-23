package com.physicmaster.modules.mine.activity.topicmap;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.modules.mine.activity.user.Book;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.excercise.GetTopicmapsResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.excercise.GetTopicmapsService;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

import java.util.List;

public class TopicmapsActivity extends BaseActivity {

    private static final String TAG = "TopicmapsActivity";
    private GridView gvTopicmaps;
    private List<GetTopicmapsResponse.DataBean.AppQuestionWrongVoListBean> mTopicmapsList;
    private int[] colors = new int[]{R.color.colorTopicmap1, R.color.colorTopicmap2, R.color
            .colorTopicmap3,
            R.color.colorTopicmap2, R.color.colorTopicmap3, R.color.colorTopicmap1,
            R.color.colorTopicmap3, R.color.colorTopicmap1, R.color.colorTopicmap2};
    private RelativeLayout rlEmpty;

    @Override
    protected void findViewById() {

        gvTopicmaps = (GridView) findViewById(R.id.gv_topicmaps);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_empty);
        initTitle();
    }

    private void initTitle() {
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("我的错题本");
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTopicmaps();
    }

    private void updateTopicmaps() {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final GetTopicmapsService service = new GetTopicmapsService(TopicmapsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GetTopicmapsResponse>() {
            @Override
            public void onGetData(GetTopicmapsResponse data) {
                loadingDialog.dismissDialog();
                mTopicmapsList = data.data.wrongChapterList;
                if (null == mTopicmapsList || mTopicmapsList.size() == 0) {
                    rlEmpty.setVisibility(View.VISIBLE);
                    gvTopicmaps.setVisibility(View.GONE);
                } else {
                    TopicmapsAdapter topicmapsAdapter = new TopicmapsAdapter();
                    gvTopicmaps.setAdapter(topicmapsAdapter);
                    rlEmpty.setVisibility(View.GONE);
                    gvTopicmaps.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(TopicmapsActivity.this, errorMsg);
                rlEmpty.setVisibility(View.VISIBLE);
                gvTopicmaps.setVisibility(View.GONE);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("", false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_topicmaps;
    }

    class TopicmapsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTopicmapsList.size();
        }

        @Override
        public GetTopicmapsResponse.DataBean.AppQuestionWrongVoListBean getItem(int position) {
            return mTopicmapsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(TopicmapsActivity.this,
                        R.layout.grid_item_topicmap, null);
                holder = new ViewHolder();
                holder.rlLayout = (RelativeLayout) convertView
                        .findViewById(R.id.rl_layout);
                holder.tvTopicmaps = (TextView) convertView
                        .findViewById(R.id.tv_topicmaps);
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
                holder.ivTopicmaps = (ImageView) convertView
                        .findViewById(R.id.iv_topicmaps);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GetTopicmapsResponse.DataBean.AppQuestionWrongVoListBean item = getItem(position);

            holder.tvTopicmaps.setText("共" + item.wrongTotal + "题");
            holder.tvTitle.setText(item.chapterName + "");
            holder.rlLayout.setBackgroundResource(colors[position % colors.length]);
            if (!TextUtils.isEmpty(item.chapterIcon)) {
                Glide.with(TopicmapsActivity.this).load(item.chapterIcon).into(holder.ivTopicmaps);
            }
            holder.rlLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TopicmapsActivity.this, TopicmapV2Activity
                            .class);
                    intent.putExtra("chapterId", item.chapterId);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        RelativeLayout rlLayout;
        TextView tvTopicmaps;
        TextView tvTitle;
        ImageView ivTopicmaps;
    }
}
