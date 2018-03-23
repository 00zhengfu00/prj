package com.physicmaster.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.utils.ViewUtils;

import java.util.List;


public abstract class BaseFragmentActivity extends AppCompatActivity {
	protected final String TAG = this.getClass().getName();
	protected Logger logger = AndroidLogger.getLogger(TAG);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/1122/3712.html
		 * 在BaseActivity.java里：我们通过判断当前sdk_int大于4.4(kitkat),则通过代码的形式设置status bar为透明
		 * (这里其实可以通过values-v19 的sytle.xml里设置windowTranslucentStatus属性为true来进行设置，但是在某些手机会不起效，所以采用代码的形式进行设置)。
		 * 还需要注意的是我们这里的AppCompatAcitivity是android.support.v7.app.AppCompatActivity支持包中的AppCompatAcitivity,也是为了在低版本的android系统中兼容toolbar。
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

	// /** 账户异常跳转 */
	// public void goToLoginActivity() {
	// openActivity(NavigationActivity.class);
	// }

	protected abstract void findViewById();

	protected abstract void initView();

	protected abstract int getContentLayout();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		FragmentManager fm = getSupportFragmentManager();
		int index = requestCode >> 16;
		if (index != 0) {
			index--;
			if (fm.getFragments() == null || index < 0
					|| index >= fm.getFragments().size()) {
				Log.w(TAG, "Activity result fragment index out of range: 0x"
						+ Integer.toHexString(requestCode));
				return;
			}
			Fragment frag = fm.getFragments().get(index);
			if (frag == null) {
				Log.w(TAG, "Activity result no fragment exists for index: 0x"
						+ Integer.toHexString(requestCode));
			} else {
				handleResult(frag, requestCode, resultCode, data);
			}
			return;
		}

	}

	/**
	 * 递归调用，对所有子Fragement生效
	 * 
	 * @param frag
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	private void handleResult(Fragment frag, int requestCode, int resultCode,
							  Intent data) {
		frag.onActivityResult(requestCode & 0xffff, resultCode, data);
		List<Fragment> frags = frag.getChildFragmentManager().getFragments();
		if (frags != null) {
			for (Fragment f : frags) {
				if (f != null) {
					handleResult(f, requestCode, resultCode, data);
				}
			}
		}
	}

	/** 自动弹出编辑框 */
	public void showInputSoft(final View view) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				view.setFocusable(true);
				view.setFocusableInTouchMode(true);
				view.requestFocus();
				ViewUtils.showInputSoft(BaseFragmentActivity.this, view);
			}
		}, 500);
	}
}
