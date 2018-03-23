/* 
 * 系统名称：lswuyou
 * 类  名  称：SelectSchoolActivity.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2016-1-18 下午8:46:59
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.home.person.address;


import android.content.Intent;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseFragmentActivity;

public class SelectAddressActivity extends BaseFragmentActivity {

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        SelectProvinceFragment fragment = new SelectProvinceFragment();
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id
                .person_school_frame_lt, fragment).commit();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_person_school;
    }

    @Override
    public void onBackPressed() {
        int index = getSupportFragmentManager().getBackStackEntryCount();
        if (index == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
