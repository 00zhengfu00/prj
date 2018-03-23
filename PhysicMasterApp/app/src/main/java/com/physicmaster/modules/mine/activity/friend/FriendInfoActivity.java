package com.physicmaster.modules.mine.activity.friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.NimUIKit;
import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.BaseFragmentActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.CommonResponse;
import com.physicmaster.net.response.im.GetFriendDetailResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.im.DeleteFriendService;
import com.physicmaster.net.service.im.GetFriendDetailService;
import com.physicmaster.net.service.im.InviteFriendService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.DeleteFriendDialog;
import com.physicmaster.widget.MoreGridView;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.RoundImageView;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class FriendInfoActivity extends BaseFragmentActivity {


    private UserDataResponse.UserDataBean.LoginVoBean mDataBean;
    private RoundImageView ivHeaders;
    private TextView tvNickName, tvIntroduction, tvGoldCoin;
    private int userId;
    private ImageView ivGender;
    private TextView tvDay;
    private TextView tvStudyTime;
    private TextView tvFinishSection;
    private Button btnSendMsg;
    private MoreGridView childGridView;
    private TextView tvFoot;
    private TextView tvHead;
    private List<GetFriendDetailResponse.DataBean.UserDetailVoBean.MedalListBean> medalList;
    private TitleBuilder titleBuilder;
    private int position;
    private boolean addFriend = false;

    @Override
    protected void findViewById() {
        ivHeaders = (RoundImageView) findViewById(R.id.iv_header);
        ivGender = (ImageView) findViewById(R.id.iv_gender);
        tvNickName = (TextView) findViewById(R.id.tv_user_name);
        tvIntroduction = (TextView) findViewById(R.id.tv_introduction);

        tvDay = (TextView) findViewById(R.id.tv_day);
        tvStudyTime = (TextView) findViewById(R.id.tv_study_time);
        tvFinishSection = (TextView) findViewById(R.id.tv_finish_section);
        childGridView = (MoreGridView) findViewById(R.id.child_gridView);
        btnSendMsg = (Button) findViewById(R.id.btn_send_msg);
        tvFoot = (TextView) findViewById(R.id.tv_foot);
        tvHead = (TextView) findViewById(R.id.tv_head);

        tvFoot.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        initTitle();
    }


    @Override
    protected void onResume() {
        super.onResume();
        String id = getIntent().getStringExtra("dtUserId");
        getFriendDetail(id);
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        titleBuilder = new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("position", position);
                        intent.putExtra("isAdded", addFriend);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setMiddleTitleText("详细资料");
    }

    private void deleteFriend() {
        DeleteFriendDialog dialog = new DeleteFriendDialog();
        dialog.setOnBack(new DeleteFriendDialog.OnBack() {
            @Override
            public void click(int btn) {
                if (R.id.btn_delete_friend == btn) {
                    doDeleteFriend(userId);
                }
            }
        });
        dialog.show(getSupportFragmentManager(), "deleteFriend");
    }

    @Override
    protected void initView() {
        position = getIntent().getIntExtra("position", -1);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_friend_info;
    }

    /**
     * 获取好友信息详情
     */
    private void getFriendDetail(String userId) {
        final GetFriendDetailService service = new GetFriendDetailService(this);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetFriendDetailResponse>() {
            @Override
            public void onGetData(GetFriendDetailResponse data) {
                refreshUI(data.data.userDetailVo);
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(FriendInfoActivity.this, "获取用户信息失败");
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("dtUserId=" + userId, false);
    }

    /**
     * 刷新用户信息
     */
    private void refreshUI(GetFriendDetailResponse.DataBean.UserDetailVoBean friendInfo) {
        userId = friendInfo.dtUserId;
        Glide.with(this).load(friendInfo.portrait).placeholder(R.drawable
                .placeholder_gray)
                .into(ivHeaders);
        if (1 == friendInfo.gender) {
            ivGender.setVisibility(View.VISIBLE);
            ivGender.setImageResource(R.mipmap.nan);
        } else if (2 == friendInfo.gender) {
            ivGender.setVisibility(View.VISIBLE);
            ivGender.setImageResource(R.mipmap.nv);
        } else {
            ivGender.setVisibility(View.GONE);
        }

        final String name = friendInfo.nickname;
        String uname = "Lv" + friendInfo.userLevel;
        String str = name + uname;
        final SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new ForegroundColorSpan(0xFFFCBE01), name.length(), str.length(), Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        sp.setSpan(new AbsoluteSizeSpan(14, true), name.length(), str.length(), Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
        tvNickName.setText(sp);
        tvIntroduction.setText(friendInfo.intro);
        tvDay.setText(friendInfo.userStudyInfo.studyDays);
        tvStudyTime.setText(friendInfo.userStudyInfo.studyTime);
        tvFinishSection.setText(friendInfo.userStudyInfo.completeCourseCount);
        final List<GetFriendDetailResponse.DataBean.UserDetailVoBean.MedalListBean> medalListAll =
                friendInfo.medalList;
        if (null != medalListAll && medalListAll.size() > 0) {
            String sex = (friendInfo.gender == 2) ? "她的勋章（" : "他的勋章（";
            tvHead.setText(sex + medalListAll.size() + "）");
            medalList = new ArrayList<>();
            if (medalListAll.size() > 3) {
                medalList.add(medalListAll.get(0));
                medalList.add(medalListAll.get(1));
                medalList.add(medalListAll.get(2));
                tvFoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medalList.clear();
                        medalList.addAll(medalListAll);
                        childGridView.setAdapter(new MyGridViewAdapter(FriendInfoActivity.this,
                                medalList));
                        tvFoot.setVisibility(View.GONE);
                    }
                });
                tvFoot.setVisibility(View.VISIBLE);
            } else {
                medalList.addAll(medalListAll);
            }
            childGridView.setAdapter(new MyGridViewAdapter(this, medalList));
        } else {
            tvFoot.setVisibility(View.GONE);
        }
        final int state = friendInfo.friendState;
        final String imUserId = friendInfo.imUserId;
        //对方的dtUserId
        final String dtUserId = friendInfo.dtUserId + "";
        if (state == GetFriendDetailResponse.DataBean.UserDetailVoBean.FRIEND) {
            btnSendMsg.setVisibility(View.VISIBLE);
            btnSendMsg.setText("发消息");
            titleBuilder.setRightImageRes(R.mipmap.shanchu_haoyou)
                    .setRightTextOrImageListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteFriend();
                        }
                    });
        } else if (state == GetFriendDetailResponse.DataBean.UserDetailVoBean.USER_SELF) {
        } else if (state == GetFriendDetailResponse.DataBean.UserDetailVoBean.BE_CONFIRM) {
            btnSendMsg.setVisibility(View.VISIBLE);
            btnSendMsg.setEnabled(false);
            btnSendMsg.setText("等待验证");
        } else if (state == GetFriendDetailResponse.DataBean.UserDetailVoBean.CONFIRMED) {
            btnSendMsg.setVisibility(View.VISIBLE);
            btnSendMsg.setEnabled(false);
            btnSendMsg.setText("等待验证");
        } else {
            btnSendMsg.setVisibility(View.VISIBLE);
            btnSendMsg.setEnabled(true);
            btnSendMsg.setText("加好友");
        }
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == GetFriendDetailResponse.DataBean.UserDetailVoBean.FRIEND) {
                    if (TextUtils.isEmpty(imUserId)) {
                        UIUtils.showToast(FriendInfoActivity.this, "对方还是老版本用户，无法使用聊天功能哦");
                        return;
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", BaseApplication.getUserData().dtUserId);
                    jsonObject.put("account", imUserId);
                    jsonObject.put("nickName", name);
                    jsonObject.put("isFriend", true);
                    if (BaseApplication.getStartupDataBean().imAdminList != null &&
                            BaseApplication.getStartupDataBean().imAdminList.size() > 0) {
                        jsonObject.put("adminUserId", BaseApplication.getStartupDataBean()
                                .imAdminList.get(0).imUserId);
                    }
                    NimUIKit.startP2PSession(FriendInfoActivity.this, jsonObject.toJSONString());
                } else if (state == GetFriendDetailResponse.DataBean.UserDetailVoBean.NO_RELATION) {
                    if (BaseApplication.getUserData().isTourist == 1) {
                        Utils.gotoLogin(FriendInfoActivity.this);
                        return;
                    }
                    inviteFriend(userId);
                }
            }
        });
    }

    /**
     * 删除好友
     *
     * @param userId
     */
    private void doDeleteFriend(int userId) {
        DeleteFriendService service = new DeleteFriendService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                UIUtils.showToast(FriendInfoActivity.this, "删除成功");
                LocalBroadcastManager.getInstance(FriendInfoActivity.this).sendBroadcast(new
                        Intent(FriendsActivity.FRIEND_DELETED));
                FriendInfoActivity.this.finish();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(FriendInfoActivity.this, "删除失败");
            }
        });
        service.postLogined("relationDtUserId=" + userId, false);
    }

    /**
     * 请求添加某人为好友
     *
     * @param userId
     */
    private void inviteFriend(int userId) {
        final InviteFriendService service = new InviteFriendService(this);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                loadingDialog.dismissDialog();
                UIUtils.showToast(FriendInfoActivity.this, "发送成功");
                btnSendMsg.setText("等待验证");
                btnSendMsg.setEnabled(false);
                addFriend = true;
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                loadingDialog.dismissDialog();
                UIUtils.showToast(FriendInfoActivity.this, "发送失败：" + errorMsg);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("isAdded", addFriend);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * GridView 适配器
     */
    public class MyGridViewAdapter extends BaseAdapter {

        private Context mContext;

        /**
         * 每个分组下的每个子项的 GridView 数据集合
         */
        private List<GetFriendDetailResponse.DataBean.UserDetailVoBean.MedalListBean> itemGridList;

        public MyGridViewAdapter(Context mContext, List<GetFriendDetailResponse.DataBean
                .UserDetailVoBean.MedalListBean>
                itemGridList) {
            this.mContext = mContext;
            this.itemGridList = itemGridList;
        }

        @Override
        public int getCount() {
            return itemGridList.size();
        }

        @Override
        public GetFriendDetailResponse.DataBean.UserDetailVoBean.MedalListBean getItem(int position) {
            return itemGridList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemViewHolder holder;
            if (null == convertView) {
                convertView = View.inflate(FriendInfoActivity.this, R.layout.gridview_item, null);
                holder = new ItemViewHolder();
                holder.tvGridView = (TextView) convertView.findViewById(R.id.tv_gridview);
                holder.ivGridView = (ImageView) convertView.findViewById(R.id.iv_gridview);
                convertView.setTag(holder);
            } else {
                holder = (ItemViewHolder) convertView.getTag();
            }
            GetFriendDetailResponse.DataBean.UserDetailVoBean.MedalListBean item = getItem
                    (position);
            Glide.with(FriendInfoActivity.this).load(item.medalImg).into(holder.ivGridView);
            holder.tvGridView.setText(item.medalName + "");
            return convertView;
        }
    }

    static class ItemViewHolder {
        TextView tvGridView;
        ImageView ivGridView;
    }
}

