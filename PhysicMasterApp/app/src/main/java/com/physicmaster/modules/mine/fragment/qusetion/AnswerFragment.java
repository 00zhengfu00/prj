package com.physicmaster.modules.mine.fragment.qusetion;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseFragment2;
import com.physicmaster.modules.mine.activity.question.AnswerDetailsActivity;
import com.physicmaster.modules.mine.activity.question.MyQuestionActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.discuss.AnswerListResponse;
import com.physicmaster.net.service.discuss.AnswerListService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songrui on 17/5/4.
 */

public class AnswerFragment extends BaseFragment2 {

    private ListView                                         lvAnswer;
    private List<AnswerListResponse.DataBean.AnswerListBean> mAnswerlist;
    private PullToRefreshLayout                              pullToRefreshLayout;
    private MyQuestionActivity                               mContext;
    private AnswerAdapter                                    answerAdapter;

    @Override
    protected void initView(View view) {
        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.pull_to_refresh_layout);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showAnswer();
            }

            @Override
            public void onPullup(int maxId) {
                upDataAnswer(maxId);
            }
        });
        mContext = (MyQuestionActivity) getActivity();
        lvAnswer = pullToRefreshLayout.getListView();
        mAnswerlist = new ArrayList<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_answer;
    }

    @Override
    public void onResume() {
        super.onResume();
        showAnswer();
    }

    @Override
    public void fetchData() {

        lvAnswer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mAnswerlist.size()) {
                    return;
                } else {
                    if (mAnswerlist.get(position).status == 1) {
                        Intent intent = new Intent(mContext, AnswerDetailsActivity.class);
                        intent.putExtra("answerId", mAnswerlist.get(position).answerId);
                        mContext.startActivity(intent);
                    } else {
                        UIUtils.showToast(mContext, "该回答已被删除！");
                        return;
                    }
                }
            }
        });
    }

    private void showAnswer() {
        final AnswerListService service = new AnswerListService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<AnswerListResponse>() {

            @Override
            public void onGetData(AnswerListResponse data) {
                mAnswerlist.clear();
                mAnswerlist.addAll(data.data.answerList);
                answerAdapter = new AnswerAdapter();
                lvAnswer.setAdapter(answerAdapter);
                pullToRefreshLayout.notifyData(data.data.nextAnswerId, data.data
                        .answerList, false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("", false);
    }

    private void upDataAnswer(int maxId) {
        final AnswerListService service = new AnswerListService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<AnswerListResponse>() {

            @Override
            public void onGetData(AnswerListResponse data) {
                mAnswerlist.addAll(data.data.answerList);
                pullToRefreshLayout.notifyData(data.data.nextAnswerId, data.data
                        .answerList, true);
                answerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("nextAnswerId=" + maxId, false);
    }

    class AnswerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAnswerlist.size();
        }

        @Override
        public AnswerListResponse.DataBean.AnswerListBean getItem(int position) {
            return mAnswerlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.list_item_answer, null);
                holder = new ViewHolder();
                holder.tvQuestionTitle = (TextView) convertView.findViewById(R.id.tv_question_title);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                holder.ivContent = (ImageView) convertView.findViewById(R.id.iv_content);
                holder.tvPraise = (TextView) convertView.findViewById(R.id.tv_praise);
                holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
                holder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final AnswerListResponse.DataBean.AnswerListBean item = getItem(position);
            holder.tvQuestionTitle.setText(item.title + "");
            holder.tvContent.setText(item.content + "");
            if (null != item.imgVo) {
                holder.ivContent.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(item.imgVo.u).placeholder(R.color.colorBackgound).into(holder.ivContent);
            } else {
                holder.ivContent.setVisibility(View.GONE);
            }
            holder.tvPraise.setText(item.likeCount + "个赞");
            holder.tvComment.setText(item.commentCount + "条评论");

            return convertView;
        }
    }

    static class ViewHolder {
        TextView  tvQuestionTitle;
        TextView  tvTime;
        TextView  tvContent;
        TextView  tvPraise;
        TextView  tvComment;
        TextView  tvDelete;
        ImageView ivContent;
    }

}
