package com.physicmaster.modules.mine.activity.question;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.modules.discuss.activity.AnswerPublishActivity;
import com.physicmaster.modules.discuss.activity.EditImageActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.discuss.QuestionDetailAnswerResponse;
import com.physicmaster.net.response.discuss.QuestionDetailResponse;
import com.physicmaster.net.service.discuss.DeleteQuestionService;
import com.physicmaster.net.service.discuss.PraiseService;
import com.physicmaster.net.service.discuss.QuestionDetailAnswersService;
import com.physicmaster.net.service.discuss.QuestionDetailService;
import com.physicmaster.net.service.discuss.UnPraiseService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.DeleteCommentDialog;
import com.physicmaster.widget.PullToRefreshLayout;
import com.physicmaster.widget.ReportDialog;
import com.physicmaster.widget.RoundImageView;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;
import static com.physicmaster.R.id.view1;
import static com.physicmaster.modules.discuss.activity.QuestionPublishActivity.EXTRA_PICTURE_PATHS;
import static com.physicmaster.modules.discuss.activity.QuestionPublishActivity.EXTRA_PICTURE_POSITION;

public class QuestionDetailsActivity extends BaseActivity {
    private ArrayList<String> mQusetion;
    private ListView lvQuestionDetails;
    private Button btnAswer;
    private TextView tvQuestionTitleHead;
    private TextView tvTimeHead;
    private TextView tvContentHead;
    private ImageView ivContentHead;
    private TextView tvSubjectHead;
    private TextView tvNumberHead;
    private PullToRefreshLayout pullToRefreshLayout;
    private QuestionDetailsAdapter questionAdapter;
    private int qid;
    private List<QuestionDetailAnswerResponse.DataBean.AnswerListBean> answerList;
    private int questionId;
    private LinearLayout llImages;
    private TitleBuilder titleBuilder;
    private QuestionDetailResponse.DataBean.QuestionDetailBean questionDetail;
    private int mPosition = -1;
    private AtomicBoolean isOnNetwork = new AtomicBoolean(false);

    @Override
    protected void findViewById() {
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showQuestion();
            }

