/* 
 * 系统名称：lswuyou
 * 类  名  称：ViewUtils.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2016-1-7 上午11:15:23
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewUtils {
    private static String BOOLEAN_TRUE = "1";

    private static String BOOLEAN_FALSE = "0";

    private static String EMPTY = "";

    private static final String TAG = "ViewUtils";

    /**
     * 判断View不为空
     *
     * @param v
     * @return
     */
    public static boolean notNullView(View v) {
        return v != null;
    }

    /**
     * 设置值
     *
     * @param tv
     * @param value
     */
    public static void setText(TextView tv, String value) {
        if (notNullView(tv)) {
            if (StringUtils.isNotEmpty(value)) {
                tv.setText(value);
            } else {
                tv.setText("");
            }
        } else {
            Log.d(TAG, "EditText/TextView组件是空值");
        }
    }

    /**
     * 在view中设置tag
     *
     * @param view
     * @param value
     */
    public static void setTag(View view, String value) {
        if (notNullView(view)) {
            view.setTag(value);
        }
    }

    /**
     * 获取输入内容
     *
     * @param tv
     * @return
     */
    public static final String getText(TextView tv) {
        return (notNullView(tv)) ? tv.getText().toString() : EMPTY;
    }

    /**
     * 设置输入框是否可编辑
     *
     * @param view
     * @param enabled
     */
    public static void setEnabled(View view, boolean enabled) {
        if (notNullView(view)) {
            view.setEnabled(enabled);
        }
    }

    /**
     * 设置Spinner值
     *
     * @param s
     * @param val
     * @param str
     */
    public static void setSpinner(Spinner s, String val, String[] str) {
        if (StringUtils.isEmpty(val) || (str == null || str.length == 0)) {
            return;
        }
        int len = str.length;
        for (int i = 0; i < len; i++) {
            if (val.equals(str[i])) {
                s.setSelection(i);
            }
        }
    }

    /**
     * @param s
     * @param seq
     */
    public static void setSpinnerPos(Spinner s, int seq) {
        int pos = seq - 1;
        if (pos >= 0) {
            s.setSelection(pos);
        }

    }

    public static String getSpinnerText(Spinner s) {
        return (String) s.getSelectedItem();
    }

    /**
     * 得到下拉框的序号值，从1开始
     *
     * @param s
     * @return
     */
    public static int getSpinnerSeq(Spinner s) {
        int pos = s.getSelectedItemPosition();
        return pos + 1;
    }

    public static void setCheckbox(CheckBox cb, String val) {
        if (BOOLEAN_TRUE.equals(val)) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }
    }

    public static String getCheckBoxVal(CheckBox checkBox) {
        if (checkBox != null) {
            return checkBox.isChecked() ? BOOLEAN_TRUE : BOOLEAN_FALSE;
        }
        return BOOLEAN_FALSE;
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param v
     */
    public static void hideInputSoft(Context context, View v) {
        if (context == null || ((Activity) context).getCurrentFocus() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 显示键盘
     *
     * @param context
     * @param v
     */
    public static void showInputSoft(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (imm != null) {
            v.requestFocus();
            imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 全屏
     *
     * @param w
     */
    public static void fullScreen(Window w) {
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
                .FLAG_FULLSCREEN);
    }
}
