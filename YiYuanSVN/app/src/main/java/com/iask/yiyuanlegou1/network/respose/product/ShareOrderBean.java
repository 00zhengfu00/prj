package com.iask.yiyuanlegou1.network.respose.product;

import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ShareOrderBean {
    // 晒单id
    private Integer sdId;
    // 中奖人头像
    private String photo;
    // 中奖人姓名
    private String userName;
    // 商品期数
    private Integer nper;
    // 商品标题
    private String title;
    // 晒单内容
    private String content;
    // 晒单图片
    private List<String> image;
    // 晒单时间 yyyy-MM-dd HH:mm:ss
    private String time;

    public Integer getSdId() {
        return sdId;
    }

    public void setSdId(Integer sdId) {
        this.sdId = sdId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getNper() {
        return nper;
    }

    public void setNper(Integer nper) {
        this.nper = nper;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
