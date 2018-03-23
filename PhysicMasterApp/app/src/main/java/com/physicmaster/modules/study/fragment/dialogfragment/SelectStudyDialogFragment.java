package com.physicmaster.modules.study.fragment.dialogfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.modules.study.activity.exercise.ExcerciseV2Activity;
import com.physicmaster.modules.videoplay.VideoPlayV2Activity;

/**
 * Created by songrui on 16/11/7.
 */

public class SelectStudyDialogFragment extends DialogFragment {


    private View mView;
    private FragmentActivity mContext;

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

        mView = inflater.inflate(R.layout.dialog_fragment_select_study, null);

        mContext = getActivity();

        Bundle bundle = getArguments();
        final String title = bundle.getString("title");
        final String title1 = bundle.getString("title1");
        final String title2 = bundle.getString("title2");
        final int videoId = bundle.getInt("videoId");
        final String chapterId = bundle.getString("chapterId");
        //TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        TextView tvVideo = (TextView) mView.findViewById(R.id.tv_video);
        tvVideo.setText(title1);
        TextView tvExercise = (TextView) mView.findViewById(R.id.tv_exercise);
        tvExercise.setText(title2);
        ImageView ivClose = (ImageView) mView.findViewById(R.id.iv_close);
        //加粗
        //TextPaint paint = tvTitle.getPaint();
        //paint.setFakeBoldText(true);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectStudyDialogFragment.this.dismiss();
            }
        });

        tvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VideoPlayV2Activity.class);
                intent.putExtra("title", title);
                intent.putExtra("videoId", videoId + "");
                intent.putExtra("chapterId", chapterId);
                startActivity(intent);
                dismiss();
            }
        });
        tvExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ExcerciseV2Activity.class);
                intent.putExtra("videoId", videoId);
                intent.putExtra("chapterId", chapterId);
                startActivity(intent);
                dismiss();

            }
        });
        return mView;
    }

}
