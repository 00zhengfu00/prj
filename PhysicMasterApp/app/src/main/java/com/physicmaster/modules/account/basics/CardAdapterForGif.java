package com.physicmaster.modules.account.basics;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.net.response.user.ShowPetResponse;
import com.physicmaster.widget.gif.AlxGifHelper;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.view.jameson.library.CardAdapterHelper;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/10 15:18
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class CardAdapterForGif extends RecyclerView.Adapter<CardAdapterForGif.ViewHolder> implements View.OnClickListener {
    public List<ShowPetResponse.DataBean.ShowPetListBean> mList              = new ArrayList<>();
    public CardAdapterHelper                              mCardAdapterHelper = new CardAdapterHelper();
    private Context mContext;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public CardAdapterForGif(Context context, List<ShowPetResponse.DataBean.ShowPetListBean> mList) {
        this.mList = mList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.view_pet_card_item, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        itemView.setOnClickListener(this);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        // holder.mImageView.setImageResource(mList.get(position).petDesImg);
        if (!TextUtils.isEmpty(mList.get(position).petDesImg)) {
//            Glide.with(mContext).load(mList.get(position).petDesImg).asGif().into(holder.mImageView);
            View gifGroup = holder.itemView.findViewById(R.id.rl_gif);
            AlxGifHelper.displayImage(mList.get(position).petDesImg,
                    (GifImageView) gifGroup.findViewById(R.id.gif_photo_view),
                    (ProgressWheel) gifGroup.findViewById(R.id.progress_wheel),
                    (TextView) gifGroup.findViewById(R.id.tv_progress), gifGroup.getWidth()
            );
        }
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final GifImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (GifImageView) itemView.findViewById(R.id.gif_photo_view);
        }

    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view, (ShowPetResponse.DataBean.ShowPetListBean) view.getTag());
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ShowPetResponse.DataBean.ShowPetListBean data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


}
