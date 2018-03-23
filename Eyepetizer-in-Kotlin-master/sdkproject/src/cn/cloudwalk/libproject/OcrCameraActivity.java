package cn.cloudwalk.libproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import cn.cloudwalk.IDCardSDK;
import cn.cloudwalk.callback.IDCardImgCallback;
import cn.cloudwalk.jni.IDCardImg;
import cn.cloudwalk.libproject.camera.AutoFocusCameraPreview;
import cn.cloudwalk.libproject.camera.Delegate;
import cn.cloudwalk.libproject.util.ImgUtil;
import cn.cloudwalk.libproject.util.Util;
import cn.cloudwalk.libproject.view.OcrMaskView;

public class OcrCameraActivity extends Activity implements Delegate, IDCardImgCallback {


    public static final int OCR_FRONT = 1, OCR_BACK = 2, TAKEPHOTO = 3, DETECT_IDCARD = 4;
    public static String FILEPATH_KEY = "filepath_key";
    private static final int CANCEL_FOCUS = 0, DRAW_LINE=1, DELAY_RESULT=2;
    int ocr_flag;

    public static final int COLORTYPE_BGR = 1, COLORTYPE_BGRA = 2, COLORTYPE_YUV = 3, COLORTYPE_NV21 = 4, COLORTYPE_NV12 = 5;
    AutoFocusCameraPreview mAutoFoucsCameraPreview;
    OcrMaskView maskView;
    ImageView mIv_idrect;

    int initRet = -1;
    IDCardSDK iDCardSDK = null;
    IDCardImg idCardImg;
    Bitmap bmpCanLine;//扫描线
    Bitmap bmpfocus;//聚焦图像
    Bitmap bmpfocused;//聚焦完成图像
    final String OutJpgName = "takephoto.jpg";//保存的图片名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
				.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.cloudwalk_activity_rect_ocr);

        ocr_flag = getIntent().getIntExtra(Contants.OCR_FLAG, -1);
        if (-1==ocr_flag) {
            Toast.makeText(this,"params error",Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        bmpCanLine = BitmapFactory.decodeResource(getResources(), R.drawable.scan_line);
        bmpfocus = BitmapFactory.decodeResource(getResources(), R.drawable.focus);
        bmpfocused = BitmapFactory.decodeResource(getResources(), R.drawable.focused);
        initView();
        initSDK();
        initCallback();

        mAutoFoucsCameraPreview.setFlag(ocr_flag);
        mAutoFoucsCameraPreview.setSizeCallback(new AutoFocusCameraPreview.SizeCallback() {
            @Override
            public void onSizeChange(int width, int height, final int ocrRectW, final int ocrRectH) {
                                maskView.setOcr(width, height, ocrRectW, ocrRectH, ocr_flag, bmpCanLine, bmpfocus, bmpfocused);
                

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ocrRectW, ocrRectH);
//                        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//                        mIv_idrect.setLayoutParams(params);
//                        if (ocr_flag == OCR_FRONT) {
//                            mIv_idrect.setBackgroundResource(R.drawable.zhengmian1);
//                        } else {
//                            mIv_idrect.setBackgroundResource(R.drawable.beimian1);
//                        }
//
//                    }
//                });
            }
        });
        deleteCachedJpg();
    }

    /**
     * 初始化相关参数
     */
    private void initView() {
        mAutoFoucsCameraPreview = (AutoFocusCameraPreview) findViewById(R.id.preview);
        maskView = (OcrMaskView) findViewById(R.id.ocrMaskView);
        mIv_idrect = (ImageView) findViewById(R.id.iv_idrect);
    }

    /**
     * 注册接口
     */
    private void initCallback() {
        iDCardSDK.cwIDCardImgCallback(this);
        mAutoFoucsCameraPreview.setDelegate(this);
    }
    /**
     * 初始化SDK
     */
    private void initSDK() {
        iDCardSDK = IDCardSDK.getInstance(this);
        initRet = iDCardSDK.cwCreateIdCardRecog(Bulider.licence);
        if (initRet != 0) {
            new AlertDialog.Builder(this).setMessage(R.string.facedectfail_appid)
                    .setNegativeButton("确定", new AlertDialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                            finish();
                        }
                    }).show();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAutoFoucsCameraPreview.cwStartCamera();
    }

    @Override
    protected void onStop() {
        mAutoFoucsCameraPreview.cwStopCamera();
        mHandler.removeCallbacksAndMessages(null);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (null!=iDCardSDK)
            iDCardSDK.cwDestroyIdCardRecog();
        if (bmpCanLine!=null && !bmpCanLine.isRecycled()){
            bmpCanLine.recycle();
        }
        if (bmpfocus!=null && !bmpfocus.isRecycled()){
            bmpfocus.recycle();
        }
        if (bmpfocused!=null && !bmpfocused.isRecycled()){
            bmpfocused.recycle();
        }
        super.onDestroy();
    }

    @Override
    public void onOpenCameraError() {
        // 打开相机出错
    }

    public void doRecog(IDCardImg idCardImg) {
        final String filepath = Util.getDiskCacheDir(OcrCameraActivity.this) + "/" + OutJpgName;
        Bitmap bitmap = ImgUtil.byteArrayBGRToBitmap
                (idCardImg.ImgData, idCardImg
                        .detect_width, idCardImg.detect_height);
        ImgUtil.saveJPGE_After(bitmap, filepath, 95);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = OcrCameraActivity.this.getIntent();
                intent.putExtra(FILEPATH_KEY, filepath);
                setResult(Activity.RESULT_OK, intent);// 返回页面1

                OcrCameraActivity.this.finish();
            }
        });
    }

    @Override
    public void onFocus(float x, float y) {
        // 显示焦点
        maskView.setFocus(x, y);
    }

    @Override
    public void onFocused() {
        // 变换焦点
        maskView.setFocused();
        mHandler.sendEmptyMessageDelayed(CANCEL_FOCUS, 150);
    }

    @Override
    public void IDCardImg(IDCardImg idCardImg) {
        mHandler.obtainMessage(DRAW_LINE,new Rect(idCardImg.left, idCardImg.top, idCardImg.right, idCardImg.bottom)).sendToTarget();
        this.idCardImg = idCardImg;
    }

    @Override
    public void IDCardDetectOk(IDCardImg idCardImg) {
        mHandler.obtainMessage(DRAW_LINE,new Rect(idCardImg.left, idCardImg.top, idCardImg.right, idCardImg.bottom)).sendToTarget();
        this.idCardImg = idCardImg;
        doRecog(idCardImg);
    }

    public void cwDrawLine(int left, int top, int right, int bottom){
        maskView.setLine(left, top, right, bottom);
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CANCEL_FOCUS) {
                maskView.clearFocus();
            } else if (msg.what == DRAW_LINE){
                Rect rect = (Rect) msg.obj;
                cwDrawLine(rect.left,rect.top,rect.right,rect.bottom);
            } else if (msg.what == DELAY_RESULT){

            }
            super.handleMessage(msg);
        }
    };

    protected void deleteCachedJpg() {
        try {
            String path = Util.getDiskCacheDir(this) + "/" + OutJpgName;
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
