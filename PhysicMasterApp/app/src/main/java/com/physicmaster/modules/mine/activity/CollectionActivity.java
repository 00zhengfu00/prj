package com.physicmaster.modules.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.modules.mine.activity.user.Book;
import com.physicmaster.modules.videoplay.VideoPlayLikeActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.excercise.GetCollectionResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.excercise.GetCollectionService;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.widget.MoreGridView;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

import java.util.List;

public class CollectionActivity extends BaseActivity {

    private static final int    TYPE_NORMAL = 0;
    private static final int    TYPE_TITLE  = 1;
    private static final String TAG         = "CollectionActivity";

    private ListView                                                        lvCollection;
    private List<GetCollectionResponse.DataBean.VideoListBean.ItemlistBean> mExercisesList;
    private List<GetCollectionResponse.DataBean.VideoListBean.ItemlistBean> mKnowledgeList;
    private RelativeLayout                                                  rlEmpty;
    private TextView                                                        tvArrow;
    private int                                                             subjectId;
    private int                                                             actionId;
    private String[]                                                        course;
    private int                                                             flag;
    private List<GetCollectionResponse.DataBean.VideoListBean.ItemlistBean> mDeepgeList;

    @Override
    protected void findViewById() {
        lvCollection = (ListView) findViewById(R.id.lv_collection);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_empty);
        // tvArrow = (TextView) findViewById(R.id.tv_arrow);
        initTitle();

