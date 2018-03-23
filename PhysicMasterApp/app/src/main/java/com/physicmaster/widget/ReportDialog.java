/* 
 * 系统名称：lswuyou
 * 类  名  称：ImageUploadDialog.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-21 上午9:04:12
 * 功能说明： 图片上传Dialog
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.physicmaster.R;


public class ReportDialog extends DialogFragment implements OnClickListener {
    /**
     * 点击回调接口
     */
    private OnBack onBack;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.report_dialog);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        dialog.getWindow().setWindowAnimations(R.style.AnimationPushOutIn);
        Button tvText = (Button) dialog.findViewById(R.id.tv_text);
        dialog.findViewById(R.id.btn_delete_comment).setOnClickListener(this);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(this);
        if (getArguments() != null) {
            String text = getArguments().getString("text");
            tvText.setText("举报这个" + text);
        }
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete_comment:
                onBack.click(R.id.btn_delete_comment);
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    public OnBack getOnBack() {
        return onBack;
    }

    public void setOnBack(OnBack onBack) {
        this.onBack = onBack;
    }

    /**
     * 点击回调接口
     */
    public interface OnBack {
        void click(int btn);
    }
}
