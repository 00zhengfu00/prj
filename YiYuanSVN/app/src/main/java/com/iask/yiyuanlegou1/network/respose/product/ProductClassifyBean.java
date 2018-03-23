package com.iask.yiyuanlegou1.network.respose.product;

/**
 * Created by Administrator on 2016/5/24.
 */
public class ProductClassifyBean {
    private Integer cateid;
    private String name;
    // 图片
    private String url;
    public Integer getCateid() {
        return cateid;
    }

    public void setCateid(Integer cateid) {
        this.cateid = cateid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
