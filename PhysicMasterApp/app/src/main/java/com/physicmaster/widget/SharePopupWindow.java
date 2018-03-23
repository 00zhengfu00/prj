/* 
 * 系统名称：lswuyou
 * 类  名  称：MyPopupMenu.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2016-4-21 下午3:28:20
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.physicmaster.R;

public class SharePopupWindow extends PopupWindow {
    private Context mContext;
    private LinearLayout llShareCircle, llSharePerson, llShareQQ, llShareQzone;
    private OnSubmitListener listener;
    public static final int ACTION_WEIXIN = 0;
    public static final int ACTION_CIRCLE = 1;
    public static final int ACTION_QQ = 2;
    public static final int ACTION_QZONE = 3;
    public static final int ACTION_WEIBO = 4;
    public static final int ACTION_LOGOUT = 5;

    public SharePopupWindow(Context mContext, OnSubmitListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        initPopupWindow();
    }

    private void initPopupWindow() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindow_edit_message, null);
        setContentView(view);
        setWidth(dp2px(mContext, 200));
        setHeight(dp2px(mContext, 250));
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);

        setAnimationStyle(android.R.style.Animation_Dialog);
        setTouchable(true);
        setFocusable(true);
        update();
        llShareCircle = (LinearLayout) view.findViewById(R.id.share_circle);
        llSharePerson = (LinearLayout) view.findViewById(R.id.share_person);
        llShareQQ = (LinearLayout) view.findViewById(R.id.share_qq);
        llShareQzone = (LinearLayout) view.findViewById(R.id.share_qzone);
        llSharePerson.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSubmit(ACTION_WEIXIN);
                dismiss();
            }
        });
        llShareCircle.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSubmit(ACTION_CIRCLE);
                dismiss();
            }
        });
        llShareQQ.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onSubmit(ACTION_QQ);
                dismiss();
            }
        });
        llShareQzone.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onSubmit(ACTION_QZONE);
                dismiss();
            }
        });
    }

    public void showPopupWindow(View view) {
        if (!isShowing()) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            showAtLocation(view, Gravity.NO_GRAVITY, location[0] - getWidth() + view.getWidth(), location[1] + dp2px(mContext, 45));
        }
    }

    public interface OnSubmitListener {
        void onSubmit(int action);
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}