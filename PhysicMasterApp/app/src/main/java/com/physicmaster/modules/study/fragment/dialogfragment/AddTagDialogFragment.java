package com.physicmaster.modules.study.fragment.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.physicmaster.R;
import com.physicmaster.utils.UIUtils;

/**
 * Created by huashigen on 2017-07-13.
 */

public class AddTagDialogFragment extends DialogFragment {
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
        rootView = inflater.inflate(R.layout.dialog_fragment_add_dir, container, false);
        Button btnOk = rootView.findViewById(R.id.btn_ok);
        Button btnCancel = rootView.findViewById(R.id.btn_cancel);
        final EditText etTag = rootView.findViewById(R.id.et_tag);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = etTag.getText().toString().trim();
                if (TextUtils.isEmpty(tag)) {
                    UIUtils.showToast(getContext(), "名称不能为空！");
                    return;
                }
                if (tag.length() > 16) {
                    UIUtils.showToast(getContext(), "名称不能超过16个字符！");
                    return;
                }
                if (onActionBtnClickListener != null) {
                    onActionBtnClickListener.onLick(tag);
                }
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
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
        public void onLick(String content);
    }
}
