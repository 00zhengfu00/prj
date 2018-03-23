package com.physicmaster.modules.explore.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.GetPictrueProcess;
import com.physicmaster.modules.ExitDialogFragment;
import com.physicmaster.modules.WebviewActivity;
import com.physicmaster.modules.account.GetUserInfoService;
import com.physicmaster.modules.study.activity.exercise.ExcerciseDetailActivity;
import com.physicmaster.modules.study.fragment.StudyFragmentV2;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;

public class ScannerAvtivity extends BaseActivity implements QRCodeView.Delegate {


    private StartupResponse.DataBean startupDataBean;

    private LinearLayout llXiangce;
    private LinearLayout llErweima;
    private ZXingView zxingview;
    private ImageView ivBack;
    private GetPictrueProcess avatorProcess;
    /**
     * Android6.0需要申请权限
     */
    private final static String DANGEROUS_PERMISSION[] = new String[]{Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE};
    private final static int CAMERA_REQUEST_CODE = 1;
    private ArrayList<String> paths;

    @Override
    protected void findViewById() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        zxingview = (ZXingView) findViewById(R.id.zxingview);
        llErweima = (LinearLayout) findViewById(R.id.ll_erweima);
        llXiangce = (LinearLayout) findViewById(R.id.ll_xiangce);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llErweima.setSelected(true);
        llErweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llErweima.setSelected(true);
                llXiangce.setSelected(false);
                permissionRequest();
            }
        });
        llXiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llErweima.setSelected(false);
                llXiangce.setSelected(true);
                startScan2();
            }


        });

    }

    private void startScan2() {
        paths = new ArrayList<>();
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setPreviewEnabled(false)
                .setSelected(paths)
                .start(ScannerAvtivity.this);
        //        Intent intent = new Intent(Intent.ACTION_PICK);
        //        intent.setType("image/*");
        //        startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void initView() {
        //设置二维码的代理
        zxingview.setDelegate(this);
        startupDataBean = BaseApplication.getStartupDataBean();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_scanner_avtivity;
    }

    @Override
    protected void onStart() {
        super.onStart();
        permissionRequest();
    }

    /**
     * Android6.0权限申请
     */
    private void permissionRequest() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(this, DANGEROUS_PERMISSION,
                    CAMERA_REQUEST_CODE);
        } else {
            // 权限已经授予,直接初始化
            startScan();
        }
    }

    private void startScan() {
        //打开后置摄像头预览,但并没有开始扫描
        zxingview.startCamera();
        //开启扫描框
        zxingview.showScanRect();
        zxingview.startSpot();
    }

    //扫描成功解析二维码成功后调用,可做对应的操作
    @Override
    public void onScanQRCodeSuccess(final String result) {
        //扫描成功后调用震动器
        vibrator();
        //处理扫描结果

        if (startupDataBean.qrcodeCfg != null) {
            Pattern pattern1 = Pattern.compile(startupDataBean.qrcodeCfg.xiti);
            Matcher matcher1 = pattern1.matcher(result);
            if (matcher1.matches()) {
                refreshUserInfo();
                Intent intent = new Intent(ScannerAvtivity.this, ExcerciseDetailActivity.class);
                intent.putExtra("qrData", result);
                //intent.putExtra("isScanner", 1);
                startActivity(intent);
                finish();
            } else {
                Pattern pattern2 = Pattern.compile(startupDataBean.qrcodeCfg.whiteUrl);
                Matcher matcher2 = pattern2.matcher(result);
                if (matcher2.matches()) {
                    Intent intent = new Intent(ScannerAvtivity.this, WebviewActivity.class);
                    intent.putExtra("url", result);
                    startActivity(intent);
                    ScannerAvtivity.this.finish();
                } else {

                    Pattern pattern3 = Pattern.compile("^(http(s?)://+\\S*)$");
                    Matcher matcher3 = pattern3.matcher(result);
                    if (matcher3.matches()) {
                        ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", "是否继续？");
                        bundle.putString("content", "此链接可能存在风险!");
                        exitDialogFragment.setArguments(bundle);
                        exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                            @Override
                            public void ok() {
                                Intent intent = new Intent(ScannerAvtivity.this, WebviewActivity.class);
                                intent.putExtra("url", result);
                                startActivity(intent);
                                ScannerAvtivity.this.finish();
                            }
                        });
                        exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                    } else {
                        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        //再次延时1.5秒后启动
        zxingview.startSpot();
    }

    private void vibrator() {
        //获取系统震动服务
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    //扫描失败后调用的方法
    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "相机打开失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * 刷新用户信息
     */
    private void refreshUserInfo() {
        //首页数据刷新
        LocalBroadcastManager.getInstance(ScannerAvtivity.this).sendBroadcast(new Intent(StudyFragmentV2.ON_SUBJECT_CHANGED));
        startService(new Intent(ScannerAvtivity.this, GetUserInfoService.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview
                .REQUEST_CODE) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            paths.clear();
            if (photos != null) {
                paths.addAll(photos);
            }
            if (paths.size() == 0) {
                Toast.makeText(this, "未检测到二维码", Toast.LENGTH_SHORT).show();
                llErweima.setSelected(true);
                llXiangce.setSelected(false);
                startScan();
                return;
            }
            String path = paths.get(0);
            //处理扫描结果
            final String result = QRCodeDecoder.syncDecodeQRCode(path);
            if (TextUtils.isEmpty(result)) {
                Toast.makeText(this, "未检测到二维码", Toast.LENGTH_SHORT).show();
                llErweima.setSelected(true);
                llXiangce.setSelected(false);
                startScan();
                return;
            } else {
                vibrator();
                if (startupDataBean.qrcodeCfg != null) {
                    Pattern pattern1 = Pattern.compile(startupDataBean.qrcodeCfg.xiti);
                    Matcher matcher1 = pattern1.matcher(result);
                    if (matcher1.matches()) {
                        refreshUserInfo();
                        Intent intent = new Intent(ScannerAvtivity.this, ExcerciseDetailActivity.class);
                        intent.putExtra("qrData", result);
                        //intent.putExtra("isScanner", 1);
                        startActivity(intent);
                        finish();
                    } else {
                        Pattern pattern2 = Pattern.compile(startupDataBean.qrcodeCfg.whiteUrl);
                        Matcher matcher2 = pattern2.matcher(result);
                        if (matcher2.matches()) {
                            Intent intent = new Intent(ScannerAvtivity.this, WebviewActivity.class);
                            intent.putExtra("url", result);
                            startActivity(intent);
                            ScannerAvtivity.this.finish();
                        } else {

                            Pattern pattern3 = Pattern.compile("^(http(s?)://+\\S*)$");
                            Matcher matcher3 = pattern3.matcher(result);
                            if (matcher3.matches()) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("title", "是否继续？");
                                        bundle.putString("content", "此链接可能存在风险!");
                                        exitDialogFragment.setArguments(bundle);
                                        exitDialogFragment.setExitListener(new ExitDialogFragment.OnExit() {
                                            @Override
                                            public void ok() {
                                                Intent intent = new Intent(ScannerAvtivity.this, WebviewActivity.class);
                                                intent.putExtra("url", result);
                                                startActivity(intent);
                                                ScannerAvtivity.this.finish();
                                            }
                                        });
                                        exitDialogFragment.show(getSupportFragmentManager(), "exit_dialog");
                                    }
                                }, 300);

                            } else {
                                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

        }
        llErweima.setSelected(true);
        llXiangce.setSelected(false);
        startScan();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults != null) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        UIUtils.showToast(this, "获取权限失败");
                        return;
                    }
                }
                startScan();
            } else {
                UIUtils.showToast(this, "获取权限失败");
            }
        }
    }

}
