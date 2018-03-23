package com.iask.yiyuanlegou1.home.person;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.AppConfigure;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.home.person.setting.SettingActivity;
import com.iask.yiyuanlegou1.widget.TitleBarView;

public class AboutUsActivity extends BaseActivity {
    private TitleBarView title;

    @Override
    protected void findViewById() {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setTitleText(R.string.about_us);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            TextView tvVersion = (TextView) findViewById(R.id.tv_version);
            StringBuilder builder = new StringBuilder();
            builder.append("版本号：");
            builder.append(info.versionName);
            builder.append(".");
            builder.append(info.versionCode);
            builder.append("-");
            builder.append(getChannel());
            if (!AppConfigure.officialVersion) {
                builder.append("\n" + "测试版本");
            }
            tvVersion.setText(builder.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }



    private String getChannel() {
        String channel = "Android";
        ApplicationInfo info = null;
        try {
            info = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            if (info.metaData == null) {
                return channel;
            }
            channel = info.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
