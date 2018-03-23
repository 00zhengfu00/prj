package com.physicmaster.modules.mine.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.explore.activity.Members2Activity;
import com.physicmaster.modules.explore.activity.MembersDetailedActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.GetMemebersResponse;
import com.physicmaster.net.response.user.MemberListBean;
import com.physicmaster.net.response.user.MemebersResponse;
import com.physicmaster.net.service.user.GetMembersService;
import com.physicmaster.net.service.user.MembersService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.TitleBuilder;
import com.umeng.analytics.MobclickAgent;

import java.util.List;


public class MymemberActivity extends BaseActivity {

    private MembersAdapter membersAdapter;
    private ListView lvMembers;
    private TitleBuilder titleBuilder;

    @Override
    protected void findViewById() {
        lvMembers = (ListView) findViewById(R.id.lv_members);

        initTitle();
    }

    private void initTitle() {
        titleBuilder = new TitleBuilder(this);
        titleBuilder.setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("我的会员");
    }

    private void getBuyMembersData() {
        final MembersService service = new MembersService(MymemberActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<MemebersResponse>() {
            @Override
            public void onGetData(final MemebersResponse data) {
                if (data.data.superMember != null) {
                    String url = data.data.superMember.poster;
                    RelativeLayout rlHead = (RelativeLayout) LayoutInflater.from(MymemberActivity.this).inflate(R.layout.view_image, null);
                    ImageView imageHead = rlHead.findViewById(R.id.image);
                    Glide.with(MymemberActivity.this).load(url).into(imageHead);
                    lvMembers.addHeaderView(rlHead);
                    imageHead.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MobclickAgent.onEvent(MymemberActivity.this, "member_open_all_step1");
                            Intent intent = new Intent();
                            intent.setClass(MymemberActivity.this, Members2Activity.class);
                            intent.putExtra("memberBanner", data.data.superMember);
                            startActivity(intent);
                        }
                    });
                }
                getMyMembersData();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(MymemberActivity.this, errorMsg);
            }
        });
        service.postLogined("", false);
    }

    @Override
    protected void initView() {
        getBuyMembersData();
    }

    private void getMyMembersData() {
        final GetMembersService service = new GetMembersService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetMemebersResponse>() {


            @Override
            public void onGetData(GetMemebersResponse data) {
                if (data.data.isShowMyCourse == 1) {
                    titleBuilder.setRightText("我的课程").setRightTextOrImageListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MymemberActivity.this, MyCourseActivity.class));
                        }
                    });
                }
                membersAdapter = new MembersAdapter(data.data.memberList);
                lvMembers.setAdapter(membersAdapter);

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(MymemberActivity.this, errorMsg);
            }
        });
        service.postLogined("", false);
    }


    class MembersAdapter extends BaseAdapter {

        private List<MemberListBean> memberList;

        public MembersAdapter(List<MemberListBean> list) {
            this.memberList = list;
        }

        @Override
        public int getCount() {
            return memberList.size();
        }

        @Override
        public MemberListBean getItem(int position) {
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
                convertView = View.inflate(MymemberActivity.this,
                        R.layout.list_item_members, null);
                holder = new ViewHolder();
                holder.tvData = (TextView) convertView
                        .findViewById(R.id.tv_data);
                holder.ivContent = (ImageView) convertView
                        .findViewById(R.id.iv_content);
                holder.btnBuy = (Button) convertView
                        .findViewById(R.id.btn_buy);
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final MemberListBean item = getItem(position);

            if (item.isMember == 1) {
                holder.tvData.setText("到期日:" + item.expiryDate);
                holder.btnBuy.setText("续费");
            } else {
                holder.btnBuy.setText("开通会员");
                holder.tvData.setText("观看更多视频请开通会员");
            }
            holder.tvTitle.setText(item.title);
            Glide.with(MymemberActivity.this).load(item.poster).into(holder.ivContent);

            holder.btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MymemberActivity.this, MembersDetailedActivity.class);
                    intent.putExtra("subjectInfo", item);
                    intent.putExtra("isMy", 1);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivContent;
        Button btnBuy;
        TextView tvData;
        TextView tvTitle;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_member;
    }
}
