package cn.cloudwalk.libproject.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import cn.cloudwalk.BankOcrInterface;
import cn.cloudwalk.BankOcrSDK;
import cn.cloudwalk.IDCardSDK;
import cn.cloudwalk.TestLog;
import cn.cloudwalk.libproject.Contants;
import cn.cloudwalk.libproject.util.LogUtils;

/**
 * 自动聚焦</br>
 * 每隔5000ms调用mCamera.autoFocus
 *
 * @author yusr
 */
public class AutoFocusCameraPreview extends SurfaceView implements SurfaceHolder.Callback,
        Camera.PreviewCallback {

    private static final String TAG = LogUtils.makeLogTag("AutoFocusCameraPreview");
    private Camera mCamera;
    int caremaId = Camera.CameraInfo.CAMERA_FACING_BACK;
    public static final int COLORTYPE_BGR = 1, COLORTYPE_BGRA = 2, COLORTYPE_YUV = 3,
            COLORTYPE_NV21 = 4, COLORTYPE_NV12 = 5;
    private int left_topx;
    private int left_topy;
    private int right_bottomx;
    private int right_bottomy;
    private int width;
    private int height;
    private int ocrRectH;
    private int ocrRectW;
    private boolean mPreviewing = true;
    private boolean mSurfaceCreated = false;

    int mScreenW = 0;
    int mScreenH = 0;


    Context context;
    float touchX;
    float touchY;
    Paint paint = new Paint();
    private CameraConfigurationManager mCameraConfigurationManager;

    int mFlag = -1;//身份证正面1 身份证反面0 银行卡2
    public void setFlag(int flag) {
        mFlag = flag;
    }
    boolean mAutoRatio;//自动旋转

	/**
	*设置自动旋转标志位
	*/
    public void setAutoRatio(boolean mAutoRatio) {
        this.mAutoRatio = mAutoRatio;
    }

    private boolean mIsAutoMode = true;//自动对焦开关

    public AutoFocusCameraPreview(Context context) {
        super(context);
        this.context = context;
    }

    public AutoFocusCameraPreview(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        this.context = context;

    }

    public AutoFocusCameraPreview(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
        this.setKeepScreenOn(true);// 保持屏幕常亮
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            mCameraConfigurationManager = new CameraConfigurationManager(getContext());
            mCameraConfigurationManager.initFromCameraParameters(mCamera);

            getHolder().addCallback(this);
            if (mPreviewing) {
                requestLayout();
            } else {
                showCameraPreview();
            }
        }
    }

    public void setScreenSize(int screenW, int screenH) {
        this.mScreenW = screenW;
        this.mScreenH = screenH;
    }

    private void autoFocus() {

        try {
            if (mCamera != null)
                mCamera.autoFocus(autoFocusCB);
        } catch (Exception e) {

            // 聚焦失败
            e.printStackTrace();

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isTouch = true;
        touchX = event.getX();
        touchY = event.getY();
        mDelegate.onFocus(touchX, touchY);

        autoFocus();
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        stopCameraPreview();
        showCameraPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mSurfaceCreated = false;
        stopCameraPreview();
    }

    public void showCameraPreview() {
        if (mCamera != null) {
            try {
                mPreviewing = true;
                mCamera.setPreviewDisplay(getHolder());

                mCameraConfigurationManager.setDesiredCameraParameters(mCamera, caremaId, mIsAutoMode);
                mCamera.startPreview();
                mCamera.setPreviewCallback(this);
                //if (mAutoFocus) {
                //    mAutoFocus = false;
                if (mIsAutoMode) {
                    isFocusedOk = false;
                    postDelayed(doAutoFocus, 1000);//1500
                }
                //}
            } catch (Exception e) {
                LogUtils.LOGE(TAG, e.toString());
            }
        }
    }

    public void stopCameraPreview() {
        if (mCamera != null) {
            try {
                if (mIsAutoMode) {
                    isFocusedOk = false;
                    removeCallbacks(doAutoFocus);
                }
                //mAutoFocus = false;
                mPreviewing = false;
                mCamera.cancelAutoFocus();
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
            } catch (Exception e) {
                LogUtils.LOGE(TAG, e.toString());
            }
        }
    }

    public void openFlashlight() {
        if (flashLightAvaliable()) {
            mCameraConfigurationManager.openFlashlight(mCamera);
        }
    }

    public void closeFlashlight() {
        if (flashLightAvaliable()) {
            mCameraConfigurationManager.closeFlashlight(mCamera);
        }
    }

    private boolean flashLightAvaliable() {
        return mCamera != null && mPreviewing && mSurfaceCreated
                && getContext().getPackageManager().hasSystemFeature(PackageManager
                .FEATURE_CAMERA_FLASH);
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (mCamera != null && mPreviewing && mSurfaceCreated) {

                autoFocus();

            }
        }
    };
    protected boolean isTouch = false;
	private boolean isFocusedOk = false;
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            isFocusedOk = success;
            if (success) {
                //if (isTouch)
                    mDelegate.onFocused();
                isTouch = false;
                if (mIsAutoMode)
                    postDelayed(doAutoFocus, 4000);//1500
                LogUtils.LOGE("对焦","成功");
            } else {
                postDelayed(doAutoFocus, 2000);//1500
                LogUtils.LOGE("对焦","失败");
            }
        }
    };

    /******************************************************************/
    public Size getPreviewSize() {
        Camera.Parameters parameters = mCamera.getParameters();
        return parameters.getPreviewSize();
    }

    Delegate mDelegate;

    public void setDelegate(Delegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    /**
     * 打开摄像头开始预览，但是并未开始识别
     */
    public void cwStartCamera() {
        if (mCamera != null) {
            return;
        }

        try {
            mCamera = Camera.open(caremaId);
        } catch (Exception e) {
            if (mDelegate != null) {
                mDelegate.onOpenCameraError();
            }
        }
        setCamera(mCamera);
    }

    /**
     * 关闭摄像头预览，并且隐藏扫描框
     */
    public void cwStopCamera() {
        if (mCamera != null) {
            stopCameraPreview();

            mCamera.release();
            mCamera = null;
        }
    }

    public interface SizeCallback {
        public void onSizeChange(int width, int height, int ocrRectH, int ocrRectW);
    }
    public void setSizeCallback(SizeCallback cb) {
        _sizeCb = cb;
    }
    SizeCallback _sizeCb = null;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float ratio = 1.58f;
        if (mFlag == Contants.OCR_FLAG_BANKCARD) {

        } else {

        }

        this.width = w;
        this.height = h;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ocrRectW = (int) (width * 70 / 100);//area in parent screen
            ocrRectH = (int) (ocrRectW / ratio);//card ratio 0.63 1.58 //1.45(1.35-1.60)
        } else {
            if (Contants.OCR_FLAG_BANKCARD==mFlag && mAutoRatio) {//银行卡
                //竖屏的模板框，宽<高
                ocrRectH = (int) (height * 70 / 100);//area in parent screen
                ocrRectW = (int) (ocrRectH / ratio);//card ratio 0.63 1.58 //1.45(1.35-1.60)

            } else {//身份证
                ocrRectW = (int) (width * 90 / 100);//area in parent screen
                ocrRectH = (int) (ocrRectW / ratio);//card ratio 0.63 1.58 //1.45(1.35-1.60)
            }
//            ocrRectW = (int) (width * 90 / 100);//area in parent screen
//            ocrRectH = (int) (ocrRectW / ratio);//card ratio 0.63 1.58 //1.45(1.35-1.60)
        }

        if (null!=_sizeCb) {
            _sizeCb.onSizeChange(width,height,ocrRectW,ocrRectH);
        }
    }

    protected void doSizeCalc(int nPictureW, int nPictureH) {
        float ratioW = 1.0f * nPictureW / this.width;
        float ratioH = 1.0f * nPictureH / this.height;

        int det = 0;
        int l = (width - ocrRectW) / 2 - det;
        int t = (height - ocrRectH) / 2 - det;
        int r = l + ocrRectW + det;
        int b = t + ocrRectH + det;

        //转换后的参数坐标
        left_topx = (int)(l * ratioW);
        left_topy = (int)(t * ratioH);
        right_bottomx = (int)(r * ratioW);
        right_bottomy = (int)(b * ratioH);

        TestLog.LogE("外框尺寸",width + "x" + height);
        TestLog.LogE("内框尺寸",ocrRectW + "x" + ocrRectH);
        TestLog.LogE("坐标",String.format("内框原始 %d,%d,%d,%d 转换后 %d,%d,%d,%d",l,t,r,b,left_topx,left_topy,right_bottomx,right_bottomy));
    }

    int mPerSecFrames = 0;//帧率
    int mCounter = 0;
    long mTimmer = 0;


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (0 == mCounter++) {
            mTimmer = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - mTimmer >= 60 * 1000) {
            Log.w("帧率/s", "" + (mPerSecFrames = mCounter / 60));
            mTimmer = System.currentTimeMillis();
            mCounter = 0;
        }

        Camera.Size size = getPreviewSize();
        int preWidth = size.width;
        int preHeight = size.height;

        //检测扫描
        if (isFocusedOk) {
            int w = preWidth;
            int h = preHeight;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            } else {
                if (w > h) {
                    int tmp = w;
                    w = h;
                    h = tmp;
                }
				//旋转耗时,将会导致主线程绘制扫描线卡顿,将旋转的业务逻辑移动至检测线程
                //data = rotateNV21Degree90(data, preWidth, preHeight);
            }
            doSizeCalc(w, h);

            /*CardType
            ID_CARD_FRONT	= 0
	        ID_CARD_BACK	= 1
	        BANK_CARD_FRONT	= 2
             */
            if (Contants.OCR_FLAG_BANKCARD == mFlag)
                BankOcrSDK.getInstance(context/*, mFlag*/).cwPushFrame(data, w, h
                        , BankOcrInterface.CW_BankOcrFormat.CW_COLORTYPE_NV21, left_topx, left_topy, right_bottomx, right_bottomy,getResources().getConfiguration().orientation);
            else if (Contants.OCR_FLAG_IDFRONT == mFlag || Contants.OCR_FLAG_IDBACK == mFlag) {
                IDCardSDK.getInstance(context).cwPushFrame(data, w, h
                        , BankOcrInterface.CW_BankOcrFormat.CW_COLORTYPE_NV21, mFlag, left_topx, left_topy, right_bottomx, right_bottomy);
                //                BankOcrSDK.getInstance(context, mFlag).cwPushFrame(data, w, h
                //                        , BankOcrInterface.CW_BankOcrFormat.CW_COLORTYPE_NV21, left_topx, left_topy, right_bottomx, right_bottomy, CloudwalkBankCardOCRActivity.OCR_IDFRONT==mFlag ? 0 : 1);
            }
        }
    }

    public static byte[] rotateNV21Degree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }

}