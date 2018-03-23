package com.lswuyou.tv.pm.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.GetMemberListResponse;
import com.lswuyou.tv.pm.net.response.GetMemberListResponse.DataBean.MemberListBean;
import com.lswuyou.tv.pm.net.service.GetMemberListService;
import com.lswuyou.tv.pm.utils.UIUtils;
import com.lswuyou.tv.pm.view.TitleBarView;

import java.util.List;

/**
 * Created by huashigen on 2017-08-23.
 */

public class MemberActivity extends BaseActivity implements View.OnClickListener {
    private TitleBarView mTitleBarView;
    private RelativeLayout rlAllMember, rlChemistry, rlPhysics, rlMath;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.all_member_lay:
            case R.id.rl_math:
            case R.id.rl_chemistry:
            case R.id.rl_physics:
                startActivity(new Intent(MemberActivity.this, BuyMemberActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
        rlAllMember = (RelativeLayout) findViewById(R.id.all_member_lay);
        rlChemistry = (RelativeLayout) findViewById(R.id.rl_math);
        rlPhysics = (RelativeLayout) findViewById(R.id.rl_chemistry);
        rlMath = (RelativeLayout) findViewById(R.id.rl_physics);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(0, R.string.memberCharge);
        rlAllMember.setOnClickListener(this);
        rlChemistry.setOnClickListener(this);
        rlPhysics.setOnClickListener(this);
        rlMath.setOnClickListener(this);
        getMemberList();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_member;
    }

    private void getMemberList() {
        GetMemberListService service = new GetMemberListService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetMemberListResponse>() {
            @Override
            public void onGetData(GetMemberListResponse data) {
                refreshUI(data.data);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(MemberActivity.this, "获取会员信息失败");
            }
        });
        service.postAES("", false);
    }

    private void refreshUI(GetMemberListResponse.DataBean data) {
        List<MemberListBean> memberList = data.memberList;
        for (MemberListBean memberListBean : memberList) {
            int subjectId = memberListBean.subjectId;
            if (subjectId == 1) {
                //物理
                if (memberListBean.isMember == 1) {
                    ImageView ivPhysics = (ImageView) findViewById(R.id.iv_physics);
                    ivPhysics.setImageResource(R.mipmap.mem_phy_yes);
                    TextView tvDesc = (TextView) findViewById(R.id.tv_phy_desc);
                    tvDesc.setText("会员到期日：" + memberListBean.expiryDate);
                } else {
                    ImageView ivPhysics = (ImageView) findViewById(R.id.iv_physics);
                    ivPhysics.setImageResource(R.mipmap.mem_phy_no);
                    TextView tvDesc = (TextView) findViewById(R.id.tv_phy_desc);
                    tvDesc.setText("购买会员可观看更多视频");
                }
            } else if (subjectId == 2) {
                //化学
                if (memberListBean.isMember == 1) {
                    ImageView ivPhysics = (ImageView) findViewById(R.id.iv_chemistry);
                    ivPhysics.setImageResource(R.mipmap.mem_che_yes);
                    TextView tvDesc = (TextView) findViewById(R.id.tv_che_desc);
                    tvDesc.setText("会员到期日：" + memberListBean.expiryDate);
                } else {
                    ImageView ivPhysics = (ImageView) findViewById(R.id.iv_chemistry);
                    ivPhysics.setImageResource(R.mipmap.mem_che_no);
                    TextView tvDesc = (TextView) findViewById(R.id.tv_che_desc);
                    tvDesc.setText("购买会员可观看更多视频");
                }
            } else {
                //数学
                if (memberListBean.isMember == 1) {
                    ImageView ivPhysics = (ImageView) findViewById(R.id.iv_math);
                    ivPhysics.setImageResource(R.mipmap.mem_math_yes);
                    TextView tvDesc = (TextView) findViewById(R.id.tv_math_desc);
                    tvDesc.setText("会员到期日：" + memberListBean.expiryDate);
                } else {
                    ImageView ivPhysics = (ImageView) findViewById(R.id.iv_math);
                    ivPhysics.setImageResource(R.mipmap.mem_math_no);
                    TextView tvDesc = (TextView) findViewById(R.id.tv_math_desc);
                    tvDesc.setText("购买会员可观看更多视频");
                }
            }
        }
    }
}
