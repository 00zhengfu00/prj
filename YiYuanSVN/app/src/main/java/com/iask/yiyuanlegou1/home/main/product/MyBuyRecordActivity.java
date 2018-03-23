package com.iask.yiyuanlegou1.home.main.product;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.account.MyBuyRecordBean;
import com.iask.yiyuanlegou1.network.respose.account.MyBuyRecordReponse;
import com.iask.yiyuanlegou1.network.respose.account.UserInfo;
import com.iask.yiyuanlegou1.network.service.product.MyBuyRecordService;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MyBuyRecordActivity extends BaseActivity {
    private TitleBarView title;
    private PtrClassicFrameLayout mPtrFrame;
    private ListView listView;
    private List<MyBuyRecordBean> myBuyRecordBeans;
    private int itemId = 0;
    private MyBuyRecordAdapter adapter;

    @Override
    protected void findViewById() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBuyRecordActivity.this.finish();
            }
        });
        title.setTitleText(R.string.my_buy_num);
    }

    @Override
    protected void initView() {
        initPtrFrame();
        initListView();

        itemId = getIntent().getIntExtra("id", 0);
        getData();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_buy_record;
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
     * listView 相关设置
     */
    @SuppressLint("NewApi")
    private void initListView() {
        listView = (ListView) findViewById(R.id.listView);
        myBuyRecordBeans = new ArrayList<MyBuyRecordBean>();
        adapter = new MyBuyRecordAdapter(myBuyRecordBeans, this);
        listView.setAdapter(adapter);
    }

    private void updateData() {
        getData();
    }

    private void getData() {
        MyBuyRecordService service = new MyBuyRecordService(this);
        service.setCallback(new IOpenApiDataServiceCallback<MyBuyRecordReponse>() {
            @Override
            public void onGetData(MyBuyRecordReponse data) {
                mPtrFrame.refreshComplete();
                try {
                    myBuyRecordBeans.clear();
                    myBuyRecordBeans.addAll(data.data.history);
                    MyBuyRecordActivity.this.runOnUiThread(new Runnable() {
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
                Toast.makeText(MyBuyRecordActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        String uid = BaseApplication.getInstance().getUserId();
        service.post("itemId=" + itemId + "&uid=" + uid, false);
    }

    private void refreshUI() {
        adapter.notifyDataSetChanged();
    }
}
