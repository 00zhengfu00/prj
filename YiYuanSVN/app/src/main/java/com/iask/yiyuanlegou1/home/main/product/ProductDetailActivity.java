package com.iask.yiyuanlegou1.home.main.product;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.base.BaseFragmentActivity;
import com.iask.yiyuanlegou1.home.HomeActivity;
import com.iask.yiyuanlegou1.home.share.ShareActivity;
import com.iask.yiyuanlegou1.home.shopping.OrderListActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.product.CacheShoppingCarData;
import com.iask.yiyuanlegou1.network.respose.product.ProductBean;
import com.iask.yiyuanlegou1.network.respose.product.ProductDetailBean;
import com.iask.yiyuanlegou1.network.respose.product.ProductDetailResponse;
import com.iask.yiyuanlegou1.network.respose.product.ShoppingCartBean;
import com.iask.yiyuanlegou1.network.service.product.ProductDetailService;
import com.iask.yiyuanlegou1.utils.UIUtils;
import com.iask.yiyuanlegou1.utils.ViewFactory;
import com.iask.yiyuanlegou1.widget.EnvaluateDialog;
import com.iask.yiyuanlegou1.widget.TitleBarView;
import com.iask.yiyuanlegou1.widget.circleview.ADInfo;
import com.iask.yiyuanlegou1.widget.circleview.CycleViewPager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by huashigen on 2016/5/18.
 */
