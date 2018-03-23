package com.physicmaster.net.bean;

import com.physicmaster.common.Constant;

import java.net.URLEncoder;

/**
 * Created by songrui on 16/11/16.
 */

public class ChagePwdBean {

    private String pwd  = "";
    private String opwd  = "";

    public ChagePwdBean(String pwd, String opwd) {
        this.pwd = pwd;
        this.opwd = opwd;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("pwd=" + URLEncoder.encode(pwd, Constant.CHARACTER_ENCODING));
            sb.append("&opwd=" + URLEncoder.encode(opwd, Constant.CHARACTER_ENCODING));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
