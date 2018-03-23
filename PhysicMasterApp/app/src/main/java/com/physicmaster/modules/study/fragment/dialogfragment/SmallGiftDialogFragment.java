package com.physicmaster.modules.study.fragment.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.physicmaster.R;


/**
 * Created by songrui on 16/11/7.
 */

public class SmallGiftDialogFragment extends DialogFragment {


    private View mView;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_fragment_gift_small, null);

        Button btnCollect = (Button) mView.findViewById(R.id.btn_collect);
        ImageView ivClose = (ImageView) mView.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmallGiftDialogFragment.this.dismiss();
            }
        });

        return mView;
    }

}
