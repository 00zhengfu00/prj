package com.physicmaster.modules.study.fragment.dialogfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.modules.WebviewActivity;
import com.physicmaster.utils.ScreenUtils;


/**
 * Created by songrui on 16/11/7.
 */

public class AdvertiseDialogFragment extends DialogFragment {


    private View mView;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_fragment_advertise, null);

        ImageView ivClose = (ImageView) mView.findViewById(R.id.iv_close);
        ImageView ivAd = (ImageView) mView.findViewById(R.id.iv_ad);
        int maxHeight = ScreenUtils.get3_4ImageMaxHeight(getActivity(), 40);
        ivAd.setAdjustViewBounds(true);
        ivAd.setMaxHeight(maxHeight);
        ivAd.setScaleType(ImageView.ScaleType.FIT_XY);

        String imgUrl = getArguments().getString("imgUrl");
        final String destUrl = getArguments().getString("destUrl");
        Glide.with(this).load(imgUrl).placeholder(R.drawable.white_background).into(ivAd);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdvertiseDialogFragment.this.dismiss();
            }
        });
        ivAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdvertiseDialogFragment.this.dismiss();
                if (!TextUtils.isEmpty(destUrl)) {
                    Intent intent = new Intent(getActivity(), WebviewActivity.class);
                    intent.putExtra("url", destUrl);
                    startActivity(intent);
                }
            }
        });
        return mView;
    }
}
