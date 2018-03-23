package com.physicmaster.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.modules.account.LoginActivity;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getName();
    protected Logger logger = AndroidLogger.getLogger(TAG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/1122/3712.html
         * 在BaseActivity.java里：我们通过判断当前sdk_int大于4.4(kitkat),则通过代码的形式设置status bar为透明
         * (这里其实可以通过values-v19 的sytle
         * .xml里设置windowTranslucentStatus属性为true来进行设置，但是在某些手机会不起效，所以采用代码的形式进行设置)。
         * 还需要注意的是我们这里的AppCompatAcitivity是android.support.v7.app
         * .AppCompatActivity支持包中的AppCompatAcitivity,也是为了在低版本的android系统中兼容toolbar。
         */
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isTranslucentStatusBar()) {
                Window window = getWindow();
                // Translucent status bar
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }*/
        setContentView(getContentLayout());
        findViewById();
        initView();
        BaseActivityManager.getInstance().pushActivity(this);
    }

    //是否statusBar 状态栏为透明 的方法 默认为真
    protected boolean isTranslucentStatusBar() {
        return true;
    }


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseActivityManager.getInstance().popActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
        }, 300);
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

    /**
     * 账户异常跳转
     */
    public void gotoLoginActivity() {
        UIUtils.showToast(this, "数据异常");
        quitApplication();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    protected int getStatusBarHeight() {
        int statusBarHeight1 = 64;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }
}
