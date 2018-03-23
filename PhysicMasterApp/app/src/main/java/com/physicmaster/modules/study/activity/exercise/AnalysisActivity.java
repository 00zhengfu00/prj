package com.physicmaster.modules.study.activity.exercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.modules.ExitDialogFragment;
import com.physicmaster.modules.mine.activity.topicmap.TopicmapAnalysisActivity;
import com.physicmaster.modules.study.fragment.section.AnalysisFragment;
import com.physicmaster.net.bean.AnswerBean;
import com.physicmaster.net.response.excercise.GetExerciseResponse;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class AnalysisActivity extends BaseActivity implements View
        .OnClickListener {
    private List<AnswerBean> answerBeanList;
    private int index = 0;
    private String quBatchId;
    private List<GetExerciseResponse.DataBean.QuestionListBean> questionList;
    private long startTime;
    private int videoId;
    private TextView tvClose;
    private TextView tvProgress;
    private ProgressBar progressBar;
    private boolean curExcerciseRight = false;
    private ViewPager viewPager;
    private SparseArray<Fragment> mFragmentList;
    private TitleBuilder titleBuilder;

    @Override
    protected void findViewById() {
        videoId = getIntent().getIntExtra("videoId", 0);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titleBuilder.setRightText("" + (position + 1) + "/" + questionList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateExercise() {
        questionList = getIntent().getParcelableArrayListExtra("excersiseData");
        answerBeanList = getIntent().getParcelableArrayListExtra("answerData");
        AnalysisFragmentManager manager = new AnalysisFragmentManager(getSupportFragmentManager());
        viewPager.setAdapter(manager);
        titleBuilder.setRightText("1/" + questionList.size());
    }

    @Override
    protected void initView() {
        mFragmentList = new SparseArray<>();
        initTitle();
        updateExercise();
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        String title = getIntent().getStringExtra("title");
        titleBuilder = new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }).setMiddleTitleText("习题解析");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_analysis;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                    @Override
                    public void ok() {
                        AnalysisActivity.this.finish();
                    }
                });
                exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class AnalysisFragmentManager extends FragmentStatePagerAdapter {

        public AnalysisFragmentManager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragmentList.get(position) == null) {
                AnalysisFragment fragment = new AnalysisFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("exercise", questionList.get(position));
                bundle.putInt("index", index);
                bundle.putInt("exercise_size", questionList.size());
                bundle.putBoolean("last_excercise_right", curExcerciseRight);
                bundle.putParcelable("userAnswer", answerBeanList.get(position));
                fragment.setArguments(bundle);
                mFragmentList.put(position, fragment);
                return fragment;
            } else {
                return mFragmentList.get(position);
            }
        }

        @Override
        public int getCount() {
            return questionList.size();
        }
    }

    @Override
    public void onBackPressed() {
        ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
        exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
            @Override
            public void ok() {
                AnalysisActivity.this.finish();
            }
        });
        exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
    }
}
