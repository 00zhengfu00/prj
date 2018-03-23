package com.physicmaster.modules.mine.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.excercise.DeleteMemoResponse;
import com.physicmaster.net.response.excercise.GetMemoResponse;
import com.physicmaster.net.service.excercise.DeleteMemoService;
import com.physicmaster.net.service.excercise.GetMemoService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends BaseActivity {


    private static final String TAG = "MemoActivity";
    private ListView lvMemo;

    private MemoAdapter                                     memoAdapter;
    private int                                             mPosition;
    private List<GetMemoResponse.DataBean.UserMemoListBean> mUserMemoList;
    private int                                             nextPageMemoId;
    private View                                            footer;
    private int                                             firstVisibleItemR;
    private int                                             visibleItemCountR;
    private SwipeRefreshLayout                              container;
    private RelativeLayout                                  rlEmpty;

    @Override
    protected void findViewById() {

        lvMemo = (ListView) findViewById(R.id.lv_memo);
        container = (SwipeRefreshLayout) findViewById(R.id.container);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_empty);
        footer = View.inflate(this, R.layout.footer_view, null);
        lvMemo.addFooterView(footer);
        mUserMemoList = new ArrayList<GetMemoResponse.DataBean.UserMemoListBean>();
        container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ShowMemo();
                setFooterStatus(false);
            }
        });
        initTitle();

    }

    private void initTitle() {
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("我的备忘");
    }

    @Override
    protected void initView() {
        ShowMemo();

        lvMemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                String memoId = mUserMemoList.get(mPosition).memoId;
                remove4service(memoId);

            }
        });


        lvMemo.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当滑动到底部时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && firstVisibleItemR != 0 && (firstVisibleItemR + visibleItemCountR) >= mUserMemoList.size()) {
                    updateMemo();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                firstVisibleItemR = firstVisibleItem;
                visibleItemCountR = visibleItemCount;
                if (footer != null) {
                    //判断可视Item是否能在当前页面完全显示
                    if (visibleItemCount == totalItemCount) {
                        // removeFooterView(footerView);
                        footer.setVisibility(View.GONE);//隐藏底部布局
                    } else {
                        // addFooterView(footerView);
                        footer.setVisibility(View.VISIBLE);//显示底部布局
                    }
                }
            }
        });

    }

    private void updateMemo() {

        final GetMemoService service = new GetMemoService(MemoActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GetMemoResponse>() {

            @Override
            public void onGetData(GetMemoResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                // Toast.makeText(MemoActivity.this, "获取成功" , Toast.LENGTH_SHORT).show();
                footer.setVisibility(View.GONE);
                List<GetMemoResponse.DataBean.UserMemoListBean> memoList = data.data.userMemoList;
                mUserMemoList.addAll(memoList);
                nextPageMemoId = data.data.nextPageMemoId;
                if (nextPageMemoId == 0) {
                    setFooterStatus(false);
                }
                if (data.data.userMemoList != null && data.data.userMemoList.size() > 0) {
                    mUserMemoList.addAll(data.data.userMemoList);
                    nextPageMemoId = data.data.nextPageMemoId;
                } else {
                    setFooterStatus(true);
                }
                memoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(MemoActivity.this, errorMsg);
                footer.setVisibility(View.GONE);
            }
        });

        service.postLogined("maxMemoId=" + nextPageMemoId, false);

    }


    private void ShowMemo() {
//        container.setRefreshing(true);

        final GetMemoService service = new GetMemoService(MemoActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GetMemoResponse>() {

            @Override
            public void onGetData(GetMemoResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                mUserMemoList.clear();
                mUserMemoList.addAll(data.data.userMemoList);
                if (null == mUserMemoList || mUserMemoList.size() == 0) {
                    rlEmpty.setVisibility(View.VISIBLE);
                    container.setVisibility(View.GONE);
                } else {
                    rlEmpty.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);

                    nextPageMemoId = data.data.nextPageMemoId;
                    memoAdapter = new MemoAdapter();
                    lvMemo.setAdapter(memoAdapter);
                    container.setRefreshing(false);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(MemoActivity.this, errorMsg);
                rlEmpty.setVisibility(View.VISIBLE);
                container.setVisibility(View.GONE);
                container.setRefreshing(false);
            }
        });
        service.postLogined("", false);
    }

    private void remove4service(String memoId) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final DeleteMemoService service = new DeleteMemoService(MemoActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<DeleteMemoResponse>() {


            @Override
            public void onGetData(DeleteMemoResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                loadingDialog.dismissDialog();
                mUserMemoList.remove(mPosition);
                memoAdapter.notifyDataSetChanged();
                UIUtils.showToast(MemoActivity.this, "删除成功");

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(MemoActivity.this, errorMsg);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("memoId=" + memoId, false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_memo;
    }


    class MemoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mUserMemoList.size();
        }

        @Override
        public GetMemoResponse.DataBean.UserMemoListBean getItem(int position) {
            return mUserMemoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MemoActivity.this,
                        R.layout.list_item_memo, null);
                holder = new ViewHolder();
                holder.tvKnowledge = (TextView) convertView
                        .findViewById(R.id.tv_knowledge);
                holder.tvTime = (TextView) convertView
                        .findViewById(R.id.tv_time);
                holder.tvContent = (TextView) convertView
                        .findViewById(R.id.tv_content);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            GetMemoResponse.DataBean.UserMemoListBean item = getItem(position);


            holder.tvKnowledge.setText(item.knowledgeName + "");
            holder.tvTime.setText(item.createTime + "");
            String content = item.content;
            holder.tvContent.setText(Html.fromHtml(content));

            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvTime;
        TextView tvContent;
        TextView tvKnowledge;

    }

    private void setFooterStatus(boolean finished) {
        TextView tvNoMore = (TextView) footer.findViewById(R.id.tv_loading);
        ProgressBar bar = (ProgressBar) footer.findViewById(R.id.progress_loading);
        if (finished) {
            tvNoMore.setText("没有更多了");
            bar.setVisibility(View.GONE);
        } else {
            tvNoMore.setText("正在加载……");
            bar.setVisibility(View.VISIBLE);
        }
    }
}
