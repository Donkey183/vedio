package com.app.video.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.video.R;
import com.app.video.config.Constants;
import com.app.video.data.ChannelContentData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.model.ChannelContentModel;
import com.app.video.ui.view.ChannelActivityView;
import com.app.video.ui.widget.CommonAlert;

public class ChannelActivity extends MFBaseActivity implements INetFinish, OnRecyclerViewItemClickListener {

    private ChannelActivityView mChannelView;
    private ChannelContentModel mChanneModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChannelView = new ChannelActivityView(this, this);
        mChanneModel = new ChannelContentModel(this);
        loadPageData();
    }

    private void loadPageData() {
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam("cid", "3");
        mChanneModel.sendHttpRequest(request, ChannelContentModel.GET_CHANNEL_CONTENT_INFO);
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        mChannelView.showPageView(mChanneModel.channelContentData.channelContentList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPageData();
    }

    @Override
    public void onItemClick(View view, int position, Object obj) {
        if (obj != null && obj instanceof ChannelContentData.ChannelContent) {
            ChannelContentData.ChannelContent content = (ChannelContentData.ChannelContent) obj;
            Intent intent = new Intent(ChannelActivity.this, VideoPlayerActivity.class);
            intent.putExtra("path", content.getCresource());
            startActivity(intent);
        }

    }


    public void onBtnClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn_next:
                if(!Constants.config.getVip_now().equals(Constants.RED)){
                    CommonAlert alert = new CommonAlert(this);
                    alert.showAlert(Constants.config.getPay1(),Constants.config.getPay2(),Constants.config.getPay_img(), -1);
                    break;
                }

        }
    }

}