        String subjectInfo = SpUtils.getString(this, CacheKeys.SUBJECT_STUDY_INFO, "");
        UserDataResponse.UserDataBean.LoginVoBean userData = BaseApplication.getUserData();
        String packageName = getPackageName();
        if (TextUtils.isEmpty(subjectInfo)) {
            if (null == userData) {
                if (Constant.PHYSICMASTER.equals(packageName)) {
                    subjectId = 1;
                } else if (Constant.CHYMISTMASTER.equals(packageName)) {
                    subjectId = 2;
                } else if (Constant.MATHMASTER.equals(packageName)) {
                    subjectId = 4;
                }
            } else {
                subjectId = getSubjectId(userData.bookId);
                if (subjectId == -1) {
                    if (Constant.PHYSICMASTER.equals(packageName)) {
                        subjectId = 1;
                    } else if (Constant.CHYMISTMASTER.equals(packageName)) {
                        subjectId = 2;
                    } else if (Constant.MATHMASTER.equals(packageName)) {
                        subjectId = 4;
                    }
                }
            }
        } else {
            if (1 == Integer.parseInt(subjectInfo)) {
                subjectId = 1;
            } else if (2 == Integer.parseInt(subjectInfo)) {
                subjectId = 2;
            } else if (4 == Integer.parseInt(subjectInfo)) {
                subjectId = 4;
            }
        }
    }

    /**
     * 根据bookId获取subjectId
     * @param bookId
     * @return
     */
    private int getSubjectId(int bookId) {
        String bookData = "[\n" +
                "    {\n" +
                "        \"books\": [\n" +
                "            {\n" +
                "                \"bookId\": 6,\n" +
                "                \"name\": \"七年级上册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 7,\n" +
                "                \"name\": \"七年级下册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 8,\n" +
                "                \"name\": \"八年级上册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 9,\n" +
                "                \"name\": \"八年级下册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 10,\n" +
                "                \"name\": \"九年级上册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 11,\n" +
                "                \"name\": \"九年级下册\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"id\": 4,\n" +
                "        \"name\": \"初中数学\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"books\": [\n" +
                "            {\n" +
                "                \"bookId\": 1,\n" +
                "                \"name\": \"八年级上册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 2,\n" +
                "                \"name\": \"八年级下册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 3,\n" +
                "                \"name\": \"九年级全册\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"初中物理\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"books\": [\n" +
                "            {\n" +
                "                \"bookId\": 4,\n" +
                "                \"name\": \"九年级上册\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"bookId\": 5,\n" +
                "                \"name\": \"九年级下册\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"id\": 2,\n" +
                "        \"name\": \"初中化学\"\n" +
                "    }\n" +
                "]";
        final List<Book> books = JSON.parseArray(bookData, Book.class);
        for (Book book : books) {
            for (Book.BooksBean booksBean : book.books) {
                if (booksBean.bookId == bookId) {
                    return book.id;
                }
            }
        }
        return -1;
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
                .setMiddleTitleText("我的收藏");
    }

    @Override
    protected void initView() {
        updateCollection(subjectId);
        //        final StartupResponse.DataBean dataBean = BaseApplication.getStartupDataBean();
        //        if (null != dataBean) {
        //            if (dataBean.subjectEduGradeList == null) {
        //                startActivity(new Intent(this, SplashActivity.class));
        //                this.finish();
        //                UIUtils.showToast(this, "数据异常");
        //                return;
        //            }
        //            final List<Item> list = new ArrayList<>();
        //            course = new String[dataBean.subjectEduGradeList.size()];
        //            for (int i = 0; i < dataBean.subjectEduGradeList.size(); i++) {
        //                Item item = new Item(dataBean.subjectEduGradeList.get(i).n);
        //                list.add(item);
        //                course[i] = dataBean.subjectEduGradeList.get(i).n;
        //            }
        //            tvArrow.setSelected(false);
        //            tvArrow.setOnClickListener(new View.OnClickListener() {
        //                @Override
        //                public void onClick(View v) {
        //                    tvArrow.setSelected(true);
        //                    new MyPopupWindow(CollectionActivity.this, new MyPopupWindow.OnSubmitListener() {
        //                        @Override
        //                        public void onSubmit(int position) {
        //                            int action = dataBean.subjectEduGradeList.get(position).i;
        //                            updateCollection(action);
        //                            tvArrow.setSelected(false);
        //                        }
        //                    }, new MyPopupWindow.OnDismissListener() {
        //                        @Override
        //                        public void onDismiss() {
        //                            tvArrow.setSelected(false);
        //                        }
        //                    }, list).showPopupWindow(tvArrow);
        //                }
        //            });
        //        } else {
        //            startActivity(new Intent(this, SplashActivity.class));
        //            this.finish();
        //            UIUtils.showToast(this, "数据异常");
        //        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        if (flag != 0) {
        //            if (1 == flag) {
        //                actionId = 0;
        //            } else if (2 == flag) {
        //                actionId = 1;
        //            } else if (4 == flag) {
        //                actionId = 2;
        //            }
        //            tvArrow.setText(course[actionId]);
        //            updateCollection(flag);
        //        } else {
        //            String subjectInfo = SpUtils.getString(CollectionActivity.this, CacheKeys.SUBJECT_STUDY_INFO, "");
        //            if (TextUtils.isEmpty(subjectInfo)) {
        //                String packageName = mContext.getPackageName();
        //                if (Constant.PHYSICMASTER.equals(packageName)) {
        //                    subjectId = 1;
        //                } else if (Constant.CHYMISTMASTER.equals(packageName)) {
        //                    subjectId = 2;
        //                } else if (Constant.MATHMASTER.equals(packageName)) {
        //                    subjectId = 4;
        //                }
        //            } else {
        //                subjectId = Integer.parseInt(subjectInfo);
        //            }
        //            if (1 == subjectId) {
        //                actionId = 0;
        //            } else if (2 == subjectId) {
        //                actionId = 1;
        //            } else if (4 == subjectId) {
        //                actionId = 2;
        //            }
        //            tvArrow.setText(course[actionId]);
        //            updateCollection(subjectId);
        //        }

    }


    private void updateCollection(final int subjectId) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final GetCollectionService service = new GetCollectionService(CollectionActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GetCollectionResponse>() {
            @Override
            public void onGetData(GetCollectionResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                loadingDialog.dismissDialog();
                //                if (1 == subjectId) {
                //                    actionId = 0;
                //                } else if (2 == subjectId) {
                //                    actionId = 1;
                //                } else if (4 == subjectId) {
                //                    actionId = 2;
                //                }
                //                flag = subjectId;
                //                tvArrow.setText(course[actionId]);
                mExercisesList = data.data.videoList.xtlist;
                mKnowledgeList = data.data.videoList.zsdlist;
                mDeepgeList = data.data.videoList.deepList;

                if ((null == mExercisesList || mExercisesList.size() == 0)
                        && (null == mKnowledgeList || mKnowledgeList.size() == 0)
                        && (null == mDeepgeList || mDeepgeList.size() == 0)) {
                    lvCollection.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);
                } else {
                    rlEmpty.setVisibility(View.GONE);
                    lvCollection.setVisibility(View.VISIBLE);
                    CollectionAdapter collectionAdapter = new CollectionAdapter();
                    lvCollection.setAdapter(collectionAdapter);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                Toast.makeText(CollectionActivity.this, errorMsg, Toast.LENGTH_SHORT)
                        .show();
                lvCollection.setVisibility(View.GONE);
                rlEmpty.setVisibility(View.VISIBLE);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("", false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_collection;
    }

    class CollectionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            //            int count = 0;
            //            if (mExercisesList != null && mExercisesList.size() > 0) {
            //                count += 2;
            //            }
            //            if (mKnowledgeList != null && mKnowledgeList.size() > 0) {
            //                count += 2;
            //            }
            return 6;  //3个标题栏和3个gridview
        }

        @Override
        public String getItem(int position) {
            return null;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == 2 || position == 4) {
                //标题栏
                return TYPE_TITLE;
            } else {
                return TYPE_NORMAL;

            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //根据当前布局类型,来加载不同布局
            int type = getItemViewType(position);

            switch (type) {
                case TYPE_NORMAL:
                    ViewHolder holder;
                    if (convertView == null) {
                        convertView = View.inflate(CollectionActivity.this,
                                R.layout.list_item_colleciotn, null);

                        holder = new ViewHolder();
                        holder.gvCollection = (MoreGridView) convertView
                                .findViewById(R.id.gv_collection);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    if (position == 1) {
                        holder.gvCollection.setAdapter(new CollectionItemAdapter(mKnowledgeList));
                    } else if (position == 3) {
                        holder.gvCollection.setAdapter(new CollectionItemAdapter(mDeepgeList));
                    } else if (position == 5) {
                        holder.gvCollection.setAdapter(new CollectionItemAdapter(mExercisesList));
                    }
                    break;

                case TYPE_TITLE:
                    //标题栏布局
                    HeaderViewHolder headerHolder;
                    if (convertView == null) {
                        convertView = View.inflate(CollectionActivity.this,
                                R.layout.grid_item_title, null);
                        headerHolder = new HeaderViewHolder();
                        headerHolder.tvTitle = (TextView) convertView
                                .findViewById(R.id.tv_title);

                        convertView.setTag(headerHolder);
                    } else {
                        headerHolder = (HeaderViewHolder) convertView.getTag();
                    }

                    if (mExercisesList.size() == 0 && mDeepgeList.size() == 0 && mKnowledgeList.size() != 0) {
                        if (position == 0) {
                            headerHolder.tvTitle.setText("知识点视频");
                        } else if (position == 2) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        } else if (position == 4) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        }
                    } else if (mExercisesList.size() == 0 && mKnowledgeList.size() == 0 && mDeepgeList.size() != 0) {
                        if (position == 0) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        } else if (position == 2) {
                            headerHolder.tvTitle.setText("精讲视频");
                        } else if (position == 4) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        }
                    } else if (mDeepgeList.size() == 0 && mKnowledgeList.size() == 0 && mExercisesList.size() != 0) {
                        if (position == 0) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        } else if (position == 2) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        } else if (position == 4) {
                            headerHolder.tvTitle.setText("习题讲解视频");
                        }
                    } else if (mDeepgeList.size() == 0 && mKnowledgeList.size() != 0 && mExercisesList.size() != 0) {
                        if (position == 0) {
                            headerHolder.tvTitle.setText("知识点视频");
                        } else if (position == 2) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        } else if (position == 4) {
                            headerHolder.tvTitle.setText("习题讲解视频");
                        }
                    } else if (mKnowledgeList.size() == 0 && mDeepgeList.size() != 0 && mExercisesList.size() != 0) {
                        if (position == 0) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        } else if (position == 2) {
                            headerHolder.tvTitle.setText("精讲视频");
                        } else if (position == 4) {
                            headerHolder.tvTitle.setText("习题讲解视频");
                        }
                    } else if (mExercisesList.size() == 0 && mKnowledgeList.size() != 0 && mDeepgeList.size() != 0) {
                        if (position == 0) {
                            headerHolder.tvTitle.setText("知识点视频");
                        } else if (position == 2) {
                            headerHolder.tvTitle.setText("精讲视频");
                        } else if (position == 4) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        }
                    } else if (mExercisesList.size() != 0 && mKnowledgeList.size() != 0 && mDeepgeList.size() != 0) {
                        if (position == 0) {
                            headerHolder.tvTitle.setText("知识点视频");
                        } else if (position == 2) {
                            headerHolder.tvTitle.setText("精讲视频");
                        } else if (position == 4) {
                            headerHolder.tvTitle.setText("习题讲解视频");
                        }
                    }


                    break;
            }
            return convertView;
        }
    }

    static class ViewHolder {
        public MoreGridView gvCollection;
    }

    static class HeaderViewHolder {
        public TextView tvTitle;
    }


    class CollectionItemAdapter extends BaseAdapter {

        private List<GetCollectionResponse.DataBean.VideoListBean.ItemlistBean> mlist;

        public CollectionItemAdapter(List<GetCollectionResponse.DataBean.VideoListBean
                .ItemlistBean> list) {
            mlist = list;
        }


        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public GetCollectionResponse.DataBean.VideoListBean.ItemlistBean getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ItemViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(CollectionActivity.this,
                        R.layout.grid_item_collection, null);
                holder = new ItemViewHolder();
                holder.ivCollection = (ImageView) convertView
                        .findViewById(R.id.iv_collection);
                holder.tvCollection = (TextView) convertView
                        .findViewById(R.id.tv_collection);

                convertView.setTag(holder);
            } else {
                holder = (ItemViewHolder) convertView.getTag();
            }
            final GetCollectionResponse.DataBean.VideoListBean.ItemlistBean item = getItem
                    (position);

            holder.tvCollection.setText(item.title + "");
            if (!TextUtils.isEmpty(item.posterurl + "")) {
                Glide.with(CollectionActivity.this).load(item.posterurl).into(holder.ivCollection);
            }
            holder.ivCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CollectionActivity.this, VideoPlayLikeActivity.class);
                    intent.putExtra("title", item.title);
                    intent.putExtra("videoId", item.videoId + "");
                    startActivity(intent);
                }
            });

            return convertView;
        }

    }

    static class ItemViewHolder {
        TextView  tvCollection;
        ImageView ivCollection;
    }

}
