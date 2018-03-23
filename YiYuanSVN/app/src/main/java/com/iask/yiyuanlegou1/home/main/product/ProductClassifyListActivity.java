package com.iask.yiyuanlegou1.home.main.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.home.main.ProductAdapter;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.home.HomePageProductBean;
import com.iask.yiyuanlegou1.network.respose.home.HomePageProductResponse;
import com.iask.yiyuanlegou1.network.service.product.ProductClassifyListService;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class ProductClassifyListActivity extends BaseActivity {

    private TitleBarView title;
    private PtrClassicFrameLayout mPtrFrame;
    private ListView listView;
    private List<HomePageProductBean> productClassifyListBeans;
    private ProductAdapter adapter;
    private int category;

    @Override
    protected void findViewById() {
    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        String titleS = getIntent().getStringExtra("title");
        category = getIntent().getIntExtra("category", 0);
        title.setTitleTextStr(titleS);

        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductClassifyListActivity.this.finish();
            }
        });
        title.setBtnRight(R.mipmap.car_white);
        title.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        initPtrFrame();
        initListView();
        setupType();
        getData(0);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_product_classify_list;
    }

    private void initPtrFrame() {
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.refresh_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(0);
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
     * listView 相关设置
     */
    @SuppressLint("NewApi")
    private void initListView() {
        listView = (ListView) findViewById(R.id.listView);
        productClassifyListBeans = new ArrayList<HomePageProductBean>();
        adapter = new ProductAdapter(productClassifyListBeans, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ProductClassifyListActivity.this, ProductDetailActivity.class);
                intent.putExtra("id",(int)id);
                startActivity(intent);
            }
        });
    }

    private void getData(int type) {
        ProductClassifyListService service = new ProductClassifyListService(this);
        service.setCallback(new IOpenApiDataServiceCallback<HomePageProductResponse>() {
            @Override
            public void onGetData(HomePageProductResponse data) {
                mPtrFrame.refreshComplete();
                try {
                    productClassifyListBeans.clear();
                    productClassifyListBeans.addAll(data.data.shopList);
                    ProductClassifyListActivity.this.runOnUiThread(new Runnable() {
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
                mPtrFrame.refreshComplete();
                Toast.makeText(ProductClassifyListActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("category=" + category + "&type=" + type + "&order=" + "DESC", false);
    }

    private void refreshUI() {
        adapter.notifyDataSetChanged();
    }

    private void setupType() {
        final TextView tv1, tv2, tv3, tv4;
        tv1 = (TextView) findViewById(R.id.hotCate);
        tv2 = (TextView) findViewById(R.id.newCate);
        tv3 = (TextView) findViewById(R.id.priceCate);
        tv4 = (TextView) findViewById(R.id.req_man_text);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(1);
                tv1.setTextColor(getResources().getColor(R.color.button_red));
                tv2.setTextColor(getResources().getColor(R.color.text_default_color));
                tv3.setTextColor(getResources().getColor(R.color.text_default_color));
                tv4.setTextColor(getResources().getColor(R.color.text_default_color));
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(0);
                tv1.setTextColor(getResources().getColor(R.color.text_default_color));
                tv2.setTextColor(getResources().getColor(R.color.button_red));
                tv3.setTextColor(getResources().getColor(R.color.text_default_color));
                tv4.setTextColor(getResources().getColor(R.color.text_default_color));
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(3);
                tv1.setTextColor(getResources().getColor(R.color.text_default_color));
                tv2.setTextColor(getResources().getColor(R.color.text_default_color));
                tv3.setTextColor(getResources().getColor(R.color.button_red));
                tv4.setTextColor(getResources().getColor(R.color.text_default_color));
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(2);
                tv1.setTextColor(getResources().getColor(R.color.text_default_color));
                tv2.setTextColor(getResources().getColor(R.color.text_default_color));
                tv3.setTextColor(getResources().getColor(R.color.text_default_color));
                tv4.setTextColor(getResources().getColor(R.color.button_red));
            }
        });
    }
}
