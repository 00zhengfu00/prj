package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

import java.util.List;

public class GetTaskResponse extends Response {


    /**
     * data : {"advancedList":[{"awardGoldCoin":10,"awardPoint":100,"awardPropCount":1,"awardPropName":"恢复1点精力值","completeCount":3,"isAward":0,"missionMame":"全对闯关10次","targetCount":10,"userMissionId":45},{"awardGoldCoin":10,"awardPoint":80,"awardPropCount":1,"awardPropName":"恢复5点精力值","completeCount":0,"isAward":0,"missionMame":"视频学习超过1小时","targetCount":1,"userMissionId":46},{"awardGoldCoin":5,"awardPoint":80,"awardPropCount":0,"completeCount":0,"isAward":0,"missionMame":"消灭10道错题","targetCount":10,"userMissionId":48},{"awardGoldCoin":3,"awardPoint":100,"awardPropCount":0,"completeCount":3,"isAward":0,"missionMame":"好友数量超过10人","targetCount":10,"userMissionId":49},{"awardGoldCoin":10,"awardPoint":100,"awardPropCount":3,"awardPropName":"恢复5点精力值","completeCount":0,"isAward":0,"missionMame":"成功邀请3个好友","targetCount":3,"userMissionId":50}],"dailyList":[{"awardGoldCoin":0,"awardPoint":5,"awardPropCount":0,"completeCount":0,"isAward":0,"missionMame":"签到","targetCount":1,"userMissionId":31},{"awardGoldCoin":0,"awardPoint":5,"awardPropCount":0,"completeCount":1,"isAward":1,"missionMame":"观看一次教学视频","targetCount":1,"userMissionId":32},{"awardGoldCoin":1,"awardPoint":10,"awardPropCount":0,"completeCount":0,"isAward":0,"missionMame":"闯关一次","targetCount":1,"userMissionId":33},{"awardGoldCoin":0,"awardPoint":3,"awardPropCount":0,"completeCount":0,"isAward":0,"missionMame":"索要一次精力瓶","targetCount":1,"userMissionId":34},{"awardGoldCoin":1,"awardPoint":5,"awardPropCount":0,"completeCount":0,"isAward":0,"missionMame":"赠送一次精力瓶","targetCount":1,"userMissionId":35}],"newcomerList":[{"awardGoldCoin":3,"awardPoint":30,"awardPropCount":0,"completeCount":1,"isAward":1,"missionMame":"挑选一只宠物","targetCount":1,"userMissionId":1},{"awardGoldCoin":3,"awardPoint":30,"awardPropCount":0,"completeCount":1,"isAward":0,"missionMame":"完善个人资料","targetCount":1,"userMissionId":2},{"awardGoldCoin":10,"awardPoint":50,"awardPropCount":0,"completeCount":3,"isAward":0,"missionMame":"添加一位好友","targetCount":1,"userMissionId":3},{"awardGoldCoin":3,"awardPoint":30,"awardPropCount":0,"completeCount":0,"isAward":0,"missionMame":"消灭一次错题","targetCount":1,"userMissionId":4},{"awardGoldCoin":3,"awardPoint":30,"awardPropCount":0,"completeCount":130534,"isAward":0,"missionMame":"完成一次视频教学","targetCount":1,"userMissionId":5},{"awardGoldCoin":10,"awardPoint":50,"awardPropCount":0,"completeCount":3,"isAward":0,"missionMame":"全对闯关1次","targetCount":1,"userMissionId":6},{"awardGoldCoin":10,"awardPoint":50,"awardPropCount":0,"completeCount":0,"isAward":0,"missionMame":"购买一次课程","targetCount":1,"userMissionId":7},{"awardGoldCoin":10,"awardPoint":50,"awardPropCount":0,"completeCount":0,"isAward":0,"missionMame":"邀请一次好友","targetCount":1,"userMissionId":8}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<TaskBean> advancedList;
        public List<TaskBean> dailyList;
        public List<TaskBean> newcomerList;


    }
}
