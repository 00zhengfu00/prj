package com.physicmaster.modules.explore.activity;

import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.net.response.user.MemberListBean;
import com.physicmaster.widget.TitleBuilder;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class MembersListActivity extends BaseActivity {

    private ListView                       lvMembers;
    private MemberListBean                 subjectInfo;
    private MembersAdapter                 membersAdapter;
    private List<MemberListBean.ItemsBean> memberList;
    private int                            isMy;
    private int type = -1;

    @Override
    protected void findViewById() {
        lvMembers = (ListView) findViewById(R.id.lv_members);
        Button btnDaifu = (Button) findViewById(R.id.btn_daifu);
        TextView tvContent = (TextView) findViewById(R.id.tv_content);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);

        initTitle();

        subjectInfo = getIntent().getParcelableExtra("subjectInfo");
        isMy = getIntent().getIntExtra("isMy", -1);

        if (subjectInfo.subjectId == 1) {
            tvTitle.setText("加入物理大师 (初中)");
            tvContent.setText("爸妈担心你的学习？物理大师帮你告诉家人如何快速提高成绩");
        } else if (subjectInfo.subjectId == 2) {
            tvTitle.setText("加入化学大师 (初中)");
            tvContent.setText("爸妈担心你的学习？化学大师帮你告诉家人如何快速提高成绩");
        } else if (subjectInfo.subjectId == 4) {
            tvTitle.setText("加入数学大师 (初中)");
            tvContent.setText("爸妈担心你的学习？数学大师帮你告诉家人如何快速提高成绩");
        }
        btnDaifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(MembersListActivity.this, "member_open_step3_parents_pay");
                Intent intent = new Intent(MembersListActivity.this, SelectWayActivity.class);
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
        memberList = subjectInfo.items;
        membersAdapter = new MembersAdapter();
        lvMembers.setAdapter(membersAdapter);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_members_list;
    }

    class MembersAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return memberList == null ? 0 : memberList.size();
        }

        @Override
        public MemberListBean.ItemsBean getItem(int position) {
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
                convertView = View.inflate(MembersListActivity.this,
                        R.layout.list_item_members_list, null);
                holder = new ViewHolder();
                holder.ivTuijian = (ImageView) convertView
                        .findViewById(R.id.iv_tuijian);
                holder.tvPriceYue = (TextView) convertView
                        .findViewById(R.id.tv_price_yue);

                holder.tv = (TextView) convertView
                        .findViewById(R.id.tv);

                holder.tv1 = (TextView) convertView
                        .findViewById(R.id.tv1);
                holder.tvGold = (TextView) convertView
                        .findViewById(R.id.tv_gold);


                holder.tvPrice = (TextView) convertView
                        .findViewById(R.id.tv_price);
                holder.btnBuy = (Button) convertView
                        .findViewById(R.id.btn_buy);
                holder.select = (RadioButton) convertView.findViewById(R.id.id_select);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final MemberListBean.ItemsBean item = getItem(position);

            if (item.isRecommend == 1) {
                holder.ivTuijian.setVisibility(View.VISIBLE);
            } else {
                holder.ivTuijian.setVisibility(View.GONE);
            }
            if (item.validDays == 30) {
                type = 1;
                holder.tv.setVisibility(View.GONE);
                holder.tv1.setVisibility(View.GONE);
                holder.tvPrice.setText(item.title + "");
                holder.tvGold.setText("赠送100积分，20金币");
            } else if (item.validDays == 180) {
                type = 6;
                holder.tv.setVisibility(View.GONE);
                holder.tv1.setVisibility(View.GONE);
                holder.tvPrice.setText(item.title + "");
                holder.tvGold.setText("赠送500积分，100金币");

            } else if (item.validDays == 365) {
                type = 12;
                holder.tv.setVisibility(View.VISIBLE);
                if (subjectInfo.subjectId == 1) {
                    holder.tv.setText("赠送物理大师必练题");
                } else if (subjectInfo.subjectId == 2) {
                    holder.tv.setText("赠送化学大师必练题");
                } else if (subjectInfo.subjectId == 4) {
                    holder.tv.setText("赠送数学大师必练题");
                }
                holder.tv1.setVisibility(View.VISIBLE);
                holder.tvPrice.setText(item.title + "");
                holder.tvGold.setText("赠送1000积分，200金币");
            }
            if (TextUtils.isEmpty(item.monthPrice)) {
                holder.tvPriceYue.setText("一个月尝鲜");
            } else {
                final String name = "每月仅需";
                String uname = item.monthPrice;
                String str = name + uname;
                final SpannableStringBuilder sp = new SpannableStringBuilder(str);
                sp.setSpan(new ForegroundColorSpan(0xFFFCBE01), name.length(), str.length(), Spanned
                        .SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
                holder.tvPriceYue.setText(sp);
            }

            holder.btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 1) {
                        MobclickAgent.onEvent(MembersListActivity.this, "member_open_step3_buy_1month");
                    } else if (type == 6) {
                        MobclickAgent.onEvent(MembersListActivity.this, "member_open_step3_buy_6month");
                    } else if (type == 12) {
                        MobclickAgent.onEvent(MembersListActivity.this, "member_open_step3_buy_12month");
                    }
                    Intent intent = new Intent(MembersListActivity.this, PayMentActivity.class);
                    intent.putExtra("memberItemId", item.memberItemId);
                    intent.putExtra("isMy", isMy);
                    startActivity(intent);

                }
            });

            return convertView;
        }
    }

    static class ViewHolder {
        Button      btnBuy;
        TextView    tvGold;
        TextView    tv;
        TextView    tv1;
        TextView    tvPriceYue;
        TextView    tvPrice;
        RadioButton select;
        ImageView   ivTuijian;
    }
}
