/* 
 * 系统名称：lswuyou
 * 类  名  称：CacheManager.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-22 下午4:39:22
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.common.cache;

import android.graphics.Bitmap;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.utils.UIUtils;
import com.tencent.connect.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CacheManager {
    /**
     * 账户私密数据文件夹,应当在用户退出登录的时候删除里面的数据
     */
    private static final String accountDataDirectory = "Secret";
    /**
     * 账户相关数据文件夹,退出登录时不应删除,方便用户下次登录时取用
     */
    private static String accountRelatedDataDirectory;
    /**
     * 账户公用数据
     */
    private static final String publicDataDirectory = "Public";

    /**
     * 处理账户私密数据
     */
    private static ACache userInfoCache = ACache.get(BaseApplication.getAppContext(),
            accountDataDirectory);
    /**
     * 处理账户公用数据
     */
    private static ACache publicDataCache = ACache.get(BaseApplication.getAppContext(),
            publicDataDirectory);
    /**
     * 处理账户相关数据
     */
//    private static ACache accountDataCache;
    /**
     * 账户私有类型的数据
     */
    public static int TYPE_USER_INFO = 1;

    /**
     * 账户数据
     */
    public CacheManager() {
    }

    /**
     * 重置账户类缓存目录，防止数据串位
     */
//    public static void reset() {
//        accountDataCache = null;
//    }

    /**
     * 删除Cache文件夹
     */
    public void clear(int type) {
        if (TYPE_USER_INFO == type) {
            userInfoCache.clear();
        } else {
            publicDataCache.clear();
        }
    }

    /**
     * 初始化处理账户相关数据的Acache
     */
//    public static boolean initAccountDataCache() {
//        if (accountDataCache == null) {
//            UserDataResponse.UserDataBean info = (UserDataResponse.UserDataBean) getObject(TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO,
//                    UserInfo.class);
//            if (info != null) {
//                accountRelatedDataDirectory = info.loginVo.createTime;
//                accountDataCache = ACache.get(BaseApplication.getAppContext(),
//                        accountRelatedDataDirectory);
//                return true;
//            }
//            Log.e("CacheManager", "initAccountDataCache failed , LoginUserInfo is null!");
//            return false;
//        }
//        return true;
//    }

    private static ACache getACache(int type) {
        if (TYPE_USER_INFO == type) {
            if (null == userInfoCache) {
                Log.e("CacheManager", "getACache userInfoCache null!");
            }
            return userInfoCache;
        } else {
            return publicDataCache;
        }
    }

    /**
     * @param type 存储类型：0：公共数据；1：用户信息数据(用户名、性别等等)；2：用户相关数据(如班级数据，学生数据等等)
     * @description 存储-字符串
     */
    public static void saveString(int type, String key, String value) {
        key = hashKeyForDisk(key);
        if (null == value) {
            return;
        }
        try {
            ACache cache = getACache(type);
            cache.put(key, value);
        } catch (Exception e) {
            UIUtils.showToast(BaseApplication.getAppContext(), "您的手机存储不足，已影响本软件的正常使用，请清理后再试~");
        }
    }

    /**
     * 获取-字符串
     */
    public static String getString(int type, String key) {
        key = hashKeyForDisk(key);
        ACache cache = getACache(type);
        if (cache != null) {
            return cache.getAsString(key);
        }
        return null;
    }

    /**
     * 存储-对象
     */
    public static void saveObject(int type, String key, Serializable value) {
        key = hashKeyForDisk(key);
        if (null == value) {
            return;
        }
        try {
            ACache cache = getACache(type);
            cache.remove(key);
            /** 转化成字符串再存，防止因为数据结构变更导致读取对象异常 */
            String objStr = JSON.toJSONString(value);
            cache.put(key, objStr);
        } catch (Exception e) {
            UIUtils.showToast(BaseApplication.getAppContext(), "您的手机存储不足，已影响本软件的正常使用，请清理后再试~");
        }
    }


    /**
     * 获取-对象
     *
     * @param <T>
     */
    public static <T> Object getObject(int type, String key, Class<T> clazz) {
        key = hashKeyForDisk(key);
        ACache cache = getACache(type);
        if (cache != null) {
            String objStr = cache.getAsString(key);
            /** 因为存的是字符串，此处转化为对象再返回 */
            Object obj = JSON.parseObject(objStr, clazz);
            return obj;
        }
        return null;
    }

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * 存储-Bitmap
     */
    public static void saveBitmap(int type, String key, Bitmap value) {
        key = hashKeyForDisk(key);
        if (null == value) {
            return;
        }
        ACache cache = getACache(type);
        cache.put(key, value);
    }

    /**
     * 获取-Bitmap
     */
    public static Bitmap getBitmap(int type, String key) {
        key = hashKeyForDisk(key);
        ACache cache = getACache(type);
        if (cache != null) {
            return cache.getAsBitmap(key);
        }
        return null;
    }

    /**
     * 删除-指定key的缓存
     */
    public static void remove(int type, String key) {
        key = hashKeyForDisk(key);
        ACache cache = getACache(type);
        if (cache != null) {
            cache.remove(key);
        }
    }

}
