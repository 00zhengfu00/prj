package com.lswuyou.tv.pm.leanback;

import android.support.v17.leanback.widget.Presenter;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by huashigen on 2017-09-04.
 */

public class CursorViewHolder extends Presenter.ViewHolder {
    public ImageView cursorView;

    public CursorViewHolder(View view) {
        super(view);
    }

    public CursorViewHolder(View view, ImageView cursor) {
        super(view);
        cursorView = cursor;
    }
}
