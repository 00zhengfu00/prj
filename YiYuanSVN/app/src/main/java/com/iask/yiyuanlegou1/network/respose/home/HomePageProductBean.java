package com.iask.yiyuanlegou1.network.respose.home;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/5/21.
 */
public class HomePageProductBean implements Serializable{
    // 商品id
    protected Integer id;
    // 商品标题
    protected String title;
    // 商品封面
    protected String thumb;
    // 总人次
    protected Integer zongrenshu;
    // 已参与人次
    protected Integer canyurenshu;
    // 剩余人次
    protected Integer shenyurenshu;
    // 云购人次价格
    protected BigDecimal yunjiage;

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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Integer getZongrenshu() {
        return zongrenshu;
    }

    public void setZongrenshu(Integer zongrenshu) {
        this.zongrenshu = zongrenshu;
    }

    public Integer getCanyurenshu() {
        return canyurenshu;
    }

    public void setCanyurenshu(Integer canyurenshu) {
        this.canyurenshu = canyurenshu;
    }

    public Integer getShenyurenshu() {
        return shenyurenshu;
    }

    public void setShenyurenshu(Integer shenyurenshu) {
        this.shenyurenshu = shenyurenshu;
    }

    public BigDecimal getYunjiage() {
        return yunjiage;
    }

    public void setYunjiage(BigDecimal yunjiage) {
        this.yunjiage = yunjiage;
    }
}
