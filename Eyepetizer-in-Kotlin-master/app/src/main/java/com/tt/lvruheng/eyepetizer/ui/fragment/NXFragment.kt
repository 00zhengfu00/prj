package com.tt.lvruheng.eyepetizer.ui.fragment

import android.content.Intent
import android.view.View
import com.tt.lvruheng.eyepetizer.R
import com.tt.lvruheng.eyepetizer.base.BaseApplication
import com.tt.lvruheng.eyepetizer.network.ServiceURL
import com.tt.lvruheng.eyepetizer.ui.InputPhoneActivity
import com.tt.lvruheng.eyepetizer.ui.WebViewActivity
import kotlinx.android.synthetic.main.fragment_nx.*

/**
 * Created by lvruheng on 2017/7/4.
 */
class NXFragment : BaseFragment() {

    var data: String? = null

    override fun getLayoutResources(): Int {
        return R.layout.fragment_nx
    }

    override fun initView() {
        linearLayout.setOnClickListener({
            val intent = Intent()
            intent.setClass(activity, WebViewActivity::class.java)
            val url = "${ServiceURL.URL}#/loan/loan/backHouse"
            intent.putExtra("url", url)
            startActivity(intent)
        })
    }

    override fun onResume() {
        super.onResume()
        if (BaseApplication.loginInfo != null) {
            container.setBackgroundResource(R.drawable.main_logined)
            ibStartNow.visibility = View.GONE
        } else {
            container.setBackgroundResource(R.drawable.main_unlogin)
            ibStartNow.visibility = View.VISIBLE
            ibStartNow.setOnClickListener({
                val intent = Intent()
                intent.setClass(activity, InputPhoneActivity::class.java)
                startActivity(intent)
            })
        }
    }
}