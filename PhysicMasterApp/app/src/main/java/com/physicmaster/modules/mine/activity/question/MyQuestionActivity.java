package com.physicmaster.modules.mine.activity.question;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseFragment2;
import com.physicmaster.modules.mine.fragment.qusetion.AnswerFragment;
import com.physicmaster.modules.mine.fragment.qusetion.QuestionFragment;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.widget.PagerTab;
import com.physicmaster.widget.TitleBuilder;

public class MyQuestionActivity extends BaseActivity {

    private int[]     msgs;
    private PagerTab  ptQuestion;
    private ViewPager vpQuestion;
    public TitleBuilder titleBuilder;

    @Override
    protected void findViewById() {
        ptQuestion = (PagerTab) findViewById(R.id.pt_qusetion);
        vpQuestion = (ViewPager) findViewById(R.id.vp_question);

        initTitle();

    }

    private void initTitle() {

        titleBuilder = new TitleBuilder(this);
        titleBuilder.setLeftImageRes(R.mipmap.fanhui)
                .setLeftText("返回")
                .setRightImageRes(R.mipmap.xiaoxi)
                .setRightTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MyQuestionActivity.this,MessageActivity.class));
                    }
                })
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("我的问答");
    }

    @Override
    protected void initView() {
        vpQuestion.setAdapter(new QuestionAdapter(getSupportFragmentManager()));
        ptQuestion.setViewPager(vpQuestion);
        vpQuestion.setCurrentItem(0);
        ptQuestion.selectTab(0);
        //        msgs = new int[3];
        //        ptQuestion.setMsgs(msgs);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_question;
    }


    class QuestionAdapter extends FragmentPagerAdapter {

        private String[] mTabNames;
        private SparseArray<BaseFragment2> sFragments = new SparseArray<>();

        public QuestionAdapter(FragmentManager fm) {
            super(fm);
            mTabNames = ScreenUtils.getStringArray(R.array.question_tab_names);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment2 fragment = sFragments.get(position);
            if (fragment == null) {
                switch (position) {
                    case 0:
                        fragment = new QuestionFragment();
                        break;
                    case 1:
                        fragment = new AnswerFragment();
                        break;

                }
                sFragments.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }

        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }
    }

    //    @Override
    //    public void onMsgChanged(int position, int disCount) {
    //        if (position == 0) {
    //            if (disCount == -1) {
    //                msgs[0] = 0;
    //            } else {
    //                msgs[0] -= 1;
    //            }
    //        } else if (position == 1) {
    //            if (disCount == -1) {
    //                msgs[1] = 0;
    //            } else {
    //                msgs[1] -= 1;
    //            }
    //        } else if (position == 2) {
    //            if (disCount == -1) {
    //                msgs[2] = 0;
    //            } else {
    //                msgs[2] -= 1;
    //            }
    //        }
    //        ptQuestion.setMsgs(msgs);
    //    }
}
