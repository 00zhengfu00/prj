package com.physicmaster.modules.mine.activity.user;

import android.content.Intent;
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
import com.physicmaster.modules.account.LoginActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.user.ChageUserService;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.SubjectNameUtil;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.PickerView;

import java.util.ArrayList;

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
public class GradeDialogFragment extends DialogFragment {

    private View         mView;
    private UserActivity mContext;

    private ArrayList<String> mGradeList = new ArrayList<>();

    private int        eduGrade;
    private String     mgrade;
    private PickerView pvGrade;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
        mContext = (UserActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_fragment_grade, null);
        mGradeList.add("七年级上");
        mGradeList.add("七年级下");
        mGradeList.add("八年级上");
        mGradeList.add("八年级下");
        mGradeList.add("九年级上");
        mGradeList.add("九年级下");
        pvGrade = (PickerView) mView.findViewById(R.id.pv_grade);
        TextView tvCancle = (TextView) mView.findViewById(R.id.tv_cancle);
        TextView tvEnsure = (TextView) mView.findViewById(R.id.tv_ensure);
        UserDataResponse.UserDataBean.LoginVoBean mDataBean = BaseApplication.getUserData();
        if (null != mDataBean) {
            pvGrade.setData(mGradeList);
            pvGrade.setSelected(mDataBean.eduGrade - 1);
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
            mContext.finish();
            UIUtils.showToast(mContext, "登录异常");
        }
        pvGrade.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                mgrade = text;
                eduGrade = SubjectNameUtil.updataGrade(text);

            }
        });
        tvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chageGrade();
                GradeDialogFragment.this.dismiss();
            }
        });


        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradeDialogFragment.this.dismiss();
            }
        });


        return mView;
    }

    private void chageGrade() {
        final ChageUserService service = new ChageUserService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {

            @Override
            public void onGetData(UserDataResponse data) {
                UIUtils.showToast(mContext, "修改成功");
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, data.data.loginVo);
                SpUtils.putString(mContext,CacheKeys.GRAGE_INFO, String.valueOf(data.data.loginVo.eduGrade));
                BaseApplication.setUserData(data.data.loginVo);
                mContext.setTvGrade(mgrade);
                //                Intent intent = new Intent("com.physicmaster.SELECT_GRADE");
                //                mContext.sendBroadcast(intent);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("eduGrade=" + eduGrade, false);
    }


}
