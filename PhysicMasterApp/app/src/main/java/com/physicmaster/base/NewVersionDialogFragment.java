package com.physicmaster.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.physicmaster.R;

/**
 * Created by songrui on 16/12/28.
 */

public class NewVersionDialogFragment extends DialogFragment implements View.OnClickListener {

    private FragmentActivity mContext;
    private View mView;
    private TextView tvTitle;
    private TextView tvExit;
    private TextView tvContent;
    private TextView tvCancle;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        mContext = getActivity();

        mView = inflater.inflate(R.layout.dialog_fragment_exit, null);
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        tvExit = (TextView) mView.findViewById(R.id.tv_exit);
        tvCancle = (TextView) mView.findViewById(R.id.tv_cancle);
        tvContent = (TextView) mView.findViewById(R.id.tv_content);

        Bundle bundleContent = getArguments();
        String title = null;
        String content = null;
        if (bundleContent != null) {
            title = bundleContent.getString("title");
            content = bundleContent.getString("content");
        }
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
            tvContent.setVisibility(View.VISIBLE);
        }

        tvExit.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        return mView;
    }

    private OnClickListener onexit;

    public void setExitListener(OnClickListener onexit) {
        this.onexit = onexit;
    }


    public interface OnClickListener {
        void ok();

        void cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit:
                onexit.ok();
                NewVersionDialogFragment.this.dismiss();
                break;
            case R.id.tv_cancle:
                NewVersionDialogFragment.this.dismiss();
                onexit.cancel();
                break;
        }
    }
}
