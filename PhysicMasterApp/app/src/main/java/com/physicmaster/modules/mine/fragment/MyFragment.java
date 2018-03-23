package com.physicmaster.modules.mine.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IUnreadCountCallback;
import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.BaseFragment;
import com.physicmaster.modules.mine.activity.CollectionActivity;
import com.physicmaster.modules.mine.activity.InvitationActivity;
import com.physicmaster.modules.mine.activity.MedalActivity;
import com.physicmaster.modules.mine.activity.MymemberActivity;
import com.physicmaster.modules.mine.activity.friend.FriendsActivity;
import com.physicmaster.modules.mine.activity.notebook.NoteBookActivity;
import com.physicmaster.modules.mine.activity.question.MyQuestionActivity;
import com.physicmaster.modules.mine.activity.setting.SettingActivity;
import com.physicmaster.modules.mine.activity.user.UserActivity;
import com.physicmaster.modules.study.activity.GoldActivity;
import com.physicmaster.modules.videoplay.cache.MyCacheActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.GetStudyInfoResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.user.GetStudyInfoService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.RoundImageView;

import org.json.JSONException;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/10 15:18
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    private FragmentActivity mContext;
    private TextView tvUser;
    private RoundImageView ivUser;
    private UserDataResponse.UserDataBean.LoginVoBean mDataBean;
    private TextView tvDay;
    private TextView tvStudyTime;
    private TextView tvFinishSection;

    private ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, iv10, iv11;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        mContext = getActivity();

        tvUser = (TextView) rootView.findViewById(R.id.tv_user);

        ivUser = (RoundImageView) rootView.findViewById(R.id.iv_user);
        LinearLayout llCourse = (LinearLayout) rootView.findViewById(R.id.ll_course);
        RelativeLayout rlCache = (RelativeLayout) rootView.findViewById(R.id.rl_cache);
        LinearLayout llCollection = (LinearLayout) rootView.findViewById(R.id.ll_collection);
        RelativeLayout rlInvitation = (RelativeLayout) rootView.findViewById(R.id.rl_invitation);
        RelativeLayout rlFriends = (RelativeLayout) rootView.findViewById(R.id.rl_friends);
        RelativeLayout rlGold = (RelativeLayout) rootView.findViewById(R.id.rl_gold);
        RelativeLayout rlMedal = (RelativeLayout) rootView.findViewById(R.id.rl_medal);
        RelativeLayout rlHelper = (RelativeLayout) rootView.findViewById(R.id.rl_helper);
        LinearLayout llTopicmaps = (LinearLayout) rootView.findViewById(R.id.ll_topicmaps);
        RelativeLayout rlSettings = (RelativeLayout) rootView.findViewById(R.id.rl_settings);
        RelativeLayout rlQuestion = (RelativeLayout) rootView.findViewById(R.id.rl_question);

        LinearLayout llStudyTime = (LinearLayout) rootView.findViewById(R.id.ll_study_time);
        LinearLayout llFinishSection = (LinearLayout) rootView.findViewById(R.id.ll_finish_section);
        LinearLayout llDay = (LinearLayout) rootView.findViewById(R.id.ll_day);

        tvDay = (TextView) rootView.findViewById(R.id.tv_day);
        tvStudyTime = (TextView) rootView.findViewById(R.id.tv_study_time);
        tvFinishSection = (TextView) rootView.findViewById(R.id.tv_finish_section);

        ivUser.setOnClickListener(this);
        tvUser.setOnClickListener(this);
        rlMedal.setOnClickListener(this);
        llCourse.setOnClickListener(this);
        llCollection.setOnClickListener(this);
        rlInvitation.setOnClickListener(this);
        rlFriends.setOnClickListener(this);
        rlGold.setOnClickListener(this);
        rlHelper.setOnClickListener(this);
        llTopicmaps.setOnClickListener(this);
        rlSettings.setOnClickListener(this);
        rlCache.setOnClickListener(this);
        rlQuestion.setOnClickListener(this);

        //图标适配
        iv1 = (ImageView) rootView.findViewById(R.id.iv_member);
        iv2 = (ImageView) rootView.findViewById(R.id.iv_question);
        iv3 = (ImageView) rootView.findViewById(R.id.iv_wrong);
        iv4 = (ImageView) rootView.findViewById(R.id.iv_collect);
        iv5 = (ImageView) rootView.findViewById(R.id.iv_medal);
        iv6 = (ImageView) rootView.findViewById(R.id.iv_friends);
        iv7 = (ImageView) rootView.findViewById(R.id.iv_gold);
        iv8 = (ImageView) rootView.findViewById(R.id.iv_cache);
        iv9 = (ImageView) rootView.findViewById(R.id.iv_invitation);
        iv10 = (ImageView) rootView.findViewById(R.id.iv_settings);
        iv11 = (ImageView) rootView.findViewById(R.id.iv_helper);
        Glide.with(this).load(R.mipmap.wodehuiyuan).into(iv1);
        Glide.with(this).load(R.mipmap.wodewenda).into(iv2);
        Glide.with(this).load(R.mipmap.wodecuotiben).into(iv3);
        Glide.with(this).load(R.mipmap.wodeshoucang).into(iv4);
        Glide.with(this).load(R.mipmap.wodexunzhang).into(iv5);
        Glide.with(this).load(R.mipmap.wodehaoyou).into(iv6);
        Glide.with(this).load(R.mipmap.wodejinbi).into(iv7);
        Glide.with(this).load(R.mipmap.wodehuancun).into(iv8);
        Glide.with(this).load(R.mipmap.yaoqinghaoyou).into(iv9);
        Glide.with(this).load(R.mipmap.shezhi).into(iv10);
        Glide.with(this).load(R.mipmap.bzyfk).into(iv11);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshStudyInfo();
        mDataBean = BaseApplication.getUserData();
        if (mDataBean != null) {
            if (!TextUtils.isEmpty(mDataBean.nickname)) {
                tvUser.setText(mDataBean.nickname);
            } else {
                tvUser.setText("用户名");
            }
            Glide.with(this).load(mDataBean.portrait).placeholder(R.drawable.placeholder_gray)
                    .into(ivUser);
        } else {
            ((BaseActivity) getActivity()).gotoLoginActivity();
            return;
        }
        FeedbackAPI.getFeedbackUnreadCount(new IUnreadCountCallback() {
            @Override
            public void onSuccess(final int i) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        View viewRedPot = rootView.findViewById(R.id.view_feedback_red);
                        View viewArrow = rootView.findViewById(R.id.iv_arrow);
                        if (i > 0) {
                            viewRedPot.setVisibility(View.VISIBLE);
                            viewArrow.setVisibility(View.GONE);
                        } else {
                            viewRedPot.setVisibility(View.GONE);
                            viewArrow.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onError(final int i, final String s) {
            }
        });
    }

    /**
     * 刷新用户数据
     */
    private void refreshStudyInfo() {
        GetStudyInfoService service = new GetStudyInfoService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<GetStudyInfoResponse>() {
            @Override
            public void onGetData(GetStudyInfoResponse data) {
                GetStudyInfoResponse.DataBean.UserStudyInfoBean userStudyInfo = data.data.userStudyInfo;
                tvDay.setText(userStudyInfo.studyDays + "");
                tvFinishSection.setText(userStudyInfo.completeCourseCount + "");
                tvStudyTime.setText(userStudyInfo.studyTime + "");
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("", false);
    }


    @Override
    public void onClick(View view) {
        if (BaseApplication.getUserData().isTourist == 1) {
//            CommonDialogFragment loginFragment = new CommonDialogFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("title", "请以注册账号登录");
//            bundle.putString("note", "您目前是游客身份，不能执行此操作");
//            bundle.putString("action", "去登录");
//            loginFragment.setArguments(bundle);
//            loginFragment.setOnActionBtnClickListener(new CommonDialogFragment.OnActionBtnClickListener() {
//                @Override
//                public void onLick() {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
//            });
//            loginFragment.show(getFragmentManager(), "login");
            Utils.gotoLogin(getActivity());
            return;
        }
        switch (view.getId()) {
            case R.id.iv_user:
            case R.id.tv_user:
                startActivity(new Intent(mContext, UserActivity.class));
                break;
            case R.id.ll_course:
                startActivity(new Intent(mContext, MymemberActivity.class));
                //我的课程
                //                startActivity(new Intent(mContext, MyCourseActivity.class));
                break;
            case R.id.rl_medal:
                startActivity(new Intent(mContext, MedalActivity.class));
                break;
            case R.id.rl_cache:
                startActivity(new Intent(mContext, MyCacheActivity.class));
                break;
            case R.id.ll_collection:
                //我的收藏
                startActivity(new Intent(mContext, CollectionActivity.class));
//                startActivity(new Intent(mContext, RecordQu2Activity.class));
                break;
            case R.id.rl_invitation:
                startActivity(new Intent(mContext, InvitationActivity.class));
                break;
            case R.id.rl_friends:
                startActivity(new Intent(mContext, FriendsActivity.class));
                break;
            case R.id.ll_topicmaps:
//                startActivity(new Intent(mContext, TopicmapsActivity.class));
                startActivity(new Intent(mContext, NoteBookActivity.class));
                break;
            case R.id.rl_settings:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.rl_gold:
                startActivity(new Intent(mContext, GoldActivity.class));
                break;
            case R.id.rl_question:
                startActivity(new Intent(mContext, MyQuestionActivity.class));
                break;
            case R.id.rl_helper:
                permissionRequest();
                break;
        }
    }

    private void startFeedback() {
        UserDataResponse.UserDataBean.LoginVoBean mDataBean = BaseApplication.getUserData();
        if (mDataBean == null) {
            ((BaseActivity) getActivity()).gotoLoginActivity();
            return;
        }
        String nickName = "";
        nickName = mDataBean.nickname;
        org.json.JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("nickname", nickName);
            jsonObject.put("userKey", BaseApplication.getUserKey().userKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FeedbackAPI.setAppExtInfo(jsonObject);
        FeedbackAPI.openFeedbackActivity();
    }

    /**
     * Android6.0需要申请权限
     */
    private final static String DANGEROUS_PERMISSION[] = new String[]{Manifest.permission.CAMERA};
    private final static int CAMERA_REQUEST_CODE = 1;

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予成功,初始化
                Log.i("result", "成功获得授权");
                startFeedback();
            } else {
                Log.i("result", "未获得授权");
                // 三方处理自己逻辑,这里只做测试用
                UIUtils.showToast(getActivity(), "您拒绝了拍照权限，反馈无法上传图片哦");
                startFeedback();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Android6.0权限申请
     */
    private void permissionRequest() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                // 申请权限
                ActivityCompat.requestPermissions(getActivity(), DANGEROUS_PERMISSION,
                        CAMERA_REQUEST_CODE);
            } else {
                // 权限已经授予,直接初始化
                startFeedback();
            }
        } else {
            startFeedback();
        }
    }
}
