
package com.lswuyou.tv.pm.activity;

import android.app.Activity;
import android.content.Intent;

import java.util.Stack;

public class BaseActivityManager {
	/**
	 * 保存所有Activity
	 */
	private volatile Stack<Activity> activityStack = new Stack<Activity>();

	private static volatile BaseActivityManager instance;

	private BaseActivityManager() {
	}

	/**
	 * 创建单例类，提供静态方法调用
	 * 
	 * @return ActivityManager
	 */
	public static BaseActivityManager getInstance() {
		if (instance == null) {
			instance = new BaseActivityManager();
		}
		return instance;
	}

	/**
	 * 退出Activity
	 * 
	 * @param activity
	 *            Activity
	 */
	public void popActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
		}
	}

	/**
	 * 获得当前栈顶的Activity
	 * 
	 * @return Activity Activity
	 */
	public Activity currentActivity() {
		Activity activity = null;
		if (!activityStack.empty()) {
			activity = activityStack.lastElement();
		}
		return activity;
	}

	/**
	 * 将当前Activity推入栈中
	 * 
	 * @param activity
	 *            Activity
	 */
	public void pushActivity(Activity activity) {
		activityStack.add(activity);
	}

	/**
	 * 退出栈中其他所有Activity
	 * 
	 * @param cls
	 *            Class 类名
	 */
	@SuppressWarnings("rawtypes")
	public void popOtherActivity(Class cls) {
		if (null == cls) {
			return;
		}

		for (Activity activity : activityStack) {
			if (null == activity || activity.getClass().equals(cls)) {
				continue;
			}

			activity.finish();
		}
	}

	/**
	 * 退出栈中所有Activity
	 * 
	 */
	public void popAllActivity() {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			activity.finish();
			popActivity(activity);
		}
	}

	public void startNextActivity(Class<?> activity) {
		Activity curActivity = currentActivity();
		Intent intent = new Intent(curActivity, activity);
		curActivity.startActivity(intent);
	}
}
