package com.iask.yiyuanlegou1.home.person.address;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.bean.account.AddAdressBean;
import com.iask.yiyuanlegou1.network.respose.CommonResponse;
import com.iask.yiyuanlegou1.network.respose.account.AddressBean;
import com.iask.yiyuanlegou1.network.service.account.AddAddressService;
import com.iask.yiyuanlegou1.network.service.account.UpdateAddressService;
import com.iask.yiyuanlegou1.utils.StringUtils;
import com.iask.yiyuanlegou1.widget.TitleBarView;

public class AddAddressInfoActivity extends BaseActivity {
    private TitleBarView title;
    private String province = "", city = "", area = "";
    private int id;
    public static final int SELECT_ADDRESS = 0x1;
    private boolean isAdd;

    @Override
    protected void findViewById() {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_address_info;
    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddressInfoActivity.this.finish();
            }
        });
        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        findViewById(R.id.layout_select_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AddAddressInfoActivity.this, SelectAddressActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
            }
        });
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        if (!isAdd) {
            AddressBean bean = (AddressBean) getIntent().getSerializableExtra("bean");
            setContent(bean);
            title.setTitleText(R.string.edit_address);
        } else {
            title.setTitleText(R.string.add_address);
        }
    }

    private void setContent(AddressBean bean) {
        EditText editUser = (EditText) findViewById(R.id.et_name);
        editUser.setText(bean.getShouhuoren());
        EditText editMobile = (EditText) findViewById(R.id.et_mobile);
        editMobile.setText(bean.getMobile());
        TextView tvProAndCity = (TextView) findViewById(R.id.tv_area);
        province = bean.getSheng();
        city = bean.getShi();
        area = bean.getXian();
        tvProAndCity.setText(getString(province) + getString(city) + getString(area));
        EditText editDetail = (EditText) findViewById(R.id.et_detail);
        editDetail.setText(bean.getJiedao());
        if ("Y".equals(bean.getDefaul())) {
            CheckBox box = (CheckBox) findViewById(R.id.set_default);
            box.setChecked(true);
        }
        id = bean.getId();
    }

    private void submit() {
        AddAdressBean bean = new AddAdressBean();
        EditText editUser = (EditText) findViewById(R.id.et_name);
        String userName = editUser.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(AddAddressInfoActivity.this, "收货人姓名不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        bean.setShouhuoren(userName);
        EditText editMobile = (EditText) findViewById(R.id.et_mobile);
        String mobile = editMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(AddAddressInfoActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isMobileNO(mobile)) {
            Toast.makeText(AddAddressInfoActivity.this, "手机号不合法！", Toast.LENGTH_SHORT).show();
            return;
        }
        bean.setMobile(mobile);
        TextView tvProAndCity = (TextView) findViewById(R.id.tv_area);
        String proAndCity = tvProAndCity.getText().toString();
        if (TextUtils.isEmpty(proAndCity)) {
            Toast.makeText(AddAddressInfoActivity.this, "请选择省市地区！", Toast.LENGTH_SHORT).show();
            return;
        }
        bean.setSheng(province);
        bean.setShi(city);
        bean.setXian(area);
        EditText editDetail = (EditText) findViewById(R.id.et_detail);
        String detail = editDetail.getText().toString();
        if (TextUtils.isEmpty(detail)) {
            Toast.makeText(AddAddressInfoActivity.this, "请输入详细地址！", Toast.LENGTH_SHORT).show();
            return;
        }
        bean.setJiedao(detail);
        CheckBox box = (CheckBox) findViewById(R.id.set_default);
        if (box.isChecked()) {
            bean.setDefaul("Y");
        } else {
            bean.setDefaul("N");
        }
        OpenApiDataServiceBase service = null;
        if (isAdd) {
            service = new AddAddressService(this);
            service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
                @Override
                public void onGetData(CommonResponse data) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddAddressInfoActivity.this, "添加成功！", Toast.LENGTH_SHORT)
                                    .show();
                            LocalBroadcastManager.getInstance(AddAddressInfoActivity.this)
                                    .sendBroadcast(new Intent(AddressManageActivity.ADDRESS_ADDED));
                            AddAddressInfoActivity.this.finish();
                        }
                    });
                }

                @Override
                public void onGetError(int errorCode, String errorMsg, Throwable error) {
                    Toast.makeText(AddAddressInfoActivity.this, errorMsg, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        } else {
            bean.setId(id);
            service = new UpdateAddressService(this);
            service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
                @Override
                public void onGetData(CommonResponse data) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddAddressInfoActivity.this, "更新成功！", Toast.LENGTH_SHORT)
                                    .show();
                            LocalBroadcastManager.getInstance(AddAddressInfoActivity.this)
                                    .sendBroadcast(new Intent(AddressManageActivity.ADDRESS_ADDED));
                            AddAddressInfoActivity.this.finish();
                        }
                    });
                }

                @Override
                public void onGetError(int errorCode, String errorMsg, Throwable error) {
                    Toast.makeText(AddAddressInfoActivity.this, errorMsg, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
        service.post(bean.toString(), true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == SELECT_ADDRESS) {
            province = data.getStringExtra("province");
            city = data.getStringExtra("city");
            area = data.getStringExtra("area");
            TextView tvProAndCity = (TextView) findViewById(R.id.tv_area);
            tvProAndCity.setText(getString(province) + getString(city) + getString(area));
        }
    }

    private String getString(String str) {
        if (TextUtils.isEmpty(str) || "null".equals(str)) {
            return "";
        } else {
            return str;
        }
    }
}
