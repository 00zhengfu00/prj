package com.iask.yiyuanlegou1.network.bean.account;

import android.text.TextUtils;

import com.iask.yiyuanlegou1.common.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/6/2.
 */
public class AddAdressBean {
    //地址纪录id
    private Integer id;
    private String shouhuoren;
    private String mobile;
    private String sheng;
    private String shi;
    private String xian;
    private String jiedao;
    private String defaul;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("dizhiid=" + id);
            builder.append("&shouhuoren=" + URLEncoder.encode(shouhuoren, Constant
                    .CHARACTER_ENCODING));
            builder.append("&mobile=" + mobile);
            builder.append("&sheng=" + URLEncoder.encode(sheng, Constant.CHARACTER_ENCODING));
            builder.append("&shi=" + URLEncoder.encode(shi, Constant.CHARACTER_ENCODING));
            if (!TextUtils.isEmpty(xian)) {
                builder.append("&xian=" + URLEncoder.encode(xian, Constant.CHARACTER_ENCODING));
            } else {
                builder.append("&xian=");
            }
            builder.append("&jiedao=" + URLEncoder.encode(jiedao, Constant.CHARACTER_ENCODING));
            builder.append("&defaul=" + defaul);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getDefaul() {
        return defaul;
    }

    public void setDefaul(String defaul) {
        this.defaul = defaul;
    }

}
