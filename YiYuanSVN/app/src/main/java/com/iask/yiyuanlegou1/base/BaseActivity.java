package com.iask.yiyuanlegou1.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.iask.yiyuanlegou1.utils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        findViewById();
        initView();
        BaseActivityManager.getInstance().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseActivityManager.getInstance().popActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public static void quitApplication() {
        BaseActivityManager.getInstance().popAllActivity();
    }

    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        // overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 账户异常跳转
     */
    public void goToLoginActivity() {
        // openActivity(NavigationActivity.class);
    }

    protected abstract void findViewById();

    protected abstract void initView();

    protected abstract int getContentLayout();

    /**
     * 自动弹出编辑框
     */
    public void showInputSoft(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.requestFocus();
                ViewUtils.showInputSoft(BaseActivity.this, view);
            }
        }, 500);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param view
     */
    public void hideInputSoft(Context context, View view) {
        ViewUtils.hideInputSoft(context, view);
    }
}
