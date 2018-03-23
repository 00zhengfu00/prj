package com.physicmaster.modules.mine.activity.topicmap;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.modules.ExitDialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.excercise.CommitTopicmapAnswerResponse;
import com.physicmaster.net.response.excercise.GetTopicmapAnalysisResponse;
import com.physicmaster.net.response.excercise.WipeOutTopicmapResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.excercise.CommitTopicmapAnswerService;
import com.physicmaster.net.service.excercise.WipeOutTopicmapService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.FinishView;
import com.physicmaster.widget.ProgressLoadingDialog;

public class TopicmapFinishActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvResidual;
    private TextView tvWipe;
    private TextView tvFinish;
    private FinishView ivFinish;
    private GetTopicmapAnalysisResponse.DataBean dataBean;
    private int topicmap_size;
    private int topicmap_right;
    private String quBatchId;
    private ImageView ivHeads;
    private String anwserJson;
    private int chapterId;

    @Override
    protected void findViewById() {
        findViewById(R.id.tv_close).setOnClickListener(this);
        ivFinish = (FinishView) findViewById(R.id.iv_finish);
        ivHeads = (ImageView) findViewById(R.id.iv_heads);
        tvWipe = (TextView) findViewById(R.id.tv_wipe);
        tvResidual = (TextView) findViewById(R.id.tv_residual);
        findViewById(R.id.btn_wipe).setOnClickListener(this);
        findViewById(R.id.btn_again).setOnClickListener(this);

        Intent intent = getIntent();
        anwserJson = intent.getStringExtra("answerJson");
        chapterId = intent.getIntExtra("chapterId", -1);
        topicmap_size = intent.getIntExtra("quCount", 0);
        topicmap_right = intent.getIntExtra("correctCount", 0);
    }

    @Override
    protected void initView() {
        tvWipe.setText(topicmap_right + "/" + topicmap_size);
        tvResidual.setText((topicmap_size - topicmap_right) + "/" + topicmap_size);
        UserDataResponse.UserDataBean.LoginVoBean mDataBean = BaseApplication.getUserData();
        if (mDataBean == null) {
            gotoLoginActivity();
            return;
        }
        Glide.with(this).load(mDataBean.portrait).placeholder(R.drawable.placeholder_gray).into
                (ivHeads);
    }

    @Override
    protected int getContentLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_topicmap_finish;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wipe:
                summitAnswer2Server();
                break;
            case R.id.btn_again:
                updateExercise();
                break;
            case R.id.tv_close:
                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                    @Override
                    public void ok() {
                        Intent intent = new Intent();
                        intent.putExtra("action", 3);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                break;
        }
    }

    private void updateExercise() {
        if (TextUtils.isEmpty(chapterId + "")) {
            UIUtils.showToast(TopicmapFinishActivity.this, "网络出问题啦，稍等一会哦！");
            return;
        }
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final WipeOutTopicmapService service = new WipeOutTopicmapService(this);
        service.setCallback(new IOpenApiDataServiceCallback<WipeOutTopicmapResponse>() {


            @Override
            public void onGetData(WipeOutTopicmapResponse data) {
                loadingDialog.dismissDialog();
                Intent intent = new Intent();
                intent.putExtra("action", 2);
                intent.putExtra("quBatchId", data.data);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(TopicmapFinishActivity.this, errorMsg);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("chapterId=" + chapterId, false);
    }

    private void summitAnswer2Server() {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final CommitTopicmapAnswerService service = new CommitTopicmapAnswerService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommitTopicmapAnswerResponse>() {
            @Override
            public void onGetData(CommitTopicmapAnswerResponse data) {
                loadingDialog.dismissDialog();
                UIUtils.showToast(TopicmapFinishActivity.this, "消灭成功");
                Intent retData = new Intent();
                retData.putExtra("action", 1);
                setResult(RESULT_OK, retData);
                finish();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(TopicmapFinishActivity.this, errorMsg);
                Intent retData = new Intent();
                retData.putExtra("action", 1);
                setResult(RESULT_OK, retData);
                finish();
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("answerJson=" + anwserJson, false);
    }
}
