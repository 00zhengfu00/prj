/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.lswuyou.tv.pm.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.channel.login.LoginManager;
import com.lswuyou.tv.pm.channel.pay.TvChannelType;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.common.Constant;
import com.lswuyou.tv.pm.fragment.BaseIndexFragment;
import com.lswuyou.tv.pm.fragment.UserFragment;
import com.lswuyou.tv.pm.leanback.MainFragment;
import com.lswuyou.tv.pm.leanback.MainFragment2;
import com.lswuyou.tv.pm.leanback.RecommendFragment;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.response.account.GetLoginCfgResponse;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.net.response.homepage.GetHomePageDataResponse;
import com.lswuyou.tv.pm.net.response.homepage.HomePageData;
import com.lswuyou.tv.pm.net.response.version.GetNewVersionResponse;
import com.lswuyou.tv.pm.net.service.GetHomePageLoginedService;
import com.lswuyou.tv.pm.net.service.GetHomePageUnLoginedService;
import com.lswuyou.tv.pm.net.service.GetLoginCfgService;
import com.lswuyou.tv.pm.net.service.GetNewVersionService;
import com.lswuyou.tv.pm.utils.Utils;
import com.lswuyou.tv.pm.view.AlertDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import reco.frame.tv.view.TvTabHost;

/*
 * MainActivity class that loads MainFragment
 */
