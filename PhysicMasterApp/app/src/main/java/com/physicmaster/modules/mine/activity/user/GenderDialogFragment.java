package com.physicmaster.modules.mine.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.user.ChageUserService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/24 17:34
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class GenderDialogFragment extends DialogFragment {

    private View         mView;
    private UserActivity mContext;
    private int mGender = 0;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
        mContext = (UserActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_fragment_gender, null);

        TextView tvMale = (TextView) mView.findViewById(R.id.tv_male);
        TextView tvFeMale = (TextView) mView.findViewById(R.id.tv_female);

        tvMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectGender(1);
                GenderDialogFragment.this.dismiss();

            }
        });

        tvFeMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectGender(2);
                GenderDialogFragment.this.dismiss();
            }
        });


        return mView;
    }

    private void selectGender(final int mGender) {

        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);

        final ChageUserService service = new ChageUserService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {

            @Override
            public void onGetData(UserDataResponse data) {
                UIUtils.showToast(mContext, "修改成功");
                if (1 == data.data.loginVo.gender) {
                    mContext.setTvGender("男");
                } else if (2 == data.data.loginVo.gender) {
                    mContext.setTvGender("女");
                }
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, data.data.loginVo);
                BaseApplication.setUserData(data.data.loginVo);
                loadingDialog.dismissDialog();
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
        service.postLogined("gender=" + mGender, false);
    }

}
