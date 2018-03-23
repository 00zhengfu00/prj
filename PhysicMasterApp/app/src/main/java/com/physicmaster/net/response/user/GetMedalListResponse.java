package com.physicmaster.net.response.user;

import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huashigen on 2016/11/21.
 */
public class GetMedalListResponse extends Response{

    /**
     * data : {"level2":[{"isClaimed":0,"medalDesc":"连续登入15天","medalImg":"http://img.thelper.cn/icon/medal/104-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/104-b.png","medalName":"天天想上"},{"isClaimed":0,"medalDesc":"学习时间超过200小时","medalImg":"http://img.thelper.cn/icon/medal/108-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/108-b.png","medalName":"终身学习"},{"isClaimed":0,"medalDesc":"在物理大师消费超过500元","medalImg":"http://img.thelper.cn/icon/medal/111-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/111-b.png","medalName":"大师传人"},{"isClaimed":0,"medalDesc":"购买学习三门学科","medalImg":"http://img.thelper.cn/icon/medal/114-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/114-b.png","medalName":"开疆拓土"},{"isClaimed":0,"medalDesc":"累计完成学习章节25章","medalImg":"http://img.thelper.cn/icon/medal/116-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/116-b.png","medalName":"硅步千里"},{"isClaimed":0,"medalDesc":"成功邀请20个好友使用物理大师","medalImg":"http://img.thelper.cn/icon/medal/119-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/119-b.png","medalName":"呼朋唤友"},{"isClaimed":0,"medalDesc":"好友人数超过50","medalImg":"http://img.thelper.cn/icon/medal/122-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/122-b.png","medalName":"高朋满座"},{"isClaimed":0,"medalDesc":"宠物达到3阶","medalImg":"http://img.thelper.cn/icon/medal/125-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/125-b.png","medalName":"日上竿头"},{"isClaimed":0,"medalDesc":"金币超过1000","medalImg":"http://img.thelper.cn/icon/medal/127-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/127-b.png","medalName":"黄金屋"},{"isClaimed":0,"medalDesc":"使用道具超过100次","medalImg":"http://img.thelper.cn/icon/medal/130-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/130-b.png","medalName":"善假于物"},{"isClaimed":0,"medalDesc":"等级到达10级","medalImg":"http://img.thelper.cn/icon/medal/133-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/133-b.png","medalName":"步步为赢"}],"level3":[{"isClaimed":0,"medalDesc":"连续登入30天","medalImg":"http://img.thelper.cn/icon/medal/105-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/105-b.png","medalName":"天天想上"},{"isClaimed":0,"medalDesc":"学习时间超过500小时","medalImg":"http://img.thelper.cn/icon/medal/109-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/109-b.png","medalName":"终身学习"},{"isClaimed":0,"medalDesc":"在物理大师消费超过1000元","medalImg":"http://img.thelper.cn/icon/medal/112-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/112-b.png","medalName":"大师传人"},{"isClaimed":0,"medalDesc":"累计完成学习章节50章","medalImg":"http://img.thelper.cn/icon/medal/117-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/117-b.png","medalName":"硅步千里"},{"isClaimed":0,"medalDesc":"成功邀请50个好友使用物理大师","medalImg":"http://img.thelper.cn/icon/medal/120-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/120-b.png","medalName":"呼朋唤友"},{"isClaimed":0,"medalDesc":"好友人数超过200","medalImg":"http://img.thelper.cn/icon/medal/123-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/123-b.png","medalName":"高朋满座"},{"isClaimed":0,"medalDesc":"金币超过2000","medalImg":"http://img.thelper.cn/icon/medal/128-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/128-b.png","medalName":"黄金屋"},{"isClaimed":0,"medalDesc":"使用道具超过300次","medalImg":"http://img.thelper.cn/icon/medal/131-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/131-b.png","medalName":"善假于物"},{"isClaimed":0,"medalDesc":"等级到达15级","medalImg":"http://img.thelper.cn/icon/medal/134-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/134-b.png","medalName":"步步为赢"}],"level0":[{"isClaimed":0,"medalDesc":"连续7天早起签到","medalImg":"http://img.thelper.cn/icon/medal/101-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/101-b.png","medalName":"早起鸟"},{"isClaimed":0,"medalDesc":"连续7天在晚上10点到12点学校10分钟","medalImg":"http://img.thelper.cn/icon/medal/102-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/102-b.png","medalName":"夜猫子"},{"isClaimed":0,"medalDesc":"第一次在物理大师购买订单","medalImg":"http://img.thelper.cn/icon/medal/106-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/106-b.png","medalName":"第一滴血"}],"level1":[{"isClaimed":0,"medalDesc":"连续登入7天","medalImg":"http://img.thelper.cn/icon/medal/103-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/103-b.png","medalName":"天天想上"},{"isClaimed":0,"medalDesc":"学习时间超过50小时","medalImg":"http://img.thelper.cn/icon/medal/107-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/107-b.png","medalName":"终身学习"},{"isClaimed":0,"medalDesc":"在物理大师消费超过200元","medalImg":"http://img.thelper.cn/icon/medal/110-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/110-b.png","medalName":"大师传人"},{"isClaimed":0,"medalDesc":"购买学习两门学科","medalImg":"http://img.thelper.cn/icon/medal/113-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/113-b.png","medalName":"开疆拓土"},{"isClaimed":0,"medalDesc":"累计完成学习章节10章","medalImg":"http://img.thelper.cn/icon/medal/115-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/115-b.png","medalName":"硅步千里"},{"isClaimed":0,"medalDesc":"成功邀请5个好友使用物理大师","medalImg":"http://img.thelper.cn/icon/medal/118-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/118-b.png","medalName":"呼朋唤友"},{"isClaimed":0,"medalDesc":"好友人数超过20","medalImg":"http://img.thelper.cn/icon/medal/121-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/121-b.png","medalName":"高朋满座"},{"isClaimed":0,"medalDesc":"宠物达到2阶","medalImg":"http://img.thelper.cn/icon/medal/124-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/124-b.png","medalName":"日上竿头"},{"isClaimed":0,"medalDesc":"金币超过500","medalImg":"http://img.thelper.cn/icon/medal/126-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/126-b.png","medalName":"黄金屋"},{"isClaimed":0,"medalDesc":"使用道具超过30次","medalImg":"http://img.thelper.cn/icon/medal/129-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/129-b.png","medalName":"善假于物"},{"isClaimed":0,"medalDesc":"等级到达5级","medalImg":"http://img.thelper.cn/icon/medal/132-a.png","medalImgBlack":"http://img.thelper.cn/icon/medal/132-b.png","medalName":"步步为赢"}],"medalNewGetList":[]}
     */

    public DataBean data;

    public static class DataBean {
        public List<LevelBean> level2;
        public List<LevelBean> level3;
        public List<LevelBean> level0;
        public List<LevelBean> level1;
        public List<LevelBean>          medalNewGetList;

        public static class LevelBean implements Serializable{
            /**
             * isClaimed : 0
             * medalDesc : 连续登入15天
             * medalImg : http://img.thelper.cn/icon/medal/104-a.png
             * medalImgBlack : http://img.thelper.cn/icon/medal/104-b.png
             * medalName : 天天想上
             */

            public int isClaimed;
            public String medalDesc;
            public String medalImg;
            public String medalImgBlack;
            public String medalName;
        }


    }
}
