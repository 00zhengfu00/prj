package com.physicmaster.modules.mine.fragment.dialogFragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.SplashActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.user.GetMedalListResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.SharedSuccService;
import com.physicmaster.utils.BitmapGetter;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.RoundImageView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.io.FileOutputStream;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;


/**
 * Created by songrui on 16/11/7.
 */

public class MedalYesDialogFragment extends DialogFragment {


    private View mView;
    private FragmentActivity mContext;
    private ShareAction shareAction;
    private LocalBroadcastManager localBroadcastManager;
    private GetMedalListResponse.DataBean.LevelBean levelBean;
    private UserDataResponse.UserDataBean.LoginVoBean mDataBean;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes()
                .height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_fragment_medal_yes, null);
        mContext = getActivity();
        TextView tvDesc = (TextView) mView.findViewById(R.id.tv_desc);
        TextView tvName = (TextView) mView.findViewById(R.id.tv_name);
        ImageView ivClose = (ImageView) mView.findViewById(R.id.iv_close);
        ImageView ivMedal = (ImageView) mView.findViewById(R.id.iv_medal);
        RelativeLayout rlWeiwxin = (RelativeLayout) mView.findViewById(R.id.rl_weiwxin);
        RelativeLayout rlFriend = (RelativeLayout) mView.findViewById(R.id.rl_friend);
        RelativeLayout rlQzone = (RelativeLayout) mView.findViewById(R.id.rl_qzone);
        RoundImageView ivUsers = (RoundImageView) mView.findViewById(R.id.iv_users);

        Bundle bundle = getArguments();
        levelBean = (GetMedalListResponse.DataBean
                .LevelBean) bundle.getSerializable("levelBean");
        mDataBean = BaseApplication.getUserData();
        if (mDataBean != null) {
            Glide.with(this).load(mDataBean.portrait).placeholder(R.drawable.placeholder_gray)
                    .into(ivUsers);
        }
        if (!TextUtils.isEmpty(levelBean.medalImg)) {
            Glide.with(this).load(levelBean.medalImg).into(ivMedal);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedalYesDialogFragment.this.dismiss();
            }
        });
        TextPaint tp = tvName.getPaint();
        tp.setFakeBoldText(true);
        tvDesc.setText(levelBean.medalDesc + "");
        tvName.setText(levelBean.medalName + "");
        rlWeiwxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curShareMedia = SHARE_MEDIA.WEIXIN;
                verifyWriteStoragePermissions(SHARE_MEDIA.WEIXIN);
            }
        });
        rlFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curShareMedia = SHARE_MEDIA.WEIXIN_CIRCLE;
                verifyWriteStoragePermissions(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
        rlQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curShareMedia = SHARE_MEDIA.QZONE;
                verifyWriteStoragePermissions(SHARE_MEDIA.QZONE);
            }
        });

        return mView;
    }

    private File getShareImage(View view) {
        File file = null;
        Bitmap bitmapQR = null;
        StartupResponse.DataBean dataBean = BaseApplication.getStartupDataBean();
        if (dataBean == null || TextUtils.isEmpty(dataBean.shareLinkImg)) {
            startActivity(new Intent(getActivity(), SplashActivity.class));
            getActivity().finish();
            UIUtils.showToast(getActivity(), "数据异常");
            return null;
        }
        String text = "大师家族 APP";
        String packageName = getActivity().getPackageName();
        if (packageName.equals(Constant.PHYSICMASTER)) {
            text = "[物理大师] APP";
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            text = "[化学大师] APP";
        } else if (packageName.equals(Constant.MATHMASTER)) {
            text = "[数学大师] APP";
        }
        int bitmapBgWidth = view.getWidth();
        int qrCodeWidth = bitmapBgWidth / 4;
        Bitmap bitmapBg = Bitmap.createBitmap(bitmapBgWidth, qrCodeWidth + ScreenUtils.dp2px
                (getActivity(), 40), Bitmap.Config.ARGB_8888);
        bitmapBg.eraseColor(Color.parseColor("#fff4f4f4"));
        int color = Color.parseColor("#ffa19483");
        bitmapQR = QRCodeEncoder.syncEncodeQRCode(dataBean.medalQrcode, qrCodeWidth, color);
        Canvas canvas = new Canvas(bitmapBg);
        Paint paint = new Paint();
        //画二维码
        canvas.drawBitmap(bitmapQR, ScreenUtils.dp2px(getActivity(), 20), ScreenUtils.dp2px
                (getActivity(), 20), paint);
        //画app名称
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setTextSize(ScreenUtils.dp2px(getActivity(), 20));
        canvas.drawText(text, qrCodeWidth + ScreenUtils.dp2px(getActivity(), 40), ScreenUtils
                .dp2px(getActivity(), 50), paint);
        //画描述文字
        paint.setAntiAlias(true);
        paint.setTextSize(ScreenUtils.dp2px(getActivity(), 16));
        canvas.drawText("长按识别二维码", qrCodeWidth + ScreenUtils.dp2px(getActivity(), 40), ScreenUtils
                .dp2px(getActivity(), 75), paint);
        Bitmap bitmapRoot = BitmapGetter.loadBitmapFromView(view);
        Bitmap bitmap = BitmapGetter.mergeBitmap_TB(bitmapRoot, bitmapBg, false);
        FileOutputStream fos;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                File sdRoot = Environment.getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_PICTURES);
                file = new File(sdRoot, "masterMedal.PNG");
                fos = new FileOutputStream(file);
            } else {
                throw new Exception("创建文件失败!");
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.destroyDrawingCache();
        return file;
    }

    /**
     * 分享
     */
    private void sharedToMedia(SHARE_MEDIA media) {
        if (null == media) {
            return;
        }
        View rootView = mView.findViewById(R.id.rl_share);
        UMImage image;
        File file = getShareImage(rootView);
        if (null == file) {
            return;
        }
        image = new UMImage(mContext, file);
        shareAction = new ShareAction(mContext);
        shareAction.setPlatform(media).setCallback(new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat", "platform" + platform);
                UIUtils.showToast(mContext, "分享成功啦");
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                UIUtils.showToast(mContext, "分享失败啦");
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                UIUtils.showToast(mContext, "分享取消了");
            }
        }).withMedia(image);
        shareAction.share();
    }

    /**
     * 分享成功调用接口
     */
    private void sharedSuccess(int type) {
        final SharedSuccService service = new SharedSuccService(mContext);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);
        service.setCallback(new IOpenApiDataServiceCallback() {
            @Override
            public void onGetData(Object data) {
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("shareType=" + type, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shareAction != null) {
            shareAction.close();
        }
    }

    public void verifyWriteStoragePermissions(SHARE_MEDIA media) {
        // Check if we have write permission
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE,
                        REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                sharedToMedia(media);
            }
        } else {
            sharedToMedia(media);
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予成功,初始化
                sharedToMedia(curShareMedia);
            } else {
                UIUtils.showToast(getActivity(), "您拒绝了存储权限，无法使用分享");
            }
        } else if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予成功,初始化
                sharedToMedia(curShareMedia);
            } else {
                UIUtils.showToast(getActivity(), "您拒绝了存储权限，无法使用分享");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //动态获取文件读取权限
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SHARE_MEDIA curShareMedia;
}
