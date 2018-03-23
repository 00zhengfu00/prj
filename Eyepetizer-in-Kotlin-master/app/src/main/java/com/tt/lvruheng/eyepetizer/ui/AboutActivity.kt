package com.tt.lvruheng.eyepetizer.ui

import com.tt.lvruheng.eyepetizer.R
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.title_style_2.*

class AboutActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }

    override fun initView() {
        tvTitle.text = "关于侬享普惠"
        ivBack.setImageResource(R.drawable.back_black)
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        tvVersion.text = "侬享普惠${versionName}"

        ivBack.setOnClickListener({
            finish()
        })
    }

}
