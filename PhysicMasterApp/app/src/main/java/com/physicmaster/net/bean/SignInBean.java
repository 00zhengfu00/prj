package com.physicmaster.net.bean;

/**
 * Created by songrui on 17/1/19.
 */
public class SignInBean {
    public int    awardPropCount; //奖励道具
    public int    resourse; // 奖励道具1图片
    public String awardPropImgUrl; // 奖励道具1图片

    public SignInBean(int awardPropCount, String awardPropImgUrl, int resourse) {
        this.awardPropCount = awardPropCount;
        this.resourse = resourse;
        this.awardPropImgUrl = awardPropImgUrl;
    }
}
