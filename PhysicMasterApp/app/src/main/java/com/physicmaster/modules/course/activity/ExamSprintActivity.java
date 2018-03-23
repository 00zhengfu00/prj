package com.physicmaster.modules.course.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.modules.course.fragment.examsprint.FragmentFactory;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.widget.PagerTab;
import com.physicmaster.widget.TitleBuilder;

public class ExamSprintActivity extends BaseActivity {

    private ViewPager vpExamSprint;
    private PagerTab  ptExamSprint;
    private Button    btnBuy;

    @Override
    protected void findViewById() {
        ptExamSprint = (PagerTab) findViewById(R.id.pt_exam_sprint);
        vpExamSprint = (ViewPager) findViewById(R.id.vp_exam_sprint);
        btnBuy = (Button) findViewById(R.id.btn_buy);


        initTitle();
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("中考冲刺");
    }

    @Override
    protected void initView() {

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(ExamSprintActivity.this,PaymentActivity.class));
            }
        });


        vpExamSprint.setAdapter(new ExamSprintAdapter(getSupportFragmentManager()));
        ptExamSprint.setViewPager(vpExamSprint);
        vpExamSprint.setCurrentItem(0);
        ptExamSprint.selectTab(0);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_exam_sprint;
    }
    class ExamSprintAdapter extends FragmentPagerAdapter {

        private String[] mTabNames;

        public ExamSprintAdapter(FragmentManager fm) {
            super(fm);
            mTabNames = ScreenUtils.getStringArray(R.array.course_tab_names);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }
    }
}
