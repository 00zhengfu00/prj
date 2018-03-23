package com.physicmaster.net.response.explore;

import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;
import com.physicmaster.net.response.excercise.GetVideoDetailsResponse.DataBean.AppVideoStudyVoBean;

/**
 * Created by huashigen on 2016/11/21.
 */
public class GetQrVideoResponse extends Response {


    /**
     * data : {"appVideoStudyVo":{"chapterId":155,"isFav":0,"m3u8Content":"#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-TARGETDURATION:11\n#EXT-X-MEDIA-SEQUENCE:0\n#EXTINF:10.010000,
     * \nhttp://app.video.lswuyou.com/250a49a5f687cee95dee021845590f4b/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00001.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
     * .com/e52bda7fff8cdf3a804437d1f66e761c/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00002.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
     * .com/a90696cfbeababff20703edfce76a4df/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00003.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
     * .com/aaf05cf25909effe4dcd14a9c8f5226d/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00004.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
     * .com/f6efa328348f544f5f3c754cea4d4a5a/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00005.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
     * .com/0df9fdca2b7f1cf63b27d3f8d0d6bf02/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00006.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
     * .com/7b16889f8117386b25411e9d9be66b4f/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00007.ts\n#EXTINF:6.873533,\nhttp://app.video.lswuyou
     * .com/dd21ad8f364200ada62d1eb60cef37d0/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00008.ts\n#EXT-X-ENDLIST\n","pointValue":20,"posterUrl":"http://img.thelper
     * .cn/dt/c/1485149625858s72kw526.jpg%40640w_360h_95Q_1e_1c_2o.jpg","questionCount":0,"timeLength":77,"timeLengthStr":"01:17","videoId":2002,"videoQuality":"HD",
     * "videoTitle":"一元二次方程1"},"replacement1":"","replacement2":"PHNjcmlwdCB0eXBlPSJ0ZXh0L2phdmFzY3JpcHQiPndpbmRvdy5RVT17ImNvbnRlbnQiOiI8ZGl2\nPjxwPuWFs
     * +S6jjxzcGFuIGNsYXNzPVwidGV4XCIgZGF0YS1leHByPVwiXFxkaXNwbGF5c3R5bGV7\neH1cIj48L3NwYW4+55qE5pa556iLPHNwYW4gY2xhc3M9XCJ0ZXhcIiBkYXRhLWV4cHI9XCJcXGRp
     * \nc3BsYXlzdHlsZXsobS1cXHNxcnR7M30peF57bV4yLTF9LXgrMz0wfVwiPjwvc3Bhbj7mmK/kuIDl\nhYPkuozmrKHmlrnnqIvvvIzliJk8c3BhbiBjbGFzcz1cInRleFwiIGRhdGEtZXhwcj1cIlxcZGlz
     * \ncGxheXN0eWxle209fVwiPjwvc3Bhbj5fX19fXzwvcD48L2Rpdj4iLCJjcmVhdGVUaW1lIjoxNDkz\nMDI0NzM3MDAwLCJkZWZhdWx0Q2hhcHRlcklkIjoxNTUsIm1haW5Lbm93bGVkZ2VJZCI6MCwicXVJ
     * \nZCI6NTUwLCJxdVR5cGUiOjEsInN1YmplY3RJZCI6NCwidXBkYXRlVGltZSI6MTQ5MzI3ODI5MjAw\nMCwidmlkZW9JZCI6MjAwMn07PC9zY3JpcHQ+"}
     */

    public DataBean data;

    public static class DataBean implements Parcelable {
        /**
         * appVideoStudyVo : {"chapterId":155,"isFav":0,"m3u8Content":"#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-TARGETDURATION:11\n#EXT-X-MEDIA-SEQUENCE:0\n#EXTINF:10.010000,
         * \nhttp://app.video.lswuyou.com/250a49a5f687cee95dee021845590f4b/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00001.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
         * .com/e52bda7fff8cdf3a804437d1f66e761c/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00002.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
         * .com/a90696cfbeababff20703edfce76a4df/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00003.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
         * .com/aaf05cf25909effe4dcd14a9c8f5226d/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00004.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
         * .com/f6efa328348f544f5f3c754cea4d4a5a/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00005.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
         * .com/0df9fdca2b7f1cf63b27d3f8d0d6bf02/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00006.ts\n#EXTINF:10.010000,\nhttp://app.video.lswuyou
         * .com/7b16889f8117386b25411e9d9be66b4f/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00007.ts\n#EXTINF:6.873533,\nhttp://app.video.lswuyou
         * .com/dd21ad8f364200ada62d1eb60cef37d0/5962F21B/4/2002/hd/1485146549666qx3xen0sjm-00008.ts\n#EXT-X-ENDLIST\n","pointValue":20,"posterUrl":"http://img.thelper
         * .cn/dt/c/1485149625858s72kw526.jpg%40640w_360h_95Q_1e_1c_2o.jpg","questionCount":0,"timeLength":77,"timeLengthStr":"01:17","videoId":2002,"videoQuality":"HD",
         * "videoTitle":"一元二次方程1"}
         * replacement1 :
         * replacement2 : PHNjcmlwdCB0eXBlPSJ0ZXh0L2phdmFzY3JpcHQiPndpbmRvdy5RVT17ImNvbnRlbnQiOiI8ZGl2
         * PjxwPuWFs+S6jjxzcGFuIGNsYXNzPVwidGV4XCIgZGF0YS1leHByPVwiXFxkaXNwbGF5c3R5bGV7
         * eH1cIj48L3NwYW4+55qE5pa556iLPHNwYW4gY2xhc3M9XCJ0ZXhcIiBkYXRhLWV4cHI9XCJcXGRp
         * c3BsYXlzdHlsZXsobS1cXHNxcnR7M30peF57bV4yLTF9LXgrMz0wfVwiPjwvc3Bhbj7mmK/kuIDl
         * hYPkuozmrKHmlrnnqIvvvIzliJk8c3BhbiBjbGFzcz1cInRleFwiIGRhdGEtZXhwcj1cIlxcZGlz
         * cGxheXN0eWxle209fVwiPjwvc3Bhbj5fX19fXzwvcD48L2Rpdj4iLCJjcmVhdGVUaW1lIjoxNDkz
         * MDI0NzM3MDAwLCJkZWZhdWx0Q2hhcHRlcklkIjoxNTUsIm1haW5Lbm93bGVkZ2VJZCI6MCwicXVJ
         * ZCI6NTUwLCJxdVR5cGUiOjEsInN1YmplY3RJZCI6NCwidXBkYXRlVGltZSI6MTQ5MzI3ODI5MjAw
         * MCwidmlkZW9JZCI6MjAwMn07PC9zY3JpcHQ+
         */

        public AppVideoStudyVoBean appVideoStudyVo;
        public String replacement1;
        public String replacement2;
        public String trySnUrl;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.appVideoStudyVo, flags);
            dest.writeString(this.replacement1);
            dest.writeString(this.replacement2);
            dest.writeString(this.trySnUrl);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.appVideoStudyVo = in.readParcelable(AppVideoStudyVoBean.class.getClassLoader());
            this.replacement1 = in.readString();
            this.replacement2 = in.readString();
            this.trySnUrl = in.readString();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
