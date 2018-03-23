package com.physicmaster.modules.study.activity.exercise;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.modules.ExitDialogFragment;
import com.physicmaster.modules.study.fragment.dialogfragment.Energy2DialogFragment;
import com.physicmaster.modules.study.fragment.section.ExcerciseFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.bean.AnswerBean;
import com.physicmaster.net.response.excercise.CommitAnswerResponse;
import com.physicmaster.net.response.excercise.GetExerciseResponse;
import com.physicmaster.net.service.excercise.CommitAnswerService;
import com.physicmaster.net.service.excercise.GetExerciseService;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcerciseActivity extends BaseActivity implements OnAnswerSubmmit,
        OnResultSubmmit, View.OnClickListener {
    private List<AnswerBean> answerBeanList;
    private int index = 0;
    private String                                                   quBatchId;
    private ArrayList<GetExerciseResponse.DataBean.QuestionListBean> questionList;
    private long                                                     startTime;
    private int                                                      videoId;
    private TextView                                                 tvClose;
    private TextView                                                 tvProgress;
    private ProgressBar                                              progressBar;
    private boolean curExcerciseRight = false;
    private SoundPool soundPool;
    private Map<String, Integer> map = new HashMap<String, Integer>();
    private TextView tvAddIntegral, tvCountDown;
    private boolean        isSoundSwitch;
    private CountDownTimer timer;

    @Override
    protected void findViewById() {
        tvAddIntegral = (TextView) findViewById(R.id.tv_add_integral);
        tvCountDown = (TextView) findViewById(R.id.tv_countdown);
        videoId = getIntent().getIntExtra("videoId", 0);
        getExcercise();
    }

    private void getExcercise() {
        if ((TextUtils.isEmpty(videoId + ""))||(videoId==0)) {
            UIUtils.showToast(ExcerciseActivity.this, "网络出问题啦，稍等一会哦！");
            return;
        }
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final GetExerciseService service = new GetExerciseService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetExerciseResponse>() {
            @Override
            public void onGetData(GetExerciseResponse data) {
                questionList = data.data.questionList;
                if (questionList != null && questionList.size() > 0) {
                    quBatchId = data.data.quBatchId;
                    startCountDown(data.data.limitTime);
                    startTime = System.currentTimeMillis();
                    tvProgress.setText(0 + "/" + questionList.size());
                    progressBar.setProgress(0 * 100 / questionList.size());

                    for (GetExerciseResponse.DataBean.QuestionListBean questionListBean :
                            questionList) {
                        AnswerBean bean = new AnswerBean();
                        bean.quId = questionListBean.quId;
                        answerBeanList.add(bean);
                    }
                    ExcerciseActivity.this.exercise();
                }
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(ExcerciseActivity.this, errorMsg);
                loadingDialog.dismissDialog();
                if (errorCode == 502) {
                    new Energy2DialogFragment()
                            .show(getSupportFragmentManager(), "energy_dialog");
                }
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("videoId=" + videoId, false);
    }

    /**
     * 开始倒计时
     * @param limitTime
     */
    private void startCountDown(final int limitTime) {
        //超时,强制提交答案
        timer = new CountDownTimer(limitTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) millisUntilFinished / 1000;
                tvCountDown.setText(seconds / 60 + ":" + seconds % 60);
            }

            @Override
            public void onFinish() {
                //超时,强制提交答案
                tvCountDown.setText("");
                summitAnswer2Server(limitTime * 1000);
            }
        };
        timer.start();
    }

    private void exercise() {
        ExcerciseFragment fragment = new ExcerciseFragment();
        fragment.setOnAnswerSubmmitListener(this);
        fragment.setOnResultSubmmitListener(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("exercise", questionList.get(index));
        bundle.putInt("index", index);
        bundle.putInt("exercise_size", questionList.size());
        bundle.putBoolean("last_excercise_right", curExcerciseRight);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim
                .slide_in_from_right, 0).add(R.id.container, fragment, "fragment_exercise")
                .commit();
    }

    @Override
    protected void initView() {
        tvClose = (TextView) findViewById(R.id.tv_close);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvClose.setOnClickListener(this);
        answerBeanList = new ArrayList<AnswerBean>();
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        isSoundSwitch = SpUtils.getBoolean(this, "isSoundSwitch", true);
        if (isSoundSwitch) {
            map.put("succSound", soundPool.load(ExcerciseActivity.this, R.raw.answerright, 1));
            map.put("failSound", soundPool.load(ExcerciseActivity.this, R.raw.answerwrong, 1));
            map.put("doublekill", soundPool.load(ExcerciseActivity.this, R.raw.doubleright, 1));
        }
    }

    @Override
    protected int getContentLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_exercise;
    }

    @Override
    public void submmit(int questionId, String answer) {
        if (index < questionList.size()) {
            AnswerBean bean = answerBeanList.get(index);
            bean.answer = answer;
        }
        index++;
        if (index >= questionList.size()) {
            long curTime = System.currentTimeMillis();
            long costTime = curTime - startTime;
            summitAnswer2Server(costTime);
        } else {
            exercise();
        }
        tvProgress.setText(index + "/" + questionList.size());
        progressBar.setProgress(index * 100 / questionList.size());
    }

    private void summitAnswer2Server(long costTime) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final CommitAnswerService service = new CommitAnswerService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommitAnswerResponse>() {
            @Override
            public void onGetData(CommitAnswerResponse data) {
                loadingDialog.dismissDialog();
                if (data.data.useType == 1) {
                    Intent intent = new Intent(ExcerciseActivity.this,
                            BreakthoughFinishActivity.class);
                    intent.putExtra("preview_result", data.data.exploreResult);
                    intent.putExtra("videoId", videoId);
                    intent.putParcelableArrayListExtra("excersiseData", questionList);
                    intent.putParcelableArrayListExtra("answerData", (ArrayList<? extends
                            Parcelable>) answerBeanList);
                    startActivity(intent);
                } else if (data.data.useType == 2) {
                    Intent intent = new Intent(ExcerciseActivity.this, FinishActivity.class);
                    intent.putExtra("review_result", data.data.isFinished);
                    intent.putExtra("videoId", videoId);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(ExcerciseActivity.this, errorMsg);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"answerList\":");
        stringBuilder.append(JSON.toJSONString(answerBeanList));
        stringBuilder.append(",\"quBatchId\":");
        stringBuilder.append("\"" + quBatchId + "\"");
        stringBuilder.append(",\"timeConsuming\":");
        stringBuilder.append(costTime / 1000);
        stringBuilder.append("}");
        service.postLogined("answerJson=" + stringBuilder.toString(), false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                    @Override
                    public void ok() {
                        ExcerciseActivity.this.finish();
                    }
                });
                exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                break;
        }
    }

    @Override
    public void submmitResult(boolean right) {
        if (curExcerciseRight && right) {
            if (isSoundSwitch) {
                soundPool.play(map.get("doublekill"), 1.0f, 1.0f, 1, 0, 1);
            }
            loadAnimation();
            tvAddIntegral.setText("+5积分");
            tvAddIntegral.setVisibility(View.VISIBLE);
        } else if (!curExcerciseRight && right) {
            if (isSoundSwitch) {
                soundPool.play(map.get("succSound"), 1.0f, 1.0f, 1, 0, 1);
            }
            loadAnimation();
            tvAddIntegral.setText("+2积分");
            tvAddIntegral.setVisibility(View.VISIBLE);
        } else {
            if (isSoundSwitch) {
                soundPool.play(map.get("failSound"), 1.0f, 1.0f, 1, 0, 1);
            }
            tvAddIntegral.setText("");
            tvAddIntegral.setVisibility(View.INVISIBLE);
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
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
        exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
            @Override
            public void ok() {
                ExcerciseActivity.this.finish();
            }
        });
        exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
    }
}
