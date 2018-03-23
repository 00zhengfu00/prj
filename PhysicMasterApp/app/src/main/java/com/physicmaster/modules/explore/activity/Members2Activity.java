package com.physicmaster.modules.explore.activity;

import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.net.response.explore.GetExploreResponse.DataBean.SuperMemberBean;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.TitleBuilder;
import com.umeng.analytics.MobclickAgent;

public class Members2Activity extends BaseActivity {

    private Button btnBuy;
    private TextView tvPrice;
    private TextView tvPriceYue;
    private Button btnDaifu;
    private TextView tvTitle;
    private TextView tvPriceYue2;
    private TextView tvPrice2;
    private TextView btnBuy2;
    private SuperMemberBean superMember;

    @Override
    protected void findViewById() {
        tvPriceYue = (TextView) findViewById(R.id.tv_price_yue);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        btnBuy = (Button) findViewById(R.id.btn_buy);

        tvPriceYue2 = (TextView) findViewById(R.id.tv_price_yue2);
        tvPrice2 = (TextView) findViewById(R.id.tv_price2);
        btnBuy2 = (Button) findViewById(R.id.btn_buy2);

        btnDaifu = (Button) findViewById(R.id.btn_daifu);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        superMember = getIntent().getParcelableExtra("memberBanner");
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
                .setMiddleTitleText("会员超值购");
    }

    @Override
    protected void initView() {

        final String name1 = "加入数理化学大师";
        String uname1 = "(初中)";
        String str1 = name1 + uname1;
        final SpannableStringBuilder sp1 = new SpannableStringBuilder(str1);
        sp1.setSpan(new AbsoluteSizeSpan(15, true), name1.length(), str1.length(), Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
        tvTitle.setText(sp1);

        final String name2 = "每月仅需";
        String uname2 = superMember.items.get(0).monthPrice;
        String str2 = name2 + uname2;
        final SpannableStringBuilder sp2 = new SpannableStringBuilder(str2);
        sp2.setSpan(new ForegroundColorSpan(0xFFFCBE01), name2.length(), str2.length(), Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        tvPriceYue.setText(sp2);
        tvPrice.setText(superMember.items.get(0).title + "");


        final String name3 = "每月仅需";
        String uname3 = superMember.items.get(1).monthPrice;
        String str3 = name3 + uname3;
        final SpannableStringBuilder sp3 = new SpannableStringBuilder(str3);
        sp3.setSpan(new ForegroundColorSpan(0xFFFCBE01), name3.length(), str3.length(), Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        tvPriceYue2.setText(sp3);
        tvPrice2.setText(superMember.items.get(1).title + "");


        btnDaifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.getUserData().isTourist == 1) {
                    Utils.gotoLogin(Members2Activity.this);
                    return;
                }
                MobclickAgent.onEvent(Members2Activity.this, "member_open_all_step2_parents_pay");
                Intent intent = new Intent(Members2Activity.this, SelectWayActivity.class);
                intent.putExtra("memberItemId", -1);
                startActivity(intent);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.getUserData().isTourist == 1) {
                    Utils.gotoLogin(Members2Activity.this);
                    return;
                }
                MobclickAgent.onEvent(Members2Activity.this, "member_open_all_step2_buy_12month");
                Intent intent = new Intent(Members2Activity.this, PayMentActivity.class);
                intent.putExtra("memberItemId", superMember.items.get(0).memberItemId);
                intent.putExtra("isMy", 0);
                startActivity(intent);
            }
        });

        btnBuy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.getUserData().isTourist == 1) {
                    Utils.gotoLogin(Members2Activity.this);
                    return;
                }
                MobclickAgent.onEvent(Members2Activity.this, "member_open_all_step2_buy_6month");
                Intent intent = new Intent(Members2Activity.this, PayMentActivity.class);
                intent.putExtra("memberItemId", superMember.items.get(1).memberItemId);
                intent.putExtra("isMy", 0);
                startActivity(intent);
            }
        });

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_members2;
    }
}
