package com.physicmaster.modules.discuss.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.OnlineStateChangeListener;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.ui.recyclerview.listener.SimpleClickListener;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.recent.AitHelper;
import com.netease.nim.uikit.recent.RecentContactsCallback;
import com.netease.nim.uikit.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.session.SessionEventListener;
import com.netease.nim.uikit.uinfo.UserInfoHelper;
import com.netease.nim.uikit.uinfo.UserInfoObservable;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseActivityManager;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.BaseFragment;
import com.physicmaster.base.MainActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.modules.account.LoginActivity;
import com.physicmaster.modules.account.LogoutHelper;
import com.physicmaster.modules.discuss.NimLoginService;
import com.physicmaster.modules.discuss.activity.AllQuestionActivity;
import com.physicmaster.modules.discuss.activity.QuestionPublishActivity;
import com.physicmaster.modules.discuss.config.preference.Preferences;
import com.physicmaster.modules.mine.activity.friend.FriendInfoActivity;
import com.physicmaster.modules.mine.activity.friend.FriendsActivity;
import com.physicmaster.modules.mine.activity.question.MessageActivity;
import com.physicmaster.modules.mine.activity.question.QuestionDetailsActivity;
import com.physicmaster.modules.mine.activity.school.Item;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.db.VideoManager;
import com.physicmaster.modules.videoplay.cache.service.DownloadService2;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.ExitLoginResponse;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.discuss.DiscussBannerResponse;
import com.physicmaster.net.response.im.GetOtherUserInfoByImResponse;
import com.physicmaster.net.response.im.GetOtherUserInfoResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.ExitLoginService;
import com.physicmaster.net.service.discuss.DiscussBannerService;
import com.physicmaster.net.service.im.GetOtherUserInfoByImService;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.MyPopupWindow;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2017/5/3 15:18
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class DiscussFragment extends BaseFragment implements View.OnClickListener {

    private FragmentActivity mContext;
    private ImageView ivNote;
    private List<Item> list = new ArrayList<>();
    //聊天列表
    // 置顶功能可直接使用，也可作为思路，供开发者充分利用RecentContact的tag字段
    public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag

    // view
    private RecyclerView recyclerView;

    private View emptyBg;

    private TextView emptyHint;

    // data
    private List<RecentContact> items;

    private Map<String, RecentContact> cached; // 暂缓刷上列表的数据（未读数红点拖拽动画运行时用）

    private RecentContactAdapter adapter;

    private boolean msgLoaded = false;

    private RecentContactsCallback callback;

    private UserInfoObservable.UserInfoObserver userInfoObserver;
    private LocalBroadcastManager localBroadcastManager;

    private QuestionCycleView questionCycleView;
    private TextView tvQuestionAll;
    private List<DiscussBannerResponse.DataBean.QaQuestionVosBean> arrayList;
    private LinearLayout llAdd;

    private static final String TAG = "DiscussFragment";
    private Logger logger = AndroidLogger.getLogger(TAG);

    private int subjectId, eduGrade;
    private View header;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_discuss;
    }

    @Override
    protected void initView() {
        mContext = getActivity();
        llAdd = (LinearLayout) rootView.findViewById(R.id.ll_add);
        ivNote = (ImageView) rootView.findViewById(R.id.iv_note);
        list.add(new Item("提问题"));
        list.add(new Item("发起会话"));
        llAdd.setOnClickListener(this);
        rootView.findViewById(R.id.ll_message).setOnClickListener(this);
        findViews();
        initMessageList();
        registerObservers(true);
        registerDropCompletedListener(true);
        registerOnlineStateChangeListener(true);
        registerEventListener();
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(nimLoginReceiver, new IntentFilter(NimLoginService.NIM_LOGIN_SUCC));
    }

    private void registerEventListener() {
        NimUIKit.setSessionListener(new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                Map<String, Object> extensionMap = message.getRemoteExtension();
                if (extensionMap != null) {
                    String userId = (String) extensionMap.get("id");
                    if (!TextUtils.isEmpty(userId)) {
                        Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
                        intent.putExtra("dtUserId", userId);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {

            }
        });
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo(final RecentContact recent) {
        GetOtherUserInfoByImService service = new GetOtherUserInfoByImService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<GetOtherUserInfoByImResponse>() {
            @Override
            public void onGetData(GetOtherUserInfoByImResponse data) {
                GetOtherUserInfoResponse.DataBean.UserDetailVoBean userDetailVo = data.data
                        .userVo;
                if (recent.getSessionType() == SessionTypeEnum.Team) {
                    NimUIKit.startTeamSession(getActivity(), recent.getContactId());
                } else if (recent.getSessionType() == SessionTypeEnum.P2P) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", BaseApplication.getUserData().dtUserId);
                    if (BaseApplication.getStartupDataBean().imAdminList != null &&
                            BaseApplication.getStartupDataBean().imAdminList.size() > 0) {
                        jsonObject.put("adminUserId", BaseApplication.getStartupDataBean()
                                .imAdminList.get(0).imUserId);
                    }
                    jsonObject.put("account", recent.getContactId());
                    //是好友
                    if (userDetailVo.friendState == 1) {
                        jsonObject.put("isFriend", true);
                    }
                    NimUIKit.startP2PSession(getActivity(), jsonObject.toJSONString());
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getActivity(), "获取聊天信息失败");
            }
        });
        service.postLogined("imUserId=" + recent.getContactId(), false);
    }

    /**
     * 获取用户信息
     */
    private void updateUserInfo(final AssistantContact recent) {
        GetOtherUserInfoByImService service = new GetOtherUserInfoByImService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<GetOtherUserInfoByImResponse>() {
            @Override
            public void onGetData(GetOtherUserInfoByImResponse data) {
                GetOtherUserInfoResponse.DataBean.UserDetailVoBean userDetailVo = data.data
                        .userVo;
                recent.setBackupName(userDetailVo.nickname);
                onRecentContactsLoaded();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
            }
        });
        service.postLogined("imUserId=" + recent.getContactId(), false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 获取科目类型
     */
    private String getSubjectType() {
        String param = "";
        String subjectInfo = SpUtils.getString(getActivity(), CacheKeys.SUBJECT_STUDY_INFO, "");
        String gradeInfo = SpUtils.getString(getActivity(), CacheKeys.GRAGE_INFO, "");
        if (TextUtils.isEmpty(subjectInfo)) {
            String packageName = mContext.getPackageName();
            if (Constant.PHYSICMASTER.equals(packageName)) {
                subjectId = 1;
            } else if (Constant.CHYMISTMASTER.equals(packageName)) {
                subjectId = 2;
            } else if (Constant.MATHMASTER.equals(packageName)) {
                subjectId = 4;
            }
        } else {
            subjectId = Integer.parseInt(subjectInfo);
        }
        if (TextUtils.isEmpty(gradeInfo)) {
            UserDataResponse.UserDataBean.LoginVoBean mDataBean = BaseApplication.getUserData();
            if (null != mDataBean) {
                eduGrade = mDataBean.eduGrade;
            } else {
                ((BaseActivity) getActivity()).gotoLoginActivity();
            }
        } else {
            eduGrade = Integer.parseInt(gradeInfo);
        }
        param = "subjectId=" + subjectId + "&gradeId=" + eduGrade;
        return param;
    }

    /**
     * 获取banner信息
     */
    private void getBannerInfo() {
        String param = getSubjectType();
        DiscussBannerService service = new DiscussBannerService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<DiscussBannerResponse>() {
            @Override
            public void onGetData(DiscussBannerResponse data) {
                if (data.data.newsCount > 0) {
                    ivNote.setImageResource(R.mipmap.new_xiaoxi);
                } else {
                    ivNote.setImageResource(R.mipmap.xiaoxi);
                }
                arrayList = data.data.qaQuestionVos;
                if (null == arrayList || 0 == arrayList.size()) {
                    return;
                }
                if (header == null) {
                    header = View.inflate(getActivity(), R.layout.layout_question_circle, null);
                    adapter.addHeaderView(header);
                }
                questionCycleView = (QuestionCycleView) header.findViewById(R.id.icv_qu_circle);
                tvQuestionAll = (TextView) header.findViewById(R.id.tv_question_all);
                tvQuestionAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, AllQuestionActivity.class);
                        startActivity(intent);
                    }
                });
                questionCycleView.setCycleData(data.data.qaQuestionVos, new QuestionCycleView
                        .ImageCycleViewListener() {

                    @Override
                    public void displayImage(String imageURL, ImageView imageView) {
                        Glide.with(getActivity()).load(imageURL).placeholder(R.color
                                .colorBackgound).into(imageView);
                    }

                    @Override
                    public void onImageClick(int position, View imageView) {
                        Intent intent = new Intent(mContext, QuestionDetailsActivity.class);
                        intent.putExtra("questionId", arrayList.get(position).qid);
                        mContext.startActivity(intent);
                    }
                });
                //获取聊天列表信息
                requestMessages(true);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                //获取聊天列表信息
                requestMessages(true);
            }
        });
        service.postLogined(param, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        getBannerInfo();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add:
                new MyPopupWindow(mContext, new MyPopupWindow.OnSubmitListener() {
                    @Override
                    public void onSubmit(int position) {
                        if (BaseApplication.getUserData().isTourist == 1) {
                            Utils.gotoLogin(getActivity());
                            return;
                        }
                        if (position == 0) {
                            startActivity(new Intent(getActivity(), QuestionPublishActivity.class));
                        } else if (position == 1) {
                            startActivity(new Intent(getActivity(), FriendsActivity.class));
                        }
                    }
                }, new MyPopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                    }
                }, list).showPopupWindow(llAdd);
                break;
            case R.id.ll_message:
                Intent intent = new Intent(mContext, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.message_list_empty_hint:
                if (BaseApplication.getUserData().isTourist == 1) {
                    Utils.gotoLogin(getActivity());
                    return;
                }
                startActivity(new Intent(mContext, FriendsActivity.class));
                break;
            default:
                break;
        }
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
//        boolean empty = items.isEmpty() && msgLoaded;
        boolean empty = items.isEmpty();
        if (empty) {
            if (emptyBg.getParent() == null) {
                adapter.addHeaderView(emptyBg);
            }
        } else {
            adapter.removeHeaderView(emptyBg);
        }
        emptyHint.setHint("还没有会话，在通讯录中找个人聊聊吧！");
        emptyHint.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerDropCompletedListener(false);
        registerOnlineStateChangeListener(false);
        if (nimLoginReceiver != null && localBroadcastManager != null) {
            localBroadcastManager.unregisterReceiver(nimLoginReceiver);
        }
    }

    /**
     * 查找页面控件
     */
    private void findViews() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        emptyBg = View.inflate(getActivity(), R.layout.list_empty_view, null);
        emptyHint = (TextView) emptyBg.findViewById(R.id.message_list_empty_hint);
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {
        items = new ArrayList<>();
        cached = new HashMap<>(3);

        // adapter
        adapter = new RecentContactAdapter(recyclerView, items);
        initCallBack();
        adapter.setCallback(callback);

        // recyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(touchListener);

        // drop listener
        DropManager.getInstance().setDropListener(new DropManager.IDropListener() {
            @Override
            public void onDropBegin() {
                touchListener.setShouldDetectGesture(false);
            }

            @Override
            public void onDropEnd() {
                touchListener.setShouldDetectGesture(true);
            }
        });
    }

    /**
     * 是否是默认加载会话
     *
     * @param account
     * @return
     */
    private boolean isDashiZhushou(String account) {
        List<StartupResponse.DataBean.IMAdmin> adminList = BaseApplication
                .getStartupDataBean().imAdminList;
        for (StartupResponse.DataBean.IMAdmin imAdmin : adminList) {
            if (imAdmin.imUserId.equals(account)) {
                return true;
            }
        }
        return false;
    }

    private void initCallBack() {
        if (callback != null) {
            return;
        }
        callback = new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {

            }

            @Override
            public void onItemClick(final RecentContact recent) {
                if (isDashiZhushou(recent.getContactId())) {
                    UserDataResponse.UserDataBean.LoginVoBean loginVoBean = BaseApplication.getUserData();
                    if (0 == loginVoBean.isImAutoConnect) {
                        Intent loginIntent = new Intent(getActivity(), NimLoginService.class);
                        loginIntent.putExtra("account", loginVoBean.imUserId);
                        loginIntent.putExtra("token", loginVoBean.imToken);
                        getActivity().startService(loginIntent);
                        loginVoBean.isImAutoConnect = 1;
                        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                                .USERINFO_LOGINVO, loginVoBean);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", BaseApplication.getUserData().dtUserId);
                    jsonObject.put("p", BaseApplication.getUserData().portraitSmall);
                    jsonObject.put("account", recent.getContactId());
                    jsonObject.put("isFriend", true);
                    NimUIKit.startP2PSession(getActivity(), jsonObject.toJSONString());
                } else {
                    //先查询是否是好友
                    getUserInfo(recent);
                }
            }

            @Override
            public String getDigestOfAttachment(MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        };
    }

    private SimpleClickListener<RecentContactAdapter> touchListener = new
            SimpleClickListener<RecentContactAdapter>() {
                @Override
                public void onItemClick(RecentContactAdapter adapter, View view, int position) {
                    if (callback != null) {
                        RecentContact recent = adapter.getItem(position);
                        callback.onItemClick(recent);
                    }
                }

                @Override
                public void onItemLongClick(RecentContactAdapter adapter, View view, int position) {
                    showLongClickMenu(adapter.getItem(position), position);
                }

                @Override
                public void onItemChildClick(RecentContactAdapter adapter, View view, int
                        position) {

                }

                @Override
                public void onItemChildLongClick(RecentContactAdapter adapter, View view, int
                        position) {

                }
            };

    OnlineStateChangeListener onlineStateChangeListener = new OnlineStateChangeListener() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            notifyDataSetChanged();
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKit.enableOnlineState()) {
            return;
        }
        if (register) {
            NimUIKit.addOnlineStateChangeListeners(onlineStateChangeListener);
        } else {
            NimUIKit.removeOnlineStateChangeListeners(onlineStateChangeListener);
        }
    }

    private void showLongClickMenu(final RecentContact recent, final int position) {
        if (isDashiZhushou(recent.getContactId())) {
            return;
        }
        CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
        alertDialog.setTitle(UserInfoHelper.getUserTitleName(recent.getContactId(), recent
                .getSessionType()));
        String title = getString(R.string.main_msg_list_delete_chatting);
        alertDialog.addItem(title, new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                // 删除会话，删除后，消息历史被一起删除
                NIMClient.getService(MsgService.class).deleteRecentContact(recent);
                NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId()
                        , recent.getSessionType());
                adapter.remove(position);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshMessages(true);
                    }
                });
            }
        });

        title = (isTagSet(recent, RECENT_TAG_STICKY) ? getString(R.string
                .main_msg_list_clear_sticky_on_top) : getString(R.string
                .main_msg_list_sticky_on_top));
        alertDialog.addItem(title, new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                if (isTagSet(recent, RECENT_TAG_STICKY)) {
                    removeTag(recent, RECENT_TAG_STICKY);
                } else {
                    addTag(recent, RECENT_TAG_STICKY);
                }
                NIMClient.getService(MsgService.class).updateRecent(recent);
                refreshMessages(false);
            }
        });

        //        alertDialog.addItem("删除该聊天（仅服务器）", new CustomAlertDialog
        // .onSeparateItemClickListener() {
        //            @Override
        //            public void onClick() {
        //                NIMClient.getService(MsgService.class)
        //                        .deleteRoamingRecentContact(recent.getContactId(), recent
        // .getSessionType())
        //                        .setCallback(new RequestCallback<Void>() {
        //                            @Override
        //                            public void onSuccess(Void param) {
        //                                Toast.makeText(getActivity(), "delete success", Toast
        //                                        .LENGTH_SHORT).show();
        //                            }
        //
        //                            @Override
        //                            public void onFailed(int code) {
        //                                Toast.makeText(getActivity(), "delete failed, code:" +
        // code,
        //                                        Toast.LENGTH_SHORT).show();
        //                            }
        //
        //                            @Override
        //                            public void onException(Throwable exception) {
        //
        //                            }
        //                        });
        //            }
        //        });
        alertDialog.show();
    }

    private void addTag(RecentContact recent, long tag) {
        tag = recent.getTag() | tag;
        recent.setTag(tag);
    }

    private void removeTag(RecentContact recent, long tag) {
        tag = recent.getTag() & ~tag;
        recent.setTag(tag);
    }

    private boolean isTagSet(RecentContact recent, long tag) {
        return (recent.getTag() & tag) == tag;
    }

    private List<AssistantContact> loadedRecents;

    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (msgLoaded) {
                    return;
                }
                UserDataResponse.UserDataBean.LoginVoBean loginVoBean = BaseApplication
                        .getUserData();
                if (loginVoBean != null && 0 == loginVoBean.isImAutoConnect) {
                    loadedRecents = new ArrayList<AssistantContact>();
                    if (isAdded()) {
                        onRecentContactsLoaded();
                    }
                    return;
                }
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable
                            exception) {
                        if (1000 == code) {
                            recents = new ArrayList<RecentContact>();
                        } else if (recents == null || 0 == recents.size()) {
                            //最近聊天列表为空，肯定没有添加大师助手
                            recents = new ArrayList<RecentContact>();
                            List<StartupResponse.DataBean.IMAdmin> imAdminList = BaseApplication
                                    .getStartupDataBean().imAdminList;
                            if (imAdminList != null) {
                                for (int i = 0; i < imAdminList.size(); i++) {
                                    AssistantContact contact = new AssistantContact();
                                    contact.setFromAccount(imAdminList.get(i).imUserId);
                                    contact.setContactId(imAdminList.get(i).imUserId);
                                    contact.setFromNick(imAdminList.get(i).nickname);
                                    contact.setTag(2);
                                    contact.setMsgType(MsgTypeEnum.custom);
                                    contact.setTime(System.currentTimeMillis());
                                    contact.setSessionType(SessionTypeEnum.P2P);
                                    contact.setMsgStatus(MsgStatusEnum.success);
                                    recents.add(contact);
                                }
                            }
                        } else {
                            //最近聊天列表不为空，但是用户没有和大师助手对话过，本地就没有存储记录，所以也需要添加
                            List<StartupResponse.DataBean.IMAdmin> adminList = BaseApplication
                                    .getStartupDataBean().imAdminList;
                            List<StartupResponse.DataBean.IMAdmin> unAddDashiZhushou = new
                                    ArrayList<>();
                            if (null != adminList) {
                                for (StartupResponse.DataBean.IMAdmin imAdmin : adminList) {
                                    String adminImId = imAdmin.imUserId;
                                    boolean add = true;
                                    for (RecentContact recent : recents) {
                                        String contactId = recent.getContactId();
                                        if (contactId.equals(adminImId)) {
                                            add = false;
                                            break;
                                        }
                                    }
                                    if (add) {
                                        unAddDashiZhushou.add(imAdmin);
                                    }
                                }
                                for (StartupResponse.DataBean.IMAdmin imAdmin : unAddDashiZhushou) {
                                    AssistantContact contact = new AssistantContact();
                                    contact.setFromAccount(imAdmin.imUserId);
                                    contact.setContactId(imAdmin.imUserId);
                                    contact.setFromNick(imAdmin.nickname);
                                    contact.setMsgType(MsgTypeEnum.custom);
                                    contact.setTime(System.currentTimeMillis());
                                    contact.setSessionType(SessionTypeEnum.P2P);
                                    contact.setMsgStatus(MsgStatusEnum.success);
                                    recents.add(contact);
                                }
                            }
                        }
                        loadedRecents = new ArrayList<AssistantContact>();
                        // 初次加载，更新离线的消息中是否有@我的消息
                        for (RecentContact loadedRecent : recents) {
                            AssistantContact contact = new AssistantContact();
                            contact.setExtension(loadedRecent.getExtension());
                            contact.setFromAccount(loadedRecent.getFromAccount());
                            contact.setContactId(loadedRecent.getContactId());
                            contact.setFromNick(loadedRecent.getFromNick());
                            contact.setMsgType(loadedRecent.getMsgType());
                            contact.setTime(loadedRecent.getTime());
                            contact.setSessionType(loadedRecent.getSessionType());
                            contact.setMsgStatus(loadedRecent.getMsgStatus());
                            contact.setAttachment(loadedRecent.getAttachment());
                            contact.setUnreadCount(loadedRecent.getUnreadCount());
                            contact.setTag(loadedRecent.getTag());
                            contact.setContent(loadedRecent.getContent());
                            contact.setRecentMessageId(loadedRecent.getRecentMessageId());
                            if (loadedRecent.getSessionType() == SessionTypeEnum.Team) {
                                updateOfflineContactAited(loadedRecent);
                            }
                            loadedRecents.add(contact);
                            if (TextUtils.isEmpty(loadedRecent.getFromNick()) || TextUtils
                                    .isEmpty(loadedRecent.getFromNick().trim())) {
                                updateUserInfo(contact);
                            }
                        }
                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                        msgLoaded = true;
                        if (isAdded()) {
                            onRecentContactsLoaded();
                        }
                    }
                });
            }
        }, delay ? 250 : 0);
    }

    private void onRecentContactsLoaded() {
        items.clear();
        if (loadedRecents != null) {
            items.addAll(loadedRecents);
//            loadedRecents = null;
        }
        refreshMessages(true);

        if (callback != null) {
            callback.onRecentContactsLoaded();
        }
    }

    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(items);
        notifyDataSetChanged();

        if (unreadChanged) {

            // 方式一：累加每个最近联系人的未读（快）
            /*
            int unreadNum = 0;
            for (RecentContact r : items) {
                unreadNum += r.getUnreadCount();
            }
            */

            // 方式二：直接从SDK读取（相对慢）
            int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();

            if (callback != null) {
                callback.onUnreadCountChange(unreadNum);
            }
            if (0 == unreadNum) {
                Intent intent = new Intent(MainActivity.UNREADMSG);
                intent.putExtra("hasUnreadMsg", false);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            } else {
                Intent intent = new Intent(MainActivity.UNREADMSG);
                intent.putExtra("hasUnreadMsg", true);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<RecentContact> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private Comparator<RecentContact> comp = new Comparator<RecentContact>() {

        @Override
        public int compare(RecentContact o1, RecentContact o2) {
            if (isDashiZhushou(o1.getContactId())) {
                if (!isDashiZhushou(o2.getContactId())) {
                    return -1;
                } else {
                    long time = o1.getTime() - o2.getTime();
                    return time == 0 ? 0 : (time > 0 ? -1 : 1);
                }
            } else {
                if (isDashiZhushou(o2.getContactId())) {
                    return 1;
                }
            }
            // 先比较置顶tag
            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
            if (sticky != 0) {
                return sticky > 0 ? -1 : 1;
            } else {
                long time = o1.getTime() - o2.getTime();
                return time == 0 ? 0 : (time > 0 ? -1 : 1);
            }
        }
    };

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(messageReceiverObserver, register);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);

        registerTeamUpdateObserver(register);
        registerTeamMemberUpdateObserver(register);
        FriendDataCache.getInstance().registerFriendDataChangedObserver
                (friendDataChangedObserver, register);
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver,
                register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver,
                register);
    }

    Observer<List<OnlineClient>> clientsObserver = new Observer<List<OnlineClient>>() {
        @Override
        public void onEvent(List<OnlineClient> onlineClients) {
            //            SessionListFragment.this.onlineClients = onlineClients;
            //            if (onlineClients == null || onlineClients.size() == 0) {
            //                multiportBar.setVisibility(View.GONE);
            //            } else {
            //                multiportBar.setVisibility(View.VISIBLE);
            //                TextView status = (TextView) multiportBar.findViewById(R.id
            // .multiport_desc_label);
            //                OnlineClient client = onlineClients.get(0);
            //                switch (client.getClientType()) {
            //                    case ClientType.Windows:
            //                        status.setText(getString(R.string.multiport_logging) +
            // getString(R
            // .string.computer_version));
            //                        break;
            //                    case ClientType.Web:
            //                        status.setText(getString(R.string.multiport_logging) +
            // getString(R
            // .string.web_version));
            //                        break;
            //                    case ClientType.iOS:
            //                    case ClientType.Android:
            //                        status.setText(getString(R.string.multiport_logging) +
            // getString(R
            // .string.mobile_version));
            //                        break;
            //                    default:
            //                        multiportBar.setVisibility(View.GONE);
            //                        break;
            //                }
            //            }
        }
    };

    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                kickOut(code);
            } else {
                if (code == StatusCode.NET_BROKEN) {
                } else if (code == StatusCode.UNLOGIN) {
                } else if (code == StatusCode.CONNECTING) {
                } else if (code == StatusCode.LOGINING) {
                } else {
                }
            }
        }
    };

    private void kickOut(StatusCode code) {
        Preferences.saveUserToken("");

        if (code == StatusCode.PWD_ERROR) {
            LogUtil.e("Auth", "user password error");
            Toast.makeText(getActivity(), R.string.login_failed, Toast.LENGTH_SHORT).show();
        } else {
            LogUtil.i("Auth", "Kicked!");
        }
        onLogout();
    }

    // 注销
    private void onLogout() {
        // 清理缓存&注销监听&清除状态
        LogoutHelper.logout();
        exitLogin();
    }

    /**
     * 退出登录
     */
    private void exitLogin() {
        ExitLoginService service = new ExitLoginService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<ExitLoginResponse>() {
            @Override
            public void onGetData(ExitLoginResponse data) {
                logger.debug("退出登录成功：onGetData: " + data.msg);
                exit();
                BaseApplication.setNimLogin(false);
                getActivity().finish();
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(LoginActivity.KICK_OUT, true);
                startActivity(intent);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                exit();
                BaseApplication.setNimLogin(false);
                getActivity().finish();
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(LoginActivity.KICK_OUT, true);
                startActivity(intent);
            }
        });
        service.postLogined("", false);
    }

    /**
     * 退出登录
     */
    private void exit() {
        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO);
        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_USERKEY);
        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.OSS_SERVER_INFO);
        SpUtils.remove(mContext, CacheKeys.SUBJECT_STUDY_INFO);
        SpUtils.remove(mContext, CacheKeys.SUBJECT_ACTION);
        SpUtils.remove(mContext, CacheKeys.GRAGE_ACTION);
        SpUtils.remove(mContext, CacheKeys.SUBJECT_COURSE_INFO);
        SpUtils.remove(mContext, CacheKeys.SUBJECT_COURSE_INFO_STR);
        SpUtils.remove(mContext, CacheKeys.GRAGE_INFO);
        SpUtils.remove(mContext, "isSoundSwitch");
        clearWebViewCache();
        BaseApplication.setUserKey(null);
        BaseApplication.setUserData(null);
        BaseApplication.getStartupDataBean().ossConfig = null;
        BaseActivityManager.getInstance().popAllActivity();
        quitDownloading();
    }

    public void clearWebViewCache() {
        CookieSyncManager.createInstance(mContext);
        CookieManager.getInstance().removeAllCookie();
    }

    private void quitDownloading() {
        mContext.sendBroadcast(new Intent(DownloadService2.QUIT_DOWNLOADING));
        AsyncTask<String, Integer, Boolean> deleteDownloadingVideoTask = new
                AsyncTask<String, Integer, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        VideoManager videoManager = new VideoManager(mContext);
                        List<VideoInfo> videoInfoList = videoManager.getDownloadingVideos();
                        if (videoInfoList != null) {
                            for (VideoInfo videoInfo : videoInfoList) {
                                videoManager.deleteVideoByVideoId(videoInfo.getId());
                            }
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean bool) {
                        super.onPostExecute(bool);
                    }
                };
        deleteDownloadingVideoTask.execute("");
    }

    /**
     * 注册群信息&群成员更新监听
     */
    private void registerTeamUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
        }
    }

    private void registerTeamMemberUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamMemberDataChangedObserver
                    (teamMemberDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver
                    (teamMemberDataChangedObserver);
        }
    }

    private void registerDropCompletedListener(boolean register) {
        if (register) {
            DropManager.getInstance().addDropCompletedListener(dropCompletedListener);
        } else {
            DropManager.getInstance().removeDropCompletedListener(dropCompletedListener);
        }
    }

    // 暂存消息，当RecentContact 监听回来时使用，结束后清掉
    private Map<String, Set<IMMessage>> cacheMessages = new HashMap<>();

    //监听在线消息中是否有@我
    private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            if (imMessages != null) {
                for (IMMessage imMessage : imMessages) {
                    if (!AitHelper.isAitMessage(imMessage)) {
                        continue;
                    }
                    Set<IMMessage> cacheMessageSet = cacheMessages.get(imMessage.getSessionId());
                    if (cacheMessageSet == null) {
                        cacheMessageSet = new HashSet<>();
                        cacheMessages.put(imMessage.getSessionId(), cacheMessageSet);
                    }
                    cacheMessageSet.add(imMessage);
                }
            }
        }
    };

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            if (!DropManager.getInstance().isTouchable()) {
                // 正在拖拽红点，缓存数据
                for (RecentContact r : recentContacts) {
                    cached.put(r.getContactId(), r);
                }

                return;
            }

            onRecentContactChanged(recentContacts);
        }
    };

    private void onRecentContactChanged(List<RecentContact> recentContacts) {
        int index;
        for (RecentContact r : recentContacts) {
            index = -1;
            for (int i = 0; i < items.size(); i++) {
                if (r.getContactId().equals(items.get(i).getContactId())
                        && r.getSessionType() == (items.get(i).getSessionType())) {
                    index = i;
                    break;
                }
            }

            if (index >= 0) {
                items.remove(index);
            }

            items.add(r);
            if (r.getSessionType() == SessionTypeEnum.Team && cacheMessages.get(r.getContactId())
                    != null) {
                AitHelper.setRecentContactAited(r, cacheMessages.get(r.getContactId()));
            }
        }

        cacheMessages.clear();

        refreshMessages(true);
    }

    DropCover.IDropCompletedListener dropCompletedListener = new DropCover.IDropCompletedListener
            () {
        @Override
        public void onCompleted(Object id, boolean explosive) {
            if (cached != null && !cached.isEmpty()) {
                // 红点爆裂，已经要清除未读，不需要再刷cached
                if (explosive) {
                    if (id instanceof RecentContact) {
                        RecentContact r = (RecentContact) id;
                        cached.remove(r.getContactId());
                    } else if (id instanceof String && ((String) id).contentEquals("0")) {
                        cached.clear();
                    }
                }

                // 刷cached
                if (!cached.isEmpty()) {
                    List<RecentContact> recentContacts = new ArrayList<>(cached.size());
                    recentContacts.addAll(cached.values());
                    cached.clear();

                    onRecentContactChanged(recentContacts);
                }
            }
        }
    };

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                RecentContact item = items.get(index);
                item.setMsgStatus(message.getStatus());
                refreshViewHolderByIndex(index);
            }
        }
    };

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (RecentContact item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
                            && item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages(true);
            }
        }
    };

    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache
            .TeamDataChangedObserver() {

        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeam(Team team) {

        }
    };

    TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache
            .TeamMemberDataChangedObserver() {
        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {

        }
    };

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            RecentContact item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    protected void refreshViewHolderByIndex(final int index) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyItemChanged(index);
            }
        });
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }

    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    refreshMessages(false);
                }
            };
        }

        UserInfoHelper.registerObserver(userInfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            UserInfoHelper.unregisterObserver(userInfoObserver);
        }
    }

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache
            .FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessages(false);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessages(false);
        }
    };

    private void updateOfflineContactAited(final RecentContact recentContact) {
        if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team
                || recentContact.getUnreadCount() <= 0) {
            return;
        }

        // 锚点
        List<String> uuid = new ArrayList<>(1);
        uuid.add(recentContact.getRecentMessageId());

        List<IMMessage> messages = NIMClient.getService(MsgService.class)
                .queryMessageListByUuidBlock(uuid);

        if (messages == null || messages.size() < 1) {
            return;
        }
        final IMMessage anchor = messages.get(0);

        // 查未读消息
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum
                        .QUERY_OLD,
                recentContact.getUnreadCount() - 1, false).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {

            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && result != null) {
                    result.add(0, anchor);
                    Set<IMMessage> messages = null;
                    // 过滤存在的@我的消息
                    for (IMMessage msg : result) {
                        if (AitHelper.isAitMessage(msg)) {
                            if (messages == null) {
                                messages = new HashSet<>();
                            }
                            messages.add(msg);
                        }
                    }

                    // 更新并展示
                    if (messages != null) {
                        AitHelper.setRecentContactAited(recentContact, messages);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private BroadcastReceiver nimLoginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            requestMessages(true);
        }
    };
}
