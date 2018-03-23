package com.physicmaster.modules.mine.activity.question;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.RoundImageView;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;

public class CommentActivity extends BaseActivity {


    private ArrayList<String> mCommentList;
    private ListView          lvComment;
    private TextView          tvSend;
    private EditText          etComment;

    @Override
    protected void findViewById() {

        lvComment = (ListView) findViewById(R.id.lv_comment);
        etComment = (EditText) findViewById(R.id.et_comment);
        tvSend = (TextView) findViewById(R.id.tv_send);
        initTitle();
    }

    private void initTitle() {
        new TitleBuilder(this)
                .setLeftImageRes(R.mipmap.fanhui)
                .setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("评论");
    }

    @Override
    protected void initView() {
        mCommentList = new ArrayList<>();
        mCommentList.add("大法师");
        mCommentList.add("撒旦");
        mCommentList.add("使馆");
        mCommentList.add("武器大师");
        mCommentList.add("阿萨德");
        mCommentList.add("尼古拉斯.赵四");
        mCommentList.add("赵云");
        mCommentList.add("武器大师");
        mCommentList.add("赵云");

        View view = LayoutInflater.from(this).inflate(R.layout.comment_head, null);
        TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
        tvNumber.setText("最新评论 ("+mCommentList.size()+"条)");
        lvComment.addHeaderView(view);

        CommentAdapter commentAdapter = new CommentAdapter();
        lvComment.setAdapter(commentAdapter);
        lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UIUtils.showToast(CommentActivity.this,position+"...");
                if (position == 2 || position == 3) {
                    etComment.setHint("评论" + mCommentList.get(position));
                } else {
                    etComment.setHint("回复" + mCommentList.get(position));
                }
                showInputSoft(etComment);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_comment;
    }

    class CommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCommentList.size();
        }

        @Override
        public String getItem(int position) {
            return mCommentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.list_item_comment
                        , null);
                holder = new ViewHolder();
                holder.ivHeader = (RoundImageView) convertView.findViewById(R.id.iv_header);
                holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
                holder.ivPraise = (ImageView) convertView.findViewById(R.id.iv_praise);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv = (TextView) convertView.findViewById(R.id.tv);
                holder.tvUserName2 = (TextView) convertView.findViewById(R.id.tv_user_name2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String item = getItem(position);

            if (position == 2 || position == 3) {
                holder.tv.setVisibility(View.VISIBLE);
                holder.tvUserName2.setVisibility(View.VISIBLE);
            } else {
                holder.tvUserName2.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
            }
            holder.tvUserName.setText(item);
            holder.tvUserName2.setText(item + "666");
            return convertView;
        }
    }

    static class ViewHolder {
        RoundImageView ivHeader;
        TextView       tvTime;
        TextView       tvUserName;
        TextView       tvUserName2;
        TextView       tv;
        TextView       tvComment;
        ImageView      ivPraise;
    }
}
