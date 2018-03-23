package com.physicmaster.modules.study.fragment.infromation;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
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
import com.physicmaster.modules.study.activity.MsgChangeListener;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.game.DeleteFreshResponse;
import com.physicmaster.net.response.game.FreshResponse;
import com.physicmaster.net.service.game.DeleteFreshService;
import com.physicmaster.net.service.game.FreshService;
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
 * 开发时间 : 2016/10/18 13:31
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class FreshFragment extends BaseFragment2 {

    private FragmentActivity mContext;
    private ListView lvFresh;
    private RelativeLayout rlEmpty;
    private Button btnAgree;
    private List<FreshResponse.DataBean.FreshNewListBean> mFreshList;
    private List<Integer> deleteList;
    private FreshAdapter freshAdapter;

    private MsgChangeListener msgChangeListener;

    private void updateFresh() {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);

        final FreshService service = new FreshService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<FreshResponse>() {
            @Override
            public void onGetData(FreshResponse data) {
                loadingDialog.dismissDialog();
                mFreshList = data.data.freshNewList;
                if (null == mFreshList || mFreshList.size() == 0) {
                    rlEmpty.setVisibility(View.VISIBLE);
                    lvFresh.setVisibility(View.GONE);
                    btnAgree.setVisibility(View.GONE);
                } else {
                    btnAgree.setVisibility(View.VISIBLE);
                    lvFresh.setVisibility(View.VISIBLE);
                    rlEmpty.setVisibility(View.GONE);
                    freshAdapter = new FreshAdapter();
                    lvFresh.setAdapter(freshAdapter);
                    btnAgree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteList = new ArrayList<Integer>();
                            int FreshListSize = mFreshList.size();
                            for (int i = 0; i < FreshListSize; i++) {
                                deleteList.add(mFreshList.get(i).newsid);
                            }
                            deleteFresh(deleteList, -1);
                        }
                    });
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
                rlEmpty.setVisibility(View.VISIBLE);
                lvFresh.setVisibility(View.GONE);
                btnAgree.setVisibility(View.GONE);
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
    protected void initView(View view) {
        mContext = getActivity();

        lvFresh = (ListView) rootView.findViewById(R.id.lv_fresh);
        btnAgree = (Button) rootView.findViewById(R.id.btn_agree);
        rlEmpty = (RelativeLayout) rootView.findViewById(R.id.rl_empty);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fresh;
    }

    @Override
    public void fetchData() {
        updateFresh();
    }

    class FreshAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFreshList.size();
        }

        @Override
        public FreshResponse.DataBean.FreshNewListBean getItem(int position) {
            return mFreshList.get(position);
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
                        R.layout.list_item_fresh, null);
                holder = new ViewHolder();
                holder.ivHeader = (RoundImageView) convertView
                        .findViewById(R.id.iv_header);
                holder.tvTime = (TextView) convertView
                        .findViewById(R.id.tv_time);
                holder.tvUserName = (TextView) convertView
                        .findViewById(R.id.tv_user_name);
                holder.tvEnergy = (TextView) convertView
                        .findViewById(R.id.tv_energy);
                holder.ivDelete = (ImageView) convertView
                        .findViewById(R.id.iv_delete);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final FreshResponse.DataBean.FreshNewListBean item = getItem(position);
            if (!TextUtils.isEmpty(item.portrait + "")) {
                Glide.with(mContext).load(item.portrait).placeholder(R.drawable
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
            holder.tvUserName.setText(item.nickname + "");
            holder.tvEnergy.setText(item.title + "");
            holder.tvTime.setText(item.createTime + "");
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteList = new ArrayList<Integer>();
                    deleteList.add(item.newsid);
                    deleteFresh(deleteList, position);
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvEnergy;
        TextView tvUserName;
        RoundImageView ivHeader;
        TextView tvTime;
        ImageView ivDelete;
    }

    private void deleteFresh(List<Integer> deleteList, final int position) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);

        final DeleteFreshService service = new DeleteFreshService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<DeleteFreshResponse>() {
            @Override
            public void onGetData(DeleteFreshResponse data) {
                loadingDialog.dismissDialog();
                UIUtils.showToast(mContext, "已忽略");
                if (position == -1) {
                    // mFreshList.clear();
                    rlEmpty.setVisibility(View.VISIBLE);
                    lvFresh.setVisibility(View.GONE);
                    btnAgree.setVisibility(View.GONE);
                    if (msgChangeListener != null) {
                        msgChangeListener.onMsgChanged(0, -1);
                    }
                } else {
                    mFreshList.remove(position);
                    if (mFreshList.size() == 0) {
                        rlEmpty.setVisibility(View.VISIBLE);
                        lvFresh.setVisibility(View.GONE);
                        btnAgree.setVisibility(View.GONE);
                        if (msgChangeListener != null) {
                            msgChangeListener.onMsgChanged(0, -1);
                        }
                    } else {
                        if (msgChangeListener != null) {
                            msgChangeListener.onMsgChanged(0, 1);
                        }
                    }
                }
                freshAdapter.notifyDataSetChanged();
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
        service.postLogined("newsIdsStr=" + deleteList.toString(), false);
    }

    public void setMsgChangeListener(MsgChangeListener msgChangeListener) {
        this.msgChangeListener = msgChangeListener;
    }

}
