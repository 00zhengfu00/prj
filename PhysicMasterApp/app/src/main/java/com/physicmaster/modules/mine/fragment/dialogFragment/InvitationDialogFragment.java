package com.physicmaster.modules.mine.fragment.dialogFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.mine.activity.InvitationActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.user.FriendInviteService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;

import static com.physicmaster.common.cache.CacheManager.TYPE_USER_INFO;


/**
 * Created by songrui on 16/11/7.
 */

public class InvitationDialogFragment extends DialogFragment {


    private View               mView;
    private Button             btnSubmit;
    private EditText           etInvitation;
    private ImageView          ivClean;
    private InvitationActivity mContext;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = (InvitationActivity) getActivity();


        mView = inflater.inflate(R.layout.dialog_fragment_invitation, null);
        btnSubmit = (Button) mView.findViewById(R.id.btn_submit);
        etInvitation = (EditText) mView.findViewById(R.id.et_invitation);
        ivClean = (ImageView) mView.findViewById(R.id.iv_clean);


        ivClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etInvitation.setText("");
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String invitation = etInvitation.getText().toString().trim();
                if (!TextUtils.isEmpty(invitation)) {
                    judgeInvite(invitation);
                } else {
                    UIUtils.showToast(mContext, "输入内容不得为空");
                    return;
                }
            }
        });

        return mView;
    }

    private void judgeInvite(String code) {


        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);

        final FriendInviteService service = new FriendInviteService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {
            @Override
            public void onGetData(UserDataResponse data) {
                CacheManager.saveObject(TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, data.data.loginVo);
                BaseApplication.setUserData(data.data.loginVo);
                loadingDialog.dismissDialog();
                InvitationDialogFragment.this.dismiss();
                UIUtils.showToast(mContext, "提交成功");
                mContext.setbtnText("已使用过好友邀请码");

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("friendInviteCode=" + code, false);
    }

}
