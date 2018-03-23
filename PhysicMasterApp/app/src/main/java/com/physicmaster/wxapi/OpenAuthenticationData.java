package com.physicmaster.wxapi;

import android.graphics.Bitmap;

import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.utils.UIUtils;

public class OpenAuthenticationData {
	
	public static final String ACCOUNT_OPENID   = "openid";
	public static final String ACCOUNT_TOKEN    = "token";
	public static final String ACCOUNT_EXPIRES  = "expires";
	public static final String ACCOUNT_NICKNAME = "nickname";
	public static final String ACCOUNT_GENDER   = "gender";
	public static final String ACCOUNT_PORTRAIT = "portrait";
	public static final String ACCOUNT_LOCATION = "location";
	public static final String ACCOUNT_AVATOR   = "avator";

	public OpenAuthenticationData() {
	}

	public void saveId(int type, String openid, String token, String expires){
		CacheManager.saveString(CacheManager.TYPE_USER_INFO, ACCOUNT_OPENID+type, openid);
		CacheManager.saveString(CacheManager.TYPE_USER_INFO, ACCOUNT_TOKEN+type, token);
		CacheManager.saveString(CacheManager.TYPE_USER_INFO, ACCOUNT_EXPIRES+type, expires);
	}
	
	public void saveData(int type, String nick, String gender, String portrait, String location){
		CacheManager.saveString(CacheManager.TYPE_USER_INFO, ACCOUNT_NICKNAME+type, nick);
		CacheManager.saveString(CacheManager.TYPE_USER_INFO, ACCOUNT_GENDER+type, gender);
		CacheManager.saveString(CacheManager.TYPE_USER_INFO, ACCOUNT_PORTRAIT+type, portrait);
		CacheManager.saveString(CacheManager.TYPE_USER_INFO, ACCOUNT_LOCATION+type, location);
	}
	
	public String getOpenId(int type){
		return CacheManager.getString(CacheManager.TYPE_USER_INFO, ACCOUNT_OPENID+type);
	}
	public String getOpenToken(int type){
		return CacheManager.getString(CacheManager.TYPE_USER_INFO, ACCOUNT_TOKEN+type);
	}
	public String getOpenExpires(int type){
		return CacheManager.getString(CacheManager.TYPE_USER_INFO, ACCOUNT_EXPIRES+type);
	}
	public String getOpenNick(int type){
		return CacheManager.getString(CacheManager.TYPE_USER_INFO, ACCOUNT_NICKNAME+type);
	}
	public String getOpenGender(int type){
		return CacheManager.getString(CacheManager.TYPE_USER_INFO, ACCOUNT_GENDER+type);
	}
	public String getOpenPortrait(int type){
		return CacheManager.getString(CacheManager.TYPE_USER_INFO, ACCOUNT_PORTRAIT+type);
	}
	public String getOpenlocation(int type){
		return CacheManager.getString(CacheManager.TYPE_USER_INFO, ACCOUNT_LOCATION+type);
	}
	public Bitmap getOpenAvator(int type){
		return CacheManager.getBitmap(CacheManager.TYPE_USER_INFO, ACCOUNT_AVATOR+type);
	}
	
	
	public static void saveAvator(int type, final String avatorUrl) {
		final String avatorName = ACCOUNT_AVATOR+type;
		new Thread() {

			@Override
			public void run() {
				Bitmap bitmap = UIUtils.getbitmap(avatorUrl);
				CacheManager.saveBitmap(CacheManager.TYPE_USER_INFO, avatorName, bitmap);
			}

		}.start();
	}
}
