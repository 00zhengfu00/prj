package com.physicmaster.modules.study.fragment.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.MainActivity;
import com.physicmaster.net.bean.SignInBean;
import com.physicmaster.net.response.account.CheckInDayPlan;

import java.util.ArrayList;


/**
 * Created by songrui on 16/11/7.
 */

public class BoxDialogFragment extends DialogFragment {
    private GridView gvProp;
    private MainActivity mContext;
    private ArrayList<SignInBean> mList;

    public OnGiftGotListener getListener() {
        return listener;
    }

    public void setListener(OnGiftGotListener listener) {
        this.listener = listener;
    }

    private OnGiftGotListener listener;
    private View mView;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mContext = (MainActivity) getContext();

        mView = inflater.inflate(R.layout.dialog_fragment_box, container, false);
        gvProp = (GridView) mView.findViewById(R.id.gv_prop);
        Button btnCollect = (Button) mView.findViewById(R.id.btn_collect);
        ImageView ivClose = (ImageView) mView.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoxDialogFragment.this.dismiss();
            }
        });
        CheckInDayPlan data = getArguments().getParcelable("data");

        mList = new ArrayList();
        if (data.awardGoldCoin != 0) {
            SignInBean signInGold = new SignInBean(data.awardGoldCoin, null, R.mipmap._jinbi_);
            mList.add(signInGold);
        }
        if (data.awardProp1Count != 0) {
            SignInBean signInPorp1 = new SignInBean(data.awardProp1Count, data.awardProp1ImgUrl, 0);
            mList.add(signInPorp1);
        }
        if (data.awardProp2Count != 0) {
            SignInBean signInGold2 = new SignInBean(data.awardProp2Count, data.awardProp2ImgUrl, 0);
            mList.add(signInGold2);
        }
        if (data.awardProp3Count != 0) {
            SignInBean signInGold3 = new SignInBean(data.awardProp3Count, data.awardProp3ImgUrl, 0);
            mList.add(signInGold3);
        }
        SignInAdapter signInAdapter = new SignInAdapter();
        gvProp.setAdapter(signInAdapter);

        btnCollect.setOnClickListener(v -> dismiss());
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes()
                .height);
    }

    class SignInAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public SignInBean getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.grid_item_sign_in, null);
                holder = new ViewHolder();
                holder.ivAward = (ImageView) convertView.findViewById(R.id.iv_award);
                holder.tvProps = (TextView) convertView.findViewById(R.id.tv_props);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SignInBean item = getItem(position);

            if (TextUtils.isEmpty(item.awardPropImgUrl)) {
                Glide.with(mContext).load(item.resourse).placeholder(R.drawable
                        .gray_background).into(holder.ivAward);
            } else {
                Glide.with(mContext).load(item.awardPropImgUrl).placeholder(R.drawable
                        .gray_background).into(holder.ivAward);
            }
            holder.tvProps.setText(item.awardPropCount + "");
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivAward;
        TextView tvProps;
    }

    public interface OnGiftGotListener {
        void onGiftGot();
    }
}
