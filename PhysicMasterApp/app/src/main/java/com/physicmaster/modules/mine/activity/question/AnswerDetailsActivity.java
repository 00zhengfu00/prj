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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.discuss.activity.EditImageActivity;
import com.physicmaster.modules.mine.activity.friend.FriendInfoActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.discuss.AnswerDetailResponse;
import com.physicmaster.net.response.discuss.CommentListResponse;
import com.physicmaster.net.service.discuss.AnswerDetailService;
import com.physicmaster.net.service.discuss.CommentListService;
import com.physicmaster.net.service.discuss.CommentService;
import com.physicmaster.net.service.discuss.DeleteAnswerService;
import com.physicmaster.net.service.discuss.DeleteCommentService;
import com.physicmaster.net.service.discuss.PraiseService;
import com.physicmaster.net.service.discuss.UnPraiseService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.CommentDialogFragment;
import com.physicmaster.widget.DeleteCommentDialog;
import com.physicmaster.widget.PullToRefreshLayout;
import com.physicmaster.widget.ReportDialog;
import com.physicmaster.widget.RoundImageView;
import com.physicmaster.widget.TitleBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.physicmaster.modules.discuss.activity.AnswerPublishActivity.EXTRA_PICTURE_PATHS;
import static com.physicmaster.modules.discuss.activity.AnswerPublishActivity.EXTRA_PICTURE_POSITION;

public class AnswerDetailsActivity extends BaseActivity {

    private TextView tvQuestionTitle;
    private RoundImageView ivHeader;
    private TextView tvUserName;
    private TextView tvContent;
    private ListView lvComment;
    private RelativeLayout rlPraise;
    private TextView tvComment;
    private int answerId, questionId, commentId;
    private TextView tvPraise;
    private ImageView ivPraise;
    private TitleBuilder titleBuilder;
    private LinearLayout llImages;
    private ImageView ivContentHead;
    private RelativeLayout rlComment;
    private TextView tvNumbers;
    private List<CommentListResponse.DataBean.CommentListBean> mCommentList;
    private CommentAdapter commentAdapter;
    private PullToRefreshLayout pullToRefreshLayout;
    private AnswerDetailResponse.DataBean.AnswerDetailBean answerDetail;
    private View view2;
    private boolean flag;
    private AtomicBoolean isOnNetwork = new AtomicBoolean(false);
    private FrameLayout flImages;

    @Override
    protected void findViewById() {

        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showAnswer();
            }

