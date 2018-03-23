package com.physicmaster.modules.mine.activity.friend;

import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.discuss.NimLoginService;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.CommonResponse;
import com.physicmaster.net.response.im.FindFriendV2Response;
import com.physicmaster.net.response.im.GetRecommendFriendsResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.im.FindFriendV2Service;
import com.physicmaster.net.service.im.GetRecommendFriendsService;
import com.physicmaster.net.service.im.InviteFriendService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.PullToRefreshLayout;
import com.physicmaster.widget.RoundImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AddFriendsActivity extends BaseActivity {

    private ImageView ivBack;
    private TextView tvFindFriend;
    private EditText etFindFriend;

    private String name;
    private List<FindFriendV2Response.DataBean.UserVoListBean> mFriendList;
    private AddFriendAdapter mAddFriendAdapter;
    private PullToRefreshLayout pullToRefreshLayout;
    private int nextRange;
    private boolean isRecommend = true;

    @Override
    protected void findViewById() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvFindFriend = (TextView) findViewById(R.id.tv_find_friend);
        etFindFriend = (EditText) findViewById(R.id.et_find_friend);

        mFriendList = new ArrayList<>();
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!TextUtils.isEmpty(name)) {
                    findFriendV2();
                } else {
                    pullToRefreshLayout.stopRefresh();
                }
            }

            @Override
            public void onPullup(int maxId) {
                if (-1 == maxId) {
                    return;
                }
                findMoreFriend(maxId);
            }
        });
        pullToRefreshLayout.getListView().setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(AddFriendsActivity.this, FriendInfoActivity.class);
            intent.putExtra("dtUserId", id + "");
            intent.putExtra("position", position);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                int position = data.getIntExtra("position", -1);
                boolean isAdded = data.getBooleanExtra("isAdded", false);
                if (isAdded && position != -1) {
                    mFriendList.get(position).added = true;
                    mAddFriendAdapter.notifyDataSetChanged();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvFindFriend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etFindFriend.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    UIUtils.showToast(AddFriendsActivity.this, "输入不能为空！");
                    return;
                }
                findFriendV2();
            }
        });

        etFindFriend.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() &&
                        KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    name = etFindFriend.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        UIUtils.showToast(AddFriendsActivity.this, "输入不能为空！");
                        return false;
                    }
                    findFriendV2();
                }
                return true;
            }
        });

        findViewById(R.id.tv_find_address_friend).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFriendsActivity.this, AddressListActivity.class);
                startActivity(intent);
            }
        });
        getRecommendFriends();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_friends;
    }

    /**
     * 查找更多好友（分页）
     */
    private void findMoreFriend(int maxId) {
        final FindFriendV2Service service = new FindFriendV2Service(this);
        service.setCallback(new IOpenApiDataServiceCallback<FindFriendV2Response>() {
            @Override
            public void onGetData(FindFriendV2Response data) {
                List<FindFriendV2Response.DataBean.UserVoListBean> searchList = data.data
                        .userVoList;
                mFriendList.addAll(searchList);
                nextRange = data.data.nextRange;
                pullToRefreshLayout.notifyData(data.data.nextId, data.data
                        .userVoList, true);
                mAddFriendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(AddFriendsActivity.this, "查找失败");
            }
        });
        service.postLogined("query=" + name + "&nextId=" + maxId + "&nextRange=" +
                nextRange, false);
    }

    /**
     * 查找好友V2
     *
     * @param
     */
    private void findFriendV2() {
        isRecommend = false;
        final FindFriendV2Service service = new FindFriendV2Service(this);
        service.setCallback(new IOpenApiDataServiceCallback<FindFriendV2Response>() {
            @Override
            public void onGetData(FindFriendV2Response data) {
                mFriendList.clear();
                mFriendList.addAll(data.data.userVoList);
                nextRange = data.data.nextRange;
                mAddFriendAdapter = new AddFriendAdapter();
                pullToRefreshLayout.getListView().setAdapter(mAddFriendAdapter);
                pullToRefreshLayout.notifyData(data.data.nextId, data.data.userVoList, false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(AddFriendsActivity.this, errorMsg);
            }
        });
        String encodeName = "";
        try {
            encodeName = URLEncoder.encode(name, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        service.postLogined("query=" + encodeName, false);
    }

    class AddFriendAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFriendList.size();
        }

        @Override
        public FindFriendV2Response.DataBean.UserVoListBean getItem(int position) {
            return mFriendList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mFriendList.get(position).dtUserId;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(AddFriendsActivity.this,
                        R.layout.list_item_addfriend, null);
                holder = new ViewHolder();
                holder.ivHeader = (RoundImageView) convertView
                        .findViewById(R.id.iv_header);
                holder.ivGender = (ImageView) convertView
                        .findViewById(R.id.iv_gender);
                holder.tvUserName = (TextView) convertView
                        .findViewById(R.id.tv_user_name);
                holder.tvIntroduction = (TextView) convertView
                        .findViewById(R.id.tv_introduction);
                holder.btnAdd = (Button) convertView
                        .findViewById(R.id.btn_add);
                holder.tvAdded = (TextView) convertView
                        .findViewById(R.id.tv_added);
                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            final FindFriendV2Response.DataBean.UserVoListBean item = getItem(position);

            String name = item.nickname;
            String uname = "Lv" + item.userLevel;
            String str = name + uname;
            final SpannableStringBuilder sp = new SpannableStringBuilder(str);
            sp.setSpan(new ForegroundColorSpan(0xFFFCBE01), name.length(), str.length(), Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            sp.setSpan(new AbsoluteSizeSpan(14, true), name.length(), str.length(), Spanned
                    .SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
            holder.tvUserName.setText(sp);
            if (isRecommend) {
                holder.tvIntroduction.setVisibility(View.VISIBLE);
                holder.tvIntroduction.setText("[来自系统推荐]");
            } else {
                holder.tvIntroduction.setVisibility(View.GONE);
            }

            if (1 == item.gender) {
                holder.ivGender.setVisibility(View.VISIBLE);
                holder.ivGender.setImageResource(R.mipmap.nan);
            } else if (2 == item.gender) {
                holder.ivGender.setVisibility(View.VISIBLE);
                holder.ivGender.setImageResource(R.mipmap.nv);
            } else {
                holder.ivGender.setVisibility(View.GONE);
            }
            Glide.with(AddFriendsActivity.this).load(item.portrait).placeholder(R.drawable
                    .placeholder_gray)
                    .into(holder.ivHeader);
            holder.btnAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    inviteFriend(item.dtUserId, position);
                }
            });
            if (item.friendState == 0) {
                holder.tvAdded.setVisibility(View.GONE);
                holder.btnAdd.setVisibility(View.VISIBLE);

                if (item.added) {
                    holder.tvAdded.setVisibility(View.VISIBLE);
                    holder.btnAdd.setVisibility(View.GONE);
                    holder.tvAdded.setText("等待验证");
                } else {
                    holder.tvAdded.setVisibility(View.GONE);
                    holder.btnAdd.setVisibility(View.VISIBLE);
                }

            } else if (item.friendState == 1) {
                holder.tvAdded.setVisibility(View.VISIBLE);
                holder.btnAdd.setVisibility(View.GONE);
                holder.tvAdded.setText("已是好友");
            } else if (item.friendState == 2) {
                holder.tvAdded.setVisibility(View.VISIBLE);
                holder.btnAdd.setVisibility(View.GONE);
                holder.tvAdded.setText("待同意");
            } else if (item.friendState == 3) {
                holder.tvAdded.setVisibility(View.VISIBLE);
                holder.btnAdd.setVisibility(View.GONE);
                holder.tvAdded.setText("待验证");
            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvIntroduction;
        TextView tvUserName;
        RoundImageView ivHeader;
        ImageView ivGender;
        TextView tvAdded;
        Button btnAdd;
    }

    /**
     * 请求添加某人为好友
     *
     * @param userId
     */
    private void inviteFriend(int userId, final int position) {
        final InviteFriendService service = new InviteFriendService(this);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                loadingDialog.dismissDialog();
                mFriendList.get(position).added = true;
                UIUtils.showToast(AddFriendsActivity.this, "发送成功");
                mAddFriendAdapter.notifyDataSetChanged();
                UserDataResponse.UserDataBean.LoginVoBean loginVoBean = BaseApplication
                        .getUserData();
                if (0 == loginVoBean.isImAutoConnect) {
                    Intent loginIntent = new Intent(AddFriendsActivity.this, NimLoginService.class);
                    loginIntent.putExtra("account", loginVoBean.imUserId);
                    loginIntent.putExtra("token", loginVoBean.imToken);
                    startService(loginIntent);
                    loginVoBean.isImAutoConnect = 1;
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                            .USERINFO_LOGINVO, loginVoBean);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                loadingDialog.dismissDialog();
                UIUtils.showToast(AddFriendsActivity.this, "发送失败：" + errorMsg);
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("invitationDtUserId=" + userId, false);
    }

    /**
     * 获取系统推荐好友
     */
    private void getRecommendFriends() {
        GetRecommendFriendsService service = new GetRecommendFriendsService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetRecommendFriendsResponse>() {
            @Override
            public void onGetData(GetRecommendFriendsResponse data) {
                mFriendList.clear();
                mFriendList.addAll(data.data.userVoList);
                mAddFriendAdapter = new AddFriendAdapter();
                pullToRefreshLayout.getListView().setAdapter(mAddFriendAdapter);
                pullToRefreshLayout.notifyData(-1, data.data.userVoList, false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        service.postLogined("", false);
    }
}
