package com.physicmaster.modules.discuss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.modules.account.LoginActivity;
import com.physicmaster.modules.account.LoginDialogActivity;
import com.physicmaster.modules.explore.activity.MembersDetailedActivity;
import com.physicmaster.modules.mine.activity.question.MessageActivity;
import com.physicmaster.modules.mine.activity.question.QuestionDetailsActivity;
import com.physicmaster.modules.study.fragment.dialogfragment.CommonDialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.discuss.QuestionListResponse;
import com.physicmaster.net.service.discuss.QuestionListService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.PullToRefreshLayout;
import com.physicmaster.widget.SelectPopupWindow;

import java.util.ArrayList;
import java.util.List;


public class AllQuestionActivity extends BaseActivity implements View.OnClickListener {

    private List<QuestionListResponse.DataBean.QaQuestionVosBean> qaQuestionVos;
    private ListView lvQuestion;
    private TextView titleMiddleTextview;
    private TextView titleRightTextview;
    private TextView titleLeftTextview;
    private ImageView titleLeftImageview;
    private QuestionAdapter questionAdapter;
    private TextView tvNumber;
    private PullToRefreshLayout pullToRefreshLayout;

    private List<StartupResponse.DataBean.EduGradeYearListBean> eduGradeYearList;
    private List<StartupResponse.DataBean.SubjectTypeListBean> subjectTypeList;

    private int subjectId = -2;
    private int eduGradeId = -2;

    @Override
    protected void findViewById() {
        titleLeftImageview = (ImageView) findViewById(R.id.title_left_imageview);
        titleLeftTextview = (TextView) findViewById(R.id.title_left_textview);
        titleMiddleTextview = (TextView) findViewById(R.id.title_middle_textview);
        titleRightTextview = (TextView) findViewById(R.id.title_right_textview);

        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (subjectId == -2 || eduGradeId == -2) {
                    showQuestion(-1, -1);
                } else {
                    showQuestion(subjectId, eduGradeId);
                }
            }