            @Override
            public void onPullup(int maxId) {
                upDataComment(maxId);
            }
        });

        rlPraise = (RelativeLayout) findViewById(R.id.rl_praise);
        tvPraise = (TextView) findViewById(R.id.tv_praise);
        ivPraise = (ImageView) findViewById(R.id.iv_praise);
        rlComment = (RelativeLayout) findViewById(R.id.rl_comment);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        mCommentList = new ArrayList<>();
        answerId = getIntent().getIntExtra("answerId", 0);
        questionId = getIntent().getIntExtra("questionId", 0);
        commentId = getIntent().getIntExtra("commentId", 0);
        lvComment = pullToRefreshLayout.getListView();
        initTitle();
    }


    @Override
    protected void onResume() {
        showAnswer();
        super.onResume();
    }

    @Override
    protected void initView() {
        View view = LayoutInflater.from(AnswerDetailsActivity.this).inflate(R.layout
                        .activity_answer_details_header,
                null);
        view.findViewById(R.id.tv_question_title).setOnClickListener(v -> {
            if (questionId != 0) {
                Intent intent = new Intent(AnswerDetailsActivity.this, QuestionDetailsActivity.class);
                intent.putExtra("questionId", questionId);
                startActivity(intent);
            }
        });
        llImages = (LinearLayout) view.findViewById(R.id.ll_images);
        flImages = (FrameLayout) view.findViewById(R.id.fl);
        tvQuestionTitle = (TextView) view.findViewById(R.id.tv_question_title);
        ivHeader = (RoundImageView) view.findViewById(R.id.iv_header);
        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        ivContentHead = (ImageView) view.findViewById(R.id.iv_content_head);

        lvComment.addHeaderView(view);

        view2 = LayoutInflater.from(this).inflate(R.layout.activity_answer_details_header2,
                null);
        tvNumbers = (TextView) view2.findViewById(R.id.tv_number);
        lvComment.addHeaderView(view2);

        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.getUserData().isTourist == 1) {
                    Utils.gotoLogin(AnswerDetailsActivity.this);
                    return;
                }
                CommentDialogFragment commentDialog = new CommentDialogFragment();
                commentDialog.setOnBack(new CommentDialogFragment.OnBack() {
                    @Override
                    public void click(String content) {
                        if (TextUtils.isEmpty(content)) {
                            UIUtils.showToast(AnswerDetailsActivity.this, "输入不能为空!");
                            return;
                        }
                        comment2Answer(content, 0);
                    }
                });
                commentDialog.show(AnswerDetailsActivity.this.getSupportFragmentManager(),
                        "comment");
            }
        });

    }

    private void deletecomment(int commentId) {
        final DeleteCommentService service = new DeleteCommentService(AnswerDetailsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback() {

            @Override
            public void onGetData(Object data) {
                UIUtils.showToast(AnswerDetailsActivity.this, "删除成功");
                showAnswer();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(AnswerDetailsActivity.this, errorMsg);
            }
        });
        service.postLogined("commentId=" + commentId, false);
    }

    private void comment2Answer(String content, int commentId) {
        final CommentService service = new CommentService(AnswerDetailsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback() {

            @Override
            public void onGetData(Object data) {
                UIUtils.showToast(AnswerDetailsActivity.this, "评论成功");
                //                commentAdapter.notifyDataSetChanged();
                showComment(1);

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(AnswerDetailsActivity.this, errorMsg);
            }
        });
        String params = null;
        try {
            content = URLEncoder.encode(content, Constant.CHARACTER_ENCODING);
            if (!TextUtils.isEmpty(commentId + "") && commentId != 0) {
                params = "answerId=" + answerId + "&content=" + content + "&replyCommentId=" +
                        commentId;
            } else {
                params = "answerId=" + answerId + "&content=" + content;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        service.postLogined(params, false);

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
                .setMiddleTitleText("回答详情");
    }

    private void showAnswer() {
        final AnswerDetailService service = new AnswerDetailService(AnswerDetailsActivity
                .this);
        service.setCallback(new IOpenApiDataServiceCallback<AnswerDetailResponse>() {

            @Override
            public void onGetData(final AnswerDetailResponse data) {
                answerDetail = data.data.answerDetail;
                tvQuestionTitle.setText(answerDetail.title + "");
                tvContent.setText(answerDetail.content + "");
                tvComment.setText("评论 (" + answerDetail.commentCount + ")");
                tvPraise.setText("点赞 (" + answerDetail.likeCount + ")");
                tvUserName.setText(answerDetail.nickname + "");

                if (!TextUtils.isEmpty(answerDetail.portrait)) {
                    Glide.with(AnswerDetailsActivity.this).load(answerDetail.portrait).into(ivHeader);
                    ivHeader.setOnClickListener(v -> {
                        Intent intent = new Intent(AnswerDetailsActivity.this, FriendInfoActivity.class);
                        intent.putExtra("dtUserId", answerDetail.dtUserId + "");
                        startActivity(intent);
                    });
                    tvUserName.setOnClickListener(v -> {
                        Intent intent = new Intent(AnswerDetailsActivity.this, FriendInfoActivity.class);
                        intent.putExtra("dtUserId", answerDetail.dtUserId + "");
                        startActivity(intent);
                    });
                }
                if (answerDetail.isLike()) {
                    ivPraise.setImageResource(R.mipmap.answer_dianzan);
                } else {
                    ivPraise.setImageResource(R.mipmap.answer_weidianzan);
                }
                rlPraise.setOnClickListener(v -> {
                    if (BaseApplication.getUserData().isTourist == 1) {
                        Utils.gotoLogin(AnswerDetailsActivity.this);
                        return;
                    }
                    boolean flag = answerDetail.isLike();
                    if (flag) {
                        unDianZan(answerId, ivPraise);
                    } else {
                        dianZan(answerId, ivPraise);
                    }
                    answerDetail.setLike(flag ? 0 : 1);
                    AnimationTools.scale(ivPraise);
                });

                if (null == answerDetail.imgVos || answerDetail.imgVos.size() == 0) {
                    ivContentHead.setVisibility(View.GONE);
                    llImages.setVisibility(View.GONE);
                    flImages.setVisibility(View.GONE);
                } else {
                    List<AnswerDetailResponse.DataBean.ImgVoBean> imgVos = answerDetail.imgVos;
                    if (imgVos.size() == 1) {
                        ivContentHead.setVisibility(View.VISIBLE);
                        llImages.setVisibility(View.GONE);
                        flImages.setVisibility(View.VISIBLE);
                        ivContentHead.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (answerDetail.imgVos != null && answerDetail.imgVos.size() > 0) {
                                    ArrayList<String> imgUrls = new ArrayList<>();
                                    for (int i = 0; i < answerDetail.imgVos.size(); i++) {
                                        imgUrls.add(answerDetail.imgVos.get(i).u);
                                    }
                                    Intent intent = new Intent(AnswerDetailsActivity.this,
                                            EditImageActivity
                                                    .class);
                                    intent.putStringArrayListExtra(EXTRA_PICTURE_PATHS, imgUrls);
                                    intent.putExtra(EXTRA_PICTURE_POSITION, 0);
                                    intent.putExtra(EditImageActivity
                                            .EXTRA_PICTURE_SELECT_READ_ONLY, true);
                                    startActivity(intent);
                                }
                            }
                        });
                        Glide.with(AnswerDetailsActivity.this).load(answerDetail.imgVos.get(0).u)
                                .into(ivContentHead);
                    } else {
                        llImages.setVisibility(View.VISIBLE);
                        flImages.setVisibility(View.VISIBLE);
                        ivContentHead.setVisibility(View.GONE);
                        displayImages(llImages, imgVos);
                    }
                }
                if (answerDetail.canRemove == 1) {
                    titleBuilder.setRightImageRes(R.mipmap.shanchu_haoyou)
                            .setRightTextOrImageListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DeleteCommentDialog dialog = new DeleteCommentDialog();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("text", "回答");
                                    dialog.setArguments(bundle);
                                    dialog.setOnBack(new DeleteCommentDialog.OnBack() {
                                        @Override
                                        public void click(int btn) {
                                            deleteAnswer(answerId);
                                        }
                                    });
                                    dialog.show(AnswerDetailsActivity.this
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
                                                Utils.gotoLogin(AnswerDetailsActivity.this);
                                                return;
                                            }
                                            Intent intent = new Intent(AnswerDetailsActivity.this, ReportActivity.class);
                                            intent.putExtra("complaintId", answerId);
                                            intent.putExtra("complaintType", 2);
                                            startActivity(intent);
                                        }
                                    });
                                    dialog.show(AnswerDetailsActivity.this
                                            .getSupportFragmentManager(), "report");
                                }
                            });
                }
                showComment(0);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(AnswerDetailsActivity.this, errorMsg);
            }
        });
        service.postLogined("answerId=" + answerId, false);
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

    /**
     * 展示题目描述图
     *
     * @param llImages
     */
    private void displayImages(LinearLayout llImages, List<AnswerDetailResponse.DataBean
            .ImgVoBean> imgVos) {
        llImages.removeAllViews();
        for (int i = 0; i < imgVos.size(); i++) {
            String url1 = imgVos.get(i).u;
            View view = generateOneLine(url1);
            llImages.addView(view);
        }
    }

    private View generateOneLine(final String url1) {
        LinearLayout line1 = (LinearLayout) View.inflate(this, R.layout.layout_one_image, null);
        if (!TextUtils.isEmpty(url1)) {
            ImageView iv1 = (ImageView) line1.findViewById(R.id.iv_1);
            Glide.with(this).load(url1).into(iv1);
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answerDetail.imgVos != null && answerDetail.imgVos.size() > 1) {
                        int position = 0;
                        ArrayList<String> imgUrls = new ArrayList<>();
                        for (int i = 0; i < answerDetail.imgVos.size(); i++) {
                            if (url1.equals(answerDetail.imgVos.get(i).u)) {
                                position = i;
                            }
                            imgUrls.add(answerDetail.imgVos.get(i).u);
                        }
                        Intent intent = new Intent(AnswerDetailsActivity.this, EditImageActivity
                                .class);
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

    private void unDianZan(int answerId, final ImageView ivPraise) {
        if (isOnNetwork.get()) {
            return;
        }
        isOnNetwork.set(true);
        final UnPraiseService service = new UnPraiseService(AnswerDetailsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback() {

            @Override
            public void onGetData(Object data) {
                ivPraise.setImageResource(R.mipmap.answer_weidianzan);
                int likeNum = 0;
                try {
                    likeNum = Integer.parseInt(answerDetail.getLikeNum());
                    answerDetail.setLikeNum(likeNum - 1 + "");
                } catch (Exception e) {
                    answerDetail.setLikeNum(answerDetail.getLikeNum() + "");
                }
                tvPraise.setText("点赞 (" + answerDetail.getLikeNum() + ")");
                isOnNetwork.set(false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(AnswerDetailsActivity.this, errorMsg);
                isOnNetwork.set(false);
            }
        });
        service.postLogined("answerId=" + answerId, false);
    }

    private void dianZan(int answerId, final ImageView ivPraise) {
        if (isOnNetwork.get()) {
            return;
        }
        isOnNetwork.set(true);
        final PraiseService service = new PraiseService(AnswerDetailsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback() {

            @Override
            public void onGetData(Object data) {
                ivPraise.setImageResource(R.mipmap.answer_dianzan);
                int likeNum = 0;
                try {
                    likeNum = Integer.parseInt(answerDetail.getLikeNum());
                    answerDetail.setLikeNum(likeNum + 1 + "");
                } catch (Exception e) {
                    answerDetail.setLikeNum(answerDetail.getLikeNum() + "");
                }
                tvPraise.setText("点赞 (" + answerDetail.getLikeNum() + ")");
                isOnNetwork.set(false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(AnswerDetailsActivity.this, errorMsg);
                isOnNetwork.set(false);
            }
        });
        service.postLogined("answerId=" + answerId, false);
    }

    private void deleteAnswer(int answerId) {
        final DeleteAnswerService service = new DeleteAnswerService(AnswerDetailsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback() {

            @Override
            public void onGetData(Object data) {
                UIUtils.showToast(AnswerDetailsActivity.this, "删除成功");
                Intent intent = new Intent();
                AnswerDetailsActivity.this.setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(AnswerDetailsActivity.this, errorMsg);
            }
        });
        service.postLogined("answerId=" + answerId, false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_answer_details;
    }

    private void showComment(final int flag) {
        final CommentListService service = new CommentListService(AnswerDetailsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<CommentListResponse>() {

            @Override
            public void onGetData(final CommentListResponse data) {
                if (data.data.commentList != null && data.data.commentList.size() != 0) {
                    mCommentList.clear();
                    mCommentList.addAll(data.data.commentList);
                    commentAdapter = new CommentAdapter();
                    lvComment.setAdapter(commentAdapter);
                    pullToRefreshLayout.notifyData(data.data.nextCommentId,
                            data.data.commentList, false);
                    tvNumbers.setText("最新评论 (" + mCommentList.size() + ")");
                    if (flag == 1) {
                        lvComment.setSelection(1);
                    }
                    lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int
                                position, long id) {
                            if (BaseApplication.getUserData().isTourist == 1) {
                                Utils.gotoLogin(AnswerDetailsActivity.this);
                                return;
                            }
                            if (position == 0 || position == 1 || position == mCommentList.size()
                                    + 2) {
                                return;
                            } else {
                                CommentDialogFragment commentDialog = new CommentDialogFragment();
                                if (TextUtils.isEmpty(mCommentList.get(position - 2)
                                        .nickname)) {
                                    return;
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("content", mCommentList.get(position -
                                            2).nickname);
                                    commentDialog.setArguments(bundle);
                                }
                                commentDialog.setOnBack(new CommentDialogFragment.OnBack() {
                                    @Override
                                    public void click(String content) {
                                        if (TextUtils.isEmpty(content)) {
                                            UIUtils.showToast(AnswerDetailsActivity.this,
                                                    "输入不能为空!");
                                            return;
                                        }
                                        comment2Answer(content, mCommentList.get(position -
                                                2).commentId);
                                    }
                                });

                                commentDialog.show(AnswerDetailsActivity.this
                                        .getSupportFragmentManager(), "comment");
                            }


                        }
                    });
                    lvComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, final
                        int position, long id) {
                            if (position == 0 || position == 1 || position == mCommentList.size()
                                    + 3) {
                                return false;
                            } else {
                                if (mCommentList.get(position - 2).canRemove == 1) {
                                    DeleteCommentDialog dialog = new DeleteCommentDialog();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("text", "评论");
                                    dialog.setArguments(bundle);
                                    dialog.setOnBack(new DeleteCommentDialog.OnBack() {
                                        @Override
                                        public void click(int btn) {

                                            deletecomment(mCommentList.get(position - 2).commentId);
                                        }
                                    });
                                    dialog.show(AnswerDetailsActivity.this
                                            .getSupportFragmentManager(), "delete");
                                } else {
                                    UIUtils.showToast(AnswerDetailsActivity.this,
                                            "别人的评论，是不可以删除的哦！");
                                    return true;
                                }

                            }

                            return true;

                        }
                    });

                } else {
                    CommentListResponse.DataBean.CommentListBean commentListBean = new
                            CommentListResponse.DataBean.CommentListBean(0, null, null, 0, null,
                            null, null, null);
                    mCommentList.clear();
                    if (mCommentList == null || mCommentList.size() == 0) {
                        mCommentList.add(commentListBean);
                    }
                    commentAdapter = new CommentAdapter();
                    lvComment.setAdapter(commentAdapter);
                    pullToRefreshLayout.notifyData(0,
                            mCommentList, false);
                    tvNumbers.setText("最新评论 (" + 0 + ")");
                }

                if (null != data.data.currentComment) {
                    View view = LayoutInflater.from(AnswerDetailsActivity.this).inflate(R.layout
                                    .list_item_comment2,
                            null);
                    RoundImageView ivHeader = (RoundImageView) view.findViewById(R.id.iv_header);
                    RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl);
                    TextView tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
                    TextView tvComment = (TextView) view.findViewById(R.id.tv_comment);
                    TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
                    TextView tv = (TextView) view.findViewById(R.id.tv);
                    TextView tvUserName2 = (TextView) view.findViewById(R.id.tv_user_name2);
                    TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
                    tvNumber.setText("当前评论");
                    if (!TextUtils.isEmpty(data.data.currentComment.portrait)) {
                        ivHeader.setVisibility(View.VISIBLE);
                        Glide.with(AnswerDetailsActivity.this).load(data.data.currentComment
                                .portrait).into(ivHeader);
                        ivHeader.setOnClickListener(v -> {
                            Intent intent = new Intent(AnswerDetailsActivity.this, FriendInfoActivity.class);
                            intent.putExtra("dtUserId", data.data.currentComment.dtUserId + "");
                            startActivity(intent);
                        });
                        tvUserName.setOnClickListener(v -> {
                            Intent intent = new Intent(AnswerDetailsActivity.this, FriendInfoActivity.class);
                            intent.putExtra("dtUserId", data.data.currentComment.dtUserId + "");
                            startActivity(intent);
                        });
                    } else {
                        ivHeader.setVisibility(View.GONE);
                    }
                    if (TextUtils.isEmpty(data.data.currentComment.nickname)) {
                        rl.setVisibility(View.GONE);
                        tvTime.setText("");
                        tvComment.setText("");
                        tvUserName.setText("");
                    } else {
                        rl.setVisibility(View.VISIBLE);
                        tvTime.setText(data.data.currentComment.replyTime + "");
                        tvComment.setText(data.data.currentComment.content + "");
                        tvUserName.setText(data.data.currentComment.nickname + "");
                        if (TextUtils.isEmpty(data.data.currentComment.replyNickname)) {
                            tv.setVisibility(View.GONE);
                            tvUserName2.setVisibility(View.GONE);
                        } else {
                            tvUserName2.setText(data.data.currentComment.replyNickname + "");
                            tv.setText("回复");
                            tv.setVisibility(View.VISIBLE);
                        }
                    }

                    if (lvComment.getHeaderViewsCount() == 2) {
                        lvComment.removeHeaderView(view2);
                        lvComment.addHeaderView(view);
                        lvComment.addHeaderView(view2);
                    }
                    lvComment.setSelection(1);

                    lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int
                                position, long id) {
                            if (BaseApplication.getUserData().isTourist == 1) {
                                Utils.gotoLogin(AnswerDetailsActivity.this);
                                return;
                            }
                            if (position == 0 || position == 2 || position == mCommentList.size()
                                    + 3) {
                                return;
                            } else {
                                if (position == 1) {
                                    CommentDialogFragment commentDialog = new CommentDialogFragment();

                                    if (TextUtils.isEmpty(data.data.currentComment.nickname)) {
                                        return;
                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("content", data.data.currentComment
                                                .nickname);
                                        commentDialog.setArguments(bundle);
                                    }
                                    commentDialog.setOnBack(new CommentDialogFragment.OnBack() {
                                        @Override
                                        public void click(String content) {
                                            if (TextUtils.isEmpty(content)) {
                                                UIUtils.showToast(AnswerDetailsActivity.this,
                                                        "输入不能为空!");
                                                return;
                                            }
                                            comment2Answer(content, data.data.currentComment
                                                    .commentId);
                                        }
                                    });
                                    commentDialog.show(AnswerDetailsActivity.this
                                            .getSupportFragmentManager(), "comment");

                                } else {

                                    CommentDialogFragment commentDialog = new CommentDialogFragment();

                                    if (TextUtils.isEmpty(mCommentList.get(position - 3)
                                            .nickname)) {
                                        return;
                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("content", mCommentList.get(position
                                                - 3).nickname);
                                        commentDialog.setArguments(bundle);
                                    }
                                    commentDialog.setOnBack(new CommentDialogFragment.OnBack() {
                                        @Override
                                        public void click(String content) {

                                            if (TextUtils.isEmpty(content)) {
                                                UIUtils.showToast(AnswerDetailsActivity.this,
                                                        "输入不能为空!");
                                                return;
                                            }
                                            comment2Answer(content, mCommentList.get(position
                                                    - 3).commentId);
                                        }
                                    });

                                    commentDialog.show(AnswerDetailsActivity.this
                                            .getSupportFragmentManager(), "comment");


                                }
                            }
                        }

                    });

                    lvComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, final
                        int position, long id) {
                            if (position == 0 || position == 2 || position == mCommentList.size()
                                    + 3) {
                                return false;
                            } else {
                                if (position == 1) {
                                    if (data.data.currentComment.canRemove == 1) {
                                        DeleteCommentDialog dialog = new DeleteCommentDialog();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("text", "评论");
                                        dialog.setArguments(bundle);
                                        dialog.setOnBack(new DeleteCommentDialog.OnBack() {
                                            @Override
                                            public void click(int btn) {
                                                deletecomment(data.data.currentComment.commentId);
                                            }
                                        });
                                        dialog.show(AnswerDetailsActivity.this
                                                .getSupportFragmentManager(), "delete");
                                    } else {
                                        UIUtils.showToast(AnswerDetailsActivity.this,
                                                "别人的评论，是不可以删除的哦！");
                                        return false;
                                    }
                                } else {
                                    if (mCommentList.get(position - 3).canRemove == 1) {
                                        DeleteCommentDialog dialog = new DeleteCommentDialog();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("text", "评论");
                                        dialog.setArguments(bundle);
                                        dialog.setOnBack(new DeleteCommentDialog.OnBack() {
                                            @Override
                                            public void click(int btn) {

                                                deletecomment(mCommentList.get(position - 3)
                                                        .commentId);
                                            }
                                        });
                                        dialog.show(AnswerDetailsActivity.this
                                                .getSupportFragmentManager(), "delete");
                                    } else {
                                        UIUtils.showToast(AnswerDetailsActivity.this,
                                                "别人的评论，是不可以删除的哦！");
                                        return false;
                                    }
                                }
                            }
                            return true;
                        }
                    });
                }

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(AnswerDetailsActivity.this, errorMsg);
            }
        });
        String params = null;
        if (!TextUtils.isEmpty(commentId + "") && commentId != 0)

        {
            params = "answerId=" + answerId + "&commentId=" + commentId;
        } else

        {
            params = "answerId=" + answerId;
        }
        service.postLogined(params, false);
    }

    private void upDataComment(int maxId) {
        final CommentListService service = new CommentListService(AnswerDetailsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<CommentListResponse>() {

            @Override
            public void onGetData(CommentListResponse data) {
                mCommentList.addAll(data.data.commentList);
                pullToRefreshLayout.notifyData(data.data.nextCommentId,
                        data.data.commentList, true);
                commentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(AnswerDetailsActivity.this, errorMsg);
            }
        });
        String params = null;
        if (!TextUtils.isEmpty(commentId + "") && commentId != 0) {
            params = "answerId=" + answerId + "&nextCommentId=" + maxId + "&commentId=" + commentId;
        } else {
            params = "answerId=" + answerId + "&nextCommentId=" + maxId;
        }
        service.postLogined(params, false);
    }

    class CommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCommentList.size();
        }

        @Override
        public CommentListResponse.DataBean.CommentListBean getItem(int position) {
            return mCommentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(AnswerDetailsActivity.this,
                        R.layout.list_item_comment, null);
                holder = new ViewHolder();
                holder.ivHeader = (RoundImageView) convertView.findViewById(R.id.iv_header);
                holder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
                holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
                holder.ivPraise = (ImageView) convertView.findViewById(R.id.iv_praise);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv = (TextView) convertView.findViewById(R.id.tv);
                holder.tvUserName2 = (TextView) convertView.findViewById(R.id.tv_user_name2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final CommentListResponse.DataBean.CommentListBean item = getItem(position);
            if (!TextUtils.isEmpty(item.portrait)) {
                holder.ivHeader.setVisibility(View.VISIBLE);
                Glide.with(AnswerDetailsActivity.this).load(item.portrait).into(holder.ivHeader);
                holder.ivHeader.setOnClickListener(v -> {
                    Intent intent = new Intent(AnswerDetailsActivity.this, FriendInfoActivity.class);
                    intent.putExtra("dtUserId", item.dtUserId + "");
                    startActivity(intent);
                });
                holder.tvUserName.setOnClickListener(v -> {
                    Intent intent = new Intent(AnswerDetailsActivity.this, FriendInfoActivity.class);
                    intent.putExtra("dtUserId", item.dtUserId + "");
                    startActivity(intent);
                });
            } else {
                holder.ivHeader.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(item.nickname)) {
                holder.rl.setVisibility(View.GONE);
                holder.tvTime.setText("");
                holder.tvComment.setText("");
                holder.tvUserName.setText("");
            } else {
                holder.rl.setVisibility(View.VISIBLE);
                holder.tvTime.setText(item.replyTime + "");
                holder.tvComment.setText(item.content + "");
                holder.tvUserName.setText(item.nickname + "");
                if (TextUtils.isEmpty(item.replyNickname)) {
                    holder.tv.setVisibility(View.GONE);
                    holder.tvUserName2.setVisibility(View.GONE);
                } else {
                    holder.tvUserName2.setText(item.replyNickname + "");
                    holder.tv.setText("回复");
                    holder.tv.setVisibility(View.VISIBLE);
                }
            }
            return convertView;
        }
    }

    static class ViewHolder {
        RoundImageView ivHeader;
        RelativeLayout rl;
        TextView tvTime;
        TextView tvUserName;
        TextView tvUserName2;
        TextView tv;
        TextView tvComment;
        ImageView ivPraise;
    }
}
