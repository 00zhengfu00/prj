package com.physicmaster.modules.course.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.physicmaster.R;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by huashigen on 2016/11/22.
 */
public class PayDialogFragment extends DialogFragment {
    private View.OnClickListener onClickListener;

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_payment);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        dialog.getWindow().setWindowAnimations(R.style.AnimationPushOutIn);
        dialog.findViewById(R.id.tv_wenxin_daifu).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.tv_zhifubao_pay).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.tv_weixin_pay).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(),"select_pay_cancle");
                dismiss();
            }
        });
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        return dialog;
    }
}
