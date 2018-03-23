/* 
 * 系统名称：lswuyou
 * 类  名  称：CommonDialog.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-30 上午11:20:22
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;


public class EnvaluateDialog extends Dialog implements View.OnClickListener {
    private int basePrice = 1;
    private int totalPrice = 10;
    private int leftCount = 0;
    private int counts = 10;
    private Context context;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_buy:
                listener.onPriceSelected(counts);
                dismiss();
                break;
            case R.id.plus:
                if (counts < leftCount) {
                    counts += 1;
                    resetPrice(counts);
                }
                break;
            case R.id.minus:
                if ((counts > 1)) {
                    counts -= 1;
                    resetPrice(counts);
                }
                break;
            case R.id.iv_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void resetPrice(int count) {
        etPrice.setText(count + "");
        totalPrice = count * basePrice;
        tvPrice.setText(totalPrice + "");
        tvCount.setText(count + "");
    }

    private RadioGroup radioGroup;
    private Button btnOk;
    private ImageView btnCancel;
    private ImageButton btnAdd, btnDel;
    private EditText etPrice;
    private TextView tvPrice, tvCount;
    private OnPriceSelectedListener listener;

    public EnvaluateDialog(Context context, int theme, int basePrice, int leftCount,
                           OnPriceSelectedListener
                                   listener) {
        super(context, theme);
        this.context = context;
        this.basePrice = basePrice;
        this.listener = listener;
        this.leftCount = leftCount;
        init();
        setPriceBtnState();
    }

    private void setPriceBtnState() {
        if (leftCount < 100) {
            findViewById(R.id.btn_price_100).setEnabled(false);
            findViewById(R.id.btn_price_100).setBackgroundColor(context.getResources().getColor(R
                    .color.darkgray));
        }
        if (leftCount < 50) {
            findViewById(R.id.btn_price_50).setEnabled(false);
            findViewById(R.id.btn_price_50).setBackgroundColor(context.getResources().getColor(R
                    .color.darkgray));
        }
        if (leftCount < 20) {
            findViewById(R.id.btn_price_20).setEnabled(false);
            findViewById(R.id.btn_price_20).setBackgroundColor(context.getResources().getColor(R
                    .color.darkgray));
        }
        if (leftCount < 10) {
            findViewById(R.id.btn_price_10).setEnabled(false);
            findViewById(R.id.btn_price_10).setBackgroundColor(context.getResources().getColor(R
                    .color.darkgray));
        }
        if (basePrice == 1 && 10 < leftCount) {
            RadioButton btn = (RadioButton) findViewById(R.id.btn_price_10);
            btn.setChecked(true);
        }

    }

    private void init() {
        setContentView(R.layout.common_dialog);
        radioGroup = (RadioGroup) findViewById(R.id.radio_price);
        btnOk = (Button) findViewById(R.id.btn_buy);
        btnAdd = (ImageButton) findViewById(R.id.plus);
        btnDel = (ImageButton) findViewById(R.id.minus);
        btnCancel = (ImageView) findViewById(R.id.iv_cancel);
        etPrice = (EditText) findViewById(R.id.input);
        tvPrice = (TextView) findViewById(R.id.tv_total_price);
        tvCount = (TextView) findViewById(R.id.tv_total_count);
        int initCout = 10;
        if (basePrice > 1) {
            initCout = 1;
        }
        if (leftCount < 10 && basePrice == 1) {
            initCout = leftCount;
        }
        counts = initCout;
        etPrice.setText(initCout + "");
        totalPrice = initCout * basePrice;
        tvPrice.setText(totalPrice + "");
        btnOk.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_price_10:
                        counts = 10;
                        resetPrice(counts);
                        break;
                    case R.id.btn_price_20:
                        counts = 20;
                        resetPrice(counts);
                        break;
                    case R.id.btn_price_50:
                        counts = 50;
                        resetPrice(counts);
                        break;
                    case R.id.btn_price_100:
                        counts = 100;
                        resetPrice(counts);
                        break;
                    default:
                        break;
                }
            }
        });

        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int temp = Integer.parseInt(s.toString());
//                    totalPrice = basePrice * mulPrice;
                    if (temp > leftCount * basePrice) {
                        Toast.makeText(context, "商品数量不足！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    counts = temp;
                    totalPrice = counts * basePrice;
                    tvPrice.setText(totalPrice + "");
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "请输入数字！", Toast.LENGTH_SHORT).show();
                    tvPrice.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void show() {
        Window window = getWindow();
        LayoutParams params = window.getAttributes();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        setCanceledOnTouchOutside(false);
        super.show();
    }

    public interface OnPriceSelectedListener {
        public void onPriceSelected(int price);
    }
}
