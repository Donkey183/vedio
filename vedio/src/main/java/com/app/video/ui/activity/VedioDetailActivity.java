package com.app.video.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.video.R;
import com.app.video.config.Constants;
import com.app.video.config.VedioConstant;
import com.app.video.model.VideoDetailModel;
import com.app.video.ui.view.VideoDetailView;
import com.app.video.ui.widget.CommonAlert;

public class VedioDetailActivity extends MFBaseActivity implements INetFinish, View.OnClickListener {


    private VideoDetailModel mDetailModel;
    private VideoDetailView mDetailView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailModel = new VideoDetailModel(this);
        mDetailView = new VideoDetailView(this, this);
        getVedioDetailInfo();
    }

    private void getVedioDetailInfo() {
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.R_TYPE, VedioConstant.CHANNEL_NORMAL);
        mDetailModel.sendHttpRequest(request, VideoDetailModel.GET_VEDIO_DERAIL);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        mDetailView.showVideoDetail(mDetailModel.videoDetailData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 99:
                CommonAlert alert = new CommonAlert(this);
                alert.showAlert(Constants.config.getPay1(),Constants.config.getPay2(),Constants.config.getPay_img(), R.id.home_layout);
                break;
            default:
                break;
        }
    }

}
