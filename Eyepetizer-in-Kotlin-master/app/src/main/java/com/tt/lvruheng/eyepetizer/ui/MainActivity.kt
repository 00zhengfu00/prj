package com.tt.lvruheng.eyepetizer.ui

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.SyncStateContract.Helpers.update
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import cn.cloudwalk.libproject.util.UIUtils
import com.tt.lvruheng.eyepetizer.R
import com.tt.lvruheng.eyepetizer.R.id.*
import com.tt.lvruheng.eyepetizer.base.BaseApplication
import com.tt.lvruheng.eyepetizer.network.IOpenApiDataServiceCallback
import com.tt.lvruheng.eyepetizer.network.response.UpdateVersionResponse
import com.tt.lvruheng.eyepetizer.network.service.UpdateVersionService
import com.tt.lvruheng.eyepetizer.ui.fragment.MineFragment
import com.tt.lvruheng.eyepetizer.ui.fragment.NXFragment
import com.tt.lvruheng.eyepetizer.ui.fragment.PHFragment
import com.tt.lvruheng.eyepetizer.utils.Constant
import com.tt.lvruheng.eyepetizer.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var nxFragment: NXFragment? = null
    var phFragment: PHFragment? = null
    var mineFragment: MineFragment? = null
    var mExitTime: Long = 0
    var toast: Toast? = null
    var lastSelectedId: Int = 0
    var dloadPgsNtftBuilder: Notification.Builder? = null
    var ntftManager: NotificationManager? = null
    val adsTime: Long = 3000
    var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRadioButton()
        initFragment(savedInstanceState)
        getNewVersion()
    }

    /**
     * 打开侬享页面
     */
    fun gotoMainPage() {
        clearState()
        lastSelectedId = 0
        rb_nx.isChecked = true
        rb_nx.setTextColor(resources.getColor(R.color.black))
        supportFragmentManager.beginTransaction().show(nxFragment)
                .hide(phFragment)
                .hide(mineFragment)
                .commit()
    }

    /**
     * 显示或隐藏导航栏
     */
    fun hideOrShowNavBar(show: Boolean) {
        if (show) {
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom)
            rg_root.startAnimation(animation)
            rg_root.visibility = View.VISIBLE
        } else {
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_bottom)
            rg_root.startAnimation(animation)
            rg_root.visibility = View.GONE
        }
    }

    /**
     * 获取新版本
     */
    private fun getNewVersion() {
        val updateService = UpdateVersionService(this)
        val version = packageManager.getPackageInfo(packageName, 0).versionName
        updateService.callback = object : IOpenApiDataServiceCallback<UpdateVersionResponse> {
            override fun onGetData(data: UpdateVersionResponse?) {
                if (data!!.data.newApp == "1") {
                    val builder = AlertDialog.Builder(this@MainActivity).setMessage(data.data.intro).setTitle("更新版本").setPositiveButton("确定") { dialog, which -> update(data.data.url) }
                    if (data.data.isforce == "0") {
                        builder.setNegativeButton("取消") { dialog, whitch ->
                            finish()
                        }
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }

            override fun onGetError(errorCode: Int, errorMsg: String?, error: Throwable?) {
            }

        }
        updateService.postLogined("type=1&version=${version}", false)
    }

    fun update(url: String) {
        val file = externalCacheDir
        if (file == null || !file.exists()) {
            showToast("存储空间不足，无法更新")
            return
        }
        UIUtils.showToast(this, "新版本后台下载中……")
        val task = DownLoadAsyncTask()
        task.execute(url)
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            //异常情况
            val mFragments: List<Fragment> = supportFragmentManager.fragments
            for (item in mFragments) {
                if (item is NXFragment) {
                    nxFragment = item
                }
                if (item is PHFragment) {
                    phFragment = item
                }
                if (item is MineFragment) {
                    mineFragment = item
                }
            }
        } else {
            nxFragment = NXFragment()
            phFragment = PHFragment()
            mineFragment = MineFragment()
            val fragmentTrans = supportFragmentManager.beginTransaction()
            fragmentTrans.add(R.id.fl_content, nxFragment)
            fragmentTrans.add(R.id.fl_content, phFragment)
            fragmentTrans.add(R.id.fl_content, mineFragment)
            fragmentTrans.commit()
        }
        supportFragmentManager.beginTransaction().show(nxFragment)
                .hide(phFragment)
                .hide(mineFragment)
                .commit()
    }

    private fun setRadioButton() {
        rb_nx.isChecked = true
        rb_nx.setTextColor(resources.getColor(R.color.black))
        rb_nx.setOnClickListener(this)
        rb_ph.setOnClickListener(this)
        rb_mine.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rb_ph -> {
                clearState()
                lastSelectedId = 1
                rb_ph.isChecked = true
                rb_ph.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(phFragment)
                        .hide(nxFragment)
                        .hide(mineFragment)
                        .commit()
            }
            R.id.rb_nx -> {
                clearState()
                lastSelectedId = 0
                rb_nx.isChecked = true
                rb_nx.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(nxFragment)
                        .hide(phFragment)
                        .hide(mineFragment)
                        .commit()
            }
            R.id.rb_mine -> {
                var logined = BaseApplication.loginInfo != null
                if (!logined) {
                    rb_mine.isChecked = false
                    if (lastSelectedId == 0) {
                        rb_nx.isChecked = true
                        rb_nx.setTextColor(resources.getColor(R.color.black))
                    } else if (lastSelectedId == 1) {
                        rb_ph.isChecked = true
                        rb_ph.setTextColor(resources.getColor(R.color.black))
                    }
                    val intent = Intent()
                    intent.setClass(this, InputPhoneActivity::class.java)
                    startActivity(intent)
                } else {
                    clearState()
                    rb_mine.isChecked = true
                    rb_mine.setTextColor(resources.getColor(R.color.black))
                    supportFragmentManager.beginTransaction().show(mineFragment)
                            .hide(phFragment)
                            .hide(nxFragment)
                            .commit()
                }
            }
        }

    }

    private fun clearState() {
        rg_root.clearCheck()
        rb_nx.setTextColor(resources.getColor(R.color.gray))
        rb_mine.setTextColor(resources.getColor(R.color.gray))
        rb_ph.setTextColor(resources.getColor(R.color.gray))
    }

    override fun onPause() {
        super.onPause()
        toast?.let { toast!!.cancel() }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 3000) {
                finish()
                toast!!.cancel()
            } else {
                mExitTime = System.currentTimeMillis()
                toast = showToast("再按一次退出程序")
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    internal inner class DownLoadAsyncTask : AsyncTask<String, Int, Int>() {

        override fun doInBackground(vararg params: String): Int? {
            var connection: HttpURLConnection? = null
            var outputStream: FileOutputStream? = null
            var inputStream: InputStream? = null
            val currentTime = System.currentTimeMillis()
            val apkPath = File(externalCacheDir, "apk") ?: return 0
            if (!apkPath.exists()) {
                apkPath.mkdir()
            }
            val file = File(apkPath, "wulidashi$currentTime.apk") ?: return 0
            try {
                val url = URL(params[0])
                connection = url.openConnection() as HttpURLConnection
                inputStream = connection.inputStream
                outputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                connection.connect()
                val totalLength = connection.contentLength
                var count = 0
                while (true) {
                    val readCount = inputStream!!.read(buffer)
                    if (readCount <= 0) {
                        break
                    } else {
                        outputStream!!.write(buffer, 0, readCount)
                        count += readCount
                        val progress = (count.toFloat() / totalLength.toFloat() * 100).toInt()
                        if (0 == progress % 5) {
                            dloadPgsNtftBuilder!!.setProgress(100, progress, false).setContentText(progress.toString() + "%")
                            ntftManager!!.notify(1, dloadPgsNtftBuilder!!.build())
                        }
                    }
                }
                connection.disconnect()
                inputStream.close()
                outputStream!!.close()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            openFile(file)
            return 100
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            ntftManager!!.cancel(1)
        }

        override fun onPreExecute() {
            super.onPreExecute()
            var title = "侬享普惠更新"
            val icon = R.mipmap.icon
            dloadPgsNtftBuilder = Notification.Builder(this@MainActivity)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText("0%")
            ntftManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            ntftManager!!.notify(1, dloadPgsNtftBuilder!!.build())
        }
    }

    private fun openFile(file: File) {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //android 7.0适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentUri = FileProvider.getUriForFile(this, packageName + ".fileprovider",
                    file)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } else {
            val uri = Uri.fromFile(file)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            startActivity(intent)
        }
    }
}
