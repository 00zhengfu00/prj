package com.physicmaster.modules.study.fragment.dialogfragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.modules.study.activity.BackpackActivity;
import com.physicmaster.modules.study.activity.exercise.ExcerciseV2Activity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.game.EnergyDetailedResponse;
import com.physicmaster.net.service.game.EnergyDetailedService;
import com.physicmaster.utils.UIUtils;

import java.util.List;

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
public class Energy2DialogFragment extends DialogFragment implements View.OnClickListener {


    private View mView;
    private ExcerciseV2Activity mContext;
    private ImageView ivFreeEnergy;
    private ImageView ivShow5;
    private ImageView ivShow30;
    private TextView tvTime;
    private TextView tvEnergy;
    private ProgressBar pbEnergy;
    private EnergyDetailedResponse.DataBean.UserGameEnergyBean userGameEnergy;
    private List<EnergyDetailedResponse.DataBean.PropListBean> propList;
    private Button btnEnergy;
    private Button btnShop5;
    private Button btnFree;
    private Button btnShop30;

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

        mContext = (ExcerciseV2Activity) getActivity();
        mView = inflater.inflate(R.layout.dialog_fragment_energy, container, false);

        btnShop30 = (Button) mView.findViewById(R.id.btn_shop_30);
        btnShop5 = (Button) mView.findViewById(R.id.btn_shop_5);
        btnFree = (Button) mView.findViewById(R.id.btn_free);
        btnEnergy = (Button) mView.findViewById(R.id.btn_energy);
        ImageView ivClose = (ImageView) mView.findViewById(R.id.iv_close);
        ivFreeEnergy = (ImageView) mView.findViewById(R.id.iv_free_energy);
        ivShow5 = (ImageView) mView.findViewById(R.id.iv_show_5);
        ivShow30 = (ImageView) mView.findViewById(R.id.iv_show_30);
        tvTime = (TextView) mView.findViewById(R.id.tv_time);
        tvEnergy = (TextView) mView.findViewById(R.id.tv_energy);
        pbEnergy = (ProgressBar) mView.findViewById(R.id.pb_energy);

        ivClose.setOnClickListener(this);

        showEnergy();
        //禁止返回物理按键
        //        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
        //            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        //                return true;
        //            }
        //        });

//        this.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                mContext.finish();
//            }
//        });

        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                mContext.finish();
                return false;
            }
        });

        return mView;
    }


    private void showEnergy() {
        final EnergyDetailedService service = new EnergyDetailedService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<EnergyDetailedResponse>() {

            @Override
            public void onGetData(EnergyDetailedResponse data) {
                //Toast.makeText(mContext, "获取成功" + data.code, Toast.LENGTH_SHORT).show();
                userGameEnergy = data.data.userGameEnergy;
                propList = data.data.propList;
                refreshUi();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("", false);
    }

    private void refreshUi() {
        //弹出第二个弹窗
        btnShop30.setOnClickListener(this);
        btnShop5.setOnClickListener(this);
        btnFree.setOnClickListener(this);
        tvEnergy.setText(userGameEnergy.energyValue + "/" + userGameEnergy.maxEnergyValue);
        pbEnergy.setMax(userGameEnergy.maxEnergyValue);
        pbEnergy.setProgress(userGameEnergy.energyValue);
        if (!TextUtils.isEmpty(propList.get(0).propImg)) {
            Glide.with(mContext).load(propList.get(0).propImg).into(ivFreeEnergy);
        }
        if (!TextUtils.isEmpty(propList.get(1).propImg)) {
            Glide.with(mContext).load(propList.get(1).propImg).into(ivShow5);
        }
        if (!TextUtils.isEmpty(propList.get(2).propImg)) {
            Glide.with(mContext).load(propList.get(2).propImg).into(ivShow30);
        }
        if (TextUtils.isEmpty(userGameEnergy.recoverFullTime)) {
            tvTime.setText("精力已满");
            btnEnergy.setText("继续闯关");

            btnEnergy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Energy2DialogFragment.this.dismiss();
                }
            });
        } else {
            tvTime.setText(userGameEnergy.recoverFullTime + "后恢复至" + userGameEnergy
                    .maxEnergyValue + "精力");
            btnEnergy.setText("补充精力");
            btnEnergy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, BackpackActivity.class));
                }
            });
        }

    }

    @Override
    public void onClick(View view) {
        DialogFragment dialogFragment = null;
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.btn_shop_30:
                FragmentTransaction ft2 = getFragmentManager().beginTransaction();

                ft2.remove(Energy2DialogFragment.this);

                ft2.addToBackStack(null);

                dialogFragment = new ShopEnergyDialogFragment();
                bundle.putSerializable("energy_30", propList.get(2));
                bundle.putString("energy", "energy_add30");
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "energy_30");
                break;
            case R.id.btn_shop_5:
                FragmentTransaction ft1 = getFragmentManager().beginTransaction();

                ft1.remove(Energy2DialogFragment.this);

                ft1.addToBackStack(null);

                dialogFragment = new ShopEnergyDialogFragment();
                bundle.putSerializable("energy_5", propList.get(1));
                bundle.putString("energy", "energy_add5");
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "energy_5");
                break;

            case R.id.btn_free:
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.remove(Energy2DialogFragment.this);

                ft.addToBackStack(null);

                dialogFragment = new FreeEnergyDialogFragment();
                //bundle.putSerializable("energy_free", propList.get(0));
                //dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "dialog_fragment");
                break;
            case R.id.iv_close:
                this.dismiss();
                mContext.finish();
                break;
        }
    }


}
