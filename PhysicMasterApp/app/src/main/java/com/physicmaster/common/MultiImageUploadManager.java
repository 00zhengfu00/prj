/* 
 * 系统名称：lswuyou
 * 类  名  称：MultiImageUploadManager.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-12-18 上午10:32:28
 * 功能说明： 阿里云多图上传管理控制类-支持失败重传
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.netease.nim.uikit.common.util.storage.StorageType;
import com.netease.nim.uikit.common.util.storage.StorageUtil;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.net.response.account.GetOssInfoResponse.DataBean.OssConfigBean;
import com.physicmaster.net.response.account.GetOssInfoResponse.DataBean.OssTokenBean;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.utils.BitmapUtils;
import com.physicmaster.utils.OssUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MultiImageUploadManager {
    /**
     * 需要上传的图片实例
     */
    private List<UploadInstance> instances;
    private Activity mContext;
    /**
     * 尚未上传成功的图片数目
     */
    private volatile int unUploadLeftCount;
    /**
     * 上传失败的图片数目
     */
    private volatile int uploadFailCount = 0;
    /**
     * 上传图片全部成功回调接口
     */
    private UploadResultListener listener;
    private Thread uploadThread;
    private String bucketName;
    private String objectKey;
    private String serverPath;
    private Logger logger = AndroidLogger.getLogger(TAG);
    private static final String TAG = "MultiImageUploadManager";
    private static int MAX_WIDTH = 1080;
    private static int IMG_QUALITY = 80;

    public void setListener(UploadResultListener listener) {
        this.listener = listener;
    }

    public MultiImageUploadManager(List<UploadInstance> instances, Activity mContext) {
        this.mContext = mContext;
        this.instances = instances;
        if (BaseApplication.getStartupDataBean() == null) {
            StartupResponse.DataBean startupData = (StartupResponse.DataBean) CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys.STARTUP_INFO, StartupResponse
                    .DataBean.class);
            BaseApplication.setStartupDataBean(startupData);
        }
        if (BaseApplication.getStartupDataBean().maxImgWidth > 0) {
            MAX_WIDTH = BaseApplication.getStartupDataBean().maxImgWidth;
        }
        if (BaseApplication.getStartupDataBean().imgQuality > 0) {
            IMG_QUALITY = BaseApplication.getStartupDataBean().imgQuality;
        }
        unUploadLeftCount = instances.size();
    }

    public void init(OssTokenBean ossTokenBean, OssConfigBean ossConfigBean) {
        this.bucketName = ossConfigBean.bucketName;
        this.objectKey = ossConfigBean.imgPath;
        this.serverPath = ossConfigBean.hostId;
        OssUtils.initOss(mContext, ossTokenBean.tempAk, ossTokenBean.tempSk, serverPath, ossTokenBean.securityToken);
    }

    /**
     * 开始上传
     */
    public void startUpload() {
        uploadAnswerImage();
    }

    /**
     * 取消上传
     */
    public void cancelUpload() {
        uploadThread.interrupt();
    }

    /**
     * 开始上传答案图片
     */
    private void uploadAnswerImage() {
        uploadFailCount = 0;
        int count = instances.size();
        for (int i = 0; i < count; i++) {
            boolean uploadSucc = instances.get(i).uploadSucc;
            if (uploadSucc) {
                /** 如果已经上传成功，则跳过 */
                continue;
            }
            uploadImage(instances.get(i));
        }
    }

    /**
     * 上传单张图片
     */
    public void uploadImage(final UploadInstance instance) {
        Bitmap bitmapOrin = BitmapUtils.getBitmap(instance.path);
        int width = bitmapOrin.getWidth();
        int height = bitmapOrin.getHeight();
        if (width > MAX_WIDTH) {
            height = (int) (height * ((float) MAX_WIDTH / width));
            width = MAX_WIDTH;
        }
        Bitmap targetBitmap = ThumbnailUtils.extractThumbnail(bitmapOrin, width, height);
        String targetName = System.currentTimeMillis() + ".jpg";
        File compressedFile = new File(StorageUtil.getDirectoryByDirType(StorageType.TYPE_IMAGE), targetName);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(compressedFile);
            targetBitmap.compress(Bitmap.CompressFormat.JPEG, IMG_QUALITY, outputStream);
            instance.width = targetBitmap.getWidth();
            instance.height = targetBitmap.getHeight();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        instance.path = compressedFile.getPath();
//        logger.debug("target file size:" + new File(instance.path).length() / 1024 + "kb");
        String path = instance.path;
//        logger.debug("compressed bitmap width:" + targetBitmap.getWidth());
//        logger.debug("compressed bitmap height:" + targetBitmap.getHeight());
        final String name = System.currentTimeMillis() + ".jpg";
        OssUtils.uploadFile(bucketName, objectKey + name, path, new OssUtils.OnUploadListener() {
            @Override
            public void onUploadSucc(String path) {
                instance.url = objectKey + name;
                instance.uploadSucc = true;
                unUploadLeftCount--;
                logger.debug("剩余需要上传的图片数：" + unUploadLeftCount);
                if (0 == unUploadLeftCount) {
                    /** 图片上传完毕 */
//                    setAnswerUrls();
                    List<ImageInfo> urls = new ArrayList<ImageInfo>();
                    for (UploadInstance instance : instances) {
                        ImageInfo imageInfo = new ImageInfo();
                        imageInfo.u = instance.url;
                        imageInfo.h = instance.height;
                        imageInfo.w = instance.width;
                        urls.add(imageInfo);
                    }
                    if (null != listener) {
                        listener.onUploadSuccess(urls);
                    }
                    return;
                }
                if (unUploadLeftCount == uploadFailCount) {
                    /** 图片全部提交，但是存在失败情况，反馈给用户 */
                    if (null != listener) {
                        listener.onUploadFail();
                    }
                }
            }

            @Override
            public void onUploadFail(String msg) {
                uploadFailCount++;
                if (unUploadLeftCount == uploadFailCount) {
                    /** 图片全部提交，但是存在失败情况，反馈给用户 */
                    if (null != listener) {
                        listener.onUploadFail();
                    }
                }
            }

            @Override
            public void onUploadProgress(PutObjectRequest request, long currentSize, long
                    totalSize) {
            }
        });
    }

    /**
     * 重试上传
     */
    public void retry() {
        uploadAnswerImage();
    }

    /**
     * 上传全部成功回调接口
     */
    public interface UploadResultListener {
        void onUploadSuccess(List<ImageInfo> urls);

        int onUploadProgress();

        void onUploadFail();
    }

    /**
     * 图片信息
     */
    public static class UploadInstance {
        public String path;
        public String url;
        public int height;
        public int width;
        public boolean uploadSucc = false;
    }

    /**
     * 图片信息
     */
    public static class ImageInfo {
        public String u;
        public int h;
        public int w;
    }

    public static UploadInstance createUploadInstance() {
        return new UploadInstance();
    }
}
