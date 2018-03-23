package com.physicmaster.modules.study.fragment.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.modules.study.activity.GoldActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.game.BuyPropResponse;
import com.physicmaster.net.response.game.GoldMallResponse;
import com.physicmaster.net.service.game.BuyPropService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;

/**
 * Created by huashigen on 16/12/2.
 */

public class BuyPropDialogFragment extends DialogFragment implements View.OnClickListener {
    private View         mView;
    private EditText     etNumber;
    private TextView     tvPrice;
    private TextView     tvNumber;
    private ImageView    ivProp;
    private TextView     tvJian;
    private TextView     tvJia;
    private Button       btnBuy;
    private ImageView    ivClose;
    private GoldActivity mContext;
    private boolean isBuy = false;
    private Integer                                number;
    private GoldMallResponse.DataBean.AppPropsBean data;
    private int                                    goldValue;
    private int                                    price;
    private int                                    number2;

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
        mContext = (GoldActivity) getActivity();
        mView = inflater.inflate(R.layout.dialog_fragment_buy_prpo, container,false);
        ivProp = (ImageView) mView.findViewById(R.id.iv_prop);
        tvNumber = (TextView) mView.findViewById(R.id.tv_number);
        tvPrice = (TextView) mView.findViewById(R.id.tv_price);
        etNumber = (EditText) mView.findViewById(R.id.et_number);


        tvJian = (TextView) mView.findViewById(R.id.tv_jian);
        tvJia = (TextView) mView.findViewById(R.id.tv_jia);
        btnBuy = (Button) mView.findViewById(R.id.btn_buy);
        ivClose = (ImageView) mView.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
        tvJia.setOnClickListener(this);
        tvJian.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        data = bundle.getParcelable("data");
        if (!TextUtils.isEmpty(data.propImg)) {
            Glide.with(mContext).load(data.propImg).into(ivProp);
        }
        btnBuy.setEnabled(false);
        btnBuy.setBackgroundResource(R.drawable.gray_background);

        String str = etNumber.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            number = Integer.valueOf(str);
            tvNumber.setText(number + "");
            tvPrice.setText(number * data.priceGoldCoin + "");
        }


        String str2 = tvNumber.getText().toString();
        if (TextUtils.isEmpty(str2)) {
            number2 = Integer.valueOf(str2);
        }
        String str3 = tvPrice.getText().toString();
        if (TextUtils.isEmpty(str3)) {
            price = Integer.valueOf(str3);
        }


        goldValue = bundle.getInt("goldValue");

        judgeBuy();


        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = etNumber.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    number = Integer.valueOf(str);
                    price = number * data.priceGoldCoin;
                    if (price >= goldValue) {
                        if (price > goldValue) {
                            tvNumber.setText(number + "");
                            tvPrice.setText("金币不足");
                            btnBuy.setEnabled(false);
                            btnBuy.setBackgroundResource(R.drawable.gray_background);
                        } else {
                            if (number == 0 || goldValue == 0) {
                                tvNumber.setText(number + "");
                                tvPrice.setText(price + "");
                                btnBuy.setEnabled(false);
                                btnBuy.setBackgroundResource(R.drawable.gray_background);
                            } else {
                                tvNumber.setText(number + "");
                                tvPrice.setText(price + "");
                                btnBuy.setEnabled(true);
                                btnBuy.setBackgroundResource(R.drawable.blue_btn_background);
                            }
                        }
                    } else {
                        if (number == 0 || goldValue == 0) {
                            tvNumber.setText(number + "");
                            tvPrice.setText(price + "");
                            btnBuy.setEnabled(false);
                            btnBuy.setBackgroundResource(R.drawable.gray_background);
                        } else {
                            tvNumber.setText(number + "");
                            tvPrice.setText(price + "");
                            btnBuy.setEnabled(true);
                            btnBuy.setBackgroundResource(R.drawable.blue_btn_background);
                        }
                    }
                    etNumber.setSelection(etNumber.getText().length());
                }
            }
        });
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jian:
                if (number < 1) {
                    tvNumber.setText(number + "");
                    etNumber.setText(number + "");
                    btnBuy.setEnabled(false);
                    btnBuy.setBackgroundResource(R.drawable.gray_background);
                    return;
                } else {
                    number--;
                    judgeBuy();
                }
                break;
            case R.id.tv_jia:
                number++;
                judgeBuy();
                break;
            case R.id.btn_buy:
                if (!isBuy) {
                    isBuy = true;
                    buyPrpo(data.propId, number);
                }
                break;
            case R.id.iv_close:
                BuyPropDialogFragment.this.dismiss();
                break;
        }
    }
    private void judgeBuy() {
        price = number * data.priceGoldCoin;
        if (price >= goldValue) {
            if (price > goldValue) {
                etNumber.setText(number + "");
                tvNumber.setText(number + "");
                tvPrice.setText("金币不足");
                btnBuy.setEnabled(false);
                btnBuy.setBackgroundResource(R.drawable.gray_background);
            } else {
                if (number == 0 || goldValue == 0) {
                    etNumber.setText(number + "");
                    tvNumber.setText(number + "");
                    tvPrice.setText(price + "");
                    btnBuy.setEnabled(false);
                    btnBuy.setBackgroundResource(R.drawable.gray_background);
                } else {
                    etNumber.setText(number + "");
                    tvNumber.setText(number + "");
                    tvPrice.setText(price + "");
                    btnBuy.setEnabled(true);
                    btnBuy.setBackgroundResource(R.drawable.blue_btn_background);
                }

            }
        } else {
            if (number == 0 || goldValue == 0) {
                etNumber.setText(number + "");
                tvNumber.setText(number + "");
                tvPrice.setText(price + "");
                btnBuy.setEnabled(false);
                btnBuy.setBackgroundResource(R.drawable.gray_background);
            } else {
                etNumber.setText(number + "");
                tvNumber.setText(number + "");
                tvPrice.setText(price + "");
                btnBuy.setEnabled(true);
                btnBuy.setBackgroundResource(R.drawable.blue_btn_background);
            }

        }
        etNumber.setSelection(etNumber.getText().length());
    }

    private void buyPrpo(int propId, int count) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);
        if (0 == count || TextUtils.isEmpty(propId + "")) {
            return;
        }
        final BuyPropService service = new BuyPropService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<BuyPropResponse>() {
            @Override
            public void onGetData(BuyPropResponse data) {
                loadingDialog.dismissDialog();
                isBuy = false;
                UIUtils.showToast(mContext, "购买成功");
                mContext.setTvTotalGold(data.data.goldValue);
                BuyPropDialogFragment.this.dismiss();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
                isBuy = false;
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                isBuy = false;
                service.cancel();
            }
        });
        service.postLogined("propId=" + propId + "&count=" + count, false);
    }
}