public class MainActivity extends FragmentActivity {
    private TvTabHost tth_container;
    private RelativeLayout laySearch;
    private TextView tvSearch;
    private UserFragment userFragment;
    private Drawable drawableSearchSelect, drawableSearchUnSelect;
    private RelativeLayout layUserInfo;
    private RoundedImageView ivHeadWeixin;
    private ImageView ivHeadDefault;
    private TextView tvNick;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        laySearch = findViewById(R.id.lay_search);
        tvSearch = findViewById(R.id.tv_search);
        laySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
        laySearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvSearch.setTextColor(getResources().getColor(R.color.white));
                    tvSearch.setCompoundDrawables(drawableSearchSelect, null, null, null);
                } else {
                    tvSearch.setTextColor(getResources().getColor(R.color.text_unselect_color));
                    tvSearch.setCompoundDrawables(drawableSearchUnSelect, null, null, null);
                }
            }
        });
        laySearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return true;
                }
                return false;
            }
        });
        layUserInfo = (RelativeLayout) findViewById(R.id.lay_user_info);
        ivHeadDefault = (ImageView) findViewById(R.id.iv_default_head);
        ivHeadWeixin = (RoundedImageView) findViewById(R.id.iv_weixin_head);
        tvNick = (TextView) findViewById(R.id.tv_nick);

        layUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isUserLogined()) {
                    startActivity(new Intent(MainActivity.this, LoginedActivity.class));
                } else {
                    getLoginCfg();
                }
            }
        });

        layUserInfo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvNick.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tvNick.setTextColor(getResources().getColor(R.color.text_unselect_color));
                }
            }
        });
        layUserInfo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    return true;
                }
                return false;
            }
        });

        drawableSearchSelect = getResources().getDrawable(R.mipmap.search_select);
        drawableSearchSelect.setBounds(0, 0, drawableSearchSelect.getMinimumWidth(), drawableSearchSelect.getMinimumHeight());
        drawableSearchUnSelect = getResources().getDrawable(R.mipmap.search_unselect);
        drawableSearchUnSelect.setBounds(0, 0, drawableSearchUnSelect.getMinimumWidth(), drawableSearchUnSelect.getMinimumHeight());
        refreshUserInfo();
        getNewVersion();
        loadFrag();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(userInfoUpdateReceiver, new IntentFilter(UserFragment.USERINFO_UPDATE));
    }

    //获取登录配置
    private void getLoginCfg() {
        GetLoginCfgService service = new GetLoginCfgService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetLoginCfgResponse>() {
            @Override
            public void onGetData(GetLoginCfgResponse data) {
                try {
                    String loginType = data.data.loginCfgVo.loginType;
                    if (loginType.equals(TvChannelType.none.name())) {
                        Intent intent = new Intent(MainActivity.this, UnLoginActivity.class);
                        startActivity(intent);
                    } else {
                        LoginManager.login(MainActivity.this, loginType);
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "获取登录配置异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(MainActivity.this, "获取登录配置信息失败！", Toast.LENGTH_SHORT).show();
            }
        });
        service.post("", true);
    }

    private void loadFrag() {
        tth_container = findViewById(R.id.tth_container);
        //添加用户中心
        userFragment = new UserFragment();
        tth_container.addPage(getFragmentManager(), userFragment, "用户中心");

        //添加精选视频列表
        RecommendFragment recommendFragment = new RecommendFragment();
        Bundle bundle0 = new Bundle();
        bundle0.putInt("subjectId", Constant.SUB_MATH);
        recommendFragment.setArguments(bundle0);
        tth_container.addPage(getFragmentManager(), recommendFragment, "免费精选");

        //添加初中数学
        MainFragment fragmentMath = new MainFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("subjectId", Constant.SUB_PHYSICS);
        fragmentMath.setArguments(bundle1);
        tth_container.addPage(getFragmentManager(), fragmentMath, "初中物理");

        //添加初中物理
        MainFragment fragmentPhysics = new MainFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("subjectId", Constant.SUB_CHEMISTRY);
        fragmentPhysics.setArguments(bundle2);
        tth_container.addPage(getFragmentManager(), fragmentPhysics, "初中化学");

        //添加初中化学
        MainFragment fragmentChemistry = new MainFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("subjectId", Constant.SUB_MATH);
        fragmentChemistry.setArguments(bundle3);
        tth_container.addPage(getFragmentManager(), fragmentChemistry, "初中数学");
//        //设监听
//        tth_container.setOnPageChangeListener(new TvTabHost.ScrollPageChangerListener() {
//            @Override
//            public void onPageSelected(int pageCurrent, boolean isTopFocused) {
//                if (!isTopFocused) {
//                    if (pageCurrent == 0) {
//                        userFragment.setInitFocus(1);
//                    }
//                }
//            }
//        });

//        tth_container.setOnTopBarFocusChangeListener(new TvTabHost.OnTopBarFocusChange() {
//            @Override
//            public void onFocusChange(boolean hasFocus, int postion) {
//
//            }
//
//            @Override
//            public void onFocusToContent(boolean hasFocus, int postion) {
//                if (!hasFocus) {
//                    indexFragments.get(postion).setSavedFocus();
//                }
//            }
//        });
        tth_container.buildLayout();
        tth_container.setCurrentPage(1);
    }

    /**
     * 获取各个tab页面的数据
     */
    private void getData() {
        OpenApiDataServiceBase service;
        Object obj = CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, LoginUserInfo.class);
        if (obj != null) {
            service = new GetHomePageLoginedService(this);
        } else {
            service = new GetHomePageUnLoginedService(this);
        }

        service.setCallback(new IOpenApiDataServiceCallback<GetHomePageDataResponse>() {
            @Override
            public void onGetData(GetHomePageDataResponse data) {
                try {
                    Toast.makeText(MainActivity.this, "获取数据成功！", Toast.LENGTH_SHORT).show();
                    initPage(data.data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "获取数据异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (obj != null) {
            service.postAES("", true);
        } else {
            service.post("", true);
        }
    }

    /**
     * 添加轮滑页面
     *
     * @param homePageData
     */
    private void initPage(HomePageData homePageData) {
    }

    private BroadcastReceiver userInfoUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshUserInfo();
        }
    };

    /**
     * 刷新微信头像及昵称
     */
    private void refreshUserInfo() {
        if (Utils.isUserLogined()) {
            LoginUserInfo userInfo = BaseApplication.getLoginUserInfo();
            ivHeadDefault.setVisibility(View.GONE);
            ivHeadWeixin.setVisibility(View.VISIBLE);
            Glide.with(this).load(userInfo.portrait).into(ivHeadWeixin);
            tvNick.setText(userInfo.nickname);
        } else {
            ivHeadDefault.setVisibility(View.VISIBLE);
            ivHeadWeixin.setVisibility(View.GONE);
            tvNick.setText("用户登录");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userInfoUpdateReceiver != null) {
            localBroadcastManager.unregisterReceiver(userInfoUpdateReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        //如果焦点不在titleBar，则将焦点定位在titleBar
        if (!tth_container.isTopFocused()) {
            tth_container.requestFocus();
            return;
        }
        AlertDialog dialog = new AlertDialog(this, "您确定要退出吗？", new AlertDialog.OnSubmitListener() {
            @Override
            public void onClick(boolean bool) {
                if (bool) {
                    MainActivity.super.onBackPressed();
                } else {

                }
            }
        });
        dialog.show();
    }

    private void sendBroadCastToFragment(int courseId, boolean add) {
        Intent intent = new Intent(BaseIndexFragment.COURSE_LIST_UPDATE);
        intent.putExtra("add", add);
        intent.putExtra("courseId", courseId);
        localBroadcastManager.sendBroadcast(intent);
    }

    /**
     * 获取新版本
     */
    private void getNewVersion() {
        GetNewVersionService service = new GetNewVersionService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetNewVersionResponse>() {
            @Override
            public void onGetData(GetNewVersionResponse data) {
                try {
                    String version = data.data.latestReleaseVersion;
                    String versionUrl = data.data.latestReleaseVersionPackageUrl_Android;
                    update(version, versionUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.postNoEncode("");
    }

    // 获取版本信息
    public String getVersion() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0";
    }

    //升级
    private void update(String version, final String versionUrl) {
        String oldVersion = getVersion();
        if (oldVersion.equals(version) || TextUtils.isEmpty(versionUrl)) {
            return;
        }
        AlertDialog dialog = new AlertDialog(this, "发现新版本，需要更新吗？", new AlertDialog
                .OnSubmitListener() {
            @Override
            public void onClick(boolean bool) {
                if (bool) {
                    Toast.makeText(MainActivity.this, "后台下载新版本中……", Toast.LENGTH_SHORT).show();
                    DownLoadAsyncTask task = new DownLoadAsyncTask();
                    String[] url = {versionUrl};
                    task.execute(url);
                } else {

                }
            }
        });
        dialog.show();
    }

    class DownLoadAsyncTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection connection = null;
            FileOutputStream outputStream = null;
            InputStream inputStream = null;
            long currentTime = System.currentTimeMillis();
            File file = new File(getExternalCacheDir(), "pmtv" + currentTime + ".apk");
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
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
                outputStream.close();
                inputStream.close();
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

    /**
     * 打开apk
     *
     * @param file
     */
    private void openFile(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
