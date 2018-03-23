package com.lswuyou.tv.pm.common;

import android.os.SystemClock;

public final class SystemParams {

	/** 屏幕宽度 */
	private static int screenWidth;

	/** 屏幕高度 */
	private static int screenHeight;

	/** 屏幕密度 */
	private static float desity;

	/** 设备名称 */
	private static String deviceName;

	/** 设备ID */
	private static String deviceID;

	/** AES密钥 */
	private static String aesKey;

	/** 应用打开时获取到的服务器的时间 */
	private static long currentTimeMillis = 0;

	/** 手机开机时间 */
	private static long bootTimeMillis = 0;
	
	public static long getServerTime(){
		long time = currentTimeMillis + (SystemClock.elapsedRealtime() - bootTimeMillis);
		long localTime = System.currentTimeMillis();
		long timedistance = (Math.abs(time - localTime)) / 1000;
		// if (timedistance > 5 * 60) {
		// Toast.makeText(BaseApplication.getAppContext(),
		// "SystemParams getServerTime exception!---time distance:" +
		// timedistance, Toast.LENGTH_SHORT).show();
		// }
		return time;
	}

	public static void setBootTimeMillis(long bootTimeMillis) {
		SystemParams.bootTimeMillis = bootTimeMillis;
	}

	public static void setCurrentTimeMillis(long currentTimeMillis) {
		SystemParams.currentTimeMillis = currentTimeMillis;
	}

	public static String getAesKey() {
		return aesKey;
	}

	public static void setAesKey(String aesKey) {
		SystemParams.aesKey = aesKey;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static void setScreenWidth(int screenWidth) {
		SystemParams.screenWidth = screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static void setScreenHeight(int screenHeight) {
		SystemParams.screenHeight = screenHeight;
	}

	public static float getDesity() {
		return desity;
	}

	public static void setDesity(float desity) {
		SystemParams.desity = desity;
	}

	public static String getDeviceName() {
		return deviceName;
	}

	public static void setDeviceName(String deviceName) {
		SystemParams.deviceName = deviceName;
	}

	public static String getDeviceID() {
		return deviceID;
	}

	public static void setDeviceID(String deviceID) {
		SystemParams.deviceID = deviceID;
	}

}
