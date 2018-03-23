package com.physicmaster.net.bean;

import android.text.TextUtils;

import com.physicmaster.common.Constant;

import java.net.URLEncoder;

/**
 * Created by songrui on 16/11/16.
 */

public class GuideFinishBean {

    private String nickname = "";
    private int petId;
    private String portrait = "";

    public GuideFinishBean(String nickname, int petId, String portrait) {
        this.nickname = nickname;
        this.petId = petId;
        this.portrait = portrait;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("nickname=" + URLEncoder.encode(nickname, Constant.CHARACTER_ENCODING));
            sb.append("&petId=" + petId);
            if (!TextUtils.isEmpty(portrait)) {
                sb.append("&portrait=" + portrait);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
