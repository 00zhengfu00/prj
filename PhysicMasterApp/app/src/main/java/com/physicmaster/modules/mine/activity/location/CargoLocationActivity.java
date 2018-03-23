package com.physicmaster.modules.mine.activity.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.TitleBuilder;

public class CargoLocationActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rlName;
    private TextView tvName;
    private RelativeLayout rlPhone;
    private TextView tvPhone;
    private LinearLayout llLocation;
    private TextView tvLocation;
    private RelativeLayout rlLocation2;
    private TextView tvLocation2;
    private SelectChageReceiver mSelectChageReceiver;
    private Button btnOk;
    private String areaId;


    @Override
    protected void findViewById() {

        rlName = (RelativeLayout) findViewById(R.id.rl_name);
        tvName = (TextView) findViewById(R.id.tv_name);
        rlPhone = (RelativeLayout) findViewById(R.id.rl_phone);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        llLocation = (LinearLayout) findViewById(R.id.ll_location);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        rlLocation2 = (RelativeLayout) findViewById(R.id.rl_location2);
        tvLocation2 = (TextView) findViewById(R.id.tv_location2);
        btnOk = (Button) findViewById(R.id.btn_ok);
        initTitle();

        rlName.setOnClickListener(this);
        rlPhone.setOnClickListener(this);
        llLocation.setOnClickListener(this);
        rlLocation2.setOnClickListener(this);

        mSelectChageReceiver = new SelectChageReceiver();
        registerReceiver(mSelectChageReceiver, new IntentFilter("com.physicmaster.SELECT_LOCATION"));
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("收货地址");
    }

    @Override
    protected void initView() {
        String location = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys.USER_LOCATION);
        String location2 = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys.USER_LOCATION2);
        String name = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys.USER_NAME);
        String phone = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys.USER_PHONE);
        areaId = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys.USER_AREAID);
        if (!TextUtils.isEmpty(location)) {
            tvLocation.setText(location);
        }
        if (!TextUtils.isEmpty(location2)) {
            tvLocation2.setText(location2);
        }
        if (!TextUtils.isEmpty(name)) {
            tvName.setText(name);
        }
        if (!TextUtils.isEmpty(phone)) {
            tvPhone.setText(phone);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check() {
        String name = tvName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            UIUtils.showToast(this, "请输入收货人姓名");
            return;
        }
        String phone = tvPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast(this, "请输入联系电话");
            return;
        }
        String location = tvLocation.getText().toString().trim();
        if (TextUtils.isEmpty(location)) {
            UIUtils.showToast(this, "请选择所在地");
            return;
        }
        String location2 = tvLocation2.getText().toString().trim();
        if (TextUtils.isEmpty(location2)) {
            UIUtils.showToast(this, "请输入详细地址");
            return;
        }

        UIUtils.showToast(CargoLocationActivity.this, "修改成功");
        Intent intent = new Intent();
        intent.putExtra("phone", phone);
        intent.putExtra("name", name);
        intent.putExtra("location", location);
        intent.putExtra("location2", location2);
        intent.putExtra("areaId", areaId);
        setResult(RESULT_OK, intent);
        finish();
    }

    public class SelectChageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.physicmaster.SELECT_LOCATION")) {
                String location = intent.getStringExtra("aresName");
                areaId = intent.getStringExtra("areaId");
                if (!TextUtils.isEmpty(location)) {
                    tvLocation.setText(location);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (null != mSelectChageReceiver) {
            unregisterReceiver(mSelectChageReceiver);
            mSelectChageReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_name:
                startActivityForResult(new Intent(this, CargoNameActivity.class), 0);
                break;
            case R.id.rl_phone:
                startActivityForResult(new Intent(this, CargoPhoneActivity.class), 1);
                break;
            case R.id.ll_location:
                startActivity(new Intent(CargoLocationActivity.this, SelectLocationActivity.class));
                break;
            case R.id.rl_location2:
                startActivityForResult(new Intent(this, DetailLocationActivity.class), 2);
                break;
        }

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_cargo_location;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (data != null && data.hasExtra("name")) {
                String name = data.getStringExtra("name");
                tvName.setText(name);
            }
        } else if (requestCode == 1) {
            if (data != null && data.hasExtra("phone")) {
                String name = data.getStringExtra("phone");
                tvPhone.setText(name);
            }
        } else if (requestCode == 2) {
            if (data != null && data.hasExtra("location2")) {
                String name = data.getStringExtra("location2");
                tvLocation2.setText(name);
            }
        }
    }
}
