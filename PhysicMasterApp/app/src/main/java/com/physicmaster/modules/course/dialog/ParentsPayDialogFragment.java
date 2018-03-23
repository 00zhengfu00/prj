package com.physicmaster.modules.course.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.physicmaster.R;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.service.account.ParentsPayLogService;
import com.physicmaster.utils.UIUtils;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;


/**
 * Created by songrui on 2016/11/22.
 */
public class ParentsPayDialogFragment extends DialogFragment {

    private ImageView ivQRcode;
    private String    shareUrl;
    private String    orderId;
    private String    qrCodeUrl;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_parentspay_fragment);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        dialog.getWindow().setWindowAnimations(R.style.AnimationPushInOut);
        ivQRcode = (ImageView) dialog.findViewById(R.id.iv_QR_code);

        shareUrl = getArguments().getString("shareUrl");
        qrCodeUrl = getArguments().getString("qrCodeUrl");
        orderId = getArguments().getString("orderId");

        createEnglishQRCode();
        dialog.findViewById(R.id.btn_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager)
                        getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                if ((!TextUtils.isEmpty(shareUrl)) && (!TextUtils.isEmpty(orderId))) {
                    clipboardManager.setText(shareUrl);
                    UIUtils.showToast(getActivity(), "代付链接已复制，快去发给你的父母吧！");
                    sharedSuccess(orderId, 11);
                } else {
                    UIUtils.showToast(getActivity(), "链接出问题啦，请稍后再试！");
                }
                ParentsPayDialogFragment.this.dismiss();
            }
        });
        //        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                dismiss();
        //            }
        //        });
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        return dialog;
    }

    private void createEnglishQRCode() {

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(qrCodeUrl, BGAQRCodeUtil.dp2px(getActivity(), 150), Color.parseColor("#ff000000"));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    ivQRcode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(getActivity(), "生成二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    /**
     * 分享日志
     */
    private void sharedSuccess(String orderNum, int type) {
        final ParentsPayLogService service = new ParentsPayLogService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback() {
            @Override
            public void onGetData(Object data) {
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
            }
        });
        service.postLogined("orderNum=" + orderNum + "&shareType=" + type, false);
    }
}
