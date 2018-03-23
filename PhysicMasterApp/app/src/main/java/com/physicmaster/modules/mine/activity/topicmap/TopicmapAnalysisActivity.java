package com.physicmaster.modules.mine.activity.topicmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.modules.ExitDialogFragment;
import com.physicmaster.modules.mine.fragment.topicmap.TopicmapAnalysisFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.bean.AnswerBean;
import com.physicmaster.net.response.excercise.GetTopicmapAnalysisResponse;
import com.physicmaster.net.service.excercise.GetTopicmapAnalysisService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class TopicmapAnalysisActivity extends BaseActivity implements View.OnClickListener {

    private List<AnswerBean> answerBeanList;
    private int index = 0;
    private long        startTime;
    private int         chapterId;
    private int         subjectId;
    private TextView    tvClose;
    private TextView    tvProgress;
    private ProgressBar progressBar;
    private boolean curExcerciseRight = false;
    private ViewPager             viewPager;
    private SparseArray<Fragment> mFragmentList;

    private TitleBuilder                                                     titleBuilder;
    private List<GetTopicmapAnalysisResponse.DataBean.QuestionWrongListBean> questionList;
    private TopicmapAnalysisFragmentManager                                  manager;
    private GetTopicmapAnalysisResponse.DataBean                             dataBean;

    @Override
    protected void findViewById() {
        chapterId = getIntent().getIntExtra("chapterId", 0);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //判断是不是要跳转下页一个标记位
            private boolean flag;

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
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        //拖的时候才进入下一页
                        flag = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        flag = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        /**
                         * 判断是不是最后一页，同是是不是拖的状态
                         */
                        if (viewPager.getCurrentItem() == manager.getCount() - 1 && !flag) {
                            Intent intent = new Intent(TopicmapAnalysisActivity.this, WipeOutTopicmapActivity.class);
                            intent.putExtra("chapterId", chapterId);
                            intent.putExtra("dataBean", dataBean);
                            startActivity(intent);
                            finish();
                        }
                        flag = true;
                        break;
                }
            }
        });
        updateExercise();
    }

    private void updateExercise() {
        if (TextUtils.isEmpty(chapterId + "")||TextUtils.isEmpty(subjectId+"")) {
            UIUtils.showToast(TopicmapAnalysisActivity.this, "网络出问题啦，稍等一会哦！");
            return;
        }
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final GetTopicmapAnalysisService service = new GetTopicmapAnalysisService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetTopicmapAnalysisResponse>() {


            @Override
            public void onGetData(GetTopicmapAnalysisResponse data) {
                dataBean = data.data;
                questionList = data.data.questionWrongList;
                chapterId = data.data.chapterId;
                loadingDialog.dismissDialog();
                manager = new TopicmapAnalysisFragmentManager
                        (getSupportFragmentManager());
                viewPager.setAdapter(manager);
                titleBuilder.setRightText("1/" + questionList.size());
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(TopicmapAnalysisActivity.this, errorMsg);
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

    @Override
    protected void initView() {
        answerBeanList = new ArrayList<AnswerBean>();
        mFragmentList = new SparseArray<>();
        initTitle();
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
                }).setMiddleTitleText("错题解析");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_topicmap_analysis;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                    @Override
                    public void ok() {
                        TopicmapAnalysisActivity.this.finish();
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

    class TopicmapAnalysisFragmentManager extends FragmentStatePagerAdapter {

        public TopicmapAnalysisFragmentManager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragmentList.get(position) == null) {
                TopicmapAnalysisFragment fragment = new TopicmapAnalysisFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("exercise", questionList.get(position));
                bundle.putInt("index", index);
                bundle.putInt("exercise_size", questionList.size());
                bundle.putBoolean("last_excercise_right", curExcerciseRight);
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
                TopicmapAnalysisActivity.this.finish();
            }
        });
        exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
    }
}
