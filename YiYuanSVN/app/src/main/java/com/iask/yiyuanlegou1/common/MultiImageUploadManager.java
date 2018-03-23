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
package com.iask.yiyuanlegou1.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.network.respose.account.AliyunServerBean;
import com.iask.yiyuanlegou1.utils.OssUtils;

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
//    private ProgressDialog progressDialog;
    public void setListener(UploadResultListener listener) {
        this.listener = listener;
    }

    public MultiImageUploadManager(List<UploadInstance> instances, Activity mContext) {
        this.mContext = mContext;
        this.instances = instances;
        unUploadLeftCount = instances.size();
    }

    public void init(String accessKeyId, String accessKeySecret, String securityToken) {
        Object object = CacheManager.getObject(CacheManager
                .TYPE_USER_INFO, CacheKeys.OSS_SERVER_INFO, AliyunServerBean.class);
        if (object == null) {
            Toast.makeText(mContext, "服务暂不可使用！", Toast.LENGTH_SHORT).show();
            return;
        }
        AliyunServerBean bean = (AliyunServerBean) object;
        this.bucketName = bean.getBucketName();
        this.objectKey = bean.getImgPath();
        this.serverPath = bean.getCdnName();
        OssUtils.initOss(mContext, accessKeyId, accessKeySecret, serverPath, securityToken);
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
        String path = instance.path;
        final long name = System.currentTimeMillis();
        OssUtils.uploadFile(bucketName, objectKey + name, path, new OssUtils.OnUploadListener() {
            @Override
            public void onUploadSucc(String path) {
                instance.url = serverPath + objectKey + name;
                instance.uploadSucc = true;
                unUploadLeftCount--;
                System.out.println("剩余需要上传的图片数：" + unUploadLeftCount);
                if (0 == unUploadLeftCount) {
                    /** 图片上传完毕 */
//                    setAnswerUrls();
                    List<String> urls = new ArrayList<String>();
                    for (UploadInstance instance : instances) {
                        urls.add(instance.url);
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
        public void onUploadSuccess(List<String> urls);

        public int onUploadProgress();

        public void onUploadFail();
    }

    /**
     * 图片信息
     */
    public static class UploadInstance {
        public String path;
        public String url;
        public boolean uploadSucc = false;
    }

    public static UploadInstance createUploadInstance() {
        return new UploadInstance();
    }
}