public class ProductDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    private TitleBarView title;
    private CycleViewPager cycleViewPager;
    private PtrClassicFrameLayout mPtrFrame;
    private LinearLayout mJoinRecordLayout, mHistoryLayout, mShareLayout, mProductPicLayout,
            mProgressLayout, mStatusDoneLayout;
    private RelativeLayout mStatusTimingLayout;
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private Button nowBuyBtn, addToCarBtn;
    private EnvaluateDialog envaluateDialog;
    private ProductDetailBean productDetailBean;
    private List<String> imageUrls = new ArrayList<String>();
    private TextView tvState, tvTitle, tvTotalNum, tvLeftNum;
    private ProgressBar progressBar;
    private TextView tvOrderCount;

    @Override
    protected void findViewById() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailActivity.this.finish();
            }
        });
        title.setTitleText(R.string.product_detail);
        nowBuyBtn = (Button) findViewById(R.id.now_buy_btn);
        nowBuyBtn.setOnClickListener(this);
        addToCarBtn = (Button) findViewById(R.id.now_add2car_btn);
        addToCarBtn.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        mJoinRecordLayout = (LinearLayout) findViewById(R.id.record_order_btn);
        mHistoryLayout = (LinearLayout) findViewById(R.id.history_orderbtn);
        mShareLayout = (LinearLayout) findViewById(R.id.share_orderbtn);
        mProductPicLayout = (LinearLayout) findViewById(R.id.pic_detail_btn);
        mProgressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        mStatusDoneLayout = (LinearLayout) findViewById(R.id.status_done);
        mStatusTimingLayout = (RelativeLayout) findViewById(R.id.status_timing);
        tvOrderCount = (TextView) findViewById(R.id.order_count);

        initPtrFrame();
        configImageLoader();
        initialize();

        getData();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_goods;
    }

    private void updateData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.refreshComplete();
            }
        }, 2000);
    }

    private void getData() {
        int id = getIntent().getIntExtra("id", 0);
        ProductDetailService service = new ProductDetailService(this);
        service.setCallback(new IOpenApiDataServiceCallback<ProductDetailResponse>() {
            @Override
            public void onGetData(ProductDetailResponse data) {
                try {
                    productDetailBean = data.data.item;
                    imageUrls.clear();
                    imageUrls.addAll(productDetailBean.getImage());
                    ProductDetailActivity.this.runOnUiThread(new Runnable() {
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
                Toast.makeText(ProductDetailActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("itemId=" + id, true);
    }

    private void refreshUI() {
        mJoinRecordLayout.setOnClickListener(this);
        mProductPicLayout.setOnClickListener(this);
        mHistoryLayout.setOnClickListener(this);
        mShareLayout.setOnClickListener(this);
        mStatusDoneLayout.setOnClickListener(this);
        findViewById(R.id.cal_detail_done).setOnClickListener(this);
        findViewById(R.id.cal_detail_done2).setOnClickListener(this);

        initialize();
        int status = productDetailBean.getStatus();
        String statusStr = "";
        if (status == 0) {
            statusStr = "进行中";
            mProgressLayout.setVisibility(View.VISIBLE);
            mStatusDoneLayout.setVisibility(View.GONE);
        } else if (status == 1) {
            statusStr = "开奖中";
            mProgressLayout.setVisibility(View.GONE);
            mStatusDoneLayout.setVisibility(View.GONE);
            mStatusTimingLayout.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_nper)).setText(productDetailBean.getNper() + "");
            findViewById(R.id.buy_cur).setVisibility(View.GONE);
            findViewById(R.id.buy_new).setVisibility(View.VISIBLE);
            findViewById(R.id.go_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(ProductDetailActivity.this, ProductDetailActivity.class);
                    intent.putExtra("id", productDetailBean.getNewItemId());
                    startActivity(intent);
                }
            });
            countDown(productDetailBean.getCountDown());
        } else if (status == 2) {
            statusStr = "已揭晓";
            mProgressLayout.setVisibility(View.GONE);
            mStatusDoneLayout.setVisibility(View.VISIBLE);
            findViewById(R.id.buy_cur).setVisibility(View.GONE);
            findViewById(R.id.buy_new).setVisibility(View.VISIBLE);
            findViewById(R.id.go_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(ProductDetailActivity.this, ProductDetailActivity.class);
                    intent.putExtra("id", productDetailBean.getNewItemId());
                    startActivity(intent);
                }
            });
        }
        ((TextView) findViewById(R.id.tv_state)).setText(statusStr);
        ((TextView) findViewById(R.id.tv_title)).setText("(第" + productDetailBean.getNper() + "期)" +
                "" + productDetailBean.getTitle());
        ((TextView) findViewById(R.id.tv_totalnum)).setText("总需" + productDetailBean.getTotalNum
                () + "人次");
        ((TextView) findViewById(R.id.tv_leftnum)).setText(productDetailBean.getTotalNum() -
                productDetailBean.getPartakeNum() + "");
        progressBar = (ProgressBar) findViewById(R.id.goodsProgress);
        progressBar.setMax(productDetailBean.getTotalNum());
        progressBar.setProgress(productDetailBean.getPartakeNum());
        Integer buyCount = productDetailBean.getBuyTimes();
        if (buyCount != null && buyCount > 0) {
            ((TextView) findViewById(R.id.tv_buy_count)).setText("您购买了" + buyCount + "人次，");
            findViewById(R.id.tv_buy_record_check).setOnClickListener(this);
        } else {
            findViewById(R.id.tv_buy_record_check).setVisibility(View.GONE);
        }
        //获奖者信息展示
        ((TextView) findViewById(R.id.nickName)).setText(productDetailBean.getUserName());
        ((TextView) findViewById(R.id.ip_and_addr)).setText(productDetailBean.getDisplayId() + "");
        ((TextView) findViewById(R.id.par_count)).setText(productDetailBean.getPartakeTimes() + "");
        ((TextView) findViewById(R.id.complete_time)).setText(productDetailBean.getAnnounceTime());
        ((TextView) findViewById(R.id.lcode_num)).setText(productDetailBean.getCode() + "");

        ImageView imageView = (ImageView) findViewById(R.id.pic_url);
        Glide.with(this).load(productDetailBean.getPhoto()).into(imageView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CacheShoppingCarData cacheShoppingCarData;
                List<ShoppingCartBean> listBeas;
//                Object list = CacheManager.getObject(CacheManager.TYPE_USER_INFO,
//                        CacheKeys.SHOPPING_CAR, CacheShoppingCarData.class);
                Object list = BaseApplication.getInstance().getShoppingCarData();
                if (list != null) {
                    cacheShoppingCarData = (CacheShoppingCarData) list;
                    listBeas = cacheShoppingCarData.shoppingCartBeans;
                    final int count = listBeas.size();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (count > 0) {
                                tvOrderCount.setVisibility(View.VISIBLE);
                                tvOrderCount.setText(count + "");
                            }
                        }
                    });
                }
            }
        }).start();
        findViewById(R.id.to_shopcar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HomeActivity.index = 2;
//                Intent intent = new Intent();
//                intent.setClass(ProductDetailActivity.this, HomeActivity.class);
//                startActivity(intent);
                startActivity(new Intent(ProductDetailActivity.this, OrderListActivity
                        .class));
            }
        });
    }

    private void initPtrFrame() {
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.refresh_view_frame);
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

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder
                        (QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager
            .ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
            }

        }

    };

    private void initialize() {
        if (imageUrls.size() <= 0) {
            return;
        }
        cycleViewPager = (CycleViewPager) getSupportFragmentManager().findFragmentById(R
                .id.slideshow_fragment);
        for (int i = 0; i < imageUrls.size(); i++) {
            ADInfo info = new ADInfo();
            info.setUrl(imageUrls.get(i));
            info.setContent("图片-->" + i);
            infos.add(info);
        }
        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));

        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);

        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);

        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(3000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    @Override
    public void onClick(View v) {
        if (productDetailBean == null) {
            return;
        }
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.record_order_btn:
                intent.putExtra("id", productDetailBean.getId());
                intent.setClass(ProductDetailActivity.this, JoinRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.history_orderbtn:
                intent.putExtra("sid", productDetailBean.getSid());
                intent.setClass(ProductDetailActivity.this, HistoryRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.share_orderbtn:
                intent.setClass(ProductDetailActivity.this, ShareActivity.class);
                intent.putExtra("sid", productDetailBean.getSid());
                startActivity(intent);
                break;
            case R.id.now_buy_btn:
                envaluateDialog = new EnvaluateDialog(this, R.style.CustomStyle,
                        productDetailBean.getUnitPrice().intValue(), productDetailBean.getLeftNum
                        (), new EnvaluateDialog
                        .OnPriceSelectedListener() {
                    @Override
                    public void onPriceSelected(int price) {
                        cacheOrder(price);
                        startActivity(new Intent(ProductDetailActivity.this, OrderListActivity
                                .class));
                    }
                });
                envaluateDialog.show();
                break;
            case R.id.pic_detail_btn:
                intent.setClass(this, ProductPicDetailActivity.class);
                intent.putExtra("id", productDetailBean.getId());
                startActivity(intent);
                break;
            case R.id.cal_detail_done:
                intent.putExtra("id", productDetailBean.getId());
                intent.setClass(ProductDetailActivity.this, CalculateDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.cal_detail_done2:
                intent.putExtra("id", productDetailBean.getId());
                intent.setClass(ProductDetailActivity.this, CalculateDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_buy_record_check:
                intent.putExtra("id", productDetailBean.getId());
                intent.setClass(ProductDetailActivity.this, MyBuyRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.now_add2car_btn:
                int addCount = 1;
                if (productDetailBean.getUnitPrice().intValue() == 1) {
                    addCount = 10;
                }
                cacheOrder(addCount);
                UIUtils.showToast(ProductDetailActivity.this, "加入成功~");
                break;
            default:
                break;
        }
    }

    /**
     * 缓存订单
     *
     * @param count
     */
    private void cacheOrder(int count) {
        CacheShoppingCarData cacheShoppingCarData;
        List<ShoppingCartBean> listBeas;
//        Object list = CacheManager.getObject(CacheManager.TYPE_USER_INFO,
//                CacheKeys.SHOPPING_CAR, CacheShoppingCarData.class);
        Object list = BaseApplication.getInstance().getShoppingCarData();
        if (list == null) {
            cacheShoppingCarData = new CacheShoppingCarData();
            listBeas = new ArrayList<ShoppingCartBean>();
            cacheShoppingCarData.shoppingCartBeans = listBeas;
        } else {
            cacheShoppingCarData = (CacheShoppingCarData) list;
            listBeas = cacheShoppingCarData.shoppingCartBeans;
        }
        //查询缓存中有没有这个商品，有的话就加一，没有就增加一个记录
        ShoppingCartBean bean = findOrder(listBeas, productDetailBean.getId());
        if (bean != null) {
//            bean.setBuyCount(bean.getBuyCount() + (price / productDetailBean.getUnitPrice()
//                    .intValue()));
            bean.setBuyCount(bean.getBuyCount() + count);
            if (bean.getBuyCount() > bean.getShenyurenshu()) {
                bean.setBuyCount(bean.getShenyurenshu());
            }
        } else {
            bean = new ShoppingCartBean();
            bean.setTitle(productDetailBean.getTitle());
            bean.setCanyurenshu(productDetailBean.getPartakeNum());
            bean.setId(productDetailBean.getId());
            bean.setShenyurenshu(productDetailBean.getLeftNum());
            bean.setYunjiage(productDetailBean.getUnitPrice());
            bean.setZongrenshu(productDetailBean.getTotalNum());
            bean.setThumb(productDetailBean.getCover());
//            bean.setBuyCount((price / productDetailBean.getUnitPrice()
//                    .intValue()));
            int counts = 10;
            if (productDetailBean.getUnitPrice().intValue() >= 10) {
                counts = 1;
            }
            bean.setBuyCount(counts);
            if (bean.getBuyCount() > bean.getShenyurenshu()) {
                bean.setBuyCount(bean.getShenyurenshu());
            }
            listBeas.add(bean);
        }
        final int productNum = listBeas.size();
        if (productNum > 0) {
            tvOrderCount.setVisibility(View.VISIBLE);
            tvOrderCount.setText(productNum + "");
        }
//        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.SHOPPING_CAR,
//                cacheShoppingCarData);
        BaseApplication.getInstance().setShoppingCarData(cacheShoppingCarData);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(HomeActivity
                .PRODUCT_NUM_CHANGED));
    }

    /**
     * 看是否缓存了这个id的商品
     *
     * @param listBeas
     * @param id
     * @return
     */
    private ShoppingCartBean findOrder(List<ShoppingCartBean> listBeas, int id) {
        for (ShoppingCartBean bean : listBeas) {
            if (bean.getId() == id) {
                return bean;
            }
        }
        return null;
    }

    private void countDown(long countDown) {
        final TextView tvCountDown = (TextView) findViewById(R.id.tv_timer);

        CountDownTimer timer = new CountDownTimer(countDown, 10) {
            @Override
            public void onTick(long l) {
                int totalSecond = (int) (l / 1000);
                int minute = totalSecond / 60;
                int second = totalSecond % 60;
                String secondStr = (second >= 10) ? second + "" : "0" + second;
                int millis = (int) (l % 1000);
                tvCountDown.setText("0" + minute + ":" + secondStr + ":" + millis);
            }

            @Override
            public void onFinish() {
                String statusStr = "已揭晓";
                mProgressLayout.setVisibility(View.GONE);
                mStatusDoneLayout.setVisibility(View.VISIBLE);
                mStatusTimingLayout.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.tv_state)).setText(statusStr);
                //获奖者信息展示
                ((TextView) findViewById(R.id.nickName)).setText(productDetailBean.getUserName());
                ((TextView) findViewById(R.id.ip_and_addr)).setText(productDetailBean
                        .getDisplayId() + "");
                ((TextView) findViewById(R.id.par_count)).setText(productDetailBean
                        .getPartakeTimes() + "");
                ((TextView) findViewById(R.id.complete_time)).setText(productDetailBean
                        .getAnnounceTime());
                ((TextView) findViewById(R.id.lcode_num)).setText(productDetailBean.getCode() + "");
            }
        };
        timer.start();
    }
}
