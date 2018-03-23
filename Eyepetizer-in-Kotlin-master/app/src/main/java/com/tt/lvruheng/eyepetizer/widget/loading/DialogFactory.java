package com.tt.lvruheng.eyepetizer.widget.loading;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StyleRes;

/**
 * author  dengyuhan
 * created 2017/4/16 04:08
 */
public interface DialogFactory<D extends Dialog> {
    /**
     * 创建dialog
     * @param context
     * @return
     */
    D onCreateDialog(Context context);

    /**
     * 设置提示消息
     * @param dialog
     * @param message
     */
    void setMessage(D dialog, CharSequence message);

    /**
     * 进入退出的动画id
     * @return
     */
    @StyleRes
    int getAnimateStyleId();
}
