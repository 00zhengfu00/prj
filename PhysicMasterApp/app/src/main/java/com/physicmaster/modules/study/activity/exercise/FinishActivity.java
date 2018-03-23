package com.physicmaster.modules.study.activity.exercise;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.modules.ExitDialogFragment;

public class FinishActivity extends BaseActivity implements View.OnClickListener {


    private Button btnAgain;
    private Button btnRest;
    private RelativeLayout activityFinish;
    private TextView tvClose;
    private RelativeLayout rlBackground;
    private TextView tvFinish;
    private ImageView ivFinish;
    private ImageView ivReward;
    private TextView tvGold;
    private int reviewResult;
    private int videoId;

    @Override
    protected void findViewById() {
        tvClose = (TextView) findViewById(R.id.tv_close);
        rlBackground = (RelativeLayout) findViewById(R.id.rl_background);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        ivFinish = (ImageView) findViewById(R.id.iv_finish);
        ivReward = (ImageView) findViewById(R.id.iv_reward);
        tvGold = (TextView) findViewById(R.id.tv_gold);
        btnAgain = (Button) findViewById(R.id.btn_again);
        btnRest = (Button) findViewById(R.id.btn_rest);
        tvClose.setOnClickListener(this);
        btnAgain.setOnClickListener(this);
        btnRest.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        reviewResult = getIntent().getIntExtra("review_result", 0);
        videoId = getIntent().getIntExtra("videoId", 0);

        if (reviewResult == 1) {
            rlBackground.setVisibility(View.VISIBLE);
            ivReward.setVisibility(View.VISIBLE);
            tvGold.setVisibility(View.VISIBLE);
            ivFinish.setImageResource(R.mipmap.xitiwancheng);
            btnRest.setVisibility(View.GONE);
            btnAgain.setText("休息一下");
            tvFinish.setText("习题" + "<font color=\'#07A7FA\'>完成</font>");
        } else if (reviewResult == 0) {
            rlBackground.setVisibility(View.GONE);
            ivReward.setVisibility(View.GONE);
            tvGold.setVisibility(View.GONE);
            ivFinish.setImageResource(R.mipmap.xitiweiwancheng);
            btnRest.setVisibility(View.VISIBLE);
            btnRest.setText("休息一下");
            btnAgain.setText("再试一次");
            tvFinish.setText("习题" + "<font color=\'#D54B1D\'>未完成</font>");
        }
    }

    @Override
    protected int getContentLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_finish;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                    @Override
                    public void ok() {
                        Intent data1 = new Intent();
                        data1.putExtra("action", 2);
                        setResult(RESULT_OK, data1);
                        finish();
                    }
                });
                exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                break;
            case R.id.btn_again:
                Intent data = new Intent();
                data.putExtra("action", 1);
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.btn_rest:
                Intent data1 = new Intent();
                data1.putExtra("action", 2);
                setResult(RESULT_OK, data1);
                finish();
                break;
        }
    }
}
