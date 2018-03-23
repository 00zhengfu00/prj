package com.physicmaster.modules.study.fragment;

/**
 * Created by huashigen on 2017-06-28.
 */

public class VideoBean {
    public static final int TYPE_UNLOCK = 0;
    public static final int TYPE_UNLIGHT = 1;
    public static final int TYPE_LINGTH1 = 2;
    public static final int TYPE_LINGTH2 = 3;
    public static final int TYPE_TITLE = 4;
    public static final int POSITION_LEFT = 1;
    public static final int POSITION_RIGHT = 0;
    public int type;
    public int position = 0;
    public String title = "";
}
