package com.physicmaster.modules.mine.activity.question;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.discuss.MessageResponse;
import com.physicmaster.net.service.discuss.LoseMessageService;
import com.physicmaster.net.service.discuss.MessageService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.PullToRefreshLayout;
import com.physicmaster.widget.RoundImageView;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends BaseActivity {


    private ListView lvMessage;
    private List<MessageResponse.DataBean.NewsListBean> mMessagelist;
    private PullToRefreshLayout pullToRefreshLayout;
    private MessageAdapter messageAdapter;
    private List<Integer> newsId = new ArrayList<Integer>();

    @Override
    protected void findViewById() {
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showMessage();
            }

            @Override
            public void onPullup(int maxId) {
                upDataMessage(maxId);
            }
        });
        initTitle();
    }

    private void initTitle() {
        new TitleBuilder(this)
                .setLeftImageRes(R.mipmap.fanhui)
                .setLeftText("返回")
                .setRightText("全部忽略")
                .setRightTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mMessagelist && mMessagelist.size() != 0) {
                            newsId.clear();
                            for (int i = 0; i < mMessagelist.size(); i++) {
                                newsId.add(mMessagelist.get(i).newsId);
                            }
                            loseMessage(newsId);
                        } else {
                            UIUtils.showToast(MessageActivity.this, "没有新消息！");
                            return;
                        }

                    }
                })
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("消息列表");
    }

    @Override
    protected void initView() {
        mMessagelist = new ArrayList<>();
        lvMessage = pullToRefreshLayout.getListView();
        showMessage();
        lvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mMessagelist.size()) {
                    return;
                } else {
                    if (mMessagelist.get(position).relatedStatus == 1) {

                        int newsType = mMessagelist.get(position).newsType;
                        Intent intent = new Intent(MessageActivity.this, AnswerDetailsActivity
                                .class);
                        if (newsType == 0) {
                            intent.putExtra("questionId", mMessagelist.get(position).relatedId);
                            intent.putExtra("answerId", mMessagelist.get(position).relatedId2);
                        } else if (newsType == 1) {
                            intent.putExtra("answerId", mMessagelist.get(position).relatedId);
                            intent.putExtra("questionId", mMessagelist.get(position).relatedId2);
                        } else if (newsType == 2) {
                            intent.putExtra("commentId", mMessagelist.get(position).relatedId);
                            intent.putExtra("answerId", mMessagelist.get(position).relatedId2);
                        } else if (newsType == 3) {
                            intent.putExtra("commentId", mMessagelist.get(position).relatedId);
                            intent.putExtra("answerId", mMessagelist.get(position).relatedId2);
                        }
                        newsId.clear();
                        newsId.add(mMessagelist.get(position).newsId);
                        loseMessage2(newsId);
                        startActivity(intent);
                    } else {
                        UIUtils.showToast(MessageActivity.this, mMessagelist.get(position)
                                .relatedErr + "");
                    }
                }
            }
        });
    }

    private void showMessage() {
        final MessageService service = new MessageService(MessageActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<MessageResponse>() {

            @Override
            public void onGetData(MessageResponse data) {
                mMessagelist.clear();
                mMessagelist.addAll(data.data.newsList);
                messageAdapter = new MessageAdapter();
                lvMessage.setAdapter(messageAdapter);
                pullToRefreshLayout.notifyData(data.data.nextNewsId,
                        data.data.newsList, false);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(MessageActivity.this, errorMsg);
            }
        });
        service.postLogined("", false);
    }

    private void upDataMessage(int nextNewsId) {
        final MessageService service = new MessageService(MessageActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<MessageResponse>() {

            @Override
            public void onGetData(MessageResponse data) {
                mMessagelist.addAll(data.data.newsList);
                pullToRefreshLayout.notifyData(data.data.nextNewsId,
                        data.data.newsList, true);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(MessageActivity.this, errorMsg);
            }
        });
        service.postLogined("nextNewsId=" + nextNewsId, false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_message;
    }

    class MessageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMessagelist.size();
        }

        @Override
        public MessageResponse.DataBean.NewsListBean getItem(int position) {
            return mMessagelist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MessageActivity.this,
                        R.layout.list_item_message
                        , null);
                holder = new ViewHolder();
                holder.ivHeader = (RoundImageView) convertView.findViewById(R.id.iv_header);
                holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
                holder.ivPraise = (ImageView) convertView.findViewById(R.id.iv_praise);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tvText = (TextView) convertView.findViewById(R.id.tv_text);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MessageResponse.DataBean.NewsListBean item = getItem(position);

            if (!TextUtils.isEmpty(item.portrait)) {
                Glide.with(MessageActivity.this).load(item.portrait).into(holder.ivHeader);
            }
            if (item.newsType == 2 || item.newsType == 3) {
                holder.tvText.setVisibility(View.VISIBLE);
                holder.tvText.setText(item.describe + "");
                if (item.commentRemoved == 1) {
                    holder.tvComment.setTextColor(getResources().getColor(R.color.colorBindGray));
                    //                    SpannableString msp = new SpannableString(item.comment
                    // + "");
                    //                    msp.setSpan(new StyleSpan(android.graphics.Typeface
                    // .ITALIC), 0, item.comment.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //斜体
                    holder.tvComment.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    holder.tvComment.setText(item.comment + "");
                } else {
                    holder.tvComment.setTextColor(getResources().getColor(R.color.colorDarkGray));
                    holder.tvComment.setText(item.comment + "");

                }
            } else {
                holder.tvText.setVisibility(View.GONE);
                holder.tvComment.setText(item.describe + "");
            }
            holder.tvUserName.setText(item.nickname + "");
            holder.tvTime.setText(item.newsTime + "");
            return convertView;
        }
    }

    static class ViewHolder {
        RoundImageView ivHeader;
        TextView tvTime;
        TextView tvUserName;
        TextView tvComment;
        TextView tvText;
        ImageView ivPraise;
    }

    private void loseMessage(List<Integer> newsId) {
        final LoseMessageService service = new LoseMessageService(MessageActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback() {

            @Override
            public void onGetData(Object data) {
                mMessagelist.clear();
                pullToRefreshLayout.notifyData(0,
                        null, false);
                messageAdapter.notifyDataSetChanged();
                UIUtils.showToast(MessageActivity.this, "已忽略所有新消息");
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(MessageActivity.this, errorMsg);
            }
        });
        service.postLogined("newsIdsJson=" + JSON.toJSONString(newsId), false);
    }

    private void loseMessage2(List<Integer> newsId) {
        final LoseMessageService service = new LoseMessageService(MessageActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback() {

            @Override
            public void onGetData(Object data) {
                showMessage();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(MessageActivity.this, errorMsg);
            }
        });
        service.postLogined("newsIdsJson=" + JSON.toJSONString(newsId), false);
    }
}
