package com.iask.yiyuanlegou1.home.person.address;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.home.main.product.AddressDetailActivity;
import com.iask.yiyuanlegou1.home.main.product.ProductClassifyAdapter;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.CommonResponse;
import com.iask.yiyuanlegou1.network.respose.account.AddressBean;
import com.iask.yiyuanlegou1.network.respose.account.AddressListResponse;
import com.iask.yiyuanlegou1.network.respose.product.ProductClassifyBean;
import com.iask.yiyuanlegou1.network.service.account.AddressListService;
import com.iask.yiyuanlegou1.network.service.account.DeleteAddressService;
import com.iask.yiyuanlegou1.widget.AlertDialog;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class AddressManageActivity extends BaseActivity {
    private TitleBarView title;
    private PtrClassicFrameLayout mPtrFrame;
    private ListView listView;
    private List<AddressBean> addressBeans;
    private AddressAdapter adapter;
    private LocalBroadcastManager localBroadcastManager;
    public static final String ADDRESS_ADDED = "address_added";

    @Override
    protected void findViewById() {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_address_manage;
    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setTitleText(R.string.address_manage);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressManageActivity.this.finish();
            }
        });
        title.setBtnRight(0, R.string.add);
        title.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AddressManageActivity.this, AddAddressInfoActivity.class);
                intent.putExtra("isAdd", true);
                startActivity(intent);
            }
        });
        initPtrFrame();
        initListView();
        getData(true);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(onAddressAddReceiver, new IntentFilter
                (ADDRESS_ADDED));
    }

    private BroadcastReceiver onAddressAddReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getData(false);
        }
    };

    private void initPtrFrame() {
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.refresh_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
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
        addressBeans = new ArrayList<AddressBean>();
        adapter = new AddressAdapter(this, addressBeans);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("isAdd", false);
                intent.putExtra("bean", addressBeans.get(position));
                intent.setClass(AddressManageActivity.this, AddAddressInfoActivity.class);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long
                    id) {
                AlertDialog dialog = new AlertDialog(AddressManageActivity.this);
                dialog.builder();
                dialog.setTitle("确定删除？");
                dialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAddress(id);
                    }
                });
                dialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    private void getData(boolean showDialog) {
        AddressListService service = new AddressListService(this);
        service.setCallback(new IOpenApiDataServiceCallback<AddressListResponse>() {
            @Override
            public void onGetData(AddressListResponse data) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPtrFrame.refreshComplete();
                        }
                    });
                    addressBeans.clear();
                    addressBeans.addAll(data.data.addressList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(AddressManageActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        String uid = BaseApplication.getInstance().getUserId();
        service.post("uid=" + uid, showDialog);
    }

    private void deleteAddress(long id) {
        DeleteAddressService service = new DeleteAddressService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                Toast.makeText(AddressManageActivity.this, data.getMsg(), Toast.LENGTH_SHORT)
                        .show();
                getData(false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(AddressManageActivity.this, errorMsg, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        service.post("dizhiid=" + id, true);
    }

}
