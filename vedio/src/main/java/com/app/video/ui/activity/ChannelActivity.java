package com.app.video.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.video.model.ChannelContentModel;
import com.app.video.ui.view.ChannelActivityView;

public class ChannelActivity extends MFBaseActivity implements View.OnClickListener, INetFinish {

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
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

}
