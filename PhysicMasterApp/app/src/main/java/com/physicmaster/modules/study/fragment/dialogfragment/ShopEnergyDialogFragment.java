package com.physicmaster.modules.study.fragment.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.game.BuyPropResponse;
import com.physicmaster.net.response.game.EnergyDetailedResponse;
import com.physicmaster.net.service.game.BuyPropService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;


/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/17 14:25
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class ShopEnergyDialogFragment extends DialogFragment {

    private View mView;
    private FragmentActivity mContext;
    private String energy;
    private EnergyDetailedResponse.DataBean.PropListBean energy_30;
    private EnergyDetailedResponse.DataBean.PropListBean energy_5;
    private boolean isBuy = false;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.shop_dialog_fragment_energy, container, false);
        Button btnPay = (Button) mView.findViewById(R.id.btn_pay);
        ImageView ivClose = (ImageView) mView.findViewById(R.id.iv_close);
        ImageView ivEnergy = (ImageView) mView.findViewById(R.id.iv_energy);
        TextView tvPay = (TextView) mView.findViewById(R.id.tv_pay);
        TextView tvEnergy = (TextView) mView.findViewById(R.id.tv_energy);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopEnergyDialogFragment.this.dismiss();
            }
        });

        mContext = getActivity();
        Bundle bundle = getArguments();
        energy_30 = (EnergyDetailedResponse.DataBean.PropListBean) bundle.getSerializable("energy_30");
        energy_5 = (EnergyDetailedResponse.DataBean.PropListBean) bundle.getSerializable("energy_5");
        energy = bundle.getString("energy");

        if ("energy_add30".equals(energy)) {
            tvPay.setText(energy_30.priceGoldCoin + "金币");
            if (!TextUtils.isEmpty(energy_30.propIntro)) {
                tvEnergy.setText(energy_30.propIntro + "");
            } else {
                tvEnergy.setText(energy_30.propName);
            }
            if (!TextUtils.isEmpty(energy_30.propImg)) {
                Glide.with(mContext).load(energy_30.propImg).into(ivEnergy);
            }
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buyProp(energy_30.propId);
                }
            });


        } else if ("energy_add5".equals(energy)) {
            tvPay.setText(energy_5.priceGoldCoin + "金币");
            if (!TextUtils.isEmpty(energy_5.propIntro)) {
                tvEnergy.setText(energy_5.propIntro + "");
            } else {
                tvEnergy.setText(energy_5.propName);
            }
            if (!TextUtils.isEmpty(energy_5.propImg)) {
                Glide.with(mContext).load(energy_5.propImg).into(ivEnergy);
            }
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isBuy) {
                        isBuy = true;
                        buyProp(energy_5.propId);
                    }

                }
            });
        }


        return mView;
    }

    private void buyProp(int propId) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);

        final BuyPropService service = new BuyPropService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<BuyPropResponse>() {
            @Override
            public void onGetData(BuyPropResponse data) {
                isBuy = false;
                loadingDialog.dismissDialog();
                ShopEnergyDialogFragment.this.dismiss();
                UIUtils.showToast(mContext, "购买成功");
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                isBuy = false;
                UIUtils.showToast(mContext, errorMsg);
                loadingDialog.dismissDialog();
                ShopEnergyDialogFragment.this.dismiss();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                isBuy = false;
                service.cancel();
            }
        });
        service.postLogined("propId=" + propId, false);
    }
}
