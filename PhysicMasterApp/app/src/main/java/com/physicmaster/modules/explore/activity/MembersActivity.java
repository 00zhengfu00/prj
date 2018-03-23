package com.physicmaster.modules.explore.activity;

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
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.MemberListBean;
import com.physicmaster.net.response.user.MemebersResponse;
import com.physicmaster.net.service.user.MembersService;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.TitleBuilder;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class MembersActivity extends BaseActivity {
    private ListView lvMembers;
    private MembersAdapter membersAdapter;
    private ImageView imageHead;
    private RelativeLayout rlHead;

    @Override
    protected void findViewById() {
        rlHead = (RelativeLayout) LayoutInflater.from(MembersActivity.this).inflate(R.layout.member_header, null);
        imageHead = rlHead.findViewById(R.id.image);
        int maxHeight = ScreenUtils.get16_9ImageMaxHeight(this, 0);
        imageHead.setAdjustViewBounds(true);
        imageHead.setMaxHeight(maxHeight);
        imageHead.setScaleType(ImageView.ScaleType.FIT_XY);
        lvMembers = (ListView) findViewById(R.id.lv_members);
        initTitle();
    }

    private void initTitle() {
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("会员专区");
    }

    @Override
    protected void initView() {
        getMembers();
    }

    private void getMembers() {
        final MembersService service = new MembersService(MembersActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<MemebersResponse>() {
            @Override
            public void onGetData(final MemebersResponse data) {
                if (data.data.superMember != null) {
                    Glide.with(MembersActivity.this).load(data.data.superMember.poster).placeholder(R.drawable.placeholder_gray).into(imageHead);
                    imageHead.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MobclickAgent.onEvent(MembersActivity.this, "member_open_all_step1");
                            Intent intent = new Intent();
                            intent.setClass(MembersActivity.this, Members2Activity.class);
                            intent.putExtra("memberBanner", data.data.superMember);
                            startActivity(intent);
                        }
                    });
                    lvMembers.addHeaderView(rlHead);
                }
                membersAdapter = new MembersAdapter(data.data.memberList);
                lvMembers.setAdapter(membersAdapter);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(MembersActivity.this, errorMsg);
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
            return memberList == null ? 0 : memberList.size();
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
                convertView = View.inflate(MembersActivity.this,
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
            int maxHeight = ScreenUtils.get16_9ImageMaxHeight(MembersActivity.this, 20);
            holder.ivContent.setAdjustViewBounds(true);
            holder.ivContent.setMaxHeight(maxHeight);
            holder.ivContent.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(MembersActivity.this).load(item.poster).placeholder(R.drawable.placeholder_gray).into(holder.ivContent);

            holder.btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(MembersActivity.this, "member_open_step1");
                    Intent intent = new Intent(MembersActivity.this, MembersDetailedActivity.class);
                    intent.putExtra("subjectInfo", item);
                    intent.putExtra("isMy", 0);
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
        return R.layout.activity_members;
    }
}
