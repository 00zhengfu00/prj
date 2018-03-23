/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.lswuyou.tv.pm.leanback;

import android.graphics.Color;
import android.support.v17.leanback.widget.Presenter;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.homepage.ChapterListResponse.DataBean.BookListBean.ItemListBean;
import com.lswuyou.tv.pm.utils.AnimationUtil;

/*
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an Image CardView
 */
public class CardPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    private static int IMAGE_WIDTH = 313;
    private static int IMAGE_HEIGHT = 176;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private static int mainAreaBackgroundColor;
    private static int cursorRes = R.drawable.cursor_1920;
    private static int cursorResTransParent = R.drawable.cursor_1920_transparent;


    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        if (selected) {
            view.setBackgroundResource(cursorRes);
        } else {
            view.setBackgroundResource(cursorResTransParent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        parent.setClipChildren(false);
        parent.setClipToPadding(false);
        sDefaultBackgroundColor = parent.getResources().getColor(R.color.transparent);
        mainAreaBackgroundColor = parent.getResources().getColor(R.color.half_transparent_40);
        sSelectedBackgroundColor = parent.getResources().getColor(R.color.selected_background);
        IMAGE_WIDTH = parent.getResources().getDimensionPixelSize(R.dimen.x544);
        IMAGE_HEIGHT = parent.getResources().getDimensionPixelSize(R.dimen.y306);
        ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        cardView.setInfoVisibility(View.VISIBLE);
        cardView.setBackgroundColor(Color.TRANSPARENT);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        final ItemListBean movie = (ItemListBean) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        if (movie.poster != null) {
            cardView.setTitleText(movie.title);
            cardView.setInfoAreaBackgroundColor(mainAreaBackgroundColor);
            cardView.setMainImageDimensions(IMAGE_WIDTH, IMAGE_HEIGHT);
            cardView.setContentViewVisible(View.GONE);
            cardView.setCardType(BaseCardView.CARD_TYPE_INFO_OVER);
            Glide.with(viewHolder.view.getContext())
                    .load(movie.poster)
                    .centerCrop()
                    .error(R.mipmap.wulidashi)
                    .placeholder(R.mipmap.loading)
                    .into(cardView.getMainImageView());
            cardView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        return false;
                    }
                    int postitionType = movie.postitionType;
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        if (postitionType == ItemListBean.LASTBOTTEM_ITEM || postitionType == ItemListBean.LAST_ITEM) {
                            AnimationUtil.leftRightShake(v);
                            return true;
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        if (postitionType == ItemListBean.FIRSTBOTTEM_ITEM || postitionType == ItemListBean.FIRST_ITEM) {
                            AnimationUtil.leftRightShake(v);
                            return true;
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if (postitionType == ItemListBean.BOTTOM_ITEM || postitionType == ItemListBean.FIRSTBOTTEM_ITEM || postitionType == ItemListBean.LASTBOTTEM_ITEM) {
                            AnimationUtil.upDownShake(v);
                            return true;
                        }
                    }
                    return false;
                }
            });
            cardView.setClipChildren(false);
            cardView.setClipToPadding(false);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
