package com.physicmaster.modules.mine.fragment.dialogFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.net.response.user.GetMedalListResponse;


/**
 * Created by songrui on 16/11/7.
 */

public class MedalNoDialogFragment extends DialogFragment {


    private View mView;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mView = inflater.inflate(R.layout.dialog_fragment_medal_no, container,false);

        Bundle bundle = getArguments();
        GetMedalListResponse.DataBean.LevelBean levelBean = (GetMedalListResponse.DataBean.LevelBean) bundle.getSerializable("levelBean");

        TextView tvName = (TextView) mView.findViewById(R.id.tv_name);
        TextView tvDesc = (TextView) mView.findViewById(R.id.tv_desc);
        ImageView ivClose = (ImageView) mView.findViewById(R.id.iv_close);
        ImageView ivMedal = (ImageView) mView.findViewById(R.id.iv_medal);
        //加粗
        TextPaint tp = tvName.getPaint();
        tp.setFakeBoldText(true);
        tvName.setText(levelBean.medalName + "");
        tvDesc.setText(levelBean.medalDesc + "");
        if (!TextUtils.isEmpty(levelBean.medalImgBlack)) {
            Glide.with(getActivity()).load(levelBean.medalImgBlack).into(ivMedal);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedalNoDialogFragment.this.dismiss();
            }
        });

        return mView;
    }

}
