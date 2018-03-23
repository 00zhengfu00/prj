package com.physicmaster.modules.study.fragment.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.physicmaster.R;

/**
 * Created by huashigen on 2017-07-13.
 */

public class DeleteDirDialogFragment extends DialogFragment {
    private OnActionBtnClickListener onActionBtnClickListener;
    private View rootView;

    public void setOnActionBtnClickListener(OnActionBtnClickListener onActionBtnClickListener) {
        this.onActionBtnClickListener = onActionBtnClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.myDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_fragment_delete_dir, container, false);
        Button btnOk = rootView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> {
            if (onActionBtnClickListener != null) {
                onActionBtnClickListener.onLick();
            }
            dismiss();
        });

        rootView.findViewById(R.id.iv_close).setOnClickListener(v -> dismiss());
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
    }

    public interface OnActionBtnClickListener {
        public void onLick();
    }
}
