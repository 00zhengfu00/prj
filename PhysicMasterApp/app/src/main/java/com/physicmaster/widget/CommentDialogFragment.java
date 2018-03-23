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
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.physicmaster.R;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.ViewUtils;


public class CommentDialogFragment extends DialogFragment implements OnClickListener {
    /**
     * 点击回调接口
     */
    private OnBack onBack;
    public EditText etComment;
    private String content;
    private boolean showSoft = false;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        CommentDialog dialog = new CommentDialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
//                    showSoft = true;
//                }
//                return false;
//            }
//        });
        dialog.getWindow().setWindowAnimations(R.style.AnimationPushOutIn);
        dialog.setContentView(R.layout.comment_dialog);
        etComment = (EditText) dialog.findViewById(R.id.et_comment);
        dialog.findViewById(R.id.tv_send).setOnClickListener(this);
        if (getArguments() != null) {
            content = getArguments().getString("content");
            etComment.setHint("回复" + content);
        }
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        showInputSoft(etComment);
        return dialog;
    }

    @Override
    public void onStop() {
//        ViewUtils.hideInputSoft(getContext(), etComment);
        super.onStop();
    }

    @Override
    public void onPause() {
//        InputMethodManager inputMgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        boolean isActive = inputMgr.isActive();
//        if (isActive) {
//            inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
//        }
//        ViewUtils.hideInputSoft(getContext(), etComment);
        super.onPause();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
//        InputMethodManager inputMgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (!showSoft) {
//            inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
//        }
        super.onDismiss(dialog);
    }

    /**
     * 自动弹出编辑框
     */
    public void showInputSoft(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.requestFocus();
                ViewUtils.showInputSoft(getActivity(), view);
            }
        }, 300);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send:
                String content = etComment.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    UIUtils.showToast(getActivity(), "评论内容不能为空");
                    return;
                }
                if (etComment.length() > 140) {
                    UIUtils.showToast(getActivity(), "评论字数不能超过140字");
                    return;
                }
                onBack.click(content);
                ViewUtils.hideInputSoft(getActivity(), etComment);
                dismiss();
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
        void click(String btn);
    }


    public EditText getEditText() {
        return this.etComment;
    }

}
