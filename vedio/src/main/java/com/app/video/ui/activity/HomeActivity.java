package com.app.video.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.video.R;
import com.app.video.config.VedioConstant;
import com.app.video.model.HomeActivityModel;
import com.app.video.ui.view.HomeActivityView;


public class HomeActivity extends MFBaseActivity implements View.OnClickListener, INetFinish {


    private HomeActivityView mHomeView;
    private HomeActivityModel mHomeModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mHomeView = new HomeActivityView(this, this);
        mHomeModel = new HomeActivityModel(this);
        preLoadPageData();
    }

    private void preLoadPageData() {
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.TOKEN, "12345");
        mHomeModel.sendHttpRequest(request, HomeActivityModel.GET_PAGE_DATA);
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_home:
                mHomeView.clickHome();
                break;
            case R.id.main_vip:
                mHomeView.clickVip();
                break;
            case R.id.main_user:
                mHomeView.clickUser();
                break;
            case R.id.main_channel:
                mHomeView.clickChannel();
                break;
            case R.id.main_vault:
                mHomeView.clickVault();
                break;
            case R.id.main_forum:
                mHomeView.clickForum();
                break;
            default:
                break;
        }
    }

}
