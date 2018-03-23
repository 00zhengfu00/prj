package com.physicmaster.modules.study.fragment.dialogfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.account.LoginActivity;
import com.physicmaster.modules.study.activity.BackpackActivity;
import com.physicmaster.modules.study.activity.DriverHouseActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.game.ShowEndeavorResponse;
import com.physicmaster.net.response.user.MainInfoResponse;
import com.physicmaster.net.response.user.PetInfoResponse;
import com.physicmaster.net.service.game.ShowEndeavorService;
import com.physicmaster.net.service.game.UseEndeavorService;
import com.physicmaster.net.service.user.PetInfoService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.gif.AlxGifHelper;
import com.pnikosis.materialishprogress.ProgressWheel;

import pl.droidsonroids.gif.GifImageView;

import static com.physicmaster.widget.PickerView.TAG;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/17 10:59
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class EndeavorDialogFragment extends DialogFragment implements View.OnClickListener {


    private ProgressBar pbSpeed;
    private TextView tvSpeed;
    private ProgressBar pbAttack;
    private TextView tvAttack;
    private ProgressBar pbSpecailAttack;
    private TextView tvSpecailAttack;
    private ProgressBar pbDefense;
    private TextView tvDefense;
    private ProgressBar pbSpecailDefense;
    private TextView tvSpecailDefense;
    private TextView tvNumber;
    private ImageView ivEndeavor;
    private ImageView ivPet;
    private BackpackActivity mContext;
    private View mView;
    private int propId;
    private int propCount;
    private View gifGroup;
    private Button btnDefenseUse;
    private Button btnSpecailAttackUse;
    private ImageView ivClose;
    private Button btnAttackUse;
    private Button btnSpeedUse;
    private Button btnSpecailDefenseUse;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes()
                .height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        mContext = (BackpackActivity) getActivity();
        mView = inflater.inflate(R.layout.dialog_fragment_endeavor, container, false);

        pbSpeed = (ProgressBar) mView.findViewById(R.id.pb_speed);
        tvSpeed = (TextView) mView.findViewById(R.id.tv_speed);
        pbAttack = (ProgressBar) mView.findViewById(R.id.pb_attack);
        tvAttack = (TextView) mView.findViewById(R.id.tv_attack);
        pbSpecailAttack = (ProgressBar) mView.findViewById(R.id.pb_specail_attack);
        tvSpecailAttack = (TextView) mView.findViewById(R.id.tv_specail_attack);
        pbDefense = (ProgressBar) mView.findViewById(R.id.pb_defense);
        tvDefense = (TextView) mView.findViewById(R.id.tv_defense);
        pbSpecailDefense = (ProgressBar) mView.findViewById(R.id.pb_specail_defense);
        tvSpecailDefense = (TextView) mView.findViewById(R.id.tv_specail_defense);
        tvNumber = (TextView) mView.findViewById(R.id.tv_number);
        ivEndeavor = (ImageView) mView.findViewById(R.id.iv_endeavor);
        //ivPet = (ImageView) mView.findViewById(R.id.iv_pet);

        ivClose = (ImageView) mView.findViewById(R.id.iv_close);
        btnSpecailAttackUse = (Button) mView.findViewById(R.id.btn_specail_attack_use);
        btnDefenseUse = (Button) mView.findViewById(R.id.btn_defense_use);
        btnAttackUse = (Button) mView.findViewById(R.id.btn_attack_use);
        btnSpeedUse = (Button) mView.findViewById(R.id.btn_speed_use);
        btnSpecailDefenseUse = (Button) mView.findViewById(R.id.btn_specail_defense_use);

        ivClose.setOnClickListener(this);
        btnSpecailAttackUse.setOnClickListener(this);
        btnDefenseUse.setOnClickListener(this);
        btnAttackUse.setOnClickListener(this);
        btnSpeedUse.setOnClickListener(this);
        btnSpecailDefenseUse.setOnClickListener(this);

        Bundle bundle = getArguments();
        propId = bundle.getInt("propId");
        propCount = bundle.getInt("propCount");
        tvNumber.setText(propCount + "");

        showEndeavor();
        getPetInfo();
        return mView;
    }

    /**
     * 获取宠物信息
     */
    private void getPetInfo() {
        PetInfoService service = new PetInfoService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<PetInfoResponse>() {
            @Override
            public void onGetData(PetInfoResponse data) {
                String petImg = data.data.petVo.petImg;
                if (!TextUtils.isEmpty(data.data.petVo.petImg)) {
                    gifGroup = mView.findViewById(R.id.iv_pet);
                    AlxGifHelper.displayImage(petImg,
                            (GifImageView) gifGroup.findViewById(R.id.gif_photo_view),
                            (ProgressWheel) gifGroup.findViewById(R.id.progress_wheel),
                            (TextView) gifGroup.findViewById(R.id.tv_progress), gifGroup
                                    .getWidth()
                    );
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getActivity(), errorMsg);
            }
        });
        service.postLogined("", false);
    }

    public interface OnUseEndeavorListener {
        void onUesEndeavor();
    }

    public OnUseEndeavorListener onUseEndeavorListener;

    public void setUseEndeavorListener(OnUseEndeavorListener onUseEndeavorListener) {
        this.onUseEndeavorListener = onUseEndeavorListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                EndeavorDialogFragment.this.dismiss();
                break;
            case R.id.btn_speed_use:
                useEndeavor(propId, 4);
                break;
            case R.id.btn_attack_use:
                useEndeavor(propId, 0);
                break;
            case R.id.btn_specail_attack_use:
                useEndeavor(propId, 2);
                break;
            case R.id.btn_defense_use:
                useEndeavor(propId, 1);
                break;
            case R.id.btn_specail_defense_use:
                useEndeavor(propId, 3);
                break;
        }
    }

    private void useEndeavor(int propId, int attributeType) {
        final UseEndeavorService service = new UseEndeavorService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<ShowEndeavorResponse>() {

            @Override
            public void onGetData(ShowEndeavorResponse data) {
                Log.d(TAG, "加载完成：onGetData: " + data.msg);
                setEndeavor(data.data);
                onUseEndeavorListener.onUesEndeavor();

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "加载失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("propId=" + propId + "&attributeType=" + attributeType, false);

    }


    private void showEndeavor() {
        if (TextUtils.isEmpty(propId + "")) {
            UIUtils.showToast(mContext, "网络有问题，请稍后再试！");
            return;
        }
        final ShowEndeavorService service = new ShowEndeavorService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<ShowEndeavorResponse>() {

            @Override
            public void onGetData(ShowEndeavorResponse data) {
                Log.d(TAG, "加载完成：onGetData: " + data.msg);
                setEndeavor(data.data);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "加载失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("propId=" + propId, false);

    }

    public void setEndeavor(ShowEndeavorResponse.DataBean data) {
        tvSpeed.setText(data.effortVo.speed + "/" + data.effortVo.maxAttributeValue);
        tvAttack.setText(data.effortVo.attack + "/" + data.effortVo.maxAttributeValue);
        tvSpecailAttack.setText(data.effortVo.specialAttack + "/" + data.effortVo
                .maxAttributeValue);
        tvDefense.setText(data.effortVo.defense + "/" + data.effortVo.maxAttributeValue);
        tvSpecailDefense.setText(data.effortVo.specialDefense + "/" + data.effortVo
                .maxAttributeValue);
        tvNumber.setText(data.propCount + "");


        pbSpeed.setMax(data.effortVo.maxAttributeValue);
        pbSpeed.setProgress(data.effortVo.speed);
        pbAttack.setMax(data.effortVo.maxAttributeValue);
        pbAttack.setProgress(data.effortVo.attack);
        pbSpecailAttack.setMax(data.effortVo.maxAttributeValue);
        pbSpecailAttack.setProgress(data.effortVo.specialAttack);
        pbDefense.setMax(data.effortVo.maxAttributeValue);
        pbDefense.setProgress(data.effortVo.defense);
        pbSpecailDefense.setMax(data.effortVo.maxAttributeValue);
        pbSpecailDefense.setProgress(data.effortVo.specialDefense);


        if (data.effortVo.speed == data.effortVo.maxAttributeValue || data.propCount == 0) {
            btnSpeedUse.setEnabled(false);
            btnSpeedUse.setBackgroundResource(R.drawable.gray_background);
        } else {
            btnSpeedUse.setEnabled(true);
            btnSpeedUse.setBackgroundResource(R.drawable.btn_background);
        }

        if (data.effortVo.attack == data.effortVo.maxAttributeValue || data.propCount == 0) {
            btnAttackUse.setEnabled(false);
            btnAttackUse.setBackgroundResource(R.drawable.gray_background);
        } else {
            btnAttackUse.setEnabled(true);
            btnAttackUse.setBackgroundResource(R.drawable.btn_background);
        }

        if (data.effortVo.specialAttack == data.effortVo.maxAttributeValue || data.propCount == 0) {
            btnSpecailAttackUse.setEnabled(false);
            btnSpecailAttackUse.setBackgroundResource(R.drawable.gray_background);
        } else {
            btnSpecailAttackUse.setEnabled(true);
            btnSpecailAttackUse.setBackgroundResource(R.drawable.btn_background);
        }

        if (data.effortVo.defense == data.effortVo.maxAttributeValue || data.propCount == 0) {
            btnDefenseUse.setEnabled(false);
            btnDefenseUse.setBackgroundResource(R.drawable.gray_background);
        } else {
            btnDefenseUse.setEnabled(true);
            btnDefenseUse.setBackgroundResource(R.drawable.btn_background);
        }

        if (data.effortVo.specialDefense == data.effortVo.maxAttributeValue || data.propCount ==
                0) {
            btnSpecailDefenseUse.setEnabled(false);
            btnSpecailDefenseUse.setBackgroundResource(R.drawable.gray_background);
        } else {
            btnSpecailDefenseUse.setEnabled(true);
            btnSpecailDefenseUse.setBackgroundResource(R.drawable.btn_background);
        }

    }
}
