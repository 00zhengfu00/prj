package com.physicmaster.modules.mine.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.NewVersionDialogFragment;
import com.physicmaster.base.SplashActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.WebviewActivity;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.TitleBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private String packageName;
    private ImageView ivMaster;
    private TextView tvMaster;
    private TextView tvInfo;

    @Override
    protected void findViewById() {

        ivMaster = (ImageView) findViewById(R.id.iv_master);
        tvMaster = (TextView) findViewById(R.id.tv_master);
        tvInfo = (TextView) findViewById(R.id.tv_info);


        findViewById(R.id.rl_question).setOnClickListener(this);
        findViewById(R.id.rl_help).setOnClickListener(this);
        findViewById(R.id.rl_version).setOnClickListener(this);
        String version = "";
        PackageManager manager = getPackageManager();
        try {
            version = manager.getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "";
            e.printStackTrace();
        }
        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        TextView tvVersion_ = (TextView) findViewById(R.id.tv_version_);
        tvVersion.setText(version);
        tvVersion_.setText(version);

        initTitle();
    }

    private void initTitle() {

        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("关于");
    }

    @Override
    protected void initView() {

        packageName = mContext.getPackageName();
        if (Constant.PHYSICMASTER.equals(packageName)) {
            tvMaster.setText("物理大师");
            tvInfo.setText("官方微信公众号: 物理大师\n客服电话: 400-812-0718\n周一至周日: 9:00-18:00");
            ivMaster.setImageResource(R.mipmap.icon_physic);
        } else if (Constant.CHYMISTMASTER.equals(packageName)) {
            tvMaster.setText("化学大师");
            tvInfo.setText("官方微信公众号: 化学大师\n客服电话: 400-812-0718\n周一至周日: 9:00-18:00");
            ivMaster.setImageResource(R.mipmap.icon_chemistory);
        } else if (Constant.MATHMASTER.equals(packageName)) {
            tvMaster.setText("数学大师");
            tvInfo.setText("官方微信公众号: 数学大师\n客服电话: 400-812-0718\n周一至周日: 9:00-18:00");
            ivMaster.setImageResource(R.mipmap.icon_math);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_question:
                if (BaseApplication.getStartupDataBean() == null) {
                    startActivity(new Intent(AboutActivity.this, SplashActivity.class));
                    AboutActivity.this.finish();
                    UIUtils.showToast(AboutActivity.this, "数据异常");
                    return;
                }
                intent = new Intent(AboutActivity.this, WebviewActivity.class);
                String questionUrl = BaseApplication.getStartupDataBean().faqPageUrl;
                intent.putExtra("url", questionUrl);
                intent.putExtra("allowShare", false);
                startActivity(intent);
                break;
            case R.id.rl_help:
                if (BaseApplication.getStartupDataBean() == null) {
                    startActivity(new Intent(AboutActivity.this, SplashActivity.class));
                    AboutActivity.this.finish();
                    UIUtils.showToast(AboutActivity.this, "数据异常");
                    return;
                }
                intent = new Intent(AboutActivity.this, WebviewActivity.class);
                String helpUrl = BaseApplication.getStartupDataBean().helpPageUrl;
                intent.putExtra("url", helpUrl);
                intent.putExtra("allowShare", false);
                startActivity(intent);
                break;
            case R.id.rl_version:
                if (BaseApplication.getStartupDataBean() == null) {
                    startActivity(new Intent(AboutActivity.this, SplashActivity.class));
                    AboutActivity.this.finish();
                    UIUtils.showToast(AboutActivity.this, "数据异常");
                    return;
                }
                int needUpdate = BaseApplication.getStartupDataBean().needUpgrade;
                if (1 == needUpdate) {
                    NewVersionDialogFragment fragment = new NewVersionDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "发现新版本");
                    fragment.setArguments(bundle);
                    fragment.setExitListener(new NewVersionDialogFragment.OnClickListener() {
                        @Override
                        public void ok() {
                            String url = BaseApplication.getStartupDataBean().latestDownloadUrl;
                            update(url);
                        }

                        @Override
                        public void cancel() {
                        }
                    });
                    fragment.show(getSupportFragmentManager(), "dialog");
                } else {
                    UIUtils.showToast(AboutActivity.this, "已是最新版本");
                }
                break;
        }
    }

    class DownLoadAsyncTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection connection = null;
            FileOutputStream outputStream = null;
            InputStream inputStream = null;
            long currentTime = System.currentTimeMillis();
            File apkPath = new File(getExternalCacheDir(), "apk");
            if (apkPath == null) {
                return 0;
            }
            if (!apkPath.exists()) {
                apkPath.mkdir();
            }
            File file = new File(apkPath, "wulidashi" + currentTime + ".apk");
            if (file == null) {
                return 0;
            }
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[256];
                connection.connect();
                int count = 0;
                while (true) {
                    int readCount = inputStream.read(buffer);
                    if (readCount <= 0) {
                        break;
                    } else {
                        outputStream.write(buffer, 0, readCount);
                        count = count + readCount;
                        publishProgress(count);
                    }
                }
                connection.disconnect();
                inputStream.close();
                outputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            openFile(file);
            return 100;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    private void openFile(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //android 7.0适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(this, "com.physicmaster.fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }

    // 检查更新
    private void update(String url) {
        File file = getExternalCacheDir();
        if (file == null || !file.exists()) {
            UIUtils.showToast(AboutActivity.this, "存储空间不足，无法更新");
            return;
        }
        UIUtils.showToast(this, "新版本后台下载中……");
        DownLoadAsyncTask task = new DownLoadAsyncTask();
        String[] urls = {url};
        task.execute(urls);
    }
}
