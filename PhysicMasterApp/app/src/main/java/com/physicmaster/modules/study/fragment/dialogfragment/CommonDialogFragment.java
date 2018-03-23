package com.physicmaster.modules.study.fragment.dialogfragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.physicmaster.R;

/**
 * Created by huashigen on 2017-07-13.
 */

public class CommonDialogFragment extends DialogFragment {
    private OnActionBtnClickListener onActionBtnClickListener;
    private OnActionBtnClickListener onActionBtnClickListener2;
    private View rootView;

    public void setOnActionBtnClickListener(OnActionBtnClickListener onActionBtnClickListener) {
        this.onActionBtnClickListener = onActionBtnClickListener;
    }

    public void setOnActionBtnClickListener2(OnActionBtnClickListener onActionBtnClickListener2) {
        this.onActionBtnClickListener2 = onActionBtnClickListener2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_fragment_common, container, false);
        Button btnBuy = (Button) rootView.findViewById(R.id.btn_buy);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString("title");
            if (!TextUtils.isEmpty(title)) {
                tvTitle.setText(title);
            }
            String note = bundle.getString("note");
            if (!TextUtils.isEmpty(note)) {
                TextView tvNote = (TextView) rootView.findViewById(R.id.tv_note);
                tvNote.setText(note);
            }
            String action = bundle.getString("action");
            if (!TextUtils.isEmpty(action)) {
                btnBuy.setText(action);
            }
            boolean showAction2 = bundle.getBoolean("showAction2");
            if (showAction2) {
                TextView tvTrial = (TextView) rootView.findViewById(R.id.tv_trial);
                tvTrial.setVisibility(View.VISIBLE);
                tvTrial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onActionBtnClickListener2 != null) {
                            onActionBtnClickListener2.onLick();
                        }
                        dismiss();
                    }
                });
                tvTrial.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            }
        }
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionBtnClickListener != null) {
                    onActionBtnClickListener.onLick();
                }
                dismiss();
            }
        });

        rootView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes()
                .height);
    }

    public interface OnActionBtnClickListener {
        public void onLick();
    }
}
