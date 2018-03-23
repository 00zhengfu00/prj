package com.physicmaster.modules.mine.activity.friend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.im.FriendListBean;
import com.physicmaster.net.response.im.GetFriendListResponse;
import com.physicmaster.net.service.im.GetFriendListService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.RoundImageView;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends BaseActivity {

    private ListView lvFriends;
    private SwipeRefreshLayout container;
    private LocalBroadcastManager localBroadcastManager;
    public static final String FRIEND_DELETED = "friend_deleted";
    private FriendsAdapter friendsAdapter;
    private RelativeLayout rlEmpty;

    private List<FriendListBean> mFriendsList;
    private EditText etSearch;

    @Override
    protected void findViewById() {
        lvFriends = (ListView) findViewById(R.id.lv_friends);
        container = (SwipeRefreshLayout) findViewById(R.id.container);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_empty);

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
                .setMiddleTitleText("我的好友")
                .setRightImageRes(R.mipmap.xinzeng)
                .setRightTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(FriendsActivity.this, AddFriendsActivity.class));
                    }
                });
    }

    @Override
    protected void initView() {
        View header = View.inflate(this, R.layout.layout_search, null);
        etSearch = (EditText) header.findViewById(R.id.et_find_friend);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<FriendListBean> serchedFriends;
                if (TextUtils.isEmpty(s.toString())) {
                    serchedFriends = mFriendsList;
                } else {
                    String searchContent = s.toString();
                    serchedFriends = new ArrayList<FriendListBean>();
                    for (FriendListBean friendListBean : mFriendsList) {
                        String nickName = friendListBean.nickname;
                        if (nickName.contains(searchContent)) {
                            serchedFriends.add(friendListBean);
                        }
                    }
                }
                friendsAdapter = new FriendsAdapter(serchedFriends);
                lvFriends.setAdapter(friendsAdapter);
                friendsAdapter.notifyDataSetChanged();
                etSearch.requestFocus();
            }
        });
        lvFriends.addHeaderView(header);
        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(FriendsActivity.this, FriendInfoActivity.class);
                intent.putExtra("dtUserId", id + "");
                startActivity(intent);
            }
        });
        container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFriendList();
            }
        });
        container.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(friendsChanged, new IntentFilter(FRIEND_DELETED));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_friends;
    }

    class FriendsAdapter extends BaseAdapter {

        private List<FriendListBean> mFriendsList;

        public FriendsAdapter(List<FriendListBean> mFriendsList) {
            this.mFriendsList = mFriendsList;
        }

        @Override
        public int getCount() {
            return mFriendsList.size();
        }

        @Override
        public FriendListBean getItem(int position) {
            return mFriendsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mFriendsList.get(position).dtUserId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(FriendsActivity.this,
                        R.layout.list_item_my_friend, null);
                holder = new ViewHolder();

                holder.ivHeader = (RoundImageView) convertView
                        .findViewById(R.id.iv_header);
                holder.ivGender = (ImageView) convertView
                        .findViewById(R.id.iv_gender);
                holder.tvUserName = (TextView) convertView
                        .findViewById(R.id.tv_user_name);
                holder.tvIntroduction = (TextView) convertView
                        .findViewById(R.id.tv_introduction);
                holder.tvIntegral = (TextView) convertView
                        .findViewById(R.id.tv_integral);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            FriendListBean item = getItem(position);

            Glide.with(FriendsActivity.this).load(item.portraitSmall).placeholder(R.drawable
                    .placeholder_gray)
                    .into(holder.ivHeader);
            if (1 == item.gender) {
                holder.ivGender.setVisibility(View.VISIBLE);
                holder.ivGender.setImageResource(R.mipmap.nan);
            } else if (2 == item.gender) {
                holder.ivGender.setVisibility(View.VISIBLE);
                holder.ivGender.setImageResource(R.mipmap.nv);
            } else {
                holder.ivGender.setVisibility(View.GONE);
            }

            String name = item.nickname;
            String uname = "Lv" + item.userLevel;
            String str = name + uname;
            final SpannableStringBuilder sp = new SpannableStringBuilder(str);
            sp.setSpan(new ForegroundColorSpan(0xFFFCBE01), name.length(), str.length(), Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            sp.setSpan(new AbsoluteSizeSpan(14, true), name.length(), str.length(), Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
            holder.tvUserName.setText(sp);

            //holder.tvIntegral.setText("积分   " + item.point + "   ");
            holder.tvIntroduction.setText(item.decs + "");
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvUserName;
        TextView tvIntroduction;
        ImageView ivGender;
        TextView tvIntegral;
        RoundImageView ivHeader;
    }

    /**
     * 获取好友列表
     */
    private void getFriendList() {
        final GetFriendListService service = new GetFriendListService(this);
        container.setRefreshing(true);
        service.setCallback(new IOpenApiDataServiceCallback<GetFriendListResponse>() {
            @Override
            public void onGetData(GetFriendListResponse data) {
                mFriendsList = data.data.friendList;
                if (null == mFriendsList || mFriendsList.size() == 0) {
                    rlEmpty.setVisibility(View.VISIBLE);
                    container.setVisibility(View.GONE);
                } else {
                    rlEmpty.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                    friendsAdapter = new FriendsAdapter(mFriendsList);
                    lvFriends.setAdapter(friendsAdapter);
                    container.setRefreshing(false);
                }
                etSearch.setText("");
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(FriendsActivity.this, errorMsg);
                rlEmpty.setVisibility(View.VISIBLE);
                container.setVisibility(View.GONE);
                container.setRefreshing(false);
            }
        });
        service.postLogined("", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFriendList();
    }

    /**
     * 删除好友消息监听
     */
    private BroadcastReceiver friendsChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getFriendList();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (friendsChanged != null) {
            localBroadcastManager.unregisterReceiver(friendsChanged);
        }
    }
}