            @Override
            public void onPullup(int maxId) {
                if (subjectId == -2 || eduGradeId == -2) {
                    updateQuestion(maxId, -1, -1);
                } else {
                    updateQuestion(maxId, subjectId, eduGradeId);
                }
            }
        });
        qaQuestionVos = new ArrayList<>();
        titleLeftImageview.setOnClickListener(this);
        titleLeftTextview.setOnClickListener(this);
        titleMiddleTextview.setOnClickListener(this);
        titleRightTextview.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        lvQuestion = pullToRefreshLayout.getListView();
        lvQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lvQuestion.getHeaderViewsCount() != 0) {

                    if (TextUtils.isEmpty(id + "") || id == 0 || position == 0 || position == qaQuestionVos.size() + 1) {
                        return;
                    } else {
                        Intent intent = new Intent(AllQuestionActivity.this, QuestionDetailsActivity
                                .class);
                        intent.putExtra("questionId", (int) id);
                        startActivityForResult(intent, 10);
                    }
                } else {
                    if (TextUtils.isEmpty(id + "") || id == 0 || position == qaQuestionVos.size() + 1) {
                        return;
                    } else {
                        Intent intent = new Intent(AllQuestionActivity.this, QuestionDetailsActivity
                                .class);
                        intent.putExtra("questionId", (int) id);
                        startActivityForResult(intent, 10);
                    }
                }
            }
        });
        if (subjectId == -2 || eduGradeId == -2) {
            showQuestion(-1, -1);
        } else {
            showQuestion(subjectId, eduGradeId);
        }

        StartupResponse.DataBean startupDataBean = BaseApplication.getStartupDataBean();
        this.eduGradeYearList = new ArrayList<>();
        this.subjectTypeList = new ArrayList<>();
        this.eduGradeYearList.clear();
        this.subjectTypeList.clear();
        this.eduGradeYearList.addAll(startupDataBean.eduGradeYearList);
        this.subjectTypeList.addAll(startupDataBean.subjectTypeList);

        StartupResponse.DataBean.EduGradeYearListBean allGradeBean = new StartupResponse.DataBean
                .EduGradeYearListBean();
        allGradeBean.state = false;
        allGradeBean.i = -1;
        allGradeBean.n = "全部";
        allGradeBean.s = new ArrayList<>();
        for (StartupResponse.DataBean.SubjectTypeListBean subjectTypeListBean : subjectTypeList) {
            allGradeBean.s.add(subjectTypeListBean.i);
        }
        this.eduGradeYearList.add(0, allGradeBean);
        StartupResponse.DataBean.SubjectTypeListBean subjectTypeBean = new StartupResponse
                .DataBean.SubjectTypeListBean();
        subjectTypeBean.state = 1;
        subjectTypeBean.i = -1;
        subjectTypeBean.n = "全部";
        this.subjectTypeList.add(0, subjectTypeBean);
    }

    //    @Override
    //    public void onBackPressed() {
    //        super.onBackPressed();
    //        this.eduGradeYearList.clear();
    //        this.eduGradeYearList = null;
    //        this.subjectTypeList.clear();
    //        this.subjectTypeList = null;
    //    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showQuestion(int subjectId, int eduGradeId) {
        final QuestionListService service = new QuestionListService(AllQuestionActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<QuestionListResponse>() {

            @Override
            public void onGetData(QuestionListResponse data) {
                if (!TextUtils.isEmpty(data.data.newsCount + "") && data.data.newsCount != 0) {
                    final View view = LayoutInflater.from(AllQuestionActivity.this).inflate(R.layout
                            .question_all_head, null);
                    tvNumber = (TextView) view.findViewById(R.id.tv_number);
                    tvNumber.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lvQuestion.removeHeaderView(view);
                            Intent intent = new Intent(AllQuestionActivity.this, MessageActivity
                                    .class);
                            startActivity(intent);
                        }
                    });
                    tvNumber.setText(data.data.newsCount + "条新消息");
                    if (lvQuestion.getHeaderViewsCount() == 0) {
                        lvQuestion.addHeaderView(view);
                    }
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
                UIUtils.showToast(AllQuestionActivity.this, errorMsg);
            }
        });
        StringBuilder params = new StringBuilder();
        params.append("subjectType=" + subjectId);
        params.append("&eduGradeYear=" + eduGradeId);
        service.postLogined(params.toString(), false);
    }

    private void updateQuestion(int maxId, int subjectId, int eduGradeId) {
        final QuestionListService service = new QuestionListService(AllQuestionActivity.this);
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
                UIUtils.showToast(AllQuestionActivity.this, errorMsg);
            }
        });
        StringBuilder params = new StringBuilder();
        params.append("&subjectType=" + subjectId);
        params.append("&eduGradeYear=" + eduGradeId);
        params.append("&nextId=" + maxId);
        service.postLogined(params.toString(), false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_all_question;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_imageview:
            case R.id.title_left_textview:
                finish();
                break;
            case R.id.title_middle_textview:
                titleMiddleTextview.setSelected(true);
                new SelectPopupWindow(AllQuestionActivity.this, new SelectPopupWindow.OnSubmitListener() {
                    @Override
                    public void onSubmit(int eduGradeId, int subjectId) {
                        AllQuestionActivity.this.subjectId = subjectId;
                        AllQuestionActivity.this.eduGradeId = eduGradeId;
                        showQuestion(subjectId, eduGradeId);
                    }
                }, new SelectPopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        titleMiddleTextview.setSelected(false);
                    }
                }, eduGradeYearList, subjectTypeList, subjectId, eduGradeId).showPopupWindow(titleMiddleTextview);
                break;
            case R.id.title_right_textview:
                if (BaseApplication.getUserData().isTourist == 1) {
                    Utils.gotoLogin(AllQuestionActivity.this);
                } else {
                    Intent intent = new Intent(AllQuestionActivity.this, QuestionPublishActivity.class);
                    startActivityForResult(intent, 10);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (10 == requestCode) {
                if (subjectId == -2 || eduGradeId == -2) {
                    showQuestion(-1, -1);
                } else {
                    showQuestion(subjectId, eduGradeId);
                }
            }
        }
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
            return qaQuestionVos.get(position).qid;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(AllQuestionActivity.this, R.layout.list_item_question, null);
                holder = new ViewHolder();
                holder.tvQuestionTitle = (TextView) convertView.findViewById(R.id.tv_question_title);
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
                Glide.with(AllQuestionActivity.this).load(item.imgVo.u).placeholder(R.color.colorBackgound).into(holder.ivContent);
            } else {
                holder.ivContent.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvQuestionTitle;
        TextView tvTime;
        TextView tvContent;
        TextView tvAnswerNumber;
        TextView tvSubject;
        ImageView ivContent;
    }


}
