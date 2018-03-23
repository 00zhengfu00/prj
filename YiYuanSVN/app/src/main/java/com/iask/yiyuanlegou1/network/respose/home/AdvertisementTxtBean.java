package com.iask.yiyuanlegou1.network.respose.home;

/**
 * Created by Administrator on 2016/5/21.
 */
public class AdvertisementTxtBean {
    //商品id
    private Integer id;
    //商品标题
    private String title;
    //中奖用户id
    private Integer qUid;
    //用户名
    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getqUid() {
        return qUid;
    }

    public void setqUid(Integer qUid) {
        this.qUid = qUid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
