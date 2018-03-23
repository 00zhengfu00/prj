package com.physicmaster.net.response.account;

import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2016/11/18.
 */
public class StartupResponse extends Response {


    public DataBean data;

    public static class DataBean implements Serializable {

        public long currentTimeMillis;//1479176152675,
        public String latestReleaseVersion;//2.0.0,      //客户端最新版本
        public String regionUrl;//http://cdn.thelper.cn/region/region-20150916.json  //行政区域的json数据文件
        public String helpPageUrl;//帮助页面
        public String faqPageUrl;  //常见问题
        public String latestDownloadUrl;//needUpgrade=true且是安卓，才有值，安卓最新版APK下载地址
        public int needUpgrade;// 需要升级
        public String upgradeTip;// needUpgrade=true，才有值，升级提示
        public AliyunServerBean ossConfig;
        public List<String> jsBridgeWhiteList;// 允许调用JSBridge的域名
        public String shareLinkImg; //在内嵌Webview中分享网页时，如果没有解析到图片，则使用此图片URL，如iOS分享到微博，必须带有一个图片
        public String lotteryPageUrl; //金币抽奖页面URL
        public String webQqGetCodeUrl;  //用于实现非“物理大师App”采用网页方式实现QQ登录和绑定
        public String webQqLoginCallbackUrl; //用于实现非“物理大师App”采用网页方式实现QQ登录和绑定
        public String helpPayIntroUrl;   //家长代付详情介绍页
        public String medalQrcode;    //徽章二维码url
        public List<IMAdmin> imAdminList;
        public int maxImgFileSize;
        public int maxImgWidth;
        public int imgQuality;
        public QrcodeCfg qrcodeCfg;
        public Advertisement popup;

        public List<EduGradeYearListBean> eduGradeYearList;
        public List<SubjectTypeListBean> subjectTypeList;

        public List<BookMenuBean> bookMenu;

        public static class BookMenuBean implements Parcelable {

            public String n;
            public List<EBean> e;
            public int i;

            public static class EBean implements Parcelable {

                public String n;
                public List<BBean> b;

                public static class BBean implements Parcelable {

                    public int i;
                    public String n;

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(this.i);
                        dest.writeString(this.n);
                    }

                    public BBean() {
                    }

                    protected BBean(Parcel in) {
                        this.i = in.readInt();
                        this.n = in.readString();
                    }

                    public static final Creator<BBean> CREATOR = new Creator<BBean>() {
                        @Override
                        public BBean createFromParcel(Parcel source) {
                            return new BBean(source);
                        }

                        @Override
                        public BBean[] newArray(int size) {
                            return new BBean[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.n);
                    dest.writeList(this.b);
                }

                public EBean() {
                }

                protected EBean(Parcel in) {
                    this.n = in.readString();
                    this.b = new ArrayList<BBean>();
                    in.readList(this.b, BBean.class.getClassLoader());
                }

                public static final Creator<EBean> CREATOR = new Creator<EBean>() {
                    @Override
                    public EBean createFromParcel(Parcel source) {
                        return new EBean(source);
                    }

                    @Override
                    public EBean[] newArray(int size) {
                        return new EBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.n);
                dest.writeList(this.e);
            }

            public BookMenuBean() {
            }

            protected BookMenuBean(Parcel in) {
                this.n = in.readString();
                this.e = new ArrayList<EBean>();
                in.readList(this.e, EBean.class.getClassLoader());
            }

            public static final Parcelable.Creator<BookMenuBean> CREATOR = new Parcelable.Creator<BookMenuBean>() {
                @Override
                public BookMenuBean createFromParcel(Parcel source) {
                    return new BookMenuBean(source);
                }

                @Override
                public BookMenuBean[] newArray(int size) {
                    return new BookMenuBean[size];
                }
            };
        }

        public static class QrcodeCfg {
            public String xiti;// 必练册，匹配习题后方的二维码内容正则，解析到以后转换成videoId，客户端跳转到视频详情
            public String whiteUrl;// URL白名单正则，如果二维码中的内容是URL，且符合此正则的，客户端直接使用内嵌WebView打开此URL
        }

        public static class Advertisement {
            public int bannerId;//广告ID
            public String pageUrl;//点击图片跳转的URL
            public String popupImgUrl;// 图片宽高比：600px : 800px
        }

        public static class EduGradeListBean {
            /**
             * gradeId : 3
             * name : 八年级上
             */

            public int i;
            public String n;
        }

        public static class IMAdmin {
            public int dtUserId;
            public String imUserId;
            public String nickname;
            public String portrait;
        }

        public static class EduGradeYearListBean {
            /**
             * i : 107
             * n : 七年级
             * s : [54,0]
             */

            public int i;
            public String n;
            public List<Integer> s;
            //0-未选中,1-选中
            public boolean state = false;
        }

        public static class SubjectTypeListBean {
            /**
             * i : 54
             * n : 数学
             */

            public int i;
            public String n;
            //0-不可选,1-未选中,2-选中
            public int state = 1;
        }
    }


}
