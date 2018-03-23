package com.physicmaster.modules.study.fragment.infromation;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.BaseFragment2;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.discuss.NimLoginService;
import com.physicmaster.modules.mine.activity.friend.FriendInfoActivity;
import com.physicmaster.modules.study.activity.MsgChangeListener;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.CommonResponse;
import com.physicmaster.net.response.im.FriendSearchBean;
import com.physicmaster.net.response.im.GetInvitationListResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.im.AgreeInvatationService;
import com.physicmaster.net.service.im.GetInvitationListService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.RoundImageView;

import java.util.ArrayList;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/18 13:32
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class FriendFragment extends BaseFragment2 {

    private ArrayList<FriendSearchBean> mFriendList = new ArrayList<>();
    private FragmentActivity mContext;
    private FriendAdapter friendAdapter;
    private RelativeLayout rlHave;
    private RelativeLayout rlEmpty;
    private Button btnAddAll;
    private int invitationStatus;
    private MsgChangeListener msgChangeListener;

    @Override
    protected void initView(View view) {
        mContext = getActivity();

        ListView lvEnergy = (ListView) rootView.findViewById(R.id.lv_friend);
        rlHave = (RelativeLayout) rootView.findViewById(R.id.rl_have);
        rlEmpty = (RelativeLayout) rootView.findViewById(R.id.rl_empty);
        btnAddAll = (Button) rootView.findViewById(R.id.btn_agree);
        btnAddAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mFriendList && mFriendList.size() > 0) {
                    String[] ids = new String[mFriendList.size()];
                    for (int i = 0; i < mFriendList.size(); i++) {
                        ids[i] = mFriendList.get(i).dtUserId + "";
                    }
                    agreeInvitation(ids, -1);
                }
            }
        });
        friendAdapter = new FriendAdapter();
        lvEnergy.setAdapter(friendAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend;
    }

    @Override
    public void fetchData() {
        getInvitations();
    }

    class FriendAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFriendList.size();
        }

        @Override
        public Object getItem(int position) {
            return mFriendList.get(position);
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
                        R.layout.list_item_friend, null);
                holder = new ViewHolder();
                holder.ivHeader = (RoundImageView) convertView
                        .findViewById(R.id.iv_header);
                holder.tvUserName = (TextView) convertView
                        .findViewById(R.id.tv_user_name);
                holder.tvAddFriend = (TextView) convertView
                        .findViewById(R.id.tv_add_friend);
                holder.btnAgree = (Button) convertView
                        .findViewById(R.id.btn_agree);
                holder.ivDelete = (ImageView) convertView
                        .findViewById(R.id.iv_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final FriendSearchBean item = (FriendSearchBean) getItem(position);
            holder.tvUserName.setText(item.nickname);
            Glide.with(mContext.getApplicationContext()).load(item.portraitSmall).placeholder(R
                    .drawable.placeholder_gray)
                    .into(holder.ivHeader);
            holder.ivHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FriendInfoActivity.class);
                    intent.putExtra("dtUserId", item.dtUserId + "");
                    startActivity(intent);
                }
            });
            if (!item.added) {
                holder.btnAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] userIds = new String[1];
                        userIds[0] = item.dtUserId + "";
                        agreeInvitation(userIds, position);
                    }
                });
                holder.btnAgree.setText("同意");
                holder.btnAgree.setEnabled(true);
            } else {
                if (invitationStatus == 1) {
                    holder.btnAgree.setText("已同意");
                    holder.btnAgree.setEnabled(false);
                } else {
                    holder.btnAgree.setText("已拒绝");
                    holder.btnAgree.setEnabled(false);
                }

            }
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] userIds = new String[1];
                    userIds[0] = item.dtUserId + "";
                    refuseInvitation(userIds, position);
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvAddFriend;
        TextView tvUserName;
        RoundImageView ivHeader;
        ImageView ivDelete;
        Button btnAgree;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 获取好友申请列表接口
     */
    private void getInvitations() {
        final GetInvitationListService service = new GetInvitationListService(mContext);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<GetInvitationListResponse>() {
            @Override
            public void onGetData(GetInvitationListResponse data) {
                mFriendList.clear();
                mFriendList.addAll(data.data.friendRelationList);
                friendAdapter.notifyDataSetChanged();
                if (mFriendList.size() == 0) {
                    rlHave.setVisibility(View.GONE);
                    btnAddAll.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);
                } else {
                    rlHave.setVisibility(View.VISIBLE);
                    btnAddAll.setVisibility(View.VISIBLE);
                    rlEmpty.setVisibility(View.GONE);
                }
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
                rlHave.setVisibility(View.GONE);
                btnAddAll.setVisibility(View.GONE);
                rlEmpty.setVisibility(View.VISIBLE);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("", true);
    }

    /**
     * 同意好友申请
     */
    private void agreeInvitation(String[] invitations, final int position) {
        invitationStatus = 1;
        AgreeInvatationService service = new AgreeInvatationService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                UIUtils.showToast(mContext, "已同意");
                if (position == -1) {
                    for (FriendSearchBean bean : mFriendList) {
                        bean.added = true;
                    }
                    rlHave.setVisibility(View.GONE);
                    btnAddAll.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);
                    if (msgChangeListener != null) {
                        msgChangeListener.onMsgChanged(2, -1);
                    }
                } else {
                    mFriendList.get(position).added = true;
                    mFriendList.remove(position);
                    if (mFriendList.size() == 0) {
                        rlHave.setVisibility(View.GONE);
                        btnAddAll.setVisibility(View.GONE);
                        rlEmpty.setVisibility(View.VISIBLE);
                        if (msgChangeListener != null) {
                            msgChangeListener.onMsgChanged(2, -1);
                        }
                    } else {
                        if (msgChangeListener != null) {
                            msgChangeListener.onMsgChanged(2, 1);
                        }
                    }
                }
                friendAdapter.notifyDataSetChanged();
                UserDataResponse.UserDataBean.LoginVoBean loginVoBean = BaseApplication
                        .getUserData();
                if (0 == loginVoBean.isImAutoConnect) {
                    Intent loginIntent = new Intent(getActivity(), NimLoginService.class);
                    loginIntent.putExtra("account", loginVoBean.imUserId);
                    loginIntent.putExtra("token", loginVoBean.imToken);
                    getActivity().startService(loginIntent);
                    loginVoBean.isImAutoConnect = 1;
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, loginVoBean);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        String invitationJson = JSON.toJSONString(invitations);
        service.postLogined("userIds=" + invitationJson + "&invitationStatus=" +
                invitationStatus, false);
    }

    /**
     * 拒绝好友申请
     */
    private void refuseInvitation(String[] invitations, final int position) {
        invitationStatus = 2;
        AgreeInvatationService service = new AgreeInvatationService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                UIUtils.showToast(mContext, "已拒绝");
                if (position == -1) {
                    for (FriendSearchBean bean : mFriendList) {
                        bean.added = true;
                    }
                    rlHave.setVisibility(View.GONE);
                    btnAddAll.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);
                    if (msgChangeListener != null) {
                        msgChangeListener.onMsgChanged(2, -1);
                    }
                } else {
                    mFriendList.get(position).added = true;
                    mFriendList.remove(position);

                    if (mFriendList.size() == 0) {
                        btnAddAll.setVisibility(View.GONE);
                        rlHave.setVisibility(View.GONE);
                        rlEmpty.setVisibility(View.VISIBLE);
                        if (msgChangeListener != null) {
                            msgChangeListener.onMsgChanged(2, -1);
                        }
                    } else {
                        if (msgChangeListener != null) {
                            msgChangeListener.onMsgChanged(2, 1);
                        }
                    }
                }

                friendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        String invitationJson = JSON.toJSONString(invitations);
        service.postLogined("userIds=" + invitationJson + "&invitationStatus=" +
                invitationStatus, false);
    }

    public void setMsgChangeListener(MsgChangeListener msgChangeListener) {
        this.msgChangeListener = msgChangeListener;
    }
}
