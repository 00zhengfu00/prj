package com.physicmaster.net.response.account;

import java.util.List;

/**
 * Created by huashigen on 2017/1/18.
 */

public class GetPlanListWraper {
    public int checkStatus;// 0：不能签到 也不能补签  1：可以签到   2：可以补签
    public int remedyCheckInGoldCoinPrice;// 一次补签操作的价格
    public String msg;
    public List<CheckInDayPlan> checkInDayPlanList;
}
