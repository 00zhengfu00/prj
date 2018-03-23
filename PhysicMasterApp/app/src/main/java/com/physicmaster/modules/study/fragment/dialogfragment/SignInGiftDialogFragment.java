package com.physicmaster.modules.study.fragment.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.net.response.account.CheckInDayPlan;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse;


/**
 * Created by songrui on 16/11/7.
 */

public class SignInGiftDialogFragment extends DialogFragment {
    private View mView;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_fragment_box_section, null);
        Button btnCollect = (Button) mView.findViewById(R.id.btn_collect);
        ImageView ivClose = (ImageView) mView.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInGiftDialogFragment.this.dismiss();
            }
        });
        Button ivAward0 = (Button) mView.findViewById(R.id.iv_award0);
        Button ivAward1 = (Button) mView.findViewById(R.id.iv_award1);
        ImageView ivAward2 = (ImageView) mView.findViewById(R.id.iv_award2);
        TextView tvAward2 = (TextView) mView.findViewById(R.id.tv_props);
        TextView tvDesc = (TextView) mView.findViewById(R.id.tv_desc);
        String desc = getArguments().getString("desc");
        if (!TextUtils.isEmpty(desc)) {
            tvDesc.setText(desc);
        } else {
            tvDesc.setVisibility(View.GONE);
        }
        CheckInDayPlan data = getArguments().getParcelable("data");
        if (data.awardGoldCoin != 0) {
            ivAward0.setBackgroundResource(R.mipmap.award_coin);
            ivAward0.setText(data.awardGoldCoin + "");
        } else {
            ivAward0.setVisibility(View.GONE);
        }
        if (data.awardPoint != 0) {
            ivAward1.setBackgroundResource(R.mipmap.award_integral);
            ivAward1.setText(data.awardPoint + "");
        } else {
            ivAward1.setVisibility(View.GONE);
        }
        if (data.awardProp1Count != 0) {
            Glide.with(this).load(data.awardProp1ImgUrl).into(ivAward2);
            tvAward2.setText(data.awardProp1Count + "");
        } else {
            mView.findViewById(R.id.rl_award2).setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivAward1.getLayoutParams();
            layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.dimen_40), 0, 0);
            ivAward1.setLayoutParams(layoutParams);
        }
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGiftDialogFragment.this.dismiss();
            }
        });
        return mView;
    }

}
