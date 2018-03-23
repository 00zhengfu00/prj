package com.physicmaster.modules.ranking.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.BaseFragment2;
import com.physicmaster.modules.mine.activity.friend.FriendInfoActivity;
import com.physicmaster.net.response.ranking.GetRankingResponse;
import com.physicmaster.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songrui on 17/6/29.
 */

public class WeekRankingFragment extends BaseFragment2 {


    private ListView lvRanking;
    private FragmentActivity mContext;
    private GetRankingResponse.DataBean.RankBean.UserRankBean userRank;
    private List<GetRankingResponse.DataBean.RankBean.RankListBean> rankLists = new ArrayList<>();
    private RankingAdapter rankingAdapter;
    private RoundImageView ivHeader;
    private TextView tvUserName;
    private TextView tvRanking;
    private TextView tvLevel;
    private TextView tvIntegral;
    private ImageView ivMembers;


    @Override
    protected void initView(View view) {
        mContext = getActivity();

        lvRanking = (ListView) view.findViewById(R.id.lv_ranking);

        View headView = LayoutInflater.from(mContext).inflate(R.layout.ranking_head, null);
        ivHeader = (RoundImageView) headView.findViewById(R.id.iv_header);
        tvUserName = (TextView) headView.findViewById(R.id.tv_user_name);
        tvRanking = (TextView) headView.findViewById(R.id.tv_ranking);
        tvIntegral = (TextView) headView.findViewById(R.id.tv_integral);
        ivMembers = (ImageView) headView.findViewById(R.id.iv_members);

        TextView tvNote = view.findViewById(R.id.tv_login_note);
        Bundle arguments = getArguments();
        if (arguments.getBoolean("isHide")) {
            lvRanking.setVisibility(View.GONE);
            tvNote.setVisibility(View.VISIBLE);
            return;
        } else {
            lvRanking.setVisibility(View.VISIBLE);
            tvNote.setVisibility(View.GONE);
        }
        GetRankingResponse.DataBean.RankBean dataBean = arguments.getParcelable("bean");
        if (dataBean != null) {
            userRank = dataBean.userRank;
            rankLists = dataBean.rankList;

            tvUserName.setText(userRank.n + "");
            tvRanking.setText(userRank.s + "");
            tvIntegral.setText(userRank.v + "");

            if (!TextUtils.isEmpty(userRank.p)) {
                Glide.with(mContext).load(userRank.p).placeholder(R.drawable
                        .placeholder_gray).into(ivHeader);
            }

            if (userRank.m == 0) {
                ivMembers.setVisibility(View.GONE);
            } else if (userRank.m == 1) {
                ivMembers.setVisibility(View.VISIBLE);
                ivMembers.setImageResource(R.mipmap.huiyuan1);
            } else if (userRank.m == 2) {
                ivMembers.setVisibility(View.VISIBLE);
                ivMembers.setImageResource(R.mipmap.huiyuan2);
            } else if (userRank.m == 3) {
                ivMembers.setVisibility(View.VISIBLE);
                ivMembers.setImageResource(R.mipmap.huiyuan3);

            }
            if (0 == BaseApplication.getUserData().isTourist) {
                lvRanking.addHeaderView(headView);
            }
            rankingAdapter = new RankingAdapter();
            lvRanking.setAdapter(rankingAdapter);
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_week_ranking;
    }


    @Override
    public void fetchData() {
    }

    class RankingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return rankLists == null ? 0 : rankLists.size();

        }

        @Override
        public GetRankingResponse.DataBean.RankBean.RankListBean getItem(int position) {
            return rankLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.list_item_rankings, null);
                holder = new ViewHolder();
                holder.ivHeader = (RoundImageView) convertView
                        .findViewById(R.id.iv_header);
                holder.tvName = (TextView) convertView
                        .findViewById(R.id.tv_name);
                holder.tvNumber = (TextView) convertView
                        .findViewById(R.id.tv_number);
                holder.tvIntegral = (TextView) convertView
                        .findViewById(R.id.tv_integral);
                holder.ivMembers = (ImageView) convertView
                        .findViewById(R.id.iv_members);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final GetRankingResponse.DataBean.RankBean.RankListBean item = getItem(position);

            holder.tvNumber.setText(item.o + "");
            holder.tvName.setText(item.n);
            holder.tvIntegral.setText(item.v + "");

            if (!TextUtils.isEmpty(item.p)) {
                Glide.with(mContext).load(item.p).placeholder(R.drawable
                        .placeholder_gray).into(holder.ivHeader);
            }
            holder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FriendInfoActivity.class);
                    intent.putExtra("dtUserId", item.id + "");
                    startActivity(intent);
                }
            });
            holder.ivHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FriendInfoActivity.class);
                    intent.putExtra("dtUserId", item.id + "");
                    startActivity(intent);
                }
            });
            if (item.m == 0) {
                holder.ivMembers.setVisibility(View.GONE);
            } else if (item.m == 1) {
                holder.ivMembers.setVisibility(View.VISIBLE);
                holder.ivMembers.setImageResource(R.mipmap.huiyuan1);
            } else if (item.m == 2) {
                holder.ivMembers.setVisibility(View.VISIBLE);
                holder.ivMembers.setImageResource(R.mipmap.huiyuan2);
            } else if (item.m == 3) {
                holder.ivMembers.setVisibility(View.VISIBLE);
                holder.ivMembers.setImageResource(R.mipmap.huiyuan3);

            }

            if (position == 0) {
                holder.tvNumber.setTextColor(getResources().getColor(R.color.transparent));
                holder.tvNumber.setBackgroundResource(R.mipmap.paihang1);
                holder.tvIntegral.setTextColor(getResources().getColor(R.color.colorRank));
            } else if (position == 1) {
                holder.tvNumber.setTextColor(getResources().getColor(R.color.transparent));
                holder.tvIntegral.setTextColor(getResources().getColor(R.color.colorRank));
                holder.tvNumber.setBackgroundResource(R.mipmap.paihang2);
            } else if (position == 2) {
                holder.tvNumber.setTextColor(getResources().getColor(R.color.transparent));
                holder.tvNumber.setBackgroundResource(R.mipmap.paihang3);
                holder.tvIntegral.setTextColor(getResources().getColor(R.color.colorRank));
            } else {
                holder.tvNumber.setTextColor(getResources().getColor(R.color.colorLineGray));
                holder.tvNumber.setBackgroundColor(Color.TRANSPARENT);
                holder.tvIntegral.setTextColor(getResources().getColor(R.color.colorDarkBlue));
            }

            return convertView;
        }

    }

    static class ViewHolder {
        TextView tvName;
        TextView tvNumber;
        ImageView ivMembers;
        TextView tvIntegral;
        RoundImageView ivHeader;
    }
}
