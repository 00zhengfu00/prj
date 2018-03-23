package com.physicmaster.modules.videoplay;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.modules.study.activity.exercise.ExcerciseV2Activity;
import com.physicmaster.utils.UIUtils;

public class PlayFinishActivity extends BaseActivity implements View.OnClickListener {


    private Button btnGoOn;
    private TextView tvClose;
    private RelativeLayout rlBackground;
    private int reviewResult;
    private int videoId;
    private String chapterId;

    @Override
    protected void findViewById() {
        tvClose = (TextView) findViewById(R.id.tv_close);
        rlBackground = (RelativeLayout) findViewById(R.id.rl_background);
        btnGoOn = (Button) findViewById(R.id.btn_goon);
        tvClose.setOnClickListener(this);
        btnGoOn.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        reviewResult = intent.getIntExtra("review_result", 0);
        videoId = intent.getIntExtra("videoId", 0);
        chapterId = intent.getStringExtra("chapterId");
        String videoTime = intent.getStringExtra("videoTime");
        int questionCount = intent.getIntExtra("questionCount", 0);
        int pointValue = intent.getIntExtra("pointValue", 0);
        TextView tvTime = (TextView) findViewById(R.id.tv_time);
        TextView tvExcersise = (TextView) findViewById(R.id.tv_excersise);
        tvTime.setText(videoTime);
        tvExcersise.setText(questionCount + "题");
        TextView tvIntegral = (TextView) findViewById(R.id.tv_integral);
        tvIntegral.setText("+" + pointValue);
        btnGoOn.setText("进入闯关");
    }

    @Override
    protected int getContentLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_play_finish;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                finish();
                break;
            case R.id.btn_goon:
                if ((!TextUtils.isEmpty(videoId + "")) && (videoId != 0)) {
                    Intent intent = new Intent(this, ExcerciseV2Activity.class);
                    intent.putExtra("videoId", videoId);
                    intent.putExtra("chapterId", chapterId);
                    startActivity(intent);
                } else {
                    UIUtils.showToast(PlayFinishActivity.this, "网络出问题啦，稍等一会哦！");
                }
                finish();
                break;
        }
    }
}
