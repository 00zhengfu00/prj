package com.iask.yiyuanlegou1.home.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.account.LoginActivity;
import com.iask.yiyuanlegou1.base.BaseFragment;
import com.iask.yiyuanlegou1.common.Constant;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.home.main.product.ProductClassifyActivity;
import com.iask.yiyuanlegou1.home.main.product.ProductDetailActivity;
import com.iask.yiyuanlegou1.home.main.product.pay.RechargeActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.account.UserInfo;
import com.iask.yiyuanlegou1.network.respose.home.AdvertisementBean;
import com.iask.yiyuanlegou1.network.respose.home.AdvertisementTxtBean;
import com.iask.yiyuanlegou1.network.respose.home.HomePageProductBean;
import com.iask.yiyuanlegou1.network.respose.home.HomePageProductResponse;
import com.iask.yiyuanlegou1.network.service.product.HomePageProduceService;
import com.iask.yiyuanlegou1.widget.HorizontalListView;
import com.iask.yiyuanlegou1.widget.ImageCycleView;
import com.iask.yiyuanlegou1.widget.TitleBarView;
import com.iask.yiyuanlegou1.widget.circleview.ADInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class BuyFragment extends BaseFragment {
    private TitleBarView title;
    private PtrClassicFrameLayout mPtrFrame;
    private ListView listView;
    private HorizontalListView productRecommendListView;
    private View footer;
    private ProductAdapter adapter;
    private RecommendProductAdapter recommendAdapter;
    private List<HomePageProductBean> productBeans = new ArrayList<HomePageProductBean>();
    private List<HomePageProductBean> productRecommendBeas = new ArrayList<HomePageProductBean>();
    private ArrayList<String> mImageUrl = new ArrayList<String>();
    private ImageCycleView cycleViewPager;
    private List<AdvertisementBean> bannerList = new ArrayList<AdvertisementBean>();
    private List<AdvertisementTxtBean> userList = new ArrayList<AdvertisementTxtBean>();
    private List<HomePageProductBean> shopList = new ArrayList<HomePageProductBean>();
    private ViewFlipper adsFlipper;
    public final int REQUEST_CODE_PRODUCT = 0X10;
    private int type = 1;
    /**
     * 记录第一行Item的数值
     */
    private int firstVisibleItemR;

    private String[] imageUrls = {"http://img.taodiantong" +
            ".cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
            "http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
            "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView() {
        title = (TitleBarView) rootView.findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setTitleText(R.string.main);
        initPtrFrame();
        initListView();

        configImageLoader();
    }

    private void getData(int type, boolean showDialog) {
        HomePageProduceService service = new HomePageProduceService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<HomePageProductResponse>() {
            @Override
            public void onGetData(HomePageProductResponse data) {
                try {
                    bannerList.clear();
                    bannerList.addAll(data.data.bannerList);
                    userList.clear();
                    userList.addAll(data.data.userList);
                    shopList.clear();
                    shopList.addAll(data.data.shopList);
                    productRecommendBeas.clear();
                    if (data.data.virtualList != null) {
                        productRecommendBeas.addAll(data.data.virtualList);
                    }
                    mPtrFrame.refreshComplete();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshUI();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        service.post("type=" + type, showDialog);
    }

    private void refreshUI() {
        initialize();
        if (userList != null && userList.size() > 0) {
            adsFlipper.removeAllViews();
            for (AdvertisementTxtBean bean : userList) {
                View item = View.inflate(getActivity(), R.layout.layout_ads_flipper_item, null);
                TextView tvNick = (TextView) item.findViewById(R.id.tv_username);
                TextView tvProduct = (TextView) item.findViewById(R.id.tv_product);
                tvNick.setText(bean.getUsername());
                tvProduct.setText("获得" + bean.getTitle());
                adsFlipper.addView(item);
            }
            adsFlipper.setFlipInterval(3000);
            adsFlipper.setAutoStart(true);
            adsFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_in));
            adsFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim
                    .flipper_push_out));
            adsFlipper.startFlipping();
        }
        if (shopList != null && shopList.size() > 0) {
            productBeans.clear();
            productBeans.addAll(shopList);
            adapter.notifyDataSetChanged();
        }
        if (productRecommendBeas != null && productRecommendBeas.size() > 0) {
            productRecommendListView.setVisibility(View.VISIBLE);
            recommendAdapter.notifyDataSetChanged();
        }
    }

    private void initPtrFrame() {
        mPtrFrame = (PtrClassicFrameLayout) rootView.findViewById(R.id.refresh_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                // mPtrFrame.autoRefresh();
            }
        }, 100);
    }

    private void setupType(View view) {
        final TextView tv1, tv2, tv3, tv4;
        tv1 = (TextView) view.findViewById(R.id.hotCate);
        tv2 = (TextView) view.findViewById(R.id.newCate);
        tv3 = (TextView) view.findViewById(R.id.priceCate);
        tv4 = (TextView) view.findViewById(R.id.req_man_text);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                getData(1, true);
                tv1.setTextColor(getResources().getColor(R.color.button_red));
                tv2.setTextColor(getResources().getColor(R.color.text_default_color));
                tv3.setTextColor(getResources().getColor(R.color.text_default_color));
                tv4.setTextColor(getResources().getColor(R.color.text_default_color));
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                getData(0, true);
                tv1.setTextColor(getResources().getColor(R.color.text_default_color));
                tv2.setTextColor(getResources().getColor(R.color.button_red));
                tv3.setTextColor(getResources().getColor(R.color.text_default_color));
                tv4.setTextColor(getResources().getColor(R.color.text_default_color));
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 3;
                getData(3, true);
                tv1.setTextColor(getResources().getColor(R.color.text_default_color));
                tv2.setTextColor(getResources().getColor(R.color.text_default_color));
                tv3.setTextColor(getResources().getColor(R.color.button_red));
                tv4.setTextColor(getResources().getColor(R.color.text_default_color));
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                getData(2, true);
                tv1.setTextColor(getResources().getColor(R.color.text_default_color));
                tv2.setTextColor(getResources().getColor(R.color.text_default_color));
                tv3.setTextColor(getResources().getColor(R.color.text_default_color));
                tv4.setTextColor(getResources().getColor(R.color.button_red));
            }
        });
    }

    private void setupHeader(View header) {
        setupType(header);
        header.findViewById(R.id.btn_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ProductClassifyActivity.class);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.btn_QA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebviewActivity.class);
                intent.putExtra("url", Constant.FRESHER_URL);
                intent.putExtra("title", R.string.common_question);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.btn_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebviewActivity.class);
                intent.putExtra("url", Constant.CONTACT_URL);
                intent.putExtra("title", R.string.contact_service);
                startActivity(intent);
            }
        });
        header.findViewById(R.id.btn_recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = CacheManager.getObject(CacheManager.TYPE_USER_INFO,
                        CacheKeys.USERINFO_LOGINVO, UserInfo.class);
                if (obj == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), RechargeActivity.class));
                }
            }
        });
        adsFlipper = (ViewFlipper) header.findViewById(R.id.win_notify);
    }

    /**
     * listView 相关设置
     */
    @SuppressLint("NewApi")
    private void initListView() {
        listView = (ListView) rootView.findViewById(R.id.listView);
        View header = View.inflate(getActivity(), R.layout.fragment_home_header, null);
        setupHeader(header);
        productRecommendListView = (HorizontalListView) header.findViewById(R.id.product_recommend);
        listView.addHeaderView(header);
        footer = View.inflate(getActivity(), R.layout.footer_view, null);
        adapter = new ProductAdapter(productBeans, getActivity());
        recommendAdapter = new RecommendProductAdapter(productRecommendBeas, getActivity());
        listView.setAdapter(adapter);
        productRecommendListView.setAdapter(recommendAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当滑动到底部时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && firstVisibleItemR != 0) {
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                firstVisibleItemR = firstVisibleItem;
                if (footer != null) {
                    //判断可视Item是否能在当前页面完全显示
                    if (visibleItemCount == totalItemCount) {
                        // removeFooterView(footerView);
                        footer.setVisibility(View.GONE);//隐藏底部布局
                    } else {
                        // addFooterView(footerView);
                        footer.setVisibility(View.VISIBLE);//显示底部布局
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", (int) id);
                intent.setClass(getActivity(), ProductDetailActivity.class);
                startActivity(intent);
            }
        });
        productRecommendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", (int) id);
                intent.setClass(getActivity(), ProductDetailActivity.class);
                startActivity(intent);
            }
        });
        cycleViewPager = (ImageCycleView) header.findViewById(R.id
                .fragment_cycle_viewpager_content);
    }

    private void updateData() {
        getData(type, false);
    }

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.mipmap
                .icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                        // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()
                .getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder
                        (QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    private void initialize() {
        if (bannerList == null && bannerList.size() <= 0) {
            return;
        }
        mImageUrl.clear();
        for (int i = 0; i < bannerList.size(); i++) {
            mImageUrl.add(bannerList.get(i).getImg());
        }
        cycleViewPager.setImageResources(mImageUrl, mAdCycleViewListener);
    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView
            .ImageCycleViewListener() {

        @Override
        public void onImageClick(int position, View imageView) {
            // TODO 单击图片处理事件
            AdvertisementBean bean = bannerList.get(position);
            if (bean.getUrl() != 0) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ProductDetailActivity.class);
                intent.putExtra("id", bean.getUrl());
                startActivity(intent);
            }
        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);//
            // 此处本人使用了ImageLoader对图片进行加装！
        }
    };

    private void setFooterStatus(boolean finished) {
        TextView tvNoMore = (TextView) footer.findViewById(R.id.tv_loading);
        ProgressBar bar = (ProgressBar) footer.findViewById(R.id.progress_loading);
        if (finished) {
            tvNoMore.setText("没有更多了");
            bar.setVisibility(View.GONE);
        } else {
            tvNoMore.setText("正在加载……");
            bar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(type, false);
        cycleViewPager.startImageCycle();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (adsFlipper != null) {
            adsFlipper.stopFlipping();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cycleViewPager.pushImageCycle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cycleViewPager.pushImageCycle();
    }
}
