package com.physicmaster.net.bean;

import android.text.TextUtils;

import com.physicmaster.common.Constant;

import java.net.URLEncoder;

/**
 * Created by songrui on 16/11/16.
 */

public class Register4PhoneBean {

    private String phone = "";
    private String token = "";// 0 // 0：获取注册验证码；1：获取找回密码验证码
    private String pwd = "";// 用户输入的密码
    private String miPush = "";

    public Register4PhoneBean(String phone, String token, String pwd, String miPush) {
        this.phone = phone;
        this.token = token;
        this.pwd = pwd;
        this.miPush = miPush;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("phone=" + URLEncoder.encode(phone, Constant.CHARACTER_ENCODING));
            sb.append("&token=" + URLEncoder.encode(token, Constant.CHARACTER_ENCODING));
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
