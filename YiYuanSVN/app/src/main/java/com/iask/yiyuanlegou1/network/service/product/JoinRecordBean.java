package com.iask.yiyuanlegou1.network.service.product;

/**
 * Created by Administrator on 2016/5/24.
 */
public class JoinRecordBean {
    // 记录id
    private Integer id;
    // 用户id
    private Integer userId;
    // 用户名
    private String userName;
    // 用户头像
    private String photo;
    // 购买个数
    private Integer num;
    // 购买时间 yyyy-MM-dd HH:mm:ss
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
