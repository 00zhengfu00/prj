package com.iask.yiyuanlegou1.home.main.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.product.ProductClassifyBean;
import com.iask.yiyuanlegou1.network.respose.product.ProductClassifyResponse;
import com.iask.yiyuanlegou1.network.service.product.ProductClassifyService;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class ProductClassifyActivity extends BaseActivity {
    private TitleBarView title;
    private PtrClassicFrameLayout mPtrFrame;
    private ListView listView;
    private List<ProductClassifyBean> productClassifyBeans;
    private ProductClassifyAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_product_classify;
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setTitleText(R.string.product_classify);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductClassifyActivity.this.finish();
            }
        });
        initPtrFrame();
        initListView();
        getData();
    }


    private void initPtrFrame() {
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.refresh_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
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
        productClassifyBeans = new ArrayList<ProductClassifyBean>();
        adapter = new ProductClassifyAdapter(productClassifyBeans, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ProductClassifyActivity.this, ProductClassifyListActivity.class);
                intent.putExtra("title", productClassifyBeans.get(position).getName());
                intent.putExtra("category", productClassifyBeans.get(position).getCateid());
                startActivity(intent);
            }
        });
    }

    private void getData() {
        mPtrFrame.autoRefresh();
        ProductClassifyService service = new ProductClassifyService(this);
        service.setCallback(new IOpenApiDataServiceCallback<ProductClassifyResponse>() {
            @Override
            public void onGetData(ProductClassifyResponse data) {
                try {
                    productClassifyBeans.clear();
                    productClassifyBeans.addAll(data.data.category);
                    mPtrFrame.refreshComplete();
                    ProductClassifyActivity.this.runOnUiThread(new Runnable() {
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
                Toast.makeText(ProductClassifyActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("", false);
    }

    private void refreshUI() {
        adapter.notifyDataSetChanged();
    }
}
