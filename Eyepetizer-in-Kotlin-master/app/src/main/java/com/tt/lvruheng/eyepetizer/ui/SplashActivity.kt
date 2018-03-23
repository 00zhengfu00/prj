package com.tt.lvruheng.eyepetizer.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.tt.lvruheng.eyepetizer.R

/**
 * Created by lvruheng on 2017/7/2.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        initView()
    }

    private fun initView() {
        val mHandler = Handler()
        mHandler.postDelayed({
            val intent = Intent()
            intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }


}
