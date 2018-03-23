package com.physicmaster.modules.discuss.session.viewholder;

import android.widget.ImageView;

import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.session.emoji.StickerManager;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.physicmaster.R;
import com.physicmaster.modules.discuss.session.extension.StickerAttachment;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderSticker extends MsgViewHolderBase {

    private ImageView baseView;

    public MsgViewHolderSticker(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_sticker;
    }

    @Override
    protected void inflateContentView() {
        baseView = findViewById(R.id.message_item_sticker_image);
        baseView.setMaxWidth(MsgViewHolderThumbBase.getImageMaxEdge());
    }

    @Override
    protected void bindContentView() {
        StickerAttachment attachment = (StickerAttachment) message.getAttachment();
        if (attachment == null) {
            return;
        }

        ImageLoader.getInstance().displayImage(StickerManager.getInstance().getStickerBitmapUri(attachment.getCatalog
                (), attachment.getChartlet()), baseView, StickerManager.getInstance().getStickerImageOptions
                (ScreenUtil.dip2px(R.dimen.mask_sticker_bubble_width)));
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
