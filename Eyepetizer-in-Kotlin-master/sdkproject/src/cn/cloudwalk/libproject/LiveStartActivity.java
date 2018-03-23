/**
 * Project Name:cwFaceForDev3
 * File Name:LiveStartActivity.java
 * Package Name:cn.cloudwalk.dev.mobilebank
 * Date:2016-5-16上午9:17:24
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd All Rights Reserved.
 */

package cn.cloudwalk.libproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cn.cloudwalk.CloudwalkSDK;
import cn.cloudwalk.libproject.callback.NoDoubleClickListener;
import cn.cloudwalk.libproject.progressHUD.CwProgressHUD;
import cn.cloudwalk.libproject.util.FileUtil;
import cn.cloudwalk.libproject.util.LogUtils;

import static cn.cloudwalk.libproject.util.FileUtil.assetsDataToDest;
import static cn.cloudwalk.libproject.util.FileUtil.unZipFolder;

/**
 * ClassName: LiveStartActivity <br/>
 * Description: <br/>
 * date: 2016-5-16 上午9:17:24 <br/>
 *
 * @author 284891377
 * @since JDK 1.7
 */
public class LiveStartActivity extends TemplatedActivity {
	private final String TAG = LogUtils.makeLogTag("LiveStartActivity");
    private String storagePath;
    private static final String MODULES = "modules";//模型文件根目录
    private static final String MODULES_ZIP = "modules.zip";//模型文件压缩包
	Button mBt_startdect;
    public CwProgressHUD processDialog;// 进度框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.cloudwalk_layout_facedect_start);
		setTitle(R.string.cloudwalk_live_title);
		mBt_startdect = (Button) findViewById(R.id.bt_startdect);
		mBt_startdect.setOnClickListener(new NoDoubleClickListener() {

			@Override
			public void onNoDoubleClick(View v) {
				if (v.getId() == R.id.bt_startdect) {

					startActivity(new Intent(LiveStartActivity.this, LiveActivity.class));
					finish();

				}

			}
		});

        checkModule();

    }

    //检查模型是否存在
    public void checkModule(){
        //再次检查模型文件是否已拷贝到app运行目录
        final File installation = new File(getFilesDir(), MODULES);
        storagePath = installation.getAbsolutePath();
        StringBuilder pFaceDetectFile = new StringBuilder(storagePath + File.separator + "faceDetector_2_4.mdl");
        StringBuilder pFaceKeyPtFile = new StringBuilder(storagePath + File.separator + "keypt_detect_model_sdm_9pts.bin");
        StringBuilder pFaceKeyPtTrackFile = new StringBuilder(storagePath + File.separator + "keypt_track_model_sdm_9pts.bin");
        StringBuilder pFaceQualityFile = new StringBuilder(storagePath + File.separator + "facequality_4_1.bin");
        StringBuilder pFaceLivenessFile = new StringBuilder(storagePath+ File.separator + "liveness171120.bin");
        //检测每个模型文件是否存在
        boolean allModulesExist = false;
        if (installation.exists() && new File(pFaceDetectFile.toString()).exists()
                && new File(pFaceKeyPtFile.toString()).exists() && new File(pFaceKeyPtTrackFile.toString()).exists()
                && new File(pFaceQualityFile.toString()).exists() && new File(pFaceLivenessFile.toString()).exists()) {
            allModulesExist = true;
        }

        if (!allModulesExist) {
            processDialog = CwProgressHUD.create(this).setStyle(CwProgressHUD.Style
                    .SPIN_INDETERMINATE)
                    .setLabel(getString(R.string.cloudwalk_copy_modules)).setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
            processDialog.show();
            mBt_startdect.setEnabled(false);
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    //
                    boolean initStage = false;
                    String dataDirPath = getFilesDir().getAbsolutePath();//data/data/package_name/files/
                    try {
                        //模型文件拷贝
                        assetsDataToDest(LiveStartActivity.this,MODULES_ZIP, dataDirPath + File.separator + MODULES_ZIP);//拷贝模型文件
                        //接压缩模型文件
                        unZipFolder(dataDirPath + File.separator + MODULES_ZIP, dataDirPath);
                        //设置模型文件路径
                        CloudwalkSDK.getInstance().setModulePath(dataDirPath + File.separator + MODULES);
                        initStage = true;
                    } catch (IOException ex) {
                        initStage = false;
                        Log.e(TAG, "copy module IOException:" + ex.getMessage());
                    } catch (RuntimeException e) {
                        initStage = false;
                        Log.e(TAG, "copy module RuntimeException:" + e.getMessage());
                    } catch (Exception e) {
                        initStage = false;
                        Log.e(TAG, "copy module Exception:" + e.getMessage());
                    } finally {
                        //删除模型文件压缩包
                        File modelsZip = new File(dataDirPath + File.separator + MODULES_ZIP);
                        if (modelsZip.exists()) {
                            FileUtil.deleteFile(modelsZip);
                        }

                        if (initStage) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //销毁进度对话框
                                    if (processDialog!=null &&processDialog.isShowing()) {
                                        processDialog.dismiss();
                                    }
                                    mBt_startdect.setEnabled(true);
                                }
                            });
                        } else {
                            //删除解压后模型文件
                            File modelsDir = new File(dataDirPath + File.separator + MODULES);
                            if (modelsDir.exists()) {
                                FileUtil.deleteFile(modelsDir);
                            }
                            //初始化失败
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //销毁进度对话框
                                    if (processDialog!=null &&processDialog.isShowing()) {
                                        processDialog.dismiss();
                                    }
                                    //提示初始化失败
                                    Toast.makeText(LiveStartActivity.this, LiveStartActivity.this.getString(R.string.cloudwalk_copy_modules_failed), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }

                    }

                }
            });
        } else {
            CloudwalkSDK.getInstance().setModulePath(getFilesDir().getAbsolutePath() + File.separator + MODULES);
        }
    }

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
}