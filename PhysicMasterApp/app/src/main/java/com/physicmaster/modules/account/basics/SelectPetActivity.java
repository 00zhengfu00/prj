package com.physicmaster.modules.account.basics;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.ShowPetResponse;
import com.physicmaster.net.service.user.ShowPetService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.view.jameson.library.CardScaleHelper;

import java.util.List;

public class SelectPetActivity extends BaseActivity {
    private Button btnPet;
    private RecyclerView mRecyclerView;
    private List<ShowPetResponse.DataBean.ShowPetListBean> showPetList;
    private CardScaleHelper mCardScaleHelper;

    @Override
    protected void findViewById() {
        btnPet = (Button) findViewById(R.id.btn_pet);
        mRecyclerView = (RecyclerView) findViewById(R.id.viewPager);
        showPet();
    }

    private void showPet() {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final ShowPetService service = new ShowPetService(SelectPetActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<ShowPetResponse>() {
            @Override
            public void onGetData(ShowPetResponse data) {
                UIUtils.showToast(SelectPetActivity.this, "获取成功");
                showPetList = data.data.showPetList;
                showPetCard(showPetList);
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(SelectPetActivity.this, errorMsg);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(() -> service.cancel());
        service.postLogined("", false);
    }

    private void showPetCard(final List<ShowPetResponse.DataBean.ShowPetListBean> showPetList) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (SelectPetActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        CardAdapterForGif cardAdapter = new CardAdapterForGif(SelectPetActivity.this, showPetList);
        mRecyclerView.setAdapter(cardAdapter);
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(0);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initView() {
        btnPet.setOnClickListener(view -> {
            if (showPetList == null || showPetList.size() == 0) {
                Toast.makeText(SelectPetActivity.this, "获取宠物失败，正在重试", Toast.LENGTH_SHORT).show();
                showPet();
            }
            int petId = showPetList.get(mCardScaleHelper.getCurrentItemPos()).petId;
            Intent intent = new Intent(SelectPetActivity.this, BasicsActivity.class);
            intent.putExtra("petId", petId);
            startActivity(intent);
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_pet;
    }
}
