package com.physicmaster.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class UIUtils {
    /**
     * 弹出String类型的文本
     */
    private static long lastTimeInMillis;
    private static String lastText;

    public static void showToast(Context mContext, String text) {
        if (!isShowMsg(text)) {
            return;
        }
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出资源ID类型的文本
     */
    public static void showToast(Context mContext, int text) {
        if (text <= 0) {
            return;
        }
        String txt = mContext.getResources().getString(text);
        if (!isShowMsg(txt)) {
            return;
        }
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断是否显示此toast信息-空信息不显示；重复信息不显示
     */
    private static boolean isShowMsg(String text) {
        if (StringUtils.isEmpty(text)) {
            return false;
        }
        long curTimeInMillis = System.currentTimeMillis();
        if (0L != lastTimeInMillis) {
            if ((curTimeInMillis - lastTimeInMillis) < 1000 && lastText.equals(text)) {
                lastTimeInMillis = curTimeInMillis;
                return false;
            }
        }
        lastTimeInMillis = curTimeInMillis;
        lastText = text;
        return true;
    }

    /**
     * 根据一个网络连接(String)获取bitmap图像
     *
     * @param imageUri
     * @return
     * @throws MalformedURLException
     */
    public static Bitmap getbitmap(String imageUri) {
        Log.v(TAG, "getbitmap:" + imageUri);
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            Log.v(TAG, "image download finished." + imageUri);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "getbitmap bmp fail---");
            bitmap = null;
        }
        return bitmap;
    }
}
