package com.physicmaster.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by huashigen on 2016/11/18.
 */
public class ProgressLoadingDialog {
    private Context context;
    private ProgressDialog progressDialog;

    public ProgressLoadingDialog(Context context) {
        this.context = context;
    }

    public void showDialog(final onCancelListener listener) {
        progressDialog = ProgressDialog.show(context, "正在加载……", "请等候……", true, true, new
                DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (listener != null) {
                            listener.onCancel();
                        }
                    }
                });
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void dismissDialog() {
        progressDialog.dismiss();
    }

    public interface onCancelListener {
        void onCancel();
    }
}
