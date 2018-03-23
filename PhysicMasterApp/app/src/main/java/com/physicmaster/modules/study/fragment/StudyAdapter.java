package com.physicmaster.modules.study.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.modules.explore.activity.MembersActivity;
import com.physicmaster.modules.study.activity.exercise.ExcerciseListActivity;
import com.physicmaster.modules.study.fragment.dialogfragment.CommonDialogFragment;
import com.physicmaster.modules.study.fragment.dialogfragment.SelectStudyDialogFragment;
import com.physicmaster.modules.videoplay.VideoPlayV2Activity;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse.DataBean.ChapterStudyBean.BaseInfo;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse.DataBean.ChapterStudyBean.DeepListBean;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse.DataBean.ChapterStudyBean.ExcerciseVideoBean;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse.DataBean.ChapterStudyBean.PreviewListBean;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse.DataBean.ChapterStudyBean.ReviewListBean;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;

/**
 * Created by huashigen on 2017-06-23.
 */

public class StudyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PreviewListBean> previewList;
    private List<DeepListBean> deepList;
    private List<ReviewListBean> reviewList;
    private SparseArrayCompat<String> titles;
    private int margin;
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_ITEM = 1;
    private String chapterId;
    private FragmentManager fragmentManager;

    public StudyAdapter(List<PreviewListBean> previewList, List<DeepListBean> deepList, List<ReviewListBean> reviewList, SparseArrayCompat<String> titles, FragmentManager
            fragmentManager) {
        this.previewList = previewList;
        this.deepList = deepList;
        this.reviewList = reviewList;
        this.titles = titles;
        this.titles = titles;
        this.fragmentManager = fragmentManager;
        int screenWidth = BaseApplication.getScreenWidth();
        margin = screenWidth / 4;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == TYPE_TITLE) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_title_item,
                    parent, false);
            TitleViewHolder titleViewHolder = new TitleViewHolder(itemView);
            return titleViewHolder;
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_item,
                    parent, false);
            StarViewHolder myViewHolder = new StarViewHolder(itemView);
            return myViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StarViewHolder) {
            StarViewHolder starViewHolder = (StarViewHolder) holder;
            final BaseInfo baseInfo = getItemBean(position);
            if (baseInfo.position == VideoBean.POSITION_LEFT) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                layoutParams.setMargins(margin, 0, 0, 0);
                starViewHolder.llContainer.setLayoutParams(layoutParams);
                starViewHolder.llContainer.setGravity(Gravity.LEFT);
            } else {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.setMargins(0, 0, margin, 0);
                starViewHolder.llContainer.setLayoutParams(layoutParams);
                starViewHolder.llContainer.setGravity(Gravity.RIGHT);
            }
            if (!TextUtils.isEmpty(baseInfo.title)) {
                starViewHolder.tvPoem.setText(baseInfo.title);
            } else {
                starViewHolder.tvPoem.setText(baseInfo.name);
            }
            if (1 == baseInfo.studyHere) {
                starViewHolder.ivStudyHere.setVisibility(View.VISIBLE);
            } else {
                starViewHolder.ivStudyHere.setVisibility(View.GONE);
            }
            if (BaseInfo.STAR_STATUS_LOCK == baseInfo.starStatus) {
                starViewHolder.ivStar.setImageResource(R.mipmap.star_lock);
            } else if (BaseInfo.STAR_STATUS_UNLIGHT == baseInfo.starStatus) {
                starViewHolder.ivStar.setImageResource(R.mipmap.star_unlock);
            } else if (BaseInfo.STAR_STATUS_LIGHT1 == baseInfo.starStatus) {
                starViewHolder.ivStar.setImageResource(R.mipmap.star_light1);
            } else if (BaseInfo.STAR_STATUS_LIGHT2 == baseInfo.starStatus) {
                starViewHolder.ivStar.setImageResource(R.mipmap.star_light2);
            }
            if (baseInfo instanceof PreviewListBean) {
                PreviewListBean previewBean = (PreviewListBean) baseInfo;
                if (previewBean.hasQu == 1) {
                    starViewHolder.llSmallStars.setVisibility(View.VISIBLE);
                    setStars(starViewHolder, previewBean.starLevel);
                } else {
                    starViewHolder.llSmallStars.setVisibility(View.GONE);
                }
            } else if (baseInfo instanceof DeepListBean) {
                DeepListBean deepBean = (DeepListBean) baseInfo;
                if (deepBean.hasQu == 1) {
                    starViewHolder.llSmallStars.setVisibility(View.VISIBLE);
                    setStars(starViewHolder, deepBean.starLevel);
                } else {
                    starViewHolder.llSmallStars.setVisibility(View.GONE);
                }
            } else {
                starViewHolder.llSmallStars.setVisibility(View.GONE);
            }
            starViewHolder.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final Context context = view.getContext();
                    if (0 == baseInfo.starStatus) {
                        CommonDialogFragment buyFragment = new CommonDialogFragment();
                        buyFragment.setOnActionBtnClickListener(new CommonDialogFragment.OnActionBtnClickListener() {
                            @Override
                            public void onLick() {
                                context.startActivity(new Intent(context, MembersActivity.class));
                            }
                        });
                        buyFragment.show(fragmentManager, "buyMember");
                    } else {
                        DialogFragment fragment = null;
                        Bundle bundle = new Bundle();
                        if (baseInfo instanceof PreviewListBean) {
                            PreviewListBean previewBean = (PreviewListBean) baseInfo;
                            //视频未播放
                            if (baseInfo.starStatus < 2) {
                                Intent intent = new Intent(view.getContext(), VideoPlayV2Activity.class);
                                intent.putExtra("videoId", previewBean.videoId + "");
                                intent.putExtra("chapterId", chapterId + "");
                                context.startActivity(intent);
                            }
                            //视频已播放
                            else {
                                //视频下存在习题
                                if (previewBean.hasQu > 0) {
                                    fragment = new SelectStudyDialogFragment();
                                    bundle.putString("title1", "导入课预习视频");
                                    bundle.putString("title2", "习题练习");
                                    bundle.putString("title", previewBean.name);
                                    bundle.putInt("videoId", previewBean.videoId);
                                    bundle.putString("chapterId", chapterId);
                                    fragment.setArguments(bundle);
                                    fragment.show(fragmentManager, "");
                                } else {
                                    //视频下不存在系统则播放视频
                                    Intent intent = new Intent(view.getContext(), VideoPlayV2Activity.class);
                                    intent.putExtra("videoId", previewBean.videoId + "");
                                    intent.putExtra("chapterId", chapterId + "");
                                    context.startActivity(intent);
                                }
                            }
                        } else if (baseInfo instanceof DeepListBean) {
                            DeepListBean deepBean = (DeepListBean) baseInfo;
                            if (baseInfo.starStatus < 2) {
                                Intent intent = new Intent(view.getContext(), VideoPlayV2Activity.class);
                                intent.putExtra("videoId", deepBean.videoId + "");
                                intent.putExtra("chapterId", chapterId + "");
                                context.startActivity(intent);
                            }
                            //视频已播放
                            else {
                                //视频下存在习题
                                if (deepBean.hasQu > 0) {
                                    fragment = new SelectStudyDialogFragment();
                                    bundle.putString("title1", "知识点精讲视频");
                                    bundle.putString("title2", "习题练习");
                                    bundle.putString("title", deepBean.name);
                                    bundle.putInt("videoId", deepBean.videoId);
                                    bundle.putString("chapterId", chapterId);
                                    fragment.setArguments(bundle);
                                    fragment.show(fragmentManager, "");
                                } else {
                                    //视频下不存在系统则播放视频
                                    Intent intent = new Intent(view.getContext(), VideoPlayV2Activity.class);
                                    intent.putExtra("videoId", deepBean.videoId + "");
                                    intent.putExtra("chapterId", chapterId + "");
                                    context.startActivity(intent);
                                }
                            }
                        } else {
                            ReviewListBean reviewBean = (ReviewListBean) baseInfo;
                            ArrayList<ExcerciseVideoBean> videoItemList = (ArrayList<ExcerciseVideoBean>) reviewBean.videoItemList;
                            Intent intent = new Intent(context, ExcerciseListActivity.class);
                            intent.putExtra("excerciseList", videoItemList);
                            intent.putExtra("chapterId", chapterId + "");
                            intent.putExtra("title", baseInfo.name);
                            context.startActivity(intent);
                        }
                    }
                }
            });
        } else {
            ((TitleViewHolder) holder).tvPoem.setText(getTitle(position));
        }
    }

    /**
     * 根据position获取标题
     *
     * @param position
     * @return
     */
    private String getTitle(int position) {
        return titles.get(position);
    }

    /**
     * 根据position获取item数据
     *
     * @param position
     * @return
     */
    private BaseInfo getItemBean(int position) {
        if (previewList != null && previewList.size() > 0) {
            if (position < previewList.size() + 1) {
                return previewList.get(position - 1);
            } else {
                if (deepList != null && deepList.size() > 0) {
                    if (position < previewList.size() + deepList.size() + 2) {
                        return deepList.get(position - previewList.size() - 2);
                    } else {
                        return reviewList.get(position - previewList.size() - deepList.size() - 3);
                    }
                } else {
                    if (reviewList != null && reviewList.size() > 0) {
                        return reviewList.get(position - previewList.size() - 2);
                    } else {
                        return null;
                    }
                }
            }
        } else {
            if (deepList != null && deepList.size() > 0) {
                if (position < deepList.size() + 1) {
                    return deepList.get(position - 1);
                } else {
                    return reviewList.get(position - deepList.size() - 2);
                }
            } else {
                if (reviewList != null && reviewList.size() > 0) {
                    return reviewList.get(position - 1);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (titles.get(position) != null) {
            return TYPE_TITLE;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (previewList != null) {
            size += previewList.size();
        }
        if (deepList != null) {
            size += deepList.size();
        }
        if (reviewList != null) {
            size += reviewList.size();
        }
        if (titles != null) {
            size += titles.size();
        }
        return size;
    }

    static class StarViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPoem;
        public LinearLayout llContainer, llSmallStars;
        public ImageView ivStar, ivStudyHere, ivSmallStar1, ivSmallStar2, ivSmallStar3;

        public StarViewHolder(View itemView) {
            super(itemView);
            tvPoem = (TextView) itemView.findViewById(R.id.tv_video_name);
            llContainer = (LinearLayout) itemView.findViewById(R.id.ll_container);
            llSmallStars = (LinearLayout) itemView.findViewById(R.id.ll_small_stars);
            ivStar = (ImageView) itemView.findViewById(R.id.iv_star);
            ivSmallStar1 = (ImageView) itemView.findViewById(R.id.iv_sstar1);
            ivSmallStar2 = (ImageView) itemView.findViewById(R.id.iv_sstar2);
            ivSmallStar3 = (ImageView) itemView.findViewById(R.id.iv_sstar3);
            ivStudyHere = (ImageView) itemView.findViewById(R.id.iv_study_position);
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPoem;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tvPoem = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    /**
     * 根据星星数目点亮星星
     *
     * @param starViewHolder
     * @param star
     */
    private void setStars(StarViewHolder starViewHolder, int star) {
        if (star >= 1) {
            starViewHolder.ivSmallStar1.setImageResource(R.mipmap.star_small_light);
            if (star >= 2) {
                starViewHolder.ivSmallStar2.setImageResource(R.mipmap.star_small_light);
                if (star >= 3) {
                    starViewHolder.ivSmallStar3.setImageResource(R.mipmap.star_small_light);
                } else {
                    starViewHolder.ivSmallStar3.setImageResource(R.mipmap.star_small_unlight);
                }
            } else {
                starViewHolder.ivSmallStar2.setImageResource(R.mipmap.star_small_unlight);
                starViewHolder.ivSmallStar3.setImageResource(R.mipmap.star_small_unlight);
            }
        } else {
            starViewHolder.ivSmallStar1.setImageResource(R.mipmap.star_small_unlight);
            starViewHolder.ivSmallStar2.setImageResource(R.mipmap.star_small_unlight);
            starViewHolder.ivSmallStar3.setImageResource(R.mipmap.star_small_unlight);
        }
    }

}
