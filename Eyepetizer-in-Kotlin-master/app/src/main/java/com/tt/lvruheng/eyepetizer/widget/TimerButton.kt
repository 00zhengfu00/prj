package com.tt.lvruheng.eyepetizer.widget

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.widget.Button
import com.tt.lvruheng.eyepetizer.R

/**
 * TODO: document your custom view class.
 */
class TimerButton : Button {
    private var countDownTimer: CountDownTimer? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.TimerButton, defStyle, 0)
        a.recycle()
    }

    /**
     * start timing
     */
    fun startTimer(period: Long) {
        countDownTimer = object : CountDownTimer(period, 1 * 1000) {
            override fun onTick(millisUntilFinished: Long) {
                text = "${millisUntilFinished / 1000}秒"
            }

            override fun onFinish() {
                text = "获取验证码"
                isEnabled = true
            }
        }
        isEnabled = false
        countDownTimer!!.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
    }
}
