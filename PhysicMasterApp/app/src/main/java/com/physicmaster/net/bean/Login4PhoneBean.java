package com.physicmaster.net.bean;

import android.text.TextUtils;

import com.physicmaster.common.Constant;

import java.net.URLEncoder;

/**
 * Created by songrui on 16/11/16.
 */

public class Login4PhoneBean {

    private String phone = "";
    private String pwd = "";// 0 // 0：获取注册验证码；1：获取找回密码验证码
    private String miPush = "";

    public Login4PhoneBean(String phone, String pwd, String miPush) {
        this.phone = phone;
        this.pwd = pwd;
        this.miPush = miPush;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("phone=" + URLEncoder.encode(phone, Constant.CHARACTER_ENCODING));
            sb.append("&pwd=" + URLEncoder.encode(pwd, Constant.CHARACTER_ENCODING));
            if (!TextUtils.isEmpty(miPush)) {
                sb.append("&miPush=" + miPush);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
