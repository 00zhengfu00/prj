package com.lswuyou.tv.pm.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;

/**
 * Created by Administrator on 2016/8/15.
 */
public class AlertDialog extends Dialog {
    private Context mContext;
    private OnSubmitListener listener;
    private Button btnYes, btnNo;
    private TextView tvTitle;
    private String title;

    public AlertDialog(Context context, OnSubmitListener listener) {
        super(context, R.style.CustomStyle);
        mContext = context;
        this.listener = listener;
        init();
    }

    public AlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    public AlertDialog(Context context, String title, OnSubmitListener listener) {
        super(context, R.style.CustomStyle);
        mContext = context;
        this.title = title;
        this.listener = listener;
        init();
    }

    protected AlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_alert);
        btnYes = (Button) findViewById(R.id.btn_yes);
        btnNo = (Button) findViewById(R.id.btn_no);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(true);
                dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(false);
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        super.show();
    }

    public interface OnSubmitListener {
        public void onClick(boolean bool);
    }
}
