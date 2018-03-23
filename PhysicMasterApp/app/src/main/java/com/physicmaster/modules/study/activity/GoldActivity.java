package com.physicmaster.modules.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.SplashActivity;
import com.physicmaster.modules.WebviewActivity;
import com.physicmaster.modules.study.fragment.dialogfragment.BuyPropDialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.game.GoldMallResponse;
import com.physicmaster.net.service.game.GoldMallService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.MoreGridView;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

import java.util.List;

public class GoldActivity extends BaseActivity {

    private static final String TAG = "GoldActivity";
    //  private ArrayList<String> mGoldList = new ArrayList<>();//道具集合
    private MoreGridView mGvPet;
    private MoreGridView mGvProp;
    private List<GoldMallResponse.DataBean.AppPropsBean> mAppPropList;
    private TextView tvTotalGold;
    private boolean isBuy = false;
    private int[] colors = new int[]{R.color.colorTopicmap1, R.color.colorTopicmap2, R.color
            .colorTopicmap3,
            R.color.colorTopicmap2, R.color.colorTopicmap3, R.color.colorTopicmap1,
            R.color.colorTopicmap3, R.color.colorTopicmap1, R.color.colorTopicmap2};
    private GoldMallResponse.DataBean dataBean;
    private RelativeLayout rlPet;
    private RelativeLayout rlAward;
    private String goldCoinLotterPageUrl;

    @Override
    protected void findViewById() {
        mGvProp = (MoreGridView) findViewById(R.id.gv_prop);
        mGvPet = (MoreGridView) findViewById(R.id.gv_pet);
        tvTotalGold = (TextView) findViewById(R.id.tv_total_gold);
        rlPet = (RelativeLayout) findViewById(R.id.rl_pet);
        rlAward = (RelativeLayout) findViewById(R.id.rl_award);

        initTitle();
    }

    private void initTitle() {
        new TitleBuilder(this)
                .setLeftText("返回")
                .setRightImageRes(R.mipmap.jinbi_jilu)
                .setRightTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(GoldActivity.this, RecordActivity.class));
                    }
                })
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("金币");
    }

    public void setTvTotalGold(String text) {
        tvTotalGold.setText(text);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        rlAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartupResponse.DataBean dataBean = BaseApplication.getStartupDataBean();
                if (null != dataBean) {
                    goldCoinLotterPageUrl = dataBean.lotteryPageUrl;
                    if (!TextUtils.isEmpty(goldCoinLotterPageUrl)) {
                        Intent intent = new Intent(GoldActivity.this, WebviewActivity.class);
                        intent.putExtra("url", goldCoinLotterPageUrl);
                        intent.putExtra("allowShare", false);
                        startActivity(intent);
                    }
                } else {
                    startActivity(new Intent(GoldActivity.this, SplashActivity.class));
                    UIUtils.showToast(GoldActivity.this, "数据异常");
                    finish();
                }
            }
        });
        showGoldMall();
    }

    private void showGoldMall() {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final GoldMallService service = new GoldMallService(GoldActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GoldMallResponse>() {
            @Override
            public void onGetData(GoldMallResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                loadingDialog.dismissDialog();
                //Toast.makeText(GoldActivity.this, "获取成功", Toast.LENGTH_SHORT).show();
                dataBean = data.data;
                mAppPropList = data.data.appProps;
                tvTotalGold.setText(data.data.goldValue + "");
                GoldAdapter goldAdapter = new GoldAdapter();
                mGvProp.setAdapter(goldAdapter);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(GoldActivity.this, errorMsg);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("", false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_gold;
    }

    class GoldAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mAppPropList.size();
        }

        @Override
        public GoldMallResponse.DataBean.AppPropsBean getItem(int position) {
            return mAppPropList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(GoldActivity.this,
                        R.layout.grid_item_gold, null);
                holder = new ViewHolder();
                holder.tvExchange = (TextView) convertView
                        .findViewById(R.id.tv_exchange);
                holder.tvInfo = (TextView) convertView
                        .findViewById(R.id.tv_info);
                holder.tvPrice = (TextView) convertView
                        .findViewById(R.id.tv_price);
                holder.ivEnergy = (ImageView) convertView
                        .findViewById(R.id.iv_energy);
                holder.rlLayout = (RelativeLayout) convertView
                        .findViewById(R.id.rl_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GoldMallResponse.DataBean.AppPropsBean item = getItem(position);

            if (!TextUtils.isEmpty(item.propImg)) {
                Glide.with(GoldActivity.this).load(item.propImg).placeholder(R.drawable
                        .gray_background).into(holder.ivEnergy);

            }
            holder.tvExchange.setText("(" + item.propIntro + ")");
            holder.tvInfo.setText(item.propName + "");
            holder.tvPrice.setText(item.priceGoldCoin + "");
            holder.rlLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BuyPropDialogFragment buyPropDialogFragment = new BuyPropDialogFragment();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", item);
                    bundle.putInt("goldValue", dataBean.goldValue);
                    buyPropDialogFragment.setArguments(bundle);
                    buyPropDialogFragment.show(getSupportFragmentManager(), "gold_dialog");


                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvExchange;
        TextView tvInfo;
        TextView tvPrice;
        ImageView ivEnergy;
        RelativeLayout rlLayout;
    }
}