            @Override
            public void onPullup(int maxId) {
                upDataAnswers(qid, maxId);
            }
        });
        btnAswer = (Button) findViewById(R.id.btn_answer);
        questionId = getIntent().getIntExtra("questionId", 0);
        answerList = new ArrayList<>();
        lvQuestionDetails = pullToRefreshLayout.getListView();
        initTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        showQuestion();

        View view = LayoutInflater.from(this).inflate(R.layout.activity_question_details_header,
                null);
        llImages = (LinearLayout) view.findViewById(R.id.ll_images);
        tvQuestionTitleHead = (TextView) view.findViewById(R.id.tv_question_title_head);
        tvTimeHead = (TextView) view.findViewById(R.id.tv_time_head);
        tvContentHead = (TextView) view.findViewById(R.id.tv_content_head);
        ivContentHead = (ImageView) view.findViewById(R.id.iv_content_head);
        tvSubjectHead = (TextView) view.findViewById(R.id.tv_subject_head);
        tvNumberHead = (TextView) view.findViewById(R.id.tv_number_head);

        lvQuestionDetails.addHeaderView(view);

        btnAswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //参与回答
                if (BaseApplication.getUserData().isTourist == 1) {
                    Utils.gotoLogin(QuestionDetailsActivity.this);
                    return;
                }
                Intent intent = new Intent(QuestionDetailsActivity.this, AnswerPublishActivity
                        .class);
                intent.putExtra("questionId", questionId);
                startActivityForResult(intent, 0);
            }
        });
        ivContentHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionDetail.imgVos != null && questionDetail.imgVos.size() >= 1) {
                    ArrayList<String> imgUrls = new ArrayList<>();
                    for (int i = 0; i < questionDetail.imgVos.size(); i++) {
                        imgUrls.add(questionDetail.imgVos.get(i).u);
                    }
                    Intent intent = new Intent(QuestionDetailsActivity.this, EditImageActivity
                            .class);
                    intent.putStringArrayListExtra(EXTRA_PICTURE_PATHS, imgUrls);
                    intent.putExtra(EXTRA_PICTURE_POSITION, 0);
                    intent.putExtra(EditImageActivity.EXTRA_PICTURE_SELECT_READ_ONLY, true);
                    startActivity(intent);
                }
            }
        });
    }

    private void initTitle() {
        titleBuilder = new TitleBuilder(this);
        titleBuilder.setLeftImageRes(R.mipmap.fanhui)
                .setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("问题详情");
    }

    private void showQuestion() {
        final QuestionDetailService service = new QuestionDetailService(QuestionDetailsActivity
                .this);
        service.setCallback(new IOpenApiDataServiceCallback<QuestionDetailResponse>() {

            @Override
            public void onGetData(QuestionDetailResponse data) {
                questionDetail = data.data.questionDetail;
                tvQuestionTitleHead.setText(data.data.questionDetail.title + "");
                tvContentHead.setText(data.data.questionDetail.content + "");
                tvTimeHead.setText(data.data.questionDetail.releaseTime + "");
                tvSubjectHead.setText(data.data.questionDetail.gradeSubject + "");
                tvNumberHead.setText("共" + data.data.questionDetail.answerCount + "条回答");
             //
                if (null == data.data.questionDetail.imgVos || data.data
                        .questionDetail.imgVos.size() == 0) {
                    ivContentHead.setVisibility(View.GONE);
                    llImages.setVisibility(View.GONE);
                } else {
                    List<QuestionDetailResponse.DataBean.ImgVoBean> imgVos = data.data
                            .questionDetail.imgVos;
                    if (imgVos.size() == 1) {
                        ivContentHead.setVisibility(View.VISIBLE);
                        llImages.setVisibility(View.GONE);
                        Glide.with(QuestionDetailsActivity.this).load(data.data
                                .questionDetail.imgVos.get(0).u).placeholder(R.color
                                .colorBackgound).into(ivContentHead);
                    } else {
                        llImages.setVisibility(View.VISIBLE);
                        ivContentHead.setVisibility(View.GONE);
                        displayImages(llImages, imgVos);
                    }
                }
                if (data.data.questionDetail.canRemove == 1) {
                    titleBuilder.setRightImageRes(R.mipmap.shanchu_haoyou)
                            .setRightTextOrImageListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DeleteCommentDialog dialog = new DeleteCommentDialog();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("text", "提问");
                                    dialog.setArguments(bundle);
                                    dialog.setOnBack(new DeleteCommentDialog.OnBack() {
                                        @Override
                                        public void click(int btn) {
                                            deleteQuestion(questionId);
                                        }
                                    });
                                    dialog.show(QuestionDetailsActivity.this
                                            .getSupportFragmentManager(), "delete");
                                }
                            });
                } else {
                    titleBuilder.setRightImageRes(R.mipmap.shanchu_haoyou)
                            .setRightTextOrImageListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ReportDialog dialog = new ReportDialog();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("text", "提问");
                                    dialog.setArguments(bundle);
                                    dialog.setOnBack(new ReportDialog.OnBack() {
                                        @Override
                                        public void click(int btn) {
                                            if (BaseApplication.getUserData().isTourist == 1) {
                                                Utils.gotoLogin(QuestionDetailsActivity.this);
                                                return;
                                            }
                                            Intent intent = new Intent(QuestionDetailsActivity.this, ReportActivity.class);
                                            intent.putExtra("complaintId", questionId);
                                            intent.putExtra("complaintType", 1);
                                            startActivity(intent);
                                        }
                                    });
                                    dialog.show(QuestionDetailsActivity.this
                                            .getSupportFragmentManager(), "report");
                                }
                            });
                }
                qid = data.data.questionDetail.qid;
                showAnswers(qid);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(QuestionDetailsActivity.this, errorMsg);
            }
        });
        service.postLogined("qid=" + questionId, false);
    }

    private void deleteQuestion(int questionId) {
        final DeleteQuestionService service = new DeleteQuestionService(QuestionDetailsActivity
                .this);
        service.setCallback(new IOpenApiDataServiceCallback() {

            @Override
            public void onGetData(Object data) {
                UIUtils.showToast(QuestionDetailsActivity.this, "删除成功");
                Intent intent = new Intent();
                QuestionDetailsActivity.this.setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("qid=" + questionId, false);
    }

    private void showAnswers(int qid) {
        final QuestionDetailAnswersService service = new QuestionDetailAnswersService
                (QuestionDetailsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<QuestionDetailAnswerResponse>() {

            @Override
            public void onGetData(QuestionDetailAnswerResponse data) {

                if (data.data.answerList != null && data.data.answerList.size() != 0) {
                    answerList.clear();
                    answerList.addAll(data.data.answerList);
                    questionAdapter = new QuestionDetailsAdapter();
                    lvQuestionDetails.setAdapter(questionAdapter);
                    pullToRefreshLayout.notifyData(data.data.nextPageNo,
                            data.data.answerList, false);

                    lvQuestionDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            if (position == 0 || position == answerList.size() + 1) {
                                return;
                            } else {
                                    Intent intent = new Intent(QuestionDetailsActivity.this,
                                            AnswerDetailsActivity.class);
                                    intent.putExtra("answerId", answerList.get(position - 1)
                                            .answerId);
                                    startActivityForResult(intent, 10);

                            }
                        }
                    });
                } else {
                    QuestionDetailAnswerResponse.DataBean.AnswerListBean answerListBean = new
                            QuestionDetailAnswerResponse.DataBean.AnswerListBean(0, 0, null, null,
                            null, null, null, -1, null, null);
                    answerList.clear();
                    if (answerList == null || answerList.size() == 0) {
                        answerList.add(answerListBean);
                    }
                    questionAdapter = new QuestionDetailsAdapter();
                    lvQuestionDetails.setAdapter(questionAdapter);
                    pullToRefreshLayout.notifyData(0,
                            answerList, false);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(QuestionDetailsActivity.this, errorMsg);
            }
        });
        service.postLogined("qid=" + qid, false);
    }

    /**
     * 展示题目描述图
     *
     * @param llImages
     */
    private void displayImages(LinearLayout llImages, List<QuestionDetailResponse.DataBean
            .ImgVoBean> imgVos) {
        llImages.removeAllViews();
        int col = imgVos.size() / 3;
        if (0 == imgVos.size() % 3) {
            col -= 1;
        }
        for (int i = 0; i <= col; i++) {
            String url1 = imgVos.get(0 + i * 3).u;
            String url2 = "";
            if (imgVos.size() > 1 + i * 3) {
                url2 = imgVos.get(1 + i * 3).u;
            }
            String url3 = "";
            if (imgVos.size() > 2 + i * 3) {
                url3 = imgVos.get(2 + i * 3).u;
            }
            View view = generateOneLine(url1, url2, url3);
            llImages.addView(view);
        }
    }

    private View generateOneLine(final String url1, final String url2, final String url3) {
        LinearLayout line1 = (LinearLayout) View.inflate(this, R.layout.layout_images_line, null);
        if (!TextUtils.isEmpty(url1)) {
            ImageView iv1 = (ImageView) line1.findViewById(R.id.iv_1);
            iv1.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(url1).placeholder(R.color.color_background)
                    .into(iv1);
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (questionDetail.imgVos != null && questionDetail.imgVos.size() > 1) {
                        int position = 0;
                        ArrayList<String> imgUrls = new ArrayList<>();
                        for (int i = 0; i < questionDetail.imgVos.size(); i++) {
                            if (url1.equals(questionDetail.imgVos.get(i).u)) {
                                position = i;
                            }
                            imgUrls.add(questionDetail.imgVos.get(i).u);
                        }
                        Intent intent = new Intent(QuestionDetailsActivity.this,
                                EditImageActivity.class);
                        intent.putStringArrayListExtra(EXTRA_PICTURE_PATHS, imgUrls);
                        intent.putExtra(EXTRA_PICTURE_POSITION, position);
                        intent.putExtra(EditImageActivity.EXTRA_PICTURE_SELECT_READ_ONLY, true);
                        startActivity(intent);
                    }
                }
            });
        }
        if (!TextUtils.isEmpty(url2)) {
            ImageView iv2 = (ImageView) line1.findViewById(R.id.iv_2);
            iv2.setVisibility(View.VISIBLE);
            Glide.with(this).load(url2).into(iv2);
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (questionDetail.imgVos != null && questionDetail.imgVos.size() > 1) {
                        int position = 0;
                        ArrayList<String> imgUrls = new ArrayList<>();
                        for (int i = 0; i < questionDetail.imgVos.size(); i++) {
                            if (url2.equals(questionDetail.imgVos.get(i).u)) {
                                position = i;
                            }
                            imgUrls.add(questionDetail.imgVos.get(i).u);
                        }
                        Intent intent = new Intent(QuestionDetailsActivity.this,
                                EditImageActivity.class);
                        intent.putStringArrayListExtra(EXTRA_PICTURE_PATHS, imgUrls);
                        intent.putExtra(EXTRA_PICTURE_POSITION, position);
                        intent.putExtra(EditImageActivity.EXTRA_PICTURE_SELECT_READ_ONLY, true);
                        startActivity(intent);
                    }
                }
            });
        }
        if (!TextUtils.isEmpty(url3)) {
            ImageView iv3 = (ImageView) line1.findViewById(R.id.iv_3);
            iv3.setVisibility(View.VISIBLE);
            Glide.with(this).load(url3).into(iv3);
            iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (questionDetail.imgVos != null && questionDetail.imgVos.size() > 1) {
                        int position = 0;
                        ArrayList<String> imgUrls = new ArrayList<>();
                        for (int i = 0; i < questionDetail.imgVos.size(); i++) {
                            if (url3.equals(questionDetail.imgVos.get(i).u)) {
                                position = i;
                            }
                            imgUrls.add(questionDetail.imgVos.get(i).u);
                        }
                        Intent intent = new Intent(QuestionDetailsActivity.this,
                                EditImageActivity.class);
                        intent.putStringArrayListExtra(EXTRA_PICTURE_PATHS, imgUrls);
                        intent.putExtra(EXTRA_PICTURE_POSITION, position);
                        intent.putExtra(EditImageActivity.EXTRA_PICTURE_SELECT_READ_ONLY, true);
                        startActivity(intent);
                    }
                }
            });
        }
        return line1;
    }

    private void upDataAnswers(int qid, int nextPageNo) {
        if (TextUtils.isEmpty(nextPageNo + "") || nextPageNo == 0) {
            pullToRefreshLayout.notifyData(0, null, true);
            return;
        }
        final QuestionDetailAnswersService service = new QuestionDetailAnswersService
                (QuestionDetailsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<QuestionDetailAnswerResponse>() {
            @Override
            public void onGetData(QuestionDetailAnswerResponse data) {
                answerList.addAll(data.data.answerList);
                if (TextUtils.isEmpty(data.data.nextPageNo + "") || data.data.nextPageNo == 0) {
                    pullToRefreshLayout.notifyData(0, null, true);
                } else {
                    pullToRefreshLayout.notifyData(data.data.nextPageNo,
                            data.data.answerList, true);
                }
                questionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(QuestionDetailsActivity.this, errorMsg);
            }
        });
        service.postLogined("qid=" + qid + "&nextPageNo=" + nextPageNo, false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_question_details;
    }


    class QuestionDetailsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return answerList.size();
        }

        @Override
        public QuestionDetailAnswerResponse.DataBean.AnswerListBean getItem(int position) {
            return answerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(QuestionDetailsActivity.this,
                        R.layout.list_item_question_details, null);
                holder = new ViewHolder();
                holder.ivHeader = (RoundImageView) convertView.findViewById(R.id.iv_header);
                holder.ivContent = (ImageView) convertView.findViewById(R.id.iv_content);
                holder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
                holder.tvQuestionTitle = (TextView) convertView.findViewById(R.id
                        .tv_question_title);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                holder.tvPraiseNumber = (TextView) convertView.findViewById(R.id.tv_praise_number);
                holder.ivPraise = (ImageView) convertView.findViewById(R.id.iv_praise);
                holder.tvCommentNumber = (TextView) convertView.findViewById(R.id
                        .tv_comment_number);
                holder.llPraise = (LinearLayout) convertView.findViewById(R.id.ll_praise);
                holder.view1 = convertView.findViewById(view1);
                holder.view2 = convertView.findViewById(R.id.view2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final QuestionDetailAnswerResponse.DataBean.AnswerListBean item = getItem(position);
            if (TextUtils.isEmpty(item.nickname)) {
                holder.rl.setVisibility(View.GONE);
                holder.tvQuestionTitle.setText("");
                holder.tvCommentNumber.setText("");
                holder.tvPraiseNumber.setText("");
                holder.tvTime.setText("");
                holder.tvContent.setText("");
                holder.llPraise.setVisibility(View.GONE);
                holder.view1.setVisibility(View.GONE);
                holder.view2.setVisibility(View.GONE);
            } else {
                holder.rl.setVisibility(View.VISIBLE);
                holder.view1.setVisibility(View.VISIBLE);
                holder.view2.setVisibility(View.VISIBLE);
                holder.llPraise.setVisibility(View.VISIBLE);
                holder.tvCommentNumber.setText(item.commentCount + "条评论");
                holder.tvQuestionTitle.setText(item.nickname + "的回答");
                holder.tvPraiseNumber.setText(item.getLikeNum() + "");
                holder.tvTime.setText(item.answerTime + "");
                holder.tvContent.setText(item.content + "");
            }

            if (null != item.imgVo) {
                holder.ivContent.setVisibility(View.VISIBLE);
                Glide.with(QuestionDetailsActivity.this).load(item.imgVo.u).placeholder(R.color
                        .colorBackgound).into(holder.ivContent);
            } else {
                holder.ivContent.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.portrait)) {
                holder.ivHeader.setVisibility(View.VISIBLE);
                Glide.with(QuestionDetailsActivity.this).load(item.portrait).into(holder.ivHeader);
            } else {
                holder.ivHeader.setVisibility(View.GONE);
            }
            if (item.isLike()) {
                holder.ivPraise.setImageResource(R.mipmap.question_dianzan);
            } else {
                if (item.isLike == 0) {
                    holder.ivPraise.setImageResource(R.mipmap.question_weidianzan);
                } else {
                    holder.ivPraise.setImageResource(R.color.transparent);
                }
            }
            holder.llPraise.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (BaseApplication.getUserData().isTourist == 1) {
                        Utils.gotoLogin(QuestionDetailsActivity.this);
                        return;
                    }
                    boolean flag = item.isLike();
                    if (flag) {
                        unDianZan(item, holder.ivPraise, holder.tvPraiseNumber);
                    } else {
                        dianZan(item, holder.ivPraise, holder.tvPraiseNumber);
                    }
                    item.setLike(flag ? 0 : 1);
                    AnimationTools.scale(holder.ivPraise);

                }
            });
            return convertView;
        }
    }

    public static class AnimationTools {
        public static void scale(View v) {
            ScaleAnimation anim = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            anim.setDuration(300);
            v.startAnimation(anim);

        }
    }

    private void dianZan(final QuestionDetailAnswerResponse.DataBean.AnswerListBean item, final
    ImageView ivPraise, final TextView tvPraiseNumber) {
        if (isOnNetwork.get()) {
            return;
        }
        isOnNetwork.set(true);
        final PraiseService service = new PraiseService(QuestionDetailsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback() {

            @Override
            public void onGetData(Object data) {
                ivPraise.setImageResource(R.mipmap.question_dianzan);
                int likeNum = 0;
                try {
                    likeNum = Integer.parseInt(item.getLikeNum());
                    item.setLikeNum(likeNum + 1 + "");
                } catch (Exception e) {
                    item.setLikeNum(item.getLikeNum() + "");
                }
                tvPraiseNumber.setText(item.getLikeNum() + "");
                isOnNetwork.set(false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(QuestionDetailsActivity.this, errorMsg);
                isOnNetwork.set(false);
            }
        });
        service.postLogined("answerId=" + item.answerId, false);
    }

    private void unDianZan(final QuestionDetailAnswerResponse.DataBean.AnswerListBean item, final ImageView ivPraise, final TextView tvPraiseNumber) {
        if (isOnNetwork.get()) {
            return;
        }
        isOnNetwork.set(true);
        final UnPraiseService service = new UnPraiseService(QuestionDetailsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback() {

            @Override
            public void onGetData(Object data) {
                ivPraise.setImageResource(R.mipmap.question_weidianzan);
                int likeNum = 0;
                try {
                    likeNum = Integer.parseInt(item.getLikeNum());
                    item.setLikeNum(likeNum - 1 + "");
                } catch (Exception e) {
                    item.setLikeNum(item.getLikeNum() + "");
                }
                tvPraiseNumber.setText(item.getLikeNum() + "");
                isOnNetwork.set(false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(QuestionDetailsActivity.this, errorMsg);
                isOnNetwork.set(false);
            }
        });
        service.postLogined("answerId=" + item.answerId, false);
    }


    static class ViewHolder {
        TextView tvQuestionTitle;
        TextView tvTime;
        TextView tvContent;
        TextView tvPraiseNumber;
        TextView tvCommentNumber;
        ImageView ivContent;
        RoundImageView ivHeader;
        RelativeLayout rl;
        ImageView ivPraise;
        View view1;
        View view2;
        LinearLayout llPraise;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || requestCode == 0) {
            showQuestion();
        }
    }
}
