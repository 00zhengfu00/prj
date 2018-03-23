package com.tt.lvruheng.eyepetizer.widget.loading.custom;

import android.app.Dialog;
import android.content.Context;

import com.tt.lvruheng.eyepetizer.R;
import com.tt.lvruheng.eyepetizer.widget.loading.DialogFactory;


/**
 * 自定义样式dialog的例子
 * author  dengyuhan
 * created 2017/4/16 05:13
 */
public class CustomDialogFactory implements DialogFactory {
    @Override
    public Dialog onCreateDialog(Context context) {
        return new CustomDialog(context, R.style.myDialogTheme);
    }

    @Override
    public void setMessage(Dialog dialog, CharSequence message) {
    }

    @Override
    public int getAnimateStyleId() {
        return R.style.Dialog_Alpha_Animation;
    }
}
