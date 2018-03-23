package com.physicmaster.modules.explore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.modules.account.LoginActivity;
import com.physicmaster.modules.account.LoginDialogActivity;
import com.physicmaster.modules.study.fragment.dialogfragment.CommonDialogFragment;
import com.physicmaster.net.response.user.MemberListBean;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.TitleBuilder;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MembersDetailedActivity extends BaseActivity {


    private ListView lvMembers;
    private List<String> membersList;
    private Button btnBuy;
    private MemberListBean subjectInfo;
    private View view;
    private int isMy;
    private View view1;
    private View view2;
    private JCVideoPlayerStandard mVideoView;
    private static final String TAG = "MembersDetailedActivity";

    @Override
    protected void findViewById() {
        lvMembers = (ListView) findViewById(R.id.lv_members);
        btnBuy = (Button) findViewById(R.id.btn_buy);
        initTitle();

        view = LayoutInflater.from(this).inflate(R.layout.activity_member_details_foot, null);
        view1 = LayoutInflater.from(this).inflate(R.layout.activity_member_details_foot1, null);
        view2 = LayoutInflater.from(this).inflate(R.layout.activity_member_details_foot2, null);
        ImageView ivContent = (ImageView) view.findViewById(R.id.iv_content);
        ImageView ivContent1 = (ImageView) view1.findViewById(R.id.iv_content);
        mVideoView = (JCVideoPlayerStandard) view2.findViewById(R.id.videoView);
        Button btnDaifu = (Button) view.findViewById(R.id.btn_daifu);


        subjectInfo = getIntent().getParcelableExtra("subjectInfo");
        isMy = getIntent().getIntExtra("isMy", -1);


        membersList = new ArrayList<>();

        if (subjectInfo.subjectId == 1) {
            membersList.add("http://img.thelper.cn/app/v3/member/1/1.jpg");
            membersList.add("http://img.thelper.cn/app/v3/member/1/2.jpg");
            membersList.add("http://img.thelper.cn/app/v3/member/1/3.jpg");
            membersList.add("http://img.thelper.cn/app/v3/member/1/4.jpg");
            Glide.with(MembersDetailedActivity.this).load("http://img.thelper.cn/app/v3/member/1/5.jpg").placeholder(R.color.colorBackgound).into(ivContent1);
            Glide.with(MembersDetailedActivity.this).load(R.mipmap.wulihuiyuan).placeholder(R.color.colorBackgound)
                    .into(ivContent);
        } else if (subjectInfo.subjectId == 2) {
            membersList.add("http://img.thelper.cn/app/v3/member/2/1.jpg");
            membersList.add("http://img.thelper.cn/app/v3/member/2/2.jpg");
            membersList.add("http://img.thelper.cn/app/v3/member/2/3.jpg");
            membersList.add("http://img.thelper.cn/app/v3/member/2/4.jpg");
            Glide.with(MembersDetailedActivity.this).load("http://img.thelper.cn/app/v3/member/2/5.jpg").placeholder(R.color.colorBackgound).into(ivContent1);
            Glide.with(MembersDetailedActivity.this).load(R.mipmap.huaxuehuiyuan).placeholder(R.color.colorBackgound)
                    .into(ivContent);
        } else if (subjectInfo.subjectId == 4) {
            membersList.add("http://img.thelper.cn/app/v3/member/4/1.jpg");
            membersList.add("http://img.thelper.cn/app/v3/member/4/2.jpg");
            membersList.add("http://img.thelper.cn/app/v3/member/4/3.jpg");
            membersList.add("http://img.thelper.cn/app/v3/member/4/4.jpg");
            Glide.with(MembersDetailedActivity.this).load("http://img.thelper.cn/app/v3/member/4/5.jpg").placeholder(R.color.colorBackgound)
                    .into(ivContent1);
            Glide.with(MembersDetailedActivity.this).load(R.mipmap.shuxuehuiyuan).placeholder(R.color.colorBackgound)
                    .into(ivContent);
        }


        btnDaifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(MembersDetailedActivity.this, "member_open_step2_parents_pay");
                if (BaseApplication.getUserData().isTourist == 1) {
                    Utils.gotoLogin(MembersDetailedActivity.this);
                    return;
                }
                Intent intent = new Intent(MembersDetailedActivity.this, SelectWayActivity.class);
                intent.putExtra("subjectId", subjectInfo.subjectId);
                startActivity(intent);
            }
        });
    }

    private void initTitle() {
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("");
    }

    @Override
    protected void initView() {

        MembersAdapter membersAdapter = new MembersAdapter(membersList);
        lvMembers.setAdapter(membersAdapter);
        lvMembers.addFooterView(view2);
        lvMembers.addFooterView(view1);
        lvMembers.addFooterView(view);
        startPlay("http://cdn.thelper.cn/app/member/video/witness.mp4");
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(MembersDetailedActivity.this, "member_open_step2_buy");
                if (BaseApplication.getUserData().isTourist == 1) {
                    Utils.gotoLogin(MembersDetailedActivity.this);
                    return;
                }
                Intent intent = new Intent(MembersDetailedActivity.this, MembersListActivity.class);
                intent.putExtra("subjectInfo", subjectInfo);
                intent.putExtra("isMy", isMy);
                startActivity(intent);
            }
        });

    }

    //开始播放视频
    private void startPlay(String info) {
        mVideoView.setUp(info, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "大师见证");
        mVideoView.setDownloadBtnVisible(View.GONE);
        mVideoView.setLikeBtnVisible(View.GONE);
        mVideoView.setBackBtnVisible(View.GONE);
        mVideoView.startPlayLogic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_members_detailed;
    }

    class MembersAdapter extends BaseAdapter {

        private List<String> memberList;

        public MembersAdapter(List<String> list) {
            this.memberList = list;
        }

        @Override
        public int getCount() {
            return memberList == null ? 0 : memberList.size();
        }

        @Override
        public String getItem(int position) {
            return memberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MembersDetailedActivity.this, R.layout.list_item_members_detailed, null);
                holder = new ViewHolder();
                holder.ivContent = (ImageView) convertView
                        .findViewById(R.id.iv_content);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String item = getItem(position);
            Glide.with(MembersDetailedActivity.this).load(item).placeholder(R.color.colorBackgound).into(holder.ivContent);
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivContent;
    }

}
