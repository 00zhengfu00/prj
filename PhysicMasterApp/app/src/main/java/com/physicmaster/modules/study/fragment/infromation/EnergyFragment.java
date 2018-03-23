package com.physicmaster.modules.study.fragment.infromation;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseFragment2;
import com.physicmaster.modules.mine.activity.friend.FriendInfoActivity;
import com.physicmaster.modules.study.activity.InformationActivity;
import com.physicmaster.modules.study.activity.MsgChangeListener;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.game.AgreeEnergyResponse;
import com.physicmaster.net.response.game.EnergyResponse;
import com.physicmaster.net.response.game.RefuseEnergyResponse;
import com.physicmaster.net.service.game.AgreeEnergyService;
import com.physicmaster.net.service.game.EnergyService;
import com.physicmaster.net.service.game.RefuseEnergyService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/18 13:32
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class EnergyFragment extends BaseFragment2 {


    private InformationActivity                                                   mContext;
    private ListView                                                              lvEnergy;
    private List<EnergyResponse.DataBean.UserEnergyListBean.AppReceiveVoListBean> appReceiveVoList;
    private List<EnergyResponse.DataBean.UserEnergyListBean.AppReceiveVoListBean>
                                                                                  appSendEnergVoList;
    private RelativeLayout                                                        rlHave;
    private RelativeLayout                                                        rlEmpty;
    private List<Integer>                                                         argeeList;
    private EnergyAdapter                                                         energyAdapter;
    private Button                                                                btnAgree;
    private Button                                                                btnDemand;
    private MsgChangeListener                                                     msgChangeListener;

    private void bufuseEnergy() {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);

        final EnergyService service = new EnergyService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<EnergyResponse>() {
            @Override
            public void onGetData(EnergyResponse data) {
                loadingDialog.dismissDialog();
                appReceiveVoList = data.data.UserEnergyList.appReceiveVoList;
                appSendEnergVoList = data.data.UserEnergyList.appSendEnergVoList;

                if (null != appSendEnergVoList && appSendEnergVoList.size() != 0) {
                    appReceiveVoList.addAll(appSendEnergVoList);
                }
                if (null == appReceiveVoList || appReceiveVoList.size() == 0) {
                    rlHave.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);
                    btnAgree.setVisibility(View.GONE);
                    btnDemand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            demandEnergy();
                        }
                    });
                } else {
                    rlHave.setVisibility(View.VISIBLE);
                    rlEmpty.setVisibility(View.GONE);
                    btnAgree.setVisibility(View.VISIBLE);
                    energyAdapter = new EnergyAdapter();
                    lvEnergy.setAdapter(energyAdapter);
                    btnAgree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            argeeList = new ArrayList<Integer>();
                            int appReceiveVoListSize = appReceiveVoList.size();
                            for (int i = 0; i < appReceiveVoListSize; i++) {
                                argeeList.add(appReceiveVoList.get(i).energyRequestId);
                            }
                            agree(argeeList, -1);
                        }
                    });
                }

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
                rlHave.setVisibility(View.GONE);
                btnAgree.setVisibility(View.GONE);
                rlEmpty.setVisibility(View.VISIBLE);
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

    private void demandEnergy() {
        DemandEnergyDialogFragment dialogFragment = new DemandEnergyDialogFragment();
        dialogFragment.show(getFragmentManager(), "dialog_fragment");
    }

    @Override
    protected void initView(View view) {
        mContext = (InformationActivity) getActivity();


        lvEnergy = (ListView) rootView.findViewById(R.id.lv_energy);
        rlHave = (RelativeLayout) rootView.findViewById(R.id.rl_have);
        rlEmpty = (RelativeLayout) rootView.findViewById(R.id.rl_empty);
        btnAgree = (Button) rootView.findViewById(R.id.btn_agree);
        btnDemand = (Button) rootView.findViewById(R.id.btn_demand);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_energy;
    }

    @Override
    public void fetchData() {
        bufuseEnergy();
    }


    class EnergyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appReceiveVoList.size();

        }

        @Override
        public EnergyResponse.DataBean.UserEnergyListBean.AppReceiveVoListBean getItem(int position) {
            return appReceiveVoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.list_item_energy, null);
                holder = new ViewHolder();
                holder.ivHeader = (RoundImageView) convertView
                        .findViewById(R.id.iv_header);
                holder.tvUserName = (TextView) convertView
                        .findViewById(R.id.tv_user_name);
                holder.tvEnergy = (TextView) convertView
                        .findViewById(R.id.tv_energy);
                holder.btnAgree = (Button) convertView
                        .findViewById(R.id.btn_agree);
                holder.ivDelete = (ImageView) convertView
                        .findViewById(R.id.iv_delete);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final EnergyResponse.DataBean.UserEnergyListBean.AppReceiveVoListBean item = getItem
                    (position);

            holder.tvUserName.setText(item.nickname + "");

            if (item.requestEnergyType == 1) {
                holder.tvEnergy.setText("请您赠送一个精力瓶");
                holder.btnAgree.setText("同意");
            } else {
                holder.btnAgree.setText("领取");
                holder.tvEnergy.setText("送您一个精力瓶");
            }

            if (!TextUtils.isEmpty(item.portrait)) {
                Glide.with(mContext.getApplicationContext()).load(item.portrait).placeholder(R
                        .drawable
                        .placeholder_gray).into(holder.ivHeader);
            }
            holder.ivHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FriendInfoActivity.class);
                    intent.putExtra("dtUserId", item.dtUserId + "");
                    startActivity(intent);
                }
            });
            holder.btnAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    argeeList = new ArrayList<Integer>();
                    argeeList.add(item.energyRequestId);
                    agree(argeeList, position);
                }
            });
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refuse(item.energyRequestId, position);
                }
            });

            return convertView;
        }
    }

    private void refuse(int energyRequestId, final int position) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);

        final RefuseEnergyService service = new RefuseEnergyService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<RefuseEnergyResponse>() {
            @Override
            public void onGetData(RefuseEnergyResponse data) {
                loadingDialog.dismissDialog();
                UIUtils.showToast(mContext, "已拒绝");
                appReceiveVoList.remove(position);
                if (appReceiveVoList.size() == 0) {
                    rlHave.setVisibility(View.GONE);
                    btnAgree.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);
                    btnDemand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            demandEnergy();
                        }
                    });
                    if (msgChangeListener != null) {
                        msgChangeListener.onMsgChanged(1, -1);
                    }
                } else {
                    if (msgChangeListener != null) {
                        msgChangeListener.onMsgChanged(1, 1);
                    }
                }
                energyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("requestId=" + energyRequestId, false);
    }

    private void agree(final List<Integer> argeeList, final int position) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);

        final AgreeEnergyService service = new AgreeEnergyService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<AgreeEnergyResponse>() {
            @Override
            public void onGetData(AgreeEnergyResponse data) {
                loadingDialog.dismissDialog();
                UIUtils.showToast(mContext, "已同意");
                if (position == -1) {

                    bufuseEnergy();
                    if (msgChangeListener != null) {
                        msgChangeListener.onMsgChanged(1, -1);
                    }
                } else {
                    appReceiveVoList.remove(position);
                    if (appReceiveVoList.size() == 0) {
                        rlHave.setVisibility(View.GONE);
                        btnAgree.setVisibility(View.GONE);
                        rlEmpty.setVisibility(View.VISIBLE);
                        btnDemand.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                demandEnergy();
                            }
                        });
                        if (msgChangeListener != null) {
                            msgChangeListener.onMsgChanged(1, -1);
                        }
                    } else {
                        if (msgChangeListener != null) {
                            msgChangeListener.onMsgChanged(1, 1);
                        }
                    }
                }
                energyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("requestId=" + argeeList.toString(), false);
    }

    static class ViewHolder {
        TextView       tvEnergy;
        TextView       tvUserName;
        RoundImageView ivHeader;
        ImageView      ivDelete;
        Button         btnAgree;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setMsgChangeListener(MsgChangeListener msgChangeListener) {
        this.msgChangeListener = msgChangeListener;
    }
}
