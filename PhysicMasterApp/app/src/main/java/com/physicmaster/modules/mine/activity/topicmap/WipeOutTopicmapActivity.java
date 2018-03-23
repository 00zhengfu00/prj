package com.physicmaster.modules.mine.activity.topicmap;

import android.content.Intent;
import android.text.Html;
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
import com.physicmaster.net.response.excercise.GetTopicmapAnalysisResponse;
import com.physicmaster.net.response.excercise.WipeOutTopicmapResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.excercise.WipeOutTopicmapService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.FinishView;
import com.physicmaster.widget.ProgressLoadingDialog;

public class WipeOutTopicmapActivity extends BaseActivity implements View.OnClickListener {


    private TextView tvFinish;
    private int chapterId;
    private FinishView ivFinish;
    private TextView tvAnalysis;
    private GetTopicmapAnalysisResponse.DataBean dataBean;
    private ImageView ivHeads;

    @Override
    protected void findViewById() {
        findViewById(R.id.tv_close).setOnClickListener(this);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        ivFinish = (FinishView) findViewById(R.id.iv_finish);
        ivHeads = (ImageView) findViewById(R.id.iv_heads);
        tvAnalysis = (TextView) findViewById(R.id.tv_analysis);
        findViewById(R.id.btn_wipe).setOnClickListener(this);
        findViewById(R.id.btn_again).setOnClickListener(this);

        chapterId = getIntent().getIntExtra("chapterId", 0);
        dataBean = (GetTopicmapAnalysisResponse.DataBean) getIntent().getSerializableExtra
                ("dataBean");
    }

    @Override
    protected void initView() {
        UserDataResponse.UserDataBean.LoginVoBean mDataBean = BaseApplication.getUserData();
        if (mDataBean == null) {
            gotoLoginActivity();
            return;
        }
        Glide.with(this).load(mDataBean.portrait).placeholder(R.drawable.placeholder_gray).into
                (ivHeads);

        tvFinish.setText("错题解析" + Html.fromHtml("<font color='#07A7FA'>已完成</font>"));

    }

    @Override
    protected int getContentLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_wipe_out_topicmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wipe:
                updateExercise();
                break;
            case R.id.btn_again:
                Intent data = new Intent();
                data.putExtra("action", 2);
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.tv_close:
                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                    @Override
                    public void ok() {
                        Intent data = new Intent();
                        data.putExtra("action", 3);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                });
                exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                break;
        }
    }

    private void updateExercise() {
        if (TextUtils.isEmpty(chapterId + "")) {
            UIUtils.showToast(WipeOutTopicmapActivity.this, "网络出问题啦，稍等一会哦！");
            return;
        }
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final WipeOutTopicmapService service = new WipeOutTopicmapService(this);
        service.setCallback(new IOpenApiDataServiceCallback<WipeOutTopicmapResponse>() {


            @Override
            public void onGetData(WipeOutTopicmapResponse data) {
                loadingDialog.dismissDialog();
                Intent intent = new Intent();
                intent.putExtra("action", 1);
                intent.putExtra("quBatchId", data.data);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(WipeOutTopicmapActivity.this, errorMsg);
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
}
