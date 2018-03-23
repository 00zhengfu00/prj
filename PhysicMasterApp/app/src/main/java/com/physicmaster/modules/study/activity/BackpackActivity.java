package com.physicmaster.modules.study.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.modules.study.fragment.dialogfragment.EndeavorDialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.game.GetBackpackResponse;
import com.physicmaster.net.response.game.UsePropResponse;
import com.physicmaster.net.service.game.GetBackpackService;
import com.physicmaster.net.service.game.UsePropService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

import java.util.List;

import static com.physicmaster.widget.PickerView.TAG;

public class BackpackActivity extends BaseActivity {

    private GridView                                            mgvBackpack;
    private List<GetBackpackResponse.DataBean.PackPropListBean> mPropList;
    private RelativeLayout                                      rlEmpty;
    private boolean isUser = false;
    @Override
    protected void findViewById() {

        mgvBackpack = (GridView) findViewById(R.id.lv_backpack);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_empty);

        initTitle();
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
                .setMiddleTitleText("背包");
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onResume() {
        showBackpack();
        super.onResume();
    }

    private void showBackpack() {
        final GetBackpackService service = new GetBackpackService(BackpackActivity.this);
//        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(BackpackActivity
//                .this);
        service.setCallback(new IOpenApiDataServiceCallback<GetBackpackResponse>() {

            @Override
            public void onGetData(GetBackpackResponse data) {
                Log.d(TAG, "加载完成：onGetData: " + data.msg);
                //Toast.makeText(BackpackActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                mPropList = data.data.packPropList;
//                loadingDialog.dismissDialog();
                if (null == mPropList || mPropList.size() == 0) {
                    mgvBackpack.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);
                } else {
                    rlEmpty.setVisibility(View.GONE);
                    mgvBackpack.setVisibility(View.VISIBLE);
                    BackpackAdapter backpackAdapter = new BackpackAdapter();
                    mgvBackpack.setAdapter(backpackAdapter);
                }

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "加载失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(BackpackActivity.this, errorMsg);
//                loadingDialog.dismissDialog();
                mgvBackpack.setVisibility(View.GONE);
                rlEmpty.setVisibility(View.VISIBLE);
            }
        });
//        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
//            @Override
//            public void onCancel() {
//                service.cancel();
//            }
//        });
        service.postLogined("", false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_backpack;
    }


    class BackpackAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPropList.size();
        }

        @Override
        public GetBackpackResponse.DataBean.PackPropListBean getItem(int position) {
            return mPropList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(BackpackActivity.this,
                        R.layout.list_item_backpack, null);
                holder = new ViewHolder();
                holder.tvBackpack = (TextView) convertView
                        .findViewById(R.id.tv_backpack);
                holder.tvNumber = (TextView) convertView
                        .findViewById(R.id.tv_number);
                holder.ivBackpack = (ImageView) convertView
                        .findViewById(R.id.iv_backpack);
                holder.btnBackpack = (Button) convertView
                        .findViewById(R.id.btn_backpack);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GetBackpackResponse.DataBean.PackPropListBean item = getItem(position);

            holder.tvBackpack.setText(item.propName + "");
            holder.tvNumber.setText(item.propCount + "");
            if (!TextUtils.isEmpty(item.propImg)) {
                Glide.with(BackpackActivity.this).load(item.propImg).into(holder.ivBackpack);
            }
            holder.btnBackpack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isUser) {
                        if (item.propType == 4) {
                            EndeavorDialogFragment endeavorDialogFragment = new EndeavorDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("propId", item.propId);
                            bundle.putInt("propCount", item.propCount);
                            endeavorDialogFragment.setArguments(bundle);
                            endeavorDialogFragment.setUseEndeavorListener(new EndeavorDialogFragment.OnUseEndeavorListener() {
                                @Override
                                public void onUesEndeavor() {
                                    showBackpack();
                                }
                            });
                            endeavorDialogFragment.show(getSupportFragmentManager(), "endeavor_dialog");
                        } else {
                            isUser = true;
                            useProp(item.propId, item.propIntro);
                        }
                    }
                }
            });

            return convertView;
        }
    }

    private void useProp(int propId, final String function) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final UsePropService service = new UsePropService(BackpackActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<UsePropResponse>() {

            @Override
            public void onGetData(UsePropResponse data) {
                loadingDialog.dismissDialog();
                Log.d(TAG, "加载完成：onGetData: " + data.msg);
                isUser = false;
                UIUtils.showToast(BackpackActivity.this, function);
                showBackpack();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                loadingDialog.dismissDialog();
                isUser = false;
                Log.e(TAG, "加载失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(BackpackActivity.this, errorMsg);
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                isUser = false;
                service.cancel();
            }
        });
        service.postLogined("propId=" + propId, false);

    }

    static class ViewHolder {
        TextView  tvBackpack;
        ImageView ivBackpack;
        Button    btnBackpack;
        TextView  tvNumber;
    }
}
