package com.physicmaster.modules;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.utils.UIUtils;

public class TestActivity extends BaseActivity {
    private ImageView imageView;
    private CountDownTimer countDownTimer;

    @Override
    protected void findViewById() {
    }

    @Override
    protected void initView() {
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);

        imageView = findViewById(R.id.imageView);
        UIUtils.showToast(this, "test");
        startActivity(new Intent(this, TestActivity.class));
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_test;
    }

    private void startTimer(long period) {
        countDownTimer = new CountDownTimer(period, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                TestActivity.this.setOnBackListener(code -> {

                });
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    private OnBackListener onBackListener;

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public interface OnBackListener {
        public void onBack(int code);
    }


}