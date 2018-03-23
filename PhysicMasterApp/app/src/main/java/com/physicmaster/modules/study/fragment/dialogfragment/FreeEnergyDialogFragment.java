package com.physicmaster.modules.study.fragment.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.game.FreeEnergyResponse;
import com.physicmaster.net.response.game.RequestEnergyResponse;
import com.physicmaster.net.service.game.FreeEnergyService;
import com.physicmaster.net.service.game.RequestEnergyService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import static com.physicmaster.widget.PickerView.TAG;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/17 15:48
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class FreeEnergyDialogFragment extends DialogFragment {

    private View mView;

    private boolean isSelected[];

    private FragmentActivity                                              mContext;
    private GridView                                                      gvFriend;
    private List<FreeEnergyResponse.DataBean.AppUserFriendInfoVoListBean> mFriendList;
    private Button                                                        btnRequest;
    private List<Integer> userIds = new ArrayList<>();
    private FriendAdapter friendAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes()
                .height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.free_dialog_fragment_energy, null);

        ImageView ivClose = (ImageView) mView.findViewById(R.id.iv_close);
        gvFriend = (GridView) mView.findViewById(R.id.gv_friend);
        btnRequest = (Button) mView.findViewById(R.id.btn_request);

        showFriend();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FreeEnergyDialogFragment.this.dismiss();
            }
        });
        //        gvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //                ImageView ivChecked = (ImageView) view.findViewById(R.id.iv_checked);
        //                if (isSelected[position]) {
        //                    ivChecked.setVisibility(View.GONE);
        //                    isSelected[position] = false;
        //                    if (userIds.contains(mFriendList.get(position).dtUserId)) {
        //                        userIds.remove((Integer) mFriendList.get(position).dtUserId);
        //                    }
        //                } else {
        //                    ivChecked.setVisibility(View.VISIBLE);
        //                    isSelected[position] = true;
        //                    if (!userIds.contains(mFriendList.get(position).dtUserId)) {
        //                        userIds.add(mFriendList.get(position).dtUserId);
        //                    }
        //                }
        //
        //            }
        //        });

        return mView;
    }

    private void requestEnergy() {
        if (userIds.size() == 0) {
            UIUtils.showToast(mContext, "请选择好友");
            return;
        }
        final RequestEnergyService service = new RequestEnergyService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<RequestEnergyResponse>() {

            @Override
            public void onGetData(RequestEnergyResponse data) {
                Log.d(TAG, "加载完成：onGetData: " + data.msg);
                UIUtils.showToast(mContext, "已经发送给你的好友啦");
                FreeEnergyDialogFragment.this.dismiss();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "加载失败：onGetData: " + errorMsg.toString());
                FreeEnergyDialogFragment.this.dismiss();
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("userIds=" + userIds.toString(), false);
    }


    private void showFriend() {
        final FreeEnergyService service = new FreeEnergyService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<FreeEnergyResponse>() {

            @Override
            public void onGetData(FreeEnergyResponse data) {
                Log.d(TAG, "加载完成：onGetData: " + data.msg);
                mFriendList = data.data.appUserFriendInfoVoList;
                if (null == mFriendList || mFriendList.size() == 0) {
                    btnRequest.setEnabled(false);
                    UIUtils.showToast(mContext, "没有可以索要精力的好友哦");
                    if (friendAdapter != null) {
                        friendAdapter.notifyDataSetChanged();
                    }
                } else {
                    btnRequest.setEnabled(true);
//                    isSelected = new boolean[mFriendList.size()];
//                    for (int i = 0; i < mFriendList.size(); i++) {
//                        isSelected[i] = false;
//                    }
                    friendAdapter = new FriendAdapter();
                    gvFriend.setAdapter(friendAdapter);

                    btnRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestEnergy();
                        }
                    });
                }

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "加载失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("", false);
    }

    class FriendAdapter extends BaseAdapter {

        public List<Boolean> isSelect;

        public FriendAdapter() {
            isSelect = new ArrayList<>();
            initData();

        }

        private void initData() {
            for (int i = 0; i < mFriendList.size(); i++) {
                isSelect.add(i,false);
            }
        }

        @Override
        public int getCount() {
            return mFriendList == null ? 0 : mFriendList.size();
        }

        @Override
        public FreeEnergyResponse.DataBean.AppUserFriendInfoVoListBean getItem(int position) {
            return mFriendList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(),
                        R.layout.grid_item_friend, null);
                holder = new ViewHolder();
                holder.tvUserName = (TextView) convertView
                        .findViewById(R.id.tv_user_name);
                holder.ivHead = (RoundImageView) convertView
                        .findViewById(R.id.iv_head);
                holder.ivChecked = (ImageView) convertView
                        .findViewById(R.id.iv_checked);
                holder.rlFriend = (RelativeLayout) convertView
                        .findViewById(R.id.rl_friend);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final FreeEnergyResponse.DataBean.AppUserFriendInfoVoListBean item = getItem(position);

            holder.tvUserName.setText(item.nickname + "");
            if (!TextUtils.isEmpty(item.portraitSmall)) {
                Glide.with(mContext).load(item.portraitSmall).placeholder(R.drawable
                        .placeholder_gray).into(holder.ivHead);
            }
            if(isSelect.get(position)) {
                holder.ivChecked.setVisibility(View.VISIBLE);
            } else {
                holder.ivChecked.setVisibility(View.GONE);
            }
            holder.rlFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isSelect.get(position)) {
                        holder.ivChecked.setVisibility(View.GONE);
                        isSelect.set(position,false);
                        if (userIds.contains(item.dtUserId)) {
                            userIds.remove((Integer) item.dtUserId);
                        }
                        notifyDataSetChanged();
                    } else {
                        holder.ivChecked.setVisibility(View.VISIBLE);
                        isSelect.set(position, true);
                        if (!userIds.contains(item.dtUserId)) {
                            userIds.add(item.dtUserId);
                        }
                        notifyDataSetChanged();

                    }
                }
            });

            return convertView;
        }
    }

    static class ViewHolder {
        TextView       tvUserName;
        RoundImageView ivHead;
        ImageView      ivChecked;
        RelativeLayout rlFriend;
    }
}
