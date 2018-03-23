package com.iask.yiyuanlegou1.network.respose.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/2.
 */
public class AddressBean implements Serializable{
    //地址纪录id
    private Integer id;
    //收货人名字
    private String shouhuoren;
    //省
    private String sheng;
    //市
    private String shi;
    //县
    private String xian;
    //街道地址
    private String jiedao;
    //手机
    private String mobile;
    //是否为默认地址
    private String defaul;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShouhuoren() {
        return shouhuoren;
    }

    public void setShouhuoren(String shouhuoren) {
        this.shouhuoren = shouhuoren;
    }

    public String getSheng() {
        return sheng;
    }

    public void setSheng(String sheng) {
        this.sheng = sheng;
    }

    public String getShi() {
        return shi;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    public String getXian() {
        return xian;
    }

    public void setXian(String xian) {
        this.xian = xian;
    }

    public String getJiedao() {
        return jiedao;
    }

    public void setJiedao(String jiedao) {
        this.jiedao = jiedao;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDefaul() {
        return defaul;
    }

    public void setDefaul(String defaul) {
        this.defaul = defaul;
    }
}
