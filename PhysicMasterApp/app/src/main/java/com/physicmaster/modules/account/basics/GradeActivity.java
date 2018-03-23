package com.physicmaster.modules.account.basics;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.widget.TitleBuilder;

import static com.physicmaster.R.id.btn_next;

public class GradeActivity extends BaseActivity implements View.OnClickListener {


    private TextView tvNineUp;
    private TextView tvNineDown;
    private TextView tvEightDown;
    private TextView tvEightUp;
    private TextView tvSevenDown;
    private TextView tvSevenUp;


    private String grade;
    private String mName;
    private String picUri;

    private int mGrade = 0;
    private Button btnNext;

    @Override
    protected void findViewById() {

        tvSevenUp = (TextView) findViewById(R.id.tv_grade_seven_up);
        tvSevenDown = (TextView) findViewById(R.id.tv_grade_seven_down);
        tvEightUp = (TextView) findViewById(R.id.tv_grade_eight_up);
        tvEightDown = (TextView) findViewById(R.id.tv_grade_eight_down);
        tvNineUp = (TextView) findViewById(R.id.tv_grade_nine_up);
        tvNineDown = (TextView) findViewById(R.id.tv_grade_nine_down);
        btnNext = (Button) findViewById(btn_next);

        initTitle();

        btnNext.setEnabled(false);
        btnNext.setBackgroundResource(R.drawable.gray_bg);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextActivity();
            }
        });
    }

    private void initTitle() {

        new TitleBuilder(this)
                .setLeftImageRes(R.mipmap.fanhui)
                .setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("选择年级");
    }

    @Override
    protected void initView() {

        mName = getIntent().getStringExtra("name");
        picUri = getIntent().getStringExtra("picUri");


        tvSevenUp.setOnClickListener(this);
        tvSevenDown.setOnClickListener(this);
        tvEightUp.setOnClickListener(this);
        tvEightDown.setOnClickListener(this);
        tvNineUp.setOnClickListener(this);
        tvNineDown.setOnClickListener(this);
    }

    private void goToNextActivity() {
        if (tvSevenUp.isSelected()) {
            mGrade = 1;
            grade = tvSevenUp.getText().toString().trim();
        } else if (tvSevenDown.isSelected()) {
            mGrade = 2;
            grade = tvSevenDown.getText().toString().trim();
        } else if (tvEightUp.isSelected()) {
            mGrade = 3;
            grade = tvEightUp.getText().toString().trim();
        } else if (tvEightDown.isSelected()) {
            mGrade = 4;
            grade = tvEightDown.getText().toString().trim();
        } else if (tvNineUp.isSelected()) {
            mGrade = 5;
            grade = tvNineUp.getText().toString().trim();
        } else if (tvNineDown.isSelected()) {
            mGrade = 6;
            grade = tvNineDown.getText().toString().trim();
        }


            Intent intent = new Intent(GradeActivity.this, SelectPetActivity.class);
            intent.putExtra("name", mName);
            intent.putExtra("picUri", picUri);
            intent.putExtra("grade", mGrade);
            startActivity(intent);


        //finish();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_grade;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_grade_seven_up:
                tvSevenUp.setSelected(true);
                tvSevenDown.setSelected(false);
                tvEightUp.setSelected(false);
                tvEightDown.setSelected(false);
                tvNineUp.setSelected(false);
                tvNineDown.setSelected(false);
                break;
            case R.id.tv_grade_seven_down:
                tvSevenUp.setSelected(false);
                tvSevenDown.setSelected(true);
                tvEightUp.setSelected(false);
                tvEightDown.setSelected(false);
                tvNineUp.setSelected(false);
                tvNineDown.setSelected(false);
                break;
            case R.id.tv_grade_eight_up:
                tvSevenUp.setSelected(false);
                tvSevenDown.setSelected(false);
                tvEightUp.setSelected(true);
                tvEightDown.setSelected(false);
                tvNineUp.setSelected(false);
                tvNineDown.setSelected(false);
                break;
            case R.id.tv_grade_eight_down:
                tvSevenUp.setSelected(false);
                tvSevenDown.setSelected(false);
                tvEightUp.setSelected(false);
                tvEightDown.setSelected(true);
                tvNineUp.setSelected(false);
                tvNineDown.setSelected(false);
                break;
            case R.id.tv_grade_nine_up:
                tvSevenUp.setSelected(false);
                tvSevenDown.setSelected(false);
                tvEightUp.setSelected(false);
                tvEightDown.setSelected(false);
                tvNineUp.setSelected(true);
                tvNineDown.setSelected(false);
                break;
            case R.id.tv_grade_nine_down:
                tvSevenUp.setSelected(false);
                tvSevenDown.setSelected(false);
                tvEightUp.setSelected(false);
                tvEightDown.setSelected(false);
                tvNineUp.setSelected(false);
                tvNineDown.setSelected(true);
                break;

        }
        btnNext.setEnabled(true);
        btnNext.setBackgroundResource(R.drawable.blue_bg);
    }
}
