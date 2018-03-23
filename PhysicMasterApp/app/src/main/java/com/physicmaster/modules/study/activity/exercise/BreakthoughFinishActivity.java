package com.physicmaster.modules.study.activity.exercise;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.modules.ExitDialogFragment;
import com.physicmaster.modules.study.fragment.dialogfragment.GoldExplianDialogFragment;
import com.physicmaster.modules.study.fragment.dialogfragment.IntegralExplianDialogFragment;
import com.physicmaster.net.bean.AnswerBean;
import com.physicmaster.net.response.excercise.CommitAnswerResponse;
import com.physicmaster.net.response.excercise.GetExerciseResponse;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.widget.GifView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.physicmaster.R.id.tv_analysis;


public class BreakthoughFinishActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout activityBreakthoughFinish;
    private ImageView ivSucceed;
    private GifView ivSucceedGif;
    private TextView tvSucceed;
    private RelativeLayout rlLayout1;
    private TextView tvGold;
    private TextView tvIntegral;
    private TextView tvRight;
    private TextView tvTime;
    private TextView tvAnalysis;
    private Button btnContinue;
    private Button btnAgain;
    private CommitAnswerResponse.DataBean.ExploreResultBean result;
    private int videoId;
    private Map<String, Integer> map = new HashMap<String, Integer>();
    private ArrayList<GetExerciseResponse.DataBean.QuestionListBean> questionList;
    private boolean isSoundSwitch;
    private MediaPlayer mediaPlayer;
    private List<AnswerBean> answerBeanList;

    @Override
    protected void findViewById() {
        activityBreakthoughFinish = (RelativeLayout) findViewById(R.id.activity_breakthough_finish);
        ivSucceed = (ImageView) findViewById(R.id.iv_succeed);
        ivSucceedGif = (GifView) findViewById(R.id.iv_succeed_gif);
        tvSucceed = (TextView) findViewById(R.id.tv_succeed);
        rlLayout1 = (RelativeLayout) findViewById(R.id.rl_layout1);
        tvGold = (TextView) findViewById(R.id.tv_gold);
        tvIntegral = (TextView) findViewById(R.id.tv_integral);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvTime = (TextView) findViewById(R.id.tv_time);
        findViewById(R.id.tv_close).setOnClickListener(this);
        findViewById(R.id.iv_gold_explian).setOnClickListener(this);
        findViewById(R.id.iv_integral_explian).setOnClickListener(this);
        findViewById(R.id.btn_continue).setOnClickListener(this);
        tvAnalysis = (TextView) findViewById(R.id.tv_analysis);
        btnContinue = (Button) findViewById(R.id.btn_continue);
        btnAgain = (Button) findViewById(R.id.btn_again);

        tvAnalysis.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnAgain.setOnClickListener(this);
        videoId = getIntent().getIntExtra("videoId", 0);
    }

    @Override
    protected void initView() {
        result = (CommitAnswerResponse.DataBean.ExploreResultBean) getIntent()
                .getSerializableExtra("preview_result");
        isSoundSwitch = SpUtils.getBoolean(this, "isSoundSwitch", true);
        questionList = getIntent().getParcelableArrayListExtra("excersiseData");
        answerBeanList = getIntent().getParcelableArrayListExtra("answerData");
        showResult();
    }

    private void showResult() {
        tvGold.setText("+" + result.awardGoldCoin);
        tvIntegral.setText("+" + result.awardPoint);
        tvTime.setText(result.timeConsuming + "");
        tvRight.setText(result.correctCount + "/" + result.questionCount);
        if (result.starLevel == 0) {
            if (isSoundSwitch) {
                mediaPlayer = MediaPlayer.create(this, R.raw.challengefail);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
            rlLayout1.setVisibility(View.GONE);
            tvAnalysis.setVisibility(View.GONE);
            btnContinue.setText("重新闯关");
            btnAgain.setText("查看解析");
            tvSucceed.setText("闯关失败");
            tvSucceed.setTextColor(getResources().getColor(R.color.colorBindGray));
            ivSucceed.setImageResource(R.mipmap.chuangguanshibai);
            ivSucceedGif.setGifResource(R.mipmap.shibai);
        } else if (result.starLevel == 1) {
            if (isSoundSwitch) {
                mediaPlayer = MediaPlayer.create(this, R.raw.challengesuccess);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
            rlLayout1.setVisibility(View.VISIBLE);
            tvAnalysis.setVisibility(View.VISIBLE);
            btnContinue.setText("继续闯关");
            btnAgain.setText("重新闯关");
            ivSucceed.setImageResource(R.mipmap.tong_chenggong);
            ivSucceedGif.setGifResource(R.mipmap.chenggong);
        } else if (result.starLevel == 2) {
            if (isSoundSwitch) {
                mediaPlayer = MediaPlayer.create(this, R.raw.challengesuccess);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
            rlLayout1.setVisibility(View.VISIBLE);
            tvAnalysis.setVisibility(View.VISIBLE);
            btnContinue.setText("继续闯关");
            btnAgain.setText("重新闯关");
            ivSucceed.setImageResource(R.mipmap.yin_chenggong);
            ivSucceedGif.setGifResource(R.mipmap.chenggong);
        } else if (result.starLevel == 3) {
            if (isSoundSwitch) {
                mediaPlayer = MediaPlayer.create(this, R.raw.challengesuccess);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
            rlLayout1.setVisibility(View.VISIBLE);
            tvAnalysis.setVisibility(View.VISIBLE);
            btnContinue.setText("继续闯关");
            btnAgain.setText("重新闯关");
            ivSucceed.setImageResource(R.mipmap.jin_chenggong);
            ivSucceedGif.setGifResource(R.mipmap.chenggong);
        }
        ivSucceedGif.play();
    }

    @Override
    protected int getContentLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_breakthough_finish;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_continue:
                if (result.starLevel == 0) {
                    exerciseAgain();
                } else {
                    continueExcercise();
                }
                break;
            case R.id.btn_again:
                if (result.starLevel == 0) {
                    lookAnalysis();
                } else {
                    exerciseAgain();
                }
                break;
            case R.id.iv_gold_explian:
                new GoldExplianDialogFragment()
                        .show(getSupportFragmentManager(), "gold_explian_dialog");
                break;
            case R.id.iv_integral_explian:
                new IntegralExplianDialogFragment()
                        .show(getSupportFragmentManager(), "integral_explian_dialog");
                break;
            case tv_analysis:
                lookAnalysis();
                break;
            case R.id.tv_close:
                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                    @Override
                    public void ok() {
                        continueExcercise();
                    }
                });
                exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                break;
        }
    }


    private void lookAnalysis() {
//        Intent intent = new Intent(BreakthoughFinishActivity.this, AnalysisActivity.class);
//        intent.putExtra("videoId", videoId);
//        intent.putParcelableArrayListExtra("excersiseData",questionList);
//        intent.putParcelableArrayListExtra("answerData", (ArrayList<? extends Parcelable>)
// answerBeanList);
//        startActivity(intent);
        Intent data = new Intent();
        data.putExtra("action", 1);
        setResult(RESULT_OK, data);
        finish();
    }

    private void exerciseAgain() {
        Intent data = new Intent();
        data.putExtra("action", 2);
        setResult(RESULT_OK, data);
        finish();
    }

    private void continueExcercise() {
        Intent data = new Intent();
        data.putExtra("action", 3);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mediaPlayer) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
