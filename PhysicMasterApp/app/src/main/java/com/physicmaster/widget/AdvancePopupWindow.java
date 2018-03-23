/* 
 * 系统名称：lswuyou
 * 类  名  称：MyPopupMenu.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：2.0.0
 * 开发人员： huashigen
 * 开发时间： 2016-12-26 下午3:28:20
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.physicmaster.R;

public class AdvancePopupWindow extends PopupWindow {
    private Context mContext;
    private MediaPlayer mediaPlayer;

    public AdvancePopupWindow(Context mContext) {
        this.mContext = mContext;
        initPopupWindow();
    }

    private void initPopupWindow() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pet_advance, null);
        setContentView(view);
//		setWidth(dp2px(mContext, 200));
//		setHeight(dp2px(mContext, 250));
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(false);

//		setAnimationStyle(android.R.style.Animation_Dialog);
        setTouchable(true);
        setFocusable(true);
        update();

        GifView gifAdvance = (GifView) view.findViewById(R.id.gif_advance);
        gifAdvance.setPlayTime(1);
        gifAdvance.setOnPlayFinishListener(new GifView.OnPlayFinishListener() {
            @Override
            public void onPlayFinish() {
                dismiss();
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });
        gifAdvance.setGifResource(R.mipmap.advance);
        mediaPlayer = MediaPlayer.create(mContext, R.raw.advance);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    public void showPopupWindow(View view) {
        if (!isShowing()) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
//			showAtLocation(view, Gravity.NO_GRAVITY, location[0] - getWidth() + view.getWidth(),
// location[1] + dp2px(mContext, 45));
            showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context
                .getResources().getDisplayMetrics());
    }
}