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
import com.physicmaster.modules.mine.activity.question.MyQuestionActivity;
import com.physicmaster.modules.mine.activity.question.QuestionDetailsActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.discuss.QuestionListResponse;
import com.physicmaster.net.service.discuss.QuestionMyListService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songrui on 17/5/4.
 */

public class QuestionFragment extends BaseFragment2 {

    private List<QuestionListResponse.DataBean.QaQuestionVosBean> qaQuestionVos;
    private ListView                                              lvQuestion;
    private MyQuestionActivity                                    mContext;
    private QuestionAdapter                                       questionAdapter;
    private PullToRefreshLayout                                   pullToRefreshLayout;

    @Override
    protected void initView(View view) {

        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.pull_to_refresh_layout);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showQuestion();
            }

            @Override
            public void onPullup(int maxId) {
                updateQuestion(maxId);
            }
        });
        qaQuestionVos = new ArrayList<>();
        mContext = (MyQuestionActivity) getActivity();
        lvQuestion = pullToRefreshLayout.getListView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qusetion;
    }

    @Override
    public void onResume() {
        super.onResume();
        showQuestion();
    }

    @Override
    public void fetchData() {
        lvQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == qaQuestionVos.size()) {
                    return;
                } else {
                    Intent intent = new Intent(mContext, QuestionDetailsActivity.class);
                    intent.putExtra("questionId", qaQuestionVos.get(position).qid);
                    mContext.startActivity(intent);
                }

            }
        });
    }

    private void showQuestion() {
        final QuestionMyListService service = new QuestionMyListService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<QuestionListResponse>() {

            @Override
            public void onGetData(QuestionListResponse data) {
                if (data.data.newsCount>0) {
                   mContext.titleBuilder.setRightImageRes(R.mipmap.new_xiaoxi);
                } else{
                   mContext.titleBuilder.setRightImageRes(R.mipmap.xiaoxi);
                }
                qaQuestionVos.clear();
                qaQuestionVos.addAll(data.data.qaQuestionVos);
                questionAdapter = new QuestionAdapter();
                lvQuestion.setAdapter(questionAdapter);
                pullToRefreshLayout.notifyData(data.data.nextId, data.data
                        .qaQuestionVos, false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("", false);
    }

    private void updateQuestion(int maxId) {
        final QuestionMyListService service = new QuestionMyListService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<QuestionListResponse>() {
            @Override
            public void onGetData(QuestionListResponse data) {
                qaQuestionVos.addAll(data.data.qaQuestionVos);
                pullToRefreshLayout.notifyData(data.data.nextId, data.data
                        .qaQuestionVos, true);
                questionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("nextId=" + maxId, false);
    }

    class QuestionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return qaQuestionVos.size();
        }

        @Override
        public QuestionListResponse.DataBean.QaQuestionVosBean getItem(int position) {
            return qaQuestionVos.get(position);
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
                        R.layout.list_item_question, null);
                holder = new ViewHolder();
                holder.tvQuestionTitle = (TextView) convertView.findViewById(R.id
                        .tv_question_title);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                holder.ivContent = (ImageView) convertView.findViewById(R.id.iv_content);
                holder.tvSubject = (TextView) convertView.findViewById(R.id.tv_subject);
                holder.tvAnswerNumber = (TextView) convertView.findViewById(R.id.tv_answer_number);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            QuestionListResponse.DataBean.QaQuestionVosBean item = getItem(position);
            holder.tvQuestionTitle.setText(item.title + "");
            holder.tvTime.setText(item.releaseTime + "");
            holder.tvContent.setText(item.content + "");
            holder.tvSubject.setText(item.gradeSubject + "");
            holder.tvAnswerNumber.setText(item.answerCount + "条回答");
            if (null != item.imgVo) {
                holder.ivContent.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(item.imgVo.u).placeholder(R.color.colorBackgound).into(holder.ivContent);
            } else {
                holder.ivContent.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView  tvQuestionTitle;
        TextView  tvTime;
        TextView  tvContent;
        TextView  tvAnswerNumber;
        TextView  tvSubject;
        ImageView ivContent;
    }
}
