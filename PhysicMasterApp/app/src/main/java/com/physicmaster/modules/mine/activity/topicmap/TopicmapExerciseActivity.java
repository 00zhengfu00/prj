package com.physicmaster.modules.mine.activity.topicmap;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.modules.ExitDialogFragment;
import com.physicmaster.modules.mine.fragment.topicmap.TopicmapExcerciseFragment;
import com.physicmaster.modules.study.activity.exercise.OnResultSubmmit;
import com.physicmaster.net.bean.AnswerBean;
import com.physicmaster.net.response.excercise.GetTopicmapAnalysisResponse;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicmapExerciseActivity extends BaseActivity implements OnTopicmapAnswerSubmmit, OnResultSubmmit,
        View.OnClickListener {

    private ArrayList<AnswerBean> answerBeanList;
    private int index         = 0;
    private int rightExercise = 0;
    private TextView    tvClose;
    private TextView    tvProgress;
    private ProgressBar progressBar;
    private boolean curExcerciseRight = false;
    private SoundPool soundPool;
    private Map<String, Integer> map = new HashMap<String, Integer>();
    private TextView                                                         tvAddIntegral;
    private boolean                                                          isSoundSwitch;
    private GetTopicmapAnalysisResponse.DataBean                             dataBean;
    private List<GetTopicmapAnalysisResponse.DataBean.QuestionWrongListBean> questionWrongList;
    private String quBatchId;

    @Override
    protected void findViewById() {
        tvAddIntegral = (TextView) findViewById(R.id.tv_add_integral);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        tvClose = (TextView) findViewById(R.id.tv_close);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        dataBean = (GetTopicmapAnalysisResponse.DataBean) getIntent().getSerializableExtra("dataBean");
        quBatchId = getIntent().getStringExtra("quBatchId");

        updateExercise();
    }

    private void updateExercise() {

        if (null == dataBean) {
            UIUtils.showToast(TopicmapExerciseActivity.this, "网络出问题啦，稍等一会哦！");
            return;
        }
        questionWrongList = dataBean.questionWrongList;
        if (null != questionWrongList || questionWrongList.size() != 0) {
            tvProgress.setText(0 + "/" + questionWrongList.size());
            progressBar.setProgress(0 * 100 / questionWrongList.size());
            TopicmapExerciseActivity.this.exercise();
        }

    }

    private void exercise() {
        TopicmapExcerciseFragment fragment = new TopicmapExcerciseFragment();
        fragment.setOnAnswerSubmmitListener(this);
        fragment.setOnResultSubmmitListener(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("exercise", questionWrongList.get(index));
        bundle.putInt("index", index);
        bundle.putInt("exercise_size", questionWrongList.size());
        bundle.putBoolean("last_excercise_right", curExcerciseRight);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim
                .slide_in_from_right, 0).add(R.id.container, fragment, "fragment_exercise")
                .commit();
    }

    @Override
    protected void initView() {
        tvClose.setOnClickListener(this);
        answerBeanList = new ArrayList<AnswerBean>();
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        isSoundSwitch = SpUtils.getBoolean(this, "isSoundSwitch", true);
        if (isSoundSwitch) {
            map.put("succSound", soundPool.load(TopicmapExerciseActivity.this, R.raw.answerright, 1));
            map.put("failSound", soundPool.load(TopicmapExerciseActivity.this, R.raw.answerwrong, 1));
            map.put("doublekill", soundPool.load(TopicmapExerciseActivity.this, R.raw.doubleright, 1));
        }
    }

    @Override
    protected int getContentLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_topicmap_exercise;
    }

    @Override
    public void submmit(int questionId, String answer, int wrongId, boolean right) {
        AnswerBean bean = new AnswerBean();
        bean.answer = answer;
        bean.quId = questionId;
        bean.wrongId = wrongId;
        answerBeanList.add(bean);
        index++;
        if (right) {
            rightExercise++;
        }
        if (index >= questionWrongList.size()) {
            summitAnswer2Server();
        } else {
            exercise();
        }


        tvProgress.setText(index + "/" + questionWrongList.size());
        progressBar.setProgress(index * 100 / questionWrongList.size());
    }

    private void summitAnswer2Server() {

        Intent intent = new Intent(TopicmapExerciseActivity.this, TopicmapFinishActivity.class);
        intent.putExtra("topicmap_size", questionWrongList.size());
        intent.putExtra("topicmap_right", rightExercise);
        intent.putExtra("dataBean", dataBean);
        intent.putExtra("quBatchId", quBatchId);
        intent.putParcelableArrayListExtra("answer_lsit", answerBeanList);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                    @Override
                    public void ok() {
                        TopicmapExerciseActivity.this.finish();
                    }
                });
                exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                TopicmapExerciseActivity.this.finish();
                break;
        }
    }

    @Override
    public void submmitResult(boolean right) {
        if (curExcerciseRight && right) {
            if (isSoundSwitch) {
                soundPool.play(map.get("doublekill"), 1.0f, 1.0f, 1, 0, 1);
            }
        } else if (!curExcerciseRight && right) {
            if (isSoundSwitch) {
                soundPool.play(map.get("succSound"), 1.0f, 1.0f, 1, 0, 1);
            }
        } else {
            if (isSoundSwitch) {
                soundPool.play(map.get("failSound"), 1.0f, 1.0f, 1, 0, 1);
            }
        }
        curExcerciseRight = right;
    }

    /**
     * 加载积分动画
     */
    private void loadAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(600);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvAddIntegral.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0f, 1, 0f, 1, 0f, 1, -2f);
        animationSet.addAnimation(translateAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        animationSet.addAnimation(alphaAnimation);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, 1, 0.5f, 1, 1.0f);
        animationSet.addAnimation(scaleAnimation);
        tvAddIntegral.startAnimation(animationSet);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != soundPool) {
            soundPool.release();
            soundPool = null;
        }
    }

    @Override
    public void onBackPressed() {
        ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
        exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
            @Override
            public void ok() {
                TopicmapExerciseActivity.this.finish();
            }
        });
        exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
    }
}
