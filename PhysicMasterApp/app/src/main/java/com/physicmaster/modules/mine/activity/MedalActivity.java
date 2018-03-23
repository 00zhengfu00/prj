package com.physicmaster.modules.mine.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.modules.mine.fragment.dialogFragment.MedalNoDialogFragment;
import com.physicmaster.modules.mine.fragment.dialogFragment.MedalYesDialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.GetMedalListResponse;
import com.physicmaster.net.service.user.GetMedalListService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class MedalActivity extends BaseActivity {

    private ExpandableListView                            expandableListView;
    private List<GetMedalListResponse.DataBean.LevelBean> level0;
    private List<GetMedalListResponse.DataBean.LevelBean> level1;
    private List<GetMedalListResponse.DataBean.LevelBean> level2;
    private List<GetMedalListResponse.DataBean.LevelBean> level3;

    @Override
    protected void findViewById() {

        expandableListView = (ExpandableListView) findViewById(R.id.id_elv);
        initTitle();
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("我的勋章");
    }

    @Override
    protected void initView() {
        getMedalList();

    }

    private void getMedalList() {
        GetMedalListService service = new GetMedalListService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetMedalListResponse>() {
            @Override
            public void onGetData(GetMedalListResponse data) {
                // 分组
                List<String> groupList = new ArrayList<>();
                groupList.add("分组1");
                groupList.add("分组2");
                groupList.add("分组3");
                groupList.add("分组4");

                // 每个分组下的每个子项的 GridView 数据集合
                level0 = data.data.level0;
                level1 = data.data.level1;
                level2 = data.data.level2;
                level3 = data.data.level3;

                // 所有分组的所有子项的 GridView 数据集合

                List<List<GetMedalListResponse.DataBean.LevelBean>> itemList = new ArrayList<>();
                itemList.add(level0);
                itemList.add(level1);
                itemList.add(level2);
                itemList.add(level3);
                // 创建适配器
                MyExpandableListViewAdapter adapter = new MyExpandableListViewAdapter(MedalActivity.this,
                        groupList, itemList);
                expandableListView.setAdapter(adapter);
                // 默认展开
                for (int i = 0; i < groupList.size(); i++) {
                    expandableListView.expandGroup(i);
                }
                expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return true;
                    }
                });
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(MedalActivity.this, errorMsg);
            }
        });
        service.postLogined("", false);
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_medal;
    }

    public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

        private Context mContext;

        /**
         * 每个分组的名字的集合
         */
        private List<String> groupList;

        /**
         * 所有分组的所有子项的 GridView 数据集合
         */
        private List<List<GetMedalListResponse.DataBean.LevelBean>> itemList;

        private GridView gridView;

        public MyExpandableListViewAdapter(Context context, List<String> groupList,
                                           List<List<GetMedalListResponse.DataBean.LevelBean>> itemList) {
            mContext = context;
            this.groupList = groupList;
            this.itemList = itemList;
        }

        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return itemList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
                parent) {
            if (null == convertView) {
                convertView = View.inflate(mContext, R.layout.expandablelist_group, null);
            }
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View
                convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(mContext, R.layout.expandablelist_item, null);
            }
            gridView = (GridView) convertView;
            // 创建 GridView 适配器
            MyGridViewAdapter gridViewAdapter = new MyGridViewAdapter(mContext, itemList.get
                    (groupPosition));
            gridView.setAdapter(gridViewAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (groupPosition == 0) {
                        GetMedalListResponse.DataBean.LevelBean level1Bean = level0.get(position);
                        if (level1Bean.isClaimed == 0) {
                            MedalNoDialogFragment medalNoDialogFragment = new MedalNoDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("levelBean",level1Bean);
                            medalNoDialogFragment.setArguments(bundle);
                            medalNoDialogFragment.show(MedalActivity.this.getSupportFragmentManager(),"levelBean");
                        } else if (level1Bean.isClaimed == 1) {
                            MedalYesDialogFragment medalYesDialogFragment = new MedalYesDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("levelBean",level1Bean);
                            medalYesDialogFragment.setArguments(bundle);
                            medalYesDialogFragment.show(MedalActivity.this.getSupportFragmentManager(),"levelBean");
                        }

                    } else if (groupPosition == 1) {
                        GetMedalListResponse.DataBean.LevelBean level2Bean = level1.get(position);
                        if (level2Bean.isClaimed == 0) {
                            MedalNoDialogFragment medalNoDialogFragment = new MedalNoDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("levelBean",level2Bean);
                            medalNoDialogFragment.setArguments(bundle);
                            medalNoDialogFragment.show(MedalActivity.this.getSupportFragmentManager(),"levelBean");
                        } else if (level2Bean.isClaimed == 1) {
                            MedalYesDialogFragment medalYesDialogFragment = new MedalYesDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("levelBean",level2Bean);
                            medalYesDialogFragment.setArguments(bundle);
                            medalYesDialogFragment.show(MedalActivity.this.getSupportFragmentManager(),"levelBean");
                        }
                    } else if (groupPosition == 2) {
                        GetMedalListResponse.DataBean.LevelBean level3Bean = level2.get(position);
                        if (level3Bean.isClaimed == 0) {
                            MedalNoDialogFragment medalNoDialogFragment = new MedalNoDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("levelBean",level3Bean);
                            medalNoDialogFragment.setArguments(bundle);
                            medalNoDialogFragment.show(MedalActivity.this.getSupportFragmentManager(),"levelBean");
                        } else if (level3Bean.isClaimed == 1) {
                            MedalYesDialogFragment medalYesDialogFragment = new MedalYesDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("levelBean",level3Bean);
                            medalYesDialogFragment.setArguments(bundle);
                            medalYesDialogFragment.show(MedalActivity.this.getSupportFragmentManager(),"levelBean");
                        }
                    } else if (groupPosition == 3) {
                        GetMedalListResponse.DataBean.LevelBean level4Bean = level3.get(position);
                        if (level4Bean.isClaimed == 0) {
                            MedalNoDialogFragment medalNoDialogFragment = new MedalNoDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("levelBean",level4Bean);
                            medalNoDialogFragment.setArguments(bundle);
                            medalNoDialogFragment.show(MedalActivity.this.getSupportFragmentManager(),"levelBean");
                        } else if (level4Bean.isClaimed == 1) {
                            MedalYesDialogFragment medalYesDialogFragment = new MedalYesDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("levelBean",level4Bean);
                            medalYesDialogFragment.setArguments(bundle);
                            medalYesDialogFragment.show(MedalActivity.this.getSupportFragmentManager(),"levelBean");
                        }
                    }
                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }


    /**
     * GridView 适配器
     */
    public class MyGridViewAdapter extends BaseAdapter {

        private Context mContext;

        /**
         * 每个分组下的每个子项的 GridView 数据集合
         */
        private List<GetMedalListResponse.DataBean.LevelBean> itemGridList;

        public MyGridViewAdapter(Context mContext, List<GetMedalListResponse.DataBean.LevelBean> itemGridList) {
            this.mContext = mContext;
            this.itemGridList = itemGridList;
        }

        @Override
        public int getCount() {
            return itemGridList.size();
        }

        @Override
        public GetMedalListResponse.DataBean.LevelBean getItem(int position) {
            return itemGridList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemViewHolder holder;
            if (null == convertView) {
                convertView = View.inflate(mContext, R.layout.gridview_item, null);
                holder = new ItemViewHolder();
                holder.tvGridView = (TextView) convertView.findViewById(R.id.tv_gridview);
                holder.ivGridView = (ImageView) convertView.findViewById(R.id.iv_gridview);
                convertView.setTag(holder);
            } else {
                holder = (ItemViewHolder) convertView.getTag();
            }
            GetMedalListResponse.DataBean.LevelBean item = getItem(position);
            if (item.isClaimed == 0) {
                Glide.with(MedalActivity.this).load(item.medalImgBlack).into(holder.ivGridView);
            } else if (item.isClaimed == 1) {
                Glide.with(MedalActivity.this).load(item.medalImg).into(holder.ivGridView);
            }
            holder.tvGridView.setText(item.medalName + "");
            return convertView;
        }
    }

    static class ItemViewHolder {
        TextView  tvGridView;
        ImageView ivGridView;
    }
}
