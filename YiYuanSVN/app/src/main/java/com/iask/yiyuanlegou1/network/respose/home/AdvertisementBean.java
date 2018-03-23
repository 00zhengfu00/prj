package com.iask.yiyuanlegou1.network.respose.home;

/**
 * Created by Administrator on 2016/5/21.
 */
public class AdvertisementBean {
    //id
    private Integer id;
    //标题
    private String title;
    //商品id
    private Integer url;
    //商品种类 id
    private Integer sid;
    //图片url
    private String img;
    //序号
    private Integer xu;

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

    public Integer getUrl() {
        return url;
    }

    public void setUrl(Integer url) {
        this.url = url;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getXu() {
        return xu;
    }

    public void setXu(Integer xu) {
        this.xu = xu;
    }
}
