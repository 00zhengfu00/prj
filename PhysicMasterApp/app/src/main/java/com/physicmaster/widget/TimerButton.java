package com.physicmaster.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.Button;


import com.physicmaster.R;

import java.util.Timer;
import java.util.TimerTask;


public class TimerButton extends Button {

    private Timer mTimer;
    private int countNumDecrease;
    private int countNumIncrease;
    private boolean isRunning = false;

    /**
     * 定义TimerButton 计时以及停止的时候的字体颜色和背景色
     */
    private int timingBackground, stopBackground, timingTextColor, stopTextColor;

    public boolean isRunning() {
        return 0 != countNumIncrease;

    }

    public TimerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public TimerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TimerButton(Context context) {
        super(context);

    }

    private void init(AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArrayed = getContext().obtainStyledAttributes(attrs, R.styleable
                    .TimerButton);
            countNumDecrease = typedArrayed.getInt(R.styleable.TimerButton_timecount, 60);
            countNumIncrease = 0;
            typedArrayed.recycle();
        }

    }

    public void onDestory() {
        clearStatus();
    }

    public void onTimerStart() {
        if (isEnabled() && !isRunning) {
            isRunning = true;
            setEnabled(false);
            setBackgroundResource(timingBackground);
            setTextColor(getResources().getColor(timingTextColor));
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    countNumIncrease++;
                    countNumDecrease--;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (countNumIncrease == 60) {
                                clearStatus();
                            } else {
                                setText(String.format(getContext().getString(R.string
                                        .transfer_dxyamcode_dy), countNumDecrease));
                            }
                        }

                    });
                }

            }, 10, 1000);
        }

    }

    public void clearStatus() {
        isRunning = false;
        if (mTimer != null) {
            mTimer.cancel();
        }
        setEnabled(true);
        mTimer = null;
        countNumIncrease = 0;
        countNumDecrease = 60;
        setText(getContext().getString(R.string.transfer_dxyamcode));
        // setBackgroundResource(R.drawable.timer_btn_background);
        setBackgroundResource(stopBackground);
        setTextColor(getResources().getColor(stopTextColor));

    }

    /**
     * 设置TimerButton的风格
     */
    public void setButtonStyle(int timingTextColor, int timingBackground, int stopTextColor, int
            stopBackground) {
        this.timingTextColor = timingTextColor;
        this.timingBackground = timingBackground;
        this.stopTextColor = stopTextColor;
        this.stopBackground = stopBackground;
    }

    /**
     * 回收资源
     */
    public void recycle() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }
}
