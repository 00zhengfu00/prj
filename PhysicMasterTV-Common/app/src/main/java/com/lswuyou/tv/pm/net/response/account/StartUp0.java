package com.lswuyou.tv.pm.net.response.account;

/**
 * Created by Administrator on 2016/8/15.
 */
public class StartUp0 {
    // App启动图片，如果没有值时，则显示App本地的启动图片
    public String startupImg;
    public String videoQuality;//1.FHD   2.HD   3.SD   4.LD
    public long currentTimeMillis;//当前时间戳
    public String loginQrCodeUrl;//新增登入二维码扫描路径 根据客户端获取token拼接 生成二维码
    public String loginQrCodeUrlBuyMember;
}
