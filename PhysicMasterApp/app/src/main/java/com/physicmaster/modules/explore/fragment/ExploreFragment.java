package com.physicmaster.modules.explore.fragment;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseFragment;
import com.physicmaster.modules.WebviewActivity;
import com.physicmaster.modules.explore.activity.Members2Activity;
import com.physicmaster.modules.explore.activity.MembersActivity;
import com.physicmaster.modules.explore.activity.NewVideoActivity;
import com.physicmaster.modules.explore.activity.ScannerAvtivity;
import com.physicmaster.modules.explore.activity.VideoPlayActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.explore.GetExploreResponse;
import com.physicmaster.net.service.explore.GetExploreService;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ImageCycleView;
import com.umeng.analytics.MobclickAgent;
import com.physicmaster.net.response.explore.GetExploreResponse.DataBean.SuperMemberBean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by songrui on 17/7/5.
 */

public class ExploreFragment extends BaseFragment implements View.OnClickListener {

    private ImageView ivContent4;
    private ImageView ivContent3;
    private ImageView ivContent2;
    private ImageView ivContent1;
    private TextView tvMore3;
    private TextView tvMore2;
    private TextView tvMore1;
    private TextView tvTitle;
    private ImageView ivPlay;
    private TextView tvSaoma;
    private FragmentActivity mContext;
    private ArrayList<String> mImageUrl = new ArrayList<>();
    private List<GetExploreResponse.DataBean.BannerBean> bannerList;
    private List<GetExploreResponse.DataBean.NewVideoListBean> newVideoList;
    private File file;
    private JCVideoPlayerStandard mVideoView;
    private ImageView iv;
    private  SuperMemberBean superMember;
    private BGABanner bgaBanner;
    /**
     * Android6.0需要申请权限
     */
    private final static String DANGEROUS_PERMISSION[] = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE};
    private final static int CAMERA_REQUEST_CODE = 1;
    private RelativeLayout rlContent1;
    private RelativeLayout rlContent2;
    private RelativeLayout rlContent3;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_explore;
    }

    @Override
    protected void initView() {

        mContext = getActivity();

        bgaBanner = rootView.findViewById(R.id.banner_main_cube);

        tvSaoma = (TextView) rootView.findViewById(R.id.tv_saoma);

        rlContent1 = (RelativeLayout) rootView.findViewById(R.id.rl_content1);
        rlContent2 = (RelativeLayout) rootView.findViewById(R.id.rl_content2);
        rlContent3 = (RelativeLayout) rootView.findViewById(R.id.rl_content3);

        //mVideoView = (JCVideoPlayerStandard) rootView.findViewById(R.id.videoView);
        ivPlay = (ImageView) rootView.findViewById(R.id.iv_play);
        iv = (ImageView) rootView.findViewById(R.id.iv);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);

        tvMore1 = (TextView) rootView.findViewById(R.id.tv_more1);
        tvMore2 = (TextView) rootView.findViewById(R.id.tv_more2);
        tvMore3 = (TextView) rootView.findViewById(R.id.tv_more3);

        ivContent1 = (ImageView) rootView.findViewById(R.id.iv_content1);
        ivContent2 = (ImageView) rootView.findViewById(R.id.iv_content2);
        int maxHeight = ScreenUtils.get16_9ImageMaxHeight(getActivity(), 20);
        ivContent1.setAdjustViewBounds(true);
        ivContent1.setMaxHeight(maxHeight);
        ivContent1.setScaleType(ImageView.ScaleType.FIT_XY);
        ivContent2.setAdjustViewBounds(true);
        ivContent2.setMaxHeight(maxHeight);
        ivContent2.setScaleType(ImageView.ScaleType.FIT_XY);

        ivContent3 = (ImageView) rootView.findViewById(R.id.iv_content3);
        ivContent4 = (ImageView) rootView.findViewById(R.id.iv_content4);

        tvSaoma.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        tvMore1.setOnClickListener(this);
        tvMore2.setOnClickListener(this);
        tvMore3.setOnClickListener(this);
        ivContent1.setOnClickListener(this);
        ivContent2.setOnClickListener(this);
        ivContent3.setOnClickListener(this);
        ivContent4.setOnClickListener(this);
        getData4Sever();
    }

    private void getData4Sever() {
        final GetExploreService service = new GetExploreService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<GetExploreResponse>() {
            @Override
            public void onGetData(GetExploreResponse data) {
                bannerList = data.data.banner;
                newVideoList = data.data.newVideoList;
                if (newVideoList == null || newVideoList.size() == 0) {
                    rlContent1.setVisibility(View.GONE);
                } else {
                    rlContent1.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(newVideoList.get(0).posterUrl)) {
                        Glide.with(mContext).load(newVideoList.get(0).posterUrl).placeholder(R.drawable.placeholder_gray).into(ivContent1);
                    }
                    tvTitle.setText(newVideoList.get(0).title + "");
                }
                mImageUrl.clear();
                if (bannerList != null) {
                    for (GetExploreResponse.DataBean.BannerBean bannerVo : bannerList) {
                        mImageUrl.add(bannerVo.imgUrl);
                    }
                }
                if (mImageUrl.size() == 0) {
                    bgaBanner.setVisibility(View.GONE);
                } else {
                    bgaBanner.setVisibility(View.VISIBLE);
                    bgaBanner.setAutoPlayAble(mImageUrl.size() > 1);
                    bgaBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                        @Override
                        public void fillBannerItem(BGABanner bgaBanner, ImageView itemView, String url, int i) {
                            itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                            Glide.with(itemView.getContext())
                                    .load(url)
                                    .placeholder(R.drawable.placeholder_gray)
                                    .error(R.drawable.placeholder_gray)
                                    .dontAnimate()
                                    .into(itemView);
                        }
                    });
                    bgaBanner.setDelegate(new BGABanner.Delegate() {
                        @Override
                        public void onBannerItemClick(BGABanner bgaBanner, View view, Object o, int i) {
                            GetExploreResponse.DataBean.BannerBean bean = bannerList.get(i);
                            if (!TextUtils.isEmpty(bean.pageUrl)) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), WebviewActivity.class);
                                intent.putExtra("url", bean.pageUrl);
                                startActivity(intent);
                            }
                        }
                    });
                    bgaBanner.setData(mImageUrl, null);
                }
                if (data.data.superMember != null) {
                    superMember = data.data.superMember;
                    Glide.with(getActivity()).load(superMember.poster).into(ivContent2);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("", false);
    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView
            .ImageCycleViewListener() {

        @Override
        public void onImageClick(int position, View imageView) {
            GetExploreResponse.DataBean.BannerBean bean = bannerList.get(position);
            if (!TextUtils.isEmpty(bean.pageUrl)) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebviewActivity.class);
                intent.putExtra("url", bean.pageUrl);
                startActivity(intent);
            }
        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            Glide.with(mContext).load(imageURL).placeholder(R.drawable.placeholder_gray)
                    .into(imageView);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_saoma:
                startActivity(new Intent(mContext, ScannerAvtivity.class));
                break;
            case R.id.tv_more1:
                if (newVideoList != null && newVideoList.size() > 0) {
                    Intent intent1 = new Intent(mContext, NewVideoActivity.class);
                    intent1.putExtra("newVideoList", (Serializable) newVideoList);
                    startActivity(intent1);
                } else {
                    UIUtils.showToast(getActivity(), "没有更多视频");
                }
                break;
            case R.id.iv_play:
            case R.id.tv_title:
            case R.id.iv_content1:
                if (newVideoList != null && newVideoList.size() > 0) {
                    Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                    intent.putExtra("videoId", newVideoList.get(0).videoId + "");
                    startActivity(intent);
                } else {
                    UIUtils.showToast(getActivity(), "暂无数据");
                }
                break;
            //会员专区
            case R.id.tv_more2:
                startActivity(new Intent(mContext, MembersActivity.class));
                break;
            case R.id.iv_content2:
                if (superMember != null) {
                    MobclickAgent.onEvent(getActivity(), "member_open_all_step1");
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), Members2Activity.class);
                    intent.putExtra("memberBanner", superMember);
                    startActivity(intent);
                }
                break;
            //好学商城
            case R.id.tv_more3:
            case R.id.iv_content3:
            case R.id.iv_content4:
                break;
        }
    }

}
