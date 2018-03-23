package com.lswuyou.tv.pm.fragment;

/**
 * Created by Administrator on 2016/9/8.
 */
public abstract class BaseIndexFragment extends BaseFragment {
    public static final String COURSE_LIST_UPDATE = "course_list_update";
    public abstract void setInitFocus(int index);
    public abstract void reset();
    public abstract void setSavedFocus();
}
