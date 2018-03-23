package com.physicmaster.modules.mine.activity.question;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.widget.TitleBuilder;

public class ReportActivity extends BaseActivity implements View.OnClickListener {


    private int complaintId;
    private int complaintType;

    @Override
    protected void findViewById() {

        RelativeLayout report1 = (RelativeLayout) findViewById(R.id.report1);
        RelativeLayout report2 = (RelativeLayout) findViewById(R.id.report2);
        RelativeLayout report3 = (RelativeLayout) findViewById(R.id.report3);
        RelativeLayout report4 = (RelativeLayout) findViewById(R.id.report4);
        RelativeLayout report5 = (RelativeLayout) findViewById(R.id.report5);
        RelativeLayout report6 = (RelativeLayout) findViewById(R.id.report6);

        report1.setOnClickListener(this);
        report2.setOnClickListener(this);
        report3.setOnClickListener(this);
        report4.setOnClickListener(this);
        report5.setOnClickListener(this);
        report6.setOnClickListener(this);
        initTitle();
    }

    private void initTitle() {
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("举报");
    }

    @Override
    protected void initView() {
        complaintId = getIntent().getIntExtra("complaintId", -1);
        complaintType = getIntent().getIntExtra("complaintType", -1);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_report;

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ReportActivity.this, Report2SeverActivity.class);
        intent.putExtra("complaintType", complaintType);
        intent.putExtra("complaintId", complaintId);
        switch (v.getId()) {
            case R.id.report1:
                intent.putExtra("reasonType", 0);
                break;
            case R.id.report2:
                intent.putExtra("reasonType", 1);
                break;
            case R.id.report3:
                intent.putExtra("reasonType", 2);
                break;
            case R.id.report4:
                intent.putExtra("reasonType", 3);
                break;
            case R.id.report5:
                intent.putExtra("reasonType", 4);
                break;
            case R.id.report6:
                intent.putExtra("reasonType", 10);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            finish();
        }
    }
}
