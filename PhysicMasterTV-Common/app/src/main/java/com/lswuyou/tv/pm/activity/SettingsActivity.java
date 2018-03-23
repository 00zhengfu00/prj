package com.lswuyou.tv.pm.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.common.Constant;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.version.GetNewVersionResponse;
import com.lswuyou.tv.pm.net.service.GetNewVersionService;
import com.lswuyou.tv.pm.utils.FileSizeUtil;
import com.lswuyou.tv.pm.view.TitleBarView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private TitleBarView mTitleBarView;
    private RelativeLayout layRatioCode, layClearRecord, layGetNewVersion, layClearCache;
    private TextView tvDefaultQuality, tvCacheSize;
    private int defaultPosition = 1;
    private AsyncTask<Void, Void, Boolean> clearCacheTask;
    private AsyncTask<Void, Void, String> calCacheTask;

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
        layRatioCode = (RelativeLayout) findViewById(R.id.ratio_lay);
        layClearRecord = (RelativeLayout) findViewById(R.id.clear_record_lay);
        layGetNewVersion = (RelativeLayout) findViewById(R.id.version_lay);
        layClearCache = (RelativeLayout) findViewById(R.id.clear_cache_lay);
        tvDefaultQuality = (TextView) findViewById(R.id.tv_quality);
        tvCacheSize = (TextView) findViewById(R.id.tv_cache_size);
        String cachedQuality = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys.VIDEO_QUELITY);
        String qualityTag = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys.VIDEO_QUELITY_TAG);
        defaultPosition = findIndex(qualityTag);
        if (!TextUtils.isEmpty(cachedQuality)) {
            tvDefaultQuality.setText(cachedQuality);
        }
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(0, R.string.settings);
        mTitleBarView.setBtnRight(0, R.string.settings);
        layClearRecord.setOnClickListener(this);
        layGetNewVersion.setOnClickListener(this);
        layClearCache.setOnClickListener(this);

        layRatioCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        defaultPosition += 1;
                        if (defaultPosition > 3) {
                            defaultPosition = 3;
                        }
                        tvDefaultQuality.setText(Constant.videoQualities[defaultPosition]);
                        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys
                                .VIDEO_QUELITY, Constant.videoQualities[defaultPosition]);
                        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys
                                .VIDEO_QUELITY_TAG, Constant.videoQualitiesTag[defaultPosition]);
                    } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                        defaultPosition -= 1;
                        if (defaultPosition < 0) {
                            defaultPosition = 0;
                        }
                        tvDefaultQuality.setText(Constant.videoQualities[defaultPosition]);
                        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys
                                .VIDEO_QUELITY, Constant.videoQualities[defaultPosition]);
                        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys
                                .VIDEO_QUELITY_TAG, Constant.videoQualitiesTag[defaultPosition]);
                    }
                }
                return false;
            }
        });
        calcurateCacheSize();
    }

    /**
     * 计算缓存大小
     */
    private void calcurateCacheSize() {
        calCacheTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                if (isCancelled()) {
                    return "";
                }
                String cacheSize = FileSizeUtil.getAutoFileOrFilesSize(Glide.getPhotoCacheDir(SettingsActivity.this).getAbsolutePath());
                return cacheSize;
            }

            @Override
            protected void onPostExecute(String size) {
                super.onPostExecute(size);
                if (!TextUtils.isEmpty(size)) {
                    tvCacheSize.setText(size);
                }
            }
        };
        calCacheTask.execute();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_settings;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_cache_lay:
                clearCache();
                break;
            case R.id.clear_record_lay:
                clearPlayRecord();
                break;
            case R.id.version_lay:
                getNewVersion();
                break;
        }
    }

    /**
     * 清理缓存
     */
    private void clearCache() {
        //清理Glide缓存图片
        clearCacheTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                if (isCancelled()) {
                    return false;
                }
                deleteFile(Glide.getPhotoCacheDir(SettingsActivity.this));
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(SettingsActivity.this, "缓存已清理！", Toast.LENGTH_SHORT).show();
                    calcurateCacheSize();
                }
                super.onPostExecute(aBoolean);
            }
        };
        clearCacheTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clearCacheTask != null) {
            clearCacheTask.cancel(true);
        }
        if (calCacheTask != null) {
            calCacheTask.cancel(true);
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param file
     */
    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        }
        file.delete();
    }

    /**
     * 清理播放记录
     */
    private void clearPlayRecord() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.PLAY_RECORD);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SettingsActivity.this, "播放记录已清除！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
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
                Toast.makeText(SettingsActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
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
    private void update(String version, String versionUrl) {
        String oldVersion = getVersion();
        if (oldVersion.equals(version)) {
            Toast.makeText(SettingsActivity.this, "当前已是最新版本！", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(SettingsActivity.this, "后台下载新版本中……", Toast.LENGTH_SHORT).show();
        DownLoadAsyncTask task = new DownLoadAsyncTask();
        String[] url = {versionUrl};
        task.execute(url);
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

    /**
     * 根据tag名称找出其在所有清晰度中的位置
     *
     * @param tag
     * @return
     */
    private int findIndex(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return 1;
        }
        String[] qualities = Constant.videoQualitiesTag;
        for (int i = 0; i < qualities.length; i++) {
            if (tag.equals(qualities[i])) {
                return i;
            }
        }
        return 1;
    }
}
