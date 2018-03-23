package com.physicmaster.base;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.permission.annotation.OnMPermissionNeverAskAgain;
import com.netease.nimlib.sdk.NimIntent;
import com.physicmaster.R;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.modules.account.UALService;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.GetTimeResponse;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.security.JksX509TrustManager;
import com.physicmaster.net.security.SSLSocketFactoryEx;
import com.physicmaster.net.service.account.GetTimeService;
import com.physicmaster.net.service.account.StartupLoginedService;
import com.physicmaster.net.service.account.StartupUnLoginService;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;

public class SplashActivity extends FragmentActivity {
    private long adsTime = 3000;
    private long startTime;
    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS
    };
    private final String[] CONTACT_PERMISSIONS = new String[]{
            Manifest.permission.READ_CONTACTS
    };
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    private final int CONTACT_PERMISSION_REQUEST_CODE = 111;

    private Logger logger = AndroidLogger.getLogger(SplashActivity.this.getClass().getSimpleName());
    private int icon;
    private Notification.Builder dloadPgsNtftBuilder;
    private NotificationManager ntftManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        requestBasicPermission();
        verifyContactPermissions();
    }

    public void verifyContactPermissions() {
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, CONTACT_PERMISSIONS,
                    CONTACT_PERMISSION_REQUEST_CODE);
        } else {
            if (BaseApplication.getUserData() != null) {
                Intent intent = new Intent(this, UALService.class);
                startService(intent);
            }
        }
    }

    private void requestBasicPermission() {
        MPermission.with(SplashActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == BASIC_PERMISSION_REQUEST_CODE) {
            MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        } else if (requestCode == CONTACT_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (BaseApplication.getUserData() != null) {
                    Intent intent = new Intent(this, UALService.class);
                    startService(intent);
                }
            } else {

            }
        }
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        getProtocol();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        UIUtils.showToast(this, "授权失败");
        finish();
    }

    /**
     * 获取访问协议
     */
    private void getProtocol() {
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.ca);
            JksX509TrustManager jksX509TrustManager = new JksX509TrustManager(inputStream,
                    Constant.KEYSTORE_PWD.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory
                    .getDefaultAlgorithm());
            InputStream keyStoreStream = this.getResources().openRawResource(R.raw.ca);
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(keyStoreStream, Constant.KEYSTORE_PWD.toCharArray());
            keyManagerFactory.init(keyStore, Constant.KEYSTORE_PWD.toCharArray());
            client.setSSLSocketFactory(new SSLSocketFactoryEx(keyStore, keyManagerFactory.getKeyManagers(), jksX509TrustManager));
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        long time = System.currentTimeMillis();
        String protocolUrl = "";
        String packageName = getPackageName();
        if (packageName.equals(Constant.PHYSICMASTER)) {
            protocolUrl = ServiceURL.PM_PROTOCOL_URL;
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            protocolUrl = ServiceURL.CM_PROTOCOL_URL;
        } else if (packageName.equals(Constant.MATHMASTER)) {
            protocolUrl = ServiceURL.MM_PROTOCOL_URL;
        }
        client.get(this, protocolUrl + "?v=" + time, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONObject data = response.getJSONObject("data");
                            //协议和host都不为空才赋值
                            String protocol = data.getString("protocol");
                            String host = data.getString("host");
                            if (!TextUtils.isEmpty(protocol) && !TextUtils.isEmpty(host)) {
                                SpUtils.putString(SplashActivity.this, "wlds_api_url", protocol +
                                        host);
                                getStartupInfo(protocol + host);
                            } else {
                                getStartupInfo("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String
                            responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        getStartupInfo("");
                    }

                    //获取失败之后直接使用默认的数据协议-启用https
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        getStartupInfo("");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        getStartupInfo("");
                    }

                }
        );
    }

    //获取服务器时间
    private void getServerTime(final String apiUrl) {
        GetTimeService service = new GetTimeService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetTimeResponse>() {
            @Override
            public void onGetData(GetTimeResponse data) {
                //客户端时间误差过大，需要启动同步机制
                if (1 == data.data.sync) {
                    OpenApiDataServiceBase.openTimeSync = true;
                    BaseApplication.setServerTime(data.data.currentTimeMillis);
                    BaseApplication.setStartTime(SystemClock.elapsedRealtime());
                }
                startup(apiUrl);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                startup(apiUrl);
            }
        });
        service.postUnLogin("ct=" + System.currentTimeMillis(), false);
    }

    private void getStartupInfo(String apiUrl) {
        getServerTime(apiUrl);
    }

    private void startup(String apiUrl) {
        if (!TextUtils.isEmpty(apiUrl)) {
            ServiceURL.DYNAMIC_URL = apiUrl;
        } else {
            String cachedApiUrl = SpUtils.getString(this, "wlds_api_url", "");
            ServiceURL.DYNAMIC_URL = cachedApiUrl;
        }
        startTime = System.currentTimeMillis();
        String miPushId = SpUtils.getString(this, SpUtils.MI_PUSH_ID, "");
        if (!isNetworkOk()) {
            UIUtils.showToast(SplashActivity.this, "网络好像出问题了，请检查！");
            return;
        }
        OpenApiDataServiceBase service = null;
        if (Utils.isUserLogined()) {
            service = new StartupLoginedService(this);
        } else {
            service = new StartupUnLoginService(this);
        }
        service.setCallback(new IOpenApiDataServiceCallback<StartupResponse>() {
            @Override
            public void onGetData(final StartupResponse data) {
                List<StartupResponse.DataBean.BookMenuBean> bookMenus = data.data.bookMenu;
//                if (bookMenus != null) {
//                    StartupResponse.DataBean.BookMenuBean math = bookMenus.get(0);
//                    bookMenus.add(math);
//                    bookMenus.remove(0);
//                }
                if (data.data.ossConfig != null) {
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.OSS_SERVER_INFO,
                            data.data.ossConfig);
                }
                BaseApplication.setStartupDataBean(data.data);
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.STARTUP_INFO, data.data);
                if (1 == data.data.needUpgrade) {
                    NewVersionDialogFragment fragment = new NewVersionDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "发现新版本");
                    bundle.putString("content", data.data.upgradeTip);
                    fragment.setArguments(bundle);
                    fragment.setExitListener(new NewVersionDialogFragment.OnClickListener() {
                        @Override
                        public void ok() {
                            update(data.data.latestDownloadUrl);
                        }

                        @Override
                        public void cancel() {
                            long curTime = System.currentTimeMillis();
                            long passTime = curTime - startTime;
                            long leftTime = adsTime - passTime;
                            if (leftTime < 0) {
                                leftTime = 0;
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    start();
                                }
                            }, leftTime);
                        }
                    });
                    fragment.show(getSupportFragmentManager(), "dialog");
                } else {
                    long curTime = System.currentTimeMillis();
                    long passTime = curTime - startTime;
                    long leftTime = adsTime - passTime;
                    if (leftTime < 0) {
                        leftTime = 0;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            start();
                        }
                    }, leftTime);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(SplashActivity.this, errorMsg);
            }
        });
        if (Utils.isUserLogined()) {
            service.postLogined("miPush=" + miPushId + "&mkc=" + getChannel(), false);
        } else {
            service.postUnLogin("miPush=" + miPushId + "&mkc=" + getChannel(), false);
        }
    }

    private void start() {
//        if (NetworkUtils.getNetworkState(this).equals(Constant.NETTYPE_WIFI)) {
//            Intent cacheService = new Intent(this, DownloadService.class);
//            startService(cacheService);
//        }
        //从未使用过跳到引导页面
        UserDataResponse.UserDataBean.LoginVoBean loginVoBean = BaseApplication.getUserData();
        if (loginVoBean == null || loginVoBean.isInitial == 0) {
            boolean isNoteBookGuide = SpUtils.getBoolean(this, SpUtils.NOTEBOOK_GUIDE, false);
            if (!isNoteBookGuide) {
                SpUtils.putBoolean(this, SpUtils.NOTEBOOK_GUIDE, true);
                startActivity(new Intent(this, NoteBookGuideActivity.class));
            } else {
                startActivity(new Intent(this, GuideActivity.class));
            }
        } else {
            boolean isNoteBookGuide = SpUtils.getBoolean(this, SpUtils.NOTEBOOK_GUIDE, false);
            Intent intent = getIntent();
            if (!isNoteBookGuide) {
                SpUtils.putBoolean(this, SpUtils.NOTEBOOK_GUIDE, true);
                Intent nIntent = new Intent(this, NoteBookGuideActivity.class);
                if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                    nIntent.putExtra(NimIntent.EXTRA_NOTIFY_CONTENT, intent.getSerializableExtra
                            (NimIntent.EXTRA_NOTIFY_CONTENT));
                }
                startActivity(nIntent);
            } else {
                Intent tIntent = new Intent(this, MainActivity.class);
                if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                    tIntent.putExtra(NimIntent.EXTRA_NOTIFY_CONTENT, intent.getSerializableExtra
                            (NimIntent.EXTRA_NOTIFY_CONTENT));
                }
                startActivity(tIntent);
            }
        }
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    /**
     * 判断网络是否正常
     */
    private boolean isNetworkOk() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService
                (Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            return false;
        } else {
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if (null != infos && infos.length > 0) {
                for (NetworkInfo info : infos) {
                    if (NetworkInfo.State.CONNECTED == info.getState()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // 检查更新
    private void update(String url) {
        if (TextUtils.isEmpty(url)) {
            long curTime = System.currentTimeMillis();
            long passTime = curTime - startTime;
            long leftTime = adsTime - passTime;
            if (leftTime < 0) {
                leftTime = 0;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            }, leftTime);
            return;
        }
        File file = getExternalCacheDir();
        if (file == null || !file.exists()) {
            UIUtils.showToast(SplashActivity.this, "存储空间不足，无法更新");
            return;
        }
        UIUtils.showToast(this, "新版本后台下载中……");
        DownLoadAsyncTask task = new DownLoadAsyncTask();
        String[] urls = {url};
        task.execute(urls);
        long curTime = System.currentTimeMillis();
        long passTime = curTime - startTime;
        long leftTime = adsTime - passTime;
        if (leftTime < 0) {
            leftTime = 0;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }, leftTime);
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
                byte[] buffer = new byte[1024];
                connection.connect();
                int totalLength = connection.getContentLength();
                int count = 0;
                while (true) {
                    int readCount = inputStream.read(buffer);
                    if (readCount <= 0) {
                        break;
                    } else {
                        outputStream.write(buffer, 0, readCount);
                        count = count + readCount;
                        int progress = (int) (((float) count / (float) totalLength) * 100);
                        if (0 == progress % 5) {
                            dloadPgsNtftBuilder.setProgress(100, progress, false).setContentText(progress + "%");
                            ntftManager.notify(1, dloadPgsNtftBuilder.build());
                        }
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
            ntftManager.cancel(1);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String packageName = getPackageName();
            String title = "物理大师更新";
            icon = R.mipmap.icon_physic;
            if (packageName.equals(Constant.PHYSICMASTER)) {
                icon = R.mipmap.icon_physic;
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                icon = R.mipmap.icon_chemistory;
                title = "化学大师更新";
            } else if (packageName.equals(Constant.MATHMASTER)) {
                icon = R.mipmap.icon_math;
                title = "数学大师更新";
            }
            dloadPgsNtftBuilder = new Notification.Builder(SplashActivity.this)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText("0%");
            ntftManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            ntftManager.notify(1, dloadPgsNtftBuilder.build());
        }
    }

    private void openFile(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //android 7.0适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider",
                    file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            startActivity(intent);
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
